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
package org.assertj.core.internal.doubles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Doubles;
import org.assertj.core.internal.DoublesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Doubles#assertIsStrictlyBetween(AssertionInfo, Double, Double, Double)}</code>.
 * 
 * @author William Delanoue
 */
class Doubles_assertIsStrictlyBetween_Test extends DoublesBaseTest {

  private static final Double ZERO = 0D;
  private static final Double ONE = 1D;
  private static final Double TWO = 2D;
  private static final Double TEN = 10D;

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> doubles.assertIsStrictlyBetween(someInfo(), null, ZERO, ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_start_is_null() {
    assertThatNullPointerException().isThrownBy(() -> doubles.assertIsStrictlyBetween(someInfo(), ONE, null, ONE));
  }

  @Test
  void should_fail_if_end_is_null() {
    assertThatNullPointerException().isThrownBy(() -> doubles.assertIsStrictlyBetween(someInfo(), ONE, ZERO, null));
  }

  @Test
  void should_pass_if_actual_is_in_range() {
    doubles.assertIsStrictlyBetween(someInfo(), ONE, ZERO, TEN);
  }

  @Test
  void should_fail_if_actual_is_equal_to_range_start() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> doubles.assertIsStrictlyBetween(info, ONE, ONE, TEN));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeBetween(ONE, ONE, TEN, false, false));
  }

  @Test
  void should_fail_if_actual_is_equal_to_range_end() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> doubles.assertIsStrictlyBetween(info, ONE, ZERO, ONE));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeBetween(ONE, ZERO, ONE, false, false));
  }

  @Test
  void should_fail_if_actual_is_not_in_range_start() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> doubles.assertIsStrictlyBetween(info, ONE, TWO, TEN));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeBetween(ONE, TWO, TEN, false, false));
  }

  @Test
  void should_fail_if_actual_is_not_in_range_end() {
    assertThatIllegalArgumentException().isThrownBy(() -> doubles.assertIsStrictlyBetween(someInfo(), ONE, ZERO, ZERO))
                                        .withMessage("The end value <0.0> must not be less than or equal to the start value <0.0>!");
  }
}
