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
import static org.assertj.core.error.ShouldBeInSameMinuteWindow.shouldBeInSameMinuteWindow;
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
class DateAssert_isInSameMinuteWindowAs_Test {

  private final Date actual = parseDatetime("2011-01-01T03:15:00");

  @AfterEach
  void tearDown() {
    useDefaultDateFormatsOnly();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Date nullDate = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(nullDate).isInSameMinuteWindowAs(new Date()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_given_date_is_null() {
    // GIVEN
    Date other = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).isInSameMinuteWindowAs(other));
    // THEN
    then(exception).hasMessage("The date to compare actual with should not be null");
  }

  @Test
  void should_pass_if_actual_is_in_same_minute_window_as_given_date() {
    assertThat(actual).isInSameMinuteWindowAs(parseDatetime("2011-01-01T03:15:59"))
                      .isInSameMinuteWindowAs(parseDatetime("2011-01-01T03:14:01"));
  }

  @Test
  void should_fail_if_actual_is_exactly_one_minute_away_from_given_date() {
    // GIVEN
    Date other = parseDatetime("2011-01-01T03:16:00");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameMinuteWindowAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameMinuteWindow(actual, other).create());
  }

  @Test
  void should_fail_if_actual_is_not_in_same_minute_window_as_given_date() {
    // GIVEN
    Date other = parseDatetime("2011-01-01T03:16:01");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameMinuteWindowAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameMinuteWindow(actual, other).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_minute_window_as_given_date_whatever_custom_comparison_strategy_is() {
    assertThat(actual).usingComparator(new YearAndMonthComparator()).isInSameMinuteWindowAs(parseDatetime("2011-01-01T03:15:59"));
  }

  @Test
  void should_fail_if_actual_is_not_in_same_minute_window_as_given_date_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Date other = parseDatetime("2011-01-01T03:13:59");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                      .isInSameMinuteWindowAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameMinuteWindow(actual, other).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_minute_window_as_given_instant() {
    // GIVEN
    Instant other = parseDatetime("2011-01-01T03:15:30").toInstant();
    // WHEN/THEN
    then(actual).isInSameMinuteWindowAs(other);
  }

  @Test
  void should_fail_if_actual_is_not_in_same_minute_window_as_given_instant() {
    // GIVEN
    Instant other = parseDatetime("2011-01-01T03:14:00").toInstant();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameMinuteWindowAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameMinuteWindow(actual, Date.from(other)).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_minute_window_as_given_date_as_string_in_default_format() {
    assertThat(actual).isInSameMinuteWindowAs("2011-01-01T03:15:59");
  }

  @Test
  void should_fail_if_actual_is_not_in_same_minute_window_as_given_date_as_string_in_default_format() {
    // GIVEN
    String other = "2011-01-01T03:16:01";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameMinuteWindowAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeInSameMinuteWindow(actual, parseDatetime(other)).create());
  }

  @Test
  void should_pass_if_actual_is_in_same_minute_window_as_given_date_as_string_following_custom_format() {
    assertThat(actual).withDateFormat("dd/MM/yyyy HH:mm:ss").isInSameMinuteWindowAs("01/01/2011 03:14:01");
  }

  @Test
  void should_fail_if_given_date_as_string_does_not_follow_expected_format() {
    // GIVEN
    String dateAsStringWithBadFormat = "31/12/2000";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isInSameMinuteWindowAs(dateAsStringWithBadFormat));
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
