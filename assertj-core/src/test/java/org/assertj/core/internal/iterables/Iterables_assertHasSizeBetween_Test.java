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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldHaveSizeBetween.shouldHaveSizeBetween;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

class Iterables_assertHasSizeBetween_Test extends IterablesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertHasSizeBetween(someInfo(), null, 0, 6))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_illegal_argument_exception_if_lower_boundary_is_greater_than_higher_boundary() {
    assertThatIllegalArgumentException().isThrownBy(() -> iterables.assertHasSizeBetween(someInfo(), actual, 4, 2))
                                        .withMessage("The higher boundary <2> must be greater than the lower boundary <4>.");
  }

  @Test
  void should_fail_if_size_of_actual_is_not_greater_than_or_equal_to_lower_boundary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertHasSizeBetween(someInfo(), actual, 4, 6))
                                                   .withMessage(shouldHaveSizeBetween(actual, actual.size(), 4, 6).create());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_less_than_higher_boundary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertHasSizeBetween(someInfo(), actual, 1, 2))
                                                   .withMessage(shouldHaveSizeBetween(actual, actual.size(), 1, 2).create());
  }

  @Test
  void should_pass_if_size_of_actual_is_between_boundaries() {
    iterables.assertHasSizeBetween(someInfo(), actual, 1, 6);
    iterables.assertHasSizeBetween(someInfo(), actual, actual.size(), actual.size());
  }
}
