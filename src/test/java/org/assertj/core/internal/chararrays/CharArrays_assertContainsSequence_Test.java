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
package org.assertj.core.internal.chararrays;

import static org.assertj.core.error.ShouldContainSequence.shouldContainSequence;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.CharArrays.*;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.CharArrays;
import org.assertj.core.internal.CharArraysBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link CharArrays#assertContainsSequence(AssertionInfo, char[], char[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class CharArrays_assertContainsSequence_Test extends CharArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = arrayOf('a', 'b', 'c', 'd');
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsSequence(someInfo(), null, arrayOf('a'));
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsSequence(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual = emptyArray();
    arrays.assertContainsSequence(someInfo(), actual, emptyArray());
  }
  
  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    arrays.assertContainsSequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    char[] sequence = { 'a', 'b', 'c', 12, 20, 22 };
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
    char[] sequence = { 6, 20 };
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
    char[] sequence = { 6, 20, 22 };
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
    arrays.assertContainsSequence(someInfo(), actual, arrayOf('a', 'b'));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertContainsSequence(someInfo(), actual, arrayOf('a', 'b', 'c', 'd'));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContainsSequence(someInfo(), null, arrayOf('A'));
  }

  @Test
  public void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContainsSequence(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError();
    arraysWithCustomComparisonStrategy.assertContainsSequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    char[] sequence = { 'A', 'b', 'c', 12, 20, 22 };
    try {
      arraysWithCustomComparisonStrategy.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence, caseInsensitiveComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    char[] sequence = { 6, 20 };
    try {
      arraysWithCustomComparisonStrategy.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence, caseInsensitiveComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_first_elements_of_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    char[] sequence = { 6, 20, 22 };
    try {
      arraysWithCustomComparisonStrategy.assertContainsSequence(info, actual, sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSequence(actual, sequence, caseInsensitiveComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsSequence(someInfo(), actual, arrayOf('A', 'b'));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsSequence(someInfo(), actual, arrayOf('A', 'b', 'c', 'd'));
  }
}
