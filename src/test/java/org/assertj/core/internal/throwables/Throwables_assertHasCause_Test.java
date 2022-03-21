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
import static org.assertj.core.error.ShouldHaveCause.shouldHaveCause;
import static org.assertj.core.error.ShouldHaveNoCause.shouldHaveNoCause;
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

class Throwables_assertHasCause_Test extends ThrowablesBaseTest {

  private static final AssertionInfo INFO = someInfo();

  @Test
  void should_pass_if_cause_has_expected_type_and_message() {
    // GIVEN
    Throwable cause = new IllegalArgumentException("wibble");
    Throwable expected = new IllegalArgumentException("wibble");
    Throwable throwable = withCause(cause);

    // WHEN
    throwables.assertHasCause(INFO, throwable, expected);

    // THEN
    // no exception thrown
  }

  @Test
  void should_pass_if_actual_has_no_cause_and_expected_cause_is_null() {
    // GIVEN
    Throwable cause = null;
    Throwable expected = null;
    Throwable throwable = withCause(cause);

    // WHEN
    throwables.assertHasCause(INFO, throwable, expected);

    // THEN
    // no exception thrown
  }

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: cause = {0} / expected = {1}")
  @MethodSource("failingData")
  void should_fail_if_cause_is_unexpected(final Throwable cause,
                                          final Throwable expected,
                                          String testDescription) {
    // GIVEN
    final Throwable throwable = withCause(cause);

    // WHEN
    expectAssertionError(() -> throwables.assertHasCause(INFO, throwable, expected));

    // THEN
    verify(failures).failure(INFO, shouldHaveCause(cause, expected));
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
  void should_fail_if_expected_cause_is_null() {
    // GIVEN
    final Throwable throwable = withCause(new Throwable());
    final Throwable expected = null;

    // WHEN
    expectAssertionError(() -> throwables.assertHasCause(INFO, throwable, expected));

    // THEN
    verify(failures).failure(INFO, shouldHaveNoCause(throwable));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    final Throwable throwable = null;
    final Throwable expected = new Throwable();

    // WHEN
    AssertionError actual = expectAssertionError(() -> throwables.assertHasCause(INFO, throwable, expected));

    // THEN
    assertThat(actual).hasMessage(actualIsNull());
  }

  private static Throwable withCause(Throwable cause) {
    return new Throwable("bang!", cause);
  }
}
