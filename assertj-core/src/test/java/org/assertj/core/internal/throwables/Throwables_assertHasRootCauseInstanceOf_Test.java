/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.throwables;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveRootCauseInstance.shouldHaveRootCauseInstance;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * {@link org.assertj.core.internal.Throwables#assertHasRootCauseInstanceOf(org.assertj.core.api.AssertionInfo, Throwable, Class)}
 * .
 * 
 * @author Jean-Christophe Gay
 */
class Throwables_assertHasRootCauseInstanceOf_Test extends ThrowablesBaseTest {

  private static final Throwable throwableWithCause = new Throwable(new Exception(new IllegalArgumentException()));

  @Test
  void should_pass_if_root_cause_is_exactly_instance_of_expected_type() {
    throwables.assertHasRootCauseInstanceOf(INFO, throwableWithCause, IllegalArgumentException.class);
  }

  @Test
  void should_pass_if_root_cause_is_instance_of_expected_type() {
    throwables.assertHasRootCauseInstanceOf(INFO, throwableWithCause, RuntimeException.class);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> throwables.assertHasRootCauseInstanceOf(INFO, actual, IOException.class));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_NullPointerException_if_given_type_is_null() {
    assertThatNullPointerException().isThrownBy(() -> throwables.assertHasRootCauseInstanceOf(INFO, throwableWithCause, null))
                                    .withMessage("The given type should not be null");
  }

  @Test
  void should_fail_if_actual_has_no_cause() {
    // GIVEN
    Class<NullPointerException> expectedCauseType = NullPointerException.class;
    // WHEN
    expectAssertionError(() -> throwables.assertHasRootCauseInstanceOf(INFO, actual, expectedCauseType));
    // THEN
    verify(failures).failure(INFO, shouldHaveRootCauseInstance(actual, expectedCauseType));
  }

  @Test
  void should_fail_if_root_cause_is_not_instance_of_expected_type() {
    // GIVEN
    Class<NullPointerException> expectedCauseType = NullPointerException.class;
    // WHEN
    expectAssertionError(() -> throwables.assertHasRootCauseInstanceOf(INFO, throwableWithCause, expectedCauseType));
    // THEN
    verify(failures).failure(INFO, shouldHaveRootCauseInstance(throwableWithCause, expectedCauseType));
  }
}
