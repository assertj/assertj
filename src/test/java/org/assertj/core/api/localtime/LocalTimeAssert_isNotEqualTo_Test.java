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
package org.assertj.core.api.localtime;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.fail;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link LocalTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class LocalTimeAssert_isNotEqualTo_Test extends LocalTimeAssertBaseTest {

  @Test
  public void test_isNotEqualTo_assertion() {
	// WHEN
	assertThat(REFERENCE).isNotEqualTo(REFERENCE.plusHours(1).toString());
	// THEN
	verify_that_isNotEqualTo_assertion_fails_and_throws_AssertionError(REFERENCE);
  }

  @Test
  public void test_isNotEqualTo_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(LocalTime.of(3, 0,
                                                                                             5)).isNotEqualTo("03:00:05"))
                                                   .withMessage(format("%n" +
                                                                       "Expecting:%n" +
                                                                       " <03:00:05>%n" +
                                                                       "not to be equal to:%n" +
                                                                       " <03:00:05>%n"));
  }

  @Test
  public void should_fail_if_timeTime_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(LocalTime.now()).isNotEqualTo((String) null))
                                        .withMessage("The String representing the LocalTime to compare actual with should not be null");
  }

  private static void verify_that_isNotEqualTo_assertion_fails_and_throws_AssertionError(LocalTime reference) {
	try {
	  assertThat(reference).isNotEqualTo(reference.toString());
	} catch (AssertionError e) {
	  // AssertionError was expected
	  return;
	}
	fail("Should have thrown AssertionError");
  }

}
