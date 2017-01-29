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
package org.assertj.core.api.offsetdatetime;

import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.time.OffsetDateTime;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * Only test String based assertion (tests with {@link java.time.OffsetDateTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
@RunWith(Theories.class)
public class OffsetDateTimeAssert_isEqualTo_Test extends OffsetDateTimeAssertBaseTest {

  @Theory
  public void test_isEqualTo_assertion(OffsetDateTime reference, OffsetDateTime dateBefore, OffsetDateTime dateEqual,
                                       OffsetDateTime dateAfter) {
    // GIVEN
    testAssumptions(reference, dateBefore, dateEqual, dateAfter);
    // WHEN
    assertThat(dateEqual).isEqualTo(reference);
    assertThat(dateEqual).isEqualTo(reference.toString());
    // THEN
    verify_that_isEqualTo_assertion_fails_and_throws_AssertionError(reference);
  }

  @Test
  public void test_isEqualTo_assertion_error_message() {
    thrown.expectAssertionError("expected:<20[12-01-01T03:03:03]Z> but was:<20[00-01-05T03:00:05]Z>");
    assertThat(of(2000, 1, 5, 3, 0, 5, 0, UTC)).isEqualTo(of(2012, 1, 1, 3, 3, 3, 0, UTC).toString());
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    expectException(IllegalArgumentException.class,
                    "The String representing the OffsetDateTime to compare actual with should not be null");
    assertThat(OffsetDateTime.now()).isEqualTo((String) null);
  }

  private static void verify_that_isEqualTo_assertion_fails_and_throws_AssertionError(OffsetDateTime reference) {
    try {
      assertThat(reference).isEqualTo(reference.plusDays(1).toString());
    } catch (AssertionError e) {
      // AssertionError was expected
      return;
    }
    fail("Should have thrown AssertionError");
  }

}
