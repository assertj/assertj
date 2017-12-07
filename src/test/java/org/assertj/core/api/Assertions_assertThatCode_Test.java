/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class Assertions_assertThatCode_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void can_invoke_late_assertion_on_assertThatCode() {
    // Given
    ThrowingCallable boom = raisingException("boom");

    // Then
    assertThatCode(boom).isInstanceOf(Exception.class)
                        .hasMessageContaining("boom");
  }

  @Test
  public void should_fail_when_asserting_no_exception_raised_but_exception_occurs() {
    // Given
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);

    // Expect
    thrown.expectAssertionError(shouldNotHaveThrown(exception));

    // When
    assertThatCode(boom).doesNotThrowAnyException();
  }

  @Test
  public void can_use_description_in_error_message() {
    // Given
    ThrowingCallable boom = raisingException("boom");

    // Expect
    thrown.expectWithMessageStartingWith(AssertionError.class, "[Test]");

    // When
    assertThatCode(boom).as("Test").doesNotThrowAnyException();
  }

  @Test
  public void error_message_contains_stacktrace() {
    // Given
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);

    // Expect
    thrown.expectAssertionErrorWithMessageContaining(
      "java.lang.Exception: boom",
      "at org.assertj.core.api.Assertions_assertThatCode_Test.error_message_contains_stacktrace"
    );

    // When
    assertThatCode(boom).doesNotThrowAnyException();
  }

  @Test
  public void should_succeed_when_asserting_no_exception_raised_and_no_exception_occurs() {
    // Given
    ThrowingCallable silent = () -> {
    };

    // Then
    assertThatCode(silent).doesNotThrowAnyException();
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
