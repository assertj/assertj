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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.bigdecimals;

import static java.math.BigDecimal.ONE;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveScale.shouldHaveScale;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;

class BigDecimals_assertHasScale_Test extends BigDecimalsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    BigDecimal nullBigDecimal = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> numbers.assertHasScale(info, nullBigDecimal, 0));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_scales_are_equal() {
    numbers.assertHasScale(info, ONE_WITH_3_DECIMALS, 3);
  }

  @Test
  void should_pass_if_scales_are_equal_whatever_custom_strategy_is_used() {
    numbersWithAbsValueComparisonStrategy.assertHasScale(info, ONE_WITH_3_DECIMALS, 3);
  }

  @Test
  void should_fail_if_scales_are_not_equal() {
    // GIVEN
    final int expectedScale = 3;
    BigDecimal zeroScaleBigDecimal = ONE;
    // WHEN
    expectAssertionError(() -> numbers.assertHasScale(info, zeroScaleBigDecimal, expectedScale));
    // THEN
    verify(failures).failure(info, shouldHaveScale(zeroScaleBigDecimal, expectedScale));
  }

  @Test
  void should_fail_if_scales_are_not_equal_whatever_custom_strategy_is_used() {
    // GIVEN
    final int expectesScale = 3;
    BigDecimal zeroScaleBigDecimal = ONE;
    // WHEN
    expectAssertionError(() -> numbers.assertHasScale(info, zeroScaleBigDecimal, expectesScale));
    // THEN
    verify(failures).failure(info, shouldHaveScale(zeroScaleBigDecimal, expectesScale));
  }

}
