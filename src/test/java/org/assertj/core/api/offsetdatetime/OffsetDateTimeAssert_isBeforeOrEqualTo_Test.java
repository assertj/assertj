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

import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.UTC;
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
public class OffsetDateTimeAssert_isBeforeOrEqualTo_Test extends OffsetDateTimeAssertBaseTest {

  @Theory
  public void test_isBeforeOrEqual_assertion(OffsetDateTime reference, OffsetDateTime dateBefore,
                                             OffsetDateTime dateEqual, OffsetDateTime dateAfter) {
    // GIVEN
    testAssumptions(reference, dateBefore, dateEqual, dateAfter);
    // WHEN
    assertThat(dateBefore).isBeforeOrEqualTo(reference);
    assertThat(dateBefore).isBeforeOrEqualTo(reference.toString());
    assertThat(dateEqual).isBeforeOrEqualTo(reference);
    assertThat(dateEqual).isBeforeOrEqualTo(reference.toString());
    // THEN
    verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(dateAfter, reference);
  }

  @Test
  public void test_isBeforeOrEqual_assertion_error_message() {
    thrown.expectAssertionError("%nExpecting:%n" +
                                "  <2000-01-05T03:00:05Z>%n" +
                                "to be before or equals to:%n" +
                                "  <1998-01-01T03:03:03Z>");
    assertThat(of(2000, 1, 5, 3, 0, 5, 0, UTC)).isBeforeOrEqualTo(of(1998, 1, 1, 3, 3, 3, 0, UTC));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    OffsetDateTime actual = null;
    assertThat(actual).isBeforeOrEqualTo(OffsetDateTime.now());
  }

  @Test
  public void should_fail_if_dateTime_parameter_is_null() {
    expectException(IllegalArgumentException.class, "The OffsetDateTime to compare actual with should not be null");
    assertThat(OffsetDateTime.now()).isBeforeOrEqualTo((OffsetDateTime) null);
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    expectException(IllegalArgumentException.class,
                    "The String representing the OffsetDateTime to compare actual with should not be null");
    assertThat(OffsetDateTime.now()).isBeforeOrEqualTo((String) null);
  }

  private static void verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(
                                                                                            OffsetDateTime dateToCheck,
                                                                                            OffsetDateTime reference) {
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
