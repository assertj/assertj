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
package org.assertj.core.internal.floatarrays;

import org.assertj.core.api.*;
import org.assertj.core.internal.*;
import org.junit.*;

import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.*;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.FloatArrays.*;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.test.TestFailures.*;
import static org.assertj.core.util.FailureMessages.*;
import static org.assertj.core.util.Lists.*;
import static org.mockito.Mockito.*;

/**
 * Tests for <code>{@link FloatArrays#assertContainsExactlyInAnyOrder(AssertionInfo, float[], float[])}</code>.
 */
public class FloatArrays_assertContainsExactlyInAnyOrder_Test extends FloatArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(6f, 8f, 10f));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_but_in_different_order() {
    AssertionInfo info = someInfo();
    arrays.assertContainsExactlyInAnyOrder(info, actual, arrayOf(6f, 10f, 8f));
  }

  @Test
  public void should_fail_if_arrays_have_different_sizes() {
    thrown.expectAssertionError();
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(6f, 8f));
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
    arrays.assertContainsExactlyInAnyOrder(someInfo(), null, arrayOf(8f));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    float[] expected = {6f, 8f, 20f};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, newArrayList(20f), newArrayList(10f),
          StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    float[] expected = {6f, 8f};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList(10f),
              StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf(1f, 2f, 3f);
    float[] expected = {1f, 2f};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList(3f),
              StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf(1f, 2f);
    float[] expected = {1f, 2f, 3f};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, newArrayList(3f), emptyList(), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(6f, -8f, 10f));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    float[] expected = {-6f, 10f, 8f};
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
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), null, arrayOf(-8f));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    float[] expected = {6f, -8f, 20f};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, newArrayList(20f), newArrayList(10f),
              absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    float[] expected = {6f, -8f};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList(10f), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(1f, 2f, 3f);
    float[] expected = {1f, 2f};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList(3f),
              absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(1f, 2f);
    float[] expected = {1f, 2f, 3f};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, newArrayList(3f), emptyList(), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
