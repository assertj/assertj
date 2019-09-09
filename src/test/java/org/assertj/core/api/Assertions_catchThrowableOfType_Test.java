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

import java.io.IOException;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_catchThrowableOfType_Test {

  @Test
  void should_build_ThrowableAssert_with_runtime_exception_thrown() {
    assertThatThrownBy(codeThrowing(new IllegalArgumentException("boom"))).isInstanceOf(IllegalArgumentException.class)
                                                                          .hasMessage("boom");
  }

  @Test
  void should_build_ThrowableAssert_with_throwable_thrown() {
    assertThatThrownBy(codeThrowing(new Throwable("boom"))).isInstanceOf(Throwable.class)
                                                           .hasMessage("boom");
  }

  @Test
  void should_be_able_to_pass_a_description_to_assertThatThrownBy() {
    // GIVEN
    // make assertThatThrownBy fail to verify the description afterwards
    ThrowingCallable code = () -> assertThatThrownBy(raisingException("boom"), "Test %s", "code").hasMessage("bam");
    // WHEN
    AssertionError assertionError = expectAssertionError(code);
    // THEN
    assertThat(assertionError).hasMessageContaining("[Test code]");
  }

  @Test
  void should_fail_if_no_throwable_was_thrown() {
    // GIVEN
    ThrowingCallable code = () -> {};
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThatThrownBy(code).hasMessage("boom ?"));
    // THEN
    assertThat(assertionError).hasMessage(format("%nExpecting code to raise a throwable."));
  }

  @Test
  void can_capture_exception_and_then_assert_following_AAA_or_BDD_style() {
    // GIVEN
    Exception exception = new Exception("boom!!");
    // WHEN
    Throwable boom = catchThrowable(codeThrowing(exception));
    // THEN
    assertThat(boom).isSameAs(exception);
  }

  @Test
  void catchThrowable_returns_null_when_no_exception_thrown() {
    // WHEN
    Throwable boom = catchThrowable(() -> {});
    // THEN
    assertThat(boom).isNull();
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
    assertThat(actual).isSameAs(expected);
  }

  @Test
  void catchThrowableOfType_should_succeed_and_return_null_if_no_exception_thrown() {
    IOException actual = catchThrowableOfType(() -> {}, IOException.class);
    assertThat(actual).isNull();
  }

  @Test
  void should_fail_with_good_message_when_assertion_is_failing() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThatThrownBy(raisingException("boom")).hasMessage("bam"))
                                                   .withMessageContaining("Expecting message to be:")
                                                   .withMessageContaining("<\"bam\">")
                                                   .withMessageContaining("but was:")
                                                   .withMessageContaining("<\"boom\">");
  }

  @Test
  void should_pass_if_message_does_not_contain_description() {
    assertThatExceptionOfType(Exception.class).isThrownBy(codeThrowing(new Exception("boom")))
                                              .withMessageNotContaining("bam");
  }

  private static ThrowingCallable raisingException(final String reason) {
    return codeThrowing(new Exception(reason));
  }

  private static ThrowingCallable codeThrowing(Throwable t) {
    return () -> {
      throw t;
    };
  }
}
