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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api.localdatetime;

import static java.time.LocalDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalDateTime;

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
public class LocalDateTimeAssert_isAfter_Test extends LocalDateTimeAssertBaseTest {

  @Theory
  public void test_isAfter_assertion(LocalDateTime referenceDate, LocalDateTime dateBefore, LocalDateTime dateAfter) {
	// GIVEN
	testAssumptions(referenceDate, dateBefore, dateAfter);
	// WHEN
	assertThat(dateAfter).isAfter(referenceDate);
	assertThat(dateAfter).isAfter(referenceDate.toString());
	// THEN
	verify_that_isAfter_assertion_fails_and_throws_AssertionError(referenceDate, referenceDate);
	verify_that_isAfter_assertion_fails_and_throws_AssertionError(dateBefore, referenceDate);
  }

  @Test
  public void test_isAfter_assertion_error_message() {
	try {
	  assertThat(parse("2000-01-01T03:00:05.123")).isAfter(parse("2000-01-01T03:00:05.123456789"));
	} catch (AssertionError e) {
	  assertThat(e).hasMessage(String.format("%n" +
		                       "Expecting:%n" +
		                       "  <2000-01-01T03:00:05.123>%n" +
		                       "to be strictly after:%n" +
		                       "  <2000-01-01T03:00:05.123456789>"));
	  return;
	}
	fail("Should have thrown AssertionError");
  }

  @Test
  public void should_fail_if_actual_is_null() {
	expectException(AssertionError.class, actualIsNull());
	LocalDateTime actual = null;
	assertThat(actual).isAfter(LocalDateTime.now());
  }

  @Test
  public void should_fail_if_dateTime_parameter_is_null() {
	expectException(IllegalArgumentException.class, "The LocalDateTime to compare actual with should not be null");
	assertThat(LocalDateTime.now()).isAfter((LocalDateTime) null);
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
	expectException(IllegalArgumentException.class,
	                "The String representing the LocalDateTime to compare actual with should not be null");
	assertThat(LocalDateTime.now()).isAfter((String) null);
  }

  private static void verify_that_isAfter_assertion_fails_and_throws_AssertionError(LocalDateTime dateToCheck,
	                                                                                LocalDateTime reference) {
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
