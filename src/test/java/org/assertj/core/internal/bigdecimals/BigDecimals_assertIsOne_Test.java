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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.bigdecimals;

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestData.someInfo;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link BigDecimals#assertIsOne(AssertionInfo, BigDecimal)}</code>.
 *
 * @author Drummond Dawson
 */
public class BigDecimals_assertIsOne_Test extends BigDecimalsBaseTest {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_succeed_since_actual_is_one() {
    numbers.assertIsOne(someInfo(), BigDecimal.ONE);
  }

  @Test
  public void should_fail_since_actual_is_not_one() {
    thrown.expectAssertionError("expected:<[1]> but was:<[0]>");
    numbers.assertIsOne(someInfo(), BigDecimal.ZERO);
  }

  @Test
  public void should_succeed_since_actual_is_one_whatever_custom_comparison_strategy_is() {
    numbersWithComparatorComparisonStrategy.assertIsOne(someInfo(), BigDecimal.ONE);
  }

  @Test
  public void should_fail_since_actual_is_not_one_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError("expected:<[1]> but was:<[0]>");
    numbersWithComparatorComparisonStrategy.assertIsOne(someInfo(), BigDecimal.ZERO);
  }

}
