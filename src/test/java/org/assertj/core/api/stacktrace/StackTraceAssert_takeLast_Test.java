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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import java.util.Arrays;
import org.assertj.core.api.AbstractStackTraceAssert;
import org.assertj.core.api.StackTraceAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link StackTraceAssert#takeLast(int)}.
 *
 * @author Ashley Scopes
 */
class StackTraceAssert_takeLast_Test {

  @Test
  void fails_when_actual_is_null() {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat((StackTraceElement[]) null);
    // Then
    assertThatThrownBy(() -> assertion.takeLast(5))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void does_nothing_when_actual_is_empty() {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat(new StackTraceElement[0]);
    // Then
    assertThatNoException()
      .isThrownBy(() -> assertion.takeLast(5));
  }

  @ValueSource(ints = {-1, -2, -5, -10_000})
  @ParameterizedTest
  void fails_when_offset_is_negative(int offset) {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat(someStackTrace());
    // Then
    assertThatThrownBy(() -> assertion.takeLast(offset))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Integer 'count' with value %d is less than 0", offset);
  }

  @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6})
  @ParameterizedTest
  void succeeds_and_returns_expected_result_for_valid_offset(int offset) {
    // Given
    StackTraceElement[] stackTrace = someStackTrace();
    AbstractStackTraceAssert<?, ?> assertion = assertThat(stackTrace);
    // When
    AbstractStackTraceAssert<?, ?> newAssertion = assertion.takeLast(offset);
    // Then
    newAssertion
      .hasSize(offset)
      .containsExactly(Arrays.copyOfRange(stackTrace, stackTrace.length - offset, stackTrace.length));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2})
  void succeeds_and_returns_expected_result_for_oversized_offset(int offsetFromLength) {
    // Given
    StackTraceElement[] stackTrace = someStackTrace();
    AbstractStackTraceAssert<?, ?> assertion = assertThat(stackTrace);
    // When
    AbstractStackTraceAssert<?, ?> newAssertion = assertion.takeLast(stackTrace.length + offsetFromLength);
    // Then
    newAssertion.isEqualTo(stackTrace);
  }

  static StackTraceElement[] someStackTrace() {
    return Thread.currentThread().getStackTrace();
  }
}
