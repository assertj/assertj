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
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.internal.ErrorMessages.iterableToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;

class ObjectArrays_assertContainsAll_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_all_iterable_values() {
    arrays.assertContainsAll(INFO, actual, list("Luke", "Yoda", "Leia"));
    arrays.assertContainsAll(INFO, actual, list("Luke", "Yoda"));
    // order is not important
    arrays.assertContainsAll(INFO, actual, list("Yoda", "Luke"));
  }

  @Test
  void should_pass_if_actual_contains_all_iterable_values_more_than_once() {
    arrays.assertContainsAll(INFO, array("Luke", "Yoda", "Leia", "Luke", "Luke"), list("Luke"));
  }

  @Test
  void should_pass_if_iterable_is_empty() {
    arrays.assertContainsAll(INFO, actual, list());
  }

  @Test
  void should_throw_error_if_iterable_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsAll(INFO, actual, null))
                                    .withMessage(iterableToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertContainsAll(INFO, null, list("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_iterable_values() {
    // GIVEN
    List<String> expected = list("Han", "Luke");
    // WHEN
    expectAssertionError(() -> arrays.assertContainsAll(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldContain(actual, expected.toArray(), newLinkedHashSet("Han")));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_all_iterable_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(INFO, actual, list("LUKE"));
  }

  @Test
  void should_pass_if_actual_contains_all_iterable_values_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(INFO, actual, list("LEIa", "YodA"));
  }

  @Test
  void should_pass_if_actual_contains_all_all_iterable_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(INFO, actual, list("LukE", "YodA", "LeiA"));
  }

  @Test
  void should_pass_if_actual_contains_all_iterable_values_more_than_once_according_to_custom_comparison_strategy() {
    actual = array("Luke", "Yoda", "Leia", "Luke", "Luke");
    arraysWithCustomComparisonStrategy.assertContainsAll(INFO, actual, list("LUKE"));
  }

  @Test
  void should_pass_if_actual_contains_all_iterable_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsAll(INFO, actual, list("LUKE", "LUKE"));
  }

  @Test
  void should_pass_if_iterable_to_look_for_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertContainsAll(INFO, actual, list());
  }

  @Test
  void should_throw_error_if_iterable_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsAll(INFO,
                                                                                                           actual,
                                                                                                           null))
                                    .withMessage(iterableToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> expected = list("Han", "LUKE");
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsAll(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldContain(actual, expected.toArray(), newLinkedHashSet("Han"),
                                           caseInsensitiveStringComparisonStrategy));
  }
}
