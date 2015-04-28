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

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link BigDecimals#assertIsNotZero(AssertionInfo, BigDecimal)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class BigDecimals_assertIsNotZero_Test extends BigDecimalsBaseTest {

  @Test
  public void should_succeed_since_actual_is_zero() {
    bigDecimals.assertIsNotZero(someInfo(), BigDecimal.ONE);
  }

  @Test
  public void should_fail_since_actual_is_not_zero() {
    try {
      bigDecimals.assertIsNotZero(someInfo(), BigDecimal.ZERO);
    } catch (AssertionError e) {
      assertThat(e.getMessage()).isEqualTo(String.format("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n"));
    }
  }

  @Test
  public void should_succeed_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    bigDecimalsWithComparatorComparisonStrategy.assertIsNotZero(someInfo(), BigDecimal.ONE);
  }

  @Test
  public void should_fail_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    try {
      bigDecimalsWithComparatorComparisonStrategy.assertIsNotZero(someInfo(), BigDecimal.ZERO);
    } catch (AssertionError e) {
      assertThat(e.getMessage()).isEqualTo(String.format("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n"));
    }
  }

}
