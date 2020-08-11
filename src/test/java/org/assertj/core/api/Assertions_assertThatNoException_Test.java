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
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

public class Assertions_assertThatNoException_Test {

  @Test
  public void should_fail_when_asserting_no_exception_raised_but_exception_occurs() {
    // Given
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);

    // Expect
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->{
      // When;
      assertThatNoException().isThrownBy(boom);
    }).withMessage(shouldNotHaveThrown(exception).create());
  }

  @Test
  public void can_use_description_in_error_message() {
    // Given
    ThrowingCallable boom = raisingException("boom");

    // Expect
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThatNoException().as("Test")
                                                                                         .isThrownBy(boom))
                                                   .withMessageStartingWith("[Test]");
  }

  @Test
  public void error_message_contains_stacktrace() {
    // Given
    Exception exception = new Exception("boom");
    ThrowingCallable boom = raisingException(exception);

    // Then
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThatNoException().isThrownBy(boom))
                                                   .withMessageContaining("java.lang.Exception: %s", "boom")
                                                   .withMessageContaining("at org.assertj.core.api.Assertions_assertThatNoException_Test.error_message_contains_stacktrace");
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
