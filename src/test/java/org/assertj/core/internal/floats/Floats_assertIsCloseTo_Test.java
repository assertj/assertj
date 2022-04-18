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
package org.assertj.core.internal.floats;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FloatsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static java.lang.Float.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.ComparatorFactory.asBigDecimal;
import static org.assertj.core.api.ComparatorFactory.isNanOrInfinity;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.internal.ErrorMessages.offsetIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

class Floats_assertIsCloseTo_Test extends FloatsBaseTest {

  private static final Float ZERO = 0f;
  private static final Float ONE = 1f;
  private static final Float TWO = 2f;
  private static final Float TEN = 10f;

  // success

  private float absDifference(Float f1, Float f2) {
    if (isNanOrInfinity(f1) || isNanOrInfinity(f2)) {
      return Math.abs(f1 - f2);
    }
    return asBigDecimal(f1).subtract(asBigDecimal(f2)).abs().floatValue();
  }

  @Test
  void should_pass_if_difference_is_less_than_given_offset() {
    floats.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    floats.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
    floats.assertIsCloseTo(someInfo(), ONE, TWO, byLessThan(TEN));
  }

  @ParameterizedTest
  @CsvSource({"0.375f, 0.125f, 0.25f", "1.1f, 1.0f, 0.1f"})
  void should_pass_if_difference_is_within_range(Float actual, Float expected, Float precision){
    floats.assertIsCloseTo(someInfo(), actual, expected, within(precision));
  }

  @ParameterizedTest
  @CsvSource({"0.375f, 0.125f, 0.2500001f", "1.1f, 1.0f, 0.1000001f"})
  void should_pass_if_difference_is_byLessThan_range(Float actual, Float expected, Float precision){
    floats.assertIsCloseTo(someInfo(), actual, expected, within(precision));
  }

  @Test
  void should_pass_if_difference_is_equal_to_given_offset() {
    floats.assertIsCloseTo(someInfo(), ONE, ONE, within(ZERO));
    floats.assertIsCloseTo(someInfo(), ONE, ZERO, within(ONE));
    floats.assertIsCloseTo(someInfo(), ONE, TWO, within(ONE));
  }

