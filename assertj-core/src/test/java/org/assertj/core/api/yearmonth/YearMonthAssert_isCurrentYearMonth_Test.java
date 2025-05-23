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
package org.assertj.core.api.yearmonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeCurrentYearMonth.shouldBeCurrentYearMonth;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.YearMonth;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("YearMonthAssert isCurrentYearMonth")
class YearMonthAssert_isCurrentYearMonth_Test extends YearMonthAssertBaseTest {

  @Test
  void should_pass_if_actual_is_current_year_month() {
    assertThat(REFERENCE).isCurrentYearMonth();
  }

  @Test
  void should_fail_if_actual_is_before_current_year_month() {
    // WHEN
    ThrowingCallable code = () -> assertThat(BEFORE).isCurrentYearMonth();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeCurrentYearMonth(BEFORE).create());
  }

  @Test
  void should_fail_if_actual_is_after_current_year_month() {
    // WHEN
    ThrowingCallable code = () -> assertThat(AFTER).isCurrentYearMonth();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeCurrentYearMonth(AFTER).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    YearMonth actual = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(actual).isCurrentYearMonth();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

}
