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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainNull.shouldNotContainNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertDoesNotContainNull(AssertionInfo, Object[])}</code>.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class ObjectArrays_assertDoesNotContainNull_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Luke", "Yoda");
  }

  @Test
  void should_pass_if_actual_does_not_contain_null() {
    arrays.assertDoesNotContainNull(INFO, actual);
  }

  @Test
  void should_pass_if_actual_is_empty() {
    arrays.assertDoesNotContainNull(INFO, array());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotContainNull(INFO, null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_contains_null() {
    // GIVEN
    var actual = array("Luke", "Yoda", null);
    // WHEN
    expectAssertionError(() -> arrays.assertDoesNotContainNull(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldNotContainNull(actual));
  }

  @Test
  void should_pass_if_actual_does_not_contain_null_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainNull(INFO, actual);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainNull(INFO, array());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainNull(INFO, null));
    // THEN
    then(error).hasMessage(actualIsNull());

  }

  @Test
  void should_fail_if_actual_contains_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    var actual = array("Luke", "Yoda", null);
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainNull(INFO, actual));
    // THEN
    verify(failures).failure(INFO, shouldNotContainNull(actual));
  }
}
