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
package org.assertj.core.internal.bigintegers;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigIntegers;
import org.assertj.core.internal.BigIntegersBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BigIntegers#assertGreaterThanOrEqualTo(AssertionInfo, BigInteger, BigInteger)}</code>.
 */
class BigIntegers_assertGreaterThanOrEqualTo_Test extends BigIntegersBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertGreaterThanOrEqualTo(someInfo(), null, ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_greater_than_other() {
    numbers.assertGreaterThanOrEqualTo(someInfo(), TEN, ONE);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other() {
    numbers.assertGreaterThanOrEqualTo(someInfo(), ONE, ONE);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other_by_comparison() {
    numbers.assertGreaterThanOrEqualTo(someInfo(), ONE, new BigInteger("1"));
  }

  @Test
  void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbers.assertGreaterThanOrEqualTo(info, ONE, TEN));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeGreaterOrEqual(ONE, TEN));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    numbersWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), TEN.negate(), ONE);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    numbersWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), ONE.negate(), ONE);
  }

  @Test
  void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(info, ONE, TEN.negate()));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeGreaterOrEqual(ONE, TEN.negate(), absValueComparisonStrategy));
  }

}
