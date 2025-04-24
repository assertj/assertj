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

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.testkit.TestData.someIndex;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ObjectArrays_assertDoesNotContain_at_Index_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertDoesNotContain(INFO, null, "Yoda", someIndex()));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_does_not_contain_value_at_Index() {
    arrays.assertDoesNotContain(INFO, actual, "Yoda", atIndex(1));
  }

  @Test
  void should_pass_if_actual_is_empty() {
    arrays.assertDoesNotContain(INFO, emptyArray(), "Yoda", someIndex());
  }

  @Test
  void should_throw_error_if_Index_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertDoesNotContain(INFO, actual, "Yoda", null))
                                    .withMessage("Index should not be null");
  }

  @Test
  void should_pass_if_Index_is_out_of_bounds() {
    arrays.assertDoesNotContain(INFO, actual, "Yoda", atIndex(6));
  }

  @Test
  void should_fail_if_actual_contains_value_at_index() {
    // GIVEN
    Index index = atIndex(0);
    // WHEN
    expectAssertionError(() -> arrays.assertDoesNotContain(INFO, actual, "Yoda", index));
    // THEN
    verify(failures).failure(INFO, shouldNotContainAtIndex(actual, "Yoda", index));
  }

  @Test
  void should_pass_if_actual_does_not_contain_value_at_Index_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(INFO, actual, "YOda", atIndex(1));
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(INFO, emptyArray(), "YOda", someIndex());
  }

  @Test
  void should_throw_error_if_Index_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContain(INFO,
                                                                                                              actual,
                                                                                                              "YOda",
                                                                                                              null))
                                    .withMessage("Index should not be null");
  }

  @Test
  void should_pass_if_Index_is_out_of_bounds_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(INFO, actual, "YOda", atIndex(6));
  }

  @Test
  void should_fail_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    // GIVEN
    Index index = atIndex(0);
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotContain(INFO, actual, "YOda", index));
    // THEN
    verify(failures).failure(INFO, shouldNotContainAtIndex(actual, "YOda", index, caseInsensitiveStringComparisonStrategy));
  }
}
