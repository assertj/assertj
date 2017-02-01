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
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.error.ShouldBeSorted.*;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link ObjectArrays#assertIsSorted(AssertionInfo, Object[])}</code>.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ObjectArrays_assertIsSorted_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Leia", "Luke", "Luke", "Vador", "Yoda");
  }

  @Test
  public void should_pass_if_actual_is_sorted_in_ascending_order() {
    arrays.assertIsSorted(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty_with_comparable_component_type() {
    arrays.assertIsSorted(someInfo(), new String[0]);
  }

  @Test
  public void should_pass_if_actual_is_empty_with_non_comparable_component_type() {
    arrays.assertIsSorted(someInfo(), array());
  }

  @Test
  public void should_pass_if_actual_contains_only_one_comparable_element() {
    actual = array("Obiwan");
    arrays.assertIsSorted(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertIsSorted(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_in_ascending_order() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Yoda", "Leia");
    try {
      arrays.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSorted(1, actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_only_one_element_with_non_comparable_component_type() {
    AssertionInfo info = someInfo();
    Object[] actual = array(new Object());
    try {
      arrays.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveMutuallyComparableElements(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_some_elements_with_non_comparable_component_type() {
    AssertionInfo info = someInfo();
    Object[] actual = array("bar", new Object(), "foo");
    try {
      arrays.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveMutuallyComparableElements(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_some_not_mutually_comparable_elements() {
    AssertionInfo info = someInfo();
    Object[] actual = new Object[] { "bar", new Integer(5), "foo" };
    try {
      arrays.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveMutuallyComparableElements(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_is_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    actual = array("leia", "Luke", "luke", "Vador", "Yoda");
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty_with_comparable_component_type_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), new String[0]);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), array());
  }

  @Test
  public void should_pass_if_actual_contains_only_one_comparable_element_according_to_custom_comparison_strategy() {
    actual = array("Obiwan");
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    arraysWithCustomComparisonStrategy.assertIsSorted(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_in_ascending_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = array("LUKE", "Yoda", "Leia");
    try {
      arraysWithCustomComparisonStrategy.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures)
          .failure(info, shouldBeSortedAccordingToGivenComparator(1, actual, comparatorForCustomComparisonStrategy()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_only_one_element_with_non_comparable_component_type_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] actual = array(new Object());
    try {
      arraysWithCustomComparisonStrategy.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldHaveComparableElementsAccordingToGivenComparator(actual, comparatorForCustomComparisonStrategy()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_some_elements_with_non_comparable_component_type_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] actual = array("bar", new Object(), "foo");
    try {
      arraysWithCustomComparisonStrategy.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldHaveComparableElementsAccordingToGivenComparator(actual, comparatorForCustomComparisonStrategy()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_some_not_mutually_comparable_elements_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] actual = new Object[] { "bar", new Integer(5), "foo" };
    try {
      arraysWithCustomComparisonStrategy.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldHaveComparableElementsAccordingToGivenComparator(actual, comparatorForCustomComparisonStrategy()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
