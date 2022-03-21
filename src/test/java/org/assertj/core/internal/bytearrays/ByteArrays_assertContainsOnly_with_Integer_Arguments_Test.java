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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.bytearrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ByteArrays.arrayOf;
import static org.assertj.core.test.ByteArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.ByteArraysBaseTest;
import org.assertj.core.test.IntArrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ByteArrays#assertContainsOnly(AssertionInfo, byte[], int[])}</code>.
 */
class ByteArrays_assertContainsOnly_with_Integer_Arguments_Test extends ByteArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_only() {
    arrays.assertContainsOnly(someInfo(), actual, IntArrays.arrayOf(6, 8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnly(someInfo(), actual, IntArrays.arrayOf(10, 8, 6));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_more_than_once() {
    actual = arrayOf(6, 8, 10, 8, 8, 8);
    arrays.assertContainsOnly(someInfo(), actual, IntArrays.arrayOf(6, 8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    arrays.assertContainsOnly(someInfo(), actual, IntArrays.arrayOf(6, 8, 10, 6, 8, 10));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual = emptyArray();
    arrays.assertContainsOnly(someInfo(), actual, IntArrays.emptyArray());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsOnly(someInfo(), actual,
                                                                                               IntArrays.emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsOnly(someInfo(), actual, (int[]) null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsOnly(someInfo(), null,
                                                                                               IntArrays.arrayOf(8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_only() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> arrays.assertContainsOnly(info, actual, IntArrays.arrayOf(6, 8, 20)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainOnly(actual, arrayOf(6, 8, 20), newArrayList((byte) 20), newArrayList((byte) 10)));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), actual, IntArrays.arrayOf(6, -8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), actual, IntArrays.arrayOf(10, -8, 6));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_more_than_once_according_to_custom_comparison_strategy() {
    actual = arrayOf(6, -8, 10, -8, 8, -8);
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), actual, IntArrays.arrayOf(6, -8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(), actual, IntArrays.arrayOf(6, -8, 10, 6, -8, 10));
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(),
                                                                                                                           actual,
                                                                                                                           IntArrays.emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(),
                                                                                                            actual,
                                                                                                            (int[]) null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnly(someInfo(),
                                                                                                                           null,
                                                                                                                           IntArrays.arrayOf(-8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsOnly(info, actual,
                                                                                                 IntArrays.arrayOf(6, -8, 20)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainOnly(actual, arrayOf(6, -8, 20), newArrayList((byte) 20), newArrayList((byte) 10),
                                               absValueComparisonStrategy));
  }
}
