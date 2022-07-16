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
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.stacktraceelement.ShouldBeNative.shouldBeNative;

import org.assertj.core.api.AbstractStackTraceElementAssert;
import org.assertj.core.api.StackTraceElementAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StackTraceElementAssert#isNativeMethod()}
 *
 * @author Ashley Scopes
 */
public class StackTraceElementAssert_isNativeMethod_Test {
  @Test
  void should_fail_if_actual_is_null() {
    // Given
    AbstractStackTraceElementAssert<?> assertion = assertThat((StackTraceElement) null);
    // Then
    assertThatThrownBy(assertion::isNativeMethod)
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_not_native() {
    // Given
    StackTraceElement frame = new StackTraceElement("foo", "bar", "baz", 100);
    AbstractStackTraceElementAssert<?> assertion = assertThat(frame);
    // Then
    assertThatThrownBy(assertion::isNativeMethod)
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldBeNative(frame).create());
  }

  @Test
  void should_succeed_if_native() {
    // Given
    StackTraceElement frame = new StackTraceElement("foo", "bar", "baz", -2);
    assumeThat(frame.isNativeMethod())
      .withFailMessage("line number misconfigured, perhaps the JRE has different behaviour?")
      .isTrue();
    AbstractStackTraceElementAssert<?> assertion = assertThat(frame);
    // Then
    assertThatNoException().isThrownBy(assertion::isNativeMethod);
  }
}
