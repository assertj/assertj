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
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Arrays.prepend;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Joel Costigliola
/// @author Florent Biville
class IterableAssert_endsWith_Test {

  private static final List<String> ACTUAL = List.of("Yoda", "Luke", "Leia", "Obi-Wan");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).endsWith("Luke", "Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_rest_of_sequence_is_null() {
    // GIVEN
    String[] rest = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).endsWith("Luke", rest));
    // THEN
    then(exception).isNotNull();
  }

  @Test
  void should_pass_if_actual_ends_with_first_only() {
    assertThat(ACTUAL).endsWith("Obi-Wan");
  }

  @Test
  void should_pass_if_actual_ends_with_first_then_sequence() {
    assertThat(ACTUAL).endsWith("Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_pass_if_actual_and_first_then_sequence_are_equal() {
    assertThat(ACTUAL).endsWith("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    // GIVEN
    String[] rest = { "Yoda", "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).endsWith("Luke", rest));
    // THEN
    then(assertionError).hasMessage(shouldEndWith(ACTUAL, prepend("Luke", rest)).create());
  }

  @Test
  void should_fail_if_actual_does_not_end_with_sequence() {
    // GIVEN
    String[] rest = { "Han", "C-3PO" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).endsWith("Obi-Wan", rest));
    // THEN
    then(assertionError).hasMessage(shouldEndWith(ACTUAL, prepend("Obi-Wan", rest)).create());
  }

  @Test
  void should_fail_if_actual_ends_with_first_elements_of_sequence_only() {
    // GIVEN
    String[] rest = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).endsWith("Luke", rest));
    // THEN
    then(assertionError).hasMessage(shouldEndWith(ACTUAL, prepend("Luke", rest)).create());
  }

  @Test
  void should_pass_if_actual_ends_with_sequence_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER).endsWith("LUKE", "Leia", "Obi-Wan");
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER).endsWith("Yoda", "LUKE", "Leia", "Obi-Wan");
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] rest = { "LUKE", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .endsWith("Yoda", rest));
    // THEN
    then(assertionError).hasMessage(shouldEndWith(ACTUAL, prepend("Yoda", rest),
                                                  new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

  @Test
  void should_fail_if_actual_does_not_end_with_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] rest = { "Han", "C-3PO" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .endsWith("Yoda", rest));
    // THEN
    then(assertionError).hasMessage(shouldEndWith(ACTUAL, prepend("Yoda", rest),
                                                  new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

  @Test
  void should_fail_if_actual_ends_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] rest = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .endsWith("Luke", rest));
    // THEN
    then(assertionError).hasMessage(shouldEndWith(ACTUAL, prepend("Luke", rest),
                                                  new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

}
