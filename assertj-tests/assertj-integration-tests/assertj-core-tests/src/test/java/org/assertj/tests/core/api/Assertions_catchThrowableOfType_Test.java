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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.tests.core.testkit.ThrowingCallableFactory.codeThrowing;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_catchThrowableOfType_Test {

  @Test
  void should_capture_exception_of_specific_type() {
    // GIVEN
    IOException exception = new IOException("boom!!");
    // WHEN
    Throwable boom = catchThrowableOfType(IOException.class, codeThrowing(exception));
    // THEN
    then(boom).isSameAs(exception);
  }

  @Test
  void catchThrowableOfType_should_fail_with_helpful_message_if_caught_exception_is_not_of_the_right_type() {
    // GIVEN
    Exception exception = new Exception("boom !");
    ThrowingCallable code = () -> catchThrowableOfType(RuntimeException.class, codeThrowing(exception));
    // WHEN
    AssertionError error = expectAssertionError(code);
    // THEN
    then(error).hasMessage("[Checking code thrown Throwable] " + shouldBeInstance(exception, RuntimeException.class).create());
  }

  @Test
  void catchThrowableOfType_should_succeed_and_return_actual_instance_with_correct_runtime_type() {
    // GIVEN
    final Exception expected = new RuntimeException("boom!!");
    // WHEN
    Exception actual = catchThrowableOfType(Exception.class, codeThrowing(expected));
    // THEN
    then(actual).isSameAs(expected);
  }

  @Test
  void catchThrowableOfType_should_fail_if_no_exception_is_thrown() {
    // WHEN
    AssertionError error = expectAssertionError(() -> catchThrowableOfType(IOException.class, () -> {}));
    // THEN
    then(error).hasMessage("Expecting code to raise an IOException");
  }

  @Test
  void should_catch_mocked_throwable() {
    // GIVEN
    Throwable throwable = mock();
    // WHEN
    Throwable actual = catchThrowableOfType(Throwable.class, codeThrowing(throwable));
    // THEN
    then(actual).isSameAs(throwable);
  }

}
