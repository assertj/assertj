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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ActualIsNotEmpty.actualIsNotEmpty;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.testkit.Name.name;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.tests.core.testkit.Name;
import org.junit.jupiter.api.Test;

/// @author Marko Bekhta
class IterableAssert_containsAnyOf_Test {

  @Test
  void should_pass_if_actual_contains_given_values() {
    assertThat(list("Luke", "Yoda", "Leia")).containsAnyOf("Luke");
  }

  @Test
  void should_pass_with_non_comparable_values() {
    // GIVEN
    Iterable<Name> names = list(name("John", "Doe"));
    // WHEN/THEN
    then(names).containsAnyOf(name("John", "Doe"), name("Jane", "Doe"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_in_different_order() {
    assertThat(list("Luke", "Yoda", "Leia")).containsAnyOf("Leia", "Yoda");
  }

  @Test
  void should_pass_if_actual_contains_all_given_values() {
    assertThat(list("Luke", "Yoda", "Leia")).containsAnyOf("Luke", "Yoda", "Leia");
  }

  @Test
  void should_pass_if_actual_contains_given_values_more_than_once() {
    assertThat(list("Luke", "Yoda", "Leia", "Luke", "Luke")).containsAnyOf("Luke");
  }

  @Test
  void should_pass_if_actual_contains_just_one_of_given_values() {
    assertThat(list("Luke", "Yoda", "Leia")).containsAnyOf("Luke", "John", "Tom");
  }

  @Test
  void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    assertThat(list("Luke", "Yoda", "Leia")).containsAnyOf("Luke", "Luke");
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsAnyOf();
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsAnyOf());
    // THEN
    then(assertionError).hasMessage(actualIsNotEmpty(actual).create());
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(list("Luke", "Yoda", "Leia")).containsAnyOf(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsAnyOf("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_of_the_given_values() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Han", "John" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsAnyOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainAnyOf(actual, expected, StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER).containsAnyOf("LUKE");
  }

  @Test
  void should_pass_if_actual_contains_given_values_in_different_order_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER).containsAnyOf("LEIA", "yODa");
  }

  @Test
  void should_pass_if_actual_contains_all_given_values_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsAnyOf("luke", "YODA", "leia");
  }

  @Test
  void should_pass_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia", "Luke", "Luke")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                            .containsAnyOf("LUke");
  }

  @Test
  void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER).containsAnyOf("LUke", "LuKe");
  }

  @Test
  void should_pass_if_actual_contains_just_one_of_given_values_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsAnyOf("LuKe", "JoHn", "ToM");
  }

  @Test
  void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Han", "John" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsAnyOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainAnyOf(actual, expected, comparisonStrategy).create());
  }

}
