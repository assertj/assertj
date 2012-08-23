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
package org.fest.assertions.internal.lists;

import static org.fest.assertions.error.ShouldBeSorted.*;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Lists.newArrayList;

import static org.mockito.Mockito.verify;

import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Lists;
import org.fest.assertions.internal.ListsBaseTest;

/**
 * Tests for <code>{@link Lists#assertIsSortedAccordingToComparator(AssertionInfo, List, Comparator)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Lists_assertIsSortedAccordingToComparator_Test extends ListsBaseTest {

  private static Comparator<String> stringDescendingOrderComparator = new Comparator<String>() {
    public int compare(String s1, String s2) {
      return -s1.compareTo(s2);
    }
  };

  private static Comparator<Object> comparator = new Comparator<Object>() {
    public int compare(Object o1, Object o2) {
      return o1.toString().compareTo(o2.toString());
    }
  };

  @Test
  public void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    lists.assertIsSortedAccordingToComparator(someInfo(), newArrayList("Yoda", "Vador", "Luke", "Leia", "Leia"),
        stringDescendingOrderComparator);
  }

  @Test
  public void should_pass_if_actual_is_sorted_according_to_given_comparator_whatever_custom_comparison_strategy_is() {
    listsWithCaseInsensitiveComparisonStrategy.assertIsSortedAccordingToComparator(someInfo(),
        newArrayList("Yoda", "Vador", "Luke", "Leia", "Leia"), stringDescendingOrderComparator);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    lists.assertIsSortedAccordingToComparator(someInfo(), newArrayList(), stringDescendingOrderComparator);
    lists.assertIsSortedAccordingToComparator(someInfo(), newArrayList(), comparator);
  }

  @Test
  public void should_pass_if_actual_contains_only_one_comparable_element() {
    lists.assertIsSortedAccordingToComparator(someInfo(), newArrayList("Obiwan"), comparator);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    lists.assertIsSortedAccordingToComparator(someInfo(), null, comparator);
  }

  @Test
  public void should_fail_if_comparator_is_null() {
    thrown.expect(NullPointerException.class);
    lists.assertIsSortedAccordingToComparator(someInfo(), newArrayList(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    List<String> actual = newArrayList("Yoda", "Vador", "Leia", "Leia", "Luke");
    try {
      lists.assertIsSortedAccordingToComparator(info, actual, stringDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSortedAccordingToGivenComparator(3, actual, stringDescendingOrderComparator));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_some_not_mutually_comparable_elements_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    List<Object> actual = newArrayList();
    actual.add("bar");
    actual.add(new Integer(5));
    actual.add("foo");
    try {
      lists.assertIsSortedAccordingToComparator(info, actual, stringDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldHaveComparableElementsAccordingToGivenComparator(actual, stringDescendingOrderComparator));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_one_element_only_not_comparable_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    List<Object> actual = newArrayList(new Object());
    try {
      lists.assertIsSortedAccordingToComparator(info, actual, stringDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldHaveComparableElementsAccordingToGivenComparator(actual, stringDescendingOrderComparator));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
