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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatReturningCode;
import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;

import org.junit.jupiter.api.Test;

public class Assertions_assertThatReturningCode_Test {

  @Test
  void can_invoke_late_assertion_on_assertThatReturningCode() {
    // Given
    AbstractTryAssert.ThrowingReturningCallable<?> failedCallable = failedReturningCallable("boom");

    // Then
    assertThatReturningCode(failedCallable)
      .isInstanceOf(Exception.class)
      .hasMessageContaining("boom");
  }

  @Test
  void should_fail_when_asserting_no_exception_raised_but_exception_occurs() {
    // Given
    Exception exception = new Exception("boom");
    AbstractTryAssert.ThrowingReturningCallable<?> failedCallable = failedReturningCallable(exception);

    // Expect
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->{
      // When;
      assertThatReturningCode(failedCallable).doesNotThrowAnyException();
    }).withMessage(shouldNotHaveThrown(exception).create());
  }

  @Test
  void can_use_description_in_error_message() {
    // Given
    AbstractTryAssert.ThrowingReturningCallable<?> failedCallable = failedReturningCallable("boom");

    // Expect
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> assertThatReturningCode(failedCallable)
        .as("Test")
        .doesNotThrowAnyException())
      .withMessageStartingWith("[Test]");
  }

  @Test
  void error_message_contains_stacktrace() {
    // Given
    AbstractTryAssert.ThrowingReturningCallable<?> failedCallable = failedReturningCallable("boom");

    // Then
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> assertThatReturningCode(failedCallable).doesNotThrowAnyException())
      .withMessageContaining("java.lang.Exception: %s", "boom")
      .withMessageContaining("at org.assertj.core.api.Assertions_assertThatReturningCode_Test.error_message_contains_stacktrace");
  }

  @Test
  void should_succeed_and_return_when_asserting_no_exception_raised_and_no_exception_occurs() {
    // Given
    AbstractTryAssert.ThrowingReturningCallable<Integer> succeed = succeedReturningCallable();

    // Then
    assertThatReturningCode(succeed)
      .doesNotThrowAnyExceptionAndReturns()
      .isNotNull()
      .isEqualTo(42);

    assertThatReturningCode(succeed)
      .doesNotThrowAnyException();
  }

  @Test
  void should_fail_when_asserting_exception_raised_but_no_exception_occurs() {
    // Given
    AbstractTryAssert.ThrowingReturningCallable<Integer> succeed = succeedReturningCallable();

    // Then
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(() -> assertThatReturningCode(succeed).isInstanceOf(Exception.class));
  }

  private AbstractTryAssert.ThrowingReturningCallable<Integer> succeedReturningCallable() {
    return () -> 42;
  }

  public AbstractTryAssert.ThrowingReturningCallable<Integer> failedReturningCallable(String reason) {
    return failedReturningCallable(new Exception(reason));
  }

  private AbstractTryAssert.ThrowingReturningCallable<Integer> failedReturningCallable(Throwable exception) {
    return () -> {
      throw exception;
    };
  }
}
