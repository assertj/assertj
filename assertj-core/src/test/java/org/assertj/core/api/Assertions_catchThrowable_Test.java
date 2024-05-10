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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_catchThrowable_Test {

  @Test
  void can_capture_exception_and_then_assert_following_AAA_or_BDD_style() {
    // GIVEN
    Exception exception = new Exception("boom!!");
    // WHEN
    Throwable boom = catchThrowable(codeThrowing(exception));
    // THEN
    then(boom).isSameAs(exception);
  }

  @Test
  void catchThrowable_returns_null_when_no_exception_thrown() {
    // WHEN
    Throwable boom = catchThrowable(() -> {});
    // THEN
    then(boom).isNull();
  }

  static ThrowingCallable codeThrowing(Throwable t) {
    return () -> {
      throw t;
    };
  }
}
