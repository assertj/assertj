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
package org.assertj.core.api.localdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDate;

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
public class LocalDateAssert_isBeforeOrEqualTo_Test extends LocalDateAssertBaseTest {

  @Theory
  public void test_isBeforeOrEqual_assertion(LocalDate referenceDate, LocalDate dateBefore,
      LocalDate dateAfter) {
    // GIVEN
    testAssumptions(referenceDate, dateBefore, dateAfter);
    // WHEN
    assertThat(dateBefore).isBeforeOrEqualTo(referenceDate);
    assertThat(referenceDate).isBeforeOrEqualTo(referenceDate);
    // THEN
    verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(dateAfter, referenceDate);
  }

  @Test
  public void test_isBeforeOrEqual_assertion_error_message() {
    thrown.expectAssertionError("%nExpecting:%n  <2000-01-05>%nto be before or equals to:%n  <1998-01-01>");
    assertThat(LocalDate.of(2000, 1, 5)).isBeforeOrEqualTo(LocalDate.of(1998, 1, 1));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    LocalDate actual = null;
    assertThat(actual).isBeforeOrEqualTo(LocalDate.now());
  }

  @Test
  public void should_fail_if_date_parameter_is_null() {
    expectException(IllegalArgumentException.class, "The LocalDate to compare actual with should not be null");
    assertThat(LocalDate.now()).isBeforeOrEqualTo((LocalDate) null);
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
    expectException(IllegalArgumentException.class,
        "The String representing the LocalDate to compare actual with should not be null");
    assertThat(LocalDate.now()).isBeforeOrEqualTo((String) null);
  }

  private static void verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(LocalDate dateToCheck,
      LocalDate reference) {
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
