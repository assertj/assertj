/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ObjectArrays_assertContainsOnly_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_only() {
    arrays.assertContainsOnly(INFO, array("Luke", null, "Yoda", null, "Leia"), array("Luke", "Yoda", "Leia", null));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnly(INFO, actual, array("Leia", "Yoda", "Luke"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_more_than_once() {
    arrays.assertContainsOnly(INFO, array("Luke", "Yoda", "Leia", "Luke", "Luke"), array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    arrays.assertContainsOnly(INFO, actual, array("Luke", "Luke", "Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsOnly(INFO, new String[0], emptyArray());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertContainsOnly(INFO, actual, emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsOnly(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertContainsOnly(INFO, null, array("Yoda")))
                                                                                                  .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_only() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsOnly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldContainOnly(actual, expected, list("Han"), list("Leia")));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnly(INFO, actual, array("LUKE", "YOda", "LeiA"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnly(INFO, actual, array("LeiA", "YOda", "LUKE"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_more_than_once_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnly(INFO,
                                                          array("Luke", "Yoda", "Leia", "Luke", "Luke"),
                                                          array("LUKE", "YOda", "LeiA"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnly(INFO,
                                                          array("Luke", "Yoda", "Leia", "LUke"),
                                                          array("LUke", "LUKE", "LuKE", "YOda", "LeiA"));
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnly(INFO,
                                                                                                   actual,
                                                                                                   emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnly(INFO,
                                                                                                            actual,
                                                                                                            null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnly(INFO,
                                                                                                   null,
                                                                                                   array("YOda")))
                                                                                                                  .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] expected = { "LUKE", "YOda", "Han" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsOnly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldContainOnly(actual, expected, list("Han"), list("Leia"),
                                                     caseInsensitiveStringComparisonStrategy));
  }
}
