/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api.zoneddatetime;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.ZonedDateTimeAssert.NULL_DATE_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.assertj.core.api.DateTimeBaseTest;
import org.junit.Test;


public class ZonedDateTimeAssert_isEqualToIgnoringHours_Test extends DateTimeBaseTest {

  private final ZonedDateTime refDatetime = ZonedDateTime.of(2000, 1, 2, 0, 0, 0, 0, UTC);

  @Test
  public void should_pass_if_actual_is_equal_to_other_ignoring_hours() {
    assertThat(refDatetime).isEqualToIgnoringHours(refDatetime.plusHours(1));
  }
  
  @Test
  public void should_pass_if_actual_is_equal_to_other_ignoring_hours_in_different_timezone() {
      ZonedDateTime utcDateTime = ZonedDateTime.of(2013, 6, 10, 0, 0, 0, 0, UTC);
    ZoneId cestTimeZone = ZoneId.of("Europe/Berlin");
    // utcDateTime = new DateTime(2013, 6, 10, 2, 0, cestTimeZone)  
    assertThat(utcDateTime).isEqualToIgnoringHours(ZonedDateTime.of(2013, 6, 10, 5, 0, 0, 0, cestTimeZone));
    // new DateTime(2013, 6, 11, 1, 0, cestTimeZone) =  DateTime(2013, 6, 10, 23, 0, DateTimeZone.UTC)
    assertThat(utcDateTime).isEqualToIgnoringHours(ZonedDateTime.of(2013, 6, 11, 1, 0, 0, 0, cestTimeZone));
    try {
      // DateTime(2013, 6, 10, 0, 0, cestTimeZone) =  DateTime(2013, 6, 9, 22, 0, DateTimeZone.UTC) 
      assertThat(utcDateTime).isEqualToIgnoringHours(ZonedDateTime.of(2013, 6, 10, 0, 0, 0, 0, cestTimeZone));
    } catch (AssertionError e) {
      return;
    }
    fail("Should have thrown AssertionError");
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_given_datetime_with_hours_ignored() {
    try {
      assertThat(refDatetime).isEqualToIgnoringHours(refDatetime.minusHours(1));
    } catch (AssertionError e) {
      assertThat(e.getMessage())
          .isEqualTo(
              "\nExpecting:\n  <2000-01-02T00:00Z>\nto have same year, month and day as:\n  <2000-01-01T23:00Z>\nbut had not.");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_as_hours_fields_are_different_even_if_time_difference_is_less_than_a_hour() {
    try {
      assertThat(refDatetime).isEqualToIgnoringHours(refDatetime.minusNanos(1));
    } catch (AssertionError e) {
      assertThat(e.getMessage())
          .isEqualTo(
              "\nExpecting:\n  <2000-01-02T00:00Z>\nto have same year, month and day as:\n  <2000-01-01T23:59:59.999999999Z>\nbut had not.");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    ZonedDateTime actual = null;
    assertThat(actual).isEqualToIgnoringHours(ZonedDateTime.now());
  }

  @Test
  public void should_throw_error_if_given_datetime_is_null() {
    expectIllegalArgumentException(NULL_DATE_TIME_PARAMETER_MESSAGE);
    assertThat(refDatetime).isEqualToIgnoringHours(null);
  }

}
