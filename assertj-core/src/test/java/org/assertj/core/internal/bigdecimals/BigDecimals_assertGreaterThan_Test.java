/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.bigdecimals;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeGreater.shouldBeGreater;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BigDecimals#assertGreaterThan(AssertionInfo, BigDecimal, bigdecimal)}</code>.
 * 
 * @author Joel Costigliola
 */
class BigDecimals_assertGreaterThan_Test extends BigDecimalsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertGreaterThan(someInfo(), null, ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_greater_than_other() {
    numbers.assertGreaterThan(someInfo(), TEN, ONE);
  }

  @Test
  void should_fail_if_actual_is_equal_to_other() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> numbers.assertGreaterThan(info, TEN, TEN));
    // THEN
    then(error).hasMessage(shouldBeGreater(TEN, TEN).create());
  }

  @Test
  void should_fail_if_actual_is_equal_to_other_by_comparison() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> numbers.assertGreaterThan(info, TEN, new BigDecimal("10.00")));
    // THEN
    then(error).hasMessage(shouldBeGreater(TEN, new BigDecimal("10.00")).create());
  }

  @Test
  void should_fail_if_actual_is_less_than_other() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> numbers.assertGreaterThan(info, ONE, TEN));
    // THEN
    then(error).hasMessage(shouldBeGreater(ONE, TEN).create());
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    numbersWithAbsValueComparisonStrategy.assertGreaterThan(someInfo(), TEN.negate(), ONE);
  }

  @Test
  void should_fail_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> numbersWithAbsValueComparisonStrategy.assertGreaterThan(info, TEN.negate(), TEN));
    // THEN
    then(error).hasMessage(shouldBeGreater(TEN.negate(), TEN, absValueComparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> numbersWithAbsValueComparisonStrategy.assertGreaterThan(info, ONE, TEN.negate()));
    // THEN
    then(error).hasMessage(shouldBeGreater(ONE, TEN.negate(), absValueComparisonStrategy).create());
  }

}
