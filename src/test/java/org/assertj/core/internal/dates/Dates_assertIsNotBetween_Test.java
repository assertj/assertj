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
package org.assertj.core.internal.dates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotBeBetween.shouldNotBeBetween;
import static org.assertj.core.internal.ErrorMessages.endDateToCompareActualWithIsNull;
import static org.assertj.core.internal.ErrorMessages.startDateToCompareActualWithIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.DatesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Dates#assertIsNotBetween(AssertionInfo, Date, Date, Date, boolean, boolean)}</code>.
 * 
 * @author Joel Costigliola
 */
class Dates_assertIsNotBetween_Test extends DatesBaseTest {

  @Override
  protected void initActualDate() {
    actual = parseDate("2011-09-27");
  }

  @Test
  void should_fail_if_actual_is_between_given_period() {
    AssertionInfo info = someInfo();
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = true;
    boolean inclusiveEnd = true;

    Throwable error = catchThrowable(() -> dates.assertIsNotBetween(info, actual, start, end, inclusiveStart, inclusiveEnd));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeBetween(actual, start, end, inclusiveStart, inclusiveEnd));
  }

  @Test
  void should_fail_if_actual_is_equals_to_start_of_given_period_and_start_is_included_in_given_period() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = true;
    boolean inclusiveEnd = false;

    Throwable error = catchThrowable(() -> dates.assertIsNotBetween(info, actual, start, end, inclusiveStart, inclusiveEnd));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeBetween(actual, start, end, inclusiveStart, inclusiveEnd));
  }

  @Test
  void should_fail_if_actual_is_equals_to_end_of_given_period_and_end_is_included_in_given_period() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-09-30");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = false;
    boolean inclusiveEnd = true;

    Throwable error = catchThrowable(() -> dates.assertIsNotBetween(info, actual, start, end, inclusiveStart, inclusiveEnd));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeBetween(actual, start, end, inclusiveStart, inclusiveEnd));
  }

  @Test
  void should_throw_error_if_start_date_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      Date end = parseDate("2011-09-30");
      dates.assertIsNotBetween(someInfo(), actual, null, end, true, true);
    }).withMessage(startDateToCompareActualWithIsNull());
  }

  @Test
  void should_throw_error_if_end_date_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      Date start = parseDate("2011-09-01");
      dates.assertIsNotBetween(someInfo(), actual, start, null, true, true);
    }).withMessage(endDateToCompareActualWithIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Date start = parseDate("2011-09-01");
      Date end = parseDate("2011-09-30");
      dates.assertIsNotBetween(someInfo(), null, start, end, true, true);
    }).withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_not_between_given_period() {
    actual = parseDate("2011-12-31");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsNotBetween(someInfo(), actual, start, end, true, true);
  }

  @Test
  void should_pass_if_actual_is_equals_to_start_of_given_period_and_start_is_not_included_in_given_period() {
    actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsNotBetween(someInfo(), actual, start, end, false, false);
    dates.assertIsNotBetween(someInfo(), actual, start, end, false, true);
  }

  @Test
  void should_pass_if_actual_is_equals_to_end_of_given_period_and_end_is_not_included_in_given_period() {
    actual = parseDate("2011-09-30");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    dates.assertIsNotBetween(someInfo(), actual, start, end, false, false);
    dates.assertIsNotBetween(someInfo(), actual, start, end, true, false);
  }

  @Test
  void should_fail_if_actual_is_between_given_period_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Date start = parseDate("2011-08-31");
    Date end = parseDate("2011-09-30");
    boolean inclusiveStart = true;
    boolean inclusiveEnd = true;

    Throwable error = catchThrowable(() -> datesWithCustomComparisonStrategy.assertIsNotBetween(info, actual, start, end,
                                                                                                inclusiveStart, inclusiveEnd));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldNotBeBetween(actual, start, end, inclusiveStart, inclusiveEnd,
                                                yearAndMonthComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_is_equals_to_start_of_given_period_and_start_is_included_in_given_period_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-09-15");
    Date start = parseDate("2011-09-01"); // = 2011-09-15 according to comparison strategy
    Date end = parseDate("2011-10-01");
    boolean inclusiveStart = true;
    boolean inclusiveEnd = false;

    Throwable error = catchThrowable(() -> datesWithCustomComparisonStrategy.assertIsNotBetween(info, actual, start, end,
                                                                                                inclusiveStart, inclusiveEnd));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldNotBeBetween(actual, start, end, inclusiveStart, inclusiveEnd,
                                                yearAndMonthComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_is_equals_to_end_of_given_period_and_end_is_included_in_given_period_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = parseDate("2011-09-15");
    Date start = parseDate("2011-08-31");
    Date end = parseDate("2011-09-30"); // = 2011-09-15 according to comparison strategy
    boolean inclusiveStart = false;
    boolean inclusiveEnd = true;

    Throwable error = catchThrowable(() -> datesWithCustomComparisonStrategy.assertIsNotBetween(info, actual, start, end,
                                                                                                inclusiveStart, inclusiveEnd));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldNotBeBetween(actual, start, end, inclusiveStart, inclusiveEnd,
                                                yearAndMonthComparisonStrategy));
  }

  @Test
  void should_throw_error_if_start_date_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> {
      Date end = parseDate("2011-09-30");
      datesWithCustomComparisonStrategy.assertIsNotBetween(someInfo(), actual, null, end, true, true);
    }).withMessage(startDateToCompareActualWithIsNull());
  }

  @Test
  void should_throw_error_if_end_date_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> {
      Date start = parseDate("2011-09-01");
      datesWithCustomComparisonStrategy.assertIsNotBetween(someInfo(), actual, start, null, true, true);
    }).withMessage(endDateToCompareActualWithIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Date start = parseDate("2011-09-01");
      Date end = parseDate("2011-09-30");
      datesWithCustomComparisonStrategy.assertIsNotBetween(someInfo(), null, start, end, true, true);
    }).withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_not_between_given_period_according_to_custom_comparison_strategy() {
    actual = parseDate("2011-12-31");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-11-30");
    datesWithCustomComparisonStrategy.assertIsNotBetween(someInfo(), actual, start, end, true, true);
  }

  @Test
  void should_pass_if_actual_is_equals_to_start_of_given_period_and_start_is_not_included_in_given_period_according_to_custom_comparison_strategy() {
    actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-15"); // = 2011-09-01 according to comparison strategy
    Date end = parseDate("2011-09-30");
    datesWithCustomComparisonStrategy.assertIsNotBetween(someInfo(), actual, start, end, false, false);
    datesWithCustomComparisonStrategy.assertIsNotBetween(someInfo(), actual, start, end, false, true);
  }

  @Test
  void should_pass_if_actual_is_equals_to_end_of_given_period_and_end_is_not_included_in_given_period_according_to_custom_comparison_strategy() {
    actual = parseDate("2011-09-30");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-15"); // = 2011-09-30 according to comparison strategy
    datesWithCustomComparisonStrategy.assertIsNotBetween(someInfo(), actual, start, end, false, false);
    datesWithCustomComparisonStrategy.assertIsNotBetween(someInfo(), actual, start, end, true, false);
  }

}
