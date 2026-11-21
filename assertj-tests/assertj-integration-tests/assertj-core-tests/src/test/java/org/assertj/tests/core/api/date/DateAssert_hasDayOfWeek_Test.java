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
package org.assertj.tests.core.api.date;

import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.Dates.parseDate;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Date;

import org.assertj.tests.core.testkit.YearAndMonthComparator;
import org.junit.jupiter.api.Test;

class DateAssert_hasDayOfWeek_Test {

  private static final Date ACTUAL = parseDate("2011-01-01");

  @Test
  void should_pass_if_actual_has_given_day_of_week() {
    assertThat(ACTUAL).hasDayOfWeek(SATURDAY);
  }

  @Test
  void should_fail_if_actual_has_not_given_day_of_week() {
    // GIVEN
    int expected = SUNDAY;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).hasDayOfWeek(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveDateField(ACTUAL, "day of week", expected).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Date actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDayOfWeek(SATURDAY));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_has_given_day_of_week_whatever_custom_comparison_strategy_is() {
    assertThat(ACTUAL).usingComparator(new YearAndMonthComparator()).hasDayOfWeek(SATURDAY);
  }

  @Test
  void should_fail_if_actual_has_not_given_day_of_week_whatever_custom_comparison_strategy_is() {
    // GIVEN
    int expected = SUNDAY;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingComparator(new YearAndMonthComparator())
                                                                      .hasDayOfWeek(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveDateField(ACTUAL, "day of week", expected).create());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Date actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                      .hasDayOfWeek(SATURDAY));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
