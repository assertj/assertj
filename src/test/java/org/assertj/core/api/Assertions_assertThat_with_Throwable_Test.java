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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.io.IOException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

public class Assertions_assertThat_with_Throwable_Test {

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
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThatThrownBy(() -> {}).hasMessage("boom ?"))
                                                   .withMessage(format("%nExpecting code to raise a throwable."));
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
  public void catchThrowableOfType_should_fail_with_a_message_containing_the_original_stack_trace_when_the_wrong_Throwable_type_was_thrown() {
    // GIVEN
    final Exception exception = new Exception("boom!!");
    ThrowingCallable codeThrowingException = codeThrowing(exception);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> catchThrowableOfType(codeThrowingException, IOException.class));
    // THEN
    assertThat(assertionError).hasMessageContaining(IOException.class.getName())
                              .hasMessageContaining(Exception.class.getName())
                              .hasMessageContaining(getStackTrace(exception));
  }

  @Test
  public void catchThrowableOfType_should_succeed_and_return_actual_instance_with_correct_class() {
    // GIVEN
    final Exception expected = new RuntimeException("boom!!");
    // WHEN
    Exception exception = catchThrowableOfType(codeThrowing(expected), Exception.class);
    // THEN
    assertThat(exception).isSameAs(expected);
  }

  @Test
  public void catchThrowableOfType_should_succeed_and_return_null_if_no_exception_thrown() {
    IOException actual = catchThrowableOfType(() -> {}, IOException.class);
    assertThat(actual).isNull();
  }

  @Test
  public void should_fail_with_good_message_when_assertion_is_failing() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThatThrownBy(raisingException("boom")).hasMessage("bam"))
                                                   .withMessageContaining("Expecting message:")
                                                   .withMessageContaining("<\"bam\">").withMessageContaining("but was:")
                                                   .withMessageContaining("<\"boom\">");
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
