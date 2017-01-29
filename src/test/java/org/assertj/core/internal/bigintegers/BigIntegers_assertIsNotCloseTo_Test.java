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
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.internal.BigIntegersBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

/**
 * Tests for <code>{@link org.assertj.core.internal.BigIntegers#assertIsNotCloseTo(AssertionInfo, BigInteger, BigInteger, Offset)}</code>.
 */
@RunWith(DataProviderRunner.class)
public class BigIntegers_assertIsNotCloseTo_Test extends BigIntegersBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    numbers.assertIsNotCloseTo(someInfo(), null, ONE, byLessThan(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_expected_value_is_null() {
    numbers.assertIsNotCloseTo(someInfo(), ONE, null, byLessThan(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    numbers.assertIsNotCloseTo(someInfo(), ONE, ZERO, null);
  }

  @Test
  public void should_fail_if_big_integers_difference_is_less_than_given_offset() {
    BigInteger FIVE = new BigInteger("5");
    AssertionInfo info = someInfo();
    try {
      numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, FIVE, FIVE, byLessThan(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info,
                               shouldNotBeEqual(FIVE, FIVE, byLessThan(ONE), FIVE.subtract(FIVE)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // @format:off
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
  public void should_fail_if_big_integers_difference_is_equal_to_given_offset(BigInteger actual, BigInteger expected,
                                                                              BigInteger offset) {
    AssertionInfo info = someInfo();
    Offset<BigInteger> bigDecimalOffset = byLessThan(offset);
    try {
      numbers.assertIsNotCloseTo(info, actual, expected, bigDecimalOffset);
    } catch (AssertionError e) {
      verify(failures).failure(info,
                               shouldNotBeEqual(actual, expected, bigDecimalOffset, actual.subtract(expected).abs()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_big_integers_difference_is_greater_than_offset() {
    numbers.assertIsNotCloseTo(someInfo(), TEN, ONE, byLessThan(ONE));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(someInfo(), null, ONE, byLessThan(ONE));
  }

  @Test
  public void should_fail_if_big_integers_are_equal() {
    BigInteger FIVE = new BigInteger("5");
    AssertionInfo info = someInfo();
    try {
      numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, FIVE, FIVE, byLessThan(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(FIVE, FIVE, byLessThan(ONE), FIVE.subtract(FIVE)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_big_integers_are_not_close_whatever_custom_comparison_strategy_is() {
    numbersWithAbsValueComparisonStrategy.assertIsNotCloseTo(someInfo(), TEN, ONE, byLessThan(ONE));
  }
}
