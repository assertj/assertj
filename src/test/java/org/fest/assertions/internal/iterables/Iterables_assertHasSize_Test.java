/*
 * Created on Sep 26, 2010
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

import static org.fest.assertions.error.ShouldHaveSize.shouldHaveSize;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Lists.newArrayList;

import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Iterables;
import org.fest.assertions.internal.IterablesBaseTest;

/**
 * Tests for <code>{@link Iterables#assertHasSize(AssertionInfo, Collection, int)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Iterables_assertHasSize_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    iterables.assertHasSize(someInfo(), newArrayList("Luke", "Yoda"), 2);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertHasSize(someInfo(), null, 8);
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    Collection<String> actual = newArrayList("Yoda");
    try {
      iterables.assertHasSize(info, actual, 8);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveSize(actual, actual.size(), 8));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size_whatever_custom_comparison_strategy_is() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertHasSize(someInfo(), newArrayList("Luke", "Yoda"), 2);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    iterablesWithCaseInsensitiveComparisonStrategy.assertHasSize(someInfo(), null, 8);
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Collection<String> actual = newArrayList("Yoda");
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertHasSize(info, actual, 8);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveSize(actual, actual.size(), 8));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
