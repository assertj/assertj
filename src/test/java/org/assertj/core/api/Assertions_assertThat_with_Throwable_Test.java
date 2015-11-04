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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

public class Assertions_assertThat_with_Throwable_Test {

  @Test
  public void should_build_ThrowableAssert_with_runtime_exception_thrown() {
    assertThatThrownBy(new ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        throw new IllegalArgumentException("something was wrong");
      }
    }).isInstanceOf(IllegalArgumentException.class)
      .hasMessage("something was wrong");
  }

  @Test
  public void should_build_ThrowableAssert_with_throwable_thrown() {
    assertThatThrownBy(new ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        throw new Throwable("something was wrong");
      }
    }).isInstanceOf(Throwable.class)
      .hasMessage("something was wrong");
  }

  @Test
  public void should_fail_if_no_throwable_was_thrown() {
    try {
      assertThatThrownBy(new ThrowingCallable() {
        @Override
        public void call() throws Throwable {
          // noop
        }
      }).hasMessage("yo");
    } catch (AssertionError e) {
      assertThat(e).hasMessage("Expecting code to raise a throwable.");
      return;
    }
    failBecauseExceptionWasNotThrown(AssertionError.class);
  }

  @Test
  public void can_capture_exception_and_then_assert_following_AAA_or_BDD_style() {
    // given
    // some preconditions

    // when
    Throwable boom = catchThrowable(raisingException("boom!!!!"));

    // then
    assertThat(boom).isInstanceOf(Exception.class)
                    .hasMessageContaining("boom");
  }

  @Test
  public void fail_with_good_message_when_assertion_is_failing() {
    try {
      assertThatThrownBy(raisingException("boom")).hasMessage("yo");
    } catch (AssertionError ae) {
      assertThat(ae).hasMessageContaining("Expecting message:")
                    .hasMessageContaining("<\"yo\">")
                    .hasMessageContaining("but was:")
                    .hasMessageContaining("<\"boom\">");
      return;
    }
    failBecauseExceptionWasNotThrown(AssertionError.class);
  }

  private ThrowingCallable raisingException(final String reason) {
    return new ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        throw new Exception(reason);
      }
    };
  }
}
