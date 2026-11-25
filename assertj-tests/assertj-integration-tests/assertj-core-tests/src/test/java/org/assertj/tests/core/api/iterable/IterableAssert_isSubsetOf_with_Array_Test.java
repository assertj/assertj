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
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

class IterableAssert_isSubsetOf_with_Array_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isSubsetOf("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_values_is_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(List.of("Yoda", "Luke")).isSubsetOf(values));
    // THEN
    then(exception).isNotNull();
  }

  @Test
  void should_pass_if_actual_is_subset_of_values() {
    assertThat(List.of("Luke", "Yoda")).isSubsetOf("Yoda", "Luke", "Chewbacca");
  }

  @Test
  void should_pass_if_actual_has_the_same_elements_as_values() {
    assertThat(List.of("Yoda", "Luke")).isSubsetOf("Luke", "Yoda");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    assertThat(List.<String> of()).isSubsetOf("Luke", "Yoda");
  }

  @Test
  void should_pass_if_actual_and_values_are_both_empty() {
    assertThat(List.<String> of()).isSubsetOf();
  }

  @Test
  void should_pass_if_both_actual_and_values_have_duplicates_but_all_elements_are_in_values() {
    assertThat(List.of("Yoda", "Yoda", "C-3PO")).isSubsetOf("Yoda", "Yoda", "C-3PO", "C-3PO");
  }

  @Test
  void should_fail_if_actual_is_not_subset_of_values() {
    // GIVEN
    List<String> actual = List.of("Yoda");
    String[] values = { "C-3PO", "Leila" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isSubsetOf(values));
    // THEN
    then(assertionError).hasMessage(shouldBeSubsetOf(actual, List.of(values), List.of("Yoda")).create());
  }

  @Test
  void should_pass_if_actual_is_subset_of_values_according_to_custom_comparison_strategy() {
    assertThat(List.of("Yoda", "Luke")).usingElementComparator(CASE_INSENSITIVE_ORDER).isSubsetOf("yoda", "lUKE");
  }

  @Test
  void should_fail_if_actual_is_not_subset_of_values_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = List.of("Yoda", "Luke");
    String[] values = { "yoda", "C-3PO" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .isSubsetOf(values));
    // THEN
    then(assertionError).hasMessage(shouldBeSubsetOf(actual, List.of(values), List.of("Luke"),
                                                     new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    // WHEN
    softly.assertThat(List.of("Luke", "Yoda")).isSubsetOf("Solo", "Luke");
    // THEN
    List<AssertionError> errors = softly.assertionErrorsCollected();
    then(errors).singleElement(THROWABLE).hasMessageContaining("to be subset of");
  }

}
