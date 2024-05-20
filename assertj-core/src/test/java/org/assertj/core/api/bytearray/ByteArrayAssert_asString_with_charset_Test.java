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
package org.assertj.core.api.bytearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.AssertionsUtil.expectAssumptionNotMetException;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.nio.charset.Charset;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.error.AssertJMultipleFailuresError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

@DisplayName("ByteArrayAssert asString")
class ByteArrayAssert_asString_with_charset_Test {

  private static final Charset TURKISH_CHARSET = Charset.forName("windows-1254");

  @Test
  void should_convert_bytes_array_to_a_proper_string_with_specific_encoding() {
    // GIVEN
    String real = "Gerçek";
    byte[] bytes = real.getBytes(TURKISH_CHARSET);
    // WHEN/THEN
    assertThat(bytes).asString(TURKISH_CHARSET)
                     .isEqualTo(real);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    byte[] bytes = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(bytes).asString(TURKISH_CHARSET));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_match() {
    // GIVEN
    String real = "Gerçek";
    byte[] bytes = real.getBytes(TURKISH_CHARSET);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(bytes).asString(TURKISH_CHARSET).isEqualTo("bar"));
    // THEN
    assertThat(assertionError).hasMessage(shouldBeEqualMessage("\"Gerçek\"", "\"bar\""))
                              .isExactlyInstanceOf(AssertionFailedError.class);
  }

  @Test
  void should_pass_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    String real = "Gerçek";
    byte[] bytes = real.getBytes(TURKISH_CHARSET);
    // WHEN/THEN
    softly.assertThat(bytes).asString(TURKISH_CHARSET).isEqualTo(real);
    softly.assertAll();
  }

  @Test
  void should_fail_with_soft_assertions_capturing_all_errors() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    String real = "Gerçek";
    byte[] bytes = real.getBytes(TURKISH_CHARSET);
    // WHEN
    softly.assertThat(bytes)
          .asString(TURKISH_CHARSET)
          .isEqualTo("bar")
          .isBlank();
    AssertionError assertionError = expectAssertionError(softly::assertAll);
    // THEN
    assertThat(assertionError).hasMessageContainingAll("Multiple Failures (2 failures)",
                                                       "-- failure 1 --",
                                                       shouldBeEqualMessage("\"Gerçek\"", "\"bar\""),
                                                       "-- failure 2 --",
                                                       "Expecting blank but was: \"Gerçek\"")
                              .isExactlyInstanceOf(AssertJMultipleFailuresError.class);
  }

  @Test
  void should_ignore_test_when_assumption_for_internally_created_hex_string_assertion_fails() {
    // GIVEN
    String real = "Gerçek";
    byte[] bytes = real.getBytes(TURKISH_CHARSET);
    // WHEN/THEN
    expectAssumptionNotMetException(() -> assumeThat(bytes).asString(TURKISH_CHARSET).isEqualTo("bar"));
  }

  @Test
  void should_run_test_when_assumption_for_internally_created_string_passes() {
    // GIVEN
    String real = "Gerçek";
    byte[] bytes = real.getBytes(TURKISH_CHARSET);
    // WHEN/THEN
    assertThatCode(() -> assumeThat(bytes).asString(TURKISH_CHARSET).startsWith("Gerç")).doesNotThrowAnyException();
  }

}
