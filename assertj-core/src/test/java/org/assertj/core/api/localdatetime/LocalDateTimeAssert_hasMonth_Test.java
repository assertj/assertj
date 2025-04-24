/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.localdatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveMonth;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDateTime;
import java.time.Month;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class LocalDateTimeAssert_hasMonth_Test {

  @Test
  void should_fail_if_given_month_is_null() {
    // GIVEN
    LocalDateTime actual = LocalDateTime.now();
    Month month = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(actual).hasMonth(month);
    // Then
    assertThatIllegalArgumentException().isThrownBy(code)
                                        .withMessage("The given Month should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDateTime actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasMonth(Month.MAY));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_in_given_month() {
    // GIVEN
    LocalDateTime actual = LocalDateTime.of(2020, Month.FEBRUARY, 2, 3, 4, 5);
    Month month = Month.JUNE;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasMonth(month));
    // THEN
    then(assertionError).hasMessage(shouldHaveMonth(actual, month).create());
  }

  @Test
  void should_pass_if_actual_is_in_given_month() {
    // GIVEN
    LocalDateTime actual = LocalDateTime.of(2022, Month.APRIL, 16, 20, 18, 59);
    // WHEN/THEN
    then(actual).hasMonth(Month.APRIL);
  }
}
