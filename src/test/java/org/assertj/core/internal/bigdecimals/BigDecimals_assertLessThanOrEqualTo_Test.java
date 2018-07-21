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
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;
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
 * Tests for <code>{@link BigDecimals#assertLessThanOrEqualTo(AssertionInfo, BigDecimal, bigdecimal)}</code>.
 * 
 * @author Joel Costigliola
 */
public class BigDecimals_assertLessThanOrEqualTo_Test extends BigDecimalsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertLessThanOrEqualTo(someInfo(), null, ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_is_less_than_other() {
    numbers.assertLessThanOrEqualTo(someInfo(), ONE, TEN);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other() {
    numbers.assertLessThanOrEqualTo(someInfo(), ONE, ONE);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other_by_comparison() {
	numbers.assertLessThanOrEqualTo(someInfo(), ONE, new BigDecimal("1.00"));
  }  
 
  @Test
  public void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();
    try {
      numbers.assertLessThanOrEqualTo(info, TEN, ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLessOrEqual(TEN, ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    numbersWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), ONE, TEN.negate());
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    numbersWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), ONE.negate(), ONE);
  }

  @Test
  public void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      numbersWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(info, TEN.negate(), ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLessOrEqual(TEN.negate(), ONE, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
