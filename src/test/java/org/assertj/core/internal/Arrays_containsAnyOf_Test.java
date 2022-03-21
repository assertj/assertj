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
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.Test;

class Arrays_containsAnyOf_Test extends BaseArraysTest {

  @Test
  void should_pass_if_actual_contains_given_values() {
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Luke"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_in_different_order() {
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Leia", "Yoda"));
  }

  @Test
  void should_pass_if_actual_contains_all_given_values() {
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_more_than_once() {
    actual = array("Luke", "Yoda", "Leia", "Luke", "Luke");
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Luke"));
  }

  @Test
  void should_pass_if_actual_contains_just_one_of_given_values() {
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Luke", "John", "Tom"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array("Luke", "Luke"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual = new String[0];
    arrays.assertContainsAnyOf(someInfo(), failures, actual, array());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsAnyOf(someInfo(), failures, actual,
                                                                                                emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsAnyOf(someInfo(), failures, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsAnyOf(someInfo(), failures, null,
                                                                                                array("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_of_the_given_values() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "John" };

    Throwable error = catchThrowable(() -> arrays.assertContainsAnyOf(info, failures, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainAnyOf(actual, expected, StandardComparisonStrategy.instance()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual, array("LUKE"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual, array("LEIA", "yODa"));
  }

  @Test
  void should_pass_if_actual_contains_all_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual,
                                                           array("luke", "YODA", "leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    actual = array("Luke", "Yoda", "Leia", "Luke", "Luke");
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual, array("LUke"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual, array("LUke", "LuKe"));
  }

  @Test
  void should_pass_if_actual_contains_just_one_of_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAnyOf(someInfo(), failures, actual,
                                                           array("LuKe", "JoHn", "ToM"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "John" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContainsAnyOf(info, failures, actual,
                                                                                                  expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainAnyOf(actual, expected, caseInsensitiveStringComparisonStrategy));
  }
}
