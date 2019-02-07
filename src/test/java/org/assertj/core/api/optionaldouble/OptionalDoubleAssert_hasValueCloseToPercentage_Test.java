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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.optionaldouble;

import static java.lang.Double.*;
import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.error.OptionalDoubleShouldHaveValueCloseToPercentage.shouldHaveValueCloseToPercentage;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.OptionalDouble;

import org.assertj.core.api.BaseTest;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class OptionalDoubleAssert_hasValueCloseToPercentage_Test extends BaseTest {

  @Test
  public void should_fail_when_actual_is_null() {
    OptionalDouble actual = null;
    double expectedValue = 10;
    Percentage percentage = withinPercentage(5);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, percentage))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_when_actual_is_empty() {
    OptionalDouble actual = OptionalDouble.empty();
    double expectedValue = 10;
    Percentage percentage = withinPercentage(5);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, percentage))
                                                   .withMessage(shouldHaveValueCloseToPercentage(expectedValue).create());
  }

  @Test
  public void should_fail_when_expected_is_null() {
    OptionalDouble actual = OptionalDouble.of(5);
    Double expectedValue = null;
    Percentage percentage = withinPercentage(5);

    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, percentage));
  }

  @Test
  public void should_fail_when_percentage_is_null() {
    OptionalDouble actual = OptionalDouble.of(5);
    double expectedValue = 5;
    Percentage percentage = null;

    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, percentage));
  }

  @Test
  public void should_fail_when_percentage_is_negative() {
    OptionalDouble actual = OptionalDouble.of(5);
    double expectedValue = 5;

    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, withPercentage(-5)));
  }

  @ParameterizedTest
  @CsvSource({
    "1, 1, 1",
    "1, 2, 100",
    "-1, -1, 1",
    "-1, -2, 100",
    "-1, 1, 200"
  })
  public void should_pass_when_difference_is_less_than_given_percentage(Double value, Double other, Double percentage) {
    assertThat(OptionalDouble.of(value)).hasValueCloseTo(other, withinPercentage(percentage));
  }

  @ParameterizedTest
  @CsvSource({
    "1, 1, 0",
    "2, 1, 100",
    "1, 2, 50",
    "-1, -1, 0",
    "-2, -1, 100",
    "-1, -2, 50"
  })
  public void should_pass_when_difference_is_equal_to_given_percentage(Double value, Double other, Double percentage) {
    assertThat(OptionalDouble.of(value)).hasValueCloseTo(other, withinPercentage(percentage));
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    OptionalDouble actual = OptionalDouble.of(1);
    double expectedValue = 10;
    Percentage percentage = withPercentage(10);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, percentage))
                                                   .withMessage(shouldHaveValueCloseToPercentage(actual, expectedValue, percentage, abs(expectedValue - actual.getAsDouble())).create());
  }

  @Test
  public void should_fail_if_actual_is_NaN_and_expected_is_not() {
    OptionalDouble actual = OptionalDouble.of(NaN);
    double expectedValue = 1;
    Percentage percentage = withPercentage(10);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, percentage))
                                                   .withMessage(shouldHaveValueCloseToPercentage(actual, expectedValue, percentage, abs(expectedValue - actual.getAsDouble())).create());
  }

  @Test
  public void should_pass_if_actual_and_expected_are_NaN() {
    assertThat(OptionalDouble.of(NaN)).hasValueCloseTo(NaN, withPercentage(10));
  }

  @Test
  public void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    OptionalDouble actual = OptionalDouble.of(POSITIVE_INFINITY);
    double expectedValue = 1;
    Percentage percentage = withPercentage(10);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, percentage))
                                                   .withMessage(shouldHaveValueCloseToPercentage(actual, expectedValue, percentage, abs(expectedValue - actual.getAsDouble())).create());
  }

  @Test
  public void should_pass_if_actual_and_expected_are_POSITIVE_INFINITY() {
    assertThat(OptionalDouble.of(POSITIVE_INFINITY)).hasValueCloseTo(POSITIVE_INFINITY, withPercentage(10));
  }

  @Test
  public void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    OptionalDouble actual = OptionalDouble.of(NEGATIVE_INFINITY);
    double expectedValue = 1;
    Percentage percentage = withPercentage(10);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, percentage))
                                                   .withMessage(shouldHaveValueCloseToPercentage(actual, expectedValue, percentage, abs(expectedValue - actual.getAsDouble())).create());
  }

  @Test
  public void should_pass_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    assertThat(OptionalDouble.of(NEGATIVE_INFINITY)).hasValueCloseTo(NEGATIVE_INFINITY, withPercentage(10));
  }

  @Test
  public void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    OptionalDouble actual = OptionalDouble.of(POSITIVE_INFINITY);
    double expectedValue = NEGATIVE_INFINITY;
    Percentage percentage = withPercentage(10);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, percentage))
                                                   .withMessage(shouldHaveValueCloseToPercentage(actual, expectedValue, percentage, abs(expectedValue - actual.getAsDouble())).create());
  }

  @Test
  public void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    OptionalDouble actual = OptionalDouble.of(NEGATIVE_INFINITY);
    double expectedValue = POSITIVE_INFINITY;
    Percentage percentage = withPercentage(10);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).hasValueCloseTo(expectedValue, percentage))
                                                   .withMessage(shouldHaveValueCloseToPercentage(actual, expectedValue, percentage, abs(expectedValue - actual.getAsDouble())).create());
  }
}
