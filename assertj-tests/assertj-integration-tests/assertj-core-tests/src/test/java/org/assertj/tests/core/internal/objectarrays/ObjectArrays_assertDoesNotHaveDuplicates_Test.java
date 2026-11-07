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
import static org.assertj.core.error.ShouldNotHaveDuplicates.shouldNotHaveDuplicates;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.assertj.tests.core.testkit.TestData;
import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 */
class ObjectArrays_assertDoesNotHaveDuplicates_Test {

  private static final AssertionInfo INFO = TestData.someInfo();
  private final String[] actual = array("Luke", "Yoda", "Leia");
  private final ObjectArrays arrays = new ObjectArrays(StandardComparisonStrategy.instance());
  private final ComparatorBasedComparisonStrategy caseInsensitiveStringComparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
  private final ObjectArrays arraysWithCustomComparisonStrategy = new ObjectArrays(caseInsensitiveStringComparisonStrategy);

  @Test
  void should_pass_if_actual_does_not_have_duplicates() {
    arrays.assertDoesNotHaveDuplicates(INFO, array("Luke", "Yoda"));
  }

  @Test
  void should_pass_if_actual_is_empty() {
    arrays.assertDoesNotHaveDuplicates(INFO, emptyArray());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotHaveDuplicates(INFO, null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_contains_duplicates() {
    // GIVEN
    var actual = array("Luke", "Yoda", "Luke", "Yoda");
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotHaveDuplicates(INFO, actual));
    // THEN
    then(error).hasMessage(shouldNotHaveDuplicates(actual, newLinkedHashSet("Luke", "Yoda")).create());
  }

  @Test
  void should_pass_if_actual_does_not_have_duplicates_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotHaveDuplicates(INFO, actual);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotHaveDuplicates(INFO, emptyArray());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotHaveDuplicates(INFO, null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_contains_duplicates_according_to_custom_comparison_strategy() {
    // GIVEN
    var actual = array("LUKE", "Yoda", "Luke", "Yoda");
    // WHEN
    var error = expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotHaveDuplicates(INFO, actual));
    // THEN
    then(error).hasMessage(shouldNotHaveDuplicates(actual, newLinkedHashSet("Luke", "Yoda"),
                                                   caseInsensitiveStringComparisonStrategy).create());
  }
}
