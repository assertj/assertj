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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

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
    thrown.expectAssertionError("Expecting code to raise a throwable.");
    assertThatThrownBy(() -> {}).hasMessage("yo");
  }

  @Test
  public void can_capture_exception_and_then_assert_following_AAA_or_BDD_style() {
    // when
    Throwable boom = catchThrowable(raisingException("boom!!!!"));

    // then
    assertThat(boom).isInstanceOf(Exception.class)
                    .hasMessageContaining("boom");
  }

  @Test
  public void fail_with_good_message_when_assertion_is_failing() {
    thrown.expectAssertionErrorWithMessageContaining("Expecting message:",
                                                     "<\"yo\">",
                                                     "but was:",
                                                     "<\"boom\">");
    assertThatThrownBy(raisingException("boom")).hasMessage("yo");
  }

  @Test
  public void can_invoke_late_assertion_on_assertThat_ThrowingCallable() {
    // Given
    ThrowingCallable boom = raisingException("boom");

    try {
      // When
      assertThatCode(boom).isInstanceOf(Exception.class)
                          .hasMessageContaining("boom");

    } catch (AssertionError assertionError) {
      // Then
      fail("Assertion error expected");
    }
  }

  @Test
  public void should_fail_when_asserting_not_exception_raised() {
    // Given
    ThrowingCallable boom = raisingException("boom");

    try {
      // When
      assertThatCode(boom).as("Test").doesNotThrowAnyException();
    } catch (AssertionError assertionError) {
      // Then
      assertThat(assertionError).hasMessage(format("[Test] %n" +
                                                   "Expecting code not to raise a throwable but caught a%n" +
                                                   "  <java.lang.Exception>%n" +
                                                   "with message :%n" +
                                                   "  \"boom\""));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_not_fail_when_asserting_not_exception_raised() {
    ThrowingCallable callable = new ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        throw new Exception("boom!");
      }
    };

    // Given
    ThrowingCallable silent = () -> {};

    try {
      // When
      assertThatCode(silent).doesNotThrowAnyException();
    } catch (AssertionError assertionError) {
      // Then
      fail("Assertion error not expected");
    }
  }

  private ThrowingCallable raisingException(final String reason) {
    return () -> {
      throw new Exception(reason);
    };
  }
}
