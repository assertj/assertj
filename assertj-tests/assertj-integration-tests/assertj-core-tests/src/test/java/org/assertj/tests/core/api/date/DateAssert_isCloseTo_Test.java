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
import static org.assertj.core.error.ShouldBeCloseTo.shouldBeCloseTo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.Dates.parseDate;
import static org.assertj.tests.core.testkit.Dates.parseDatetime;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.time.Instant;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.assertj.tests.core.testkit.ResetGlobalConfigurationExtension;
import org.assertj.tests.core.testkit.YearAndMonthComparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResetGlobalConfigurationExtension.class)
class DateAssert_isCloseTo_Test {

  private static final long DELTA = 100;

  private final Date actual = parseDatetime("2011-01-01T03:15:05");

  @AfterEach
  void tearDown() {
    Assertions.useDefaultDateFormatsOnly();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Date nullActual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(nullActual).isCloseTo(parseDate("2010-01-01"), 10));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_given_date_is_null() {
    // GIVEN
    Date other = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).isCloseTo(other, 10));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The date to compare actual with should not be null");
  }

  @Test
  void should_throw_error_if_given_date_as_string_is_null() {
    // GIVEN
    String other = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).isCloseTo(other, 10));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The date to compare actual with should not be null");
  }

  @Test
  void should_fail_if_actual_is_not_close_to_given_date_by_less_than_given_delta() {
    // GIVEN
    Date other = new Date(actual.getTime() + DELTA + 1);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isCloseTo(other, DELTA));
    // THEN
    then(assertionError).hasMessage(shouldBeCloseTo(actual, other, DELTA, 101).create());
  }

  @Test
  void should_pass_if_actual_is_equal_to_given_date() {
    // GIVEN
    Date other = parseDatetime("2011-01-01T03:15:05");
    // WHEN/THEN
    then(actual).isCloseTo(other, DELTA);
  }

  @Test
  void should_pass_if_actual_is_close_to_given_date_by_exactly_given_delta() {
    // GIVEN
    Date other = new Date(actual.getTime() - DELTA);
    // WHEN/THEN
    then(actual).isCloseTo(other, DELTA);
  }

  @Test
  void should_fail_if_actual_is_not_close_to_given_instant_by_less_than_given_delta() {
    // GIVEN
    Instant other = actual.toInstant().plusMillis(DELTA + 1);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isCloseTo(other, DELTA));
    // THEN
    then(assertionError).hasMessage(shouldBeCloseTo(actual, Date.from(other), DELTA, 101).create());
  }

  @Test
  void should_pass_if_actual_is_close_to_given_instant_by_less_than_given_delta() {
    // GIVEN
    Instant other = actual.toInstant().plusMillis(DELTA);
    // WHEN/THEN
    then(actual).isCloseTo(other, DELTA);
  }

  @Test
  void should_fail_if_actual_is_not_close_to_given_date_as_string_in_default_format() {
    // GIVEN
    String other = "2011-01-01T03:15:05.101";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isCloseTo(other, DELTA));
    // THEN
    then(assertionError).hasMessage(shouldBeCloseTo(actual, parseDatetime(other), DELTA, 101).create());
  }

  @Test
  void should_pass_if_actual_is_close_to_given_date_as_string_in_default_format() {
    // GIVEN
    String other = "2011-01-01T03:15:05.100";
    // WHEN/THEN
    then(actual).isCloseTo(other, DELTA);
  }

  @Test
  void should_fail_if_actual_is_not_close_to_given_date_as_string_in_custom_format() {
    // GIVEN
    String other = "01/01/2011 03:15:06";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).withDateFormat("dd/MM/yyyy HH:mm:ss")
                                                                      .isCloseTo(other, DELTA));
    // THEN
    then(assertionError).hasMessage(shouldBeCloseTo(actual, parseDatetime("2011-01-01T03:15:06"), DELTA, 1000).create());
  }

  @Test
  void should_pass_if_actual_is_close_to_given_date_as_string_in_custom_format() {
    // GIVEN
    String other = "01/01/2011 03:15:05";
    // WHEN/THEN
    then(actual).withDateFormat("dd/MM/yyyy HH:mm:ss").isCloseTo(other, DELTA);
  }

  @Test
  void should_fail_if_given_date_as_string_does_not_follow_any_supported_format() {
    // GIVEN
    String other = "31/12/2000";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isCloseTo(other, DELTA));
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
  void should_fail_if_actual_is_not_close_to_given_date_by_less_than_given_delta_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Date other = new Date(actual.getTime() + DELTA + 1);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                      .isCloseTo(other, DELTA));
    // THEN
    then(assertionError).hasMessage(shouldBeCloseTo(actual, other, DELTA, 101).create());
  }

  @Test
  void should_pass_if_actual_is_close_to_given_date_by_less_than_given_delta_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Date other = parseDatetime("2011-01-01T03:15:05");
    // WHEN/THEN
    then(actual).usingComparator(new YearAndMonthComparator()).isCloseTo(other, DELTA);
  }

}
