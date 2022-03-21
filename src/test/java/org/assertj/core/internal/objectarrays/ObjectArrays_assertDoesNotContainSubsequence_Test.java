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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotContainSubsequence.shouldNotContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertDoesNotContainSubsequence(AssertionInfo, Object[], Object[])} </code>.
 * 
 * @author Marcin Mikosik
 */
class ObjectArrays_assertDoesNotContainSubsequence_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  private void expectFailure(ObjectArrays arrays, Object[] actual, Object[] subsequence, int index) {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> arrays.assertDoesNotContainSubsequence(info, actual, subsequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotContainSubsequence(actual, subsequence, arrays.getComparisonStrategy(), index));
  }

  @Test
  void should_fail_if_actual_contains_sequence() {
    Object[] subsequence = array("Yoda", "Leia");
    expectFailure(arrays, actual, subsequence, 0);
  }

  @Test
  void should_fail_if_actual_and_sequence_are_equal() {
    Object[] subsequence = array("Yoda", "Luke", "Leia", "Obi-Wan");
    expectFailure(arrays, actual, subsequence, 0);
  }

  @Test
  void should_fail_if_actual_contains_full_sequence_even_if_partial_sequence_is_found_before() {
    // note that actual starts with {"Yoda", "Luke"} a partial sequence of {"Yoda", "Luke", "Obi-Wan"}
    actual = array("Yoda", "Luke", "Leia", "Yoda", "Luke", "Obi-Wan");
    Object[] subsequence = array("Yoda", "Luke", "Obi-Wan");
    expectFailure(arrays, actual, subsequence, 0);
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual = new String[0];
    arrays.assertDoesNotContainSubsequence(someInfo(), actual, emptyArray());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertDoesNotContainSubsequence(someInfo(), null,
                                                                                                            array("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertDoesNotContainSubsequence(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertDoesNotContainSubsequence(someInfo(), actual,
                                                                                                            emptyArray()));
  }

  @Test
  void should_pass_if_subsequence_is_bigger_than_actual() {
    Object[] subsequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    arrays.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_pass_if_actual_does_not_contain_whole_subsequence() {
    Object[] subsequence = { "Han", "C-3PO" };
    arrays.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_pass_if_actual_contains_first_elements_of_subsequence() {
    Object[] subsequence = { "Leia", "Obi-Wan", "Han" };
    arrays.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(someInfo(),
                                                                                                                                        null,
                                                                                                                                        array("YOda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(someInfo(),
                                                                                                                         actual,
                                                                                                                         null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(someInfo(),
                                                                                                                                        actual,
                                                                                                                                        emptyArray()));
  }

  @Test
  void should_pass_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    Object[] subsequence = { "LUKE", "LeiA", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  void should_pass_if_actual_does_not_contain_whole_subsequence_according_to_custom_comparison_strategy() {
    Object[] sequence = { "Han", "C-3PO" };
    arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(someInfo(), actual, sequence);
  }

  @Test
  void should_pass_if_actual_contains_first_elements_of_subsequence_according_to_custom_comparison_strategy() {
    Object[] sequence = { "LeiA", "Obi-Wan", "Han" };
    arraysWithCustomComparisonStrategy.assertDoesNotContainSubsequence(someInfo(), actual, sequence);
  }

  @Test
  void should_fail_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    Object[] subsequence = array("LUKE", "LeiA");
    expectFailure(arraysWithCustomComparisonStrategy, actual, subsequence, 1);
  }

  @Test
  void should_fail_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    Object[] subsequence = array("YOda", "LUKE", "LeiA", "Obi-WAn");
    expectFailure(arraysWithCustomComparisonStrategy, actual, subsequence, 0);
  }
}
