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
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class ObjectArrays_assertContainsExactly_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_contains_exactly_given_values() {
    arrays.assertContainsExactly(INFO, actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_with_null_elements() {
    arrays.assertContainsExactly(INFO, array("Luke", "Yoda", "Leia", null), array("Luke", "Yoda", "Leia", null));
  }

  @Test
  void should_pass_if_actual_contains_given_values_exactly_with_duplicate_elements() {
    arrays.assertContainsExactly(INFO, array("Luke", "Yoda", "Yoda"), array("Luke", "Yoda", "Yoda"));
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactly(INFO, array(), array());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertContainsExactly(INFO, actual, array()));
  }

  @Test
  void should_fail_if_arrays_have_different_sizes() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertContainsExactly(INFO, actual, array("Luke", "Yoda")));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactly(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertContainsExactly(INFO, null, array("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsExactly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldContainExactly(actual, asList(expected), list("Han"), list("Leia")), asList(actual),
                             asList(expected));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_in_different_order() {
    // GIVEN
    Object[] expected = { "Luke", "Leia", "Yoda" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsExactly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, elementsDifferAtIndex("Yoda", "Leia", 1), asList(actual), asList(expected));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    // GIVEN
    var actual = array("Luke", "Leia", "Luke");
    Object[] expected = { "Luke", "Leia" };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsExactly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldContainExactly(actual, asList(expected), list(), list("Luke")), asList(actual),
                             asList(expected));
  }

  @Test
  void should_fail_if_arrays_have_different_sizes_for_large_arrays() {
    // GIVEN
    Object[] actual = new Object[2000];
    Object[] expected = new Object[actual.length + 1];
    for (int i = 0; i < actual.length; i++) {
      actual[i] = String.valueOf(i);
      expected[i] = String.valueOf(i);
    }
    expected[actual.length] = "extra";
    // WHEN
    expectAssertionError(() -> arrays.assertContainsExactly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldContainExactly(actual, asList(expected), list("extra"), list()), asList(actual),
                             asList(expected));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsExactly(INFO, actual,
                                                             array("LUKE", "YODA", "Leia"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldContainExactly(actual, asList(expected), list("Han"), list("Leia"),
                                                  caseInsensitiveStringComparisonStrategy),
                             asList(actual), asList(expected));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_in_different_order_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] expected = { "Luke", "Leia", "Yoda" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, elementsDifferAtIndex("Yoda", "Leia", 1, caseInsensitiveStringComparisonStrategy),
                             asList(actual), asList(expected));
  }

  @Test
  void should_fail_if_actual_contains_all_given_values_but_size_differ_according_to_custom_comparison_strategy() {
    // GIVEN
    var actual = array("Luke", "Leia", "Luke");
    Object[] expected = { "Luke", "Leia" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsExactly(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldContainExactly(actual, asList(expected), list(), list("Luke"),
                                                  caseInsensitiveStringComparisonStrategy),
                             asList(actual), asList(expected));
  }

}
