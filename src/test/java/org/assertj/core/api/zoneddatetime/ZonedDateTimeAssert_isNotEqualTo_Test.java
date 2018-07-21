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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.zoneddatetime;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

/**
 * Tests specific to {@link org.assertj.core.api.ZonedDateTimeAssert#isNotEqualTo(ZonedDateTime)} that can't be
 * done in {@link org.assertj.core.api.AbstractAssert#isNotEqualTo(Object)} tests.
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class ZonedDateTimeAssert_isNotEqualTo_Test extends ZonedDateTimeAssertBaseTest {

  @Test
  public void isNotEqualTo_should_compare_datetimes_in_actual_timezone() {
    ZonedDateTime utcDateTime = ZonedDateTime.of(2013, 6, 10, 2, 0, 0, 0, ZoneOffset.UTC);
    ZoneId cestTimeZone = ZoneId.of("Europe/Berlin");
    ZonedDateTime cestDateTime = ZonedDateTime.of(2013, 6, 10, 2, 0, 0, 0, cestTimeZone);
    // datetime are not equals because they are in different timezone
    assertThat(utcDateTime).as("in UTC time zone").isNotEqualTo(cestDateTime);
    assertThat(cestDateTime).as("in CEST time zone").isNotEqualTo(utcDateTime);
  }

  @Test
  public void should_pass_if_actual_dateTime_is_null_and_expected_dateTime_as_string_is_not() {
    assertThat((ZonedDateTime) null).isNotEqualTo("2000-01-01T01:00:00+01:00");
  }

  @Test
  public void should_pass_if_actual_dateTime_is_null_and_expected_dateTime_is_not() {
    assertThat((ZonedDateTime) null).isNotEqualTo(ZonedDateTime.now());
  }

  @Test
  public void should_pass_if_dateTime_as_ZoneDateTime_is_null() {
    assertThat(ZonedDateTime.now()).isNotEqualTo((ZonedDateTime) null);
  }

}
