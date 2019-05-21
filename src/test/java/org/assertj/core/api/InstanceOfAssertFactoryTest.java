/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.BDDMockito.willReturn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Stefano Cordio
 */
@ExtendWith(MockitoExtension.class)
class InstanceOfAssertFactoryTest {

  private InstanceOfAssertFactory<Integer, Assert<?, ?>> underTest;

  @Mock
  private AssertFactory<Integer, Assert<?, ?>> mockAssertFactory;

  @Mock
  private Assert<?, ?> mockAssert;

  @BeforeEach
  void setUp() {
    underTest = new InstanceOfAssertFactory<>(Integer.class, mockAssertFactory);
  }

  @Test
  void should_throw_npe_if_no_type_is_given() {
    // WHEN
    Throwable thrown = catchThrowable(() -> new InstanceOfAssertFactory<>(null, mockAssertFactory));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("type").create());
  }

  @Test
  void should_throw_npe_if_no_assert_factory_is_given() {
    // WHEN
    Throwable thrown = catchThrowable(() -> new InstanceOfAssertFactory<>(Object.class, null));

    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("assertFactory").create());
  }

  @Test
  void should_return_assert_factory_result_if_actual_is_an_instance_of_given_type() {
    // GIVEN
    int value = 0;
    willReturn(mockAssert).given(mockAssertFactory).createAssert(value);
    // WHEN
    Assert<?, ?> result = underTest.createAssert(value);
    // THEN
    then(result).isSameAs(mockAssert);
  }

  @Test
  void should_throw_assertion_error_if_actual_is_not_an_instance_of_given_type() {
    // GIVEN
    String value = "string";
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.createAssert(value));
    // THEN
    then(error).hasMessage(shouldBeInstance("string", Integer.class).create());
  }

}
