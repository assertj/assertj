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

import static java.time.LocalDate.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class LocalDateAssert_isToday_Test extends LocalDateAssertBaseTest {

  @Theory
  public void test_isToday_assertion(LocalDate referenceDate, LocalDate dateBefore, LocalDate dateAfter) {
    // GIVEN
    testAssumptions(referenceDate, dateBefore, dateAfter);
    // WHEN
    assertThat(referenceDate).isToday();
    // THEN
    verify_that_isToday_assertion_fails_and_throws_AssertionError(dateBefore);
    verify_that_isToday_assertion_fails_and_throws_AssertionError(dateAfter);
  }

  @Test
  public void test_isToday_assertion_error_message() {
    thrown.expectAssertionError("%n" +
                                "Expecting:%n" +
                                " <2000-01-01>%n" +
                                "to be today but was not.");
    assertThat(parse("2000-01-01")).isToday();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    expectException(AssertionError.class, actualIsNull());
    LocalDate actual = null;
    assertThat(actual).isToday();
  }

  private static void verify_that_isToday_assertion_fails_and_throws_AssertionError(LocalDate dateToCheck) {
    try {
      assertThat(dateToCheck).isToday();
    } catch (AssertionError e) {
      // AssertionError was expected.
      return;
    }
    fail(String.format("%s should have thrown AssertionError", dateToCheck));
  }

}
