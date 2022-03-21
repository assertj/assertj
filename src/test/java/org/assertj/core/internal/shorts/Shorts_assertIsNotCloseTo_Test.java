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
package org.assertj.core.internal.shorts;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ShortsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Shorts_assertIsNotCloseTo_Test extends ShortsBaseTest {

  private static final Short ZERO = 0;
  private static final Short ONE = 1;

  @ParameterizedTest
  @CsvSource({
      "1, 3, 1",
      "-1, -3, 1",
      "1, -2, 2",
      "-1, 2, 2"
  })
  void should_pass_if_difference_is_greater_than_offset(short actual, short other, short offset) {
    shorts.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset));
    shorts.assertIsNotCloseTo(someInfo(), actual, other, within(offset));
  }

  @ParameterizedTest
  @CsvSource({
      "1, 0, 1",
      "1, 2, 1",
      "-1, 0, 1",
      "1, -1, 2",
      "-1, 1, 2"
  })
  void should_pass_if_difference_is_equal_to_strict_offset(short actual, short other, short offset) {
    shorts.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset));
  }

  @ParameterizedTest
  @CsvSource({
      "1, 2, 10",
      "1, 2, 2",
      "1, 0, 2",
      "0, 1, 2"
  })
  void should_fail_if_actual_is_too_close_to_the_other_value(short actual, short other, short offset) {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> shorts.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(actual, other, byLessThan(offset), (short) abs(actual - other)));
  }

  @ParameterizedTest
  @CsvSource({
      "1, 2, 10",
      "1, 0, 2",
      "0, 1, 2"
  })
  void should_fail_if_actual_is_too_close_to_the_other_value_with_strict_offset(short actual, short other,
                                                                                short offset) {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> shorts.assertIsNotCloseTo(info, actual, other, byLessThan(offset)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(actual, other, byLessThan(offset), (short) abs(actual - other)));
  }

  @ParameterizedTest
  @CsvSource({
      "1, 1, 0",
      "1, 0, 1",
      "1, 2, 1"
  })
  void should_fail_if_difference_is_equal_to_given_offset(short actual, short other, short offset) {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> shorts.assertIsNotCloseTo(someInfo(), actual, other, within(offset)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(actual, other, within(offset), (short) abs(actual - other)));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> shorts.assertIsNotCloseTo(someInfo(), null, ONE,
                                                                                               byLessThan(ONE)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> shorts.assertIsNotCloseTo(someInfo(), ONE, null, byLessThan(ONE)));
  }

  @Test
  void should_fail_if_offset_is_null() {
    assertThatNullPointerException().isThrownBy(() -> shorts.assertIsNotCloseTo(someInfo(), ONE, ZERO, null));
  }

}
