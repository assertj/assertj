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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.integers;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.IntegersBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.error.ShouldNotBeEqualWithinPercentage.shouldNotBeEqualWithinPercentage;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

public class Integers_assertIsNotCloseToPercentage_Test extends IntegersBaseTest {

  private static final Integer ZERO = 0;
  private static final Integer ONE = 1;
  private static final Integer TEN = 10;
  private static final Integer ONE_HUNDRED = 100;

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> integers.assertIsNotCloseToPercentage(someInfo(), null, ONE, withPercentage(ONE)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> integers.assertIsNotCloseToPercentage(someInfo(), ONE, null, withPercentage(ONE)));
  }

  @Test
  public void should_fail_if_percentage_is_null() {
    assertThatNullPointerException().isThrownBy(() -> integers.assertIsNotCloseToPercentage(someInfo(), ONE, ZERO, null));
  }

  @Test
  public void should_fail_if_percentage_is_negative() {
    assertThatIllegalArgumentException().isThrownBy(() -> integers.assertIsNotCloseToPercentage(someInfo(), ONE, ZERO, withPercentage(-1)));
  }

  @ParameterizedTest
  @CsvSource({
    "1, 2, 1",
    "1, 11, 90",
    "-1, -2, 1",
    "-1, -11, 90",
    "0, -1, 99"
  })
  public void should_pass_if_difference_is_greater_than_given_percentage(Integer actual, Integer other, Integer percentage) {
    integers.assertIsNotCloseToPercentage(someInfo(), actual, other, withPercentage(percentage));
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
  public void should_fail_if_difference_is_equal_to_given_percentage(Integer actual, Integer other,
                                                                     Integer percentage) {
    AssertionInfo info = someInfo();
    try {
      integers.assertIsNotCloseToPercentage(someInfo(), actual, other, withPercentage(percentage));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqualWithinPercentage(actual, other, withPercentage(percentage),
                                                                      abs(actual - other)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_too_close_to_expected_value() {
    AssertionInfo info = someInfo();
    try {
      integers.assertIsNotCloseToPercentage(someInfo(), ONE, TEN, withPercentage(ONE_HUNDRED));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqualWithinPercentage(ONE, TEN, withinPercentage(100),
        TEN - ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
