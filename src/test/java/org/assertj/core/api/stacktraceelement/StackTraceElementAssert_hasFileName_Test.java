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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.stacktraceelement.ShouldHaveFileName.shouldHaveFileName;

import org.assertj.core.api.AbstractStackTraceElementAssert;
import org.assertj.core.api.StackTraceElementAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link StackTraceElementAssert#hasFileName(String)}
 *
 * @author Ashley Scopes
 */
class StackTraceElementAssert_hasFileName_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // Given
    AbstractStackTraceElementAssert<?> assertion = assertThat((StackTraceElement) null);
    // Then
    assertThatThrownBy(() -> assertion.hasFileName("foo"))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @NullSource
  @ValueSource(strings = {"someFileName", "anotherFileName.kt"})
  @ParameterizedTest
  void should_succeed_if_expected_matches_actual(String expectedFileName) {
    // Given
    StackTraceElement stackTraceElement = new StackTraceElement(
      "SomeClass", "someMethod", expectedFileName, 123);
    AbstractStackTraceElementAssert<?> assertion = assertThat(stackTraceElement);
    // Then
    assertion.hasFileName(expectedFileName);
  }

  @NullSource
  @ValueSource(strings = {"someFileName", "anotherFileName.kt"})
  @ParameterizedTest
  void should_fail_if_expected_does_not_match_actual(String expectedFileName) {
    // Given
    StackTraceElement stackTraceElement = new StackTraceElement(
      "SomeClass", "someMethod", "SomeClass.java", 123);
    AbstractStackTraceElementAssert<?> assertion = assertThat(stackTraceElement);
    // Then
    assertThatThrownBy(() -> assertion.hasFileName(expectedFileName))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldHaveFileName(stackTraceElement, expectedFileName).create());
  }
}
