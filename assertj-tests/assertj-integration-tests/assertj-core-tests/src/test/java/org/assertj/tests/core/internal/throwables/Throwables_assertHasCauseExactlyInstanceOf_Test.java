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
package org.assertj.tests.core.internal.throwables;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveCauseExactlyInstance.shouldHaveCauseExactlyInstance;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Throwables;
import org.junit.jupiter.api.Test;

/**
 * @author Jean-Christophe Gay
 */
class Throwables_assertHasCauseExactlyInstanceOf_Test {

  private static final AssertionInfo INFO = someInfo();
  private final Throwables throwables = Throwables.instance();
  private Throwable actual = new NullPointerException("Throwable message");

  private final Throwable throwableWithCause = new Throwable(new IllegalArgumentException());

  @Test
  void should_pass_if_cause_is_exactly_instance_of_expected_type() {
    throwables.assertHasCauseExactlyInstanceOf(INFO, throwableWithCause, IllegalArgumentException.class);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var error = expectAssertionError(() -> throwables.assertHasCauseExactlyInstanceOf(INFO, actual,
                                                                                      IllegalArgumentException.class));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_NullPointerException_if_given_type_is_null() {
    // GIVEN
    Class<? extends Throwable> type = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> throwables.assertHasCauseExactlyInstanceOf(INFO, throwableWithCause, type));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class)
                   .hasMessage("The given type should not be null");
  }

  @Test
  void should_fail_if_actual_has_no_cause() {
    // GIVEN
    Class<NullPointerException> expectedCauseType = NullPointerException.class;
    // WHEN
    var error = expectAssertionError(() -> throwables.assertHasCauseExactlyInstanceOf(INFO, actual, expectedCauseType));
    // THEN
    then(error).hasMessage(shouldHaveCauseExactlyInstance(actual, expectedCauseType).create());
  }

  @Test
  void should_fail_if_cause_is_not_instance_of_expected_type() {
    // GIVEN
    Class<NullPointerException> expectedCauseType = NullPointerException.class;
    // WHEN
    var error = expectAssertionError(() -> throwables.assertHasCauseExactlyInstanceOf(INFO, throwableWithCause,
                                                                                      expectedCauseType));
    // THEN
    then(error).hasMessage(shouldHaveCauseExactlyInstance(throwableWithCause, expectedCauseType).create());
  }

  @Test
  void should_fail_if_cause_is_not_exactly_instance_of_expected_type() {
    // GIVEN
    Class<RuntimeException> expectedCauseType = RuntimeException.class;
    // WHEN
    var error = expectAssertionError(() -> throwables.assertHasCauseExactlyInstanceOf(INFO, throwableWithCause,
                                                                                      expectedCauseType));
    // THEN
    then(error).hasMessage(shouldHaveCauseExactlyInstance(throwableWithCause, expectedCauseType).create());
  }
}
