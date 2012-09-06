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

import static org.fest.assertions.error.ShouldStartWith.shouldStartWith;
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
 * Tests for <code>{@link IntArrays#assertStartsWith(AssertionInfo, int[], int[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class IntArrays_assertStartsWith_Test extends IntArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = arrayOf(6, 8, 10, 12);
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertStartsWith(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertStartsWith(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertStartsWith(someInfo(), null, arrayOf(8));
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    int[] sequence = { 6, 8, 10, 12, 20, 22 };
    try {
      arrays.assertStartsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_start_with_sequence() {
    AssertionInfo info = someInfo();
    int[] sequence = { 8, 10 };
    try {
      arrays.assertStartsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    AssertionInfo info = someInfo();
    int[] sequence = { 6, 20 };
    try {
      arrays.assertStartsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private void verifyFailureThrownWhenSequenceNotFound(AssertionInfo info, int[] sequence) {
    verify(failures).failure(info, shouldStartWith(actual, sequence));
  }

  @Test
  public void should_pass_if_actual_starts_with_sequence() {
    arrays.assertStartsWith(someInfo(), actual, arrayOf(6, 8, 10));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertStartsWith(someInfo(), actual, arrayOf(6, 8, 10, 12));
  }

  @Test
  public void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertStartsWith(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_sequence_is_empty_whatever_custom_comparison_strategy_is() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arraysWithCustomComparisonStrategy.assertStartsWith(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertStartsWith(someInfo(), null, arrayOf(-8));
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    int[] sequence = { 6, -8, 10, 12, 20, 22 };
    try {
      arraysWithCustomComparisonStrategy.assertStartsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldStartWith(actual, sequence, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_start_with_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    int[] sequence = { -8, 10 };
    try {
      arraysWithCustomComparisonStrategy.assertStartsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldStartWith(actual, sequence, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_starts_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    int[] sequence = { 6, 20 };
    try {
      arraysWithCustomComparisonStrategy.assertStartsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldStartWith(actual, sequence, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_starts_with_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertStartsWith(someInfo(), actual, arrayOf(6, -8, 10));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertStartsWith(someInfo(), actual, arrayOf(6, -8, 10, 12));
  }
}
