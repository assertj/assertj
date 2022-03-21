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
package org.assertj.core.internal.longarrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.test.LongArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.LongArrays;
import org.assertj.core.internal.LongArraysBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link LongArrays#assertIsSortedAccordingToComparator(AssertionInfo, long[], Comparator)}</code>
 * 
 * @author Joel Costigliola
 */
class LongArrays_assertIsSortedAccordingToComparator_Test extends LongArraysBaseTest {

  private Comparator<Long> longDescendingOrderComparator;
  private Comparator<Long> longSquareComparator;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = new long[] { 4L, 3L, 2L, 2L, 1L };
    longDescendingOrderComparator = (long1, long2) -> -long1.compareTo(long2);
    longSquareComparator = (long1, long2) -> new Long(long1 * long1).compareTo(new Long(long2 * long2));
  }

  @Test
  void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), actual, longDescendingOrderComparator);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), longDescendingOrderComparator);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), longSquareComparator);
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertIsSortedAccordingToComparator(someInfo(), null,
                                                                                                                longDescendingOrderComparator))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_comparator_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), null));
  }

  @Test
  void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    AssertionInfo info = someInfo();
    actual = new long[] { 3L, 2L, 1L, 9L };

    Throwable error = catchThrowable(() -> arrays.assertIsSortedAccordingToComparator(info, actual,
                                                                                      longDescendingOrderComparator));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeSortedAccordingToGivenComparator(2, actual, longDescendingOrderComparator));
  }

}
