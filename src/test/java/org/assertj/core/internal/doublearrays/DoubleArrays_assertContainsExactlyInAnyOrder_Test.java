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

import org.assertj.core.api.*;
import org.assertj.core.internal.*;
import org.junit.*;

import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.*;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.DoubleArrays.*;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.util.FailureMessages.*;
import static org.assertj.core.util.Lists.*;

/**
 * Tests for <code>{@link DoubleArrays#assertContainsExactlyInAnyOrder(AssertionInfo, double[], double[])}</code>.
 */
public class DoubleArrays_assertContainsExactlyInAnyOrder_Test extends DoubleArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(6d, 8d, 10d));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_but_in_different_order() {
    AssertionInfo info = someInfo();
    arrays.assertContainsExactlyInAnyOrder(info, actual, arrayOf(6d, 10d, 8d));
  }

  @Test
  public void should_fail_if_arrays_have_different_sizes() {
    thrown.expectAssertionError();
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(6d, 8d));
  }

  @Test
  public void should_fail_if_expected_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_expected_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsExactlyInAnyOrder(someInfo(), null, arrayOf(8d));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly() {
    double[] expected = { 6d, 8d, 20d };
    thrown.expectAssertionError(shouldContainExactlyInAnyOrder(actual, expected, newArrayList(20d), newArrayList(10d),
                                                               StandardComparisonStrategy.instance()));
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    double[] expected = { 6d, 8d };
    thrown.expectAssertionError(shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList(10d),
                                                               StandardComparisonStrategy.instance()));
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    actual = arrayOf(1d, 2d, 3d);
    double[] expected = { 1d, 2d };
    thrown.expectAssertionError(shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList(3d),
                                                               StandardComparisonStrategy.instance()));
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    actual = arrayOf(1d, 2d);
    double[] expected = {1d, 2d, 3d};
    thrown.expectAssertionError(shouldContainExactlyInAnyOrder(actual, expected, newArrayList(3d), emptyList(), StandardComparisonStrategy.instance()));
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, expected);
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(6d, -8d, 10d));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    double[] expected = { -6d, 10d, 8d };
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_expected_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError();
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_expected_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), null, arrayOf(-8d));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    double[] expected = {6d, -8d, 20d};
    thrown.expectAssertionError(shouldContainExactlyInAnyOrder(actual, expected, newArrayList(20d),
                                                               newArrayList(10d), absValueComparisonStrategy));
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    double[] expected = { 6d, 8d };
    thrown.expectAssertionError(shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList(10d),
                                                               absValueComparisonStrategy));
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, expected);

  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not_according_to_custom_comparison_strategy() {
    actual = arrayOf(1d, 2d, 3d);
    double[] expected = { 1d, 2d };
    thrown.expectAssertionError(shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList(3d),
                                                               absValueComparisonStrategy));
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not_according_to_custom_comparison_strategy() {
    actual = arrayOf(1d, 2d);
    double[] expected = { 1d, 2d, 3d };
    thrown.expectAssertionError(shouldContainExactlyInAnyOrder(actual, expected, newArrayList(3d), emptyList(),
                                                               absValueComparisonStrategy));
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, expected);
  }

}
