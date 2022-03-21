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
package org.assertj.core.internal.longs;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.error.ShouldNotBeEqualWithinPercentage.shouldNotBeEqualWithinPercentage;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Longs_assertIsNotCloseToPercentage_Test extends LongsBaseTest {

  private static final Long ZERO = 0L;
  private static final Long ONE = 1L;
  private static final Long TEN = 10L;
  private static final Long ONE_HUNDRED = 100L;

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertIsNotCloseToPercentage(someInfo(), null, ONE,
                                                                                                        withPercentage(ONE)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> longs.assertIsNotCloseToPercentage(someInfo(), ONE, null,
                                                                                         withPercentage(ONE)));
  }

  @Test
  void should_fail_if_percentage_is_null() {
    assertThatNullPointerException().isThrownBy(() -> longs.assertIsNotCloseToPercentage(someInfo(), ONE, ZERO, null));
  }

  @Test
  void should_fail_if_percentage_is_negative() {
    assertThatIllegalArgumentException().isThrownBy(() -> longs.assertIsNotCloseToPercentage(someInfo(), ONE, ZERO,
                                                                                             withPercentage(-1L)));
  }

  @ParameterizedTest
  @CsvSource({
      "1, 2, 1",
      "1, 11, 90",
      "-1, -2, 1",
      "-1, -11, 90",
      "0, -1, 99"
  })
  void should_pass_if_difference_is_greater_than_given_percentage(Long actual, Long other, Long percentage) {
    longs.assertIsNotCloseToPercentage(someInfo(), actual, other, withPercentage(percentage));
  }

  @ParameterizedTest
  @CsvSource({
      "1, 1, 0",
      "2, 1, 100",
      "1, 2, 50",
      "-1, -1, 0",
      "-2, -1, 100",
      "-1, -2, 50"
  })
  void should_fail_if_difference_is_equal_to_given_percentage(Long actual, Long other, Long percentage) {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> longs.assertIsNotCloseToPercentage(someInfo(), actual, other,
                                                                              withPercentage(percentage)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqualWithinPercentage(actual, other, withPercentage(percentage),
                                                                    abs(actual - other)));
  }

  @Test
  void should_fail_if_actual_is_too_close_to_expected_value() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> longs.assertIsNotCloseToPercentage(someInfo(), ONE, TEN, withPercentage(ONE_HUNDRED)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqualWithinPercentage(ONE, TEN, withinPercentage(100),
                                                                    TEN - ONE));
  }
}
