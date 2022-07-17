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
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.assertj.core.api.AbstractStackTraceAssert;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ThrowableAssert#stackTrace()}.
 *
 * @author Ashley Scopes
 */
class ThrowableAssert_stackTrace_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;

    // THEN
    assertThatThrownBy(() -> assertThat(actual).stackTrace())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  // While null is not valid for the Java API, we expect to allow it to enable asserting on
  // erroneous API values using the chained assertion. For the .hasStackTrace and .hasNoStackTrace
  // calls, we explicitly disallow this to prevent hiding this behaviour as a bug. If the user
  // really intends to allow null values, they can only do it explicitly using this method. In this
  // case they are on their own if anything else breaks in their code that is under test!
  @Test
  void should_return_assertions_for_null_stack_trace() {
    // GIVEN
    Throwable ex = mock(Throwable.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS));
    when(ex.getStackTrace())
      .thenReturn(null);

    // WHEN
    AbstractStackTraceAssert<?, ?> actualAssert = assertThat(ex).stackTrace();

    // THEN
    // Perform sanity checks to ensure the correct stack trace was asserted upon.
    assertThatCode(actualAssert::isNull)
      .doesNotThrowAnyException();
    assertThatCode(actualAssert::isNotEmpty)
      .isInstanceOf(AssertionError.class);
    assertThatCode(() -> actualAssert.first().isEqualTo(ex.getStackTrace()[0]))
      .isInstanceOf(AssertionError.class);
  }

  @Test
  void should_return_assertions_for_empty_stack_trace() {
    // GIVEN
    Throwable ex = mock(Throwable.class, withSettings().defaultAnswer(RETURNS_DEEP_STUBS));
    when(ex.getStackTrace())
      .thenReturn(new StackTraceElement[0]);

    // WHEN
    AbstractStackTraceAssert<?, ?> actualAssert = assertThat(ex).stackTrace();

    // THEN
    // Perform sanity checks to ensure the correct stack trace was asserted upon.
    assertThatCode(actualAssert::isEmpty)
      .doesNotThrowAnyException();
    assertThatCode(actualAssert::isNull)
      .isInstanceOf(AssertionError.class);
    assertThatCode(() -> actualAssert.first().isEqualTo(ex.getStackTrace()[0]))
      .isInstanceOf(AssertionError.class);
  }

  @Test
  void should_return_assertions_for_filled_stack_trace() {
    // GIVEN
    Throwable ex = new RuntimeException("Bang!").fillInStackTrace();

    // WHEN
    AbstractStackTraceAssert<?, ?> actualAssert = assertThat(ex).stackTrace();

    // THEN
    // Perform sanity checks to ensure the correct stack trace was asserted upon.
    assertThatCode(() -> actualAssert.first().isEqualTo(ex.getStackTrace()[0]))
      .doesNotThrowAnyException();
    assertThatCode(() -> actualAssert.first().isEqualTo(ex.getStackTrace()[1]))
      .isInstanceOf(AssertionError.class);
    assertThatCode(() -> actualAssert.last().isEqualTo(ex.getStackTrace()[0]))
      .isInstanceOf(AssertionError.class);
  }
}
