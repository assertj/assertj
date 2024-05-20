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
package org.assertj.core.api.zoneddatetime;

import static java.lang.String.format;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.AbstractZonedDateTimeAssert.NULL_DATE_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class ZonedDateTimeAssert_isEqualToIgnoringHours_Test {

  private final ZonedDateTime refDatetime = ZonedDateTime.of(2000, 1, 2, 0, 0, 0, 0, UTC);

  @Test
  void should_pass_if_actual_is_equal_to_other_ignoring_hours() {
    assertThat(refDatetime).isEqualToIgnoringHours(refDatetime.plusHours(1));
  }

  @Test
  void should_pass_if_actual_is_equal_to_other_ignoring_hours_in_different_timezone() {
    // GIVEN
    ZonedDateTime utcDateTime = ZonedDateTime.of(2013, 6, 10, 0, 0, 0, 0, UTC);
    ZoneId cestTimeZone = ZoneId.of("Europe/Berlin");
    // new DateTime(2013, 6, 10, 5, 0, cestTimeZone) = DateTime(2013, 6, 10, 3, 0, DateTimeZone.UTC)
    assertThat(utcDateTime).isEqualToIgnoringHours(ZonedDateTime.of(2013, 6, 10, 5, 0, 0, 0, cestTimeZone));
    // new DateTime(2013, 6, 11, 1, 0, cestTimeZone) = DateTime(2013, 6, 10, 23, 0, DateTimeZone.UTC)
    assertThat(utcDateTime).isEqualToIgnoringHours(ZonedDateTime.of(2013, 6, 11, 1, 0, 0, 0, cestTimeZone));
    // DateTime(2013, 6, 10, 0, 0, cestTimeZone) = DateTime(2013, 6, 9, 22, 0, DateTimeZone.UTC)
    ZonedDateTime expected = ZonedDateTime.of(2013, 6, 10, 0, 0, 0, 0, cestTimeZone);

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(utcDateTime).isEqualToIgnoringHours(expected));

    // THEN
    assertThat(assertionError).hasMessage(format("%n" +
                                                 "Expecting actual:%n" +
                                                 "  2013-06-10T00:00Z (java.time.ZonedDateTime)%n" +
                                                 "to have same year, month and day as:%n" +
                                                 "  2013-06-09T22:00Z (java.time.ZonedDateTime)%n" +
                                                 "but had not."));
  }

  @Test
  void should_fail_if_actual_is_not_equal_to_given_datetime_with_hours_ignored() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(refDatetime).isEqualToIgnoringHours(refDatetime.minusHours(1)));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n  2000-01-02T00:00Z (java.time.ZonedDateTime)%nto have same year, month and day as:%n  2000-01-01T23:00Z (java.time.ZonedDateTime)%nbut had not."));
  }

  @Test
  void should_fail_as_hours_fields_are_different_even_if_time_difference_is_less_than_a_hour() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(refDatetime).isEqualToIgnoringHours(refDatetime.minusNanos(1)));
    // THEN
    then(assertionError).hasMessage(format(
                                           "%nExpecting actual:%n  2000-01-02T00:00Z (java.time.ZonedDateTime)%nto have same year, month and day as:%n  2000-01-01T23:59:59.999999999Z (java.time.ZonedDateTime)%nbut had not."));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    ZonedDateTime actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEqualToIgnoringHours(ZonedDateTime.now()));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_given_datetime_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(refDatetime).isEqualToIgnoringHours(null))
                                        .withMessage(NULL_DATE_TIME_PARAMETER_MESSAGE);
  }

}
