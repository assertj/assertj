/*
 * Created on Dec 2, 2010
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
package org.fest.assertions.internal.lists;

import static java.util.Collections.emptyList;

import static org.fest.assertions.data.Index.atIndex;
import static org.fest.assertions.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.*;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Lists.newArrayList;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Index;
import org.fest.assertions.internal.Lists;
import org.fest.assertions.internal.ListsBaseTest;

/**
 * Tests for <code>{@link Lists#assertDoesNotContain(AssertionInfo, List, Object, Index)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Lists_assertDoesNotContain_Test extends ListsBaseTest {

  private static List<String> actual = newArrayList("Yoda", "Luke", "Leia");

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    lists.assertDoesNotContain(someInfo(), null, "Yoda", someIndex());
  }

  @Test
  public void should_pass_if_actual_does_not_contain_value_at_index() {
    lists.assertDoesNotContain(someInfo(), actual, "Yoda", atIndex(1));
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    lists.assertDoesNotContain(someInfo(), emptyList(), "Yoda", someIndex());
  }

  @Test
  public void should_throw_error_if_index_is_null() {
    thrown.expectNullPointerException("Index should not be null");
    lists.assertDoesNotContain(someInfo(), actual, "Yoda", null);
  }

  @Test
  public void should_pass_if_index_is_out_of_bounds() {
    lists.assertDoesNotContain(someInfo(), actual, "Yoda", atIndex(6));
  }

  @Test
  public void should_fail_if_actual_contains_value_at_index() {
    AssertionInfo info = someInfo();
    Index index = atIndex(0);
    try {
      lists.assertDoesNotContain(info, actual, "Yoda", index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainAtIndex(actual, "Yoda", index));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_does_not_contain_value_at_index_according_to_custom_comparison_strategy() {
    listsWithCaseInsensitiveComparisonStrategy.assertDoesNotContain(someInfo(), actual, "Yoda", atIndex(1));
  }

  @Test
  public void should_fail_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Index index = atIndex(0);
    try {
      listsWithCaseInsensitiveComparisonStrategy.assertDoesNotContain(info, actual, "YODA", index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainAtIndex(actual, "YODA", index, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
