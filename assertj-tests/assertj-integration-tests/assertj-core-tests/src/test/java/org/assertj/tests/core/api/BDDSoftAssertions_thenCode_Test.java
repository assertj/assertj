/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.tests.core.testkit.ThrowingCallableFactory.codeThrowing;

import org.assertj.core.api.BDDSoftAssertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class BDDSoftAssertions_thenCode_Test {

  private final BDDSoftAssertions softly = new BDDSoftAssertions();

  @Test
  void should_succeed_when_asserting_no_exception_was_thrown() {
    // GIVEN
    ThrowingCallable silent = () -> {};
    // WHEN
    softly.thenCode(silent).doesNotThrowAnyException();
    // THEN
    then(softly.errorsCollected()).isEmpty();
  }

  @Test
  void should_collect_error_when_asserting_no_exception_was_thrown_and_an_exception_was_thrown() {
    // GIVEN
    ThrowingCallable boom = codeThrowing(new Exception("boom"));
    // WHEN
    softly.thenCode(boom).doesNotThrowAnyException();
    // THEN
    then(softly.errorsCollected()).singleElement(THROWABLE).hasMessageContaining("boom");
  }

  @Test
  void can_invoke_late_assertion() {
    // GIVEN
    ThrowingCallable boom = codeThrowing(new Exception("boom!"));
    // WHEN
    softly.thenCode(boom).isInstanceOf(Exception.class)
          .hasMessageContaining("boom!");
    // THEN
    then(softly.errorsCollected()).isEmpty();
  }

}
