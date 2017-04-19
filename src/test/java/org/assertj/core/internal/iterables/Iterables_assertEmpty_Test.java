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

import static java.util.Collections.emptyList;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Iterables#assertEmpty(AssertionInfo, Collection)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Iterables_assertEmpty_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_actual_is_empty() {
    iterables.assertEmpty(someInfo(), emptyList());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertEmpty(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_has_elements() {
    AssertionInfo info = someInfo();
    Collection<String> actual = newArrayList("Yoda");
    try {
      iterables.assertEmpty(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEmpty(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertEmpty(someInfo(), emptyList());
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    iterablesWithCaseInsensitiveComparisonStrategy.assertEmpty(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_has_elements_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    Collection<String> actual = newArrayList("Yoda");
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertEmpty(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEmpty(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
