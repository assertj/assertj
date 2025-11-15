/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSorted;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.error.ShouldBeSorted.shouldHaveComparableElementsAccordingToGivenComparator;
import static org.assertj.core.error.ShouldBeSorted.shouldHaveMutuallyComparableElements;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class ObjectArrays_assertIsSorted_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Leia", "Luke", "Luke", "Vador", "Yoda");
  }

  @Test
  void should_pass_if_actual_is_sorted_in_ascending_order() {
    arrays.assertIsSorted(INFO, actual);
  }

  @Test
  void should_pass_if_actual_is_empty_with_comparable_component_type() {
    arrays.assertIsSorted(INFO, new String[0]);
  }

  @Test
  void should_pass_if_actual_is_empty_with_non_comparable_component_type() {
    arrays.assertIsSorted(INFO, array());
  }

  @Test
  void should_pass_if_actual_contains_only_one_comparable_element() {
    arrays.assertIsSorted(INFO, array("Obiwan"));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertIsSorted(INFO, null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_sorted_in_ascending_order() {
    // GIVEN
    var actual = array("Luke", "Yoda", "Leia");
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertIsSorted(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeSorted(1, actual).create());
  }

  @Test
  void should_fail_if_actual_has_only_one_element_with_non_comparable_component_type() {
    // GIVEN
    Object[] actual = array(new Object());
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertIsSorted(INFO, actual));
    // THEN
    then(error).hasMessage(shouldHaveMutuallyComparableElements(actual).create());
  }

  @Test
  void should_fail_if_actual_has_some_elements_with_non_comparable_component_type() {
    // GIVEN
    Object[] actual = array("bar", new Object(), "foo");
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertIsSorted(INFO, actual));
    // THEN
    then(error).hasMessage(shouldHaveMutuallyComparableElements(actual).create());
  }

  @Test
  void should_fail_if_actual_has_some_not_mutually_comparable_elements() {
    // GIVEN
    Object[] actual = new Object[] { "bar", 5, "foo" };
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertIsSorted(INFO, actual));
    // THEN
    then(error).hasMessage(shouldHaveMutuallyComparableElements(actual).create());
  }

  @Test
  void should_pass_if_actual_is_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    actual = array("leia", "Luke", "luke", "Vador", "Yoda");
    arraysWithCustomComparisonStrategy.assertIsSorted(INFO, actual);
  }

  @Test
  void should_pass_if_actual_is_empty_with_comparable_component_type_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertIsSorted(INFO, new String[0]);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertIsSorted(INFO, array());
  }

  @Test
  void should_pass_if_actual_contains_only_one_comparable_element_according_to_custom_comparison_strategy() {
    actual = array("Obiwan");
    arraysWithCustomComparisonStrategy.assertIsSorted(INFO, actual);
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertIsSorted(INFO, null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    // GIVEN
    var actual = array("LUKE", "Yoda", "Leia");
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertIsSorted(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeSortedAccordingToGivenComparator(1, actual, comparatorForCustomComparisonStrategy()).create());
  }

  @Test
  void should_fail_if_actual_has_only_one_element_with_non_comparable_component_type_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] actual = array(new Object());
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertIsSorted(INFO, actual));
    // THEN
    then(error).hasMessage(shouldHaveComparableElementsAccordingToGivenComparator(actual,
                                                                                  comparatorForCustomComparisonStrategy()).create());
  }

  @Test
  void should_fail_if_actual_has_some_elements_with_non_comparable_component_type_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] actual = array("bar", new Object(), "foo");
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertIsSorted(INFO, actual));
    // THEN
    then(error).hasMessage(shouldHaveComparableElementsAccordingToGivenComparator(actual,
                                                                                  comparatorForCustomComparisonStrategy()).create());
  }

  @Test
  void should_fail_if_actual_has_some_not_mutually_comparable_elements_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] actual = new Object[] { "bar", 5, "foo" };
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertIsSorted(INFO, actual));
    // THEN
    then(error).hasMessage(shouldHaveComparableElementsAccordingToGivenComparator(actual,
                                                                                  comparatorForCustomComparisonStrategy()).create());
  }

}
