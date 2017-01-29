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
package org.assertj.core.api.localtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.LocalTime;

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
public class LocalTimeAssert_isBeforeOrEqualTo_Test extends LocalTimeAssertBaseTest {

  @Theory
  public void test_isBeforeOrEqual_assertion(LocalTime referenceTime, LocalTime timeBefore,
	                                         LocalTime timeAfter) {
	// GIVEN
	testAssumptions(referenceTime, timeBefore, timeAfter);
	// WHEN
	assertThat(timeBefore).isBeforeOrEqualTo(referenceTime);
	assertThat(referenceTime).isBeforeOrEqualTo(referenceTime);
	// THEN
	verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(timeAfter, referenceTime);
  }

  @Test
  public void test_isBeforeOrEqual_assertion_error_message() {
    thrown.expectAssertionError("%n" +
                                "Expecting:%n" +
                                "  <03:00:05>%n" +
                                "to be before or equals to:%n" +
                                "  <03:00:04>");
    assertThat(LocalTime.of(3, 0, 5)).isBeforeOrEqualTo(LocalTime.of(3, 0, 4));
  }

  @Test
  public void should_fail_if_actual_is_null() {
	expectException(AssertionError.class, actualIsNull());
	LocalTime actual = null;
	assertThat(actual).isBeforeOrEqualTo(LocalTime.now());
  }

  @Test
  public void should_fail_if_timeTime_parameter_is_null() {
	expectException(IllegalArgumentException.class, "The LocalTime to compare actual with should not be null");
	assertThat(LocalTime.now()).isBeforeOrEqualTo((LocalTime) null);
  }

  @Test
  public void should_fail_if_timeTime_as_string_parameter_is_null() {
	expectException(IllegalArgumentException.class,
	                "The String representing the LocalTime to compare actual with should not be null");
	assertThat(LocalTime.now()).isBeforeOrEqualTo((String) null);
  }

  private static void verify_that_isBeforeOrEqual_assertion_fails_and_throws_AssertionError(LocalTime timeToCheck,
	                                                                                        LocalTime reference) {
	try {
	  assertThat(timeToCheck).isBeforeOrEqualTo(reference);
	} catch (AssertionError e) {
	  // AssertionError was expected, test same assertion with String based parameter
	  try {
		assertThat(timeToCheck).isBeforeOrEqualTo(reference.toString());
	  } catch (AssertionError e2) {
		// AssertionError was expected (again)
		return;
	  }
	}
	fail("Should have thrown AssertionError");
  }

}
