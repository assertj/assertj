/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.dates;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInTheFuture.shouldBeInTheFuture;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.DateUtil.monthOf;
import static org.assertj.core.util.FailureMessages.actualIsNull;

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
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> dates.assertIsInTheFuture(info, actual));

    // THEN
    then(error).hasMessage(shouldBeInTheFuture(actual).create());
  }

  @Test
  void should_fail_if_actual_is_today() {
    // GIVEN
    AssertionInfo info = someInfo();
    actual = new Date();
    // WHEN
    var error = expectAssertionError(() -> dates.assertIsInTheFuture(info, actual));

    // THEN
    then(error).hasMessage(shouldBeInTheFuture(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> dates.assertIsInTheFuture(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_in_the_future() {
    actual = parseDate("2111-01-01");
    new Date(new Date().getTime() + 3600);
    dates.assertIsInTheFuture(someInfo(), actual);
    dates.assertIsInTheFuture(someInfo(), new Date(new Date().getTime() + 1000));
  }

  @Test
  void should_fail_if_actual_is_not_in_the_future_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> datesWithCustomComparisonStrategy.assertIsInTheFuture(info, actual));

    // THEN
    then(error).hasMessage(shouldBeInTheFuture(actual, yearAndMonthComparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_is_today_according_to_custom_comparison_strategy() {
    // GIVEN
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
    // WHEN
    var error = expectAssertionError(() -> datesWithCustomComparisonStrategy.assertIsInTheFuture(info, actual));

    // THEN
    then(error).hasMessage(shouldBeInTheFuture(actual, yearAndMonthComparisonStrategy).create());
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
