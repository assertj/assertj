/*
 * Created on Nov 29, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.doublearrays;

import static org.fest.assertions.error.ShouldBeSorted.*;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.DoubleArrays.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.DoubleArrays;
import org.fest.assertions.internal.DoubleArraysBaseTest;

/**
 * Tests for <code>{@link DoubleArrays#assertIsSorted(AssertionInfo, Object[])}</code>.
 * 
 * @author Joel Costigliola
 */
public class DoubleArrays_assertIsSorted_Test extends DoubleArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = arrayOf(1.0, 2.0, 3.0, 4.0, 4.0);
  }

  @Test
  public void should_pass_if_actual_is_sorted_in_ascending_order() {
    arrays.assertIsSorted(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    arrays.assertIsSorted(someInfo(), emptyArray());
  }

  @Test
  public void should_pass_if_actual_contains_only_one_element() {
    arrays.assertIsSorted(someInfo(), arrayOf(1.0));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertIsSorted(someInfo(), (double[]) null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_in_ascending_order() {
    AssertionInfo info = someInfo();
    actual = arrayOf(1.0, 3.0, 2.0);
    try {
      arrays.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSorted(1, actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_is_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    actual = arrayOf(1.0, -2.0, 3.0, -4.0, 4.0);
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), emptyArray());
  }

  @Test
  public void should_pass_if_actual_contains_only_one_element_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), arrayOf(1.0));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), (double[]) null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(1.0, 3.0, 2.0);
    try {
      arraysWithCustomComparisonStrategy.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures)
          .failure(info, shouldBeSortedAccordingToGivenComparator(1, actual, comparatorForCustomComparisonStrategy()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
