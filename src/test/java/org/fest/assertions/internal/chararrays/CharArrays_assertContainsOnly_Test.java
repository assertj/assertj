/*
 * Created on Dec 20, 2010
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
package org.fest.assertions.internal.chararrays;

import static org.fest.assertions.error.ShouldContainOnly.shouldContainOnly;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.CharArrays.*;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Sets.newLinkedHashSet;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.CharArrays;
import org.fest.assertions.internal.CharArraysBaseTest;

/**
 * Tests for <code>{@link CharArrays#assertContainsOnly(AssertionInfo, char[], char[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class CharArrays_assertContainsOnly_Test extends CharArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_only() {
    arrays.assertContainsOnly(someInfo(), actual, arrayOf('a', 'b', 'c'));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnly(someInfo(), actual, arrayOf('c', 'b', 'a'));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_more_than_once() {
    actual = arrayOf('a', 'b', 'c', 'c', 'c');
    arrays.assertContainsOnly(someInfo(), actual, arrayOf('a', 'b', 'c'));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    arrays.assertContainsOnly(someInfo(), actual, arrayOf('a', 'b', 'c', 'a', 'b', 'c'));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertContainsOnly(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsOnly(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsOnly(someInfo(), null, arrayOf('a'));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only() {
    AssertionInfo info = someInfo();
    char[] expected = { 'a', 'b', 'd' };
    try {
      arrays.assertContainsOnly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnly(actual, expected, newLinkedHashSet('d'), newLinkedHashSet('c')));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), actual, arrayOf('A', 'b', 'c'));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), actual, arrayOf('c', 'b', 'A'));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_more_than_once_according_to_custom_comparison_strategy() {
    actual = arrayOf('A', 'b', 'c', 'c', 'c');
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), actual, arrayOf('A', 'b', 'c'));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), actual, arrayOf('A', 'b', 'c', 'A', 'b', 'c'));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_empty_whatever_custom_comparison_strategy_is() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), null, arrayOf('A'));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    char[] expected = { 'A', 'b', 'd' };
    try {
      arraysWithCustomComparisonStrategy.assertContainsOnly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnly(actual, expected, newLinkedHashSet('d'), newLinkedHashSet('c'), caseInsensitiveComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
