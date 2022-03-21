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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class ThrowingConsumer_accept_Test {

  @Test
  void should_rethrow_checked_exception_as_runtime_exception() {
    // GIVEN
    IOException ioException = new IOException("boom!");
    ThrowingConsumer<?> throwingConsumer = throwingConsumer(ioException);
    // WHEN
    Throwable throwable = catchThrowable(() -> throwingConsumer.accept(null));
    // THEN
    then(throwable).isExactlyInstanceOf(RuntimeException.class)
                   .hasCause(ioException);
  }

  @Test
  void should_rethrow_runtime_exception_as_is() {
    // GIVEN
    RuntimeException runtimeException = new RuntimeException("boom!");
    ThrowingConsumer<?> throwingConsumer = throwingConsumer(runtimeException);
    // WHEN
    Throwable throwable = catchThrowable(() -> throwingConsumer.accept(null));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

  @Test
  void should_rethrow_assertion_error_as_is() {
    // GIVEN
    AssertionError assertionError = new AssertionError("boom!");
    ThrowingConsumer<?> throwingConsumer = throwingConsumer(assertionError);
    // WHEN
    Throwable throwable = catchThrowable(() -> throwingConsumer.accept(null));
    // THEN
    then(throwable).isSameAs(assertionError);
  }

  private static ThrowingConsumer<?> throwingConsumer(Throwable throwable) {
    return value -> {
      throw throwable;
    };
  }

}
