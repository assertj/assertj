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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeBeforeYear.shouldBeBeforeYear;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.Dates.parseDate;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Date;

import org.assertj.tests.core.testkit.YearAndMonthComparator;
import org.junit.jupiter.api.Test;

class DateAssert_isBeforeYear_Test {

  private final Date actual = parseDate("2011-01-01");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Date nullActual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(nullActual).isBeforeYear(2010));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_strictly_before_given_year() {
    // GIVEN
    int year = 2010;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isBeforeYear(year));
    // THEN
    then(assertionError).hasMessage(shouldBeBeforeYear(actual, year).create());
  }

  @Test
  void should_fail_if_actual_year_is_equal_to_given_year() {
    // GIVEN
    int year = 2011;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isBeforeYear(year));
    // THEN
    then(assertionError).hasMessage(shouldBeBeforeYear(actual, year).create());
  }

  @Test
  void should_pass_if_actual_is_strictly_before_given_year() {
    // GIVEN
    int year = 2020;
    // WHEN/THEN
    then(actual).isBeforeYear(year);
  }

  @Test
  void should_fail_if_actual_is_not_strictly_before_given_year_whatever_custom_comparison_strategy_is() {
    // GIVEN
    int year = 2010;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                      .isBeforeYear(year));
    // THEN
    then(assertionError).hasMessage(shouldBeBeforeYear(actual, year).create());
  }

  @Test
  void should_fail_if_actual_year_is_equal_to_given_year_whatever_custom_comparison_strategy_is() {
    // GIVEN
    int year = 2011;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                      .isBeforeYear(year));
    // THEN
    then(assertionError).hasMessage(shouldBeBeforeYear(actual, year).create());
  }

  @Test
  void should_pass_if_actual_is_strictly_before_given_year_whatever_custom_comparison_strategy_is() {
    // GIVEN
    int year = 2020;
    // WHEN/THEN
    then(actual).usingComparator(new YearAndMonthComparator()).isBeforeYear(year);
  }

}
