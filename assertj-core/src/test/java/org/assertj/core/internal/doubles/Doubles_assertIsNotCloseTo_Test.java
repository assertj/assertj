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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal.doubles;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.internal.DoublesBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Doubles_assertIsNotCloseTo_Test extends DoublesBaseTest {

  private static final Double ZERO = 0.0;
  private static final Double ONE = 1.0;
  private static final Double TWO = 2.0;
  private static final Double TEN = 10.0;

  @ParameterizedTest
  @CsvSource({
      "1.0, 3.0, 1.0",
      "1.0, 10.0, 2.0",
      "1.0, 1.1, 0.0999999999" })
  void should_pass_if_difference_is_more_than_given_offset(double actual, double expected, double precision) {
    doubles.assertIsNotCloseTo(INFO, actual, expected, within(precision));
    doubles.assertIsNotCloseTo(INFO, expected, actual, within(precision));
    doubles.assertIsNotCloseTo(INFO, actual, expected, byLessThan(precision));
    doubles.assertIsNotCloseTo(INFO, expected, actual, byLessThan(precision));
  }

  @ParameterizedTest
  @CsvSource({
      "1.0, 2.0, 1.0",
      "1.0, 1.1, 0.1" })
  void should_pass_if_difference_is_equal_to_the_given_strict_offset(double actual, double expected, double precision) {
    doubles.assertIsNotCloseTo(INFO, actual, expected, byLessThan(precision));
    doubles.assertIsNotCloseTo(INFO, expected, actual, byLessThan(precision));
  }

  @Test
  void should_pass_if_actual_is_POSITIVE_INFINITY_and_expected_is_not() {
    doubles.assertIsNotCloseTo(INFO, POSITIVE_INFINITY, ONE, byLessThan(ONE));
    doubles.assertIsNotCloseTo(INFO, POSITIVE_INFINITY, ONE, within(ONE));
  }

  @Test
  void should_pass_if_actual_is_POSITIVE_INFINITY_and_expected_is_NEGATIVE_INFINITY() {
    doubles.assertIsNotCloseTo(INFO, POSITIVE_INFINITY, NEGATIVE_INFINITY, byLessThan(ONE));
    doubles.assertIsNotCloseTo(INFO, POSITIVE_INFINITY, NEGATIVE_INFINITY, within(ONE));
  }

  @Test
  void should_pass_if_actual_is_NEGATIVE_INFINITY_and_expected_is_not() {
    doubles.assertIsNotCloseTo(INFO, NEGATIVE_INFINITY, ONE, byLessThan(ONE));
    doubles.assertIsNotCloseTo(INFO, NEGATIVE_INFINITY, ONE, within(ONE));
  }

  @Test
  void should_pass_if_actual_is_NEGATIVE_INFINITY_and_expected_is_POSITIVE_INFINITY() {
    doubles.assertIsNotCloseTo(INFO, NEGATIVE_INFINITY, POSITIVE_INFINITY, byLessThan(ONE));
    doubles.assertIsNotCloseTo(INFO, NEGATIVE_INFINITY, POSITIVE_INFINITY, within(ONE));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Double actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> doubles.assertIsNotCloseTo(INFO, actual, ONE, within(ONE)));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> doubles.assertIsNotCloseTo(INFO, ONE, null, offset(ONE)))
                                    .withMessage("The given number should not be null");
  }

  @Test
  void should_fail_if_offset_is_null() {
    assertThatNullPointerException().isThrownBy(() -> doubles.assertIsNotCloseTo(INFO, ONE, ZERO, null))
                                    .withMessage("The given offset should not be null");
  }

  @ParameterizedTest
  @CsvSource({
      "1.0, 1.0, 0.0",
      "1.0, 0.0, 1.0",
      "1.0, 2.0, 1.0",
      "1.0, 1.1, 0.1"
  })
  void should_fail_if_difference_is_equal_to_given_offset(Double actual, Double other, Double offset) {
    // WHEN
    expectAssertionError(() -> doubles.assertIsNotCloseTo(INFO, actual, other, within(offset)));
    // THEN
    verify(failures).failure(INFO, shouldNotBeEqual(actual, other, within(offset), absDiff(actual, other)));
  }

  @Test
  void should_fail_if_actual_is_too_close_to_expected_value() {
    // WHEN
    expectAssertionError(() -> doubles.assertIsNotCloseTo(INFO, ONE, TWO, within(TEN)));
    // THEN
    verify(failures).failure(INFO, shouldNotBeEqual(ONE, TWO, within(TEN), ONE));
  }

  @Test
  void should_fail_if_actual_is_too_close_to_expected_value_with_strict_offset() {
    // WHEN
    expectAssertionError(() -> doubles.assertIsNotCloseTo(INFO, ONE, TWO, byLessThan(TEN)));
    // THEN
    verify(failures).failure(INFO, shouldNotBeEqual(ONE, TWO, byLessThan(TEN), ONE));
  }

  @Test
  void should_fail_if_actual_and_expected_are_NaN() {
    expectAssertionError(() -> doubles.assertIsNotCloseTo(INFO, NaN, NaN, within(ONE)));
  }

  @Test
  void should_fail_if_actual_and_expected_are_POSITIVE_INFINITY() {
    expectAssertionError(() -> doubles.assertIsNotCloseTo(INFO, POSITIVE_INFINITY, POSITIVE_INFINITY, within(ONE)));
  }

  @Test
  void should_fail_if_actual_and_expected_are_NEGATIVE_INFINITY() {
    expectAssertionError(() -> doubles.assertIsNotCloseTo(INFO, NEGATIVE_INFINITY, NEGATIVE_INFINITY, within(ONE)));
  }

}
