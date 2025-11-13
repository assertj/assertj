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
import static org.assertj.core.error.ActualIsNotEmpty.actualIsNotEmpty;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ObjectArrays_assertStartsWith_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertStartsWith(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertStartsWith(INFO, new String[0], emptyArray());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertStartsWith(INFO, actual, emptyArray()));
    // THEN
    then(error).hasMessage(actualIsNotEmpty(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertStartsWith(INFO, null, array("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    // GIVEN
    Object[] sequence = { "Yoda", "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertStartsWith(INFO, actual, sequence));
    // THEN
    then(error).hasMessage(shouldStartWith(actual, sequence).create());
  }

  @Test
  void should_fail_if_actual_does_not_start_with_sequence() {
    // GIVEN
    Object[] sequence = { "Han", "C-3PO" };
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertStartsWith(INFO, actual, sequence));
    // THEN
    then(error).hasMessage(shouldStartWith(actual, sequence).create());
  }

  @Test
  void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    // GIVEN
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertStartsWith(INFO, actual, sequence));
    // THEN
    then(error).hasMessage(shouldStartWith(actual, sequence).create());
  }

  @Test
  void should_pass_if_actual_starts_with_sequence() {
    arrays.assertStartsWith(INFO, actual, array("Yoda", "Luke", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertStartsWith(INFO, actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertStartsWith(INFO,
                                                                                                          actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> arraysWithCustomComparisonStrategy.assertStartsWith(INFO,
                                                                                                 actual,
                                                                                                 emptyArray()));
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] sequence = { "Yoda", "LUKE", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertStartsWith(INFO, actual,
                                                                                                          sequence));
    // THEN
    then(error).hasMessage(shouldStartWith(actual, sequence, caseInsensitiveStringComparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_does_not_start_with_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] sequence = { "Han", "C-3PO" };
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertStartsWith(INFO, actual,
                                                                                                          sequence));
    // THEN
    then(error).hasMessage(shouldStartWith(actual, sequence, caseInsensitiveStringComparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_starts_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] sequence = { "LEia", "Obi-Wan", "Han" };
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertStartsWith(INFO, actual,
                                                                                                          sequence));
    // THEN
    then(error).hasMessage(shouldStartWith(actual, sequence, caseInsensitiveStringComparisonStrategy).create());
  }

  @Test
  void should_pass_if_actual_starts_with_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertStartsWith(INFO, actual, array("Yoda", "LUKE", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertStartsWith(INFO, actual, array("Yoda", "LUKE", "Leia", "Obi-Wan"));
  }
}
