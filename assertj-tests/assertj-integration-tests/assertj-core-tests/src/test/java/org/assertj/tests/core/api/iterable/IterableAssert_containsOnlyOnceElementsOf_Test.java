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

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author CAL027
class IterableAssert_containsOnlyOnceElementsOf_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyOnceElementsOf(list("Yoda")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_iterable_of_values_to_look_for_is_null() {
    // GIVEN
    List<String> values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(List.<String> of()).containsOnlyOnceElementsOf(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsOnlyOnceElementsOf(List.of());
  }

  @Test
  void should_fail_if_iterable_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyOnceElementsOf(List.of()));
    // THEN
    then(assertionError).hasMessage(actualIsNotEmpty(actual).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once() {
    assertThat(list("Luke", "Yoda", "Leia", "Leia")).containsOnlyOnceElementsOf(list("Yoda", "Luke"));
  }

  @Test
  void should_fail_if_actual_contains_given_values_more_than_once_or_not_at_all() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia", "Luke");
    List<String> expected = list("Luke", "Yoda", "Han");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyOnceElementsOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainsOnlyOnce(actual, expected.toArray(), newLinkedHashSet("Han"),
                                                           newLinkedHashSet("Luke")).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_according_to_custom_comparison_strategy() {
    assertThat(list("Luke", "Yoda", "Leia")).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                            .containsOnlyOnceElementsOf(list("LUKE", "yoda"));
  }

  @Test
  void should_fail_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia", "LUKE");
    List<String> expected = list("luke", "YOda");
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsOnlyOnceElementsOf(expected));
    // THEN
    then(assertionError).hasMessage(shouldContainsOnlyOnce(actual, expected.toArray(), newLinkedHashSet(),
                                                           newLinkedHashSet("luke"), comparisonStrategy).create());
  }

}
