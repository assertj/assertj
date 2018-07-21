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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.bigdecimals;

import static java.math.BigDecimal.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeGreater.shouldBeGreater;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link BigDecimals#assertGreaterThan(AssertionInfo, BigDecimal, bigdecimal)}</code>.
 * 
 * @author Joel Costigliola
 */
public class BigDecimals_assertGreaterThan_Test extends BigDecimalsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertGreaterThan(someInfo(), null, ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_is_greater_than_other() {
    numbers.assertGreaterThan(someInfo(), TEN, ONE);
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other() {
    AssertionInfo info = someInfo();
    try {
      numbers.assertGreaterThan(info, TEN, TEN);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreater(TEN, TEN));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other_by_comparison() {
	AssertionInfo info = someInfo();
	try {
	  numbers.assertGreaterThan(info, TEN, new BigDecimal("10.00"));
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldBeGreater(TEN, new BigDecimal("10.00")));
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
  @Test
  public void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();
    try {
      numbers.assertGreaterThan(info, ONE, TEN);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreater(ONE, TEN));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    numbersWithAbsValueComparisonStrategy.assertGreaterThan(someInfo(), TEN.negate(), ONE);
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      numbersWithAbsValueComparisonStrategy.assertGreaterThan(info, TEN.negate(), TEN);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreater(TEN.negate(), TEN, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      numbersWithAbsValueComparisonStrategy.assertGreaterThan(info, ONE, TEN.negate());
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreater(ONE, TEN.negate(), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
