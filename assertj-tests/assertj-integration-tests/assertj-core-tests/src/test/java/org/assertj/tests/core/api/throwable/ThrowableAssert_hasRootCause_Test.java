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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveNoCause.shouldHaveNoCause;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCause;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Throwables.getStackTrace;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ThrowableAssert_hasRootCause_Test {

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual = {0} / expected root cause = {1}")
  @MethodSource("passingData")
  void should_pass_if_root_cause_is_expected(Throwable actual, Throwable expectedRootCause, String testDescription) {
    assertThat(actual).hasRootCause(expectedRootCause);
  }

  // @format:off
  private static Stream<Arguments> passingData() {
    return Stream.of(Arguments.of(withRootCause(new IllegalArgumentException("bang")), new IllegalArgumentException("bang"), "same root cause"),
                     Arguments.of(withCause(new IllegalArgumentException("wibble")), new IllegalArgumentException("wibble"), "same cause"),
                     Arguments.of(new Throwable(), null, "no cause"));
  }
  // @format:on

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: root cause = {0} / expected = {1}")
  @MethodSource("failingData")
  void should_fail_if_root_cause_is_unexpected(Throwable actualRootCause, Throwable expectedRootCause, String testDescription) {
    // GIVEN
    Throwable actual = withRootCause(actualRootCause);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasRootCause(expectedRootCause));
    // THEN
    then(assertionError).hasMessage(shouldHaveRootCause(actual, actualRootCause, expectedRootCause).create());
  }

  // @format:off
  private static Stream<Arguments> failingData() {
    return Stream.of(Arguments.of(null, new RuntimeException(), "no actual cause"),
                     Arguments.of(new IllegalArgumentException(), new NullPointerException(), "different root cause type"),
                     Arguments.of(new IllegalArgumentException("right"), new IllegalArgumentException("wrong"), "different root cause message"),
                     Arguments.of(new IllegalArgumentException(), new IllegalArgumentException("wrong"), "no root cause message"),
                     Arguments.of(new IllegalArgumentException("one"), new NullPointerException("two"), "different root cause type and message"));
  }
  // @format:on

  @Test
  void should_fail_with_explicit_message_if_actual_and_expected_root_cause_have_different_types() {
    // GIVEN
    Throwable rootCause = new IllegalStateException();
    Throwable cause = new RuntimeException(rootCause);
    Throwable actual = new IllegalArgumentException(cause);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasRootCause(new NullPointerException()));
    // THEN
    then(assertionError).hasMessage(format("%n" +
                                           "Expecting a root cause with type:%n" +
                                           "  \"java.lang.NullPointerException\"%n" +
                                           "but type was:%n" +
                                           "  \"java.lang.IllegalStateException\".%n" +
                                           "Throwable that failed the check:%n" +
                                           "%n" +
                                           "%s",
                                           getStackTrace(actual)));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasRootCause(new Throwable()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_expected_root_cause_is_null() {
    // GIVEN
    Throwable actual = withRootCause(new NullPointerException());
    Throwable expected = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasRootCause(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoCause(actual).create());
  }

  private static Throwable withRootCause(Throwable rootCause) {
    Throwable cause = rootCause == null ? null : new RuntimeException("cause", rootCause);
    return withCause(cause);
  }

  private static Throwable withCause(Throwable cause) {
    return new IllegalStateException("throwable", cause);
  }

}
