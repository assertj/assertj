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
package org.assertj.core.internal.bigdecimals;

import static java.math.BigDecimal.*;

import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link BigDecimals#assertNotEqualByComparison(AssertionInfo, BigDecimal, bigdecimal)}</code>.
 * 
 * @author Joel Costigliola
 */
public class BigDecimals_assertNotEqualByComparison_Test extends BigDecimalsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    numbers.assertNotEqualByComparison(someInfo(), null, ONE);
  }

  @Test
  public void should_pass_if_big_decimals_are_not_equal_by_comparison() {
    numbers.assertNotEqualByComparison(someInfo(), TEN, ONE);
  }

  @Test
  public void should_fail_if_big_decimals_are_equal_by_comparison() {
    AssertionInfo info = someInfo();
    try {
      numbers.assertNotEqualByComparison(info, ONE_WITH_3_DECIMALS, ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(ONE_WITH_3_DECIMALS, ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    numbersWithAbsValueComparisonStrategy.assertNotEqualByComparison(someInfo(), null, ONE);
  }

  @Test
  public void should_pass_if_big_decimals_are_not_equal_by_comparison_whatever_custom_comparison_strategy_is() {
    numbersWithAbsValueComparisonStrategy.assertNotEqualByComparison(someInfo(), TEN, ONE);
  }

  @Test
  public void should_fail_if_big_decimals_are_equal_by_comparison_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      numbersWithAbsValueComparisonStrategy.assertNotEqualByComparison(info, ONE_WITH_3_DECIMALS, ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(ONE_WITH_3_DECIMALS, ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
