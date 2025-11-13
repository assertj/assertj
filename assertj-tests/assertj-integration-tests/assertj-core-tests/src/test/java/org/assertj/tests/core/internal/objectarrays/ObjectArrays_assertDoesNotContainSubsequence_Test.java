/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainSubsequence.shouldNotContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.internal.ObjectArrays;
import org.junit.jupiter.api.Test;

/**
 * @author Marcin Mikosik
 */
class ObjectArrays_assertDoesNotContainSubsequence_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  private void expectFailure(ObjectArrays arrays, Object[] actual, Object[] subsequence, int index) {
    // WHEN
    expectAssertionError(() -> arrays.assertDoesNotContainSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldNotContainSubsequence(actual, subsequence, arrays.getComparisonStrategy(), index));
  }

  @Test
  void should_fail_if_actual_contains_sequence() {
    expectFailure(arrays, actual, array("Yoda", "Leia"), 0);
  }

  @Test
  void should_fail_if_actual_and_sequence_are_equal() {
    expectFailure(arrays, actual, array("Yoda", "Luke", "Leia", "Obi-Wan"), 0);
  }

  @Test
  void should_fail_if_actual_contains_full_sequence_even_if_partial_sequence_is_found_before() {
    // note that actual starts with {"Yoda", "Luke"} a partial sequence of {"Yoda", "Luke", "Obi-Wan"}
    expectFailure(arrays, array("Yoda", "Luke", "Leia", "Yoda", "Luke", "Obi-Wan"), array("Yoda", "Luke", "Obi-Wan"), 0);
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertDoesNotContainSubsequence(INFO, new String[0], emptyArray());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertDoesNotContainSubsequence(INFO, null, array("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertDoesNotContainSubsequence(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertDoesNotContainSubsequence(INFO, actual, emptyArray()));
  }

  @Test
  void should_pass_if_subsequence_is_bigger_than_actual() {
    arrays.assertDoesNotContainSubsequence(INFO, actual,
                                           new Object[] { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" });
  }

  @Test
  void should_pass_if_actual_does_not_contain_whole_subsequence() {
    arrays.assertDoesNotContainSubsequence(INFO, actual, new Object[] { "Han", "C-3PO" });
  }

  @Test
  void should_pass_if_actual_contains_first_elements_of_subsequence() {
    arrays.assertDoesNotContainSubsequence(INFO, actual, new Object[] { "Leia", "Obi-Wan", "Han" });
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(INFO,
                                                                                                                         null,
                                                                                                                         array("YOda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(INFO,
                                                                                                                         actual,
                                                                                                                         null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(INFO, actual,
                                                                                                                emptyArray()));
  }

  @Test
  void should_pass_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(INFO, actual, new Object[] { "LUKE", "LeiA", "Obi-Wan",
        "Han", "C-3PO", "R2-D2", "Anakin" });
  }

  @Test
  void should_pass_if_actual_does_not_contain_whole_subsequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(INFO, actual, new Object[] { "Han", "C-3PO" });
  }

  @Test
  void should_pass_if_actual_contains_first_elements_of_subsequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(INFO, actual, new Object[] { "LeiA", "Obi-Wan", "Han" });
  }

  @Test
  void should_fail_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    expectFailure(arraysWithCustomComparisonStrategy, actual, array("LUKE", "LeiA"), 1);
  }

  @Test
  void should_fail_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    expectFailure(arraysWithCustomComparisonStrategy, actual, array("YOda", "LUKE", "LeiA", "Obi-WAn"), 0);
  }
}
