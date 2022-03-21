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
import static org.assertj.core.error.ShouldHaveCauseReference.shouldHaveCauseReference;
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

class Throwables_assertHasCauseReference_Test extends ThrowablesBaseTest {

  private static final AssertionInfo INFO = someInfo();

  @Test
  void should_pass_if_actual_cause_and_expected_cause_are_the_same_instance() {
    // GIVEN
    Throwable cause = new IllegalArgumentException("wibble");
    Throwable throwable = withCause(cause);
    // WHEN
    throwables.assertHasCauseReference(INFO, throwable, cause);
    // THEN
    // no exception thrown
  }

  @Test
  void should_pass_if_both_actual_and_expected_causes_are_null() {
    // GIVEN
    Throwable cause = null;
    Throwable throwable = withCause(cause);
    // WHEN
    throwables.assertHasCauseReference(INFO, throwable, cause);
    // THEN
    // no exception thrown
  }

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: cause = {0} / expected = {1}")
  @MethodSource("failingData")
  void should_fail_if_cause_is_not_same_as_expected(final Throwable cause,
                                                    final Throwable expected,
                                                    String testDescription) {
    // GIVEN
    final Throwable throwable = withCause(cause);
    // WHEN
    expectAssertionError(() -> throwables.assertHasCauseReference(INFO, throwable, expected));
    // THEN
    verify(failures).failure(INFO, shouldHaveCauseReference(cause, expected));
  }

  private static Stream<Arguments> failingData() {
    return Stream.of(Arguments.of(null, new Throwable(), "no actual cause"),
                     Arguments.of(new Throwable(), new Throwable(), "same type different instance"),
                     Arguments.of(new Throwable(), null, "expecting null cause"),
                     Arguments.of(new IllegalArgumentException(), new IndexOutOfBoundsException(), "different types"));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    final Throwable throwable = null;
    final Throwable cause = new Throwable();
    // WHEN
    AssertionError actual = expectAssertionError(() -> throwables.assertHasCauseReference(INFO, throwable, cause));
    // THEN
    assertThat(actual).hasMessage(actualIsNull());
  }

  private static Throwable withCause(Throwable cause) {
    return new Throwable("bang!", cause);
  }
}
