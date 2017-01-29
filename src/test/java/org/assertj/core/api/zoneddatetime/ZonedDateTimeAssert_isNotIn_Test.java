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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.Test;

/**
 * Tests specific to {@link org.assertj.core.api.ZonedDateTimeAssert#isNotIn(ZonedDateTime...)} that can't be
 * done in {@link org.assertj.core.api.AbstractAssert#isNotIn(Object...)} tests.
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class ZonedDateTimeAssert_isNotIn_Test extends ZonedDateTimeAssertBaseTest {

  @Test
  public void isNotIn_should_compare_datetimes_in_actual_timezone() {
    ZonedDateTime utcDateTime = ZonedDateTime.of(2013, 6, 10, 0, 0, 0, 0, ZoneOffset.UTC);
    ZoneId cestTimeZone = ZoneId.of("Europe/Berlin");
    ZonedDateTime cestDateTime = ZonedDateTime.of(2013, 6, 10, 0, 0, 0, 0, cestTimeZone);
    // cestDateTime and utcDateTime are not equals in same timezone
    assertThat(utcDateTime).isNotIn(cestDateTime, ZonedDateTime.now());
  }
}
