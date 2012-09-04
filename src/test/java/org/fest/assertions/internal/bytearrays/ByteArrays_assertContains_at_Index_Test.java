/*
 * Created on Dec 14, 2010
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
package org.fest.assertions.internal.bytearrays;

import static org.fest.assertions.data.Index.atIndex;
import static org.fest.assertions.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.fest.util.FailureMessages.*;
import static org.fest.assertions.test.ByteArrays.emptyArray;
import static org.fest.assertions.test.TestData.*;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Index;
import org.fest.assertions.internal.ByteArrays;
import org.fest.assertions.internal.ByteArraysBaseTest;

/**
 * Tests for <code>{@link ByteArrays#assertContains(AssertionInfo, byte[], byte, Index)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ByteArrays_assertContains_at_Index_Test extends ByteArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContains(someInfo(), null, (byte) 8, someIndex());
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    thrown.expectAssertionError(actualIsEmpty());
    arrays.assertContains(someInfo(), emptyArray(), (byte) 8, someIndex());
  }

  @Test
  public void should_throw_error_if_Index_is_null() {
    thrown.expectNullPointerException("Index should not be null");
    arrays.assertContains(someInfo(), actual, (byte) 8, null);
  }

  @Test
  public void should_throw_error_if_Index_is_out_of_bounds() {
    thrown.expectIndexOutOfBoundsException("Index should be between <0> and <2> (inclusive,) but was <6>");
    arrays.assertContains(someInfo(), actual, (byte) 8, atIndex(6));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_value_at_index() {
    AssertionInfo info = someInfo();
    byte value = 6;
    Index index = atIndex(1);
    try {
      arrays.assertContains(info, actual, value, index);
    } catch (AssertionError e) {
      byte found = 8;
      verify(failures).failure(info, shouldContainAtIndex(actual, value, index, found));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_value_at_index() {
    arrays.assertContains(someInfo(), actual, (byte) 8, atIndex(1));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), null, (byte) -8, someIndex());
  }

  @Test
  public void should_fail_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsEmpty());
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), emptyArray(), (byte) -8, someIndex());
  }

  @Test
  public void should_throw_error_if_Index_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException("Index should not be null");
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, (byte) -8, null);
  }

  @Test
  public void should_throw_error_if_Index_is_out_of_bounds_whatever_custom_comparison_strategy_is() {
    thrown.expectIndexOutOfBoundsException("Index should be between <0> and <2> (inclusive,) but was <6>");
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, (byte) -8, atIndex(6));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_value_at_index_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    byte value = 6;
    Index index = atIndex(1);
    try {
      arraysWithCustomComparisonStrategy.assertContains(info, actual, value, index);
    } catch (AssertionError e) {
      byte found = 8;
      verify(failures).failure(info, shouldContainAtIndex(actual, value, index, found, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, (byte) -8, atIndex(1));
  }
}
