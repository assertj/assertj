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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.math.BigDecimal;

import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link BigDecimals#assertIsNotPositive(AssertionInfo, BigDecimal))}</code>.
 * 
 * @author Nicolas François
 */
class BigDecimals_assertIsNotPositive_Test extends BigDecimalsBaseTest {

  @Test
  void should_succeed_since_actual_is_not_positive() {
    numbers.assertIsNotPositive(someInfo(), new BigDecimal(-6));
  }

  @Test
  void should_succeed_since_actual_is_zero() {
    numbers.assertIsNotPositive(someInfo(), BigDecimal.ZERO);
  }

  @Test
  void should_fail_since_actual_is_positive() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> numbers.assertIsNotPositive(someInfo(), new BigDecimal(6)));
    // THEN
    then(assertionError).hasMessage(shouldBeLessOrEqual(new BigDecimal(6), BigDecimal.ZERO).create());
  }

  @Test
  void should_fail_since_actual_can_be_positive_according_to_custom_comparison_strategy() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> numbersWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(), new BigDecimal(-1)));
    // THEN
    then(assertionError).hasMessage(shouldBeLessOrEqual(new BigDecimal(-1), BigDecimal.ZERO, absValueComparisonStrategy).create());
  }

  @Test
  void should_fail_since_actual_is_positive_according_to_custom_comparison_strategy() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> numbersWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(), BigDecimal.ONE));
    // THEN
    then(assertionError).hasMessage(shouldBeLessOrEqual(BigDecimal.ONE, BigDecimal.ZERO, absValueComparisonStrategy).create());
  }

}
