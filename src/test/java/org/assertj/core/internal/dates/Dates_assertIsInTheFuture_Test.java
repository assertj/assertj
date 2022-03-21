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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeInTheFuture.shouldBeInTheFuture;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.DateUtil.monthOf;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Calendar;
import java.util.Date;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.DatesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Dates#assertIsInTheFuture(AssertionInfo, Date)}</code>.
 * 
 * @author Joel Costigliola
 */
class Dates_assertIsInTheFuture_Test extends DatesBaseTest {

  @Test
  void should_fail_if_actual_is_not_in_the_future() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> dates.assertIsInTheFuture(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeInTheFuture(actual));
  }

  @Test
  void should_fail_if_actual_is_today() {
    AssertionInfo info = someInfo();
    actual = new Date();

    Throwable error = catchThrowable(() -> dates.assertIsInTheFuture(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeInTheFuture(actual));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> dates.assertIsInTheFuture(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_in_the_future() {
    actual = parseDate("2111-01-01");
    dates.assertIsInTheFuture(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_not_in_the_future_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> datesWithCustomComparisonStrategy.assertIsInTheFuture(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeInTheFuture(actual, yearAndMonthComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_is_today_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    // we want actual to be different from today but still in the same month so that it is = today according to our
    // comparison strategy (that compares only month and year)
    // => if we are at the end of the month we subtract one day instead of adding one
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 1);
    Date tomorrow = cal.getTime();
    cal.add(Calendar.DAY_OF_MONTH, -2);
    Date yesterday = cal.getTime();
    actual = monthOf(tomorrow) == monthOf(new Date()) ? tomorrow : yesterday;

    Throwable error = catchThrowable(() -> datesWithCustomComparisonStrategy.assertIsInTheFuture(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeInTheFuture(actual, yearAndMonthComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> datesWithCustomComparisonStrategy.assertIsInTheFuture(someInfo(),
                                                                                                                           null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_in_the_future_according_to_custom_comparison_strategy() {
    actual = parseDate("2111-01-01");
    datesWithCustomComparisonStrategy.assertIsInTheFuture(someInfo(), actual);
  }

}
