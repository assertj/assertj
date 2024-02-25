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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.mockito.BDDMockito.willReturn;

import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Stefano Cordio
 */
@ExtendWith(MockitoExtension.class)
class InstanceOfAssertFactoryTest {

  @Mock
  private AbstractAssert<?, ?> abstractAssert;

  @Nested
  class With_Class {

    private InstanceOfAssertFactory<Integer, AbstractAssert<?, ?>> underTest;

    @Mock
    private AssertFactory<Integer, AbstractAssert<?, ?>> assertFactory;

    @BeforeEach
    void setUp() {
      underTest = new InstanceOfAssertFactory<>(Integer.class, assertFactory);
    }

    @Test
    void constructor_should_fail_if_no_type_is_given() {
      // WHEN
      Throwable thrown = catchThrowable(() -> new InstanceOfAssertFactory<>(null, assertFactory));
      // THEN
      then(thrown).isInstanceOf(NullPointerException.class)
                  .hasMessage(shouldNotBeNull("type").create());
    }

    @Test
    void constructor_should_fail_if_no_assert_factory_is_given() {
      // WHEN
      Throwable thrown = catchThrowable(() -> new InstanceOfAssertFactory<>(Object.class, null));
      // THEN
      then(thrown).isInstanceOf(NullPointerException.class)
                  .hasMessage(shouldNotBeNull("delegate").create());
    }

    @Test
    void getType_should_return_given_class() {
      // WHEN
      Type result = this.underTest.getType();
      // THEN
      then(result).isEqualTo(Integer.class);
    }

    @Test
    void getRawClass_should_return_given_class() {
      // GIVEN
      InstanceOfAssertFactory<Integer, AbstractAssert<?, ?>> underTest = new InstanceOfAssertFactory<>(Integer.class,
                                                                                                       assertFactory);
      // WHEN
      Class<Integer> result = underTest.getRawClass();
      // THEN
      then(result).isEqualTo(Integer.class);
    }

    @Test
    void createAssert_should_return_assert_factory_result_if_actual_is_an_instance_of_given_type() {
      // GIVEN
      int value = 0;
      willReturn(abstractAssert).given(assertFactory).createAssert(value);
      // WHEN
      Assert<?, ?> result = underTest.createAssert(value);
      // THEN
      then(result).isSameAs(abstractAssert);
    }

    @Test
    void createAssert_should_throw_assertion_error_if_actual_is_not_an_instance_of_given_type() {
      // GIVEN
      String value = "string";
      // WHEN
      Throwable throwable = catchThrowable(() -> underTest.createAssert(value));
      // THEN
      then(throwable).isInstanceOf(ClassCastException.class)
                     .hasMessage("Cannot cast %s to %s", value.getClass().getName(), underTest.getRawClass().getName());
    }

    @Test
    void toString_should_return_expected_value() {
      // WHEN
      String result = underTest.toString();
      // THEN
      then(result).isEqualTo("InstanceOfAssertFactory for %s", Integer.class.getTypeName());
    }

  }

  @Nested
  class With_Class_and_Type_array {

  }

}
