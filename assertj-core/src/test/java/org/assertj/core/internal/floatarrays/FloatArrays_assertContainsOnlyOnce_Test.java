/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.floatarrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.FloatArrays.arrayOf;
import static org.assertj.core.testkit.FloatArrays.emptyArray;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FloatArrays;
import org.assertj.core.internal.FloatArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link FloatArrays#assertContainsOnlyOnce(AssertionInfo, float[], float[])}</code>.
 * 
 * @author William Delanoue
 */
class FloatArrays_assertContainsOnlyOnce_Test extends FloatArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_only() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, 8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(10, 8, 6));
  }

  @Test
  void should_fail_if_actual_contains_given_values_only_more_than_once() {
    // GIVEN
    AssertionInfo info = someInfo();
    actual = arrayOf(6, -8, 10, -6, -8, 10, -8, 6);
    float[] expected = { 6, -8, 20 };
    // WHEN
    var error = expectAssertionError(() -> arrays.assertContainsOnlyOnce(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((float) 20),
                                                  newLinkedHashSet((float) 6, (float) -8)).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, 8, 10, 6, 8, 10));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual = emptyArray();
    arrays.assertContainsOnlyOnce(someInfo(), actual, emptyArray());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsOnlyOnce(someInfo(), actual,
                                                                                                   emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsOnlyOnce(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsOnlyOnce(someInfo(), null, arrayOf(8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_only() {
    // GIVEN
    AssertionInfo info = someInfo();
    float[] expected = { 6, 8, 20 };
    // WHEN
    var error = expectAssertionError(() -> arrays.assertContainsOnlyOnce(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((float) 20),
                                                  newLinkedHashSet()).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, -8, 10));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf(10, -8, 6));
  }

  @Test
  void should_fail_if_actual_contains_given_values_only_more_than_once_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    actual = arrayOf(6, -8, 10, -6, -8, 10, -8);
    float[] expected = { 6, -8, 20 };
    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((float) 20),
                                                  newLinkedHashSet((float) 6, (float) -8),
                                                  absValueComparisonStrategy).create());
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, 8, 10, 6, -8, 10));
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(),
                                                                                                                               actual,
                                                                                                                               emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(),
                                                                                                                actual,
                                                                                                                null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(),
                                                                                                                               null,
                                                                                                                               arrayOf(-8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    float[] expected = { 6, -8, 20 };
    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((float) 20),
                                                  newLinkedHashSet(),
                                                  absValueComparisonStrategy).create());
  }
}
