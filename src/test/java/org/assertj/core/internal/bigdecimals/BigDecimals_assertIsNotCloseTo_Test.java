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
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for <code>{@link org.assertj.core.internal.BigDecimals#assertIsNotCloseTo(AssertionInfo, BigDecimal, BigDecimal, org.assertj.core.data.Offset)}</code>.
 *
 * @author Chris Arnott
 */
class BigDecimals_assertIsNotCloseTo_Test extends BigDecimalsBaseTest {

  private static final BigDecimal FIVE = new BigDecimal("5");

  @Test
  void should_pass_if_difference_is_greater_than_offset() {
    numbers.assertIsNotCloseTo(someInfo(), TEN, ONE, byLessThan(ONE));
    numbers.assertIsNotCloseTo(someInfo(), TEN, ONE, within(ONE));
    numbers.assertIsNotCloseTo(someInfo(), TEN, ONE, offset(ONE));
  }

  @ParameterizedTest
  @CsvSource({
      "1.0, 0.0, 1.0",
      "-1.0, 0.0, 1.0",
      "-1.0, 1.0, 2.0",
      "0.0, 0.000000000000000000000001, 0.000000000000000000000001"
  })
  void should_pass_if_difference_is_equal_to_strict_offset(BigDecimal actual, BigDecimal expected,
                                                           BigDecimal offsetValue) {
    numbers.assertIsNotCloseTo(someInfo(), actual, expected, byLessThan(offsetValue));
  }

  @Test
  void should_fail_if_difference_is_less_than_given_offset() {
    BigDecimal fiveDotOne = new BigDecimal("5.1");
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, fiveDotOne, FIVE,
                                                                                                    within(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(fiveDotOne, FIVE, within(ONE), fiveDotOne.subtract(FIVE)));
  }

  @Test
  void should_fail_if_difference_is_less_than_given_strict_offset() {
    BigDecimal fiveDotOne = new BigDecimal("5.1");
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, fiveDotOne, FIVE,
                                                                                                    byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(fiveDotOne, FIVE, byLessThan(ONE), fiveDotOne.subtract(FIVE)));
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
  void should_fail_if_difference_is_equal_to_given_offset(BigDecimal actual, BigDecimal expected,
                                                          BigDecimal offsetValue) {
    AssertionInfo info = someInfo();
    Offset<BigDecimal> offset = within(offsetValue);

    Throwable error = catchThrowable(() -> numbers.assertIsNotCloseTo(info, actual, expected, offset));

    assertThat(error).isInstanceOf(AssertionError.class);
    BigDecimal diff = actual.subtract(expected).abs();
    verify(failures).failure(info, shouldNotBeEqual(actual, expected, offset, diff));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertIsNotCloseTo(someInfo(), null, ONE,
                                                                                                byLessThan(ONE)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> numbers.assertIsNotCloseTo(someInfo(), ONE, null, byLessThan(ONE)));
  }

  @Test
  void should_fail_if_offset_is_null() {
    assertThatNullPointerException().isThrownBy(() -> numbers.assertIsNotCloseTo(someInfo(), ONE, ZERO, null));
  }

  // with comparison strategy

  @Test
  void should_pass_if_difference_is_greater_than_offset_whatever_custom_comparison_strategy_is() {
    numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(someInfo(), TEN, ONE, byLessThan(ONE));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(someInfo(),
                                                                                                                              null,
                                                                                                                              ONE,
                                                                                                                              byLessThan(ONE)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_big_decimals_are_equal_whatever_custom_comparison_strategy_is() {
    BigDecimal fiveDotZero = new BigDecimal("5.0");
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, fiveDotZero, FIVE,
                                                                                                    byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(fiveDotZero, FIVE, byLessThan(ONE), fiveDotZero.subtract(FIVE)));
  }

  @ParameterizedTest
  @CsvSource({
      "1.0, 0.0, 1.0",
      "-1.0, 0.0, 1.0",
      "-1.0, 1.0, 2.0",
      "0.0, 0.000000000000000000000001, 0.000000000000000000000001"
  })
  void should_pass_if_difference_is_equal_to_strict_offset_whatever_custom_comparison_strategy_is(BigDecimal actual,
                                                                                                  BigDecimal expected,
                                                                                                  BigDecimal offsetValue) {
    numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(someInfo(), actual, expected, byLessThan(offsetValue));
  }

}
