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

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_assertThatNoException_Test {

  @Test
  void should_fail_when_asserting_no_exception_raised_but_exception_occurs() {
    // GIVEN
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThatNoException().isThrownBy(boom));
    // THEN
    then(assertionError).hasMessage(shouldNotHaveThrown(exception).create());
  }

  @Test
  void can_use_description_in_error_message() {
    // GIVEN
    ThrowingCallable boom = raisingException(new Exception("boom"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThatNoException().as("Test").isThrownBy(boom));
    // THEN
    then(assertionError).hasMessageStartingWith("[Test]");
  }

  @Test
  void error_message_contains_stacktrace() {
    // GIVEN
    ThrowingCallable boom = raisingException(new Exception("boom"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThatNoException().isThrownBy(boom));
    // THEN
    then(assertionError).hasMessageContaining("java.lang.Exception: %s", "boom")
                        .hasMessageContaining("at org.assertj.tests.core.api.Assertions_assertThatNoException_Test.error_message_contains_stacktrace");
  }

  private ThrowingCallable raisingException(Exception exception) {
    return () -> {
      throw exception;
    };
  }

}
