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
package org.assertj.tests.core.internal.objectarrays;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * @author Lovro Pandzic
 */
class ObjectArrays_assertContainsExactlyInAnyOrder_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_exactly_in_any_order_given_values() {
    arrays.assertContainsExactlyInAnyOrder(INFO, actual, array("Leia", "Yoda", "Luke"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_any_order_with_null_elements() {
    arrays.assertContainsExactlyInAnyOrder(INFO,
                                           array("Luke", "Yoda", "Leia", null),
                                           array("Leia", null, "Yoda", "Luke"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactlyInAnyOrder(INFO, array(), array());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(INFO, actual, array()));
  }

  @Test
  void should_fail_if_arrays_have_different_sizes() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(INFO, actual, array("Luke", "Yoda")));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertContainsExactlyInAnyOrder(INFO, null, array("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_in_any_order() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsExactlyInAnyOrder(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldContainExactlyInAnyOrder(actual, expected, list("Han"), list("Leia"),
                                                                  StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    // GIVEN
    var actual = array("Luke", "Leia", "Luke");
    Object[] expected = { "Luke", "Leia" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsExactlyInAnyOrder(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldContainExactlyInAnyOrder(actual, expected, emptyList(), list("Luke"),
                                                            StandardComparisonStrategy.instance()));
  }

  @Test
  void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    // GIVEN
    var actual = array("Luke", "Leia");
    Object[] expected = { "Luke", "Leia", "Luke" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsExactlyInAnyOrder(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldContainExactlyInAnyOrder(actual, expected, list("Luke"), emptyList(),
                                                            StandardComparisonStrategy.instance()));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(INFO, actual,
                                                                       array("LUKE", "YODA", "Leia"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_in_any_order_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(INFO, actual,
                                                                                                  expected));
    // THEN
    verify(failures).failure(INFO, shouldContainExactlyInAnyOrder(actual, expected, list("Han"), list("Leia"),
                                                                  caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_duplicates_and_expected_does_not_according_to_custom_comparison_strategy() {
    // GIVEN
    var actual = array("Luke", "Leia", "Luke");
    Object[] expected = { "Luke", "Leia" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(INFO, actual,
                                                                                                  expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldContainExactlyInAnyOrder(actual, expected, emptyList(), list("Luke"),
                                                            caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_expected_contains_duplicates_and_actual_does_not_according_to_custom_comparison_strategy() {
    // GIVEN
    var actual = array("Luke", "Leia");
    Object[] expected = { "Luke", "Leia", "Luke" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsExactlyInAnyOrder(INFO, actual,
                                                                                                  expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldContainExactlyInAnyOrder(actual, expected, list("Luke"), emptyList(),
                                                            caseInsensitiveStringComparisonStrategy));
  }

}
