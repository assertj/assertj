/*
 * Created on Sep 30, 2010
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
package org.fest.assertions.internal.iterables;

import static org.fest.assertions.error.ShouldNotContainNull.shouldNotContainNull;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Lists.newArrayList;

import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Iterables;
import org.fest.assertions.internal.IterablesBaseTest;

/**
 * Tests for <code>{@link Iterables#assertDoesNotContainNull(AssertionInfo, Collection)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Iterables_assertDoesNotContainNull_Test extends IterablesBaseTest {

  private List<String> actual = newArrayList("Luke", "Yoda");

  @Test
  public void should_pass_if_actual_does_not_contain_null() {
    iterables.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    actual = newArrayList();
    iterables.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertDoesNotContainNull(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_contains_null() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Luke", "Yoda", null);
    try {
      iterables.assertDoesNotContainNull(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainNull(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_does_not_contain_null_whatever_custom_comparison_strategy_is() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    actual = newArrayList();
    iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainNull(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_contains_null_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Luke", "Yoda", null);
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainNull(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainNull(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
