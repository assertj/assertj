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

import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.CharArrays.arrayOf;
import static org.assertj.core.test.CharArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.CharArrays;
import org.assertj.core.internal.CharArraysBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link CharArrays#assertContainsOnlyOnce(AssertionInfo, char[], char[])}</code>.
 * 
 * @author William Delanoue
 */
public class CharArrays_assertContainsOnlyOnce_Test extends CharArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_only() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf('a', 'b', 'c'));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf('c', 'b', 'a'));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_only_more_than_once() {
    AssertionInfo info = someInfo();
    actual = arrayOf('a', 'b', 'b', 'a', 'c', 'd');
    char[] expected = { 'a', 'b', 'e' };
    try {
      arrays.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet('e'), newLinkedHashSet('a', 'b')));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf('a', 'b', 'c', 'a', 'b', 'c'));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual = emptyArray();
    arrays.assertContainsOnlyOnce(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    arrays.assertContainsOnlyOnce(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsOnlyOnce(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsOnlyOnce(someInfo(), null, arrayOf('a'));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only() {
    AssertionInfo info = someInfo();
    char[] expected = { 'a', 'b', 'd' };
    try {
      arrays.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet('d'), newLinkedHashSet()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf('A', 'b', 'C'));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf('C', 'B', 'A'));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_only_more_than_once_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf('a', 'B', 'b', 'A', 'c', 'd');
    char[] expected = { 'a', 'B', 'e' };
    try {
      arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet('e'), newLinkedHashSet('a', 'B'),
              caseInsensitiveComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy
        .assertContainsOnlyOnce(someInfo(), actual, arrayOf('a', 'b', 'c', 'A', 'b', 'C'));
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError();
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), null, arrayOf((char) -8));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    char[] expected = { 'A', 'b', 'D' };
    try {
      arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet('D'), newLinkedHashSet(),
              caseInsensitiveComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
