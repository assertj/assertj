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
import static org.assertj.core.api.Fail.fail;
import static org.assertj.core.api.Fail.shouldHaveThrown;
import static org.assertj.core.test.ExpectedException.none;

import java.io.IOException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class Assertions_catchThrowableOfType_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_build_ThrowableAssert_with_runtime_exception_thrown() {
    assertThatThrownBy(codeThrowing(new IllegalArgumentException("boom"))).isInstanceOf(IllegalArgumentException.class)
                                                                          .hasMessage("boom");
  }

  @Test
  public void should_build_ThrowableAssert_with_throwable_thrown() {
    assertThatThrownBy(codeThrowing(new Throwable("boom"))).isInstanceOf(Throwable.class)
                                                           .hasMessage("boom");
  }

  @Test
  public void should_be_able_to_pass_a_description_to_assertThatThrownBy() {
    Throwable assertionError = catchThrowable(() -> {
      // make assertThatThrownBy fail to verify the description afterwards
      assertThatThrownBy(raisingException("boom"), "Test %s", "code").hasMessage("bam");
    });
    assertThat(assertionError).isInstanceOf(AssertionError.class)
                              .hasMessageContaining("[Test code]");
  }

  @Test
  public void should_fail_if_no_throwable_was_thrown() {
    thrown.expectAssertionError("%nExpecting code to raise a throwable.");
    assertThatThrownBy(() -> {}).hasMessage("boom ?");
  }

  @Test
  public void can_capture_exception_and_then_assert_following_AAA_or_BDD_style() {
    // when
    Exception exception = new Exception("boom!!");
    Throwable boom = catchThrowable(codeThrowing(exception));

    // then
    assertThat(boom).isSameAs(exception);
  }

  @Test
  public void catchThrowable_returns_null_when_no_exception_thrown() {
    // when
    Throwable boom = catchThrowable(() -> {});

    // then
    assertThat(boom).isNull();
  }

  @Test
  public void catchThrowableOfType_should_fail_with_good_message_if_wrong_type() {
    try {
      catchThrowableOfType(raisingException("boom!!"), RuntimeException.class);
    } catch (AssertionError e) {
      assertThat(e).hasMessageContaining(RuntimeException.class.getName())
                   .hasMessageContaining(Exception.class.getName());
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  @Test
  public void catchThrowableOfType_should_succeed_and_return_actual_instance_with_correct_class() {
    final Exception expected = new RuntimeException("boom!!");
    Exception actual = null;
    try {
      actual = catchThrowableOfType(codeThrowing(expected), Exception.class);
    } catch (AssertionError a) {
      fail("catchThrowableOfType should not have asserted", a);
    }
    assertThat(actual).isSameAs(expected);
  }

  @Test
  public void catchThrowableOfType_should_succeed_and_return_null_if_no_exception_thrown() {
    IOException actual = catchThrowableOfType(() -> {}, IOException.class);
    assertThat(actual).isNull();
  }

  @Test
  public void should_fail_with_good_message_when_assertion_is_failing() {
    thrown.expectAssertionErrorWithMessageContaining("Expecting message:",
                                                     "<\"bam\">",
                                                     "but was:",
                                                     "<\"boom\">");
    assertThatThrownBy(raisingException("boom")).hasMessage("bam");
  }

  private ThrowingCallable raisingException(final String reason) {
    return codeThrowing(new Exception(reason));
  }

  protected static ThrowingCallable codeThrowing(Throwable t) {
    return () -> {
      throw t;
    };
  }

}
