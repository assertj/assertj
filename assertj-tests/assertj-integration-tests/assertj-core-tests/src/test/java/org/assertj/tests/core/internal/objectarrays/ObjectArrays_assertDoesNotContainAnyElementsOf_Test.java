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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;

class ObjectArrays_assertDoesNotContainAnyElementsOf_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable() {
    arrays.assertDoesNotContainAnyElementsOf(INFO, actual, list("Han"));
  }

  @Test
  void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable_even_if_duplicated() {
    arrays.assertDoesNotContainAnyElementsOf(INFO, actual, list("Han", "Han", "Anakin"));
  }

  @Test
  void should_throw_error_if_given_iterable_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> arrays.assertDoesNotContainAnyElementsOf(INFO, actual,
                                                                                                   List.<String> of()))
                                        .withMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_given_iterable_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertDoesNotContainAnyElementsOf(INFO, actual, null))
                                    .withMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotContainAnyElementsOf(INFO, null, list("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_contains_one_element_of_given_iterable() {
    // GIVEN
    List<String> list = list("Vador", "Yoda", "Han");
    // WHEN
    expectAssertionError(() -> arrays.assertDoesNotContainAnyElementsOf(INFO, actual, list));
    // THEN
    verify(failures).failure(INFO, shouldNotContain(actual, list.toArray(), newLinkedHashSet("Yoda")));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainAnyElementsOf(INFO, actual, list("Han"));
  }

  @Test
  void should_pass_if_actual_does_not_contain_any_elements_of_given_iterable_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainAnyElementsOf(INFO, actual,
                                                                         list("Han", "Han", "Anakin"));
  }

  @Test
  void should_fail_if_actual_contains_one_element_of_given_iterable_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> expected = list("LuKe", "YODA", "Han");
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainAnyElementsOf(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldNotContain(actual, expected.toArray(), newLinkedHashSet("LuKe", "YODA"),
                                                    caseInsensitiveStringComparisonStrategy));
  }
}
