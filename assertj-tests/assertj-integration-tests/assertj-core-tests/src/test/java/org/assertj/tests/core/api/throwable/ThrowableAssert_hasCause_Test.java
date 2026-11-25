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
import static org.assertj.core.error.ShouldHaveCause.shouldHaveCause;
import static org.assertj.core.error.ShouldHaveNoCause.shouldHaveNoCause;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Throwables.getStackTrace;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ThrowableAssert_hasCause_Test {

  @Test
  void should_pass_if_cause_has_expected_type_and_message() {
    // GIVEN
    Throwable cause = new IllegalArgumentException("wibble");
    Throwable expected = new IllegalArgumentException("wibble");
    Throwable actual = withCause(cause);
    // WHEN/THEN
    then(actual).hasCause(expected);
  }

  @Test
  void should_pass_if_actual_has_no_cause_and_expected_cause_is_null() {
    // GIVEN
    Throwable actual = withCause(null);
    Throwable expected = null;
    // WHEN/THEN
    then(actual).hasCause(expected);
  }

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: cause = {0} / expected = {1}")
  @MethodSource("failingData")
  void should_fail_if_cause_is_unexpected(Throwable cause, Throwable expected, String testDescription) {
    // GIVEN
    Throwable actual = withCause(cause);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasCause(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveCause(actual, expected).create());
  }

  // @format:off
  private static Stream<Arguments> failingData() {
    return Stream.of(Arguments.of(null, new Throwable(), "no actual cause"),
                     Arguments.of(new NullPointerException(), new IllegalArgumentException(), "different cause type"),
                     Arguments.of(new NullPointerException("right"), new NullPointerException("wrong"), "different cause message"),
                     Arguments.of(new NullPointerException(), new NullPointerException("wrong"), "no cause message"),
                     Arguments.of(new IllegalArgumentException("right"), new NullPointerException("wrong"), "different cause type and message"));
  }
  // @format:on

  @Test
  void should_fail_with_explicit_message_if_actual_and_expected_cause_have_different_types() {
    // GIVEN
    Throwable actual = new IllegalArgumentException(new IllegalStateException());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasCause(new NullPointerException()));
    // THEN
    then(assertionError).hasMessage(format("%n" +
                                           "Expecting a cause with type:%n" +
                                           "  \"java.lang.NullPointerException\"%n" +
                                           "but type was:%n" +
                                           "  \"java.lang.IllegalStateException\".%n%n" +
                                           "Throwable that failed the check:%n" +
                                           "%s",
                                           getStackTrace(actual)));
  }

  @Test
  void should_fail_if_expected_cause_is_null() {
    // GIVEN
    Throwable actual = withCause(new Throwable());
    Throwable expected = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasCause(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoCause(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasCause(new Throwable()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  private static Throwable withCause(Throwable cause) {
    return new Throwable("bang!", cause);
  }

}
