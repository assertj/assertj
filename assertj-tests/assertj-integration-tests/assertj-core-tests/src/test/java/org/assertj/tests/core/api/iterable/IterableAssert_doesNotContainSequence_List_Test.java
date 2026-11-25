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
class IterableAssert_doesNotContainSequence_List_Test {

  private static final List<String> ACTUAL = List.of("Yoda", "Luke", "Leia", "Obi-Wan");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainSequence(List.of("Yoda")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    // GIVEN
    List<String> sequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(exception).hasMessage(nullSequence());
  }

  @Test
  void should_throw_error_if_sequence_is_empty() {
    // GIVEN
    List<String> sequence = List.of();
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(exception).hasMessage(emptySequence());
  }

  @Test
  void should_pass_if_actual_does_not_contain_the_whole_sequence() {
    assertThat(ACTUAL).doesNotContainSequence(List.of("Han", "C-3PO"));
  }

  @Test
  void should_pass_if_actual_contains_the_first_elements_of_sequence_but_not_the_whole_sequence() {
    assertThat(ACTUAL).doesNotContainSequence(List.of("Luke", "Leia", "Han"));
  }

  @Test
  void should_fail_if_actual_contains_sequence() {
    // GIVEN
    List<String> sequence = List.of("Luke", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(ACTUAL, sequence.toArray(), 1).create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER).doesNotContainSequence(List.of("luke", "LEIA", "Han"));
  }

  @Test
  void should_fail_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> sequence = List.of("LUKe", "leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(ACTUAL, sequence.toArray(), 1,
                                                             new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

}
