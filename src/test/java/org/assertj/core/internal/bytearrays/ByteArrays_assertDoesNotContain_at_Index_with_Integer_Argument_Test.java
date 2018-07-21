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
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.assertj.core.test.ByteArrays.emptyArray;
import static org.assertj.core.test.TestData.someIndex;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.ByteArraysBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link ByteArrays#assertDoesNotContain(AssertionInfo, byte[], int, Index)}</code>.
 */
public class ByteArrays_assertDoesNotContain_at_Index_with_Integer_Argument_Test extends ByteArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertDoesNotContain(someInfo(), null, 8, someIndex()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_does_not_contain_value_at_Index() {
    arrays.assertDoesNotContain(someInfo(), actual, 6, atIndex(1));
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    arrays.assertDoesNotContain(someInfo(), emptyArray(), 8, someIndex());
  }

  @Test
  public void should_throw_error_if_Index_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertDoesNotContain(someInfo(), actual, 8, null))
                                    .withMessage("Index should not be null");
  }

  @Test
  public void should_pass_if_Index_is_out_of_bounds() {
    arrays.assertDoesNotContain(someInfo(), actual, 8, atIndex(6));
  }

  @Test
  public void should_fail_if_actual_contains_value_at_index() {
    AssertionInfo info = someInfo();
    Index index = atIndex(0);
    try {
      arrays.assertDoesNotContain(info, actual, 6, index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainAtIndex(actual, (byte) 6, index));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), null, -8, someIndex()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_does_not_contain_value_at_Index_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), actual, 6, atIndex(1));
  }

  @Test
  public void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), emptyArray(), -8, someIndex());
  }

  @Test
  public void should_throw_error_if_Index_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(),
                                                                                                              actual,
                                                                                                              -8, null))
                                    .withMessage("Index should not be null");
  }

  @Test
  public void should_pass_if_Index_is_out_of_bounds_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(someInfo(), actual, -8, atIndex(6));
  }

  @Test
  public void should_fail_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Index index = atIndex(0);
    try {
      arraysWithCustomComparisonStrategy.assertDoesNotContain(info, actual, 6, index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainAtIndex(actual, (byte) 6, index, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
