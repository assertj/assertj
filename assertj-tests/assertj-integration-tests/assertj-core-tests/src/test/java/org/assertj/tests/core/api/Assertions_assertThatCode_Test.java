/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenCode;
import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;
import static org.assertj.core.error.ShouldNotHaveThrownExcept.shouldNotHaveThrownExcept;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_assertThatCode_Test {

  @Test
  void can_invoke_late_assertion() {
    // GIVEN
    ThrowingCallable boom = raisingException("boom!");

    // WHEN/THEN
    thenCode(boom).isInstanceOf(Exception.class)
                  .hasMessageContaining("boom!");
  }

  @Test
  void should_fail_when_asserting_no_exception_was_thrown_and_an_exception_was_thrown() {
    // GIVEN
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThatCode(boom).doesNotThrowAnyException());
    // THEN
    then(error).hasMessage(shouldNotHaveThrown(exception).create());
  }

  @Test
  void should_fail_when_asserting_no_exception_was_thrown_except_an_empty_list_and_an_exception_was_thrown() {
    // GIVEN
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThatCode(boom).doesNotThrowAnyExceptionExcept());
    // THEN
    then(error).hasMessage(shouldNotHaveThrownExcept(exception).create());
  }

  @Test
  void should_fail_when_asserting_no_exception_was_thrown_except_some_and_a_non_ignored_exception_was_thrown() {
    // GIVEN
    Exception exception = new IllegalArgumentException("boom");
    ThrowingCallable boom = raisingException(exception);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThatCode(boom).doesNotThrowAnyExceptionExcept(IllegalStateException.class,
                                                                                                          IOException.class));
    // THEN
    then(error).hasMessage(shouldNotHaveThrownExcept(exception, IllegalStateException.class, IOException.class).create());
  }

  @Test
  void can_use_description_in_error_message() {
    // GIVEN
    ThrowingCallable boom = raisingException("boom");
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThatCode(boom).as("Test").doesNotThrowAnyException());
    // THEN
    then(error).hasMessageStartingWith("[Test]");
  }

  @Test
  void error_message_contains_stacktrace() {
    // GIVEN
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThatCode(boom).doesNotThrowAnyException());
    // THEN
    then(error).hasMessageContainingAll("java.lang.Exception: boom",
                                        "at org.assertj.tests.core.api.Assertions_assertThatCode_Test.error_message_contains_stacktrace");
  }

  @Test
  void should_succeed_when_asserting_no_exception_was_thrown() {
    // GIVEN
    ThrowingCallable silent = () -> {};
    // WHEN/THEN
    thenCode(silent).doesNotThrowAnyException();
  }

  @Test
  void should_succeed_when_asserting_no_exception_was_thrown_except_an_empty_list() {
    // GIVEN
    ThrowingCallable silent = () -> {};
    // WHEN/THEN
    thenCode(silent).doesNotThrowAnyExceptionExcept();
  }

  @Test
  void should_succeed_when_asserting_no_exception_was_thrown_except_some() {
    // GIVEN
    ThrowingCallable silent = () -> {};
    // WHEN/THEN
    thenCode(silent).doesNotThrowAnyExceptionExcept(IOException.class, IllegalStateException.class);
  }

  @Test
  void should_succeed_when_asserting_no_exception_was_thrown_except_one_that_is_an_ignored() {
    // GIVEN
    ThrowingCallable boom = raisingException(new IllegalArgumentException("boom"));
    // WHEN/THEN
    thenCode(boom).doesNotThrowAnyExceptionExcept(IOException.class, IllegalArgumentException.class);
  }

  @Test
  void should_succeed_when_asserting_no_exception_was_thrown_except_one_that_inherits_an_ignored_exception() {
    // GIVEN
    ThrowingCallable boom = raisingException(new IllegalArgumentException("boom"));
    // WHEN/THEN
    thenCode(boom).doesNotThrowAnyExceptionExcept(RuntimeException.class);
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
