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

import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link BigDecimals#assertIsPositive(org.assertj.core.api.AssertionInfo, Comparable)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class BigDecimals_assertIsPositive_Test extends BigDecimalsBaseTest {

  @Test
  public void should_succeed_since_actual_is_positive() {
    bigDecimals.assertIsPositive(someInfo(), BigDecimal.ONE);
  }

  @Test
  public void should_fail_since_actual_is_not_positive() {
    thrown.expectAssertionError("%nExpecting:%n <0>%nto be greater than:%n <0>");
    bigDecimals.assertIsPositive(someInfo(), BigDecimal.ZERO);
  }

  @Test
  public void should_fail_since_actual_is_zero() {
	thrown.expectAssertionError("%nExpecting:%n <0>%nto be greater than:%n <0>");
	bigDecimals.assertIsPositive(someInfo(), BigDecimal.ZERO);
  }
  
  @Test
  public void should_succeed_since_actual_is_positive_according_to_custom_comparison_strategy() {
    bigDecimalsWithComparatorComparisonStrategy.assertIsPositive(someInfo(), BigDecimal.ONE);
  }

  @Test
  public void should_fail_since_actual_is_not_positive_according_to_custom_comparison_strategy() {
    thrown
        .expectAssertionError("%nExpecting:%n <0>%nto be greater than:%n <0> when comparing values using 'BigDecimalComparator'");
    bigDecimalsWithComparatorComparisonStrategy.assertIsPositive(someInfo(), BigDecimal.ZERO);
  }

}
