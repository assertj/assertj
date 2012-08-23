/*
 * Created on Apr 27, 2010
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
 * Copyright @2010-2015 the original author or authors.
 */
package org.fest.assertions.internal.iterables;

import static org.fest.assertions.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Iterables;
import org.fest.assertions.internal.IterablesBaseTest;

import org.junit.Test;

/**
 * Tests for <code>{@link Iterables#assertHasSameSizeAs(AssertionInfo, Iterable, Iterable)}</code>.
 * 
 * @author Nicolas François
 */
public class Iterables_assertHasSameSizeAs_with_Iterable_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    iterables.assertHasSameSizeAs(someInfo(), newArrayList("Yoda", "Luke"), newArrayList("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertHasSameSizeAs(someInfo(), null, newArrayList("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_other_is_null() {
    thrown.expectNullPointerException("The iterable to look for should not be null");
    Iterable<?> other = null;
    iterables.assertHasSameSizeAs(someInfo(), newArrayList("Yoda", "Luke"), other);
  }

  @Test
  public void should_fail_if_actual_size_is_not_equal_to_other_size() {
    AssertionInfo info = someInfo();
    Collection<String> actual = newArrayList("Yoda");
    Collection<String> other = newArrayList("Solo", "Luke", "Leia");
    try {
      iterables.assertHasSameSizeAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveSameSizeAs(actual, actual.size(), other.size()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_has_same_size_as_other_whatever_custom_comparison_strategy_is() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertHasSameSizeAs(someInfo(), newArrayList("Luke", "Yoda"), newArrayList("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    iterablesWithCaseInsensitiveComparisonStrategy.assertHasSameSizeAs(someInfo(), null, newArrayList("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_other_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException("The iterable to look for should not be null");
    Iterable<?> other = null;
    iterables.assertHasSameSizeAs(someInfo(), newArrayList("Yoda", "Luke"), other);
  }

  @Test
  public void should_fail_if_actual_size_is_not_equal_to_other_size_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Collection<String> actual = newArrayList("Yoda");
    Collection<String> other = newArrayList("Solo", "Luke", "Leia");
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertHasSameSizeAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveSameSizeAs(actual, actual.size(), other.size()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
