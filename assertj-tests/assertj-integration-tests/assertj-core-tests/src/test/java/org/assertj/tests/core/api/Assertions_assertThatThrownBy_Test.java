/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveMessage.shouldHaveMessage;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_assertThatThrownBy_Test {

  @Test
  void should_work_with_runtime_exception_thrown() {
    // WHEN/THEN
    assertThatThrownBy(codeThrowing(new IllegalArgumentException("boom"))).isInstanceOf(IllegalArgumentException.class)
                                                                          .hasMessage("boom");
  }

  @Test
  void should_work_with_throwable_thrown() {
    // WHEN/THEN
    assertThatThrownBy(codeThrowing(new Throwable("boom"))).isInstanceOf(Throwable.class)
                                                           .hasMessage("boom");
  }

  @Test
  void should_work_with_method_reference_having_vararg_parameter() {
    // WHEN/THEN
    assertThatThrownBy(Assertions_assertThatThrownBy_Test::methodThrowing).isInstanceOf(Exception.class)
                                                                          .hasMessage("boom");
  }

  @Test
  void should_support_description() {
    // GIVEN
    Throwable throwable = new Exception("boom");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThatThrownBy(codeThrowing(throwable), "Test %s",
                                                                                  "code").hasMessage("bam"));
    // THEN
    then(assertionError).hasMessageContaining("[Test code]");
  }

  @Test
  void should_fail_if_no_throwable_was_thrown() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThatThrownBy(() -> {}).hasMessage("boom ?"));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting code to raise a throwable."));
  }

  @Test
  void should_fail_with_proper_message_when_assertion_is_failing() {
    // GIVEN
    Throwable throwable = new Exception("boom");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThatThrownBy(codeThrowing(throwable)).hasMessage("bam"));
    // THEN
    then(assertionError).hasMessage(shouldHaveMessage(throwable, "bam").create());
  }

  private static ThrowingCallable codeThrowing(Throwable t) {
    return () -> {
      throw t;
    };
  }

  private static void methodThrowing(Object... parameters) throws Exception {
    throw new Exception("boom");
  }

}
