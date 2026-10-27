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
import static org.assertj.core.error.ShouldBeNotInTheFuture.shouldBeNotInTheFuture;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.Dates.parseDate;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Date;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.YearAndMonthComparator;
import org.junit.jupiter.api.Test;

class DateAssert_isNotInTheFuture_Test {

  private static final ComparatorBasedComparisonStrategy YEAR_AND_MONTH_COMPARISON_STRATEGY = new ComparatorBasedComparisonStrategy(new YearAndMonthComparator());

  @Test
  void should_pass_if_actual_is_in_the_past() {
    // GIVEN
    Date actual = parseDate("2000-01-01");
    // WHEN/THEN
    then(actual).isNotInTheFuture();
  }

  @Test
  void should_pass_if_actual_is_now() {
    // GIVEN
    Date actual = new Date();
    // WHEN/THEN
    then(actual).isNotInTheFuture();
  }

  @Test
  void should_fail_if_actual_is_in_the_future() {
    // GIVEN
    Date actual = parseDate("2111-01-01");
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).isNotInTheFuture());
    // THEN
    then(error).hasMessage(shouldBeNotInTheFuture(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Date actual = null;
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).isNotInTheFuture());
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_now_according_to_custom_comparison_strategy() {
    // GIVEN the custom comparison strategy compares only year and month
    Date actual = new Date();
    // WHEN/THEN
    then(actual).usingComparator(new YearAndMonthComparator())
                .isNotInTheFuture();
  }

  @Test
  void should_fail_if_actual_is_in_the_future_according_to_custom_comparison_strategy() {
    // GIVEN
    Date actual = parseDate("2111-01-01");
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).usingComparator(new YearAndMonthComparator())
                                                             .isNotInTheFuture());
    // THEN
    then(error).hasMessage(shouldBeNotInTheFuture(actual, YEAR_AND_MONTH_COMPARISON_STRATEGY).create());
  }

}
