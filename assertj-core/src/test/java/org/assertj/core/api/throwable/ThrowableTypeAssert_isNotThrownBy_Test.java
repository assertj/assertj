/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.ThrowingCallableFactory.codeThrowing;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class ThrowableTypeAssert_isNotThrownBy_Test {

  @Test
  void should_pass_if_nothing_is_thrown_by_callable_code() {
    assertThatExceptionOfType(Throwable.class).isNotThrownBy(() -> {});
  }

  @Test
  void should_pass_if_given_exception_is_not_thrown_by_callable_code() {
    assertThatExceptionOfType(IllegalArgumentException.class).isNotThrownBy(codeThrowing(new IOException()));
  }

  @Test
  void should_fail_if_given_exception_is_thrown_by_callable_code() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThatExceptionOfType(IOException.class).isNotThrownBy(codeThrowing(new IOException())));
    // THEN
    then(assertionError).hasMessage("Expecting code not to raise a java.io.IOException");
  }

  @Test
  void should_fail_if_a_subclass_of_given_exception_is_thrown_by_callable_code() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThatExceptionOfType(Exception.class).isNotThrownBy(codeThrowing(new IOException())));
    // THEN
    then(assertionError).hasMessage("Expecting code not to raise a java.lang.Exception");
  }

  @Test
  void should_fail_honoring_user_description() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThatExceptionOfType(IOException.class).as("oops")
                                                                                                .isNotThrownBy(codeThrowing(new IOException())));
    // THEN
    then(assertionError).hasMessageContainingAll("oops", "Expecting code not to raise a java.io.IOException");
  }

}
