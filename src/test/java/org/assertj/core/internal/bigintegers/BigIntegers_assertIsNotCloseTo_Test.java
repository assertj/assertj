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
package org.assertj.core.internal.bigintegers;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;
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

import java.math.BigInteger;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.internal.BigIntegersBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for <code>{@link org.assertj.core.internal.BigIntegers#assertIsNotCloseTo(AssertionInfo, BigInteger, BigInteger, Offset)}</code>.
 */
class BigIntegers_assertIsNotCloseTo_Test extends BigIntegersBaseTest {

  private static final BigInteger FIVE = new BigInteger("5");

  @Test
  void should_pass_if_difference_is_greater_than_offset() {
    numbers.assertIsNotCloseTo(someInfo(), TEN, ONE, within(ONE));
    numbers.assertIsNotCloseTo(someInfo(), TEN, ONE, offset(ONE));
    numbers.assertIsNotCloseTo(someInfo(), TEN, ONE, byLessThan(ONE));
  }

  @ParameterizedTest
  @CsvSource({
      "1, 0, 1",
      "-1, 0, 1",
      "1, -1, 2",
      "-1, 1, 2"
  })
  void should_pass_if_difference_is_equal_to_strict_offset(BigInteger actual, BigInteger expected,
                                                           BigInteger value) {
    numbers.assertIsNotCloseTo(someInfo(), actual, expected, byLessThan(value));
  }

  @Test
  void should_fail_if_difference_is_less_than_given_offset() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, ONE, FIVE,
                                                                                                    within(TEN)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(ONE, FIVE, within(TEN), FIVE.subtract(ONE)));
  }

  @Test
  void should_fail_if_difference_is_less_than_given_strict_offset() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, ONE, FIVE,
                                                                                                    byLessThan(TEN)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(ONE, FIVE, byLessThan(TEN), FIVE.subtract(ONE)));
  }

  @ParameterizedTest
  @CsvSource({
      "1, 1, 0",
      "1, 0, 1",
      "-1, 0, 1",
      "-1, -1, 0",
      "-1, 1, 2",
      "0, 1, 1",
      "-1, -1, 0"
  })
  void should_fail_if_difference_is_equal_to_given_offset(BigInteger actual, BigInteger expected,
                                                          BigInteger offset) {
    AssertionInfo info = someInfo();
    Offset<BigInteger> bigDecimalOffset = within(offset);

    Throwable error = catchThrowable(() -> numbers.assertIsNotCloseTo(info, actual, expected, bigDecimalOffset));

    assertThat(error).isInstanceOf(AssertionError.class);
    BigInteger diff = actual.subtract(expected).abs();
    verify(failures).failure(info, shouldNotBeEqual(actual, expected, bigDecimalOffset, diff));
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
  void should_pass_if_big_integers_are_not_close_whatever_custom_comparison_strategy_is() {
    numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(someInfo(), TEN, ONE, byLessThan(ONE));
  }

  @Test
  void should_fail_if_difference_is_less_than_given_offset_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, FIVE, FIVE,
                                                                                                    byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(FIVE, FIVE, byLessThan(ONE), FIVE.subtract(FIVE)));
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
  void should_fail_if_big_integers_are_equal_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, FIVE, FIVE,
                                                                                                    byLessThan(ONE)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(FIVE, FIVE, byLessThan(ONE), FIVE.subtract(FIVE)));
  }
}
