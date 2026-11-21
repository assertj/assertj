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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeBetween.shouldNotBeBetween;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.Dates.parseDate;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Date;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.ResetGlobalConfigurationExtension;
import org.assertj.tests.core.testkit.YearAndMonthComparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResetGlobalConfigurationExtension.class)
class DateAssert_isNotBetween_Test {

  private static final ComparatorBasedComparisonStrategy YEAR_AND_MONTH_COMPARISON_STRATEGY = new ComparatorBasedComparisonStrategy(new YearAndMonthComparator());

  @AfterEach
  void tearDown() {
    Assertions.useDefaultDateFormatsOnly();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Date actual = null;
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).isNotBetween(start, end, true, true));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_start_date_is_null() {
    // GIVEN
    Date actual = parseDate("2011-09-27");
    Date end = parseDate("2011-09-30");
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isNotBetween(null, end, true, true));
    // THEN
    then(npe).hasMessage("The start date of period to compare actual with should not be null");
  }

  @Test
  void should_throw_error_if_end_date_is_null() {
    // GIVEN
    Date actual = parseDate("2011-09-27");
    Date start = parseDate("2011-09-01");
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isNotBetween(start, null, true, true));
    // THEN
    then(npe).hasMessage("The end date of period to compare actual with should not be null");
  }

  @Test
  void should_fail_if_actual_is_between_given_period() {
    // GIVEN
    Date actual = parseDate("2011-09-27");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).isNotBetween(start, end, true, true));
    // THEN
    then(error).hasMessage(shouldNotBeBetween(actual, start, end, true, true).create());
  }

  @Test
  void should_fail_if_actual_is_equals_to_start_of_given_period_and_start_is_included_in_given_period() {
    // GIVEN
    Date actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).isNotBetween(start, end, true, false));
    // THEN
    then(error).hasMessage(shouldNotBeBetween(actual, start, end, true, false).create());
  }

  @Test
  void should_fail_if_actual_is_equals_to_end_of_given_period_and_end_is_included_in_given_period() {
    // GIVEN
    Date actual = parseDate("2011-09-30");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).isNotBetween(start, end, false, true));
    // THEN
    then(error).hasMessage(shouldNotBeBetween(actual, start, end, false, true).create());
  }

  @Test
  void should_pass_if_actual_is_not_between_given_period() {
    // GIVEN
    Date actual = parseDate("2011-12-31");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    // WHEN/THEN
    then(actual).isNotBetween(start, end, true, true);
  }

  @Test
  void should_pass_if_actual_is_equals_to_start_of_given_period_and_start_is_not_included_in_given_period() {
    // GIVEN
    Date actual = parseDate("2011-09-01");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    // WHEN/THEN
    then(actual).isNotBetween(start, end, false, false)
                .isNotBetween(start, end, false, true);
  }

  @Test
  void should_pass_if_actual_is_equals_to_end_of_given_period_and_end_is_not_included_in_given_period() {
    // GIVEN
    Date actual = parseDate("2011-09-30");
    Date start = parseDate("2011-09-01");
    Date end = parseDate("2011-09-30");
    // WHEN/THEN
    then(actual).isNotBetween(start, end, false, false)
                .isNotBetween(start, end, true, false);
  }

  @Nested
  class Default_boundaries_inclusion {

    @Test
    void should_pass_if_actual_is_equals_to_end_of_given_period() {
      // GIVEN
      Date actual = parseDate("2011-09-30");
      Date start = parseDate("2011-09-01");
      Date end = parseDate("2011-09-30");
      // WHEN/THEN
      then(actual).isNotBetween(start, end);
    }

    @Test
    void should_fail_if_actual_is_equals_to_start_of_given_period() {
      // GIVEN
      Date actual = parseDate("2011-09-01");
      Date start = parseDate("2011-09-01");
      Date end = parseDate("2011-09-30");
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).isNotBetween(start, end));
      // THEN
      then(error).hasMessage(shouldNotBeBetween(actual, start, end, true, false).create());
    }

  }

  @Nested
  class Instant_overloads {

    @Test
    void should_pass_if_actual_is_not_between_given_period() {
      // GIVEN
      Date actual = parseDate("2011-12-31");
      Date start = parseDate("2011-09-01");
      Date end = parseDate("2011-09-30");
      // WHEN/THEN
      then(actual).isNotBetween(start.toInstant(), end.toInstant())
                  .isNotBetween(start.toInstant(), end.toInstant(), true, true);
    }

    @Test
    void should_fail_if_actual_is_between_given_period() {
      // GIVEN
      Date actual = parseDate("2011-09-27");
      Date start = parseDate("2011-09-01");
      Date end = parseDate("2011-09-30");
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).isNotBetween(start.toInstant(), end.toInstant()));
      // THEN
      then(error).hasMessage(shouldNotBeBetween(actual, start, end, true, false).create());
    }

    @Test
    void should_fail_if_actual_is_equals_to_end_of_given_period_and_end_is_included_in_given_period() {
      // GIVEN
      Date actual = parseDate("2011-09-30");
      Date start = parseDate("2011-09-01");
      Date end = parseDate("2011-09-30");
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).isNotBetween(start.toInstant(), end.toInstant(), false, true));
      // THEN
      then(error).hasMessage(shouldNotBeBetween(actual, start, end, false, true).create());
    }

  }

  @Nested
  class String_overloads {

    @Test
    void should_pass_if_actual_is_not_between_given_period_in_default_date_format() {
      // GIVEN
      Date actual = parseDate("2011-12-31");
      // WHEN/THEN
      then(actual).isNotBetween("2011-09-01", "2011-09-30")
                  .isNotBetween("2011-09-01", "2011-09-30", true, true)
                  .isNotBetween("2011-12-31", "2012-01-31", false, true);
    }

    @Test
    void should_fail_if_actual_is_between_given_period_in_default_date_format() {
      // GIVEN
      Date actual = parseDate("2011-09-27");
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).isNotBetween("2011-09-01", "2011-09-30"));
      // THEN
      then(error).hasMessage(shouldNotBeBetween(actual, parseDate("2011-09-01"), parseDate("2011-09-30"), true,
                                                false).create());
    }

    @Test
    void should_fail_if_actual_is_equals_to_end_of_given_period_in_default_date_format_and_end_is_included() {
      // GIVEN
      Date actual = parseDate("2011-09-30");
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).isNotBetween("2011-09-01", "2011-09-30", false, true));
      // THEN
      then(error).hasMessage(shouldNotBeBetween(actual, parseDate("2011-09-01"), parseDate("2011-09-30"), false,
                                                true).create());
    }

    @Test
    void should_pass_if_actual_is_not_between_given_period_in_custom_date_format() {
      // GIVEN
      Date actual = parseDate("2011-12-31");
      // WHEN/THEN
      then(actual).withDateFormat("dd/MM/yyyy")
                  .isNotBetween("01/09/2011", "30/09/2011")
                  .isNotBetween("01/09/2011", "30/09/2011", false, true);
    }

    @Test
    void should_fail_if_given_period_does_not_follow_expected_date_format() {
      // GIVEN
      Date actual = parseDate("2011-12-31");
      String start = "01/09/2011";
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).isNotBetween(start, "2011-09-30"));
      // THEN
      then(error).hasMessage(format("Failed to parse " + start + " with any of these date formats:%n" +
                                    "   [yyyy-MM-dd'T'HH:mm:ss.SSSX,%n" +
                                    "    yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                    "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                    "    yyyy-MM-dd'T'HH:mm:ssX,%n" +
                                    "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                    "    yyyy-MM-dd HH:mm:ss,%n" +
                                    "    yyyy-MM-dd]"));
    }

    @Test
    void should_fail_if_given_period_does_not_follow_expected_date_format_specifying_boundaries_inclusion() {
      // GIVEN
      Date actual = parseDate("2011-12-31");
      String end = "30/09/2011";
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).isNotBetween("2011-09-01", end, false, true));
      // THEN
      then(error).hasMessageStartingWith("Failed to parse " + end + " with any of these date formats:");
    }

  }

  @Nested
  class With_custom_comparison_strategy {

    @Test
    void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
      // GIVEN
      Date actual = null;
      Date start = parseDate("2011-09-01");
      Date end = parseDate("2011-09-30");
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                               .isNotBetween(start, end, true, true));
      // THEN
      then(error).hasMessage(actualIsNull());
    }

    @Test
    void should_throw_error_if_start_date_is_null_whatever_custom_comparison_strategy_is() {
      // GIVEN
      Date actual = parseDate("2011-09-27");
      Date end = parseDate("2011-09-30");
      // WHEN
      var npe = catchNullPointerException(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                  .isNotBetween(null, end, true, true));
      // THEN
      then(npe).hasMessage("The start date of period to compare actual with should not be null");
    }

    @Test
    void should_throw_error_if_end_date_is_null_whatever_custom_comparison_strategy_is() {
      // GIVEN
      Date actual = parseDate("2011-09-27");
      Date start = parseDate("2011-09-01");
      // WHEN
      var npe = catchNullPointerException(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                  .isNotBetween(start, null, true, true));
      // THEN
      then(npe).hasMessage("The end date of period to compare actual with should not be null");
    }

    @Test
    void should_fail_if_actual_is_between_given_period_according_to_custom_comparison_strategy() {
      // GIVEN
      Date actual = parseDate("2011-09-27");
      Date start = parseDate("2011-08-31");
      Date end = parseDate("2011-09-30");
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                               .isNotBetween(start, end, true, true));
      // THEN
      then(error).hasMessage(shouldNotBeBetween(actual, start, end, true, true, YEAR_AND_MONTH_COMPARISON_STRATEGY).create());
    }

    @Test
    void should_fail_if_actual_is_equals_to_start_of_given_period_and_start_is_included_in_given_period_according_to_custom_comparison_strategy() {
      // GIVEN
      Date actual = parseDate("2011-09-15");
      Date start = parseDate("2011-09-01"); // = 2011-09-15 according to comparison strategy
      Date end = parseDate("2011-10-01");
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                               .isNotBetween(start, end, true, false));
      // THEN
      then(error).hasMessage(shouldNotBeBetween(actual, start, end, true, false, YEAR_AND_MONTH_COMPARISON_STRATEGY).create());
    }

    @Test
    void should_fail_if_actual_is_equals_to_end_of_given_period_and_end_is_included_in_given_period_according_to_custom_comparison_strategy() {
      // GIVEN
      Date actual = parseDate("2011-09-15");
      Date start = parseDate("2011-08-31");
      Date end = parseDate("2011-09-30"); // = 2011-09-15 according to comparison strategy
      // WHEN
      var error = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                               .isNotBetween(start, end, false, true));
      // THEN
      then(error).hasMessage(shouldNotBeBetween(actual, start, end, false, true, YEAR_AND_MONTH_COMPARISON_STRATEGY).create());
    }

    @Test
    void should_pass_if_actual_is_not_between_given_period_according_to_custom_comparison_strategy() {
      // GIVEN
      Date actual = parseDate("2011-12-31");
      Date start = parseDate("2011-09-01");
      Date end = parseDate("2011-11-30");
      // WHEN/THEN
      then(actual).usingComparator(new YearAndMonthComparator())
                  .isNotBetween(start, end, true, true);
    }

    @Test
    void should_pass_if_actual_is_equals_to_start_of_given_period_and_start_is_not_included_in_given_period_according_to_custom_comparison_strategy() {
      // GIVEN
      Date actual = parseDate("2011-09-01");
      Date start = parseDate("2011-09-15"); // = 2011-09-01 according to comparison strategy
      Date end = parseDate("2011-09-30");
      // WHEN/THEN
      then(actual).usingComparator(new YearAndMonthComparator())
                  .isNotBetween(start, end, false, false)
                  .isNotBetween(start, end, false, true);
    }

    @Test
    void should_pass_if_actual_is_equals_to_end_of_given_period_and_end_is_not_included_in_given_period_according_to_custom_comparison_strategy() {
      // GIVEN
      Date actual = parseDate("2011-09-30");
      Date start = parseDate("2011-09-01");
      Date end = parseDate("2011-09-15"); // = 2011-09-30 according to comparison strategy
      // WHEN/THEN
      then(actual).usingComparator(new YearAndMonthComparator())
                  .isNotBetween(start, end, false, false)
                  .isNotBetween(start, end, true, false)
                  .isNotBetween(start, end);
    }

  }

}
