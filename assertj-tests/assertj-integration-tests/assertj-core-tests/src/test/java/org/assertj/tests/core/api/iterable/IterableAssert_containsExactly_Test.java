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
package org.assertj.tests.core.api.iterable;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.Configuration.MAX_INDICES_FOR_PRINTING;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactlyWithIndexes;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.testkit.SinglyIterableFactory.createSinglyIterable;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.stream.IntStream;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.IndexedDiff;
import org.junit.jupiter.api.Test;

/// @author Joel Costigliola
/// @author Nicolas François
class IterableAssert_containsExactly_Test {

  @Test
  void should_pass_if_actual_contains_exactly_given_values() {
    assertThat(list("Luke", "Yoda", "Leia")).containsExactly("Luke", "Yoda", "Leia");
  }

  @Test
  void should_pass_if_non_restartable_actual_contains_exactly_given_values() {
    // GIVEN
    Iterable<String> actual = createSinglyIterable(list("Luke", "Yoda", "Leia"));
    // WHEN/THEN
    then(actual).containsExactly("Luke", "Yoda", "Leia");
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_with_null_elements() {
    assertThat(list("Luke", "Yoda", "Leia", null)).containsExactly("Luke", "Yoda", "Leia", null);
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsExactly();
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactly());
    // THEN
    then(assertionError).hasMessage(shouldContainExactly(actual, emptyList(), emptyList(), actual).create());
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(List.<String> of()).containsExactly(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactly("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactly(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactly(actual, list(expected), list("Han"), list("Leia")).create());
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_in_different_order() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Leia", "Yoda" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactly(expected));
    // THEN
    List<IndexedDiff> indexDiffs = list(new IndexedDiff("Yoda", "Leia", 1),
                                        new IndexedDiff("Leia", "Yoda", 2));
    then(assertionError).hasMessage(shouldContainExactlyWithIndexes(actual, list(expected), indexDiffs).create());
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    // GIVEN
    List<String> actual = list("Luke", "Leia", "Luke");
    String[] expected = { "Luke", "Leia" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactly(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactly(actual, list(expected), emptyList(), list("Luke")).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsExactly("LUKE", "YODA", "Leia");
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Yoda", "Han" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsExactly(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactly(actual, list(expected), list("Han"), list("Leia"),
                                                         comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_in_different_order_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Leia", "Yoda" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsExactly(expected));
    // THEN
    List<IndexedDiff> indexDiffs = list(new IndexedDiff("Yoda", "Leia", 1),
                                        new IndexedDiff("Leia", "Yoda", 2));
    then(assertionError).hasMessage(shouldContainExactlyWithIndexes(actual, list(expected), indexDiffs,
                                                                    comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Leia", "Luke");
    String[] expected = { "LUKE", "Leia" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsExactly(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactly(actual, list(expected), emptyList(), list("Luke"),
                                                         comparisonStrategy).create());
  }

  @Test
  void should_fail_if_order_does_not_match_and_total_printed_indexes_should_be_equal_to_max_elements_for_printing() {
    // GIVEN
    // actual: [0, 1, ..., MAX_INDICES_FOR_PRINTING], expected: [1, 2, ..., MAX_INDICES_FOR_PRINTING, 0]
    // => elements differ at every index, i.e. MAX_INDICES_FOR_PRINTING + 1 differences
    List<Integer> actual = IntStream.rangeClosed(0, MAX_INDICES_FOR_PRINTING).boxed().toList();
    Integer[] expected = IntStream.rangeClosed(1, MAX_INDICES_FOR_PRINTING + 1)
                                  .map(i -> i % (MAX_INDICES_FOR_PRINTING + 1))
                                  .boxed()
                                  .toArray(Integer[]::new);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactly(expected));
    // THEN
    int maxIndex = MAX_INDICES_FOR_PRINTING - 1;
    then(assertionError).hasMessageContaining("element at index " + maxIndex + ":")
                        .hasMessageNotContaining("element at index " + (maxIndex + 1) + ":");
  }

}
