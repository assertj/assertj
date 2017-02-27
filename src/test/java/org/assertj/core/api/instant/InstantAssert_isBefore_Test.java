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
package org.assertj.core.api.instant;


import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;

@RunWith(Theories.class)
public class InstantAssert_isBefore_Test extends InstantAssertBaseTest {

  @Theory
  public void test_isBefore_assertion(Instant referenceDate, Instant dateBefore, Instant dateAfter) {
    // GIVEN
    testAssumptions(referenceDate, dateBefore, dateAfter);
    // WHEN
    assertThat(dateBefore).isBefore(referenceDate);
    // THEN
    verify_that_isBefore_assertion_fails_and_throws_AssertionError(referenceDate, referenceDate);
    verify_that_isBefore_assertion_fails_and_throws_AssertionError(dateAfter, referenceDate);
  }

  @Test
  public void test_isBefore_assertion_error_message() {
    Instant instantReference = Instant.parse("2007-12-03T10:15:30.00Z");
    Instant instantAfter = Instant.parse("2007-12-03T10:15:35.00Z");
    thrown.expectAssertionError("%nExpecting:%n  <2007-12-03T10:15:35Z>%nto be strictly before:%n  <2007-12-03T10:15:30Z>");
    assertThat(instantAfter).isBefore(instantReference);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    Instant actual = null;
    assertThat(actual).isBefore(Instant.now());
  }

  @Test
  public void should_fail_if_date_parameter_is_null() {
    expectException(IllegalArgumentException.class, "The Instant to compare actual with should not be null");
    assertThat(Instant.now()).isBefore((Instant) null);
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
    expectException(IllegalArgumentException.class,
      "The String representing the Instant to compare actual with should not be null");
    assertThat(Instant.now()).isBefore((String) null);
  }

  private static void verify_that_isBefore_assertion_fails_and_throws_AssertionError(Instant dateToTest,
                                                                                     Instant reference) {
    try {
      assertThat(dateToTest).isBefore(reference);
    } catch (AssertionError e) {
      // AssertionError was expected, test same assertion with String based parameter
      try {
        assertThat(dateToTest).isBefore(reference.toString());
      } catch (AssertionError e2) {
        // AssertionError was expected (again)
        return;
      }
    }
    fail("Should have thrown AssertionError");
  }

}
