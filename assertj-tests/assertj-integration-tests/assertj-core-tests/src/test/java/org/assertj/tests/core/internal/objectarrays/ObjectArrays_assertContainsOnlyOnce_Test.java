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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * @author William Delanoue
 */
class ObjectArrays_assertContainsOnlyOnce_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_given_values_only_once() {
    arrays.assertContainsOnlyOnce(INFO, actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_even_if_actual_type_is_not_comparable() {
    // Locale class does not implement Comparable
    arrays.assertContainsOnlyOnce(INFO, array(Locale.ROOT, Locale.US, Locale.US), array(Locale.ROOT));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnlyOnce(INFO, actual, array("Leia", "Yoda", "Luke"));
  }

  @Test
  void should_fail_if_actual_contains_given_values_more_than_once() {
    // GIVEN
    var actual = array("Luke", "Yoda", "Han", "Luke", "Yoda", "Han", "Yoda", "Luke");
    String[] expected = { "Luke", "Yoda", "Leia" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsOnlyOnce(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Leia"),
                                                    newLinkedHashSet("Luke", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_even_if_duplicated() {
    arrays.assertContainsOnlyOnce(INFO, actual, array("Luke", "Yoda", "Leia", "Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual = new String[] {};
    arrays.assertContainsOnlyOnce(INFO, actual, emptyArray());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertContainsOnlyOnce(INFO, actual, emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsOnlyOnce(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertContainsOnlyOnce(INFO, null, array("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_values() {
    // GIVEN
    String[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsOnlyOnce(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"), newLinkedHashSet()));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(INFO, actual, array("Luke", "yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(INFO, actual, array("Leia", "yoda", "Luke"));
  }

  @Test
  void should_fail_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    // GIVEN
    var actual = array("Luke", "yODA", "Han", "luke", "yoda", "Han", "YodA");
    String[] expected = { "Luke", "yOda", "Leia" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Leia"), newLinkedHashSet("Luke", "yOda"),
                                                    caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_pass_if_actual_contains_given_values_only_once_according_to_custom_comparison_strategy_even_if_duplicated_() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(INFO, actual,
                                                              array("Luke", "Yoda", "Leia", "Luke", "yODA", "LeiA"));
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(INFO,
                                                                                                       actual,
                                                                                                       emptyArray()));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(INFO,
                                                                                                                actual,
                                                                                                                null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(INFO,
                                                                                                       null,
                                                                                                       array("yoda")))
                                                                                                                      .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_values_only_once_according_to_custom_comparison_strategy() {
    // GIVEN
    String[] expected = { "Luke", "yoda", "han" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(INFO, actual, expected));
    // THEN
    verify(failures).failure(
                             INFO,
                             shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("han"), newLinkedHashSet(),
                                                    caseInsensitiveStringComparisonStrategy));
  }
}
