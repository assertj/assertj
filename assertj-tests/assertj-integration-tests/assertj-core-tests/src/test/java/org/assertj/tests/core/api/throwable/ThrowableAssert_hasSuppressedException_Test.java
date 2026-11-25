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
package org.assertj.tests.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSuppressedException.shouldHaveSuppressedException;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThrowableAssert_hasSuppressedException_Test {

  private static final String IAE_EXCEPTION_MESSAGE = "invalid arg";
  private static final String NPE_EXCEPTION_MESSAGE = "null arg";

  private Throwable throwableWithSuppressedExceptions;

  @BeforeEach
  void setUp() {
    throwableWithSuppressedExceptions = new Throwable();
    throwableWithSuppressedExceptions.addSuppressed(new IllegalArgumentException(IAE_EXCEPTION_MESSAGE));
    throwableWithSuppressedExceptions.addSuppressed(new NullPointerException(NPE_EXCEPTION_MESSAGE));
  }

  @Test
  void should_pass_if_one_of_the_suppressed_exceptions_has_the_expected_type_and_message() {
    assertThat(throwableWithSuppressedExceptions).hasSuppressedException(new IllegalArgumentException(IAE_EXCEPTION_MESSAGE));
  }

  @Test
  void should_pass_if_actual_has_the_expected_suppressed_exception() {
    // GIVEN
    Throwable suppressedException = new NullPointerException();
    Throwable actual = new Throwable();
    actual.addSuppressed(suppressedException);
    // WHEN/THEN
    then(actual).hasSuppressedException(suppressedException);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSuppressedException(new Throwable()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_NullPointerException_if_expected_suppressed_exception_is_null() {
    // GIVEN
    Throwable expectedSuppressedException = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(new Throwable()).hasSuppressedException(expectedSuppressedException));
    // THEN
    then(npe).hasMessage("The expected suppressed exception should not be null");
  }

  @Test
  void should_fail_if_actual_has_no_suppressed_exception_and_expected_suppressed_exception_is_not_null() {
    // GIVEN
    Throwable actual = new NullPointerException("Throwable message");
    Throwable expectedSuppressedException = new Throwable();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSuppressedException(expectedSuppressedException));
    // THEN
    then(assertionError).hasMessage(shouldHaveSuppressedException(actual, expectedSuppressedException).create());
  }

  @Test
  void should_fail_if_suppressed_exception_is_not_instance_of_expected_type() {
    // GIVEN
    Throwable expectedSuppressedException = new NullPointerException(IAE_EXCEPTION_MESSAGE);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(throwableWithSuppressedExceptions).hasSuppressedException(expectedSuppressedException));
    // THEN
    then(assertionError).hasMessage(shouldHaveSuppressedException(throwableWithSuppressedExceptions,
                                                                  expectedSuppressedException).create());
  }

  @Test
  void should_fail_if_suppressed_exception_has_not_the_expected_message() {
    // GIVEN
    Throwable expectedSuppressedException = new IllegalArgumentException(IAE_EXCEPTION_MESSAGE + "foo");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(throwableWithSuppressedExceptions).hasSuppressedException(expectedSuppressedException));
    // THEN
    then(assertionError).hasMessage(shouldHaveSuppressedException(throwableWithSuppressedExceptions,
                                                                  expectedSuppressedException).create());
  }

  @Test
  void should_fail_if_suppressed_exception_has_no_message_and_the_expected_suppressed_exception_has_one() {
    // GIVEN
    Throwable expectedSuppressedException = new IllegalArgumentException("error cause");
    Throwable actual = new Throwable();
    actual.addSuppressed(new IllegalArgumentException());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSuppressedException(expectedSuppressedException));
    // THEN
    then(assertionError).hasMessage(shouldHaveSuppressedException(actual, expectedSuppressedException).create());
  }

  @Test
  void should_fail_if_suppressed_exception_has_different_type_and_message_to_expected_suppressed_exception() {
    // GIVEN
    Throwable expectedSuppressedException = new NullPointerException("error cause");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(throwableWithSuppressedExceptions).hasSuppressedException(expectedSuppressedException));
    // THEN
    then(assertionError).hasMessage(shouldHaveSuppressedException(throwableWithSuppressedExceptions,
                                                                  expectedSuppressedException).create());
  }

}
