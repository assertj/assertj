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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal.doubles;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.internal.ErrorMessages.offsetIsNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.DoublesBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Doubles_assertIsCloseTo_Test extends DoublesBaseTest {

  private static final Double ZERO = 0d;
  private static final Double ONE = 1d;
  private static final Double TWO = 2d;
  private static final Double TEN = 10d;

  // success

  @ParameterizedTest
  @CsvSource({
      "1.0, 1.0, 0.0",
      "1.0, 2.0, 1.0",
      "1.0, 0.0, 1.0",
      "1.0, 1.0, 10",
      "0.375, 0.125, 0.25",
      "1.0, 1.1, 0.1",
      "NaN, NaN, 1.0" })
  void should_pass_if_difference_is_less_than_or_equal_to_given_precision(double actual, double expected, double precision) {
    doubles.assertIsCloseTo(INFO, actual, expected, within(precision));
    doubles.assertIsCloseTo(INFO, expected, actual, within(precision));
  }

  @ParameterizedTest
  @CsvSource({
      "1.0, 2.0, 10",
      "0.375, 0.125, 0.2500001",
      "1.1, 1.0, 0.1000001",
      "NaN, NaN, 1.0" })
  void should_pass_if_difference_is_less_than_given_offset(Double actual, Double expected, Double precision) {
    doubles.assertIsCloseTo(INFO, actual, expected, byLessThan(precision));
  }

  @Test
  void should_pass_if_actual_and_expected_are_POSITIVE_INFINITY() {
    doubles.assertIsCloseTo(INFO, POSITIVE_INFINITY, POSITIVE_INFINITY, within(ONE));
    doubles.assertIsCloseTo(INFO, POSITIVE_INFINITY, POSITIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  void should_pass_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    doubles.assertIsCloseTo(INFO, NEGATIVE_INFINITY, NEGATIVE_INFINITY, within(ONE));
    doubles.assertIsCloseTo(INFO, NEGATIVE_INFINITY, NEGATIVE_INFINITY, byLessThan(ONE));
  }

  // error or failure

  @ParameterizedTest
  @CsvSource({
      "1.0, 1.1, 0.1",
      "1.0, 2.0, 1.0",
      "0.375, 0.125, 0.25" })
  void should_fail_if_difference_is_greater_than_or_equal_to_given_precision(Double expected, Double actual, Double precision) {
    // WHEN
    expectAssertionError(() -> doubles.assertIsCloseTo(INFO, actual, expected, byLessThan(precision)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(actual, expected, byLessThan(precision), absDiff(expected, actual)));
  }

  @ParameterizedTest
  @CsvSource({
      "1.0, 1.1, 0.0999999",
      "0.375, 0.125, 0.2499999" })
  void should_fail_if_difference_is_greater_than_given_precision(Double expected, Double actual, Double precision) {
    // WHEN
    expectAssertionError(() -> doubles.assertIsCloseTo(INFO, actual, expected, within(precision)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(actual, expected, within(precision), absDiff(expected, actual)));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Double actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> doubles.assertIsCloseTo(INFO, actual, ONE, within(ONE)));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> doubles.assertIsCloseTo(INFO, 6.0, null, offset(1.0)))
                                    .withMessage("The given number should not be null");
  }

  @Test
  void should_throw_error_if_offset_is_null() {
    assertThatNullPointerException().isThrownBy(() -> doubles.assertIsCloseTo(INFO, ONE, ZERO, null))
                                    .withMessage("The given offset should not be null");
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    // WHEN
    expectAssertionError(() -> doubles.assertIsCloseTo(INFO, ONE, TEN, within(ONE)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(ONE, TEN, within(ONE), TEN - ONE));
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value_with_a_strict_offset() {
    // WHEN
    expectAssertionError(() -> doubles.assertIsCloseTo(INFO, ONE, TEN, byLessThan(ONE)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(ONE, TEN, byLessThan(ONE), TEN - ONE));
  }

  @Test
  void should_fail_if_difference_is_equal_to_the_given_strict_offset() {
    // WHEN
    expectAssertionError(() -> doubles.assertIsCloseTo(INFO, TWO, ONE, byLessThan(ONE)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(TWO, ONE, byLessThan(ONE), TWO - ONE));
  }

  @Test
  void should_fail_if_actual_is_NaN_and_expected_is_not() {
    // WHEN
    expectAssertionError(() -> doubles.assertIsCloseTo(INFO, NaN, ONE, within(ONE)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(NaN, ONE, within(ONE), absDiff(NaN, ONE)));
  }

  @Test
  void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    // WHEN
    expectAssertionError(() -> doubles.assertIsCloseTo(INFO, POSITIVE_INFINITY, ONE, within(ONE)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(POSITIVE_INFINITY, ONE, within(ONE), absDiff(POSITIVE_INFINITY, ONE)));
  }

  @Test
  void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    // WHEN
    expectAssertionError(() -> doubles.assertIsCloseTo(INFO, NEGATIVE_INFINITY, ONE, within(ONE)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(NEGATIVE_INFINITY, ONE, within(ONE), absDiff(NEGATIVE_INFINITY, ONE)));
  }

  @Test
  void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    // WHEN
    expectAssertionError(() -> doubles.assertIsCloseTo(INFO, POSITIVE_INFINITY, NEGATIVE_INFINITY, within(ONE)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(POSITIVE_INFINITY, NEGATIVE_INFINITY, within(ONE),
                                                 absDiff(POSITIVE_INFINITY, NEGATIVE_INFINITY)));
  }

  @Test
  void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    // WHEN
    expectAssertionError(() -> doubles.assertIsCloseTo(INFO, NEGATIVE_INFINITY, POSITIVE_INFINITY, within(ONE)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(NEGATIVE_INFINITY, POSITIVE_INFINITY, within(ONE),
                                                 absDiff(NEGATIVE_INFINITY, POSITIVE_INFINITY)));
  }

  // with comparison strategy

  @Test
  void should_pass_if_difference_is_less_than_given_offset_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(INFO, ONE, ONE, within(ONE));
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(INFO, ONE, TWO, within(TEN));
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(INFO, ONE, TWO, byLessThan(TEN));
  }

  @Test
  void should_pass_if_difference_is_equal_to_given_offset_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(INFO, ONE, ONE, within(ZERO));
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(INFO, ONE, ZERO, within(ONE));
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(INFO, ONE, TWO, within(ONE));
  }

  @Test
  void should_throw_error_if_offset_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    ThrowingCallable code = () -> doublesWithAbsValueComparisonStrategy.assertIsCloseTo(INFO, 8d, 8d, null);
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(code)
                                    .withMessage(offsetIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    // WHEN
    expectAssertionError(() -> doublesWithAbsValueComparisonStrategy.assertIsCloseTo(INFO, 6d, 8d, offset(1d)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(6d, 8d, offset(1d), 2d));
  }

  @Test
  void should_fail_if_actual_is_not_strictly_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    // WHEN
    expectAssertionError(() -> doublesWithAbsValueComparisonStrategy.assertIsCloseTo(INFO, 6d, 8d, byLessThan(1d)));
    // THEN
    verify(failures).failure(INFO, shouldBeEqual(6d, 8d, byLessThan(1d), 2d));
  }

  @Test
  void should_throw_error_if_expected_value_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    ThrowingCallable code = () -> doublesWithAbsValueComparisonStrategy.assertIsCloseTo(INFO, 6d, null, offset(1d));
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(code)
                                    .withMessage("The given number should not be null");
  }

}
