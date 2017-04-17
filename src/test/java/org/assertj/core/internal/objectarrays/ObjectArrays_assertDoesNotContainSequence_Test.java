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

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;

import static org.assertj.core.error.ShouldNotContainSequence.shouldNotContainSequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link ObjectArrays#assertDoesNotContainSequence(AssertionInfo, Object[], Object[])}</code>.
 *
 * @author Chris Arnott
 */
public class ObjectArrays_assertDoesNotContainSequence_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  public void should_fail_if_actual_contains_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = array("Luke", "Leia");
    try {
      arrays.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainSequence(actual, sequence, 1));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_and_sequence_are_equal() {
    AssertionInfo info = someInfo();
    Object[] sequence = array("Yoda", "Luke", "Leia", "Obi-Wan");
    try {
      arrays.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainSequence(actual, sequence, 0));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_full_sequence_even_if_partial_sequence_is_found_before() {
    AssertionInfo info = someInfo();
    actual = array("Yoda", "Luke", "Leia", "Yoda", "Luke", "Obi-Wan");
    // note that actual starts with {"Yoda", "Luke"} a partial sequence of {"Yoda", "Luke", "Obi-Wan"}
    Object[] sequence = array("Yoda", "Luke", "Obi-Wan");
    try {
      arrays.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainSequence(actual, sequence, 3));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual = new String[0];
    arrays.assertDoesNotContainSequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertDoesNotContainSequence(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertDoesNotContainSequence(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    arrays.assertDoesNotContainSequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_pass_if_sequence_is_bigger_than_actual() {
    arrays.assertDoesNotContainSequence(someInfo(), actual,
                                        array("Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin"));
  }

  @Test
  public void should_pass_if_actual_does_not_contain_whole_sequence() {
    arrays.assertDoesNotContainSequence(someInfo(), actual, array("Han", "C-3PO"));
  }

  @Test
  public void should_pass_if_actual_contains_first_elements_of_sequence() {
    arrays.assertDoesNotContainSequence(someInfo(), actual, array("Leia", "Obi-Wan", "Han"));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(someInfo(), null, array("YOda"));
  }

  @Test
  public void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError();
    arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_pass_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(someInfo(), actual, array("LUKE", "LeiA", "Obi-Wan",
                                                                                              "Han", "C-3PO", "R2-D2",
                                                                                              "Anakin"));
  }

  @Test
  public void should_pass_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(someInfo(), actual, array("Han", "C-3PO"));
  }

  @Test
  public void should_pass_if_actual_contains_first_elements_of_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(someInfo(), actual, array("LeiA", "Obi-Wan", "Han"));
  }

  @Test
  public void should_fail_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = array("LUKE", "LeiA");
    try {
      arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainSequence(actual, sequence, 1,
                                                              caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = array("YOda", "LUKE", "LeiA", "Obi-WAn");
    try {
      arraysWithCustomComparisonStrategy.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainSequence(actual, sequence, 0,
                                                              caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
