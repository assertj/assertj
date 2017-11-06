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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class Assertions_assertThat_with_Throwable_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_build_ThrowableAssert_with_runtime_exception_thrown() {
    assertThatThrownBy(() -> {
      throw new IllegalArgumentException("something was wrong");
    }).isInstanceOf(IllegalArgumentException.class)
      .hasMessage("something was wrong");
  }

  @Test
  public void should_build_ThrowableAssert_with_throwable_thrown() {
    assertThatThrownBy(() -> {
      throw new Throwable("something was wrong");
    }).isInstanceOf(Throwable.class)
      .hasMessage("something was wrong");
  }

  @Test
  public void should_fail_if_no_throwable_was_thrown() {
    thrown.expectAssertionError("%nExpecting code to raise a throwable.");
    assertThatThrownBy(() -> {}).hasMessage("yo");
  }

  @Test
  public void can_capture_exception_and_then_assert_following_AAA_or_BDD_style() {
    // when
    Exception e = new Exception("boom!!");
    Throwable boom = catchThrowable(raisingException(e));

    // then
    assertThat(boom).isSameAs(e);
  }

  @Test
  public void catchThrowableOfType_should_fail_with_good_message_if_wrong_type() {
    boolean failed = false;
    try {
      catchThrowableOfType(raisingException("boom!!"), RuntimeException.class);
      failed = true;
    } catch (AssertionError e) {
      assertThat(e)
        .hasMessageContaining(RuntimeException.class.getName())
        .hasMessageContaining(Exception.class.getName());
    }
    if (failed) {
      Fail.shouldHaveThrown(AssertionError.class);
    }
  }

  @Test
  public void catchThrowableOfType_should_succeed_and_return_actual_instance_with_correct_class() {
    final Exception expected = new RuntimeException("boom!!");
    Exception actual = null;
    try {
      actual = catchThrowableOfType(raisingException(expected), Exception.class);
    } catch (AssertionError a) {
      Fail.fail("catchThrowableOfType should not have asserted", a);
    }
    assertThat(actual).isSameAs(expected);
  }

  @Test
  public void should_fail_with_good_message_when_assertion_is_failing() {
    thrown.expectAssertionErrorWithMessageContaining("Expecting message:",
                                                     "<\"yo\">",
                                                     "but was:",
                                                     "<\"boom\">");
    assertThatThrownBy(raisingException("boom")).hasMessage("yo");
  }

  private ThrowingCallable raisingException(final String reason) {
    return () -> {
      throw new Exception(reason);
    };
  }

  private ThrowingCallable raisingException(final Throwable t) {
	  return () -> { throw t.fillInStackTrace(); };
  }
}
