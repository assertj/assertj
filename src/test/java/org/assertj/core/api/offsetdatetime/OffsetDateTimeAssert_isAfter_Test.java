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
package org.assertj.core.api.offsetdatetime;

import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.OffsetDateTime;

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
public class OffsetDateTimeAssert_isAfter_Test extends OffsetDateTimeAssertBaseTest {

  @Theory
  public void test_isAfter_assertion(OffsetDateTime referenceDate, OffsetDateTime dateBefore, OffsetDateTime dateEqual,
                                     OffsetDateTime dateAfter) {
    // GIVEN
    testAssumptions(referenceDate, dateBefore, dateEqual, dateAfter);
    // WHEN
    assertThat(dateAfter).isAfter(referenceDate);
    assertThat(dateAfter).isAfter(referenceDate.toString());
    // THEN
    verify_that_isAfter_assertion_fails_and_throws_AssertionError(referenceDate, referenceDate);
    verify_that_isAfter_assertion_fails_and_throws_AssertionError(dateBefore, referenceDate);
  }

  @Test
  public void test_isAfter_assertion_error_message() {
    thrown.expectAssertionError("%n" +
                                "Expecting:%n" +
                                "  <2000-01-01T03:00:05.123Z>%n" +
                                "to be strictly after:%n" +
                                "  <2000-01-01T03:00:05.123456789Z>");
    assertThat(parse("2000-01-01T03:00:05.123Z")).isAfter(parse("2000-01-01T03:00:05.123456789Z"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    OffsetDateTime actual = null;
    assertThat(actual).isAfter(OffsetDateTime.now());
  }

  @Test
  public void should_fail_if_dateTime_parameter_is_null() {
    expectException(IllegalArgumentException.class, "The OffsetDateTime to compare actual with should not be null");
    assertThat(OffsetDateTime.now()).isAfter((OffsetDateTime) null);
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    expectException(IllegalArgumentException.class,
                    "The String representing the OffsetDateTime to compare actual with should not be null");
    assertThat(OffsetDateTime.now()).isAfter((String) null);
  }

  private static void verify_that_isAfter_assertion_fails_and_throws_AssertionError(OffsetDateTime dateToCheck,
                                                                                    OffsetDateTime reference) {
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
