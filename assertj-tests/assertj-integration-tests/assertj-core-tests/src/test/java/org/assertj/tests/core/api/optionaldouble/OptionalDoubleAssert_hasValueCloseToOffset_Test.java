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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.optionaldouble;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.OptionalDoubleShouldHaveValueCloseToOffset.shouldHaveValueCloseToOffset;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.OptionalDouble;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OptionalDoubleAssert_hasValueCloseToOffset_Test {

  @SuppressWarnings("OptionalAssignedToNull")
  @Test
  void should_fail_when_OptionalDouble_is_null() {
    // GIVEN
    OptionalDouble nullActual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(nullActual).hasValueCloseTo(10.0, within(2.0)));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_OptionalDouble_is_empty() {
    // GIVEN
    double expectedValue = 10.0;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(OptionalDouble.empty()).hasValueCloseTo(10.0, within(2.0)));
    // THEN
    then(error).hasMessage(shouldHaveValueCloseToOffset(expectedValue).create());
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    // GIVEN
    double expectedValue = 10.0;
    Offset<Double> offset = within(1.0);
    OptionalDouble actual = OptionalDouble.of(1.0);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasValueCloseTo(expectedValue, offset));
    // THEN
    double difference = abs(expectedValue - actual.getAsDouble());
    then(error).hasMessage(shouldHaveValueCloseToOffset(actual, expectedValue, offset, difference).create());
  }

  @Test
  void should_fail_if_offset_is_null() {
    Offset<Double> offset = null;
    assertThatNullPointerException().isThrownBy(() -> assertThat(OptionalDouble.of(10.0)).hasValueCloseTo(10.0, offset));
  }

  @ParameterizedTest
  @CsvSource({
      "10.0, 10.0, 2.0",
      "-10.0, -10.0, 2.0",
      "1.0, 1.0, 1.0",
      "-1.0, -1.0, 1.0",
      "1.0, 2.0, 5.0",
      "-1.0, -1.0, 2.0",
      // exact diff
      "1.0, 1.0, 0.0",
      "1.0, 0.0, 1.0",
      "-1.0, 0.0, 1.0",
      "1.0, 2.0, 1.0",
  })
  void should_pass_if_OptionalDouble_has_expected_value_with_less_than_given_offset(double optionalValue, double expected,
                                                                                    double offset) {
    assertThat(OptionalDouble.of(optionalValue)).hasValueCloseTo(expected, within(offset));
  }
}
