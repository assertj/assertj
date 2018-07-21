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
package org.assertj.core.internal.bytearrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.ByteArrays.*;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.ByteArraysBaseTest;
import org.assertj.core.test.IntArrays;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link ByteArrays#assertStartsWith(AssertionInfo, byte[], int[])}</code>.
 */
public class ByteArrays_assertStartsWith_with_Integer_Arguments_Test extends ByteArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = arrayOf(6, 8, 10, 12);
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertStartsWith(someInfo(), actual, (int[]) null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual = emptyArray();
    arrays.assertStartsWith(someInfo(), actual, IntArrays.emptyArray());
  }
  
  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertStartsWith(someInfo(), actual, IntArrays.emptyArray()));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertStartsWith(someInfo(), null, IntArrays.arrayOf(8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    try {
      arrays.assertStartsWith(info, actual, IntArrays.arrayOf(6, 8, 10, 12, 20, 22));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldStartWith(actual, arrayOf(6, 8, 10, 12, 20, 22)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_start_with_sequence() {
    AssertionInfo info = someInfo();
    try {
      arrays.assertStartsWith(info, actual, IntArrays.arrayOf(8, 10));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldStartWith(actual, arrayOf(8, 10)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    AssertionInfo info = someInfo();
    try {
      arrays.assertStartsWith(info, actual, IntArrays.arrayOf(6, 20));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldStartWith(actual, arrayOf(6, 20)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_starts_with_sequence() {
    arrays.assertStartsWith(someInfo(), actual, IntArrays.arrayOf(6, 8, 10));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertStartsWith(someInfo(), actual, IntArrays.arrayOf(6, 8, 10, 12));
  }

  @Test
  public void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertStartsWith(someInfo(),
                                                                                                                               actual,
                                                                                                                               (int[]) null))
                                                         .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertStartsWith(someInfo(), actual, IntArrays.emptyArray()));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertStartsWith(someInfo(), null, IntArrays.arrayOf(-8)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      arraysWithCustomComparisonStrategy.assertStartsWith(info, actual, IntArrays.arrayOf(6, -8, 10, 12, 20, 22));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldStartWith(actual, arrayOf(6, -8, 10, 12, 20, 22), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_start_with_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      arraysWithCustomComparisonStrategy.assertStartsWith(info, actual, IntArrays.arrayOf(-8, 10));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldStartWith(actual, arrayOf(-8, 10), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_starts_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      arraysWithCustomComparisonStrategy.assertStartsWith(info, actual, IntArrays.arrayOf(6, 20));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldStartWith(actual, arrayOf(6, 20), absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_starts_with_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertStartsWith(someInfo(), actual, IntArrays.arrayOf(6, -8, 10));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertStartsWith(someInfo(), actual, IntArrays.arrayOf(6, -8, 10, 12));
  }
}
