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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static java.util.Collections.emptyList;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.Configuration.MAX_INDICES_FOR_PRINTING;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactlyWithIndexes;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.internal.iterables.SinglyIterableFactory.createSinglyIterable;
import static org.assertj.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.stream.IntStream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.IndexedDiff;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsExactly(AssertionInfo, Iterable, Object[])}</code>.
 *
 * @author Joel Costigliola
 */
class Iterables_assertContainsExactly_Test extends IterablesBaseTest {

  @Test
  void should_pass_if_actual_contains_exactly_given_values() {
    iterables.assertContainsExactly(INFO, actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_non_restartable_actual_contains_exactly_given_values() {
    iterables.assertContainsExactly(INFO, createSinglyIterable(actual), array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_with_null_elements() {
    // GIVEN
    actual.add(null);
    // WHEN/THEN
    iterables.assertContainsExactly(INFO, actual, array("Luke", "Yoda", "Leia", null));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    // GIVEN
    actual.clear();
    // WHEN/THEN
    iterables.assertContainsExactly(INFO, actual, array());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    Object[] values = emptyArray();
    // WHEN/THEN
    expectAssertionError(() -> iterables.assertContainsExactly(INFO, actual, values));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> iterables.assertContainsExactly(INFO, emptyList(), null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertContainsExactly(INFO, actual, array("Yoda")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> iterables.assertContainsExactly(INFO, actual, expected));
    // THEN
    List<String> notFound = list("Han");
    List<String> notExpected = list("Leia");
    verify(failures).failure(INFO, shouldContainExactly(actual, asList(expected), notFound, notExpected), actual,
                             asList(expected));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_in_different_order() {
    // GIVEN
    Object[] expected = { "Luke", "Leia", "Yoda" };
    // WHEN
    expectAssertionError(() -> iterables.assertContainsExactly(INFO, actual, expected));
    // THEN
    List<IndexedDiff> indexDiffs = list(new IndexedDiff("Yoda", "Leia", 1),
                                        new IndexedDiff("Leia", "Yoda", 2));
    verify(failures).failure(INFO, shouldContainExactlyWithIndexes(actual, list(expected), indexDiffs), actual, list(expected));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    // GIVEN
    actual = list("Luke", "Leia", "Luke");
    Object[] expected = { "Luke", "Leia" };
    // WHEN
    expectAssertionError(() -> iterables.assertContainsExactly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldContainExactly(actual, asList(expected), emptyList(), list("Luke")), actual,
                             asList(expected));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(INFO, actual, array("LUKE", "YODA", "Leia"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldContainExactly(actual, asList(expected), list("Han"), list("Leia"), comparisonStrategy),
                             actual, asList(expected));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_in_different_order_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] expected = { "Luke", "Leia", "Yoda" };
    // WHEN
    expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(INFO, actual, expected));
    // THEN
    List<IndexedDiff> indexDiffs = list(new IndexedDiff("Yoda", "Leia", 1),
                                        new IndexedDiff("Leia", "Yoda", 2));
    verify(failures).failure(INFO, shouldContainExactlyWithIndexes(actual, list(expected), indexDiffs, comparisonStrategy),
                             actual, list(expected));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    // GIVEN
    actual = list("Luke", "Leia", "Luke");
    Object[] expected = { "LUKE", "Leia" };
    // WHEN
    expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldContainExactly(actual, asList(expected), emptyList(), list("Luke"), comparisonStrategy),
                             actual, asList(expected));
  }

  @Test
  void should_fail_if_order_does_not_match_and_total_printed_indexes_should_be_equal_to_max_elements_for_printing() {
    // GIVEN
    List<Integer> actual = IntStream.rangeClosed(0, MAX_INDICES_FOR_PRINTING).boxed().collect(toList());
    Object[] expected = IntStream.rangeClosed(0, MAX_INDICES_FOR_PRINTING).boxed().sorted(reverseOrder()).toArray();
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertContainsExactly(INFO, actual, expected));
    // THEN
    int maxIndex = MAX_INDICES_FOR_PRINTING - 1;
    then(error).hasMessageContaining("index " + maxIndex)
               .hasMessageNotContaining("index " + maxIndex + 1);
  }
}
