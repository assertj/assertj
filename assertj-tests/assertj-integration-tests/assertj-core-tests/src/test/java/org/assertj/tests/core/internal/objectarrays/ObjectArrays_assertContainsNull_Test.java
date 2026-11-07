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
import static org.assertj.core.error.ShouldContainNull.shouldContainNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.assertj.tests.core.testkit.TestData;
import org.junit.jupiter.api.Test;

class ObjectArrays_assertContainsNull_Test {

  private static final AssertionInfo INFO = TestData.someInfo();
  private final String[] actual = array("Luke", "Yoda", null);
  private final ObjectArrays arrays = new ObjectArrays(StandardComparisonStrategy.instance());
  private final ComparatorBasedComparisonStrategy caseInsensitiveStringComparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
  private final ObjectArrays arraysWithCustomComparisonStrategy = new ObjectArrays(caseInsensitiveStringComparisonStrategy);

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
    var error = expectAssertionError(() -> arrays.assertContainsNull(INFO, actual));
    // THEN
    then(error).hasMessage(shouldContainNull(actual).create());
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
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertContainsNull(INFO, actual));
    // THEN
    then(error).hasMessage(shouldContainNull(actual).create());
  }
}
