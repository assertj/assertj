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

import static org.assertj.core.error.ShouldBeSorted.*;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;


import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Lists;
import org.assertj.core.internal.ListsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Lists#assertIsSorted(AssertionInfo, List)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Lists_assertIsSorted_Test extends ListsBaseTest {

  private List<String> actual = newArrayList("Leia", "Luke", "Luke", "Vador", "Yoda");

  @Test
  public void should_pass_if_actual_is_sorted_in_ascending_order() {
    lists.assertIsSorted(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    actual = newArrayList("leia", "LUKE", "luke", "Vador", "Yoda");
    listsWithCaseInsensitiveComparisonStrategy.assertIsSorted(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    lists.assertIsSorted(someInfo(), newArrayList());
  }

  @Test
  public void should_pass_if_actual_contains_only_one_comparable_element() {
    lists.assertIsSorted(someInfo(), newArrayList("Obiwan"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    lists.assertIsSorted(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_in_ascending_order() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Luke", "Yoda", "Leia");
    try {
      lists.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSorted(1, actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Luke", "Yoda", "Leia");
    try {
      listsWithCaseInsensitiveComparisonStrategy.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSortedAccordingToGivenComparator(1, actual, comparisonStrategy.getComparator()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_only_one_non_comparable_element() {
    AssertionInfo info = someInfo();
    List<Object> actual = newArrayList(new Object());
    try {
      lists.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveMutuallyComparableElements(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_some_non_comparable_elements() {
    AssertionInfo info = someInfo();
    List<Object> actual = newArrayList("bar", new Object(), "foo");
    try {
      lists.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveMutuallyComparableElements(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_some_not_mutually_comparable_elements() {
    AssertionInfo info = someInfo();
    List<Object> actual = newArrayList();
    actual.add("bar");
    actual.add(new Integer(5));
    actual.add("foo");
    try {
      lists.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveMutuallyComparableElements(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
