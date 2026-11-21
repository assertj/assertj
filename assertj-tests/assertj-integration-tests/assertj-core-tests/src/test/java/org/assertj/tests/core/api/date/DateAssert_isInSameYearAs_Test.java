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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.useDefaultDateFormatsOnly;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInSameYear.shouldBeInSameYear;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.Dates.parseDate;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.time.Instant;
import java.util.Date;

import org.assertj.tests.core.testkit.ResetGlobalConfigurationExtension;
import org.assertj.tests.core.testkit.YearAndMonthComparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResetGlobalConfigurationExtension.class)
class DateAssert_isInSameYearAs_Test {

  private final Date actual = parseDate("2011-01-01");

  @AfterEach
  void tearDown() {
    useDefaultDateFormatsOnly();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Date nullDate = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(nullDate).isInSameYearAs(new Date()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_given_date_is_null() {
    // GIVEN
    Date other = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).isInSameYearAs(other));
    // THEN
    then(exception).hasMessage("The date to compare actual with should not be null");
  }

  @Test
  void should_pass_if_actual_is_in_same_year_as_given_date() {
    assertThat(actual).isInSameYearAs(parseDate("2011-05-11"));
  }

  @Test
  void should_fail_if_actual_is_not_in_same_year_as_given_date() {
    // GIVEN
    Date other = parseDate("2000-01-01");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameYearAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameYear(actual, other).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_year_as_given_date_whatever_custom_comparison_strategy_is() {
    assertThat(actual).usingComparator(new YearAndMonthComparator()).isInSameYearAs(parseDate("2011-05-11"));
  }

  @Test
  void should_fail_if_actual_is_not_in_same_year_as_given_date_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Date other = parseDate("2000-01-01");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                      .isInSameYearAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameYear(actual, other).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_year_as_given_instant() {
    // GIVEN
    Instant other = parseDate("2011-12-31").toInstant();
    // WHEN/THEN
    then(actual).isInSameYearAs(other);
  }

  @Test
  void should_fail_if_actual_is_not_in_same_year_as_given_instant() {
    // GIVEN
    Instant other = parseDate("2012-01-01").toInstant();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameYearAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameYear(actual, Date.from(other)).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_year_as_given_date_as_string_in_default_format() {
    assertThat(actual).isInSameYearAs("2011-05-11");
  }

  @Test
  void should_fail_if_actual_is_not_in_same_year_as_given_date_as_string_in_default_format() {
    // GIVEN
    String other = "2000-01-01";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameYearAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameYear(actual, parseDate(other)).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_year_as_given_date_as_string_following_custom_format() {
    assertThat(actual).withDateFormat("dd/MM/yyyy").isInSameYearAs("11/05/2011");
  }

  @Test
  void should_fail_if_given_date_as_string_does_not_follow_expected_format() {
    // GIVEN
    String dateAsStringWithBadFormat = "31/12/2000";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameYearAs(dateAsStringWithBadFormat));
    // THEN
    then(assertionError).hasMessage(format("Failed to parse 31/12/2000 with any of these date formats:%n" +
                                           "   [yyyy-MM-dd'T'HH:mm:ss.SSSX,%n" +
                                           "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                           "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                           "    yyyy-MM-dd'T'HH:mm:ssX,%n" +
                                           "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                           "    yyyy-MM-dd HH:mm:ss,%n" +
                                           "    yyyy-MM-dd]"));
  }

}
