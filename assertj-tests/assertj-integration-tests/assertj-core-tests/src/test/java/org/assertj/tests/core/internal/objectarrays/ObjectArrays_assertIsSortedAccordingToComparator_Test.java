/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class ObjectArrays_assertIsSortedAccordingToComparator_Test extends ObjectArraysBaseTest {

  private Comparator<String> byDescendingOrderComparator;
  private Comparator<Object> comparator;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = array("Yoda", "Vador", "Luke", "Luke", "Leia");
    byDescendingOrderComparator = (s1, s2) -> -s1.compareTo(s2);
    comparator = Comparator.comparing(Object::toString);
  }

  @Test
  void should_pass_if_actual_is_sorted_according_to_given_comparator() {
    arrays.assertIsSortedAccordingToComparator(INFO, actual, byDescendingOrderComparator);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_given_comparator_is() {
    arrays.assertIsSortedAccordingToComparator(INFO, new String[0], byDescendingOrderComparator);
    arrays.assertIsSortedAccordingToComparator(INFO, new String[0], comparator);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertIsSortedAccordingToComparator(INFO, null, comparator));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_comparator_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertIsSortedAccordingToComparator(INFO, array(), null));
  }

  @Test
  void should_fail_if_actual_is_not_sorted_according_to_given_comparator() {
    // GIVEN
    var actual = array("Yoda", "Vador", "Leia", "Leia", "Luke");
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertIsSortedAccordingToComparator(INFO, actual,
                                                                                                 byDescendingOrderComparator));
    // THEN
    then(error).hasMessage(shouldBeSortedAccordingToGivenComparator(3, actual, byDescendingOrderComparator).create());
  }
}
