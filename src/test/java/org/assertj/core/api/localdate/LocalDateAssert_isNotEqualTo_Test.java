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

import java.time.LocalDate;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * Only test String based assertion (tests with {@link LocalDate} are already defined in assertj-core)
 */
@RunWith(Theories.class)
public class LocalDateAssert_isNotEqualTo_Test extends LocalDateAssertBaseTest {

  @Theory
  public void test_isNotEqualTo_assertion(LocalDate referenceDate) {
	// WHEN
	assertThat(referenceDate).isNotEqualTo(referenceDate.plusDays(1).toString());
	// THEN
	verify_that_isNotEqualTo_assertion_fails_and_throws_AssertionError(referenceDate);
  }

  @Test
  public void test_isNotEqualTo_assertion_error_message() {
    thrown.expectAssertionError("%nExpecting:%n <2000-01-05>%nnot to be equal to:%n <2000-01-05>%n");
    assertThat(LocalDate.of(2000, 1, 5)).isNotEqualTo(LocalDate.of(2000, 1, 5).toString());
  }

  @Test
  public void should_fail_if_date_as_string_parameter_is_null() {
	expectException(IllegalArgumentException.class,
	                "The String representing the LocalDate to compare actual with should not be null");
	assertThat(LocalDate.now()).isNotEqualTo((String) null);
  }

  private static void verify_that_isNotEqualTo_assertion_fails_and_throws_AssertionError(LocalDate reference) {
	try {
	  assertThat(reference).isNotEqualTo(reference.toString());
	} catch (AssertionError e) {
	  // AssertionError was expected
	  return;
	}
	fail("Should have thrown AssertionError");
  }

}
