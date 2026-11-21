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
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.Dates.parseDatetime;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Date;

import org.assertj.tests.core.testkit.YearAndMonthComparator;
import org.junit.jupiter.api.Test;

class DateAssert_hasMinute_Test {

  private static final Date ACTUAL = parseDatetime("2011-01-01T03:49:02");

  @Test
  void should_pass_if_actual_has_given_minute() {
    assertThat(ACTUAL).hasMinute(49);
  }

  @Test
  void should_fail_if_actual_has_not_given_minute() {
    // GIVEN
    int expected = 5;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).hasMinute(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveDateField(ACTUAL, "minute", expected).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Date actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMinute(49));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_has_given_minute_whatever_custom_comparison_strategy_is() {
    assertThat(ACTUAL).usingComparator(new YearAndMonthComparator()).hasMinute(49);
  }

  @Test
  void should_fail_if_actual_has_not_given_minute_whatever_custom_comparison_strategy_is() {
    // GIVEN
    int expected = 5;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingComparator(new YearAndMonthComparator())
                                                                      .hasMinute(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveDateField(ACTUAL, "minute", expected).create());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Date actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                                      .hasMinute(49));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
