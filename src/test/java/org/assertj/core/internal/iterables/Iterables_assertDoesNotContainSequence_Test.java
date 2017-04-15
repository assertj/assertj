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
package org.assertj.core.internal.iterables;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.error.ShouldNotContainSequence.shouldNotContainSequence;
import static org.assertj.core.test.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link Iterables#assertDoesNotContainSequence(AssertionInfo, Iterable, Object[])}</code>.
 *
 * @author Chris Arnott
 */
public class Iterables_assertDoesNotContainSequence_Test extends IterablesBaseTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = newArrayList("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    iterables.assertDoesNotContainSequence(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual.clear();
    iterables.assertDoesNotContainSequence(someInfo(), actual, array());
  }
  
  @Test
  // TODO CTA
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    iterables.assertDoesNotContainSequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertDoesNotContainSequence(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_pass_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    iterables.assertDoesNotContainSequence(info, actual, sequence);
  }

  @Test
  public void should_pass_if_actual_does_not_contain_whole_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };
    iterables.assertDoesNotContainSequence(info, actual, sequence);
  }

  @Test
  public void should_pass_if_actual_contains_first_elements_of_sequence_but_not_whole_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Luke", "Leia", "Han" };
    iterables.assertDoesNotContainSequence(info, actual, sequence);
  }

  private void verifyFailureThrownWhenSequenceNotFound(AssertionInfo info, Object[] sequence, int index) {
    verify(failures).failure(info, shouldNotContainSequence(actual, sequence, index));
  }

  @Test
  public void should_fail_if_actual_contains_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = array("Luke", "Leia");
    try {
      iterables.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence, 1);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_and_sequence_are_equal() {
    AssertionInfo info = someInfo();
    Object[] sequence = array("Yoda", "Luke", "Leia", "Obi-Wan");
    try {
      iterables.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence, 0);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_both_partial_and_complete_sequence() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Yoda", "Luke", "Yoda", "Obi-Wan");
    Object[] sequence = array("Yoda", "Obi-Wan");
    try {
      iterables.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence, 2);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_sequence_that_specifies_multiple_times_the_same_value_bug_544() {
    AssertionInfo info = someInfo();
    actual = newArrayList("a", "-", "b", "-", "c");
    Object[] sequence = array("a", "-", "b", "-", "c");
    try {
      iterables.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence, 0);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };
    iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainSequence(info, actual, sequence);
  }

  @Test
  public void should_pass_if_actual_contains_first_elements_of_sequence_but_not_whole_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Luke", "Leia", "Han" };
    iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainSequence(info, actual, sequence);
  }

  @Test
  public void should_fail_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = array("LUKe", "leia");
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainSequence(actual, sequence, 1, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = array("YODA", "luke", "lEIA", "Obi-wan");
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainSequence(actual, sequence, 0, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
