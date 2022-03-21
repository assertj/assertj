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
package org.assertj.core.internal.doubles;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeInfinite.shouldNotBeInfinite;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.DoublesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Doubles assertIsNotInfinite")
class Doubles_assertIsNotInfinite_Test extends DoublesBaseTest {

  @ParameterizedTest
  @ValueSource(doubles = {
      Double.MAX_VALUE,
      Double.MIN_NORMAL,
      Double.MIN_VALUE,
      Double.NaN,
      0.0,
      1.0,
      -1.0,
  })
  void should_succeed_when_actual_is_not_infinite(double actual) {
    // WHEN/THEN
    doubles.assertIsNotInfinite(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(doubles = {
      Double.POSITIVE_INFINITY,
      Double.NEGATIVE_INFINITY
  })
  void should_fail_when_actual_is_infinite(double actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> doubles.assertIsNotInfinite(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldNotBeInfinite(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Double actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> doubles.assertIsNotInfinite(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
