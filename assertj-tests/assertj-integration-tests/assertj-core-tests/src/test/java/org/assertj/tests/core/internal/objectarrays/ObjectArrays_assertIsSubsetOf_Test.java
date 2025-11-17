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
import static org.assertj.core.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.assertj.core.internal.ErrorMessages.iterableToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.junit.jupiter.api.Test;

class ObjectArrays_assertIsSubsetOf_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_is_subset_of_set() {
    arrays.assertIsSubsetOf(INFO, array("Yoda", "Luke"), list("Luke", "Yoda", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_has_the_same_elements_as_set() {
    arrays.assertIsSubsetOf(INFO, array("Yoda", "Luke"), list("Luke", "Yoda"));
  }

  @Test
  void should_pass_if_actual_is_empty() {
    arrays.assertIsSubsetOf(INFO, new String[0], list("Luke", "Yoda"));
  }

  @Test
  void should_pass_if_actual_and_set_are_both_empty() {
    arrays.assertIsSubsetOf(INFO, new String[0], list());
  }

  @Test
  void should_pass_if_actual_has_duplicates_but_all_elements_are_in_values() {
    arrays.assertIsSubsetOf(INFO, array("Yoda", "Yoda"), list("Yoda"));
  }

  @Test
  void should_pass_if_values_has_duplicates_but_all_elements_are_in_values() {
    arrays.assertIsSubsetOf(INFO, array("Yoda", "C-3PO"), list("Yoda", "Yoda", "C-3PO"));
  }

  @Test
  void should_pass_if_both_actual_and_values_have_duplicates_but_all_elements_are_in_values() {
    arrays.assertIsSubsetOf(INFO,
                            array("Yoda", "Yoda", "Yoda", "C-3PO", "Obi-Wan"),
                            list("Yoda", "Yoda", "C-3PO", "C-3PO", "Obi-Wan"));
  }

  @Test
  void should_throw_error_if_set_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertIsSubsetOf(INFO, array("Yoda", "Luke"), null))
                                    .withMessage(iterableToLookForIsNull());
  }

  @Test
  void should_throw_error_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertIsSubsetOf(INFO, null, list("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_subset_of_values() {
    // GIVEN
    var actual = array("Yoda");
    List<String> values = list("C-3PO", "Leila");
    List<String> extra = list("Yoda");
    // WHEN
    var error = expectAssertionError(() -> arrays.assertIsSubsetOf(INFO, actual, values));
    // THEN
    then(error).hasMessage(shouldBeSubsetOf(actual, values, extra).create());
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_is_subset_of_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertIsSubsetOf(INFO, array("Yoda", "Luke"), list("yoda", "lUKE"));
  }

  @Test
  void should_pass_if_actual_contains_duplicates_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertIsSubsetOf(INFO, array("Luke", "Luke"), list("LUke", "yoda"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertIsSubsetOf(INFO, array("Yoda", "Luke"), list("LUke", "LuKe", "yoda"));
  }

  @Test
  void should_fail_if_actual_is_not_subset_of_values_according_to_custom_comparison_strategy() {
    // GIVEN
    var actual = array("Yoda", "Luke");
    List<String> values = list("yoda", "C-3PO");
    List<String> extra = list("Luke");
    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertIsSubsetOf(INFO, actual, values));
    // THEN
    then(error).hasMessage(shouldBeSubsetOf(actual, values, extra, caseInsensitiveStringComparisonStrategy).create());
  }

}
