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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.zoneddatetime;

import static java.lang.String.format;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.assertj.core.util.temporal.DefaultZonedDateTimeComparator;
import org.junit.jupiter.api.Test;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class ZonedDateTimeAssert_isAfter_Test extends ZonedDateTimeAssertBaseTest {

  @Test
  public void test_isAfter_assertion() {
    // WHEN
    assertThat(AFTER).isAfter(REFERENCE);
    assertThat(AFTER).isAfter(REFERENCE.format(DateTimeFormatter.ISO_DATE_TIME));
    // THEN
    verify_that_isAfter_assertion_fails_and_throws_AssertionError(REFERENCE, REFERENCE);
    verify_that_isAfter_assertion_fails_and_throws_AssertionError(BEFORE, REFERENCE);
  }

  @Test
  public void isAfter_should_compare_datetimes_in_actual_timezone() {
    ZonedDateTime utcDateTime = ZonedDateTime.of(2013, 6, 10, 0, 0, 0, 0, UTC);
    ZoneId cestTimeZone = ZoneId.of("Europe/Berlin");
    ZonedDateTime cestDateTime = ZonedDateTime.of(2013, 6, 10, 1, 0, 0, 0, cestTimeZone);
    // utcDateTime > cestDateTime
    assertThat(utcDateTime).as("in UTC time zone").isAfter(cestDateTime);
    try {
      ZonedDateTime equalsCestDateTime = ZonedDateTime.of(2013, 6, 10, 2, 0, 0, 0, cestTimeZone);
      assertThat(utcDateTime).as("in UTC time zone").isAfter(equalsCestDateTime);
    } catch (AssertionError e) {
      return;
    }
    fail("Should have thrown AssertionError");
  }

  @Test
  public void test_isAfter_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(ZonedDateTime.of(2000, 1, 5, 3, 0, 5, 0, UTC)).isAfter(ZonedDateTime.of(2012, 1, 1, 3, 3, 3, 0, UTC)))
                                                   .withMessage(format("%nExpecting:%n" +
                                                     "  <2000-01-05T03:00:05Z>%n" +
                                                     "to be strictly after:%n" +
                                                     "  <2012-01-01T03:03:03Z>" +
                                                     "when comparing values using '%s'",
                                                     DefaultZonedDateTimeComparator.getInstance()));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      ZonedDateTime actual = null;
      assertThat(actual).isAfter(ZonedDateTime.now());
    }).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_dateTime_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(ZonedDateTime.now()).isAfter((ZonedDateTime) null))
                                        .withMessage("The ZonedDateTime to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(ZonedDateTime.now()).isAfter((String) null))
                                        .withMessage("The String representing the ZonedDateTime to compare actual with should not be null");
  }

  private static void verify_that_isAfter_assertion_fails_and_throws_AssertionError(ZonedDateTime dateToCheck,
      ZonedDateTime reference) {
    try {
      assertThat(dateToCheck).isAfter(reference);
    } catch (AssertionError e) {
      // AssertionError was expected, test same assertion with String based parameter
      try {
        assertThat(dateToCheck).isAfter(reference.toString());
      } catch (AssertionError e2) {
        // AssertionError was expected (again)
        return;
      }
    }
    fail("Should have thrown AssertionError");
  }

}
