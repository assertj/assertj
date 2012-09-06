/*
 * Created on Dec 14, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.intarrays;

import static org.fest.assertions.error.ShouldContainSequence.shouldContainSequence;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.IntArrays.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.IntArrays;
import org.fest.assertions.internal.IntArraysBaseTest;

/**
 * Tests for <code>{@link IntArrays#assertContainsSequence(AssertionInfo, int[], int[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class IntArrays_assertContainsSequence_Test extends IntArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = arrayOf(6, 8, 10, 12);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsSequence(someInfo(), null, arrayOf(8));
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsSequence(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertContainsSequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    int[] sequence = { 6, 8, 10, 12, 20, 22 };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_whole_sequence() {
    AssertionInfo info = someInfo();
    int[] sequence = { 6, 20 };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_first_elements_of_sequence() {
    AssertionInfo info = someInfo();
    int[] sequence = { 6, 20, 22 };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_sequence() {
    arrays.assertContainsSequence(someInfo(), actual, arrayOf(6, 8));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertContainsSequence(someInfo(), actual, arrayOf(6, 8, 10, 12));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContainsSequence(someInfo(), null, arrayOf(-8));
  }

  @Test
  public void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContainsSequence(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_sequence_is_empty_whatever_custom_comparison_strategy_is() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arraysWithCustomComparisonStrategy.assertContainsSequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    int[] sequence = { 6, -8, 10, 12, 20, 22 };
    try {
      arraysWithCustomComparisonStrategy.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    int[] sequence = { 6, 20 };
    try {
      arraysWithCustomComparisonStrategy.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_first_elements_of_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    int[] sequence = { 6, 20, 22 };
    try {
      arraysWithCustomComparisonStrategy.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsSequence(someInfo(), actual, arrayOf(6, -8));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsSequence(someInfo(), actual, arrayOf(6, -8, 10, 12));
  }
}
