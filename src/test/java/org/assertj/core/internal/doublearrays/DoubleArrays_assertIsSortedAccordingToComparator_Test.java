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
package org.assertj.core.internal.doublearrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.test.DoubleArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Comparator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.DoubleArrays;
import org.assertj.core.internal.DoubleArraysBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link DoubleArrays#assertIsSortedAccordingToComparator(AssertionInfo, double[], Comparator)}</code>
 * 
 * @author Joel Costigliola
 */
class DoubleArrays_assertIsSortedAccordingToComparator_Test extends DoubleArraysBaseTest {

  private Comparator<Double> doubleDescendingOrderComparator;
  private Comparator<Double> doubleSquareComparator;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = new double[] { 4.0, 3.0, 2.0, 2.0, 1.0 };
    doubleDescendingOrderComparator = (double1, double2) -> -double1.compareTo(double2);
    doubleSquareComparator = (double1,
                              double2) -> new Double(double1 * double1).compareTo(new Double(double2 * double2));
  }

  @Test
  void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), actual, doubleDescendingOrderComparator);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), doubleDescendingOrderComparator);
    arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), doubleSquareComparator);
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertIsSortedAccordingToComparator(someInfo(), null,
                                                                                                                doubleDescendingOrderComparator))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_comparator_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertIsSortedAccordingToComparator(someInfo(), emptyArray(), null));
  }

  @Test
  void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    actual = new double[] { 3.0, 2.0, 1.0, 9.0 };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertIsSortedAccordingToComparator(someInfo(),
                                                                                                                actual,
                                                                                                                doubleDescendingOrderComparator))
                                                   .withMessage(shouldBeSortedAccordingToGivenComparator(2, actual,
                                                                                                         doubleDescendingOrderComparator).create());
  }

}
