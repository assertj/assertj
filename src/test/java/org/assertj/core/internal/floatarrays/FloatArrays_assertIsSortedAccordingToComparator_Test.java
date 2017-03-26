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
package org.assertj.core.internal.floatarrays;

import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.test.FloatArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FloatArrays;
import org.assertj.core.internal.FloatArraysBaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link FloatArrays#assertIsSortedAccordingToComparator(AssertionInfo, float[], Comparator)}</code>
 * 
 * @author Joel Costigliola
 */
public class FloatArrays_assertIsSortedAccordingToComparator_Test extends FloatArraysBaseTest {

  private Comparator<Float> floatDescendingOrderComparator;
  private Comparator<Float> floatSquareComparator;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = new float[] { 4.0f, 3.0f, 2.0f, 2.0f, 1.0f };
    floatDescendingOrderComparator = new Comparator<Float>() {
      @Override
      public int compare(Float float1, Float float2) {
        return -float1.compareTo(float2);
      }
    };
    floatSquareComparator = new Comparator<Float>() {
      @Override
      public int compare(Float float1, Float float2) {
        return new Float(float1 * float1).compareTo(new Float(float2 * float2));
      }
    };
  }

  @Test
  public void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), actual, floatDescendingOrderComparator);
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), floatDescendingOrderComparator);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), floatSquareComparator);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertIsSortedAccordingToComparator(someInfo(), null, floatDescendingOrderComparator);
  }

  @Test
  public void should_fail_if_comparator_is_null() {
    thrown.expectNullPointerException();
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    actual = new float[] { 3.0f, 2.0f, 1.0f, 9.0f };
    try {
      arrays.assertIsSortedAccordingToComparator(info, actual, floatDescendingOrderComparator);
    } catch (AssertionError e) {
      verify(failures).failure(info,
                               shouldBeSortedAccordingToGivenComparator(2, actual, floatDescendingOrderComparator));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
