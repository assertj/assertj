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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@RunWith(Theories.class)
public class ZonedDateTimeAssert_isAfterOrEqualTo_Test extends ZonedDateTimeAssertBaseTest {

  @Theory
  public void test_isAfterOrEqual_assertion(ZonedDateTime referenceDate, ZonedDateTime dateBefore, ZonedDateTime dateAfter) {
    // GIVEN
    testAssumptions(referenceDate, dateBefore, dateAfter);
    // WHEN
    assertThat(dateAfter).isAfterOrEqualTo(referenceDate);
    assertThat(dateAfter).isAfterOrEqualTo(referenceDate.toString());
    assertThat(referenceDate).isAfterOrEqualTo(referenceDate);
    assertThat(referenceDate).isAfterOrEqualTo(referenceDate.toString());
    // THEN
    verify_that_isAfterOrEqual_assertion_fails_and_throws_AssertionError(dateBefore, referenceDate);
  }

  @Test
  public void isAfterOrEqualTo_should_compare_datetimes_in_actual_timezone() {
    ZonedDateTime utcDateTime = ZonedDateTime.of(2013, 6, 10, 0, 0, 0, 0, UTC);
    ZoneId cestTimeZone = ZoneId.of("Europe/Berlin");
    ZonedDateTime cestDateTime1 = ZonedDateTime.of(2013, 6, 10, 2, 0, 0, 0, cestTimeZone);
    ZonedDateTime cestDateTime2 = ZonedDateTime.of(2013, 6, 10, 1, 0, 0, 0, cestTimeZone);
    // utcDateTime = cestDateTime1
    assertThat(utcDateTime).as("in UTC time zone").isAfterOrEqualTo(cestDateTime1);
    // utcDateTime > cestDateTime2
    assertThat(utcDateTime).as("in UTC time zone").isAfterOrEqualTo(cestDateTime2);
  }

  @Test
  public void test_isAfterOrEqual_assertion_error_message() {
    thrown.expectAssertionError("%nExpecting:%n  <2000-01-05T03:00:05Z>%nto be after or equals to:%n  <2012-01-01T03:03:03Z>");
    assertThat(ZonedDateTime.of(2000, 1, 5, 3, 0, 5, 0, UTC)).isAfterOrEqualTo(ZonedDateTime.of(2012, 1, 1, 3, 3, 3, 0, UTC));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    ZonedDateTime actual = null;
    assertThat(actual).isAfterOrEqualTo(ZonedDateTime.now());
  }

  @Test
  public void should_fail_if_dateTime_parameter_is_null() {
    expectException(IllegalArgumentException.class, "The ZonedDateTime to compare actual with should not be null");
    assertThat(ZonedDateTime.now()).isAfterOrEqualTo((ZonedDateTime) null);
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    expectException(IllegalArgumentException.class,
                    "The String representing the ZonedDateTime to compare actual with should not be null");
    assertThat(ZonedDateTime.now()).isAfterOrEqualTo((String) null);
  }

  private static void verify_that_isAfterOrEqual_assertion_fails_and_throws_AssertionError(ZonedDateTime dateToCheck,
      ZonedDateTime reference) {
    try {
      assertThat(dateToCheck).isAfterOrEqualTo(reference);
    } catch (AssertionError e) {
      // AssertionError was expected, test same assertion with String based parameter
      try {
        assertThat(dateToCheck).isAfterOrEqualTo(reference.toString());
      } catch (AssertionError e2) {
        // AssertionError was expected (again)
        return;
      }
    }
    fail("Should have thrown AssertionError");
  }

}
