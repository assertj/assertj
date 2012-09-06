/*
 * Created on Nov 22, 2010
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
package org.fest.assertions.internal.iterables;

import static org.fest.assertions.error.ShouldContainSequence.shouldContainSequence;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ObjectArrays.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;
import static org.fest.util.Lists.newArrayList;

import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Iterables;
import org.fest.assertions.internal.IterablesBaseTest;

/**
 * Tests for <code>{@link Iterables#assertContainsSequence(AssertionInfo, Collection, Object[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Iterables_assertContainsSequence_Test extends IterablesBaseTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = newArrayList("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    iterables.assertContainsSequence(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    iterables.assertContainsSequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertContainsSequence(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      iterables.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_whole_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };
    try {
      iterables.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_first_elements_of_sequence_but_not_whole_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Luke", "Leia", "Han" };
    try {
      iterables.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private void verifyFailureThrownWhenSequenceNotFound(AssertionInfo info, Object[] sequence) {
    verify(failures).failure(info, shouldContainSequence(actual, sequence));
  }

  @Test
  public void should_pass_if_actual_contains_sequence() {
    iterables.assertContainsSequence(someInfo(), actual, array("Luke", "Leia"));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    iterables.assertContainsSequence(someInfo(), actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  public void should_pass_if_actual_contains_both_partial_and_complete_sequence() {
    actual = newArrayList("Yoda", "Luke", "Yoda", "Obi-Wan");
    iterables.assertContainsSequence(someInfo(), actual, array("Yoda", "Obi-Wan"));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_fail_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_first_elements_of_sequence_but_not_whole_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Luke", "Leia", "Han" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), actual, array("LUKe", "leia"));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), actual,
        array("YODA", "luke", "lEIA", "Obi-wan"));
  }

}
