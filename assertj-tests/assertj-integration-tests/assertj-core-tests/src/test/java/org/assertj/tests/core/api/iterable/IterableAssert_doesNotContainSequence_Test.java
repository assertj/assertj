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
import static org.assertj.core.error.ShouldNotContainSequence.shouldNotContainSequence;
import static org.assertj.core.internal.ErrorMessages.emptySequence;
import static org.assertj.core.internal.ErrorMessages.nullSequence;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Chris Arnott
class IterableAssert_doesNotContainSequence_Test {

  private static final List<String> ACTUAL = List.of("Yoda", "Luke", "Leia", "Obi-Wan");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainSequence("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    // GIVEN
    String[] sequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(exception).hasMessage(nullSequence());
  }

  @Test
  void should_throw_error_if_sequence_is_empty() {
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(ACTUAL).doesNotContainSequence());
    // THEN
    then(exception).hasMessage(emptySequence());
  }

  @Test
  void should_pass_if_sequence_is_bigger_than_actual() {
    assertThat(ACTUAL).doesNotContainSequence("Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin");
  }

  @Test
  void should_pass_if_actual_does_not_contain_the_whole_sequence() {
    assertThat(ACTUAL).doesNotContainSequence("Han", "C-3PO");
  }

  @Test
  void should_pass_if_actual_contains_the_first_elements_of_sequence_but_not_the_whole_sequence() {
    assertThat(ACTUAL).doesNotContainSequence("Luke", "Leia", "Han");
  }

  @Test
  void should_fail_if_actual_contains_sequence() {
    // GIVEN
    String[] sequence = { "Luke", "Leia" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(ACTUAL, sequence, 1).create());
  }

  @Test
  void should_fail_if_actual_and_sequence_are_equal() {
    // GIVEN
    String[] sequence = { "Yoda", "Luke", "Leia", "Obi-Wan" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(ACTUAL, sequence, 0).create());
  }

  @Test
  void should_fail_if_actual_contains_both_partial_and_complete_sequence() {
    // GIVEN
    List<String> actual = List.of("Yoda", "Luke", "Yoda", "Obi-Wan");
    String[] sequence = { "Yoda", "Obi-Wan" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(actual, sequence, 2).create());
  }

  @Test
  void should_fail_if_actual_contains_sequence_that_specifies_multiple_times_the_same_value() {
    // GIVEN
    List<String> actual = List.of("a", "-", "b", "-", "c");
    String[] sequence = { "a", "-", "b", "-", "c" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(actual, sequence, 0).create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER).doesNotContainSequence("Han", "C-3PO");
  }

  @Test
  void should_pass_if_actual_contains_first_elements_of_sequence_but_not_whole_sequence_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER).doesNotContainSequence("Luke", "Leia", "Han");
  }

  @Test
  void should_fail_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] sequence = { "LUKe", "leia" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(ACTUAL, sequence, 1,
                                                             new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

  @Test
  void should_fail_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] sequence = { "YODA", "luke", "lEIA", "Obi-wan" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(ACTUAL, sequence, 0,
                                                             new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

}
