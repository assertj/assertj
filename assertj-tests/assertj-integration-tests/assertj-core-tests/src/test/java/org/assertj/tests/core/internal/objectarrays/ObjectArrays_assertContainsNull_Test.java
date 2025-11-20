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

import static org.assertj.core.error.ShouldContainNull.shouldContainNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class ObjectArrays_assertContainsNull_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Luke", "Yoda", null);
  }

  @Test
  void should_pass_if_actual_contains_null() {
    arrays.assertContainsNull(INFO, actual);
  }

  @Test
  void should_pass_if_actual_contains_only_null_values() {
    arrays.assertContainsNull(INFO, array(null, null));
  }

  @Test
  void should_pass_if_actual_contains_null_more_than_once() {
    arrays.assertContainsNull(INFO, array("Luke", null, null));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatAssertionErrorIsThrownBy(() -> arrays.assertContainsNull(INFO, null)).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_null() {
    // GIVEN
    var actual = array("Luke", "Yoda");
    // WHEN
    expectAssertionError(() -> arrays.assertContainsNull(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldContainNull(actual));
  }

  @Test
  void should_pass_if_actual_contains_null_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertContainsNull(INFO, actual);
  }

  @Test
  void should_pass_if_actual_contains_only_null_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsNull(INFO, array(null, null));
  }

  @Test
  void should_pass_if_actual_contains_null_more_than_once_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsNull(INFO, array("Luke", null, null));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatAssertionErrorIsThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsNull(INFO, null))
                                                                                                               .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    var actual = array("Luke", "Yoda");
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsNull(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldContainNull(actual));
  }
}
