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
package org.fest.assertions.internal.shortarrays;

import static org.fest.assertions.error.ShouldContain.shouldContain;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ShortArrays.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Sets.newLinkedHashSet;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.ShortArrays;
import org.fest.assertions.internal.ShortArraysBaseTest;

/**
 * Tests for <code>{@link ShortArrays#assertContains(AssertionInfo, short[], short[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShortArrays_assertContains_Test extends ShortArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values() {
    arrays.assertContains(someInfo(), actual, arrayOf(6));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_in_different_order() {
    arrays.assertContains(someInfo(), actual, arrayOf(8, 10));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_values() {
    arrays.assertContains(someInfo(), actual, arrayOf(6, 8, 10));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_more_than_once() {
    actual = arrayOf(6, 8, 10, 10, 8);
    arrays.assertContains(someInfo(), actual, arrayOf(8));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    arrays.assertContains(someInfo(), actual, arrayOf(6, 6));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertContains(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContains(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContains(someInfo(), null, arrayOf(8));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values() {
    AssertionInfo info = someInfo();
    short[] expected = { 6, 8, 9 };
    try {
      arrays.assertContains(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet((short) 9)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, arrayOf(-6));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, arrayOf(-8, 10));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, arrayOf(6, -8, 10));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    actual = arrayOf(6, 8, 10, -10, 8);
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, arrayOf(10));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, arrayOf(6, 6, -6));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_empty_whatever_custom_comparison_strategy_is() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), null, arrayOf(8));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    short[] expected = { 6, 8, 9 };
    try {
      arraysWithCustomComparisonStrategy.assertContains(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet((short) 9), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
