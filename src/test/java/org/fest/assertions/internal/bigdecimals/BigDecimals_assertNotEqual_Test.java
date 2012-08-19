/*
 * Created on Oct 24, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.bigdecimals;

import static java.math.BigDecimal.*;

import static org.fest.assertions.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.BigDecimals;
import org.fest.assertions.internal.BigDecimalsBaseTest;

/**
 * Tests for <code>{@link BigDecimals#assertNotEqual(AssertionInfo, BigDecimal, bigdecimal)}</code>.
 * 
 * @author Joel Costigliola
 */
public class BigDecimals_assertNotEqual_Test extends BigDecimalsBaseTest {

  private static final BigDecimal ONE_WITH_3_DECIMALS = new BigDecimal("1.000");

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    bigDecimals.assertNotEqual(someInfo(), null, ONE);
  }

  @Test
  public void should_pass_if_big_decimals_are_not_equal() {
    bigDecimals.assertNotEqual(someInfo(), ONE, ONE_WITH_3_DECIMALS);
  }

  @Test
  public void should_fail_if_big_decimals_are_equal() {
    AssertionInfo info = someInfo();
    try {
      bigDecimals.assertNotEqual(info, ONE, ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(ONE, ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    bigDecimalsWithComparatorComparisonStrategy.assertNotEqual(someInfo(), null, ONE);
  }

  @Test
  public void should_pass_if_big_decimals_are_not_equal_according_to_custom_comparison_strategy() {
    bigDecimalsWithComparatorComparisonStrategy.assertNotEqual(someInfo(), TEN, ONE);
  }

  @Test
  public void should_fail_if_big_decimals_are_equal_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      bigDecimalsWithComparatorComparisonStrategy.assertNotEqual(info, ONE_WITH_3_DECIMALS, ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(ONE_WITH_3_DECIMALS, ONE, comparatorComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
