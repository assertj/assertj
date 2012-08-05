/*
 * Created on Feb 8, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal.bigdecimals;

import static org.fest.assertions.test.TestData.someInfo;

import java.math.BigDecimal;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.BigDecimals;
import org.fest.assertions.internal.BigDecimalsBaseTest;

/**
 * Tests for <code>{@link BigDecimals#assertIsNegative(AssertionInfo, BigDecimal)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class BigDecimals_assertIsNegative_Test extends BigDecimalsBaseTest {

  @Test
  public void should_succeed_since_actual_is_negative() {
    bigDecimals.assertIsNegative(someInfo(), new BigDecimal("-1.0"));
  }

  @Test
  public void should_fail_since_actual_is_not_negative() {
    thrown.expectAssertionError("expected:<1> to be less than:<0>");
    bigDecimals.assertIsNegative(someInfo(), BigDecimal.ONE);
  }

  @Test
  public void should_succeed_since_actual_is_negative_according_to_custom_comparison_strategy() {
    bigDecimalsWithComparatorComparisonStrategy.assertIsNegative(someInfo(), new BigDecimal("-1.0"));
  }

  @Test
  public void should_fail_since_actual_is_not_negative_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError("expected:<1> to be less than:<0>");
    bigDecimalsWithComparatorComparisonStrategy.assertIsNegative(someInfo(), BigDecimal.ONE);
  }

}
