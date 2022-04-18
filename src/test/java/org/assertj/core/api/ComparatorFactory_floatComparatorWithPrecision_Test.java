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
package org.assertj.core.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

class ComparatorFactory_floatComparatorWithPrecision_Test {

  private final ComparatorFactory INSTANCE = ComparatorFactory.INSTANCE;

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.001f, 0.000001f",
    "0.12345f, 0.12346f, 0.00001f, 1e-6",
    "0.54321, 0.54320, 0.0001, 1e-4",
    "1.2463, 1.2464, 0.0001, 1e-6"})
  void should_pass_for_expected_equal_to_actual_in_certain_range_max_min(Float expected, Float actual, Float precision, Float smallDifference) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    Float max = Math.max(expected, actual), min = Math.min(expected, actual);
    assertThat(comparator.compare(max, min)).isEqualTo(0);
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.001f, 0.000001f",
    "0.12345f, 0.12346f, 0.00001f, 1e-6",
    "0.54321, 0.54320, 0.0001, 1e-4",
    "1.2463, 1.2464, 0.0001, 1e-6"})
  void should_pass_for_expected_equal_to_actual_in_certain_range_min_max(Float expected, Float actual, Float precision, Float smallDifference) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    Float max = Math.max(expected, actual), min = Math.min(expected, actual);
    assertThat(comparator.compare(min, max)).isEqualTo(0);
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.001f, 0.000001f",
    "0.12345f, 0.12346f, 0.00001f, 1e-6",
    "0.54321, 0.54320, 0.0001, 1e-4",
    "1.2463, 1.2464, 0.0001, 1e-6"})
  void should_pass_for_expected_equal_to_actual_in_certain_range_max_plus_difference(Float expected, Float actual, Float precision, Float smallDifference) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    Float max = Math.max(expected, actual), min = Math.min(expected, actual);
    assertThat(comparator.compare(max + smallDifference, min)).isEqualTo(1);
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.001f, 0.000001f",
    "0.12345f, 0.12346f, 0.00001f, 1e-6",
    "0.54321, 0.54320, 0.0001, 1e-4",
    "1.2463, 1.2464, 0.0001, 1e-6"})
  void should_pass_for_expected_equal_to_actual_in_certain_range_min_minus_difference(Float expected, Float actual, Float precision, Float smallDifference) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    Float max = Math.max(expected, actual), min = Math.min(expected, actual);
    assertThat(comparator.compare(min - smallDifference, max)).isEqualTo(-1);
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.000999f, 0.000001f",
    "0.12345f, 0.12346f, 0.000009f, 0.00001f",
    "0.7654321f, 0.7654320f, 9e-8, 1e-7",
    "1.2463, 1.2464, 9e-5, 1e-4"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_max_min(Float expected, Float actual, Float precision, Float smallDifference) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    Float max = Math.max(expected, actual), min = Math.min(expected, actual);
    expectAssertionError(() -> assertThat(comparator.compare(max, min)).isEqualTo(0));
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.000999f, 0.000001f",
    "0.12345f, 0.12346f, 0.000009f, 0.00001f",
    "0.7654321f, 0.7654320f, 9e-8, 1e-7",
    "1.2463, 1.2464, 9e-5, 1e-4"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_min_max(Float expected, Float actual, Float precision, Float smallDifference) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    Float max = Math.max(expected, actual), min = Math.min(expected, actual);
    expectAssertionError(() -> assertThat(comparator.compare(min, max)).isEqualTo(0));
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.000999f, 0.000001f",
    "0.12345f, 0.12346f, 0.000009f, 0.00001f",
    "0.7654321f, 0.7654320f, 9e-8, 1e-7",
    "1.2463, 1.2464, 9e-5, 1e-4"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_max_minus_difference(Float expected, Float actual, Float precision, Float smallDifference) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    Float max = Math.max(expected, actual), min = Math.min(expected, actual);
    expectAssertionError(() -> assertThat(comparator.compare(max - smallDifference, min)).isEqualTo(1));
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.000999f, 0.000001f",
    "0.12345f, 0.12346f, 0.000009f, 0.00001f",
    "0.7654321f, 0.7654320f, 9e-8, 1e-7",
    "1.2463, 1.2464, 9e-5, 1e-4"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_min_plus_difference(Float expected, Float actual, Float precision, Float smallDifference) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    Float max = Math.max(expected, actual), min = Math.min(expected, actual);
    expectAssertionError(() -> assertThat(comparator.compare(min + smallDifference, max)).isEqualTo(-1));
  }

  @Test
  void should_pass_for_infinity_comparing_1() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    assertThat(comparator1f.compare(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)).isEqualTo(0);
  }

  @Test
  void should_pass_for_infinity_comparing_2() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    assertThat(comparator1f.compare(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)).isEqualTo(0);
  }

  @Test
  void should_pass_for_infinity_comparing_3() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    assertThat(comparator1f.compare(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY)).isEqualTo(1);
  }

  @Test
  void should_pass_for_infinity_comparing_4() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    assertThat(comparator1f.compare(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY)).isEqualTo(-1);
  }

  @Test
  void should_pass_for_nan_comparing() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    assertThat(comparator1f.compare(Float.NaN, Float.NaN)).isEqualTo(0);
  }

  @Test
  void should_pass_for_nan_precision() {
    Comparator<Float> comparatorNan = INSTANCE.floatComparatorWithPrecision(Float.NaN);
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(comparatorNan.compare(1f, 2f)).isEqualTo(0));
  }

  @Test
  void should_fail_for_positive_infinity_precision() {
    Comparator<Float> comparatorPositiveInfinity = INSTANCE.floatComparatorWithPrecision(Float.POSITIVE_INFINITY);
    assertThatIllegalArgumentException().isThrownBy(()-> assertThat(comparatorPositiveInfinity.compare(1f, 2f)).isEqualTo(0));
  }

  @Test
  void should_fail_for_negative_infinity_precision() {
    Comparator<Float> comparatorNegativeInfinity = INSTANCE.floatComparatorWithPrecision(Float.NEGATIVE_INFINITY);
    assertThatIllegalArgumentException().isThrownBy(()-> assertThat(comparatorNegativeInfinity.compare(1f, 2f)).isEqualTo(0));
  }
}
