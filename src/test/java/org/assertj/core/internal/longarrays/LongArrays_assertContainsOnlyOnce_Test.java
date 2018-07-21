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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.longarrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.LongArrays.arrayOf;
import static org.assertj.core.test.LongArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.LongArrays;
import org.assertj.core.internal.LongArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link LongArrays#assertContainsOnlyOnce(AssertionInfo, long[], long[])}</code>.
 * 
 * @author William Delanoue
 */
public class LongArrays_assertContainsOnlyOnce_Test extends LongArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_only() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, 8, 10));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(10, 8, 6));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_only_more_than_once() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, -8, 10, -6, -8, 10, -8, 6);
    long[] expected = { 6, -8, 20 };
    try {
      arrays.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet(20L), newLinkedHashSet(6L, -8L)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_even_if_duplicated() {
    arrays.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, 8, 10, 6, 8, 10));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual = emptyArray();
    arrays.assertContainsOnlyOnce(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsOnlyOnce(someInfo(), actual, emptyArray()));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsOnlyOnce(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsOnlyOnce(someInfo(), null, arrayOf(8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only() {
    AssertionInfo info = someInfo();
    long[] expected = { 6, 8, 20 };
    try {
      arrays.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((long) 20), newLinkedHashSet()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, -8, 10));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_in_different_order_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf(10, -8, 6));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_only_more_than_once_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = arrayOf(6, -8, 10, -6, -8, 10, -8);
    long[] expected = { 6, -8, 20 };
    try {
      arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((long) 20), newLinkedHashSet((long) 6, (long) -8),
              absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, arrayOf(6, 8, 10, 6, -8, 10));
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, emptyArray()));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(),
                                                                                                                actual,
                                                                                                                null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(someInfo(), null, arrayOf(-8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    long[] expected = { 6, -8, 20 };
    try {
      arraysWithCustomComparisonStrategy.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet((long) 20), newLinkedHashSet(),
              absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
