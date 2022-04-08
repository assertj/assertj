package org.assertj.core.api.localdate;
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
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;


public class LocalDateAssert_hasYear_Test {
  private final LocalDate actual = LocalDate.of(2022,1,1);

  @Test
  void should_pass_if_actual_is_in_year_2022() {
    assertThat(actual).hasYear(2022);
  }

  @Test
  void should_fail_if_actual_is_not_in_year_2021() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasYear(2021));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting year of:%n  " + actual +
      " (java.time.LocalDateTime)%nto be year:%n  2021%n"));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDate actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasYear(LocalDate.now().getYear()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

//  @Test
//  void should_throw_error_if_given_localdate_is_null() {
//    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(refLocalDate).isEqualToIgnoringHours(null))
//      .withMessage(NULL_LOCAL_DATE_TIME_PARAMETER_MESSAGE);
//  }

}