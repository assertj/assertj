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
package org.assertj.core.internal.bigintegers;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigIntegers;
import org.assertj.core.internal.BigIntegersBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BigIntegers#assertNotEqual(AssertionInfo, BigInteger, BigInteger)}</code>.
 */
class BigIntegers_assertNotEqual_Test extends BigIntegersBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertNotEqual(someInfo(), null, ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_big_integers_are_not_equal() {
    numbers.assertNotEqual(someInfo(), ONE, TEN);
  }

  @Test
  void should_fail_if_big_integers_are_equal() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbers.assertNotEqual(info, ONE, ONE));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(ONE, ONE));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbersWithComparatorComparisonStrategy.assertNotEqual(someInfo(),
                                                                                                                            null,
                                                                                                                            ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_big_integers_are_not_equal_according_to_custom_comparison_strategy() {
    numbersWithComparatorComparisonStrategy.assertNotEqual(someInfo(), TEN, ONE);
  }

  @Test
  void should_fail_if_big_integers_are_equal_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> numbersWithComparatorComparisonStrategy.assertNotEqual(info, ONE, ONE));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(ONE, ONE, comparatorComparisonStrategy));
  }
}
