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
package org.assertj.core.internal.throwables;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotHaveStackTrace.shouldNotHaveStackTrace;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Throwables;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Throwables#assertDoesNotHaveStackTrace(AssertionInfo, Throwable)}.
 *
 * @author Ashley Scopes
 */
class Throwables_assertDoesNotHaveStackTrace_Test extends ThrowablesBaseTest {
  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;

    // THEN
    assertThatThrownBy(() -> throwables.assertDoesNotHaveStackTrace(someInfo(), actual))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_NullPointerException_if_stack_trace_is_null() {
    // GIVEN
    Throwable actual = mock(Throwable.class);

    when(actual.getStackTrace())
      .thenReturn(null);

    // THEN
    assertThatThrownBy(() -> throwables.assertDoesNotHaveStackTrace(someInfo(), actual))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("Expected the throwable object's stack trace to not be null. This is likely a bug in your code or test.");
  }

  @Test
  void should_fail_if_stack_trace_is_present() {
    // GIVEN
    Throwable actual = new Throwable("Blah blah").fillInStackTrace();;

    // THEN
    assertThatThrownBy(() -> throwables.assertDoesNotHaveStackTrace(someInfo(), actual))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotHaveStackTrace(actual).create());
  }

  @Test
  void should_succeed_if_stack_trace_is_not_present() {
    // GIVEN
    Throwable actual = new StacklessException();

    // THEN
    assertThatCode(() -> throwables.assertDoesNotHaveStackTrace(someInfo(), actual))
      .doesNotThrowAnyException();
  }

  private static class StacklessException extends RuntimeException {
    private StacklessException() {
      super("Blah blah", null, false, false);
    }
  }
}
