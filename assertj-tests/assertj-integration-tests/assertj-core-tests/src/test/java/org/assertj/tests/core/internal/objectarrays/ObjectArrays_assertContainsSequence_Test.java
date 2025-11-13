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
import static org.assertj.core.error.ShouldContainSequence.shouldContainSequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ObjectArrays_assertContainsSequence_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    arrays.assertContainsSequence(INFO, actual, array("Luke", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertContainsSequence(INFO, actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_contains_full_sequence_even_if_partial_sequence_is_found_before() {
    // note that actual starts with {"Yoda", "Luke"} a partial sequence of {"Yoda", "Luke", "Obi-Wan"}
    arrays.assertContainsSequence(INFO,
                                  array("Yoda", "Luke", "Leia", "Yoda", "Luke", "Obi-Wan"),
                                  array("Yoda", "Luke", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsSequence(INFO, new String[0], emptyArray());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertContainsSequence(INFO, null, array("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsSequence(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertContainsSequence(INFO, actual, emptyArray()));
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    // GIVEN
    Object[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsSequence(INFO, actual, sequence));
    // THEN
    verifyFailureThrownWhenSequenceNotFound(INFO, sequence);
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_sequence() {
    // GIVEN
    Object[] sequence = { "Han", "C-3PO" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsSequence(INFO, actual, sequence));
    // THEN
    verifyFailureThrownWhenSequenceNotFound(INFO, sequence);
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_sequence() {
    // GIVEN
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsSequence(INFO, actual, sequence));
    // THEN
    verifyFailureThrownWhenSequenceNotFound(INFO, sequence);
  }

  private void verifyFailureThrownWhenSequenceNotFound(AssertionInfo INFO, Object[] sequence) {
    verify(failures).failure(INFO, shouldContainSequence(actual, sequence));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsSequence(INFO,
                                                                                                       null,
                                                                                                       array("YOda")))
                                                                                                                      .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsSequence(INFO,
                                                                                                                actual,
                                                                                                                null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsSequence(INFO,
                                                                                                       actual,
                                                                                                       emptyArray()));
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] sequence = { "LUKE", "LeiA", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsSequence(INFO, actual, sequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSequence(actual, sequence, caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] sequence = { "Han", "C-3PO" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsSequence(INFO, actual, sequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSequence(actual, sequence, caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] sequence = { "LeiA", "Obi-Wan", "Han" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsSequence(INFO, actual, sequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSequence(actual, sequence, caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsSequence(INFO, actual, array("LUKE", "LeiA"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsSequence(INFO, actual, array("YOda", "LUKE", "LeiA", "Obi-WAn"));
  }
}
