/*
 * Created on May 28, 2012
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.internal.bigdecimals;

import static org.fest.assertions.test.TestData.someInfo;

import java.math.BigDecimal;

import org.junit.Test;

import org.fest.assertions.internal.BigDecimalsBaseTest;

/**
 * Tests for <code>{@link BigDecimals#assertIsNotNegative(AssertionInfo, BigDecimal))}</code>.
 * 
 * @author Nicolas François
 */
public class BigDecimals_assertIsNotNegative_Test extends BigDecimalsBaseTest {

  @Test
  public void should_succeed_since_actual_is_not_negative() {
    bigDecimals.assertIsNotNegative(someInfo(), new BigDecimal(6));
  }

  @Test
  public void should_succeed_since_actual_is_zero() {
    bigDecimals.assertIsNotNegative(someInfo(), BigDecimal.ZERO);
  }

  @Test
  public void should_fail_since_actual_is_negative() {
    thrown.expectAssertionError("expected:<-6> to be greater than or equal to:<0>");
    bigDecimals.assertIsNotNegative(someInfo(), new BigDecimal(-6));
  }

  @Test
  public void should_succeed_since_actual_is_not_negative_according_to_custom_comparison_strategy() {
    bigDecimalsWithAbsValueComparisonStrategy.assertIsNotNegative(someInfo(), new BigDecimal(-1));
  }

  @Test
  public void should_succeed_since_actual_positive_is_not_negative_according_to_custom_comparison_strategy() {
    bigDecimalsWithAbsValueComparisonStrategy.assertIsNotNegative(someInfo(), BigDecimal.ONE);
  }

}
