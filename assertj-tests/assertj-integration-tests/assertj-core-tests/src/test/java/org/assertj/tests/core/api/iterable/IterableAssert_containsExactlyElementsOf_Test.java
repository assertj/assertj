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
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactlyWithIndexes;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.IndexedDiff;
import org.junit.jupiter.api.Test;

/// @author Jean-Christophe Gay
class IterableAssert_containsExactlyElementsOf_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyElementsOf(list("Yoda")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_iterable_of_values_to_look_for_is_null() {
    // GIVEN
    List<String> values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(List.<String> of()).containsExactlyElementsOf(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsExactlyElementsOf(List.of());
  }

  @Test
  void should_fail_if_iterable_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyElementsOf(List.of()));
    // THEN
    then(assertionError).hasMessage(shouldContainExactly(actual, emptyList(), emptyList(), actual).create());
  }

  @Test
  void should_pass_if_actual_contains_exactly_given_values() {
    assertThat(list("Luke", "Yoda", "Leia")).containsExactlyElementsOf(list("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    List<String> expected = list("Luke", "Yoda", "Han");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyElementsOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactly(actual, expected, list("Han"), list("Leia")).create());
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_in_different_order() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    List<String> expected = list("Luke", "Leia", "Yoda");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyElementsOf(expected));
    // THEN
    List<IndexedDiff> indexDiffs = list(new IndexedDiff("Yoda", "Leia", 1),
                                        new IndexedDiff("Leia", "Yoda", 2));
    then(assertionError).hasMessage(shouldContainExactlyWithIndexes(actual, expected, indexDiffs).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsExactlyElementsOf(list("LUKE", "YODA", "Leia"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    List<String> expected = list("Luke", "Yoda", "Han");
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsExactlyElementsOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactly(actual, expected, list("Han"), list("Leia"),
                                                         comparisonStrategy).create());
  }

}
