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
package org.assertj.core.internal.bytearrays;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.test.ByteArrays.emptyArray;
import static org.assertj.core.test.TestData.someIndex;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.ByteArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ByteArrays#assertContains(AssertionInfo, byte[], int, Index)}</code>.
 */
class ByteArrays_assertContains_at_Index_with_Integer_Argument_Test extends ByteArraysBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContains(someInfo(), null, 8, someIndex()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContains(someInfo(), emptyArray(), 8,
                                                                                           someIndex()))
                                                   .withMessage(actualIsEmpty());
  }

  @Test
  void should_throw_error_if_Index_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContains(someInfo(), actual, 8, null))
                                    .withMessage("Index should not be null");
  }

  @Test
  void should_throw_error_if_Index_is_out_of_bounds() {
    assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> arrays.assertContains(someInfo(),
                                                                                                      actual, 8,
                                                                                                      atIndex(6)))
                                                              .withMessageContaining(format("Index should be between <0> and <2> (inclusive) but was:%n <6>"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_at_index() {
    AssertionInfo info = someInfo();
    Index index = atIndex(1);

    Throwable error = catchThrowable(() -> arrays.assertContains(info, actual, 6, index));

    assertThat(error).isInstanceOf(AssertionError.class);
    byte found = 8;
    verify(failures).failure(info, shouldContainAtIndex(actual, (byte) 6, index, found));
  }

  @Test
  void should_pass_if_actual_contains_value_at_index() {
    arrays.assertContains(someInfo(), actual, 8, atIndex(1));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(),
                                                                                                                       null, -8,
                                                                                                                       someIndex()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(),
                                                                                                                       emptyArray(),
                                                                                                                       -8,
                                                                                                                       someIndex()))
                                                   .withMessage(actualIsEmpty());
  }

  @Test
  void should_throw_error_if_Index_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(),
                                                                                                        actual, -8,
                                                                                                        null))
                                    .withMessage("Index should not be null");
  }

  @Test
  void should_throw_error_if_Index_is_out_of_bounds_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(),
                                                                                                                                  actual,
                                                                                                                                  -8,
                                                                                                                                  atIndex(6)))
                                                              .withMessageContaining(format("Index should be between <0> and <2> (inclusive) but was:%n <6>"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_at_index_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Index index = atIndex(1);

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertContains(info, actual, 6, index));

    assertThat(error).isInstanceOf(AssertionError.class);
    byte found = 8;
    verify(failures).failure(info, shouldContainAtIndex(actual, (byte) 6, index, found, absValueComparisonStrategy));
  }

  @Test
  void should_pass_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, -8, atIndex(1));
  }
}
