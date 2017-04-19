/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.error.ShouldContainSubsequence.shouldContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertContainsSubsequence(AssertionInfo, Object[], Object[])}</code>.
 * 
 * @author Marcin Mikosik
 */
public class ObjectArrays_assertContainsSubsequence_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  public void should_pass_if_actual_contains_sequence() {
    arrays.assertContainsSubsequence(someInfo(), actual, array("Yoda", "Leia"));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertContainsSubsequence(someInfo(), actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  public void should_pass_if_actual_contains_full_sequence_even_if_partial_sequence_is_found_before() {
    actual = array("Yoda", "Luke", "Leia", "Yoda", "Luke", "Obi-Wan");
    // note that actual starts with {"Yoda", "Luke"} a partial sequence of {"Yoda", "Luke", "Obi-Wan"}
    arrays.assertContainsSubsequence(someInfo(), actual, array("Yoda", "Luke", "Obi-Wan"));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual = new String[0];
    arrays.assertContainsSubsequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsSubsequence(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsSubsequence(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    arrays.assertContainsSubsequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_subsequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    Object[] subsequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      arrays.assertContainsSubsequence(info, actual, subsequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSubsequenceNotFound(info, subsequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_whole_subsequence() {
    AssertionInfo info = someInfo();
    Object[] subsequence = { "Han", "C-3PO" };
    try {
      arrays.assertContainsSubsequence(info, actual, subsequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSubsequenceNotFound(info, subsequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_first_elements_of_subsequence() {
    AssertionInfo info = someInfo();
    Object[] subsequence = { "Leia", "Obi-Wan", "Han" };
    try {
      arrays.assertContainsSubsequence(info, actual, subsequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSubsequenceNotFound(info, subsequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private void verifyFailureThrownWhenSubsequenceNotFound(AssertionInfo info, Object[] sequence) {
    verify(failures).failure(info, shouldContainSubsequence(actual, sequence));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContainsSubsequence(someInfo(), null, array("YOda"));
  }

  @Test
  public void should_throw_error_if_subsequence_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContainsSubsequence(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError();
    arraysWithCustomComparisonStrategy.assertContainsSubsequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] subsequence = { "LUKE", "LeiA", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      arraysWithCustomComparisonStrategy.assertContainsSubsequence(info, actual, subsequence);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainSubsequence(actual, subsequence, caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_whole_subsequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };
    try {
      arraysWithCustomComparisonStrategy.assertContainsSubsequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainSubsequence(actual, sequence, caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_first_elements_of_subsequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "LeiA", "Obi-Wan", "Han" };
    try {
      arraysWithCustomComparisonStrategy.assertContainsSubsequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainSubsequence(actual, sequence, caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsSubsequence(someInfo(), actual, array("LUKE", "LeiA"));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsSubsequence(someInfo(), actual,
        array("YOda", "LUKE", "LeiA", "Obi-WAn"));
  }
}
