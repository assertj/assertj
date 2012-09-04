/*
 * Created on Dec 20, 2010
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

import static org.fest.assertions.data.Index.atIndex;
import static org.fest.assertions.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.DoubleArrays.emptyArray;
import static org.fest.assertions.test.TestData.*;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Index;
import org.fest.assertions.internal.DoubleArrays;
import org.fest.assertions.internal.DoubleArraysBaseTest;

/**
 * Tests for <code>{@link DoubleArrays#assertDoesNotContain(AssertionInfo, double[], double, Index)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class DoubleArrays_assertDoesNotContain_at_Index_Test extends DoubleArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertDoesNotContain(someInfo(), null, 8d, someIndex());
  }

  @Test
  public void should_pass_if_actual_does_not_contain_value_at_Index() {
    arrays.assertDoesNotContain(someInfo(), actual, 6d, atIndex(1));
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    arrays.assertDoesNotContain(someInfo(), emptyArray(), 8d, someIndex());
  }

  @Test
  public void should_throw_error_if_Index_is_null() {
    thrown.expectNullPointerException("Index should not be null");
    arrays.assertDoesNotContain(someInfo(), actual, 8d, null);
  }

  @Test
  public void should_pass_if_Index_is_out_of_bounds() {
    arrays.assertDoesNotContain(someInfo(), actual, 8d, atIndex(6));
  }

  @Test
  public void should_fail_if_actual_contains_value_at_index() {
    AssertionInfo info = someInfo();
    Index index = atIndex(0);
    try {
      arrays.assertDoesNotContain(info, actual, 6d, index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainAtIndex(actual, 6d, index));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), null, -8d, someIndex());
  }

  @Test
  public void should_pass_if_actual_does_not_contain_value_at_Index_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), actual, 6d, atIndex(1));
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), emptyArray(), -8d, someIndex());
  }

  @Test
  public void should_throw_error_if_Index_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException("Index should not be null");
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), actual, -8d, null);
  }

  @Test
  public void should_pass_if_Index_is_out_of_bounds_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), actual, -8d, atIndex(6));
  }

  @Test
  public void should_fail_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Index index = atIndex(0);
    try {
      arraysWithCustomComparisonStrategy.assertDoesNotContain(info, actual, 6d, index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainAtIndex(actual, 6d, index, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
