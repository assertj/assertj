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
package org.fest.assertions.internal.longarrays;

import static org.fest.assertions.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.LongArrays.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.LongArrays;
import org.fest.assertions.internal.LongArraysBaseTest;

/**
 * Tests for <code>{@link LongArrays#assertIsSortedAccordingToComparator(AssertionInfo, long[], Comparator)}</code>
 * 
 * @author Joel Costigliola
 */
public class LongArrays_assertIsSortedAccordingToComparator_Test extends LongArraysBaseTest {

  private Comparator<Long> longDescendingOrderComparator;
  private Comparator<Long> longSquareComparator;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = new long[] { 4L, 3L, 2L, 2L, 1L };
    longDescendingOrderComparator = new Comparator<Long>() {
      public int compare(Long long1, Long long2) {
        return -long1.compareTo(long2);
      }
    };
    longSquareComparator = new Comparator<Long>() {
      public int compare(Long long1, Long long2) {
        return new Long(long1 * long1).compareTo(new Long(long2 * long2));
      }
    };
  }

  @Test
  public void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), actual, longDescendingOrderComparator);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), longDescendingOrderComparator);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), longSquareComparator);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertIsSortedAccordingToComparator(someInfo(), null, longDescendingOrderComparator);
  }

  @Test
  public void should_fail_if_comparator_is_null() {
    thrown.expect(NullPointerException.class);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    actual = new long[] { 3L, 2L, 1L, 9L };
    try {
      arrays.assertIsSortedAccordingToComparator(info, actual, longDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSortedAccordingToGivenComparator(2, actual, longDescendingOrderComparator));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
