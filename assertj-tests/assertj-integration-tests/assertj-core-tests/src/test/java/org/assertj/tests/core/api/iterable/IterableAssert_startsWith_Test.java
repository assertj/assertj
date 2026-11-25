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
import static org.assertj.core.error.ActualIsNotEmpty.actualIsNotEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Iterator;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Joel Costigliola
class IterableAssert_startsWith_Test {

  private static final List<String> ACTUAL = List.of("Yoda", "Luke", "Leia", "Obi-Wan");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).startsWith("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    // GIVEN
    String[] sequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).startsWith(sequence));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_sequence_are_empty() {
    assertThat(List.of()).startsWith();
  }

  @Test
  void should_fail_if_sequence_to_look_for_is_empty_and_actual_is_not() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).startsWith());
    // THEN
    then(assertionError).hasMessage(actualIsNotEmpty(ACTUAL).create());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    // GIVEN
    String[] sequence = { "Yoda", "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).startsWith(sequence));
    // THEN
    then(assertionError).hasMessage(shouldStartWith(ACTUAL, sequence).create());
  }

  @Test
  void should_fail_if_actual_does_not_start_with_sequence() {
    // GIVEN
    String[] sequence = { "Han", "C-3PO" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).startsWith(sequence));
    // THEN
    then(assertionError).hasMessage(shouldStartWith(ACTUAL, sequence).create());
  }

  @Test
  void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    // GIVEN
    String[] sequence = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).startsWith(sequence));
    // THEN
    then(assertionError).hasMessage(shouldStartWith(ACTUAL, sequence).create());
  }

  @Test
  void should_pass_if_actual_starts_with_sequence() {
    assertThat(ACTUAL).startsWith("Yoda", "Luke", "Leia");
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal() {
    assertThat(ACTUAL).startsWith("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_pass_if_infinite_iterable_starts_with_given_sequence() {
    // GIVEN
    Iterable<Integer> actual = infiniteListOfNumbers();
    // WHEN/THEN
    then(actual).startsWith(1, 2, 3, 4, 5);
  }

  @Test
  void should_fail_if_actual_does_not_start_with_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] sequence = { "Han", "C-3PO" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .startsWith(sequence));
    // THEN
    then(assertionError).hasMessage(shouldStartWith(ACTUAL, sequence,
                                                    new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

  @Test
  void should_fail_if_actual_starts_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] sequence = { "YODA", "luke", "Leia", "Obi-Wan", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .startsWith(sequence));
    // THEN
    then(assertionError).hasMessage(shouldStartWith(ACTUAL, sequence,
                                                    new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

  @Test
  void should_pass_if_actual_starts_with_sequence_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER).startsWith("YODA", "luke", "Leia");
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER).startsWith("Yoda", "LUke", "LeIA", "oBi-WaN");
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    // WHEN
    softly.assertThat(List.of("Luke", "Yoda")).startsWith("Obiwan");
    // THEN
    List<AssertionError> errors = softly.assertionErrorsCollected();
    then(errors).singleElement(THROWABLE).hasMessageContaining("Obiwan");
  }

  private static Iterable<Integer> infiniteListOfNumbers() {
    return () -> new Iterator<>() {

      int number = 1;

      @Override
      public boolean hasNext() {
        return true;
      }

      @Override
      public Integer next() {
        return number++;
      }
    };
  }

}
