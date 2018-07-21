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
package org.assertj.core.internal.floatarrays;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.test.FloatArrays.emptyArray;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.*;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.internal.FloatArrays;
import org.assertj.core.internal.FloatArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link FloatArrays#assertContains(AssertionInfo, float[], float, Index)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class FloatArrays_assertContains_at_Index_Test extends FloatArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContains(someInfo(), null, 8f, someIndex()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContains(someInfo(), emptyArray(), 8f, someIndex()))
                                                   .withMessage(actualIsEmpty());
  }

  @Test
  public void should_throw_error_if_Index_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContains(someInfo(), actual, 8f, null))
                                    .withMessage("Index should not be null");
  }

  @Test
  public void should_throw_error_if_Index_is_out_of_bounds() {
    assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> arrays.assertContains(someInfo(),
                                                                                                      actual, 8f,
                                                                                                      atIndex(6)))
                                                              .withMessageContaining(format("Index should be between <0> and <2> (inclusive) but was:%n <6>"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_value_at_index() {
    float value = 6f;
    AssertionInfo info = someInfo();
    Index index = atIndex(1);
    try {
      arrays.assertContains(info, actual, value, index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainAtIndex(actual, value, index, 8f));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_value_at_index() {
    arrays.assertContains(someInfo(), actual, 8f, atIndex(1));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(), null, -8f, someIndex()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(), emptyArray(), -8f, someIndex()))
                                                   .withMessage(actualIsEmpty());
  }

  @Test
  public void should_throw_error_if_Index_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(),
                                                                                                        actual, -8f,
                                                                                                        null))
                                    .withMessage("Index should not be null");
  }

  @Test
  public void should_throw_error_if_Index_is_out_of_bounds_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertContains(someInfo(),
                                                                                                                                  actual,
                                                                                                                                  -8f,
                                                                                                                                  atIndex(6)))
                                                              .withMessageContaining(format("Index should be between <0> and <2> (inclusive) but was:%n <6>"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_value_at_index_according_to_custom_comparison_strategy() {
    float value = 6f;
    AssertionInfo info = someInfo();
    Index index = atIndex(1);
    try {
      arraysWithCustomComparisonStrategy.assertContains(info, actual, value, index);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainAtIndex(actual, value, index, 8f, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_value_at_index_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertContains(someInfo(), actual, -8f, atIndex(1));
  }
}
