/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.longarrays;

import static org.assertj.core.error.ShouldNotHaveDuplicates.shouldNotHaveDuplicates;
import static org.assertj.core.test.LongArrays.*;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.LongArrays;
import org.assertj.core.internal.LongArraysBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link LongArrays#assertDoesNotHaveDuplicates(AssertionInfo, long[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class LongArrays_assertDoesNotHaveDuplicates_Test extends LongArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = arrayOf(6L, 8L);
  }

  @Test
  public void should_pass_if_actual_does_not_have_duplicates() {
    arrays.assertDoesNotHaveDuplicates(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    arrays.assertDoesNotHaveDuplicates(someInfo(), emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertDoesNotHaveDuplicates(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_contains_duplicates() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6L, 8L, 6L, 8L);
    try {
      arrays.assertDoesNotHaveDuplicates(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotHaveDuplicates(actual, newLinkedHashSet(6L, 8L)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_does_not_have_duplicates_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotHaveDuplicates(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotHaveDuplicates(someInfo(), emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertDoesNotHaveDuplicates(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6L, -8L, 6L, -8L);
    try {
      arraysWithCustomComparisonStrategy.assertDoesNotHaveDuplicates(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotHaveDuplicates(actual, newLinkedHashSet(6L, -8L), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
