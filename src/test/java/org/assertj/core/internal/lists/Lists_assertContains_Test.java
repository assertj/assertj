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
package org.assertj.core.internal.lists;

import static java.util.Collections.emptyList;

import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.*;
import static org.assertj.core.util.Lists.newArrayList;


import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.Lists;
import org.assertj.core.internal.ListsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Lists#assertContains(AssertionInfo, List, Object, Index)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Lists_assertContains_Test extends ListsBaseTest {

  private static List<String> actual = newArrayList("Yoda", "Luke", "Leia");

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    lists.assertContains(someInfo(), null, "Yoda", someIndex());
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    thrown.expectAssertionError(actualIsEmpty());
    lists.assertContains(someInfo(), emptyList(), "Yoda", someIndex());
  }

  @Test
  public void should_throw_error_if_Index_is_null() {
    thrown.expectNullPointerException("Index should not be null");
    lists.assertContains(someInfo(), actual, "Yoda", null);
  }

  @Test
  public void should_throw_error_if_Index_is_out_of_bounds() {
    thrown.expectIndexOutOfBoundsException("Index should be between <0> and <2> (inclusive,) but was:%n <6>");
    lists.assertContains(someInfo(), actual, "Yoda", atIndex(6));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_value_at_index() {
    AssertionInfo info = someInfo();
    Index index = atIndex(1);
    try {
      lists.assertContains(info, actual, "Han", index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainAtIndex(actual, "Han", index, "Luke"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_value_at_index() {
    lists.assertContains(someInfo(), actual, "Luke", atIndex(1));
  }

  @Test
  public void should_pass_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    listsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, "Luke", atIndex(1));
    listsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, "luke", atIndex(1));
    listsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, "LUKE", atIndex(1));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_value_at_index_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Index index = atIndex(1);
    try {
      listsWithCaseInsensitiveComparisonStrategy.assertContains(info, actual, "Han", index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainAtIndex(actual, "Han", index, "Luke", comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
