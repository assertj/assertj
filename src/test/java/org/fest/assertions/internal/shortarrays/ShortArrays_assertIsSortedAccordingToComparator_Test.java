/*
 * Created on Nov 29, 2010
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
package org.fest.assertions.internal.shortarrays;

import static org.fest.assertions.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ShortArrays.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.ShortArrays;
import org.fest.assertions.internal.ShortArraysBaseTest;

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
      public int compare(Short short1, Short short2) {
        return -short1.compareTo(short2);
      }
    };
    shortAscendingOrderComparator = new Comparator<Short>() {
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
    thrown.expect(NullPointerException.class);
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
