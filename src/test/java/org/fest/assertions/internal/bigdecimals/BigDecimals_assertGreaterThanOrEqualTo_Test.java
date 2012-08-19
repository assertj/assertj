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

import static org.fest.assertions.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
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
 * Tests for <code>{@link BigDecimals#assertGreaterThanOrEqualTo(AssertionInfo, BigDecimal, bigdecimal)}</code>.
 * 
 * @author Joel Costigliola
 */
public class BigDecimals_assertGreaterThanOrEqualTo_Test extends BigDecimalsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    bigDecimals.assertGreaterThanOrEqualTo(someInfo(), null, ONE);
  }

  @Test
  public void should_pass_if_actual_is_greater_than_other() {
    bigDecimals.assertGreaterThanOrEqualTo(someInfo(), TEN, ONE);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other() {
    bigDecimals.assertGreaterThanOrEqualTo(someInfo(), ONE, ONE);
  }

  @Test
  public void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();
    try {
      bigDecimals.assertGreaterThanOrEqualTo(info, ONE, TEN);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreaterOrEqual(ONE, TEN));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    bigDecimalsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), TEN.negate(), ONE);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    bigDecimalsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), ONE.negate(), ONE);
  }

  @Test
  public void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      bigDecimalsWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(info, ONE, TEN.negate());
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeGreaterOrEqual(ONE, TEN.negate(), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
