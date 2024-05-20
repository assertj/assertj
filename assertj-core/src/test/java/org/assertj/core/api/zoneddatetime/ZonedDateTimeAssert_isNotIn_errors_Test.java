/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.zoneddatetime;

import static java.lang.String.format;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link ZonedDateTime} are already defined in assertj-core)
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
class ZonedDateTimeAssert_isNotIn_errors_Test extends ZonedDateTimeAssertBaseTest {

  @Test
  void test_isNotIn_assertion() {
    // WHEN
    assertThat(REFERENCE).isNotIn(REFERENCE.plusNanos(1).toString(), REFERENCE.plusNanos(2).toString());
    // THEN
    verify_that_isNotIn_assertion_fails_and_throws_AssertionError(REFERENCE);
  }

  @Test
  void test_isNotIn_assertion_error_message() {
    // GIVEN
    ZonedDateTime actual = ZonedDateTime.of(2000, 1, 5, 3, 0, 5, 0, UTC);
    ZonedDateTime other = ZonedDateTime.of(2012, 1, 1, 3, 3, 3, 0, UTC);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotIn(actual.toString(), other.toString()));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n" +
                                           "  2000-01-05T03:00:05Z (java.time.ZonedDateTime)%n" +
                                           "not to be in:%n" +
                                           "  [2000-01-05T03:00:05Z (java.time.ZonedDateTime),%n" +
                                           "    2012-01-01T03:03:03Z (java.time.ZonedDateTime)]%n"));
  }

  @Test
  void should_fail_if_dateTimes_as_string_array_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(ZonedDateTime.now()).isNotIn((String[]) null))
                                        .withMessage("The given ZonedDateTime array should not be null");
  }

  @Test
  void should_fail_if_dateTimes_as_string_array_parameter_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(ZonedDateTime.now()).isNotIn(new String[0]))
                                        .withMessage("The given ZonedDateTime array should not be empty");
  }

  private static void verify_that_isNotIn_assertion_fails_and_throws_AssertionError(ZonedDateTime reference) {
    try {
      assertThat(reference).isNotIn(reference.toString(), reference.plusNanos(1).toString());
    } catch (AssertionError e) {
      // AssertionError was expected
      return;
    }
    fail("Should have thrown AssertionError");
  }

}