  @Test
  void should_pass_if_actual_and_expected_are_POSITIVE_INFINITY() {
    floats.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, POSITIVE_INFINITY, within(ONE));
    floats.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, POSITIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  void should_pass_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    floats.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, NEGATIVE_INFINITY, within(ONE));
    floats.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, NEGATIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  void should_pass_if_actual_and_expected_are_NaN() {
    floats.assertIsCloseTo(someInfo(), NaN, NaN, within(ONE));
    floats.assertIsCloseTo(someInfo(), NaN, NaN, byLessThan(ONE));
  }

  // error or failure

  @ParameterizedTest
  @CsvSource({"1.1f, 1.0f, 0.1f", "0.375f, 0.125f, 0.25f"})
  void should_fail_if_actual_is_bigger_than_expected_byLessThan_range(Float expected, Float actual, Float precision) {
    AssertionError assertionError = expectAssertionError(() -> floats.assertIsCloseTo(someInfo(), actual, expected,
      byLessThan(precision)));
    then(assertionError).hasMessage(shouldBeEqual(actual, expected, byLessThan(precision), absDifference(expected, actual)).create());
  }

  @ParameterizedTest
  @CsvSource({"1.1f, 1.0f, 0.0999999f", "0.375f, 0.125f, 0.2499999f"})
  void should_fail_if_actual_is_bigger_than_expected_within_range(Float expected, Float actual, Float precision) {
    AssertionError assertionError = expectAssertionError(() -> floats.assertIsCloseTo(someInfo(), actual, expected, within(precision)));
    then(assertionError).hasMessage(shouldBeEqual(actual, expected, within(precision), absDifference(expected, actual)).create());
  }


  @Test
  void should_throw_error_if_actual_is_null() {
    AssertionError assertionError = expectAssertionError(() -> floats.assertIsCloseTo(someInfo(), null, ONE, within(ONE)));
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> floats.assertIsCloseTo(someInfo(), 6f, null, offset(1f)))
      .withMessage("The given number should not be null");
  }

  @Test
  void should_throw_error_if_offset_is_null() {
    assertThatNullPointerException().isThrownBy(() -> floats.assertIsCloseTo(someInfo(), ONE, ZERO, null));
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> floats.assertIsCloseTo(info, ONE, TEN, within(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(ONE, TEN, within(ONE), TEN - ONE));
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value_with_a_strict_offset() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> floats.assertIsCloseTo(info, ONE, TEN, byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(ONE, TEN, byLessThan(ONE), TEN - ONE));
  }

  @Test
  void should_fail_if_difference_is_equal_to_the_given_strict_offset() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> floats.assertIsCloseTo(info, TWO, ONE, byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(TWO, ONE, byLessThan(ONE), TWO - ONE));
  }

  @Test
  void should_fail_if_actual_is_NaN_and_expected_is_not() {
    AssertionError assertionError = expectAssertionError(() -> floats.assertIsCloseTo(someInfo(), NaN, ONE, within(ONE)));
    then(assertionError).hasMessage(shouldBeEqual(NaN, ONE, within(ONE), NaN - ONE).create());
  }

  @Test
  void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    AssertionError assertionError = expectAssertionError(() -> floats.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, ONE, within(ONE)));
    then(assertionError).hasMessage(shouldBeEqual(POSITIVE_INFINITY, ONE, within(ONE), absDifference(POSITIVE_INFINITY, ONE)).create());
  }

  @Test
  void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    AssertionError assertionError = expectAssertionError(() -> floats.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, ONE, within(ONE)));
    then(assertionError).hasMessage(shouldBeEqual(NEGATIVE_INFINITY, ONE, within(ONE), absDifference(NEGATIVE_INFINITY, ONE)).create());
  }

  @Test
  void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    AssertionError assertionError = expectAssertionError(() -> floats.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, NEGATIVE_INFINITY, within(ONE)));
    then(assertionError).hasMessage(shouldBeEqual(POSITIVE_INFINITY, NEGATIVE_INFINITY, within(ONE), absDifference(POSITIVE_INFINITY, NEGATIVE_INFINITY)).create());
  }

  @Test
  void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    AssertionError assertionError = expectAssertionError(() -> floats.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, POSITIVE_INFINITY, within(ONE)));
    then(assertionError).hasMessage(shouldBeEqual(NEGATIVE_INFINITY, POSITIVE_INFINITY, within(ONE), absDifference(POSITIVE_INFINITY, NEGATIVE_INFINITY)).create());
  }

  // with comparison strategy

  @Test
  void should_pass_if_difference_is_less_than_given_offset_whatever_custom_comparison_strategy_is() {
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, byLessThan(TEN));
  }

  @Test
  void should_pass_if_difference_is_equal_to_given_offset_whatever_custom_comparison_strategy_is() {
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ONE, within(ZERO));
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ZERO, within(ONE));
    floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, within(ONE));
  }

  @Test
  void should_throw_error_if_offset_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(),
        8f,
        8f,
        null))
      .withMessage(offsetIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> floatsWithAbsValueComparisonStrategy.assertIsCloseTo(info, 6f, 8f, offset(1f)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(6f, 8f, offset(1f), 2f));
  }

  @Test
  void should_fail_if_actual_is_not_strictly_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> floatsWithAbsValueComparisonStrategy.assertIsCloseTo(info, 6f, 8f, byLessThan(1f)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(6f, 8f, byLessThan(1f), 2f));
  }

  @Test
  void should_throw_error_if_expected_value_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> floatsWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(),
        6f, null,
        offset(1f)))
      .withMessage("The given number should not be null");
  }

}
