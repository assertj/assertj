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
import static org.assertj.core.api.AbstractZonedDateTimeAssert.COMPARATOR_DESC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

/**
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class ZonedDateTimeAssert_isBeforeOrEqualTo_Test extends ZonedDateTimeAssertBaseTest {

  @Test
  public void test_isBeforeOrEqual_assertion() {
	// WHEN
	assertThat(BEFORE).isBeforeOrEqualTo(REFERENCE);
	assertThat(BEFORE).isBeforeOrEqualTo(REFERENCE.toString());
	assertThat(REFERENCE).isBeforeOrEqualTo(REFERENCE);
	assertThat(REFERENCE).isBeforeOrEqualTo(REFERENCE.toString());
	// THEN
	verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(AFTER, REFERENCE);
  }

  @Test
  public void isBeforeOrEqualTo_should_compare_datetimes_in_actual_timezone() {
	ZonedDateTime utcDateTime = ZonedDateTime.of(2013, 6, 10, 0, 0, 0, 0, ZoneOffset.UTC);
	ZoneId cestTimeZone = ZoneId.of("Europe/Berlin");
	ZonedDateTime cestDateTime1 = ZonedDateTime.of(2013, 6, 10, 2, 0, 0, 0, cestTimeZone);
	ZonedDateTime cestDateTime2 = ZonedDateTime.of(2013, 6, 10, 3, 0, 0, 0, cestTimeZone);
	// utcDateTime = cestDateTime1
	assertThat(utcDateTime).as("in UTC time zone").isBeforeOrEqualTo(cestDateTime1);
	// utcDateTime < cestDateTime2
	assertThat(utcDateTime).as("in UTC time zone").isBeforeOrEqualTo(cestDateTime2);
  }

  @Test
  public void test_isBeforeOrEqual_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(ZonedDateTime.of(2000, 1, 5, 3, 0, 5, 0,
                                                                                                 UTC)).isBeforeOrEqualTo(ZonedDateTime.of(1998,
                                                                                                                                          1,
                                                                                                                                          1,
                                                                                                                                          3,
                                                                                                                                          3,
                                                                                                                                          3,
                                                                                                                                          0,
                                                                                                                                          UTC)))
                                                   .withMessage(format("%n" +
                                                                       "Expecting:%n" +
                                                                       "  <2000-01-05T03:00:05Z>%n" +
                                                                       "to be before or equals to:%n" +
                                                                       "  <1998-01-01T03:03:03Z>" +
                                                                       "when comparing values using '%s'", COMPARATOR_DESC));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      ZonedDateTime actual = null;
      assertThat(actual).isBeforeOrEqualTo(ZonedDateTime.now());
    }).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_dateTime_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(ZonedDateTime.now()).isBeforeOrEqualTo((ZonedDateTime) null))
                                        .withMessage("The ZonedDateTime to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(ZonedDateTime.now()).isBeforeOrEqualTo((String) null))
                                        .withMessage("The String representing the ZonedDateTime to compare actual with should not be null");
  }

  private static void verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(ZonedDateTime dateToCheck,
	                                                                                        ZonedDateTime reference) {
	try {
	  assertThat(dateToCheck).isBeforeOrEqualTo(reference);
	} catch (AssertionError e) {
	  // AssertionError was expected, test same assertion with String based parameter
	  try {
		assertThat(dateToCheck).isBeforeOrEqualTo(reference.toString());
	  } catch (AssertionError e2) {
		// AssertionError was expected (again)
		return;
	  }
	}
	fail("Should have thrown AssertionError");
  }

}
