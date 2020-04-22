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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.bytearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.AssertionsUtil.expectAssumptionViolatedException;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.error.AssertJMultipleFailuresError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * Tests for <code>{@link org.assertj.core.api.ByteArrayAssert#asHexString()}</code>.
 */
@DisplayName("ByteArrayAssert asHexString")
public class ByteArrayAssert_asHexString_Test {

  private static final byte[] BYTES = new byte[] { -1, 0, 1 };

  @Test
  public void should_pass() {
    // GIVEN
    // WHEN / THEN
    assertThat(BYTES).asHexString()
                     .startsWith("FF")
                     .isEqualTo("FF0001");
  }

  @Test
  public void should_fail_if_actual_does_not_match() {
    // GIVEN
    byte[] actual = new byte[] { -1, 0, 1 };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).asHexString().isEqualTo("010203"));
    // THEN
    assertThat(assertionError).hasMessageContainingAll("Expecting:",
                                                       "<\"FF0001\">",
                                                       "to be equal to:",
                                                       "<\"010203\">",
                                                       "but was not.")
                              .isExactlyInstanceOf(AssertionFailedError.class);
  }

  @Test
  public void should_pass_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    // WHEN / THEN
    softly.assertThat(BYTES).asHexString().isEqualTo("FF0001");
    softly.assertAll();
  }

  @Test
  public void should_fail_with_soft_assertions_capturing_all_errors() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    // WHEN
    softly.assertThat(BYTES)
          .asHexString()
          .isEqualTo("010203")
          .isBlank();
    AssertionError assertionError = expectAssertionError(softly::assertAll);
    // THEN
    assertThat(assertionError).hasMessageContainingAll("Multiple Failures (2 failures)",
                                                       "-- failure 1 --",
                                                       "Expecting:",
                                                       "<\"FF0001\">",
                                                       "to be equal to:",
                                                       "<\"010203\">",
                                                       "but was not.",
                                                       "-- failure 2 --",
                                                       "Expecting blank but was:<\"FF0001\">")
                              .isExactlyInstanceOf(AssertJMultipleFailuresError.class);
  }

  @Test
  public void should_ignore_test_when_assumption_for_internally_created_hex_string_assertion_fails() {
    expectAssumptionViolatedException(() -> assumeThat(BYTES).asHexString().isEqualTo("other"));
  }

  @Test
  public void should_run_test_when_assumption_for_internally_created_string_passes() {
    assertThatCode(() -> assumeThat(BYTES).asHexString().startsWith("FF")).doesNotThrowAnyException();
  }

}
