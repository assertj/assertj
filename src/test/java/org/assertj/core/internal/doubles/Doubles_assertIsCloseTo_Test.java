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

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.internal.ErrorMessages.offsetIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DoublesBaseTest;
import org.junit.jupiter.api.Test;

class Doubles_assertIsCloseTo_Test extends DoublesBaseTest {

  private static final Double ZERO = 0d;
  private static final Double ONE = 1d;
  private static final Double TWO = 2d;
  private static final Double TEN = 10d;

  // success

  @Test
  void should_pass_if_difference_is_less_than_given_offset() {
    doubles.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    doubles.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
    doubles.assertIsCloseTo(someInfo(), ONE, TWO, byLessThan(TEN));
  }

  @Test
  void should_pass_if_difference_is_equal_to_given_offset() {
    doubles.assertIsCloseTo(someInfo(), ONE, ONE, within(ZERO));
    doubles.assertIsCloseTo(someInfo(), ONE, ZERO, within(ONE));
    doubles.assertIsCloseTo(someInfo(), ONE, TWO, within(ONE));
  }

  @Test
  void should_pass_if_actual_and_expected_are_POSITIVE_INFINITY() {
    doubles.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, POSITIVE_INFINITY, within(ONE));
    doubles.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, POSITIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  void should_pass_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    doubles.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, NEGATIVE_INFINITY, within(ONE));
    doubles.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, NEGATIVE_INFINITY, byLessThan(ONE));
  }

  @Test
  void should_pass_if_actual_and_expected_are_NaN() {
    doubles.assertIsCloseTo(someInfo(), NaN, NaN, within(ONE));
    doubles.assertIsCloseTo(someInfo(), NaN, NaN, byLessThan(ONE));
  }

  // error or failure

  @Test
  void should_throw_error_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doubles.assertIsCloseTo(someInfo(), null, ONE, within(ONE)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> doubles.assertIsCloseTo(someInfo(), 6d, null, offset(1d)))
                                    .withMessage("The given number should not be null");
  }

  @Test
  void should_throw_error_if_offset_is_null() {
    assertThatNullPointerException().isThrownBy(() -> doubles.assertIsCloseTo(someInfo(), ONE, ZERO, null));
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> doubles.assertIsCloseTo(info, ONE, TEN, within(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(ONE, TEN, within(ONE), TEN - ONE));
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value_with_a_strict_offset() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> doubles.assertIsCloseTo(info, ONE, TEN, byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(ONE, TEN, byLessThan(ONE), TEN - ONE));
  }

  @Test
  void should_fail_if_difference_is_equal_to_the_given_strict_offset() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> doubles.assertIsCloseTo(info, TWO, ONE, byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(TWO, ONE, byLessThan(ONE), TWO - ONE));
  }

  @Test
  void should_fail_if_actual_is_NaN_and_expected_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doubles.assertIsCloseTo(someInfo(), NaN, ONE, within(ONE)));
  }

  @Test
  void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doubles.assertIsCloseTo(someInfo(), POSITIVE_INFINITY, ONE,
                                                                                             within(ONE)));
  }

  @Test
  void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doubles.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY, ONE,
                                                                                             within(ONE)));
  }

  @Test
  void should_fail_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doubles.assertIsCloseTo(someInfo(), POSITIVE_INFINITY,
                                                                                             NEGATIVE_INFINITY, within(ONE)));
  }

  @Test
  void should_fail_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doubles.assertIsCloseTo(someInfo(), NEGATIVE_INFINITY,
                                                                                             POSITIVE_INFINITY, within(ONE)));
  }

  // with comparison strategy

  @Test
  void should_pass_if_difference_is_less_than_given_offset_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, byLessThan(TEN));
  }

  @Test
  void should_pass_if_difference_is_equal_to_given_offset_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ONE, within(ZERO));
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ZERO, within(ONE));
    doublesWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, within(ONE));
  }

  @Test
  void should_throw_error_if_offset_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> doublesWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(),
                                                                                                            new Double(8d),
                                                                                                            new Double(8d),
                                                                                                            null))
                                    .withMessage(offsetIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> doublesWithAbsValueComparisonStrategy.assertIsCloseTo(info, new Double(6d),
                                                                                                 new Double(8d), offset(1d)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(6d, 8d, offset(1d), 2d));
  }

  @Test
  void should_fail_if_actual_is_not_strictly_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> doublesWithAbsValueComparisonStrategy.assertIsCloseTo(info, new Double(6d),
                                                                                                 new Double(8d), byLessThan(1d)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(6d, 8d, byLessThan(1d), 2d));
  }

  @Test
  void should_throw_error_if_expected_value_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> doublesWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(),
                                                                                                            6d, null,
                                                                                                            offset(1d)))
                                    .withMessage("The given number should not be null");
  }

}
