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

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

/**
 * Tests specific to {@link org.assertj.core.api.ZonedDateTimeAssert#isEqualTo(ZonedDateTime)} that can't be
 * done in {@link org.assertj.core.api.AbstractAssert#isEqualTo(Object)} tests.
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class ZonedDateTimeAssert_isEqualTo_Test extends ZonedDateTimeAssertBaseTest {

  @Test
  public void isEqualTo_should_compare_datetimes_in_actual_timezone() {
    ZonedDateTime utcDateTime = ZonedDateTime.of(2013, 6, 10, 0, 0, 0, 0, UTC);
    ZoneId cestTimeZone = ZoneId.of("Europe/Berlin");
    ZonedDateTime cestDateTime = ZonedDateTime.of(2013, 6, 10, 2, 0, 0, 0, cestTimeZone);
    // datetime are equals in same timezone
    assertThat(utcDateTime).as("in UTC time zone").isEqualTo(cestDateTime);
    assertThat(cestDateTime).as("in CEST time zone").isEqualTo(utcDateTime);
  }

  @Test
  public void should_pass_if_both_are_null() {
    assertThat((ZonedDateTime) null).isEqualTo((ZonedDateTime) null);
  }

}
