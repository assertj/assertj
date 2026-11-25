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
import static org.assertj.core.error.ShouldContainSequence.shouldContainSequence;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.nullSequence;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Chris Arnott
class IterableAssert_containsSequence_List_Test {

  private final List<String> actual = list("Yoda", "Luke", "Leia", "Obi-Wan");

  @Test
  void should_throw_error_if_sequence_is_null() {
    // GIVEN
    List<String> sequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).containsSequence(sequence));
    // THEN
    then(exception).hasMessage(nullSequence());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSequence(list("Yoda")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsSequence(List.of());
  }

  @Test
  void should_fail_if_sequence_is_empty_and_actual_is_not() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSequence(List.of()));
    // THEN
    then(assertionError).hasMessage(actualIsNotEmpty(actual).create());
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    assertThat(actual).containsSequence(list("Luke", "Leia"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_sequence() {
    // GIVEN
    List<String> sequence = list("Luke", "Leia", "Han");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(actual, sequence.toArray()).create());
  }

  @Test
  void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER).containsSequence(list("LUKe", "leia"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> sequence = list("Luke", "Leia", "Han");
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(actual, sequence.toArray(), comparisonStrategy).create());
  }

}
