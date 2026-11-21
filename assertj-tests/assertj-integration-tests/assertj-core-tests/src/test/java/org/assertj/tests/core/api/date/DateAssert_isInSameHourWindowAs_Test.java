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
import static org.assertj.core.error.ShouldBeInSameHourWindow.shouldBeInSameHourWindow;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.Dates.parseDatetime;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.time.Instant;
import java.util.Date;

import org.assertj.tests.core.testkit.ResetGlobalConfigurationExtension;
import org.assertj.tests.core.testkit.YearAndMonthComparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResetGlobalConfigurationExtension.class)
class DateAssert_isInSameHourWindowAs_Test {

  private final Date actual = parseDatetime("2011-01-01T03:01:02");

  @AfterEach
  void tearDown() {
    useDefaultDateFormatsOnly();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Date nullDate = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(nullDate).isInSameHourWindowAs(new Date()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_given_date_is_null() {
    // GIVEN
    Date other = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).isInSameHourWindowAs(other));
    // THEN
    then(exception).hasMessage("The date to compare actual with should not be null");
  }

  @Test
  void should_pass_if_actual_is_in_same_hour_window_as_given_date() {
    assertThat(actual).isInSameHourWindowAs(parseDatetime("2011-01-01T03:59:02"))
                      .isInSameHourWindowAs(parseDatetime("2011-01-01T02:01:03"));
  }

  @Test
  void should_fail_if_time_difference_is_exactly_one_hour() {
    // GIVEN
    Date other = parseDatetime("2011-01-01T04:01:02");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameHourWindowAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameHourWindow(actual, other).create());
  }

  @Test
  void should_fail_if_actual_is_not_in_same_hour_window_as_given_date() {
    // GIVEN
    Date other = parseDatetime("2011-01-01T04:01:03");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameHourWindowAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameHourWindow(actual, other).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_hour_window_as_given_date_whatever_custom_comparison_strategy_is() {
    assertThat(actual).usingComparator(new YearAndMonthComparator()).isInSameHourWindowAs(parseDatetime("2011-01-01T03:59:02"));
    assertThat(actual).usingComparator(new YearAndMonthComparator()).isInSameHourWindowAs(parseDatetime("2011-01-01T04:01:00"));
  }

  @Test
  void should_fail_if_actual_is_not_in_same_hour_window_as_given_date_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Date other = parseDatetime("2011-01-01T04:01:03");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                      .isInSameHourWindowAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameHourWindow(actual, other).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_hour_window_as_given_instant() {
    // GIVEN
    Instant other = parseDatetime("2011-01-01T03:30:00").toInstant();
    // WHEN/THEN
    then(actual).isInSameHourWindowAs(other);
  }

  @Test
  void should_fail_if_actual_is_not_in_same_hour_window_as_given_instant() {
    // GIVEN
    Instant other = parseDatetime("2011-01-01T02:01:02").toInstant();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameHourWindowAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameHourWindow(actual, Date.from(other)).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_hour_window_as_given_date_as_string_in_default_format() {
    assertThat(actual).isInSameHourWindowAs("2011-01-01T03:59:02");
  }

  @Test
  void should_fail_if_actual_is_not_in_same_hour_window_as_given_date_as_string_in_default_format() {
    // GIVEN
    String other = "2011-01-01T04:01:03";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameHourWindowAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameHourWindow(actual, parseDatetime(other)).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_hour_window_as_given_date_as_string_following_custom_format() {
    assertThat(actual).withDateFormat("dd/MM/yyyy HH:mm:ss").isInSameHourWindowAs("01/01/2011 03:59:02");
  }

  @Test
  void should_fail_if_given_date_as_string_does_not_follow_expected_format() {
    // GIVEN
    String dateAsStringWithBadFormat = "31/12/2000";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameHourWindowAs(dateAsStringWithBadFormat));
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
