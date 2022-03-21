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
package org.assertj.core.internal.shorts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Shorts;
import org.assertj.core.internal.ShortsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Shorts#assertLessThanOrEqualTo(AssertionInfo, Short, short)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Shorts_assertLessThanOrEqualTo_Test extends ShortsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> shorts.assertLessThanOrEqualTo(someInfo(), null, (short) 8))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_less_than_other() {
    shorts.assertLessThanOrEqualTo(someInfo(), (short) 6, (short) 8);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other() {
    shorts.assertLessThanOrEqualTo(someInfo(), (short) 6, (short) 6);
  }

  @Test
  void should_fail_if_actual_is_greater_than_other() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> shorts.assertLessThanOrEqualTo(info, (short) 8, (short) 6));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeLessOrEqual((short) 8, (short) 6));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> shortsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(),
                                                                                                                                  null,
                                                                                                                                  (short) 8))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    shortsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), (short) 6, (short) -8);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    shortsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), (short) 6, (short) -6);
  }

  @Test
  void should_fail_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> shortsWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(info, (short) -8,
                                                                                                        (short) 6));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeLessOrEqual((short) -8, (short) 6, absValueComparisonStrategy));
  }
}
