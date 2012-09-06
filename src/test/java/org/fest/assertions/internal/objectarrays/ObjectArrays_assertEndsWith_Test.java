/*
 * Created on Dec 2, 2010
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
package org.fest.assertions.internal.objectarrays;

import static org.fest.assertions.error.ShouldEndWith.shouldEndWith;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ObjectArrays.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.ObjectArrays;
import org.fest.assertions.internal.ObjectArraysBaseTest;

/**
 * Tests for <code>{@link ObjectArrays#assertEndsWith(AssertionInfo, Object[], Object[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ObjectArrays_assertEndsWith_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertEndsWith(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertEndsWith(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertEndsWith(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Yoda", "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      arrays.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_end_with_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };
    try {
      arrays.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_ends_with_first_elements_of_sequence_only() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    try {
      arrays.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private void verifyFailureThrownWhenSequenceNotFound(AssertionInfo info, Object[] sequence) {
    verify(failures).failure(info, shouldEndWith(actual, sequence));
  }

  @Test
  public void should_pass_if_actual_ends_with_sequence() {
    arrays.assertEndsWith(someInfo(), actual, array("Luke", "Leia", "Obi-Wan"));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertEndsWith(someInfo(), actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  public void should_throw_error_if_sequence_is_empty_whatever_custom_comparison_strategy_is() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Yoda", "LUKE", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      arraysWithCustomComparisonStrategy.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, sequence, caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_end_with_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };
    try {
      arraysWithCustomComparisonStrategy.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, sequence, caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_ends_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    try {
      arraysWithCustomComparisonStrategy.assertEndsWith(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, sequence, caseInsensitiveStringComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_ends_with_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, array("LUKE", "Leia", "Obi-Wan"));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, array("Yoda", "LUKE", "Leia", "Obi-Wan"));
  }
}
