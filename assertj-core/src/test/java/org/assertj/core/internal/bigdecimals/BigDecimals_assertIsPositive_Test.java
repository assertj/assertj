/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.bigdecimals;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.testkit.TestData.someInfo;

import java.math.BigDecimal;

import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BigDecimals#assertIsPositive(org.assertj.core.api.AssertionInfo, Comparable)}</code>.
 *
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
class BigDecimals_assertIsPositive_Test extends BigDecimalsBaseTest {

  @Test
  void should_succeed_since_actual_is_positive() {
    numbers.assertIsPositive(someInfo(), BigDecimal.ONE);
  }

  @Test
  void should_fail_since_actual_is_not_positive() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertIsPositive(someInfo(), BigDecimal.ZERO))
                                                   .withMessage(format("%nExpecting actual:%n  0%nto be greater than:%n  0%n"));
  }

  @Test
  void should_fail_since_actual_is_zero() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertIsPositive(someInfo(), BigDecimal.ZERO))
                                                   .withMessage(format("%nExpecting actual:%n  0%nto be greater than:%n  0%n"));
  }

  @Test
  void should_succeed_since_actual_is_positive_according_to_custom_comparison_strategy() {
    numbersWithComparatorComparisonStrategy.assertIsPositive(someInfo(), BigDecimal.ONE);
  }

  @Test
  void should_fail_since_actual_is_not_positive_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbersWithComparatorComparisonStrategy.assertIsPositive(someInfo(),
                                                                                                                              BigDecimal.ZERO))
                                                   .withMessage(format("%nExpecting actual:%n  0%nto be greater than:%n  0%nwhen comparing values using org.assertj.core.util.BigDecimalComparator"));
  }

}
