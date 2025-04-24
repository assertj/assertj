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
package org.assertj.core.internal.bytearrays;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.ByteArrays.arrayOf;
import static org.assertj.core.testkit.ByteArrays.emptyArray;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.ByteArraysBaseTest;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.testkit.IntArrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ByteArrays#assertContainsExactlyInAnyOrder(AssertionInfo, byte[], int[])}</code>.
 */
class ByteArrays_assertContainsExactlyInAnyOrder_with_Integer_Arguments_Test extends ByteArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_any_order() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, IntArrays.arrayOf(6, 8, 10));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), emptyArray(), IntArrays.emptyArray());
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_but_in_different_order() {
    AssertionInfo info = someInfo();
    arrays.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 10, 8));
  }

  @Test
  void should_fail_if_arrays_have_different_sizes() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual,
                                                                                                            IntArrays.arrayOf(6,
                                                                                                                              8)));
  }

  @Test
  void should_fail_if_expected_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual,
                                                                                                            IntArrays.emptyArray()));
  }

  @Test
  void should_throw_error_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual,
                                                                                             (int[]) null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), null,
                                                                                                            IntArrays.arrayOf(8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8, 20)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8, 20),
                                                                  newArrayList((byte) 20), newArrayList((byte) 10),
                                                                  StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8), emptyList(), newArrayList((byte) 10),
                                                            StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8, 8);

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8), emptyList(), newArrayList((byte) 8),
                                                            StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8);

    Throwable error = catchThrowable(() -> arrays.assertContainsExactlyInAnyOrder(info, actual, IntArrays.arrayOf(6, 8, 8)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8, 8), newArrayList((byte) 8), emptyList(),
                                                            StandardComparisonStrategy.instance()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, IntArrays.arrayOf(6, -8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, IntArrays.arrayOf(-6, 10, 8));
  }

  @Test
  void should_fail_if_expected_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(),
                                                                                                                                        actual,
                                                                                                                                        IntArrays.emptyArray()));
  }

  @Test
  void should_throw_error_if_expected_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(),
                                                                                                                         actual,
                                                                                                                         (int[]) null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(),
                                                                                                                                        null,
                                                                                                                                        IntArrays.arrayOf(-8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    byte[] expected = { 6, -8, 20 };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              IntArrays.arrayOf(6,
                                                                                                                                -8,
                                                                                                                                20)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected, newArrayList((byte) 20),
                                                                  newArrayList((byte) 10), absValueComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              IntArrays.arrayOf(6,
                                                                                                                                8)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8), emptyList(), newArrayList((byte) 10),
                                                                  absValueComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_duplicates_and_expected_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8, 8);

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              IntArrays.arrayOf(6,
                                                                                                                                8)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8), emptyList(), newArrayList((byte) 8),
                                                            absValueComparisonStrategy));
  }

  @Test
  void should_fail_if_expected_contains_duplicates_and_actual_does_not_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, 8);

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(info, actual,
                                                                                                              IntArrays.arrayOf(6,
                                                                                                                                8,
                                                                                                                                8)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldContainExactlyInAnyOrder(actual, arrayOf(6, 8, 8), newArrayList((byte) 8), emptyList(),
                                                            absValueComparisonStrategy));
  }

}
