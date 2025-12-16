/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_catchThrowable_Test {

  @Test
  void should_capture_exception() {
    // GIVEN
    Exception exception = new Exception("boom!!");
    // WHEN
    Throwable boom = catchThrowable(codeThrowing(exception));
    // THEN
    then(boom).isSameAs(exception);
  }

  @Test
  void catchThrowable_should_fail_when_no_exception_is_thrown() {
    // WHEN
    var error = expectAssertionError(() -> catchThrowable(() -> {}));
    // THEN
    then(error).hasMessage("Expecting code to raise a Throwable");
  }

  static ThrowingCallable codeThrowing(Throwable t) {
    return () -> {
      throw t;
    };
  }
}
