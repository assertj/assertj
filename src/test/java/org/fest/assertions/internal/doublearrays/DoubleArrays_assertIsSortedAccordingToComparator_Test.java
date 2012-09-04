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
package org.fest.assertions.internal.doublearrays;

import static org.fest.assertions.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.DoubleArrays.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.DoubleArrays;
import org.fest.assertions.internal.DoubleArraysBaseTest;

/**
 * Tests for <code>{@link DoubleArrays#assertIsSortedAccordingToComparator(AssertionInfo, double[], Comparator)}</code>
 * 
 * @author Joel Costigliola
 */
public class DoubleArrays_assertIsSortedAccordingToComparator_Test extends DoubleArraysBaseTest {

  private Comparator<Double> doubleDescendingOrderComparator;
  private Comparator<Double> doubleSquareComparator;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = new double[] { 4.0, 3.0, 2.0, 2.0, 1.0 };
    doubleDescendingOrderComparator = new Comparator<Double>() {
      public int compare(Double double1, Double double2) {
        return -double1.compareTo(double2);
      }
    };
    doubleSquareComparator = new Comparator<Double>() {
      public int compare(Double double1, Double double2) {
        return new Double(double1 * double1).compareTo(new Double(double2 * double2));
      }
    };
  }

  @Test
  public void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), actual, doubleDescendingOrderComparator);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), doubleDescendingOrderComparator);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), doubleSquareComparator);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertIsSortedAccordingToComparator(someInfo(), null, doubleDescendingOrderComparator);
  }

  @Test
  public void should_fail_if_comparator_is_null() {
    thrown.expect(NullPointerException.class);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    actual = new double[] { 3.0, 2.0, 1.0, 9.0 };
    try {
      arrays.assertIsSortedAccordingToComparator(info, actual, doubleDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSortedAccordingToGivenComparator(2, actual, doubleDescendingOrderComparator));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
