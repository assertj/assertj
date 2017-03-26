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
package org.assertj.core.internal.shortarrays;

import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.test.ShortArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ShortArrays;
import org.assertj.core.internal.ShortArraysBaseTest;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link ShortArrays#assertIsSortedAccordingToComparator(AssertionInfo, short[], Comparator)}</code>
 * 
 * @author Joel Costigliola
 */
public class ShortArrays_assertIsSortedAccordingToComparator_Test extends ShortArraysBaseTest {

  private Comparator<Short> shortDescendingOrderComparator;
  private Comparator<Short> shortAscendingOrderComparator;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = new short[] { 4, 3, 2, 2, 1 };
    shortDescendingOrderComparator = new Comparator<Short>() {
      @Override
      public int compare(Short short1, Short short2) {
        return -short1.compareTo(short2);
      }
    };
    shortAscendingOrderComparator = new Comparator<Short>() {
      @Override
      public int compare(Short short1, Short short2) {
        return -short1.compareTo(short2);
      }
    };
  }

  @Test
  public void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), actual, shortDescendingOrderComparator);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), shortDescendingOrderComparator);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), shortAscendingOrderComparator);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertIsSortedAccordingToComparator(someInfo(), null, shortDescendingOrderComparator);
  }

  @Test
  public void should_fail_if_comparator_is_null() {
    thrown.expectNullPointerException();
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    actual = new short[] { 3, 2, 1, 9 };
    try {
      arrays.assertIsSortedAccordingToComparator(info, actual, shortDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSortedAccordingToGivenComparator(2, actual, shortDescendingOrderComparator));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
