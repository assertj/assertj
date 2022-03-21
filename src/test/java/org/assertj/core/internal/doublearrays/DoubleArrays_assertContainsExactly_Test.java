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
package org.assertj.core.internal.doublearrays;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.DoubleArrays.arrayOf;
import static org.assertj.core.test.DoubleArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DoubleArrays;
import org.assertj.core.internal.DoubleArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link DoubleArrays#assertContainsExactly(AssertionInfo, double[], double[])}</code>.
 */
class DoubleArrays_assertContainsExactly_Test extends DoubleArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_exactly() {
    arrays.assertContainsExactly(someInfo(), actual, arrayOf(6d, 8d, 10d));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_with_duplicates() {
    actual = arrayOf(6d, 8d, 8d);
    arrays.assertContainsExactly(someInfo(), actual, arrayOf(6d, 8d, 8d));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactly(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  void should_fail_if_actual_contains_given_values_exactly_but_in_different_order() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual,
                                                                                                  arrayOf(6d, 10d, 8d)))
                                                   .withMessage(elementsDifferAtIndex(8d, 10d, 1).create());
  }

  @Test
  void should_fail_if_arrays_have_different_sizes() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual,
                                                                                                  arrayOf(6d, 8d)));
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
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), null, arrayOf(8d)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    double[] expected = { 6d, 8d, 20d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual, expected))
                                                   .withMessage(shouldContainExactly(actual, asList(expected), newArrayList(20d),
                                                                                     newArrayList(10d)).create());
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    double[] expected = { 6d, 8d, 10d, 10d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactly(someInfo(), actual, expected))
                                                   .withMessage(shouldContainExactly(actual, asList(expected), newArrayList(10d),
                                                                                     newArrayList()).create());
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    actual = arrayOf(6, -8, 8);
    arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(), actual, arrayOf(6d, -8d, 8d));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_different_order_according_to_custom_comparison_strategy() {
    double[] expected = { -6d, 10d, 8d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(),
                                                                                                                              actual,
                                                                                                                              expected))
                                                   .withMessage(elementsDifferAtIndex(8d, 10d, 1,
                                                                                      absValueComparisonStrategy).create());
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
                                                                                                                              arrayOf(-8d)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    double[] expected = { 6d, -8d, 20d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(),
                                                                                                                              actual,
                                                                                                                              expected))
                                                   .withMessage(format(shouldContainExactly(actual, asList(expected),
                                                                                            newArrayList(20d),
                                                                                            newArrayList(10d),
                                                                                            absValueComparisonStrategy).create()));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    double[] expected = { 6d, 8d, 10d, 10d };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(someInfo(),
                                                                                                                              actual,
                                                                                                                              expected))
                                                   .withMessage(format(shouldContainExactly(actual, asList(expected),
                                                                                            newArrayList(10d),
                                                                                            newArrayList(),
                                                                                            absValueComparisonStrategy).create()));
  }
}
