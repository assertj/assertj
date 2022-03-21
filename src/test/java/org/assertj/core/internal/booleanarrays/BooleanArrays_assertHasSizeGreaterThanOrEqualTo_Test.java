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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.booleanarrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveSizeGreaterThanOrEqualTo.shouldHaveSizeGreaterThanOrEqualTo;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.BooleanArraysBaseTest;
import org.junit.jupiter.api.Test;

class BooleanArrays_assertHasSizeGreaterThanOrEqualTo_Test extends BooleanArraysBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSizeGreaterThanOrEqualTo(someInfo(), null,
                                                                                                              6))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_greater_than_or_equal_to_boundary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSizeGreaterThanOrEqualTo(someInfo(), actual,
                                                                                                              6))
                                                   .withMessage(shouldHaveSizeGreaterThanOrEqualTo(actual, actual.length,
                                                                                                   6).create());
  }

  @Test
  void should_pass_if_size_of_actual_is_greater_than_boundary() {
    arrays.assertHasSizeGreaterThanOrEqualTo(someInfo(), actual, 1);
  }

  @Test
  void should_pass_if_size_of_actual_is_equal_to_boundary() {
    arrays.assertHasSizeGreaterThanOrEqualTo(someInfo(), actual, actual.length);
  }
}
