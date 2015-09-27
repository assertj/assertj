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
package org.assertj.core.api.localtime;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.time.LocalTime;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * Only test String based assertion (tests with {@link LocalTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@RunWith(Theories.class)
public class LocalTimeAssert_isIn_Test extends LocalTimeAssertBaseTest {

  @Theory
  public void test_isIn_assertion(LocalTime referenceTime) {
	// WHEN
	assertThat(referenceTime).isIn(referenceTime.toString(), referenceTime.plusHours(1).toString());
	// THEN
	verify_that_isIn_assertion_fails_and_throws_AssertionError(referenceTime);
  }

  @Test
  public void test_isIn_assertion_error_message() {
	try {
	  assertThat(LocalTime.of(3, 0, 5)).isIn("03:03:03");
	} catch (AssertionError e) {
	  assertThat(e).hasMessage(format("%n" +
		                              "Expecting:%n" +
		                              " <03:00:05>%n" +
		                              "to be in:%n" +
		                              " <[03:03:03]>%n"));
	  return;
	}
	fail("Should have thrown AssertionError");
  }

  @Test
  public void should_fail_if_timeTimes_as_string_array_parameter_is_null() {
	expectException(IllegalArgumentException.class, "The given LocalTime array should not be null");
	assertThat(LocalTime.now()).isIn((String[]) null);
  }

  @Test
  public void should_fail_if_timeTimes_as_string_array_parameter_is_empty() {
	expectException(IllegalArgumentException.class, "The given LocalTime array should not be empty");
	assertThat(LocalTime.now()).isIn(new String[0]);
  }

  private static void verify_that_isIn_assertion_fails_and_throws_AssertionError(LocalTime reference) {
	try {
	  assertThat(reference).isIn(reference.plusHours(1).toString(), reference.plusHours(2).toString());
	} catch (AssertionError e) {
	  // AssertionError was expected
	  return;
	}
	fail("Should have thrown AssertionError");
  }

}
