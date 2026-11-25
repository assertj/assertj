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
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.testkit.SinglyIterableFactory.createSinglyIterable;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Joel Costigliola
class IterableAssert_containsOnly_Test {

  @Test
  void should_pass_if_actual_contains_given_values_only() {
    assertThat(list("Luke", "Yoda", "Leia")).containsOnly("Luke", "Yoda", "Leia");
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_with_null_elements() {
    assertThat(list("Luke", "Yoda", "Leia", null, null)).containsOnly("Luke", null, "Yoda", "Leia", null);
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order() {
    assertThat(list("Luke", "Yoda", "Leia")).containsOnly("Leia", "Yoda", "Luke");
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_more_than_once() {
    assertThat(list("Luke", "Yoda", "Leia", "Luke", "Luke")).containsOnly("Luke", "Yoda", "Leia");
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    assertThat(list("Luke", "Yoda", "Leia")).containsOnly("Luke", "Luke", "Luke", "Yoda", "Leia");
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsOnly();
  }

  @Test
  void should_pass_if_nonrestartable_actual_contains_only_given_values() {
    // GIVEN
    Iterable<String> actual = createSinglyIterable(list("Luke", "Yoda", "Leia"));
    // WHEN/THEN
    then(actual).containsOnly("Luke", "Yoda", "Leia");
  }

  @Test
  void should_fail_if_actual_is_empty_and_array_of_values_to_look_for_is_not() {
    // GIVEN
    List<String> actual = list();
    String[] expected = { "Luke", "Yoda", "Leia" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnly(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainOnly(actual, expected, list("Luke", "Yoda", "Leia"), emptyList()).create());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = {};
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnly(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainOnly(actual, expected, emptyList(), list("Luke", "Yoda", "Leia")).create());
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    // GIVEN
    String[] expected = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(list("Luke", "Yoda", "Leia")).containsOnly(expected));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnly("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_values() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnly(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainOnly(actual, expected, list("Han"), list("Leia")).create());
  }

  @Test
  void should_fail_if_actual_contains_additional_elements() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Yoda" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnly(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainOnly(actual, expected, emptyList(), list("Leia")).create());
  }

  @Test
  void should_fail_if_actual_contains_a_subset_of_expected_elements() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Yoda", "Obiwan", "Leia" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnly(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainOnly(actual, expected, list("Obiwan"), emptyList()).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsOnly("LUKE", "YODA", "Leia");
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsOnly("LEIA", "yoda", "LukE");
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_more_than_once_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia", "Luke", "Luke")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                            .containsOnly("luke", "YOda", "LeIA");
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_even_if_duplicated_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia", "LUKE")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                    .containsOnly("LUke", "LUKE", "lukE", "YOda", "Leia");
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Yoda", "Han" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsOnly(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainOnly(actual, expected, list("Han"), list("Leia"),
                                                      comparisonStrategy).create());
  }

}
