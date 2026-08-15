/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotHaveThrownExcept.shouldNotHaveThrownExcept;
import static org.assertj.tests.core.testkit.ThrowingCallableFactory.codeThrowing;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class ThrowableAssert_doesNotThrowAnyExceptionExcept_Test {

  @Test
  void should_pass_if_no_exception_is_thrown() {
    // GIVEN
    ThrowingCallable silent = () -> {};
    // WHEN/THEN
    assertThatCode(silent).doesNotThrowAnyExceptionExcept(IOException.class, IllegalStateException.class);
  }

  @Test
  void should_pass_if_thrown_exception_is_ignored() {
    // GIVEN
    ThrowingCallable boom = codeThrowing(new IllegalArgumentException("boom"));
    // WHEN/THEN
    assertThatCode(boom).doesNotThrowAnyExceptionExcept(IOException.class, IllegalArgumentException.class);
  }

  @Test
  void should_pass_if_thrown_exception_inherits_ignored_exception() {
    // GIVEN
    ThrowingCallable boom = codeThrowing(new IllegalArgumentException("boom"));
    // WHEN/THEN
    assertThatCode(boom).doesNotThrowAnyExceptionExcept(RuntimeException.class);
  }

  @Test
  void should_fail_if_exception_is_thrown_and_ignored_exceptions_are_empty() {
    // GIVEN
    Exception exception = new Exception("boom");
    ThrowingCallable boom = codeThrowing(exception);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThatCode(boom).doesNotThrowAnyExceptionExcept());
    // THEN
    then(error).hasMessage(shouldNotHaveThrownExcept(exception).create());
  }

  @Test
  void should_fail_if_non_ignored_exception_is_thrown() {
    // GIVEN
    Exception exception = new IllegalArgumentException("boom");
    ThrowingCallable boom = codeThrowing(exception);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThatCode(boom).doesNotThrowAnyExceptionExcept(IllegalStateException.class,
                                                                                                          IOException.class));
    // THEN
    then(error).hasMessage(shouldNotHaveThrownExcept(exception, IllegalStateException.class, IOException.class).create());
  }

}
