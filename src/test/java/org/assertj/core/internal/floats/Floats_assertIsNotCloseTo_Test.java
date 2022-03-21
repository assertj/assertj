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
package org.assertj.core.internal.floats;

import static java.lang.Float.NEGATIVE_INFINITY;
import static java.lang.Float.NaN;
import static java.lang.Float.POSITIVE_INFINITY;
import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FloatsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Floats_assertIsNotCloseTo_Test extends FloatsBaseTest {

  private static final Float ZERO = 0f;
  private static final Float ONE = 1f;
  private static final Float TWO = 2f;
  private static final Float THREE = 3f;
  private static final Float TEN = 10f;

  @Test
  void should_pass_if_difference_is_more_than_given_offset() {
    floats.assertIsNotCloseTo(someInfo(), ONE, THREE, byLessThan(ONE));
    floats.assertIsNotCloseTo(someInfo(), ONE, THREE, within(ONE));
    floats.assertIsNotCloseTo(someInfo(), ONE, TEN, byLessThan(TWO));
    floats.assertIsNotCloseTo(someInfo(), ONE, TEN, within(TWO));
  }

  @Test
  void should_pass_if_difference_is_equal_to_the_given_strict_offset() {
    floats.assertIsNotCloseTo(someInfo(), ONE, TWO, byLessThan(ONE));
    floats.assertIsNotCloseTo(someInfo(), TWO, ONE, byLessThan(ONE));
  }

  @Test
  void should_pass_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    floats.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY, ONE, byLessThan(ONE));
    floats.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY, ONE, within(ONE));
  }

  @Test
  void should_pass_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    floats.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY, NEGATIVE_INFINITY, byLessThan(ONE));
    floats.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY, NEGATIVE_INFINITY, within(ONE));
  }

  @Test
  void should_pass_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    floats.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY, ONE, byLessThan(ONE));
    floats.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY, ONE, within(ONE));
  }

  @Test
  void should_pass_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    floats.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY, POSITIVE_INFINITY, byLessThan(ONE));
    floats.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY, POSITIVE_INFINITY, within(ONE));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> floats.assertIsNotCloseTo(someInfo(), null, ONE,
                                                                                               byLessThan(ONE)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> floats.assertIsNotCloseTo(someInfo(), ONE, null, byLessThan(ONE)));
  }

  @Test
  void should_fail_if_offset_is_null() {
    assertThatNullPointerException().isThrownBy(() -> floats.assertIsNotCloseTo(someInfo(), ONE, ZERO, null));
  }

  @ParameterizedTest
  @CsvSource({
      "1.0, 1.0, 0.0",
      "1.0, 0.0, 1.0",
      "1.0, 2.0, 1.0"
  })
  void should_fail_if_difference_is_equal_to_given_offset(Float actual, Float other, Float offset) {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> floats.assertIsNotCloseTo(someInfo(), actual, other, within(offset)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(actual, other, within(offset), abs(actual - other)));
  }

  @Test
  void should_fail_if_actual_is_too_close_to_expected_value() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> floats.assertIsNotCloseTo(info, ONE, TWO, within(TEN)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(ONE, TWO, within(TEN), TWO - ONE));
  }

  @Test
  void should_fail_if_actual_is_too_close_to_expected_value_with_strict_offset() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> floats.assertIsNotCloseTo(info, ONE, TWO, byLessThan(TEN)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldNotBeEqual(ONE, TWO, byLessThan(TEN), TWO - ONE));
  }

  @Test
  void should_fail_if_actual_and_expected_are_NaN() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> floats.assertIsNotCloseTo(someInfo(), NaN, NaN,
                                                                                               within(ONE)));
  }

  @Test
  void should_fail_if_actual_and_expected_are_POSITIVE_INFINITY() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> floats.assertIsNotCloseTo(someInfo(), POSITIVE_INFINITY,
                                                                                               POSITIVE_INFINITY, within(ONE)));
  }

  @Test
  void should_fail_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> floats.assertIsNotCloseTo(someInfo(), NEGATIVE_INFINITY,
                                                                                               NEGATIVE_INFINITY, within(ONE)));
  }
}
