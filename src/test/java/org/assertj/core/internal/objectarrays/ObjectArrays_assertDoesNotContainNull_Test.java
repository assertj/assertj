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
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotContainNull.shouldNotContainNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertDoesNotContainNull(AssertionInfo, Object[])}</code>.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class ObjectArrays_assertDoesNotContainNull_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Luke", "Yoda");
  }

  @Test
  void should_pass_if_actual_does_not_contain_null() {
    arrays.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_empty() {
    actual = Arrays.array();
    arrays.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertDoesNotContainNull(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_contains_null() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Yoda", null);

    Throwable error = catchThrowable(() -> arrays.assertDoesNotContainNull(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotContainNull(actual));
  }

  @Test
  void should_pass_if_actual_does_not_contain_null_whatever_custom_comparison_strategy_is() {
    arraysWithCustomComparisonStrategy.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    actual = Arrays.array();
    arraysWithCustomComparisonStrategy.assertDoesNotContainNull(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainNull(someInfo(),
                                                                                                                                 null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_contains_null_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    actual = array("Luke", "Yoda", null);

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertDoesNotContainNull(info, actual));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotContainNull(actual));
  }
}
