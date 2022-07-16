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
package org.assertj.core.api.stacktraceelement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStackTraceElementAssert;
import org.assertj.core.api.StackTraceElementAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link StackTraceElementAssert#rawLineNumber()}
 *
 * @author Ashley Scopes
 */
class StackTraceElementAssert_rawLineNumber_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // Given
    AbstractStackTraceElementAssert<?> assertion = assertThat((StackTraceElement) null);
    // Then
    assertThatThrownBy(assertion::rawLineNumber)
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_assertion_fails_on_rawLineNumber() {
    // Given
    StackTraceElement frame = new StackTraceElement("className", "methodName", "fileName", 102);
    AbstractIntegerAssert<?> assertion = assertThat(frame)
      .rawLineNumber()
      .withFailMessage("That isn't right");
    // Then
    assertThatThrownBy(() -> assertion.isEqualTo(5678))
      .isInstanceOf(AssertionError.class)
      .hasMessage("That isn't right");
  }

  @ValueSource(ints = {-1, -2, -3})
  @ParameterizedTest
  void should_fail_on_unknown_values_if_rawLineNumber_asserted_to_be_null(int value) {
    // Given
    StackTraceElement frame = new StackTraceElement("className", "methodName", "fileName", value);
    AbstractIntegerAssert<?> assertion = assertThat(frame)
      .rawLineNumber()
      .withFailMessage("That isn't right");
    // Then
    assertThatThrownBy(assertion::isNull)
      .isInstanceOf(AssertionError.class)
      .hasMessage("That isn't right");
  }

  @Test
  void should_succeed_if_assertion_succeeds_on_rawLineNumber() {
    // Given
    StackTraceElement frame = new StackTraceElement("className", "methodName", "fileName", 102);
    AbstractIntegerAssert<?> assertion = assertThat(frame).rawLineNumber();
    // Then
    assertThatNoException()
      .isThrownBy(() -> assertion.isEqualTo(102));
  }

  @ValueSource(ints = {-1, -2, -3})
  @ParameterizedTest
  void should_succeed_on_unknown_values_if_rawLineNumber_asserted_to_be_negative_and_equal(int value) {
    // Given
    StackTraceElement frame = new StackTraceElement("className", "methodName", "fileName", value);
    AbstractIntegerAssert<?> assertion = assertThat(frame).rawLineNumber();
    // Then
    assertThatNoException()
      .isThrownBy(assertion::isNegative);
    assertThatNoException()
      .isThrownBy(() -> assertion.isEqualTo(value));
  }
}
