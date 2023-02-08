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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalStateException;
import static org.assertj.core.api.Assertions_catchThrowable_Test.codeThrowing;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.mock;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_catchIllegalStateException_Test {

  @Test
  void catchIllegalStateException_should_fail_with_good_message_if_wrong_type() {
    // GIVEN
    ThrowingCallable code = () -> catchIllegalStateException(raisingException("boom!!"));
    // WHEN
    AssertionError assertionError = expectAssertionError(code);
    // THEN
    assertThat(assertionError).hasMessageContainingAll(IllegalStateException.class.getName(), Exception.class.getName());
  }

  @Test
  void catchIllegalStateException_should_succeed_and_return_actual_instance_with_correct_class() {
    // GIVEN
    final IllegalStateException expected = new IllegalStateException("boom!!");
    // WHEN
    IllegalStateException actual = catchIllegalStateException(codeThrowing(expected));
    // THEN
    then(actual).isSameAs(expected);
  }

  @Test
  void catchIllegalStateException_should_succeed_and_return_null_if_no_exception_thrown() {
    // WHEN
    IllegalStateException actual = catchIllegalStateException(() -> {});
    // THEN
    then(actual).isNull();
  }

  @Test
  void catchIllegalStateException_should_catch_mocked_throwable() {
    // GIVEN
    IllegalStateException illegalStateException = mock(IllegalStateException.class);
    // WHEN
    Throwable actual = catchIllegalStateException(codeThrowing(illegalStateException));
    // THEN
    then(actual).isSameAs(illegalStateException);
  }

  static ThrowingCallable raisingException(final String reason) {
    return codeThrowing(new Exception(reason));
  }

}
