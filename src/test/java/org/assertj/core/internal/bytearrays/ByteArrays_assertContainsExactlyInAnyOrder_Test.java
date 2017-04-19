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
package org.assertj.core.internal.bytearrays;

import org.assertj.core.api.*;
import org.assertj.core.internal.*;
import org.junit.*;

import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.*;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.ByteArrays.*;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.test.TestFailures.*;
import static org.assertj.core.util.FailureMessages.*;
import static org.assertj.core.util.Lists.*;
import static org.mockito.Mockito.*;

/**
 * Tests for <code>{@link ByteArrays#assertContainsExactlyInAnyOrder(AssertionInfo, byte[], byte[])}</code>.
 */
public class ByteArrays_assertContainsExactlyInAnyOrder_Test extends ByteArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(6, 8, 10));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_but_in_different_order() {
    AssertionInfo info = someInfo();
    arrays.assertContainsExactlyInAnyOrder(info, actual, arrayOf(6, 10, 8));
  }

  @Test
  public void should_fail_if_arrays_have_different_sizes() {
    thrown.expectAssertionError();
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(6, 8));
  }

  @Test
  public void should_fail_if_expected_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_expected_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, (byte[]) null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsExactlyInAnyOrder(someInfo(), null, arrayOf(8));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    byte[] expected = {6, 8, 20};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected,
          newArrayList((byte) 20), newArrayList((byte) 10), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    byte[] expected = {6, 8};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList((byte) 10),
              StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8, 8);
    byte[] expected = {6, 8};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList((byte) 8),
              StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8);
    byte[] expected = {6, 8, 8};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, newArrayList((byte) 8), emptyList(), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(6, -8, 10));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    byte[] expected = {-6, 10, 8};
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
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, (byte[]) null);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), null, arrayOf(-8));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    byte[] expected = {6, -8, 20};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, newArrayList((byte) 20),
          newArrayList((byte) 10), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    byte[] expected = {6, 8};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList((byte) 10), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8, 8);
    byte[] expected = {6, 8};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList((byte) 8), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8);
    byte[] expected = {6, 8, 8};
    try {
      arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, newArrayList((byte) 8), emptyList(), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
