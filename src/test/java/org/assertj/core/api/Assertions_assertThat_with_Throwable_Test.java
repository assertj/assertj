/*
 * Created on Feb 8, 2011
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(BigDecimal)}</code>.
 */
public class Assertions_assertThat_with_Throwable_Test {

  @Test
  public void can_capture_exception_and_then_assert_following_AAA_or_BDD_style() throws Exception {
    // given
    // some preconditions

    // when
    Throwable boom = Assertions.catchThrowable(raisingException("boom!!!!"));

    // then
    assertThat(boom)
            .isInstanceOf(Exception.class)
            .hasMessageContaining("boom");
  }

  @Test
  public void can_capture_and_assert_with_AssertJ() throws Exception {
    Assertions.assertThatThrownBy(raisingException("boom!!!!"))
            .isInstanceOf(Exception.class)
            .hasMessageContaining("boom");
  }

  @Test
  public void fail_with_good_message_when_nothing_was_captured() throws Exception {
    try {
      Assertions.assertThatThrownBy(notRaisingException())
              .hasMessage("yo");
    } catch (AssertionError ae) {
      Assertions.assertThat(ae).hasMessageContaining("Expected a throwable to have been raised");
    }
  }

  @Test
  public void fail_with_good_message_when_assertion_is_failing() throws Exception {
    try {
      Assertions.assertThatThrownBy(raisingException("boom"))
              .hasMessage("yo");
    } catch (AssertionError ae) {
      Assertions.assertThat(ae)
              .hasMessageContaining("Expecting message:")
              .hasMessageContaining("<\"yo\">")
              .hasMessageContaining("but was:")
              .hasMessageContaining("<\"boom\">");
    }
  }

  private ThrowingCallable notRaisingException() {
    return new ThrowingCallable() {
      @Override public void call() throws Throwable { }
    };
  }

  private ThrowingCallable raisingException(final String reason) {
    return new ThrowingCallable() {
      @Override public void call() throws Throwable {
        throw new Exception(reason);
      }
    };
  }
}
