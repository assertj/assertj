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
package org.assertj.core.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions_catchThrowableOfType_Test.raisingException;
import static org.assertj.core.api.Assertions_catchThrowable_Test.codeThrowing;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class Assertions_assertThatThrownBy_Test {

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
    then(assertionError).hasMessageContaining("[Test code]");
  }

  @Test
  void should_fail_if_no_throwable_was_thrown() {
    // GIVEN
    ThrowingCallable code = () -> {};
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThatThrownBy(code).hasMessage("boom ?"));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting code to raise a throwable."));
  }

  @Test
  void should_fail_with_good_message_when_assertion_is_failing() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThatThrownBy(raisingException("boom")).hasMessage("bam"));
    // THEN
    then(assertionError).hasMessageContainingAll("Expecting message to be:",
                                                 "\"bam\"",
                                                 "but was:",
                                                 "\"boom\"");
  }

}
