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
import static org.assertj.core.api.Assertions.useDefaultDateFormatsOnly;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;
import static org.assertj.core.error.ShouldHaveSameTime.shouldHaveSameTime;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.Dates.parseDate;
import static org.assertj.tests.core.testkit.Dates.parseDatetime;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.assertj.tests.core.testkit.ResetGlobalConfigurationExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResetGlobalConfigurationExtension.class)
class DateAssert_hasSameTimeAs_Test {

  @AfterEach
  void tearDown() {
    useDefaultDateFormatsOnly();
  }

  @Nested
  class With_Date {

    @Test
    void should_pass_if_actual_has_same_time() {
      // GIVEN
      Date actual = parseDatetime("2011-01-01T03:49:17");
      Date expected = new Date(actual.getTime());
      // WHEN/THEN
      then(actual).hasSameTimeAs(expected);
    }

    @Test
    void should_pass_if_actual_has_same_time_as_timestamp_and_vice_versa() {
      // GIVEN
      Date date = new Date();
      Timestamp timestamp = new Timestamp(date.getTime());
      // WHEN/THEN
      then(date).hasSameTimeAs(timestamp);
      then(timestamp).hasSameTimeAs(date);
    }

    @Test
    void should_fail_if_actual_has_not_same_time() {
      // GIVEN
      Date actual = parseDatetime("2011-01-01T03:49:17");
      Date expected = new Date(actual.getTime() + 1);
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTimeAs(expected));
      // THEN
      then(assertionError).hasMessage(shouldHaveSameTime(actual, expected).create());
    }

    @Test
    void should_fail_if_actual_is_null() {
      // GIVEN
      Date actual = null;
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTimeAs(new Date()));
      // THEN
      then(assertionError).hasMessage(actualIsNull());
    }

    @Test
    void should_throw_error_if_expected_is_null() {
      // GIVEN
      Date expected = null;
      // WHEN/THEN
      thenNullPointerException().isThrownBy(() -> assertThat(new Date()).hasSameTimeAs(expected))
                                .withMessage("The date to compare actual with should not be null");
    }

  }

  @Nested
  class With_String {

    @Test
    void should_pass_if_actual_has_same_time_as_date_string_in_default_date_only_format() {
      // GIVEN
      Date actual = parseDate("2011-01-01");
      // WHEN/THEN
      then(actual).hasSameTimeAs("2011-01-01");
    }

    @Test
    void should_pass_if_actual_has_same_time_as_date_string_in_default_datetime_format() {
      // GIVEN
      Date actual = parseDatetime("2003-04-26T12:00:00");
      // WHEN/THEN
      then(actual).hasSameTimeAs("2003-04-26T12:00:00");
    }

    @Test
    void should_pass_if_actual_has_same_time_as_timestamp_string_with_custom_date_format() {
      // GIVEN
      Date actual = parseDatetime("2003-04-26T12:59:59.999");
      Timestamp timestamp = new Timestamp(actual.getTime());
      // WHEN/THEN
      then(actual).withDateFormat("yyyy-MM-dd HH:mm:ss.SSS").hasSameTimeAs(timestamp.toString());
    }

    @Test
    void should_pass_if_actual_has_same_time_as_date_string_following_custom_date_format() {
      // GIVEN
      Date actual = parseDate("2000-12-31");
      // WHEN/THEN
      then(actual).withDateFormat(new SimpleDateFormat("dd/MM/yyyy")).hasSameTimeAs("31/12/2000");
    }

    @Test
    void should_fail_if_actual_has_not_same_time_as_date_string() {
      // GIVEN
      Date actual = parseDatetime("2003-04-26T12:00:00");
      String expected = "2003-04-27T12:00:00";
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTimeAs(expected));
      // THEN
      then(assertionError).hasMessage(shouldHaveSameTime(actual, parseDatetime(expected)).create());
    }

    @Test
    void should_fail_if_date_string_does_not_follow_expected_format() {
      // GIVEN
      Date actual = parseDate("2000-12-31");
      String dateAsStringWithBadFormat = "31/12/2000";
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTimeAs(dateAsStringWithBadFormat));
      // THEN
      then(assertionError).hasMessage(format("Failed to parse " + dateAsStringWithBadFormat +
                                             " with any of these date formats:%n" +
                                             "   [yyyy-MM-dd'T'HH:mm:ss.SSSX,%n" +
                                             "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                             "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                             "    yyyy-MM-dd'T'HH:mm:ssX,%n" +
                                             "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                             "    yyyy-MM-dd HH:mm:ss,%n" +
                                             "    yyyy-MM-dd]"));
    }

    @Test
    void should_fail_if_actual_is_null() {
      // GIVEN
      Date actual = null;
      // WHEN
      var assertionError = expectAssertionError(() -> assertThat(actual).hasSameTimeAs("2003-04-26T12:00:00"));
      // THEN
      then(assertionError).hasMessage(actualIsNull());
    }

    @Test
    void should_throw_error_if_expected_is_null() {
      // GIVEN
      String expected = null;
      // WHEN/THEN
      thenNullPointerException().isThrownBy(() -> assertThat(new Date()).hasSameTimeAs(expected))
                                .withMessage("The date to compare actual with should not be null");
    }

  }

}
