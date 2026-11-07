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
import static org.assertj.core.error.ShouldBeToday.shouldBeToday;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.DateUtil.monthOf;
import static org.assertj.core.util.DateUtil.tomorrow;
import static org.assertj.core.util.DateUtil.yesterday;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Date;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Dates;
import org.assertj.core.internal.DatesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Dates#assertIsToday(AssertionInfo, Date)}</code>.
 * 
 * @author Joel Costigliola
 */
class Dates_assertIsToday_Test extends DatesBaseTest {

  @Test
  void should_fail_if_actual_is_not_today() {
    // GIVEN
    AssertionInfo info = someInfo();
    actual = parseDate("2111-01-01");
    // WHEN
    var error = expectAssertionError(() -> dates.assertIsToday(info, actual));

    // THEN
    then(error).hasMessage(shouldBeToday(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> dates.assertIsToday(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_today() {
    dates.assertIsToday(someInfo(), new Date());
  }

  @Test
  void should_fail_if_actual_is_not_today_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    actual = parseDate("2111-01-01");
    // WHEN
    var error = expectAssertionError(() -> datesWithCustomComparisonStrategy.assertIsToday(info, actual));

    // THEN
    then(error).hasMessage(shouldBeToday(actual, yearAndMonthComparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> datesWithCustomComparisonStrategy.assertIsToday(someInfo(),
                                                                                                                     null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_today_according_to_custom_comparison_strategy() {
    // we want actual to be different from today but still in the same month so that it is equal to today
    // according to our comparison strategy (that compares only month and year).
    // => if we are at the end of the month we subtract one day instead of adding one
    actual = monthOf(tomorrow()) == monthOf(new Date()) ? tomorrow() : yesterday();
    datesWithCustomComparisonStrategy.assertIsToday(someInfo(), actual);
  }

}
