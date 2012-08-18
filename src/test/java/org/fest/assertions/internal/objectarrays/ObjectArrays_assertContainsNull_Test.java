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
package org.fest.assertions.internal.objectarrays;

import static org.fest.assertions.error.ShouldContainNull.shouldContainNull;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.ObjectArrays;
import org.fest.assertions.internal.ObjectArraysBaseTest;

/**
 * Tests for <code>{@link ObjectArrays#assertContainsNull(AssertionInfo, Object[])}</code>.
 * 
 * @author Joel Costigliola
 */
public class ObjectArrays_assertContainsNull_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Luke", "Yoda", null);
  }

  @Test
  public void should_pass_if_actual_contains_null() {
    arrays.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_contains_only_null_values() {
    actual = array((String) null, (String) null);
    arrays.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_contains_null_more_than_once() {
    actual = array("Luke", null, null);
    arrays.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsNull(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_null() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Yoda");
    try {
      arrays.assertContainsNull(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainNull(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_null_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_contains_only_null_values_according_to_custom_comparison_strategy() {
    actual = array((String) null, (String) null);
    arraysWithCustomComparisonStrategy.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_contains_null_more_than_once_according_to_custom_comparison_strategy() {
    actual = array("Luke", null, null);
    arraysWithCustomComparisonStrategy.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertContainsNull(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_null_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Yoda");
    try {
      arraysWithCustomComparisonStrategy.assertContainsNull(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainNull(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
