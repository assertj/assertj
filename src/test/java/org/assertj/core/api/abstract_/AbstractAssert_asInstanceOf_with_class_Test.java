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
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ConcreteAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for <code>{@link AbstractAssert#asInstanceOf(Class)}</code>.
 */
@ExtendWith(MockitoExtension.class)
class AbstractAssert_asInstanceOf_with_class_Test {

  private AbstractAssert<?, ?> underTest;

  private final Object actual = 6;

  @BeforeEach
  void setUp() {
    underTest = new ConcreteAssert(actual);
  }

  @Test
  void should_return_a_type_updated_object_assert_with_same_actual() {
    // WHEN
    AbstractObjectAssert<?, Integer> result = underTest.asInstanceOf(Integer.class);
    // THEN
    assertThat(result).extracting("actual").isEqualTo(actual);
  }

  @Test
  void should_fail_if_actual_is_not_an_instance_of_the_input_type() {
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.asInstanceOf(String.class));
    // THEN
    assertThat(thrown).isInstanceOf(AssertionError.class)
                      .hasMessage(shouldBeInstance(actual, String.class).create());
  }

}
