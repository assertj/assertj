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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.internal.bigdecimals;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.math.BigDecimal.*;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link org.assertj.core.internal.BigDecimals#assertIsNotCloseTo(AssertionInfo, BigDecimal, BigDecimal, org.assertj.core.data.Offset)}</code>.
 *
 * @author Chris Arnott
 */
@RunWith(DataProviderRunner.class)
public class BigDecimals_assertIsNotCloseTo_Test extends BigDecimalsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    bigDecimals.assertIsNotCloseTo(someInfo(), null, ONE, offset(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if__expected_value_is_null() {
    bigDecimals.assertIsNotCloseTo(someInfo(), ONE, null, within(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    bigDecimals.assertIsNotCloseTo(someInfo(), ONE, ZERO, null);
  }
  
  @Test
  public void should_fail_if_big_decimals_difference_is_less_than_given_offset() {

    BigDecimal FIVE_POINT_ONE = new BigDecimal("5.0");
    BigDecimal FIVE = new BigDecimal("5");
    AssertionInfo info = someInfo();
    try {
      bigDecimalsWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, FIVE_POINT_ONE, FIVE, offset(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(FIVE_POINT_ONE, FIVE, offset(ONE), FIVE_POINT_ONE.subtract(FIVE)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // @format:off
  @Test
  @DataProvider({
    "1.0, 1.0, 0.0",
    "1.0, 0.0, 1.0",
    "-1.0, 0.0, 1.0",
    "-1.0, -1.0, 0.0",
    "-1.0, 1.0, 2.0",
    "0.0, 0.000000000000000000000001, 0.000000000000000000000001",
    "-0.000000000000000000000001, -0.000000000000000000000001, 0.0"
  })
  // @format:on
  public void should_pass_if_big_decimals_difference_is_equal_to_given_offset(BigDecimal actual, BigDecimal expected, BigDecimal offset) {
    bigDecimals.assertIsNotCloseTo(someInfo(), actual, expected, offset(offset));
  }

  @Test
  public void should_pass_if_big_decimals_difference_is_greater_than_offset() {
    bigDecimals.assertIsNotCloseTo(someInfo(), TEN, ONE, offset(ONE));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    bigDecimalsWithAbsValueComparisonStrategy.assertIsNotCloseTo(someInfo(), null, ONE, offset(ONE));
  }

  @Test
  public void should_fail_if_big_decimals_are_equal() {
    BigDecimal FIVE_POINT_ZERO = new BigDecimal("5.0");
    BigDecimal FIVE = new BigDecimal("5");
    AssertionInfo info = someInfo();
    try {
      bigDecimalsWithAbsValueComparisonStrategy.assertIsNotCloseTo(info, FIVE_POINT_ZERO, FIVE, offset(ONE));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(FIVE_POINT_ZERO, FIVE, offset(ONE), FIVE_POINT_ZERO.subtract(FIVE)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_big_decimals_are_not_equal_by_comparison_whatever_custom_comparison_strategy_is() {
    bigDecimalsWithAbsValueComparisonStrategy.assertIsNotCloseTo(someInfo(), TEN, ONE, offset(ONE));
  }
}
