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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.assertj.core.testkit.ThrowingCallableFactory.codeThrowing;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

class ExpectThrowableAssert_isThrownBy_Test {

  @Test
  void should_build_ExpectThrowableAssert_with_exception_thrown_by_lambda() {
    // WHEN
    NoSuchElementException ex = new NoSuchElementException("no such element!");
    // THEN
    thenExceptionOfType(NoSuchElementException.class).isThrownBy(codeThrowing(ex))
                                                     .isSameAs(ex)
                                                     .withNoCause();
  }

  @Test
  void should_allow_to_check_exception_thrown_by_lambda() {
    // WHEN
    Throwable exceptionWithCause = new NoSuchElementException("this too 234", new IllegalArgumentException("The cause"));
    // THEN
    thenExceptionOfType(NoSuchElementException.class).isThrownBy(codeThrowing(exceptionWithCause))
                                                     .withMessage("this too 234")
                                                     .withMessage("this %s %d", "too", 234)
                                                     .withMessageStartingWith("this")
                                                     .withMessageStartingWith("%s", "this")
                                                     .withMessageEndingWith("234")
                                                     .withMessageEndingWith("too %d", 234)
                                                     .withMessageMatching(".*is.*")
                                                     .withStackTraceContaining("this")
                                                     .withStackTraceContaining("is %s", "to")
                                                     .withCauseExactlyInstanceOf(IllegalArgumentException.class)
                                                     .withCauseInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_fail_if_nothing_is_thrown_by_lambda() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {}));
    // THEN
    then(assertionError).hasMessage("%nExpecting code to throw a java.util.NoSuchElementException, but no throwable was thrown.".formatted());
  }
}
