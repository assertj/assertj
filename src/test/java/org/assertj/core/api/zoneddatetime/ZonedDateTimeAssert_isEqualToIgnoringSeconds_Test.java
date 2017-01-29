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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.zoneddatetime;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.AbstractZonedDateTimeAssert.NULL_DATE_TIME_PARAMETER_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.ZonedDateTime;

import org.assertj.core.api.BaseTest;
import org.junit.Test;


public class ZonedDateTimeAssert_isEqualToIgnoringSeconds_Test extends BaseTest {

  private final ZonedDateTime refDatetime = ZonedDateTime.of(2000, 1, 1, 23, 51, 0, 0, UTC);

  @Test
  public void should_pass_if_actual_is_equal_to_other_ignoring_second_fields() {
    assertThat(refDatetime).isEqualToIgnoringSeconds(refDatetime.plusSeconds(1));
  }

  @Test
  public void should_fail_if_actual_is_not_equal_to_given_datetime_with_second_ignored() {
    thrown.expectAssertionError(
        "%nExpecting:%n  <2000-01-01T23:51Z>%nto have same year, month, day, hour and minute as:%n  <2000-01-01T23:52Z>%nbut had not.");
    assertThat(refDatetime).isEqualToIgnoringSeconds(refDatetime.plusMinutes(1));
  }

  @Test
  public void should_fail_as_seconds_fields_are_different_even_if_time_difference_is_less_than_a_second() {
    thrown.expectAssertionError(
        "%nExpecting:%n  <2000-01-01T23:51Z>%nto have same year, month, day, hour and minute as:%n  <2000-01-01T23:50:59.999999999Z>%nbut had not.");
    assertThat(refDatetime).isEqualToIgnoringSeconds(refDatetime.minusNanos(1));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    ZonedDateTime actual = null;
    assertThat(actual).isEqualToIgnoringSeconds(ZonedDateTime.now());
  }

  @Test
  public void should_throw_error_if_given_datetime_is_null() {
    expectIllegalArgumentException(NULL_DATE_TIME_PARAMETER_MESSAGE);
    assertThat(refDatetime).isEqualToIgnoringSeconds(null);
  }

}
