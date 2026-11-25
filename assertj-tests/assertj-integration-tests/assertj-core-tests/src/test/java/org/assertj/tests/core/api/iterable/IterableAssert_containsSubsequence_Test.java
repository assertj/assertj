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
import static org.assertj.core.error.ShouldContainSubsequence.actualDoesNotHaveEnoughElementsToContainSubsequence;
import static org.assertj.core.error.ShouldContainSubsequence.shouldContainSubsequence;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Marcin Mikosik
/// @author Harry Cummings
class IterableAssert_containsSubsequence_Test {

  private final List<String> actual = list("Yoda", "Luke", "Leia", "Obi-Wan");

  @Test
  void should_pass_if_actual_contains_subsequence_without_elements_between() {
    assertThat(actual).containsSubsequence("Luke", "Leia");
  }

  @Test
  void should_pass_if_actual_contains_subsequence_with_elements_between() {
    assertThat(actual).containsSubsequence("Yoda", "Leia");
  }

  @Test
  void should_pass_if_actual_with_duplicate_elements_contains_subsequence() {
    // GIVEN
    List<String> actual = list("Yoda", "Luke", "Yoda", "Obi-Wan");
    // WHEN/THEN
    then(actual).containsSubsequence("Yoda", "Obi-Wan")
                .containsSubsequence("Luke", "Obi-Wan")
                .containsSubsequence("Yoda", "Yoda");
  }

  @Test
  void should_pass_if_actual_and_subsequence_are_equal() {
    assertThat(actual).containsSubsequence("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_pass_if_actual_contains_full_subsequence_even_if_partial_subsequence_is_found_before() {
    // GIVEN
    // note that actual starts with {"Yoda", "Luke"} a partial sequence of {"Yoda", "Luke", "Obi-Wan"}
    List<String> actual = list("Yoda", "Luke", "Leia", "Yoda", "Luke", "Obi-Wan");
    // WHEN/THEN
    then(actual).containsSubsequence("Yoda", "Luke", "Obi-Wan");
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsSubsequence();
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    // GIVEN
    String[] subsequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).containsSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence());
    // THEN
    then(assertionError).hasMessage(actualIsNotEmpty(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_subsequence_is_bigger_than_actual() {
    // GIVEN
    String[] subsequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(actualDoesNotHaveEnoughElementsToContainSubsequence(actual, subsequence).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_subsequence() {
    // GIVEN
    String[] subsequence = { "Han", "C-3PO" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, 0,
                                                             StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence() {
    // GIVEN
    String[] subsequence = { "Luke", "Leia", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, 2,
                                                             StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_does_not_have_enough_elements_left_to_contain_subsequence_elements_still_to_be_matched() {
    // GIVEN
    List<String> actual = list("Leia", "Luke", "Yoda", "Obi-Wan", "Anakin");
    String[] subsequence = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, 2,
                                                             StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_contains_subsequence_according_to_custom_comparison_strategy() {
    assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER).containsSubsequence("yODa", "leia");
  }

  @Test
  void should_pass_if_actual_and_subsequence_are_equal_according_to_custom_comparison_strategy() {
    assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER).containsSubsequence("YODA", "luke", "lEIA", "Obi-wan");
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsSubsequence("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_subsequence_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String[] subsequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsSubsequence());
    // THEN
    then(assertionError).hasMessage(actualIsNotEmpty(actual).create());
  }

  @Test
  void should_fail_if_subsequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] subsequence = { "LUKE", "LeiA", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(actualDoesNotHaveEnoughElementsToContainSubsequence(actual, subsequence).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_subsequence_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] subsequence = { "Han", "C-3PO" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, 0, comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] subsequence = { "Luke", "Leia", "Han" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSubsequence(actual, subsequence, 2, comparisonStrategy).create());
  }

}
