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
package org.assertj.core.api.localdatetime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * Only test String based assertion (tests with {@link LocalDateTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@RunWith(Theories.class)
public class LocalDateTimeAssert_isNotIn_Test extends LocalDateTimeAssertBaseTest {

  @Theory
  public void test_isNotIn_assertion(LocalDateTime referenceDate) {
    // WHEN
    assertThat(referenceDate).isNotIn(referenceDate.plusDays(1).toString(), referenceDate.plusDays(2).toString());
    // THEN
    verify_that_isNotIn_assertion_fails_and_throws_AssertionError(referenceDate);
  }

  @Test
  public void test_isNotIn_assertion_error_message() {
    thrown.expectAssertionError(
        "%nExpecting:%n <2000-01-05T03:00:05>%nnot to be in:%n <[2000-01-05T03:00:05, 2012-01-01T03:03:03]>%n");
    assertThat(LocalDateTime.of(2000, 1, 5, 3, 0, 5)).isNotIn(LocalDateTime.of(2000, 1, 5, 3, 0, 5).toString(),
                                                              LocalDateTime.of(2012, 1, 1, 3, 3, 3).toString());
  }

  @Test
  public void should_fail_if_dateTimes_as_string_array_parameter_is_null() {
    expectException(IllegalArgumentException.class, "The given LocalDateTime array should not be null");
    assertThat(LocalDateTime.now()).isNotIn((String[]) null);
  }

  @Test
  public void should_fail_if_dateTimes_as_string_array_parameter_is_empty() {
    expectException(IllegalArgumentException.class, "The given LocalDateTime array should not be empty");
    assertThat(LocalDateTime.now()).isNotIn(new String[0]);
  }

  private static void verify_that_isNotIn_assertion_fails_and_throws_AssertionError(LocalDateTime reference) {
    try {
      assertThat(reference).isNotIn(reference.toString(), reference.plusDays(1).toString());
    } catch (AssertionError e) {
      // AssertionError was expected
      return;
    }
    fail("Should have thrown AssertionError");
  }

}
