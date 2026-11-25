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
import static org.assertj.core.error.ShouldHaveRootCauseInstance.shouldHaveRootCauseInstance;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/// @author Jean-Christophe Gay
class ThrowableAssert_hasRootCauseInstanceOf_Test {

  private static final Throwable THROWABLE_WITH_CAUSE = new Throwable(new Exception(new IllegalArgumentException()));

  @Test
  void should_pass_if_root_cause_is_exactly_instance_of_expected_type() {
    assertThat(THROWABLE_WITH_CAUSE).hasRootCauseInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_pass_if_root_cause_is_instance_of_expected_type() {
    assertThat(THROWABLE_WITH_CAUSE).hasRootCauseInstanceOf(RuntimeException.class);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasRootCauseInstanceOf(IOException.class));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_NullPointerException_if_given_type_is_null() {
    // GIVEN
    Class<? extends Throwable> type = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(THROWABLE_WITH_CAUSE).hasRootCauseInstanceOf(type));
    // THEN
    then(npe).hasMessage("The given type should not be null");
  }

  @Test
  void should_fail_if_actual_has_no_cause() {
    // GIVEN
    Throwable actual = new NullPointerException("Throwable message");
    Class<NullPointerException> expectedCauseType = NullPointerException.class;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasRootCauseInstanceOf(expectedCauseType));
    // THEN
    then(assertionError).hasMessage(shouldHaveRootCauseInstance(actual, expectedCauseType).create());
  }

  @Test
  void should_fail_if_root_cause_is_not_instance_of_expected_type() {
    // GIVEN
    Class<NullPointerException> expectedCauseType = NullPointerException.class;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(THROWABLE_WITH_CAUSE).hasRootCauseInstanceOf(expectedCauseType));
    // THEN
    then(assertionError).hasMessage(shouldHaveRootCauseInstance(THROWABLE_WITH_CAUSE, expectedCauseType).create());
  }

}
