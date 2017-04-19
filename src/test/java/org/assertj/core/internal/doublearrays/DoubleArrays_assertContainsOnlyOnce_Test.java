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
package org.assertj.core.internal.doublearrays;

import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.DoubleArrays.arrayOf;
import static org.assertj.core.test.DoubleArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DoubleArrays;
import org.assertj.core.internal.DoubleArraysBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link DoubleArrays#assertContainsOnlyOnce(AssertionInfo, double[], double[])}</code>.
 * 
 * @author William Delanoue
 */
public class DoubleArrays_assertContainsOnlyOnce_Test extends DoubleArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_only() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, 8, 10));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(10, 8, 6));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_only_more_than_once() {
    actual = arrayOf(6, -8, 10, -6, -8, 10, -8, 6);
    double[] expected = { 6, -8, 20 };
    thrown.expectAssertionError(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((double) 20),
                                                       newLinkedHashSet((double) 6, (double) -8)));
    arrays.assertContainsOnlyOnce(someInfo(), actual, expected);
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, 8, 10, 6, 8, 10));
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
    arrays.assertContainsOnlyOnce(someInfo(), null, arrayOf(8));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only() {
    double[] expected = { 6, 8, 20 };
    thrown.expectAssertionError( shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((double) 20), newLinkedHashSet()));
    arrays.assertContainsOnlyOnce(someInfo(), actual, expected);
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, -8, 10));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf(10, -8, 6));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_only_more_than_once_according_to_custom_comparison_strategy() {
    actual = arrayOf(6, -8, 10, -6, -8, 10, -8);
    double[] expected = { 6, -8, 20 };
    thrown.expectAssertionError(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((double) 20),
                                                       newLinkedHashSet((double) 6, (double) -8), absValueComparisonStrategy));
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, expected);
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, 8, 10, 6, -8, 10));
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
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), null, arrayOf(-8));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    double[] expected = { 6, -8, 20 };
    thrown.expectAssertionError(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((double) 20), newLinkedHashSet(),
                                                       absValueComparisonStrategy));
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, expected);
  }
}
