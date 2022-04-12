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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.localdate;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

public class LocalDateAssert_hasMonthInt_Test {


  @Test
  void should_pass_if_actual_is_in_given_month() {
    // GIVEN
    LocalDate actual = LocalDate.of(2021, 2, 22);
    // WHEN/THEN
    then(actual).hasMonth(2);
  }

  @Test
  void should_pass_if_actual_is_in_given_month2() {
    // GIVEN
    LocalDate actual = LocalDate.of(2021, 2, 22);
    LocalDate other = LocalDate.of(2022, 2, 21);
    // WHEN/THEN
    then(actual).hasMonth(other.getMonthValue());
  }

  @Test
  void should_fail_if_actual_is_not_in_given_month() {
    // GIVEN
    LocalDate actual = LocalDate.of(2022, 1, 1);
    int wrongMonth = 12;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasMonth(wrongMonth));
    // THEN
    then(assertionError).hasMessage(shouldHaveDateField(actual, "month", wrongMonth).create());
  }

  @Test
  void should_fail_if_actual_is_not_in_given_month2() {
    // GIVEN
    LocalDate actual = LocalDate.of(2022, 1, 1);
    LocalDate other = LocalDate.of(2022, 2, 1);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasMonth(other.getMonthValue()));
    // THEN
    then(assertionError).hasMessage(shouldHaveDateField(actual, "month", other.getMonth().getValue()).create());
  }
  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDate actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasMonth(LocalDate.now().getMonthValue()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
