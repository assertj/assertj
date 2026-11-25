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
import static org.assertj.core.error.ShouldNotContainSubsequence.shouldNotContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.emptySubsequence;
import static org.assertj.core.internal.ErrorMessages.nullSubsequence;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.Set;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Chris Arnott
class IterableAssert_doesNotContainSubsequence_List_Test {

  private static final List<String> ACTUAL = List.of("Yoda", "Luke", "Leia", "Obi-Wan");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainSubsequence(List.of("Yoda")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    // GIVEN
    List<String> subsequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(nullSubsequence());
  }

  @Test
  void should_throw_error_if_subsequence_is_empty() {
    // GIVEN
    List<String> subsequence = List.of();
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(exception).hasMessage(emptySubsequence());
  }

  @Test
  void should_pass_if_actual_does_not_contain_whole_subsequence() {
    assertThat(ACTUAL).doesNotContainSubsequence(List.of("Han", "C-3PO"));
  }

  @Test
  void should_pass_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence() {
    assertThat(ACTUAL).doesNotContainSubsequence(List.of("Luke", "Leia", "Han"));
  }

  @Test
  void should_fail_if_actual_contains_subsequence_with_elements_between() {
    // GIVEN
    List<String> subsequence = List.of("Yoda", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, subsequence.toArray(), 0).create());
  }

  @Test
  void should_fail_if_actual_contains_subsequence_given_as_a_set() {
    // GIVEN
    Set<String> subsequence = newLinkedHashSet("Luke", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, subsequence.toArray(), 1).create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_whole_subsequence_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                      .doesNotContainSubsequence(List.of("Luke", "LEIA", "Han"));
  }

  @Test
  void should_fail_if_actual_contains_subsequence_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> subsequence = List.of("yODa", "leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .doesNotContainSubsequence(subsequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSubsequence(ACTUAL, subsequence.toArray(),
                                                                new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER),
                                                                0).create());
  }

}
