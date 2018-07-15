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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.zoneddatetime;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.fail;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link ZonedDateTime} are already defined in assertj-core)
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class ZonedDateTimeAssert_isEqualTo_errors_Test extends ZonedDateTimeAssertBaseTest {

  @Test
  public void test_isEqualTo_assertion() {
    // WHEN
    assertThat(REFERENCE).isEqualTo(REFERENCE.toString());
    // THEN
    verify_that_isEqualTo_assertion_fails_and_throws_AssertionError(REFERENCE);
  }

  @Test
  public void test_isEqualTo_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(ZonedDateTime.of(2000, 1, 5, 3, 0, 5, 0, UTC)).isEqualTo(ZonedDateTime.of(2012, 1, 1, 3, 3, 3, 0, UTC).toString()))
                                                   .withMessage("expected:<20[12-01-01T03:03:03]Z> but was:<20[00-01-05T03:00:05]Z>");
  }

  @Test
  public void should_fail_if_actual_dateTime_is_null_and_expected_dateTime_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((ZonedDateTime) null).isEqualTo(ZonedDateTime.of(2000, 1, 5, 3, 0, 5, 0, UTC)))
                                                   .withMessage("expected:<2000-01-05T03:00:05Z> but was:<null>");
  }

  @Test
  public void should_fail_if_actual_dateTime_is_null_and_expected_dateTime_as_string_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((ZonedDateTime) null).isEqualTo("2000-01-01T01:00:00+01:00"))
                                                   .withMessage("expected:<2000-01-01T01:00+01:00> but was:<null>");
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(ZonedDateTime.now()).isEqualTo((String) null))
                                        .withMessage("The String representing the ZonedDateTime to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_expected_ZoneDateTime_is_null_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(ZonedDateTime.of(2000, 1, 5, 3, 0, 5, 0, UTC)).isEqualTo((ZonedDateTime) null))
                                                   .withMessage("expected:<null> but was:<2000-01-05T03:00:05Z>");
  }

  private static void verify_that_isEqualTo_assertion_fails_and_throws_AssertionError(ZonedDateTime reference) {
    try {
      assertThat(reference).isEqualTo(reference.plusNanos(1).toString());
    } catch (AssertionError e) {
      // AssertionError was expected
      return;
    }
    fail("Should have thrown AssertionError");
  }

}
