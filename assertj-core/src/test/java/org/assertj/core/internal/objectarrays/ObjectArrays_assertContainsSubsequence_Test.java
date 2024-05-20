/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ActualIsNotEmpty.actualIsNotEmpty;
import static org.assertj.core.error.ShouldContainSubsequence.actualDoesNotHaveEnoughElementsToContainSubsequence;
import static org.assertj.core.error.ShouldContainSubsequence.shouldContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertContainsSubsequence(AssertionInfo, Object[], Object[])}</code>.
 * 
 * @author Marcin Mikosik
 */
class ObjectArrays_assertContainsSubsequence_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_pass_if_actual_contains_subsequence_with_elements_between() {
    arrays.assertContainsSubsequence(INFO, actual, array("Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_subsequence_without_elements_between() {
    arrays.assertContainsSubsequence(someInfo(), actual, array("Luke", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_subsequence_are_equal() {
    arrays.assertContainsSubsequence(INFO, actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_contains_full_subsequence_even_if_partial_subsequence_is_found_before() {
    // GIVEN
    actual = array("Yoda", "Luke", "Leia", "Yoda", "Luke", "Obi-Wan");
    // WHEN/THEN
    // note that actual starts with {"Yoda", "Luke"} a partial sequence of {"Yoda", "Luke", "Obi-Wan"}
    arrays.assertContainsSubsequence(INFO, actual, array("Yoda", "Luke", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    // GIVEN
    actual = new String[0];
    // WHEN/THEN
    arrays.assertContainsSubsequence(INFO, actual, emptyArray());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertContainsSubsequence(INFO, actual, array("Yoda")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    // GIVEN
    Object[] subsequence = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> arrays.assertContainsSubsequence(INFO, actual, subsequence),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    // GIVEN
    Object[] subsequence = {};
    // WHEN
    expectAssertionError(() -> arrays.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, actualIsNotEmpty(actual));
  }

  @Test
  void should_fail_if_subsequence_is_bigger_than_actual() {
    // GIVEN
    Object[] subsequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, actualDoesNotHaveEnoughElementsToContainSubsequence(actual, subsequence));
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence() {
    // GIVEN
    Object[] subsequence = { "Luke", "Leia", "Han" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, 2, StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_subsequence() {
    // GIVEN
    Object[] subsequence = { "Luke", "C-3PO" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, 1, StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_actual_does_not_have_enough_elements_left_to_contain_subsequence_elements_still_to_be_matched() {
    // GIVEN
    actual = array("Leia", "Luke", "Yoda", "Obi-Wan", "Anakin");
    Object[] subsequence = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, 2, StandardComparisonStrategy.instance()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_subsequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsSubsequence(INFO, actual, array("LUKE", "LeiA"));
  }

  @Test
  void should_pass_if_actual_and_subsequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsSubsequence(INFO, actual, array("YOda", "LUKE", "LeiA", "Obi-WAn"));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsSubsequence(INFO,
                                                                                                                            actual,
                                                                                                                            array("Yoda")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Object[] subsequence = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> arraysWithCustomComparisonStrategy.assertContainsSubsequence(INFO,
                                                                                                                       actual,
                                                                                                                       subsequence),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Object[] subsequence = {};
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, actualIsNotEmpty(actual));
  }

  @Test
  void should_fail_if_subsequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] subsequence = { "LUKE", "LeiA", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, actualDoesNotHaveEnoughElementsToContainSubsequence(actual, subsequence));
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_subsequence_elements_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] subsequence = { "Han", "C-3PO" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, 0, caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_subsequence_according_to_custom_comparison_strategy() {
    // GIVEN
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
    Object[] subsequence = { "LeiA", "Obi-Wan", "Han" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, 2, caseInsensitiveStringComparisonStrategy));
  }
}
