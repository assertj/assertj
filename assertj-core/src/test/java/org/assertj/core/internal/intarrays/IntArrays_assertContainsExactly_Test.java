/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal.intarrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.IntArrays.arrayOf;
import static org.assertj.core.testkit.IntArrays.emptyArray;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.IntArrays;
import org.assertj.core.internal.IntArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link IntArrays#assertContainsExactly(AssertionInfo, int[], int[])}</code>.
 */
class IntArrays_assertContainsExactly_Test extends IntArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_exactly() {
    arrays.assertContainsExactly(someInfo(), actual, arrayOf(6, 8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_with_duplicate_elements() {
    actual = arrayOf(6, 8, 8);
    arrays.assertContainsExactly(someInfo(), actual, arrayOf(6, 8, 8));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactly(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  void should_fail_if_actual_contains_given_values_exactly_but_in_different_order() {
    AssertionInfo info = someInfo();
    int[] expected = arrayOf(6, 10, 8);

    Throwable error = catchThrowable(() -> arrays.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, elementsDifferAtIndex(8, 10, 1), asList(actual), asList(expected));
  }

  @Test
  void should_fail_if_arrays_have_different_sizes() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual,
                                                                                                  arrayOf(6, 8)));
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual,
                                                                                                  emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), null, arrayOf(8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    int[] expected = { 6, 8, 20 };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                        newArrayList(20), newArrayList(10)),
                             asList(actual), asList(expected));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    int[] expected = { 6, 8, 10, 10 };

    Throwable error = catchThrowable(() -> arrays.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                        newArrayList(10), newArrayList()),
                             asList(actual), asList(expected));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, arrayOf(6, -8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    int[] expected = { -6, 10, 8 };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual,
                                                                                                    expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, elementsDifferAtIndex(8, 10, 1, absValueComparisonStrategy), asList(actual), asList(expected));
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(),
                                                                                                                              actual,
                                                                                                                              emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(),
                                                                                                               actual,
                                                                                                               null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(),
                                                                                                                              null,
                                                                                                                              arrayOf(-8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    int[] expected = { 6, -8, 20 };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactly(actual, asList(expected), newArrayList(20), newArrayList(10),
                                                  absValueComparisonStrategy),
                             asList(actual), asList(expected));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    int[] expected = { 6, 8, 10, 10 };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactly(actual, asList(expected),
                                                        newArrayList(10), newArrayList(),
                                                        absValueComparisonStrategy),
                             asList(actual), asList(expected));
  }

}
