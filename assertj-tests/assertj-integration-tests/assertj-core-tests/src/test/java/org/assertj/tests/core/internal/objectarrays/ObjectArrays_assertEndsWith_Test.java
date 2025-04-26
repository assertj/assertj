/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Florent Biville
 */
class ObjectArrays_assertEndsWith_Test extends ObjectArraysBaseTest {

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertEndsWith(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertEndsWith(INFO, new String[0], emptyArray());
  }

  @Test
  void should_pass_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    arrays.assertEndsWith(INFO, array("Yoda", "Luke"), emptyArray());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertEndsWith(INFO, null, array("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    // GIVEN
    Object[] sequence = { "Yoda", "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    expectAssertionError(() -> arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), sequence));
    // THEN
    verify(failures).failure(INFO, shouldEndWith(array("Yoda", "Luke", "Leia", "Obi-Wan"), sequence));
  }

  @Test
  void should_fail_if_actual_does_not_end_with_sequence() {
    // GIVEN
    Object[] sequence = { "Han", "C-3PO" };
    // WHEN
    expectAssertionError(() -> arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), sequence));
    // THEN
    verify(failures).failure(INFO, shouldEndWith(array("Yoda", "Luke", "Leia", "Obi-Wan"), sequence));
  }

  @Test
  void should_fail_if_actual_ends_with_first_elements_of_sequence_only() {
    // GIVEN
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    expectAssertionError(() -> arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), sequence));
    // THEN
    verify(failures).failure(INFO, shouldEndWith(array("Yoda", "Luke", "Leia", "Obi-Wan"), sequence));
  }

  @Test
  void should_pass_if_actual_ends_with_sequence() {
    arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), array("Luke", "Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), emptyArray());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    // GIVEN
    var actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
    Object[] sequence = { "Yoda", "LUKE", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertEndsWith(INFO, actual, sequence));
    // THEN
    verify(failures).failure(INFO, shouldEndWith(actual, sequence, caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_does_not_end_with_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    var actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
    Object[] sequence = { "Han", "C-3PO" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertEndsWith(INFO, actual, sequence));
    // THEN
    verify(failures).failure(INFO, shouldEndWith(actual, sequence, caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_ends_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    // GIVEN
    var actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertEndsWith(INFO, actual, sequence));
    // THEN
    verify(failures).failure(INFO, shouldEndWith(actual, sequence, caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_pass_if_actual_ends_with_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(INFO,
                                                      array("Yoda", "Luke", "Leia", "Obi-Wan"),
                                                      array("LUKE", "Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(INFO,
                                                      array("Yoda", "Luke", "Leia", "Obi-Wan"),
                                                      array("Yoda", "LUKE", "Leia", "Obi-Wan"));
  }
}
