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
package org.assertj.core.internal.comparables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Comparator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparablesBaseTest;
import org.assertj.core.util.AbsValueComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Comparables#assertEqualByComparison(AssertionInfo, Comparable, Comparable)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Comparables_assertEqualByComparison_Test extends ComparablesBaseTest {

  @Override
  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return new AbsValueComparator<BigDecimal>();
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> comparables.assertEqualByComparison(someInfo(), null, 8))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_objects_are_equal() {
    BigDecimal a = new BigDecimal("10.0");
    BigDecimal e = new BigDecimal("10.000");
    // we use BigDecimal to ensure that 'compareTo' is being called, since BigDecimal is the only Comparable where
    // 'compareTo' is not consistent with 'equals'
    assertThat(a.equals(e)).isFalse();
    comparables.assertEqualByComparison(someInfo(), a, e);
  }

  @Test
  void should_fail_if_objects_are_not_equal() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> comparables.assertEqualByComparison(info, "Luke", "Yoda"));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual("Luke", "Yoda", info.representation()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> comparablesWithCustomComparisonStrategy.assertEqualByComparison(someInfo(),
                                                                                                                                     null,
                                                                                                                                     BigDecimal.ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_objects_are_equal_whatever_custom_comparison_strategy_is() {
    BigDecimal a = new BigDecimal("10.0");
    BigDecimal e = new BigDecimal("10.000");
    // we use BigDecimal to ensure that 'compareTo' is being called, since BigDecimal is the only Comparable where
    // 'compareTo' is not consistent with 'equals'
    assertThat(a.equals(e)).isFalse();
    comparablesWithCustomComparisonStrategy.assertEqualByComparison(someInfo(), a, e);
  }

  @Test
  void should_fail_if_objects_are_not_equal_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> comparablesWithCustomComparisonStrategy.assertEqualByComparison(info, "Luke", "Yoda"));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual("Luke", "Yoda", info.representation()));
  }

}
