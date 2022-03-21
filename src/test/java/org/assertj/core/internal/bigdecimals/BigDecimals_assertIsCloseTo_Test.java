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
package org.assertj.core.internal.bigdecimals;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
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

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for <code>{@link org.assertj.core.internal.BigDecimals#assertIsCloseTo(org.assertj.core.api.AssertionInfo, java.math.BigDecimal, java.math.BigDecimal, org.assertj.core.data.Offset)}</code>.
 *
 * @author Joel Costigliola
 */
class BigDecimals_assertIsCloseTo_Test extends BigDecimalsBaseTest {

  private static final BigDecimal NINE = new BigDecimal(9.0);
  private static final BigDecimal TWO = new BigDecimal(2);

  @Test
  void should_pass_if_difference_is_less_than_given_offset() {
    numbers.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    numbers.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
    numbers.assertIsCloseTo(someInfo(), ONE, TWO, byLessThan(TEN));
  }

  @ParameterizedTest
  @CsvSource({
      "1.0, 1.0, 0.0",
      "1.0, 0.0, 1.0",
      "-1.0, 0.0, 1.0",
      "-1.0, -1.0, 0.0",
      "-1.0, 1.0, 2.0",
      "0.0, 0.000000000000000000000001, 0.000000000000000000000001",
      "-0.000000000000000000000001, -0.000000000000000000000001, 0.0"
  })
  void should_pass_if_big_decimals_difference_is_equal_to_given_offset(BigDecimal actual, BigDecimal expected,
                                                                       BigDecimal offset) {
    numbers.assertIsCloseTo(someInfo(), actual, expected, offset(offset));
  }

  // error or failure

  @Test
  void should_throw_error_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertIsCloseTo(someInfo(), null, ONE, within(ONE)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> numbers.assertIsCloseTo(someInfo(),
                                                                              new BigDecimal(6.0),
                                                                              null, offset(ONE)))
                                    .withMessage("The given number should not be null");
  }

  @Test
  void should_throw_error_if_offset_is_null() {
    assertThatNullPointerException().isThrownBy(() -> numbers.assertIsCloseTo(someInfo(), ONE, ZERO, null));
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbers.assertIsCloseTo(info, ONE, TEN, within(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(ONE, TEN, within(ONE), NINE));
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value_with_a_strict_offset() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbers.assertIsCloseTo(info, ONE, TEN, byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(ONE, TEN, byLessThan(ONE), NINE));
  }

  @Test
  void should_fail_if_difference_is_equal_to_the_given_strict_offset() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbers.assertIsCloseTo(info, TWO, ONE, byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(TWO, ONE, byLessThan(ONE), ONE));
  }

  // with comparison strategy

  @Test
  void should_pass_if_difference_is_less_than_given_offset_whatever_custom_comparison_strategy_is() {
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, byLessThan(TEN));
  }

  @Test
  void should_pass_if_difference_is_equal_to_given_offset_whatever_custom_comparison_strategy_is() {
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ONE, within(ZERO));
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ZERO, within(ONE));
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, within(ONE));
  }

  @Test
  void should_throw_error_if_offset_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(),
                                                                                                            ONE,
                                                                                                            TWO,
                                                                                                            null))
                                    .withMessage(offsetIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithAbsValueComparisonStrategy.assertIsCloseTo(info, ONE, TEN, offset(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(ONE, TEN, offset(ONE), NINE));
  }

  @Test
  void should_fail_if_actual_is_not_strictly_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithAbsValueComparisonStrategy.assertIsCloseTo(info, ONE, TEN,
                                                                                                 byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(ONE, TEN, byLessThan(ONE), NINE));
  }

  @Test
  void should_throw_error_if_expected_value_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(),
                                                                                                            TWO,
                                                                                                            null,
                                                                                                            offset(ONE)))
                                    .withMessage("The given number should not be null");
  }

}
