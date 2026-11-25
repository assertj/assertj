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
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Joel Costigliola
/// @author William Delanoue
class IterableAssert_doesNotContainAnyElementsOf_Test {

  private static final List<String> ACTUAL = List.of("Luke", "Yoda", "Leia");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainAnyElementsOf(List.of("Yoda")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_given_iterable_is_null() {
    // GIVEN
    List<String> values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).doesNotContainAnyElementsOf(values));
    // THEN
    then(exception).hasMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_given_iterable_is_empty() {
    // GIVEN
    List<String> values = List.of();
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(ACTUAL).doesNotContainAnyElementsOf(values));
    // THEN
    then(exception).hasMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable() {
    assertThat(ACTUAL).doesNotContainAnyElementsOf(List.of("Han"));
  }

  @Test
  void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable_even_if_duplicated() {
    assertThat(ACTUAL).doesNotContainAnyElementsOf(List.of("Han", "Han", "Anakin"));
  }

  @Test
  void should_fail_if_actual_contains_one_element_of_given_iterable() {
    // GIVEN
    List<String> values = List.of("Vador", "Yoda", "Han");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainAnyElementsOf(values));
    // THEN
    then(assertionError).hasMessage(shouldNotContain(ACTUAL, values.toArray(), newLinkedHashSet("Yoda")).create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER).doesNotContainAnyElementsOf(List.of("Han"));
  }

  @Test
  void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable_even_if_duplicated_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                      .doesNotContainAnyElementsOf(List.of("Han", "Han", "Anakin"));
  }

  @Test
  void should_fail_if_actual_contains_one_element_of_given_iterable_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> values = List.of("LuKe", "YODA", "Han");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .doesNotContainAnyElementsOf(values));
    // THEN
    then(assertionError).hasMessage(shouldNotContain(ACTUAL, values.toArray(), newLinkedHashSet("LuKe", "YODA"),
                                                     new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

}
