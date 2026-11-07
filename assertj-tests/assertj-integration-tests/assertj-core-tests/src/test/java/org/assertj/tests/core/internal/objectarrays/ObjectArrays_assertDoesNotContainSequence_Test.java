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
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainSequence.shouldNotContainSequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.assertj.tests.core.testkit.TestData;
import org.junit.jupiter.api.Test;

/**
 * @author Chris Arnott
 */
class ObjectArrays_assertDoesNotContainSequence_Test {

  private static final AssertionInfo INFO = TestData.someInfo();
  private final String[] actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  private final ObjectArrays arrays = new ObjectArrays(StandardComparisonStrategy.instance());
  private final ComparatorBasedComparisonStrategy caseInsensitiveStringComparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
  private final ObjectArrays arraysWithCustomComparisonStrategy = new ObjectArrays(caseInsensitiveStringComparisonStrategy);

  @Test
  void should_fail_if_actual_contains_sequence() {
    // GIVEN
    Object[] sequence = array("Luke", "Leia");
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotContainSequence(INFO, actual, sequence));
    // THEN
    then(error).hasMessage(shouldNotContainSequence(actual, sequence, 1).create());
  }

  @Test
  void should_fail_if_actual_and_sequence_are_equal() {
    // GIVEN
    Object[] sequence = array("Yoda", "Luke", "Leia", "Obi-Wan");
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotContainSequence(INFO, actual, sequence));
    // THEN
    then(error).hasMessage(shouldNotContainSequence(actual, sequence, 0).create());
  }

  @Test
  void should_fail_if_actual_contains_full_sequence_even_if_partial_sequence_is_found_before() {
    // GIVEN
    var actual = array("Yoda", "Luke", "Leia", "Yoda", "Luke", "Obi-Wan");
    // note that actual starts with {"Yoda", "Luke"} a partial sequence of {"Yoda", "Luke", "Obi-Wan"}
    Object[] sequence = array("Yoda", "Luke", "Obi-Wan");
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotContainSequence(INFO, actual, sequence));
    // THEN
    then(error).hasMessage(shouldNotContainSequence(actual, sequence, 3).create());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertDoesNotContainSequence(INFO, new String[0], emptyArray());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotContainSequence(INFO, null, array("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertDoesNotContainSequence(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertDoesNotContainSequence(INFO, actual, emptyArray()));
  }

  @Test
  void should_pass_if_sequence_is_bigger_than_actual() {
    arrays.assertDoesNotContainSequence(INFO, actual,
                                        array("Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin"));
  }

  @Test
  void should_pass_if_actual_does_not_contain_whole_sequence() {
    arrays.assertDoesNotContainSequence(INFO, actual, array("Han", "C-3PO"));
  }

  @Test
  void should_pass_if_actual_contains_first_elements_of_sequence() {
    arrays.assertDoesNotContainSequence(INFO, actual, array("Leia", "Obi-Wan", "Han"));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(INFO, null,
                                                                                                           array("YOda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(INFO,
                                                                                                                      actual,
                                                                                                                      null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(INFO,
                                                                                                             actual,
                                                                                                             emptyArray()));
  }

  @Test
  void should_pass_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(INFO, actual, array("LUKE", "LeiA", "Obi-Wan",
                                                                                        "Han", "C-3PO", "R2-D2",
                                                                                        "Anakin"));
  }

  @Test
  void should_pass_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(INFO, actual, array("Han", "C-3PO"));
  }

  @Test
  void should_pass_if_actual_contains_first_elements_of_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(INFO, actual, array("LeiA", "Obi-Wan", "Han"));
  }

  @Test
  void should_fail_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] sequence = array("LUKE", "LeiA");
    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(INFO, actual,
                                                                                                           sequence));
    // THEN
    then(error).hasMessage(shouldNotContainSequence(actual, sequence, 1, caseInsensitiveStringComparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] sequence = array("YOda", "LUKE", "LeiA", "Obi-WAn");
    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(INFO, actual,
                                                                                                           sequence));
    // THEN
    then(error).hasMessage(shouldNotContainSequence(actual, sequence, 0, caseInsensitiveStringComparisonStrategy).create());
  }
}
