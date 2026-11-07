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
package org.assertj.tests.core.internal.shortarrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSorted;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.ShortArrays.arrayOf;
import static org.assertj.tests.core.testkit.ShortArrays.emptyArray;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ShortArrays;
import org.assertj.tests.core.testkit.AbsValueComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShortArrays#assertIsSorted(AssertionInfo, short[])}</code>.
 * 
 * @author Joel Costigliola
 */
class ShortArrays_assertIsSorted_Test {

  private short[] actual = new short[] { 1, 2, 3, 4, 4 };
  private final ShortArrays arrays = ShortArrays.instance();
  private final ComparatorBasedComparisonStrategy absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<>());
  private final ShortArrays arraysWithCustomComparisonStrategy = new ShortArrays(absValueComparisonStrategy);

  @Test
  void should_pass_if_actual_is_sorted_in_ascending_order() {
    arrays.assertIsSorted(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_empty() {
    arrays.assertIsSorted(someInfo(), emptyArray());
  }

  @Test
  void should_pass_if_actual_contains_only_one_element() {
    arrays.assertIsSorted(someInfo(), arrayOf(1));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertIsSorted(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_sorted_in_ascending_order() {
    AssertionInfo info = someInfo();
    actual = arrayOf(1, 3, 2);

    var error = expectAssertionError(() -> arrays.assertIsSorted(info, actual));

    then(error).hasMessage(shouldBeSorted(1, actual).create());
  }

  @Test
  void should_pass_if_actual_is_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    actual = new short[] { 1, -2, 3, -4, 4 };
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), emptyArray());
  }

  @Test
  void should_pass_if_actual_contains_only_one_element_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), arrayOf(1));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(),
                                                                                                                       null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(1, 3, 2);

    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertIsSorted(info, actual));

    then(error).hasMessage(shouldBeSortedAccordingToGivenComparator(1, actual, new AbsValueComparator<>()).create());
  }

}
