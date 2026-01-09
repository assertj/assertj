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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsEmpty;
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
class ObjectArrays_assertContains_at_Index_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertContains(INFO, null, "Yoda", someIndex()));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertContains(INFO, emptyArray(), "Yoda", someIndex()));
    // THEN
    then(error).hasMessage(actualIsEmpty());
  }

  @Test
  void should_throw_error_if_Index_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContains(INFO, actual, "Yoda", null))
                                    .withMessage("Index should not be null");
  }

  @Test
  void should_throw_error_if_Index_is_out_of_bounds() {
    assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> arrays.assertContains(INFO,
                                                                                                      actual, "Yoda",
                                                                                                      atIndex(6)))
                                                              .withMessageContaining("Index should be between <0> and <2> (inclusive) but was:%n <6>".formatted());
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_at_index() {
    // GIVEN
    Index index = atIndex(1);
    // WHEN
    expectAssertionError(() -> arrays.assertContains(INFO, actual, "Han", index));
    // THEN
    verify(failures).failure(INFO, shouldContainAtIndex(actual, "Han", index, "Luke"));
  }

  @Test
  void should_pass_if_actual_contains_value_at_index() {
    arrays.assertContains(INFO, actual, "Luke", atIndex(1));
  }

  @Test
  void should_throw_error_if_Index_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(INFO,
                                                                                                        actual, "YODa",
                                                                                                        null))
                                    .withMessage("Index should not be null");
  }

  @Test
  void should_throw_error_if_Index_is_out_of_bounds_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(INFO,
                                                                                                                                  actual,
                                                                                                                                  "YodA",
                                                                                                                                  atIndex(6)))
                                                              .withMessageContaining("Index should be between <0> and <2> (inclusive) but was:%n <6>".formatted());
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_at_index_according_to_custom_comparison_strategy() {
    // GIVEN
    Index index = atIndex(1);
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContains(INFO, actual, "Han", index));
    // THEN
    verify(failures).failure(INFO, shouldContainAtIndex(actual, "Han", index, "Luke", caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_pass_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(INFO, actual, "LUKe", atIndex(1));
  }
}
