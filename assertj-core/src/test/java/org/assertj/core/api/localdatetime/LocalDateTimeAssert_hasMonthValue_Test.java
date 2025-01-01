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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class LocalDateTimeAssert_hasMonthValue_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDateTime actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasMonthValue(1));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_not_same_month_value() {
    // GIVEN
    LocalDateTime actual = LocalDateTime.of(2030, 8, 5, 0, 59, 59);
    int monthValue = 10;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasMonthValue(monthValue));
    // THEN
    then(assertionError).hasMessage(shouldHaveDateField(actual, "month", monthValue).create());
  }

  @Test
  void should_pass_if_actual_has_same_month_value() {
    // GIVEN
    LocalDateTime actual = LocalDateTime.of(2000, 3, 21, 15, 30, 0);
    // WHEN/THEN
    then(actual).hasMonthValue(3);
  }
}
