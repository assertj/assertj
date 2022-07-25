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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.Assertions_catchThrowable_Test.codeThrowing;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_catchThrowableOfType_Test {

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

  @Test
  void catchThrowableOfType_should_fail_with_good_message_if_wrong_type() {
    // GIVEN
    ThrowingCallable code = () -> catchThrowableOfType(raisingException("boom!!"), RuntimeException.class);
    // WHEN
    AssertionError assertionError = expectAssertionError(code);
    // THEN
    assertThat(assertionError).hasMessageContainingAll(RuntimeException.class.getName(), Exception.class.getName());
  }

  @Test
  void catchThrowableOfType_should_succeed_and_return_actual_instance_with_correct_class() {
    // GIVEN
    final Exception expected = new RuntimeException("boom!!");
    // WHEN
    Exception actual = catchThrowableOfType(codeThrowing(expected), Exception.class);
    // THEN
    then(actual).isSameAs(expected);
  }

  @Test
  void catchThrowableOfType_should_succeed_and_return_null_if_no_exception_thrown() {
    // WHEN
    IOException actual = catchThrowableOfType(() -> {}, IOException.class);
    // THEN
    then(actual).isNull();
  }

  @Test
  void should_catch_mocked_throwable() {
    // GIVEN
    Throwable throwable = mock(Throwable.class);
    // WHEN
    Throwable actual = catchThrowableOfType(codeThrowing(throwable), Throwable.class);
    // THEN
    then(actual).isSameAs(throwable);
  }

  static ThrowingCallable raisingException(final String reason) {
    return codeThrowing(new Exception(reason));
  }

}
