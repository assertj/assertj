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
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import org.assertj.core.api.AbstractStackTraceAssert;
import org.assertj.core.api.AbstractStackTraceElementAssert;
import org.assertj.core.api.StackTraceAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StackTraceAssert#first()}.
 *
 * @author Ashley Scopes
 */
class StackTraceAssert_first_Test {
  @Test
  void fails_when_actual_is_null() {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat((StackTraceElement[]) null);
    // Then
    assertThatThrownBy(assertion::first)
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void fails_when_actual_is_empty() {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat(new StackTraceElement[0]);
    // Then
    assertThatThrownBy(assertion::first)
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void returns_assertions_for_top_element_when_successful() {
    // Given
    StackTraceElement frame0 = new StackTraceElement("Class0", "method0", "file0", 0);
    StackTraceElement frame1 = new StackTraceElement("Class1", "method1", "file1", 1);
    StackTraceElement frame2 = new StackTraceElement("Class2", "method2", "file2", 2);
    StackTraceElement[] stackTrace = {frame0, frame1, frame2};
    AbstractStackTraceAssert<?, ?> assertion = assertThat(stackTrace);
    // Then
    assertThatNoException().isThrownBy(() -> assertion
      .first()
      .satisfies(
        frame -> assertThat(frame).className().isEqualTo("Class0"),
        frame -> assertThat(frame).methodName().isEqualTo("method0"),
        frame -> assertThat(frame).fileName().isEqualTo("file0"),
        frame -> assertThat(frame).lineNumber().isEqualTo(0)
      ));
  }

  @Test
  void uses_meaningful_description_for_top_element_assertions() {
    // Given
    StackTraceElement frame0 = new StackTraceElement("Class0", "method0", "file0", 0);
    StackTraceElement frame1 = new StackTraceElement("Class1", "method1", "file1", 1);
    StackTraceElement frame2 = new StackTraceElement("Class2", "method2", "file2", 2);
    StackTraceElement[] stackTrace = {frame0, frame1, frame2};
    AbstractStackTraceElementAssert<?> assertion = assertThat(stackTrace).first();
    // Then
    assertThat(assertion.descriptionText()).isEqualTo("StackTrace check element for frame #0");
  }
}
