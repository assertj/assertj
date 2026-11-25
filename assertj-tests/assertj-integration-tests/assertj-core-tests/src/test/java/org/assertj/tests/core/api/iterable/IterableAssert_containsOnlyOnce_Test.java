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
import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.Locale;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author William Delanoue
class IterableAssert_containsOnlyOnce_Test {

  @Test
  void should_pass_if_actual_contains_given_values_only_once() {
    assertThat(list("Luke", "Yoda", "Leia")).containsOnlyOnce("Luke", "Yoda", "Leia");
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_even_if_actual_type_is_not_comparable() {
    // GIVEN
    // Locale class does not implement Comparable
    List<Locale> actual = list(Locale.ROOT, Locale.US, Locale.US);
    // WHEN/THEN
    then(actual).containsOnlyOnce(Locale.ROOT);
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_with_null_element() {
    assertThat(list("Luke", "Yoda", "Leia", null)).containsOnlyOnce("Luke", null, "Yoda", "Leia", null);
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_in_different_order() {
    assertThat(list("Luke", "Yoda", "Leia")).containsOnlyOnce("Leia", "Yoda", "Luke");
  }

  @Test
  void should_fail_if_actual_contains_given_values_more_than_once() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia", "Luke", "Luke", null, null);
    String[] expected = { "Luke", "Luke", "Yoda", "Han", null };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyOnce(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"),
                                                           newLinkedHashSet("Luke", null)).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_null_value() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia", "Luke", "Luke");
    String[] expected = { null };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyOnce(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((String) null),
                                                           newLinkedHashSet()).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_even_if_duplicated() {
    assertThat(list("Luke", "Yoda", "Leia")).containsOnlyOnce("Luke", "Luke", "Luke", "Yoda", "Leia");
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsOnlyOnce();
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyOnce());
    // THEN
    then(assertionError).hasMessage(actualIsNotEmpty(actual).create());
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(List.<String> of()).containsOnlyOnce(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyOnce("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_only_once() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyOnce(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"),
                                                           newLinkedHashSet()).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsOnlyOnce("LUKE", "YODA", "Leia");
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_in_different_order_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsOnlyOnce("LEIA", "yoda", "LukE");
  }

  @Test
  void should_fail_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia", "Luke", "Luke");
    String[] expected = { "luke", "YOda", "LeIA" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsOnlyOnce(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet(), newLinkedHashSet("luke"),
                                                           comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_given_values_more_than_once_even_if_duplicated_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia", "LUKE");
    String[] expected = { "LUke", "LUke", "lukE", "YOda", "Leia", "Han" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsOnlyOnce(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"),
                                                           newLinkedHashSet("LUke", "lukE"), comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    String[] expected = { "Luke", "Yoda", "Han" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsOnlyOnce(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"), newLinkedHashSet(),
                                                           comparisonStrategy).create());
  }

}
