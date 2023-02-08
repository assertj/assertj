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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal.bigdecimals;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.test.TestData.someInfo;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BigDecimals#assertIsZero(AssertionInfo, BigDecimal)}</code>.
 *
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
class BigDecimals_assertIsZero_Test extends BigDecimalsBaseTest {

  @Test
  void should_succeed_since_actual_is_zero() {
    numbers.assertIsZero(someInfo(), BigDecimal.ZERO);
  }

  @Test
  void should_fail_since_actual_is_not_zero() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertIsZero(someInfo(), BigDecimal.ONE))
                                                   .withMessage(shouldBeEqualMessage("1", "0"));
  }

  @Test
  void should_succeed_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    numbersWithComparatorComparisonStrategy.assertIsZero(someInfo(), BigDecimal.ZERO);
  }

  @Test
  void should_fail_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbersWithComparatorComparisonStrategy.assertIsZero(someInfo(),
                                                                                                                          BigDecimal.ONE))
                                                   .withMessage(shouldBeEqualMessage("1", "0"));
  }

}
