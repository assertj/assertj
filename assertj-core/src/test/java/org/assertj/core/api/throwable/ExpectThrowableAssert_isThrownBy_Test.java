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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.throwable;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

class ExpectThrowableAssert_isThrownBy_Test {

  @Test
  void should_build_ExpectThrowableAssert_with_exception_thrown_by_lambda() {
    NoSuchElementException ex = new NoSuchElementException("no such element!");
    // @format:off
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {throw ex;})
                                                     .isSameAs(ex)
                                                     .withNoCause();
    // @format:on
  }

  @Test
  void should_allow_to_check_exception_thrown_by_lambda() {
    // @format:off
    Throwable exceptionWithCause = new NoSuchElementException("this too 234").initCause(new IllegalArgumentException("The cause"));
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> { throw exceptionWithCause;})
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
    // @format:on
  }

  @Test
  void should_fail_if_nothing_is_thrown_by_lambda() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {}))
                                                   .withMessage(format("%nExpecting code to raise a throwable."));
  }
}
