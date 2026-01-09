/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.AssertionsUtil.expectAssumptionNotMetException;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class ByteArrayAssert_asHexString_Test {

  private static final byte[] BYTES = new byte[] { -1, 0, 1 };

  @Test
  void should_pass() {
    assertThat(BYTES).asHexString()
                     .startsWith("FF")
                     .isEqualTo("FF0001");
  }

  @Test
  void should_fail_if_actual_does_not_match() {
    // GIVEN
    byte[] actual = new byte[] { -1, 0, 1 };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).asHexString().isEqualTo("010203"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("\"FF0001\"", "\"010203\""))
                        .isExactlyInstanceOf(AssertionFailedError.class);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    byte[] bytes = null;
    // WHEN
    var error = expectAssertionError(() -> assertThat(bytes).asHexString());
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    // WHEN / THEN
    softly.assertThat(BYTES).asHexString().isEqualTo("FF0001");
    softly.assertAll();
  }

  @Test
  void should_fail_with_soft_assertions_capturing_all_errors() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    // WHEN
    softly.assertThat(BYTES)
          .asHexString()
          .isEqualTo("010203")
          .isBlank();
    var assertionError = expectAssertionError(softly::assertAll);
    // THEN
    then(assertionError).hasMessageStartingWith("2 assertion errors:")
                        .hasMessageContainingAll("-- error 1 --",
                                                 shouldBeEqualMessage("\"FF0001\"", "\"010203\""),
                                                 "-- error 2 --",
                                                 "Expecting blank but was: \"FF0001\"");
  }

  @Test
  void should_ignore_test_when_assumption_for_internally_created_hex_string_assertion_fails() {
    expectAssumptionNotMetException(() -> assumeThat(BYTES).asHexString().isEqualTo("other"));
  }

  @Test
  void should_run_test_when_assumption_for_internally_created_string_passes() {
    assertThatCode(() -> assumeThat(BYTES).asHexString().startsWith("FF")).doesNotThrowAnyException();
  }

}
