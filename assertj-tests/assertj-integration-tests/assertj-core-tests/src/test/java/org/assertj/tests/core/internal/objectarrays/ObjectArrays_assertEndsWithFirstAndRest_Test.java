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
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.prepend;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class ObjectArrays_assertEndsWithFirstAndRest_Test extends ObjectArraysBaseTest {

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"),
                                                                            "Luke", null));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertEndsWith(INFO, null, "Luke", array("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    // GIVEN
    Object[] sequence = { "Yoda", "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    expectAssertionError(() -> arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), "Luke", sequence));
    // THEN
    verifyFailureThrownWhenSequenceNotFound("Luke", sequence);
  }

  @Test
  void should_fail_if_actual_does_not_end_with_sequence() {
    // GIVEN
    Object[] sequence = { "Han", "C-3PO" };
    // WHEN
    expectAssertionError(() -> arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), "Obi-Wan", sequence));
    // THEN
    verifyFailureThrownWhenSequenceNotFound("Obi-Wan", sequence);
  }

  @Test
  void should_fail_if_actual_ends_with_first_elements_of_sequence_only() {
    // GIVEN
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    expectAssertionError(() -> arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), "Luke", sequence));
    // THEN
    verifyFailureThrownWhenSequenceNotFound("Luke", sequence);
  }

  private void verifyFailureThrownWhenSequenceNotFound(Object first, Object[] sequence) {
    verify(failures).failure(INFO, shouldEndWith(array("Yoda", "Luke", "Leia", "Obi-Wan"), prepend(first, sequence)));
  }

  @Test
  void should_pass_if_actual_ends_with_first_then_sequence() {
    arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), "Luke", array("Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_and_first_then_sequence_are_equal() {
    arrays.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), "Yoda", array("Luke", "Leia", "Obi-Wan"));
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
    Object[] sequence = { "LUKE", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertEndsWith(INFO, actual, "Yoda", sequence));
    // THEN
    verify(failures).failure(INFO, shouldEndWith(actual, prepend("Yoda", sequence),
                                                 caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_does_not_end_with_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
    Object[] sequence = { "Han", "C-3PO" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertEndsWith(INFO, actual, "Yoda", sequence));
    // THEN
    verify(failures).failure(INFO, shouldEndWith(actual, prepend("Yoda", sequence),
                                                 caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_ends_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"),
                                                                                 "Luke", sequence));
    // THEN
    verify(failures).failure(INFO, shouldEndWith(array("Yoda", "Luke", "Leia", "Obi-Wan"), prepend("Luke", sequence),
                                                 caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_pass_if_actual_ends_with_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), "LUKE",
                                                      array("Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(INFO, array("Yoda", "Luke", "Leia", "Obi-Wan"), "Yoda",
                                                      array("LUKE", "Leia", "Obi-Wan"));
  }
}
