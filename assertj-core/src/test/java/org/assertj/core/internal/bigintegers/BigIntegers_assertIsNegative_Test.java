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
package org.assertj.core.internal.bigintegers;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeLess.shouldBeLess;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.math.BigInteger;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigIntegers;
import org.assertj.core.internal.BigIntegersBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BigIntegers#assertIsNegative(AssertionInfo, BigInteger)}</code>.
 */
class BigIntegers_assertIsNegative_Test extends BigIntegersBaseTest {

  @Test
  void should_succeed_since_actual_is_negative() {
    numbers.assertIsNegative(someInfo(), new BigInteger("-1"));
  }

  @Test
  void should_succeed_since_actual_is_negative_according_to_custom_comparison_strategy() {
    numbersWithComparatorComparisonStrategy.assertIsNegative(someInfo(), new BigInteger("-1"));
  }

  @Test
  void should_fail_since_actual_is_zero() {
    // WHEN
    AssertionError error = expectAssertionError(() -> numbersWithAbsValueComparisonStrategy.assertIsNegative(someInfo(),
                                                                                                             BigInteger.ZERO));
    // THEN
    then(error).hasMessage(shouldBeLess(BigInteger.ZERO, BigInteger.ZERO, absValueComparisonStrategy).create());
  }

  @Test
  void should_fail_since_actual_is_not_negative() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> numbers.assertIsNegative(someInfo(), BigInteger.ONE));
    // THEN
    then(assertionError).hasMessage(shouldBeLess(BigInteger.ONE, BigInteger.ZERO).create());
  }

  @Test
  void should_fail_since_actual_is_not_negative_according_to_custom_comparison_strategy() {
    // WHEN
    AssertionError error = expectAssertionError(() -> numbersWithAbsValueComparisonStrategy.assertIsNegative(someInfo(),
                                                                                                             BigInteger.valueOf(-1)));
    // THEN
    then(error).hasMessage(shouldBeLess(BigInteger.valueOf(-1), BigInteger.ZERO, absValueComparisonStrategy).create());
  }

}
