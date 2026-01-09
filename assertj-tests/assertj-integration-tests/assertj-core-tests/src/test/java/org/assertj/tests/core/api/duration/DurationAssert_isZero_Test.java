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
package org.assertj.tests.core.api.duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Filip Hrisafov
 */
class DurationAssert_isZero_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Duration actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isZero());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "PT1S", "PT-1S" })
  void should_fail_if_actual_is_not_zero(Duration actual) {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isZero());
    // THEN
    then(assertionError).hasMessage("%nexpected: %s%n but was: %s",
                                    STANDARD_REPRESENTATION.toStringOf(Duration.ZERO),
                                    STANDARD_REPRESENTATION.toStringOf(actual));
  }

  @Test
  void should_pass_if_actual_is_zero() {
    // GIVEN
    Duration actual = Duration.ZERO;
    // WHEN/THEN
    assertThat(actual).isZero();
  }

}
