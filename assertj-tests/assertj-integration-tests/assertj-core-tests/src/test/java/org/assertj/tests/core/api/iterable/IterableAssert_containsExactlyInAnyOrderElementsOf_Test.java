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
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Filip Hrisafov
class IterableAssert_containsExactlyInAnyOrderElementsOf_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyInAnyOrderElementsOf(list("Yoda")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_iterable_of_values_to_look_for_is_null() {
    // GIVEN
    List<String> values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(List.<String> of()).containsExactlyInAnyOrderElementsOf(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsExactlyInAnyOrderElementsOf(List.of());
  }

  @Test
  void should_pass_if_actual_contains_exactly_given_values_in_different_order() {
    assertThat(list("Yoda", "Luke", "Yoda")).containsExactlyInAnyOrderElementsOf(list("Yoda", "Yoda", "Luke"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    List<String> expected = list("Luke", "Yoda", "Han");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyInAnyOrderElementsOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactlyInAnyOrder(actual, expected.toArray(), list("Han"), list("Leia"),
                                                                   StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    // GIVEN
    List<String> actual = list("Luke", "Leia");
    List<String> expected = list("Luke", "Leia", "Luke");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyInAnyOrderElementsOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactlyInAnyOrder(actual, expected.toArray(), list("Luke"), emptyList(),
                                                                   StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsExactlyInAnyOrderElementsOf(list("leia", "LUKE", "YODA"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    List<String> expected = list("Luke", "Yoda", "Han");
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsExactlyInAnyOrderElementsOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactlyInAnyOrder(actual, expected.toArray(), list("Han"), list("Leia"),
                                                                   comparisonStrategy).create());
  }

}
