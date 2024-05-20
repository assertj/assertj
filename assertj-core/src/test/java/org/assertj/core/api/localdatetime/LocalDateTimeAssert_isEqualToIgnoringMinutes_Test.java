/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.localdatetime;

import static java.lang.String.format;
import static org.assertj.core.api.AbstractLocalDateTimeAssert.NULL_LOCAL_DATE_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class LocalDateTimeAssert_isEqualToIgnoringMinutes_Test {

  private final LocalDateTime refLocalDateTime = LocalDateTime.of(2000, 1, 1, 23, 0, 0, 0);

  @Test
  void should_pass_if_actual_is_equal_to_other_ignoring_minute_fields() {
    assertThat(refLocalDateTime).isEqualToIgnoringMinutes(refLocalDateTime.plusMinutes(1));
  }

  @Test
  void should_fail_if_actual_is_not_equal_to_given_localdatetime_with_minute_ignored() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(refLocalDateTime).isEqualToIgnoringMinutes(refLocalDateTime.minusMinutes(1)));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n  2000-01-01T23:00 (java.time.LocalDateTime)%nto have same year, month, day and hour as:%n  2000-01-01T22:59 (java.time.LocalDateTime)%nbut had not."));
  }

  @Test
  void should_fail_as_minutes_fields_are_different_even_if_time_difference_is_less_than_a_minute() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(refLocalDateTime).isEqualToIgnoringMinutes(refLocalDateTime.minusNanos(1)));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n  2000-01-01T23:00 (java.time.LocalDateTime)%nto have same year, month, day and hour as:%n  2000-01-01T22:59:59.999999999 (java.time.LocalDateTime)%nbut had not."));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    LocalDateTime actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualToIgnoringMinutes(LocalDateTime.now()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_given_localdatetime_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(refLocalDateTime).isEqualToIgnoringMinutes(null))
                                        .withMessage(NULL_LOCAL_DATE_TIME_PARAMETER_MESSAGE);
  }

}
