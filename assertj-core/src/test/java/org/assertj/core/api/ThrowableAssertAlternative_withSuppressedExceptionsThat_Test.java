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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.ThrowingCallableFactory.codeThrowing;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThrowableAssertAlternative_withSuppressedExceptionsThat_Test {

  private final Throwable throwable = new Exception("initial error");

  @BeforeEach
  public final void setUp() {
    Throwable invalidArgException = new IllegalArgumentException("invalid argument");
    Throwable ioException = new IOException("IO error");
    throwable.addSuppressed(invalidArgException);
    throwable.addSuppressed(ioException);
  }

  @Test
  void should_return_suppressed_exceptions_assertion() {
    // WHEN
    var suppressedExceptionsAssert = new ThrowableAssertAlternative<>(throwable).withSuppressedExceptionsThat();
    // THEN
    then(suppressedExceptionsAssert.actual).containsExactly(throwable.getSuppressed());
  }

  @Test
  void should_be_able_to_call_throwable_array_assertions_on_actual_suppressed_exceptions() {
    assertThatException().isThrownBy(codeThrowing(throwable))
                         .withSuppressedExceptionsThat()
                         .hasSize(2)
                         .containsExactly(throwable.getSuppressed());
  }

  @Test
  void should_be_able_to_return_to_the_initial_throwable_and_chain_throwable_assertions() {
    assertThatException().isThrownBy(codeThrowing(throwable))
                         .withSuppressedExceptionsThat()
                         .isNotEmpty()
                         .returnToInitialThrowable()
                         .isSameAs(throwable)
                         .hasMessage("initial error");
  }

  @Test
  void should_add_navigation_description_if_none_was_set() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThatException().isThrownBy(codeThrowing(throwable))
                                                                         .withSuppressedExceptionsThat()
                                                                         .isEmpty());
    // THEN
    then(assertionError).hasMessageContaining("[checking suppressed exceptions]");
  }

  @Test
  void should_ignore_navigation_description_if_a_description_was_set() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThatException().isThrownBy(codeThrowing(throwable))
                                                                         .withSuppressedExceptionsThat()
                                                                         .as("my desc")
                                                                         .isEmpty());
    // THEN
    then(assertionError).message()
                        .contains("my desc")
                        .doesNotContain("checking suppressed exceptions");
  }

  @Test
  void should_fail_if_throwable_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var nullPointerException = catchNullPointerException(() -> new ThrowableAssertAlternative<>(actual).withSuppressedExceptionsThat());
    // THEN
    then(nullPointerException).hasMessage("Can not perform assertions on the suppressed exceptions of a null throwable.");
  }

}
