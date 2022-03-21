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
package org.assertj.core.internal.throwables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveNoCause.shouldHaveNoCause;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCause;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Throwables_assertHasRootCause_Test extends ThrowablesBaseTest {

  private static final AssertionInfo INFO = someInfo();

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: throwable = {0} / expected root cause = {1}")
  @MethodSource("passingData")
  void should_pass_if_root_cause_is_expected(Throwable throwable,
                                             Throwable expectedRootCause,
                                             String testDescription) {
    // WHEN
    throwables.assertHasRootCause(INFO, throwable, expectedRootCause);
    // THEN
    // no exception thrown
  }

  // @format:off
  private static Stream<Arguments> passingData() {
    return Stream.of(Arguments.of(withRootCause(new IllegalArgumentException("bang")), new IllegalArgumentException("bang"), "same root cause"),
                     Arguments.of(withCause(new IllegalArgumentException("wibble")), new IllegalArgumentException("wibble"), "same cause"),
                     Arguments.of(new Throwable(), null, "no cause"));
  }
  // @format:on

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: throwable = {0} / expected = {1}")
  @MethodSource("failingData")
  void should_fail_if_root_cause_is_unexpected(final Throwable actualRootCause,
                                               final Throwable unexpectedRootCause,
                                               String testDescription) {
    // GIVEN
    final Throwable throwable = withRootCause(actualRootCause);
    // WHEN
    expectAssertionError(() -> throwables.assertHasRootCause(INFO, throwable, unexpectedRootCause));
    // THEN
    verify(failures).failure(INFO, shouldHaveRootCause(throwable, actualRootCause, unexpectedRootCause));
  }

  // @format:off
  private static Stream<Arguments> failingData() {
    return Stream.of(Arguments.of(null, new RuntimeException(), "no actual cause"),
                     Arguments.of(new IllegalArgumentException(), new NullPointerException(), "different root cause type"),
                     Arguments.of(new IllegalArgumentException("right"), new IllegalArgumentException("wrong"), "different root cause message"),
                     Arguments.of(new IllegalArgumentException(), new IllegalArgumentException("wrong"), "no root cause message"),
                     Arguments.of(new IllegalArgumentException("one"), new IllegalArgumentException("two"), "different root cause type and message"));
  }
  // @format:on

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    final Throwable throwable = null;
    final Throwable expected = new Throwable();
    // WHEN
    AssertionError actual = expectAssertionError(() -> throwables.assertHasRootCause(INFO, throwable, expected));
    // THEN
    assertThat(actual).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_root_cause_is_null() {
    // GIVEN
    Throwable rootCause = new NullPointerException();
    final Throwable throwable = withRootCause(rootCause);
    final Throwable expected = null;
    // WHEN
    expectAssertionError(() -> throwables.assertHasRootCause(INFO, throwable, expected));
    // THEN
    verify(failures).failure(INFO, shouldHaveNoCause(throwable));
  }

  private static Throwable withRootCause(Throwable rootCause) {
    Throwable cause = rootCause == null ? null : new RuntimeException("cause", rootCause);
    return withCause(cause);
  }

  private static Throwable withCause(Throwable cause) {
    return new IllegalStateException("throwable", cause);
  }
}
