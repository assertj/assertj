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
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import org.assertj.core.api.AbstractStackTraceElementAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.StackTraceElementAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StackTraceElementAssert#fileName()}
 *
 * @author Ashley Scopes
 */
class StackTraceElementAssert_fileName_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // Given
    AbstractStackTraceElementAssert<?> assertion = assertThat((StackTraceElement) null);
    // Then
    assertThatThrownBy(assertion::fileName)
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_assertion_fails_on_fileName() {
    // Given
    StackTraceElement frame = new StackTraceElement("declaringClass", "methodName", "this is a file name", 1234);
    AbstractStringAssert<?> assertion = assertThat(frame)
      .fileName()
      .withFailMessage("That isn't right");
    // Then
    assertThatThrownBy(() -> assertion.isEqualTo("potato"))
      .isInstanceOf(AssertionError.class)
      .hasMessage("That isn't right");
  }

  @Test
  void should_allow_null_fileNames() {
    // Given
    StackTraceElement frame = new StackTraceElement("declaringClass", "methodName", null, 1234);
    AbstractStringAssert<?> assertion = assertThat(frame).fileName();
    // Then
    assertThatNoException()
      .isThrownBy(assertion::isNull);
  }

  @Test
  void should_allow_non_null_fileNames() {
    // Given
    StackTraceElement frame = new StackTraceElement("declaringClass", "methodName", "this is a file name", 1234);
    AbstractStringAssert<?> assertion = assertThat(frame).fileName();
    // Then
    assertThatNoException()
      .isThrownBy(() -> assertion.isEqualTo("this is a file name"));
  }
}
