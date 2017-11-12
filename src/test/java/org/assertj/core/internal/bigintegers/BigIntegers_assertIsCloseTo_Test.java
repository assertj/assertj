/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.bigintegers;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static java.math.BigInteger.ZERO;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.internal.ErrorMessages.offsetIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigIntegersBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

/**
 * Tests for <code>{@link org.assertj.core.internal.BigIntegers#assertIsCloseTo(AssertionInfo, BigInteger, BigInteger, org.assertj.core.data.Offset)}</code>.
 */
@RunWith(DataProviderRunner.class)
public class BigIntegers_assertIsCloseTo_Test extends BigIntegersBaseTest {

  private static final BigInteger TWO = new BigInteger("2");
  private static final BigInteger FIVE = new BigInteger("5");
  private static final BigInteger SIX = new BigInteger("6");
  private static final BigInteger NINE = new BigInteger("9");

  @Test
  public void should_pass_if_difference_is_less_than_given_offset() {
    numbers.assertIsCloseTo(someInfo(), FIVE, SIX, offset(TWO));
    numbers.assertIsCloseTo(someInfo(), FIVE, SIX, byLessThan(TWO));
    numbers.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    numbers.assertIsCloseTo(someInfo(), ONE, ONE, byLessThan(ONE));
    numbers.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
    numbers.assertIsCloseTo(someInfo(), ONE, TWO, byLessThan(TEN));
  }

  @Test
  @DataProvider({
      "1, 1, 0",
      "1, 0, 1",
      "-1, 0, 1",
      "-1, -1, 0",
      "-1, 1, 2",
      "0, 1, 1",
      "-1, -1, 0"
  })
  // @format:on
  public void should_pass_if_big_integers_difference_is_equal_to_given_offset(BigInteger actual, BigInteger expected,
                                                                              BigInteger offset) {
    numbers.assertIsCloseTo(someInfo(), actual, expected, offset(offset));
  }

  @Test
  @DataProvider({
      "1, 0, 1",
      "-1, 0, 1",
      "0, -1, 1",
      "-1, 1, 2",
      "0, 1, 1"
  })
  // @format:on
  public void should_fail_if_big_integers_difference_is_equal_to_given_strict_offset(BigInteger actual,
                                                                                     BigInteger expected,
                                                                                     BigInteger offset) {
    AssertionInfo info = someInfo();
    try {
      numbers.assertIsCloseTo(someInfo(), actual, expected, byLessThan(offset));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(actual, expected, byLessThan(offset), offset));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    numbers.assertIsCloseTo(someInfo(), null, ONE, offset(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if__expected_value_is_null() {
    numbers.assertIsCloseTo(someInfo(), ONE, null, within(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    numbers.assertIsCloseTo(someInfo(), ONE, ZERO, null);
  }

  // @format:off
  // error or failure

  @Test
  public void should_throw_error_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    numbers.assertIsCloseTo(someInfo(), null, ONE, within(ONE));
  }

  @Test
  public void should_throw_error_if_expected_value_is_null() {
    thrown.expectNullPointerException("The given number should not be null");
    numbers.assertIsCloseTo(someInfo(), SIX, null, offset(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_throw_error_if_offset_is_null() {
    numbers.assertIsCloseTo(someInfo(), ONE, ZERO, null);
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    AssertionInfo info = someInfo();
    try {
      numbers.assertIsCloseTo(info, ONE, TEN, within(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(ONE, TEN, within(ONE), NINE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value_with_a_strict_offset() {
    AssertionInfo info = someInfo();
    try {
      numbers.assertIsCloseTo(info, ONE, TEN, byLessThan(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(ONE, TEN, byLessThan(ONE), NINE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_difference_is_equal_to_the_given_strict_offset() {
    AssertionInfo info = someInfo();
    try {
      numbers.assertIsCloseTo(info, TWO, ONE, byLessThan(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(TWO, ONE, byLessThan(ONE), ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // with comparison stratgey

  @Test
  public void should_pass_if_difference_is_less_than_given_offset_whatever_custom_comparison_strategy_is() {
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ONE, within(ONE));
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, within(TEN));
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, byLessThan(TEN));
  }

  @Test
  public void should_pass_if_difference_is_equal_to_given_offset_whatever_custom_comparison_strategy_is() {
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ONE, within(ZERO));
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, ZERO, within(ONE));
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, within(ONE));
  }

  @Test
  public void should_throw_error_if_offset_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(offsetIsNull());
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), ONE, TWO, null);
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      numbersWithAbsValueComparisonStrategy.assertIsCloseTo(info, ONE, TEN, offset(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(ONE, TEN, offset(ONE), NINE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_not_strictly_close_enough_to_expected_value_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      numbersWithAbsValueComparisonStrategy.assertIsCloseTo(info, ONE, TEN, byLessThan(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(ONE, TEN, byLessThan(ONE), NINE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_expected_value_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException("The given number should not be null");
    numbersWithAbsValueComparisonStrategy.assertIsCloseTo(someInfo(), TWO, null, offset(ONE));
  }

}
