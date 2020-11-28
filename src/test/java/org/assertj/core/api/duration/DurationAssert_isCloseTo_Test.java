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
package org.assertj.core.api.duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withMarginOf;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.error.ShouldBeCloseTo.shouldBeCloseTo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Duration;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("DurationAssert isCloseTo")
class DurationAssert_isCloseTo_Test {

  @ParameterizedTest(name = "PT2M should close to {0} withMarginOf {1}")
  @CsvSource({
      "PT1M, PT70S",
      "PT70S, PT1M",
      "PT2M, PT0S",
      "PT2M, PT1S",
      "PT0M, PT2M",
      "PT0M, PT3M",
      "PT1M, PT3M",
      "PT-1M, PT3M",
      "PT-1M, PT4M",
      "PT-10S, PT130S",
      "PT-10S, PT131S",
      "PT1M, PT70S"
  })
  void should_pass_if_close_enough_to_the_given_duration(Duration expected, Duration allowedDifference) {
    // GIVEN
    Duration actual = Duration.ofMinutes(2);
    // WHEN/THEN
    assertThat(actual).isCloseTo(expected, allowedDifference)
                      .isCloseTo(expected, withMarginOf(allowedDifference));
  }

  @ParameterizedTest(name = "PT2M should not be close to {0} withMarginOf {1}")
  @CsvSource({
      "PT1M, PT10S, PT1M",
      "PT59S, PT1M, PT1M1S",
      "PT1M, PT59S, PT1M",
      "PT1M31S, PT10S, PT29S",
      "PT0M, PT15S, PT2M"
  })
  void should_fail_if_not_close_enough_to_the_given_duration(Duration expected, Duration allowedDifference, Duration difference) {
    // GIVEN
    Duration actual = Duration.ofMinutes(2);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isCloseTo(expected, allowedDifference));
    // THEN
    then(assertionError).hasMessage(shouldBeCloseTo(actual, expected, allowedDifference, difference).create());
  }

  @Test
  void should_fail_when_actual_duration_is_null() {
    // GIVEN
    Duration actual = null;
    Duration expected = Duration.ofDays(4);
    Duration allowedDifference = Duration.ofDays(5);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isCloseTo(expected, allowedDifference));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_IllegalArgumentException_when_expected_duration_is_null() {
    // GIVEN
    Duration actual = Duration.ofMinutes(2);
    Duration expected = null;
    Duration allowedDifference = Duration.ofDays(5);
    // WHEN
    ThrowingCallable code = () -> assertThat(actual).isCloseTo(expected, allowedDifference);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("expected duration should not be null");
  }

  @Test
  void should_throw_IllegalArgumentException_when_allowed_difference_duration_is_null() {
    // GIVEN
    Duration actual = Duration.ofMinutes(2);
    Duration expected = Duration.ofDays(5);
    Duration allowedDifference = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(actual).isCloseTo(expected, allowedDifference);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("allowed difference duration should not be null");
  }

  @Test
  void should_throw_IllegalArgumentException_when_allowed_difference_duration_is_negative() {
    // GIVEN
    Duration actual = Duration.ofMinutes(2);
    Duration expected = Duration.ofDays(5);
    Duration allowedDifference = Duration.ofDays(-5);
    // WHEN
    ThrowingCallable code = () -> assertThat(actual).isCloseTo(expected, allowedDifference);
    // THEN
    thenIllegalArgumentException().isThrownBy(code)
                                  .withMessage("allowed difference duration should be >= 0");
  }

}
