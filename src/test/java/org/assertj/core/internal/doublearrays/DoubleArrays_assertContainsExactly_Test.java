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

import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainExactly.shouldHaveSameSize;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.DoubleArrays.arrayOf;
import static org.assertj.core.test.DoubleArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DoubleArrays;
import org.assertj.core.internal.DoubleArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.Test;

/**
 * Tests for <code>{@link DoubleArrays#assertContainsExactly(AssertionInfo, double[], double[])}</code>.
 */
public class DoubleArrays_assertContainsExactly_Test extends DoubleArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_exactly() {
    arrays.assertContainsExactly(someInfo(), actual, arrayOf(6d, 8d, 10d));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_with_duplicates() {
    actual = arrayOf(6d, 8d, 8d);
    arrays.assertContainsExactly(someInfo(), actual, arrayOf(6d, 8d, 8d));
  }
  
  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactly(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  public void should_fail_if_actual_contains_given_values_exactly_but_in_different_order() {
    thrown.expectAssertionError(elementsDifferAtIndex(8d, 10d, 1));
    arrays.assertContainsExactly(someInfo(), actual, arrayOf(6d, 10d, 8d));
  }

  @Test
  public void should_fail_if_arrays_have_different_sizes() {
    thrown.expectAssertionError();
    arrays.assertContainsExactly(someInfo(), actual, arrayOf(6d, 8d));
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    arrays.assertContainsExactly(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsExactly(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsExactly(someInfo(), null, arrayOf(8d));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly() {
    double[] expected = { 6d, 8d, 20d };
    thrown.expectAssertionError(shouldContainExactly(actual, expected, newArrayList(20d), newArrayList(10d)));
    arrays.assertContainsExactly(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    double[] expected = { 6d, 8d };
    thrown.expectAssertionError(shouldHaveSameSize(actual, expected, 3, 2, StandardComparisonStrategy.instance()));
    arrays.assertContainsExactly(someInfo(), actual, expected);
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    actual = arrayOf(6, -8, 8);
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, arrayOf(6d, -8d, 8d));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    double[] expected = { -6d, 10d, 8d };
    thrown.expectAssertionError(elementsDifferAtIndex(8d, 10d, 1, absValueComparisonStrategy));
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError();
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), null, arrayOf(-8d));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    double[] expected = { 6d, -8d, 20d };
    thrown.expectAssertionError(shouldContainExactly(actual, expected, newArrayList(20d),
                                                     newArrayList(10d), absValueComparisonStrategy));
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    double[] expected = { 6d, 8d };
    thrown.expectAssertionError(shouldHaveSameSize(actual, expected, 3, 2, absValueComparisonStrategy));
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, expected);
  }
}
