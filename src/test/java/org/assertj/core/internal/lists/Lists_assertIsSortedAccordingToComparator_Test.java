/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.lists;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.error.ShouldBeSorted.shouldHaveComparableElementsAccordingToGivenComparator;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Lists;
import org.assertj.core.internal.ListsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Lists#assertIsSortedAccordingToComparator(AssertionInfo, List, Comparator)}</code>.
 * 
 * @author Joel Costigliola
 */
class Lists_assertIsSortedAccordingToComparator_Test extends ListsBaseTest {

  private static Comparator<String> stringDescendingOrderComparator = (s1, s2) -> -s1.compareTo(s2);
  private static Comparator<Object> comparator = (o1, o2) -> o1.toString().compareTo(o2.toString());

  @Test
  void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    lists.assertIsSortedAccordingToComparator(someInfo(), newArrayList("Yoda", "Vador", "Luke", "Leia", "Leia"),
                                              stringDescendingOrderComparator);
  }

  @Test
  void should_pass_if_actual_is_sorted_according_to_given_comparator_whatever_custom_comparison_strategy_is() {
    listsWithCaseInsensitiveComparisonStrategy.assertIsSortedAccordingToComparator(someInfo(),
                                                                                   newArrayList("Yoda", "Vador", "Luke", "Leia",
                                                                                                "Leia"),
                                                                                   stringDescendingOrderComparator);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    lists.assertIsSortedAccordingToComparator(someInfo(), newArrayList(), stringDescendingOrderComparator);
    lists.assertIsSortedAccordingToComparator(someInfo(), newArrayList(), comparator);
  }

  @Test
  void should_pass_if_actual_contains_only_one_comparable_element() {
    lists.assertIsSortedAccordingToComparator(someInfo(), newArrayList("Obiwan"), comparator);
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> lists.assertIsSortedAccordingToComparator(someInfo(), null,
                                                                                                               comparator))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_comparator_is_null() {
    assertThatNullPointerException().isThrownBy(() -> lists.assertIsSortedAccordingToComparator(someInfo(), newArrayList(),
                                                                                                null));
  }

  @Test
  void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    List<String> actual = newArrayList("Yoda", "Vador", "Leia", "Leia", "Luke");

    Throwable error = catchThrowable(() -> lists.assertIsSortedAccordingToComparator(info, actual,
                                                                                     stringDescendingOrderComparator));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeSortedAccordingToGivenComparator(3, actual, stringDescendingOrderComparator));
  }

  @Test
  void should_fail_if_actual_has_some_not_mutually_comparable_elements_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    List<Object> actual = newArrayList();
    actual.add("bar");
    actual.add(new Integer(5));
    actual.add("foo");

    Throwable error = catchThrowable(() -> lists.assertIsSortedAccordingToComparator(info, actual,
                                                                                     stringDescendingOrderComparator));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldHaveComparableElementsAccordingToGivenComparator(actual, stringDescendingOrderComparator));
  }

  @Test
  void should_fail_if_actual_has_one_element_only_not_comparable_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    List<Object> actual = newArrayList(new Object());

    Throwable error = catchThrowable(() -> lists.assertIsSortedAccordingToComparator(info, actual,
                                                                                     stringDescendingOrderComparator));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info,
                             shouldHaveComparableElementsAccordingToGivenComparator(actual, stringDescendingOrderComparator));
  }

}
