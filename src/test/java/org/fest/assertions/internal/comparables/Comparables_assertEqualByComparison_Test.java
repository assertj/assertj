/*
 * Created on Oct 17, 2010
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
package org.fest.assertions.internal.comparables;

import static org.fest.assertions.error.ShouldBeEqual.shouldBeEqual;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Comparator;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Comparables;
import org.fest.assertions.internal.ComparablesBaseTest;
import org.fest.assertions.util.AbsValueComparator;

/**
 * Tests for <code>{@link Comparables#assertEqualByComparison(AssertionInfo, Comparable, Comparable)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Comparables_assertEqualByComparison_Test extends ComparablesBaseTest {

  @Override
  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return new AbsValueComparator<BigDecimal>();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    comparables.assertEqualByComparison(someInfo(), null, 8);
  }

  @Test
  public void should_pass_if_objects_are_equal() {
    BigDecimal a = new BigDecimal("10.0");
    BigDecimal e = new BigDecimal("10.000");
    // we use BigDecimal to ensure that 'compareTo' is being called, since BigDecimal is the only Comparable where
    // 'compareTo' is not consistent with 'equals'
    assertFalse(a.equals(e));
    comparables.assertEqualByComparison(someInfo(), a, e);
  }

  @Test
  public void should_fail_if_objects_are_not_equal() {
    AssertionInfo info = someInfo();
    try {
      comparables.assertEqualByComparison(info, "Luke", "Yoda");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual("Luke", "Yoda"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    comparablesWithCustomComparisonStrategy.assertEqualByComparison(someInfo(), null, BigDecimal.ONE);
  }

  @Test
  public void should_pass_if_objects_are_equal_whatever_custom_comparison_strategy_is() {
    BigDecimal a = new BigDecimal("10.0");
    BigDecimal e = new BigDecimal("10.000");
    // we use BigDecimal to ensure that 'compareTo' is being called, since BigDecimal is the only Comparable where
    // 'compareTo' is not consistent with 'equals'
    assertFalse(a.equals(e));
    comparablesWithCustomComparisonStrategy.assertEqualByComparison(someInfo(), a, e);
  }

  @Test
  public void should_fail_if_objects_are_not_equal_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      comparablesWithCustomComparisonStrategy.assertEqualByComparison(info, "Luke", "Yoda");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual("Luke", "Yoda"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
