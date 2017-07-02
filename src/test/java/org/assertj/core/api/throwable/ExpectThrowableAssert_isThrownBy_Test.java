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
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.ExpectedException.none;

import java.util.NoSuchElementException;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class ExpectThrowableAssert_isThrownBy_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_build_ExpectThrowableAssert_with_exception_thrown_by_lambda() {
    NoSuchElementException ex = new NoSuchElementException("no such element!");
    // @format:off
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {throw ex;})
                                                     .isSameAs(ex)
                                                     .withNoCause();
    // @format:on
  }

  @Test
  public void should_allow_to_check_exception_thrown_by_lambda() {
    // @format:off
    Throwable exceptionWithCause = new NoSuchElementException("this too").initCause(new IllegalArgumentException("The cause"));
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> { throw exceptionWithCause;})
                                                     .withMessage("this too")
                                                     .withMessage("this %s", "too")
                                                     .withMessageStartingWith("this")
                                                     .withMessageEndingWith("too")
                                                     .withMessageMatching(".*is.*")
                                                     .withStackTraceContaining("this")
                                                     .withCauseExactlyInstanceOf(IllegalArgumentException.class)
                                                     .withCauseInstanceOf(IllegalArgumentException.class);
    // @format:on
  }

  @Test
  public void should_fail_if_nothing_is_thrown_by_lambda() {
    thrown.expectAssertionError("%nExpecting code to raise a throwable.");
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {});
  }
}
