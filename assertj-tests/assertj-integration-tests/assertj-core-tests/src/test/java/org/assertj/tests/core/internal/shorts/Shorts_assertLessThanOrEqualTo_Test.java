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
package org.assertj.tests.core.internal.shorts;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Shorts;
import org.assertj.tests.core.testkit.AbsValueComparator;
import org.junit.jupiter.api.Test;

class Shorts_assertLessThanOrEqualTo_Test {

  private final ComparatorBasedComparisonStrategy absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<>());
  private final Shorts shortsWithAbsValueComparisonStrategy = new Shorts(absValueComparisonStrategy);

  @Test
  void should_fail_if_actual_is_null() {
    assertThatAssertionErrorIsThrownBy(() -> Shorts.instance().assertLessThanOrEqualTo(someInfo(), null,
                                                                                       (short) 8)).withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_less_than_other() {
    Shorts.instance().assertLessThanOrEqualTo(someInfo(), (short) 6, (short) 8);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other() {
    Shorts.instance().assertLessThanOrEqualTo(someInfo(), (short) 6, (short) 6);
  }

  @Test
  void should_fail_if_actual_is_greater_than_other() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> Shorts.instance().assertLessThanOrEqualTo(info, (short) 8, (short) 6));
    // THEN
    then(error).hasMessage(shouldBeLessOrEqual((short) 8, (short) 6).create());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> shortsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), null,
                                                                                                          (short) 8)).withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    shortsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), (short) 6, (short) -8);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    shortsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), (short) 6, (short) -6);
  }

  @Test
  void should_fail_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> shortsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(info, (short) -8,
                                                                                                        (short) 6));
    // THEN
    then(error).hasMessage(shouldBeLessOrEqual((short) -8, (short) 6, absValueComparisonStrategy).create());
  }
}
