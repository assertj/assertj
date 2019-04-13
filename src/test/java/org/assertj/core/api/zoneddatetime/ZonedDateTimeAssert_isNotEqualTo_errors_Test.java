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
package org.assertj.core.api.zoneddatetime;

import static java.lang.String.format;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.fail;

import java.time.ZonedDateTime;

import org.assertj.core.internal.ChronoZonedDateTimeByInstantComparator;
import org.junit.jupiter.api.Test;

/**
 * Only test String based assertion (tests with {@link ZonedDateTime} are already defined in assertj-core)
 *
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class ZonedDateTimeAssert_isNotEqualTo_errors_Test extends ZonedDateTimeAssertBaseTest {

  @Test
  public void test_isNotEqualTo_assertion() {
    // WHEN
    assertThat(REFERENCE).isNotEqualTo(REFERENCE.plusNanos(1).toString());
    // THEN
    verify_that_isNotEqualTo_assertion_fails_and_throws_AssertionError(REFERENCE);
  }

  @Test
  public void test_isNotEqualTo_assertion_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->{
      ZonedDateTime date = ZonedDateTime.of(2000, 1, 5, 3, 0, 5, 0, UTC);
      assertThat(date).isNotEqualTo(date.toString());
    }).withMessage(format("%nExpecting:%n" +
      " <2000-01-05T03:00:05Z>%n" +
      "not to be equal to:%n" +
      " <2000-01-05T03:00:05Z>%n" +
      "when comparing values using '%s'",
      ChronoZonedDateTimeByInstantComparator.getInstance()));
  }

  @Test
  public void should_fail_if_dateTime_as_string_parameter_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(ZonedDateTime.now()).isNotEqualTo((String) null))
                                        .withMessage("The String representing the ZonedDateTime to compare actual with should not be null");
  }

  private static void verify_that_isNotEqualTo_assertion_fails_and_throws_AssertionError(ZonedDateTime reference) {
    try {
      assertThat(reference).isNotEqualTo(reference.toString());
    } catch (AssertionError e) {
      // AssertionError was expected
      return;
    }
    fail("Should have thrown AssertionError");
  }

}
