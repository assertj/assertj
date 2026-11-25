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
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.testkit.SinglyIterableFactory.createSinglyIterable;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Iterables;

/// @author Alex Ruiz
/// @author Joel Costigliola
class IterableAssert_containsSequence_Test {

  private final List<String> actual = list("Yoda", "Luke", "Leia", "Obi-Wan");

  @Test
  void should_throw_error_if_sequence_is_null() {
    // GIVEN
    String[] sequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).containsSequence(sequence));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    assertThat(List.<String> of()).containsSequence();
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSequence());
    // THEN
    then(assertionError).hasMessage(actualIsNotEmpty(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSequence("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    // GIVEN
    String[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(actual, sequence).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_sequence() {
    // GIVEN
    String[] sequence = { "Han", "C-3PO" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(actual, sequence).create());
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_sequence_but_not_whole_sequence() {
    // GIVEN
    String[] sequence = { "Luke", "Leia", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(actual, sequence).create());
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    assertThat(actual).containsSequence("Luke", "Leia");
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal() {
    assertThat(actual).containsSequence("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_pass_if_actual_contains_both_partial_and_complete_sequence() {
    assertThat(list("Yoda", "Luke", "Yoda", "Obi-Wan")).containsSequence("Yoda", "Obi-Wan");
  }

  @Test
  void should_pass_if_actual_contains_sequence_that_specifies_multiple_times_the_same_value_bug_544() {
    assertThat(list("a", "-", "b", "-", "c")).containsSequence("a", "-", "b", "-", "c");
  }

  @Test
  void should_pass_if_actual_is_an_infinite_sequence_and_contains_sequence() {
    // GIVEN
    Iterable<String> actual = Iterables.cycle("Leia", "Luke", "Yoda", "Obi-Wan");
    // WHEN/THEN
    then(actual).containsSequence("Luke", "Yoda", "Obi-Wan", "Leia")
                .containsSequence("Luke", "Yoda")
                .containsSequence("Luke", "Yoda", "Obi-Wan", "Leia", "Luke", "Yoda");
  }

  @Test
  void should_pass_if_actual_is_a_singly_traversable_sequence_and_contains_sequence() {
    // GIVEN
    Iterable<String> actual = createSinglyIterable(list("Leia", "Luke", "Yoda", "Obi-Wan"));
    // WHEN/THEN
    then(actual).containsSequence("Leia", "Luke", "Yoda", "Obi-Wan");
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] sequence = { "Han", "C-3PO" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(actual, sequence, comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_sequence_but_not_whole_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] sequence = { "Luke", "Leia", "Han" };
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldContainSequence(actual, sequence, comparisonStrategy).create());
  }

  @Test
  void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER).containsSequence("LUKe", "leia");
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER).containsSequence("YODA", "luke", "lEIA", "Obi-wan");
  }

}
