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

import static org.fest.assertions.error.ShouldBeLess.shouldBeLess;
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
 * Tests for <code>{@link BigDecimals#assertLessThan(AssertionInfo, BigDecimal, bigdecimal)}</code>.
 * 
 * @author Joel Costigliola
 */
public class BigDecimals_assertLessThan_Test extends BigDecimalsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    bigDecimals.assertLessThan(someInfo(), null, ONE);
  }

  @Test
  public void should_pass_if_actual_is_less_than_other() {
    bigDecimals.assertLessThan(someInfo(), ONE, TEN);
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other() {
    AssertionInfo info = someInfo();
    try {
      bigDecimals.assertLessThan(info, TEN, TEN);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess(TEN, TEN));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();
    try {
      bigDecimals.assertLessThan(info, TEN, ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess(TEN, ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    bigDecimalsWithAbsValueComparisonStrategy.assertLessThan(someInfo(), ONE, TEN.negate());
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      bigDecimalsWithAbsValueComparisonStrategy.assertLessThan(info, TEN.negate(), TEN);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess(TEN.negate(), TEN, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      bigDecimalsWithAbsValueComparisonStrategy.assertLessThan(info, TEN.negate(), ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess(TEN.negate(), ONE, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
