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
package org.fest.assertions.internal.booleanarrays;

import static org.fest.assertions.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.BooleanArrays.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.BooleanArrays;
import org.fest.assertions.internal.BooleanArraysBaseTest;

/**
 * Tests for <code>{@link BooleanArrays#assertIsSortedAccordingToComparator(AssertionInfo, boolean[], Comparator)}</code>
 * 
 * @author Joel Costigliola
 */
public class BooleanArrays_assertIsSortedAccordingToComparator_Test extends BooleanArraysBaseTest {

  private Comparator<Boolean> booleanDescendingOrderComparator;
  private Comparator<Boolean> booleanAscendingOrderComparator;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = new boolean[] { true, true, false, false };
    booleanDescendingOrderComparator = new Comparator<Boolean>() {
      public int compare(Boolean boolean1, Boolean boolean2) {
        return -boolean1.compareTo(boolean2);
      }
    };
    booleanAscendingOrderComparator = new Comparator<Boolean>() {
      public int compare(Boolean boolean1, Boolean boolean2) {
        return boolean1.compareTo(boolean2);
      }
    };
  }

  @Test
  public void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), actual, booleanDescendingOrderComparator);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), booleanDescendingOrderComparator);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), booleanAscendingOrderComparator);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertIsSortedAccordingToComparator(someInfo(), null, booleanDescendingOrderComparator);
  }

  @Test
  public void should_fail_if_comparator_is_null() {
    thrown.expect(NullPointerException.class);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    actual = new boolean[] { true, true, false, true };
    try {
      arrays.assertIsSortedAccordingToComparator(info, actual, booleanDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSortedAccordingToGivenComparator(2, actual, booleanDescendingOrderComparator));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
