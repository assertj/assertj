/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveExactlyTypes.elementsTypesDifferAtIndex;
import static org.assertj.core.error.ShouldHaveExactlyTypes.shouldHaveTypes;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;

import java.util.LinkedList;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.jupiter.api.Test;

class ObjectArrays_assertHasExactlyElementsOfTypes_Test extends ObjectArraysBaseTest {

  private static final WritableAssertionInfo INFO = someInfo();

  private static final Object[] ACTUAL = { "a", new LinkedList<>(), 10L };

  @Test
  void should_pass_if_actual_has_exactly_elements_of_the_expected_types_in_order() {
    arrays.assertHasExactlyElementsOfTypes(INFO, ACTUAL, String.class, LinkedList.class, Long.class);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Object[] array = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasExactlyElementsOfTypes(INFO, array, String.class));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_one_element_in_actual_does_not_have_the_expected_type() {
    // GIVEN
    Class<?>[] expected = { String.class, LinkedList.class, Double.class };
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasExactlyElementsOfTypes(INFO, ACTUAL, expected));
    // THEN
    then(error).hasMessage(shouldHaveTypes(ACTUAL, list(expected), list(Double.class), list(Long.class)).create());
  }

  @Test
  void should_fail_if_types_of_elements_are_not_in_the_same_order_as_expected() {
    // GIVEN
    Class<?>[] expected = { LinkedList.class, String.class, Long.class };
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasExactlyElementsOfTypes(INFO, ACTUAL, expected));
    // THEN
    then(error).hasMessage(elementsTypesDifferAtIndex(ACTUAL[0], LinkedList.class, 0).create());
  }

  @Test
  void should_fail_if_actual_has_more_elements_than_expected() {
    // GIVEN
    Class<?>[] expected = { String.class };
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasExactlyElementsOfTypes(INFO, ACTUAL, expected));
    // THEN
    then(error).hasMessage(shouldHaveTypes(ACTUAL, list(expected), list(), list(LinkedList.class, Long.class)).create());
  }

  @Test
  void should_fail_if_actual_elements_types_are_found_but_there_are_not_enough_expected_type_elements() {
    // GIVEN
    Class<?>[] expected = { String.class, LinkedList.class, Long.class, Long.class };
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasExactlyElementsOfTypes(INFO, ACTUAL, expected));
    // THEN
    then(error).hasMessage(shouldHaveTypes(ACTUAL, list(expected), list(Long.class), list()).create());
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_has_exactly_elements_of_the_expected_types_whatever_the_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertHasExactlyElementsOfTypes(INFO, ACTUAL, String.class, LinkedList.class, Long.class);
  }

  @Test
  void should_fail_if_one_element_in_actual_does_not_have_the_expected_type_whatever_the_custom_comparison_strategy_is() {
    // GIVEN
    Class<?>[] expected = { String.class, LinkedList.class, Double.class };
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertHasExactlyElementsOfTypes(INFO,
                                                                                                                         ACTUAL,
                                                                                                                         expected));
    // THEN
    then(error).hasMessage(shouldHaveTypes(ACTUAL, list(expected), list(Double.class), list(Long.class)).create());
  }

  @Test
  void should_fail_if_types_of_elements_are_not_in_the_same_order_as_expected_whatever_the_custom_comparison_strategy_is() {
    // GIVEN
    Class<?>[] expected = { LinkedList.class, String.class, Long.class };
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertHasExactlyElementsOfTypes(INFO,
                                                                                                                         ACTUAL,
                                                                                                                         expected));
    // THEN
    then(error).hasMessage(elementsTypesDifferAtIndex(ACTUAL[0], LinkedList.class, 0).create());
  }

  @Test
  void should_fail_if_actual_elements_types_are_found_but_there_are_not_enough_expected_type_elements_whatever_the_custom_comparison_strategy_is() {
    // GIVEN
    Class<?>[] expected = { String.class, LinkedList.class, Long.class, Long.class };
    // WHEN
    AssertionError error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertHasExactlyElementsOfTypes(INFO,
                                                                                                                         ACTUAL,
                                                                                                                         expected));
    // THEN
    then(error).hasMessage(shouldHaveTypes(ACTUAL, list(expected), list(Long.class), list()).create());
  }
}
