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
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.iterableToLookForIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.testkit.SinglyIterableFactory.createSinglyIterable;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Joel Costigliola
class IterableAssert_containsAll_Test {

  @Test
  void should_pass_if_actual_contains_all_iterable_values() {
    assertThat(list("Luke", "Yoda", "Leia")).containsAll(list("Luke"));
    // order does not matter
    assertThat(list("Luke", "Yoda", "Leia")).containsAll(list("Leia", "Yoda"));
  }

  @Test
  void should_pass_if_actual_contains_all_iterable_values_more_than_once() {
    assertThat(list("Luke", "Yoda", "Leia", "Luke", "Luke")).containsAll(list("Luke"));
  }

  @Test
  void should_pass_if_actual_contains_all_iterable_values_even_if_duplicated() {
    assertThat(list("Luke", "Yoda", "Leia")).containsAll(list("Luke", "Luke"));
  }

  @Test
  void should_pass_if_nonrestartable_actual_contains_given_values() {
    // GIVEN
    Iterable<String> actual = createSinglyIterable(list("Luke", "Yoda", "Leia"));
    // WHEN/THEN
    then(actual).containsAll(list("Luke", "Luke"));
  }

  @Test
  void should_throw_error_if_iterable_of_values_to_look_for_is_null() {
    // GIVEN
    List<String> values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(list("Luke", "Yoda", "Leia")).containsAll(values));
    // THEN
    then(exception).hasMessage(iterableToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsAll(list("Yoda")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_values() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    List<String> expected = list("Han", "Luke");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsAll(expected));
    // THEN
    then(assertionError).hasMessage(shouldContain(actual, expected.toArray(), newLinkedHashSet("Han")).create());
  }

  @Test
  void should_pass_if_actual_contains_all_iterable_values_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER).containsAll(list("LUKE"));
  }

  @Test
  void should_pass_if_actual_contains_all_iterable_values_in_different_order_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER).containsAll(list("LEIA", "yODa"));
  }

  @Test
  void should_pass_if_actual_contains_all_all_iterable_values_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER).containsAll(list("luke", "YODA"));
  }

  @Test
  void should_pass_if_actual_contains_all_iterable_values_more_than_once_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia", "Luke", "Luke")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                            .containsAll(list("LUke"));
  }

  @Test
  void should_pass_if_actual_contains_all_iterable_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER).containsAll(list("LUke", "LuKe"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    List<String> expected = list("Han", "LUKE");
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsAll(expected));
    // THEN
    then(assertionError).hasMessage(shouldContain(actual, expected.toArray(), newLinkedHashSet("Han"),
                                                  comparisonStrategy).create());
  }

}
