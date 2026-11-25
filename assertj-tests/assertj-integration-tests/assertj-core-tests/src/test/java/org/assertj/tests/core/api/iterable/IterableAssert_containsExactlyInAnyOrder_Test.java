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

/// @author Lovro Pandzic
class IterableAssert_containsExactlyInAnyOrder_Test {

  @Test
  void should_pass_if_actual_contains_exactly_given_values() {
    assertThat(list("Luke", "Yoda", "Leia")).containsExactlyInAnyOrder("Luke", "Yoda", "Leia");
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_with_null_elements() {
    assertThat(list("Luke", "Yoda", "Leia")).containsExactlyInAnyOrder("Leia", "Yoda", "Luke");
    assertThat(list("Luke", "Yoda", "Leia", null)).containsExactlyInAnyOrder("Leia", null, "Yoda", "Luke");
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsExactlyInAnyOrder();
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = {};
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyInAnyOrder(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactlyInAnyOrder(actual, expected, emptyList(), actual,
                                                                   StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_expected_is_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(List.<String> of()).containsExactlyInAnyOrder(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyInAnyOrder());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyInAnyOrder(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactlyInAnyOrder(actual, expected, list("Han"), list("Leia"),
                                                                   StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_contains_all_given_values_in_different_order() {
    assertThat(list("Luke", "Yoda", "Leia")).containsExactlyInAnyOrder("Luke", "Leia", "Yoda");
  }

  @Test
  void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    // GIVEN
    List<String> actual = list("Luke", "Leia", "Luke");
    String[] expected = { "Luke", "Leia" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyInAnyOrder(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactlyInAnyOrder(actual, expected, emptyList(), list("Luke"),
                                                                   StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    // GIVEN
    List<String> actual = list("Luke", "Leia");
    String[] expected = { "Luke", "Leia", "Luke" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyInAnyOrder(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactlyInAnyOrder(actual, expected, list("Luke"), emptyList(),
                                                                   StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsExactlyInAnyOrder("LUKE", "YODA", "Leia");
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Yoda", "Han" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsExactlyInAnyOrder(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactlyInAnyOrder(actual, expected, list("Han"), list("Leia"),
                                                                   comparisonStrategy).create());
  }

  @Test
  void should_pass_if_actual_contains_all_given_values_in_different_order_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsExactlyInAnyOrder("LUKE", "leia", "Yoda");
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Leia", "Luke");
    String[] expected = { "LUKE", "Leia" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsExactlyInAnyOrder(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainExactlyInAnyOrder(actual, expected, emptyList(), list("Luke"),
                                                                   comparisonStrategy).create());
  }

}
