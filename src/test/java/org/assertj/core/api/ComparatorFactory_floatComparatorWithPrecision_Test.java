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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

class ComparatorFactory_floatComparatorWithPrecision_Test {

  private final ComparatorFactory INSTANCE = ComparatorFactory.INSTANCE;

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.001f, 0.000001f",
    "0.110f, 0.111f, 0.001f, 0.000001f",
    "0.12345f, 0.12346f, 0.00001f, 1e-6",
    "0.12346f, 0.12345f, 0.00001f, 1e-6",
    "0.54321f, 0.54320f, 0.0001f, 1e-4",
    "0.54320f, 0.54321f, 0.0001f, 1e-4",
    "1.2463f, 1.2464f, 0.0001f, 1e-6",
    "1.2464f, 1.2463f, 0.0001f, 1e-6"})
  void should_pass_for_expected_equal_to_actual_in_certain_range_max_min(Float expected, Float actual, Float precision) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    then(comparator.compare(expected, actual)).isZero();
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.00099f",
    "0.12346f, 0.12345f, 0.0000099f",
    "0.54321f, 0.54320f, 0.0000099f",
    "1.2464f, 1.2463f, 0.000099f"})
  void should_pass_for_expected_equal_to_actual_in_certain_range_max_plus_difference(Float expected, Float actual, Float precision) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    then(comparator.compare(expected, actual)).isOne();
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.0000099f",
    "0.112f, 0.111f, 0.0000099f",
    "0.12346f, 0.12345f, 0.0000099f",
    "0.12347f, 0.12346f, 0.0000099f",
    "0.54321f, 0.54320f, 0.0000099f",
    "0.54322f, 0.54321f, 0.0000099f",
    "1.2464f, 1.2463f, 0.000099f",
    "1.2465f, 1.2464f, 0.000099f"})
  void should_pass_for_expected_equal_to_actual_in_certain_range_min_minus_difference(Float expected, Float actual, Float precision) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    then(comparator.compare(expected, actual)).isOne();
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.000999f, 0.000001f",
    "0.110f, 0.111f, 0.000999f, 0.000001f",
    "0.12345f, 0.12346f, 0.000009f, 0.00001f",
    "0.12346f, 0.12345f, 0.000009f, 0.00001f",
    "0.7654321f, 0.7654320f, 9e-8, 1e-7",
    "0.7654320f, 0.7654321f, 9e-8, 1e-7",
    "1.2463f, 1.2464f, 9e-5, 1e-4",
    "1.2464f, 1.2463f, 9e-5, 1e-4"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_max_min(Float expected, Float actual, Float precision) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    expectAssertionError(() -> then(comparator.compare(expected, actual)).isZero());
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.000999f",
    "0.110f, 0.111f, 0.000999f",
    "0.12345f, 0.12346f, 0.000009f",
    "0.12346f, 0.12345f, 0.000009f",
    "0.7654321f, 0.7654320f, 9e-8",
    "0.7654320f, 0.7654321f, 9e-8",
    "1.2463f, 1.2464f, 9e-5",
    "1.2464f, 1.2463f, 9e-5"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_min_max(Float expected, Float actual, Float precision) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    expectAssertionError(() -> then(comparator.compare(expected, actual)).isZero());
  }

  @ParameterizedTest
  @CsvSource({"0.11099f, 0.110f, 0.000999f",
    "0.123459f, 0.12345f, 0.000009f",
    "0.76543210f, 0.76543211f, 9e-8",
    "1.2463, 1.2464, 9e-5"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_max_minus_difference(Float expected, Float actual, Float precision) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    expectAssertionError(() -> then(comparator.compare(expected, actual)).isOne());
  }

  @ParameterizedTest
  @CsvSource({"0.111f, 0.110f, 0.000999f",
    "0.1234590f, 0.1234591f, 0.000009f",
    "0.76543210f, 0.76543211f, 9e-8",
    "1.24639f, 1.246479f, 9e-5"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_min_plus_difference(Float expected, Float actual, Float precision) {
    Comparator<Float> comparator = INSTANCE.floatComparatorWithPrecision(precision);
    expectAssertionError(() -> then(comparator.compare(expected, actual)).isEqualTo(-1));
  }

  @Test
  void should_pass_for_infinity_comparing_1() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    then(comparator1f.compare(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)).isZero();
  }

  @Test
  void should_pass_for_infinity_comparing_2() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    then(comparator1f.compare(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)).isZero();
  }

  @Test
  void should_pass_for_infinity_comparing_3() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    then(comparator1f.compare(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY)).isOne();
  }

  @Test
  void should_pass_for_infinity_comparing_4() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    then(comparator1f.compare(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY)).isEqualTo(-1);
  }

  @Test
  void should_pass_for_nan_comparing() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    then(comparator1f.compare(Float.NaN, Float.NaN)).isZero();
  }

  @Test
  void should_pass_for_nan_precision() {
    Comparator<Float> comparatorNan = INSTANCE.floatComparatorWithPrecision(Float.NaN);
    assertThatIllegalArgumentException().isThrownBy(() -> then(comparatorNan.compare(1f, 2f)).isZero());
  }

  @Test
  void should_fail_for_positive_infinity_precision() {
    Comparator<Float> comparatorPositiveInfinity = INSTANCE.floatComparatorWithPrecision(Float.POSITIVE_INFINITY);
    assertThatIllegalArgumentException().isThrownBy(()-> then(comparatorPositiveInfinity.compare(1f, 2f)).isZero());
  }

  @Test
  void should_fail_for_negative_infinity_precision() {
    Comparator<Float> comparatorNegativeInfinity = INSTANCE.floatComparatorWithPrecision(Float.NEGATIVE_INFINITY);
    assertThatIllegalArgumentException().isThrownBy(()-> then(comparatorNegativeInfinity.compare(1f, 2f)).isZero());
  }
}
