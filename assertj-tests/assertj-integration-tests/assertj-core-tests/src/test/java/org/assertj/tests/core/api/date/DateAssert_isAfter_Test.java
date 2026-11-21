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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAfter.shouldBeAfter;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.Dates.parseDate;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.time.Instant;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.ResetGlobalConfigurationExtension;
import org.assertj.tests.core.testkit.YearAndMonthComparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResetGlobalConfigurationExtension.class)
class DateAssert_isAfter_Test {

  private static final ComparatorBasedComparisonStrategy YEAR_AND_MONTH_COMPARISON_STRATEGY = new ComparatorBasedComparisonStrategy(new YearAndMonthComparator());

  private final Date actual = parseDate("2011-01-15");

  @AfterEach
  void tearDown() {
    Assertions.useDefaultDateFormatsOnly();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Date nullActual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(nullActual).isAfter(parseDate("2010-01-01")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_given_date_is_null() {
    // GIVEN
    Date other = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).isAfter(other));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The date to compare actual with should not be null");
  }

  @Test
  void should_throw_error_if_given_date_as_string_is_null() {
    // GIVEN
    String other = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).isAfter(other));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The date to compare actual with should not be null");
  }

  @Test
  void should_fail_if_actual_is_not_strictly_after_given_date() {
    // GIVEN
    Date other = parseDate("2020-01-01");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isAfter(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAfter(actual, other).create());
  }

  @Test
  void should_pass_if_actual_is_strictly_after_given_date() {
    // GIVEN
    Date other = parseDate("2000-01-01");
    // WHEN/THEN
    then(actual).isAfter(other);
  }

  @Test
  void should_fail_if_actual_is_equal_to_given_date() {
    // GIVEN
    Date other = parseDate("2011-01-15");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isAfter(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAfter(actual, other).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_given_instant() {
    // GIVEN
    Instant other = actual.toInstant();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isAfter(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAfter(actual, Date.from(other)).create());
  }

  @Test
  void should_fail_if_actual_is_not_strictly_after_given_instant() {
    // GIVEN
    Instant other = parseDate("2020-01-01").toInstant();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isAfter(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAfter(actual, Date.from(other)).create());
  }

  @Test
  void should_pass_if_actual_is_strictly_after_given_instant() {
    // GIVEN
    Instant other = parseDate("2000-01-01").toInstant();
    // WHEN/THEN
    then(actual).isAfter(other);
  }

  @Test
  void should_fail_if_actual_is_not_strictly_after_given_date_as_string_in_default_format() {
    // GIVEN
    String other = "2020-01-01";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isAfter(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAfter(actual, parseDate(other)).create());
  }

  @Test
  void should_pass_if_actual_is_strictly_after_given_date_as_string_in_default_format() {
    // GIVEN
    String other = "2000-01-01";
    // WHEN/THEN
    then(actual).isAfter(other);
  }

  @Test
  void should_pass_if_actual_is_strictly_after_given_date_time_as_string_in_default_format() {
    // GIVEN
    String other = "2011-01-14T23:59:59";
    // WHEN/THEN
    then(actual).isAfter(other);
  }

  @Test
  void should_fail_if_actual_is_not_strictly_after_given_date_as_string_in_custom_format() {
    // GIVEN
    String other = "31/12/2011";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).withDateFormat("dd/MM/yyyy").isAfter(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAfter(actual, parseDate("2011-12-31")).create());
  }

  @Test
  void should_pass_if_actual_is_strictly_after_given_date_as_string_in_custom_format() {
    // GIVEN
    String other = "31/12/2000";
    // WHEN/THEN
    then(actual).withDateFormat("dd/MM/yyyy").isAfter(other);
  }

  @Test
  void should_fail_if_given_date_as_string_does_not_follow_any_supported_format() {
    // GIVEN
    String other = "31/12/2000";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isAfter(other));
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

  @Test
  void should_fail_if_actual_is_not_strictly_after_given_date_according_to_custom_comparison_strategy() {
    // GIVEN
    Date other = parseDate("2020-01-01");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                      .isAfter(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAfter(actual, other, YEAR_AND_MONTH_COMPARISON_STRATEGY).create());
  }

  @Test
  void should_pass_if_actual_is_strictly_after_given_date_according_to_custom_comparison_strategy() {
    // GIVEN
    Date other = parseDate("2000-01-01");
    // WHEN/THEN
    then(actual).usingComparator(new YearAndMonthComparator()).isAfter(other);
  }

  @Test
  void should_fail_if_actual_is_equal_to_given_date_according_to_custom_comparison_strategy() {
    // GIVEN
    Date other = parseDate("2011-01-01");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                      .isAfter(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAfter(actual, other, YEAR_AND_MONTH_COMPARISON_STRATEGY).create());
  }

}
