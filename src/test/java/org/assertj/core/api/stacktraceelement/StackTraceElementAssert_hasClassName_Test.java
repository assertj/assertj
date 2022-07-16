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
import static org.assertj.core.error.stacktraceelement.ShouldHaveClassName.shouldHaveClassName;

import org.assertj.core.api.AbstractStackTraceElementAssert;
import org.assertj.core.api.StackTraceElementAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StackTraceElementAssert#hasClassName(String)}
 *
 * @author Ashley Scopes
 */
class StackTraceElementAssert_hasClassName_Test {
  @Test
  void should_fail_if_actual_is_null() {
    // Given
    AbstractStackTraceElementAssert<?> assertion = assertThat((StackTraceElement) null);
    // Then
    assertThatThrownBy(() -> assertion.hasClassName("foo"))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_expected_is_null() {
    // Given
    StackTraceElement stackTraceElement = someStackTraceElement();
    AbstractStackTraceElementAssert<?> assertion = assertThat(stackTraceElement);
    // Then
    assertThatThrownBy(() -> assertion.hasClassName(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("className must not be null");
  }

  @Test
  void should_succeed_if_expected_matches_actual() {
    // Given
    StackTraceElement stackTraceElement = someStackTraceElement();
    AbstractStackTraceElementAssert<?> assertion = assertThat(stackTraceElement);
    // Then
    assertion.hasClassName("org.assertj.core.example.ExampleClass");
  }

  @Test
  void should_fail_if_expected_does_not_match_actual() {
    // Given
    StackTraceElement stackTraceElement = someStackTraceElement();
    AbstractStackTraceElementAssert<?> assertion = assertThat(stackTraceElement);
    // Then
    assertThatThrownBy(() -> assertion.hasClassName("org.assertj.ExampleInterface"))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldHaveClassName(stackTraceElement, "org.assertj.ExampleInterface").create());
  }

  static StackTraceElement someStackTraceElement() {
    return new StackTraceElement(
      "org.assertj.core.example.ExampleClass",
      "exampleMethod",
      "ExampleClass.java",
      492
    );
  }
}
