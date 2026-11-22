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
package org.assertj.tests.core.api.yearmonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

class YearMonthAssert_isStrictlyBetween_Test {

  private static final YearMonth START = YearMonth.of(2000, 1);
  private static final YearMonth END = YearMonth.of(2000, 3);

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    YearMonth actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isStrictlyBetween(START, END));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_start_is_null() {
    // GIVEN
    YearMonth actual = YearMonth.of(2000, 2);
    YearMonth start = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isStrictlyBetween(start, END));
    // THEN
    then(npe).hasMessage("The start range to compare actual with should not be null");
  }

  @Test
  void should_fail_if_end_is_null() {
    // GIVEN
    YearMonth actual = YearMonth.of(2000, 2);
    YearMonth end = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isStrictlyBetween(START, end));
    // THEN
    then(npe).hasMessage("The end range to compare actual with should not be null");
  }

  @Test
  void should_pass_if_actual_is_strictly_between_start_and_end() {
    assertThat(YearMonth.of(2000, 2)).isStrictlyBetween(START, END);
  }

  @Test
  void should_fail_if_actual_is_equal_to_start() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(START).isStrictlyBetween(START, END));
    // THEN
    then(assertionError).hasMessage(shouldBeBetween(START, START, END, false, false).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_end() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(END).isStrictlyBetween(START, END));
    // THEN
    then(assertionError).hasMessage(shouldBeBetween(END, START, END, false, false).create());
  }

  @Test
  void should_fail_if_actual_is_before_start() {
    // GIVEN
    YearMonth actual = YearMonth.of(1999, 12);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isStrictlyBetween(START, END));
    // THEN
    then(assertionError).hasMessage(shouldBeBetween(actual, START, END, false, false).create());
  }

  @Test
  void should_fail_if_actual_is_after_end() {
    // GIVEN
    YearMonth actual = YearMonth.of(2000, 4);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isStrictlyBetween(START, END));
    // THEN
    then(assertionError).hasMessage(shouldBeBetween(actual, START, END, false, false).create());
  }

}
