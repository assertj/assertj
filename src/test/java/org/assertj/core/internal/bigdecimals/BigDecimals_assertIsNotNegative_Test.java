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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.bigdecimals;

import static org.assertj.core.test.TestData.someInfo;

import java.math.BigDecimal;

import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.Test;


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
    thrown.expectAssertionError("%nExpecting:%n <-6>%nto be greater than or equal to:%n <0>");
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
