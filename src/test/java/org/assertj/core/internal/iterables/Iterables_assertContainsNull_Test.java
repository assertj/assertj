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
package org.assertj.core.internal.iterables;

import static org.assertj.core.error.ShouldContainNull.shouldContainNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Iterables#assertContainsNull(AssertionInfo, Collection)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Iterables_assertContainsNull_Test extends IterablesBaseTest {

  private List<String> actual = newArrayList("Luke", "Yoda", null);

  @Test
  public void should_pass_if_actual_contains_null() {
    iterables.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_contains_only_null_values() {
    actual = newArrayList(null, null);
    iterables.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_contains_null_more_than_once() {
    actual.add(null);
    iterables.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertContainsNull(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_null() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Luke", "Yoda");
    try {
      iterables.assertContainsNull(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainNull(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_null_whatever_custom_comparison_strategy_is() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_contains_only_null_values_whatever_custom_comparison_strategy_is() {
    actual = newArrayList(null, null);
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_contains_null_more_than_once_whatever_custom_comparison_strategy_is() {
    actual.add(null);
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsNull(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsNull(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_null_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Luke", "Yoda");
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsNull(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainNull(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
