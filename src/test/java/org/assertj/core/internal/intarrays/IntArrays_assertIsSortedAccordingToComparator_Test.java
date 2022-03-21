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
package org.assertj.core.internal.intarrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.test.IntArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.IntArrays;
import org.assertj.core.internal.IntArraysBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link IntArrays#assertIsSortedAccordingToComparator(AssertionInfo, int[], Comparator)}</code>
 * 
 * @author Joel Costigliola
 */
class IntArrays_assertIsSortedAccordingToComparator_Test extends IntArraysBaseTest {

  private Comparator<Integer> intDescendingOrderComparator;
  private Comparator<Integer> intSquareComparator;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = new int[] { 4, 3, 2, 2, 1 };
    intDescendingOrderComparator = (int1, int2) -> -int1.compareTo(int2);
    intSquareComparator = (int1, int2) -> new Integer(int1 * int1).compareTo(new Integer(int2 * int2));
  }

  @Test
  void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), actual, intDescendingOrderComparator);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), intDescendingOrderComparator);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), intSquareComparator);
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertIsSortedAccordingToComparator(someInfo(), null,
                                                                                                                intDescendingOrderComparator))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_comparator_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), null));
  }

  @Test
  void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    actual = new int[] { 3, 2, 1, 9 };

    Throwable error = catchThrowable(() -> arrays.assertIsSortedAccordingToComparator(info, actual,
                                                                                      intDescendingOrderComparator));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeSortedAccordingToGivenComparator(2, actual, intDescendingOrderComparator));
  }

}
