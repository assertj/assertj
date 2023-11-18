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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;
import static org.assertj.core.error.ShouldNotHaveThrownExcept.shouldNotHaveThrownExcept;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class Assertions_assertThatCode_Test {

  @Test
  void can_invoke_late_assertion_on_assertThatCode() {
    // Given
    ThrowingCallable boom = raisingException("boom");

    // Then
    assertThatCode(boom).isInstanceOf(Exception.class)
                        .hasMessageContaining("boom");
  }

  @Test
  void should_fail_when_asserting_no_exception_raised_but_exception_occurs() {
    // Given
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);

    // Expect
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      // When;
      assertThatCode(boom).doesNotThrowAnyException();
    }).withMessage(shouldNotHaveThrown(exception).create());
  }

  @Test
  void should_fail_when_asserting_no_exception_raised_other_than_empty_but_exception_occurs() {
    // Given
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);

    // Expect
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      // When;
      assertThatCode(boom).doesNotThrowAnyExceptionExcept();
    }).withMessage(shouldNotHaveThrownExcept(exception).create());
  }

  @Test
  void should_fail_when_asserting_no_exception_raised_other_than_with_values_but_not_matching_exception_occurs() {
    // Given
    Exception exception = new IllegalArgumentException("boom");
    ThrowingCallable boom = raisingException(exception);

    // Expect
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      // When;
      assertThatCode(boom).doesNotThrowAnyExceptionExcept(IllegalStateException.class, IOException.class);
    }).withMessage(shouldNotHaveThrownExcept(exception, IllegalStateException.class, IOException.class).create());
  }

  @Test
  void should_fail_when_asserting_no_exception_raised_other_than_with_values_but_a_subtype_of_that_exception_occurs() {
    // Given
    Exception exception = new IllegalArgumentException("boom");
    ThrowingCallable boom = raisingException(exception);

    // Expect
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      // When;
      assertThatCode(boom).doesNotThrowAnyExceptionExcept(NumberFormatException.class);
    }).withMessage(shouldNotHaveThrownExcept(exception, NumberFormatException.class).create());
  }

  @Test
  void can_use_description_in_error_message() {
    // Given
    ThrowingCallable boom = raisingException("boom");

    // Expect
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThatCode(boom).as("Test")
                                                                                         .doesNotThrowAnyException())
                                                   .withMessageStartingWith("[Test]");
  }

  @Test
  void error_message_contains_stacktrace() {
    // Given
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);

    // Then
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThatCode(boom).doesNotThrowAnyException())
                                                   .withMessageContaining("java.lang.Exception: %s", "boom")
                                                   .withMessageContaining("at org.assertj.core.api.Assertions_assertThatCode_Test.error_message_contains_stacktrace");
  }

  @Test
  void should_succeed_when_asserting_no_exception_raised_and_no_exception_occurs() {
    // Given
    ThrowingCallable silent = () -> {};

    // Then
    assertThatCode(silent).doesNotThrowAnyException();
  }

  @Test
  void should_succeed_when_asserting_no_exception_raised_other_than_empty_and_no_exception_occurs() {
    // Given
    ThrowingCallable silent = () -> {};

    // Then
    assertThatCode(silent).doesNotThrowAnyExceptionExcept();
  }

  @Test
  void should_succeed_when_asserting_no_exception_raised_except_with_values_and_no_exception_occurs() {
    // Given
    ThrowingCallable silent = () -> {};

    // Then
    assertThatCode(silent).doesNotThrowAnyExceptionExcept(IOException.class, IllegalStateException.class);
  }

  @Test
  void should_succeed_when_asserting_no_exception_raised_except_with_values_and_a_matching_exception_occurs() {
    // Given
    Exception exception = new IllegalArgumentException("boom");
    ThrowingCallable boom = raisingException(exception);

    // Then
    assertThatCode(boom).doesNotThrowAnyExceptionExcept(IOException.class, IllegalArgumentException.class);
  }

  @Test
  void should_succeed_when_asserting_no_exception_raised_except_with_values_and_a_supertype_of_that_exception_occurs() {
    // Given
    Exception exception = new IllegalArgumentException("boom");
    ThrowingCallable boom = raisingException(exception);

    // Then
    assertThatCode(boom).doesNotThrowAnyExceptionExcept(RuntimeException.class);
  }

  private ThrowingCallable raisingException(final String reason) {
    return raisingException(new Exception(reason));
  }

  private ThrowingCallable raisingException(final Exception exception) {
    return () -> {
      throw exception;
    };
  }
}
