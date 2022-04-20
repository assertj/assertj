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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

class ComparatorFactory_doubleComparatorWithPrecision_Test {

  private final ComparatorFactory INSTANCE = ComparatorFactory.INSTANCE;

  @ParameterizedTest
  @CsvSource({"0.111, 0.110, 0.001",
    "0.111, 0.110, 0.001",
    "0.12345, 0.12346, 0.00001",
    "0.12346, 0.12345, 0.00001",
    "0.7654321, 0.7654320, 0.0000001",
    "0.7654320, 0.7654321, 0.0000001",
    "1.2463, 1.2464, 0.0001",
    "1.2464, 1.2463, 0.0001"})
  void should_pass_for_expected_equal_to_actual_in_certain_range_max_min(Double expected, Double actual, Double precision) {
    Comparator<Double> comparator = INSTANCE.doubleComparatorWithPrecision(precision);
    then(comparator.compare(expected, actual)).isZero();
  }

  @ParameterizedTest
  @CsvSource({"0.111, 0.110, 0.00099",
    "0.12346, 0.12345, 0.0000099",
    "0.7654321, 0.7654320, 0.000000099",
    "1.2464, 1.2463, 0.000099"})
  void should_pass_for_expected_equal_to_actual_in_certain_range_max_difference(Double expected, Double actual, Double precision) {
    Comparator<Double> comparator = INSTANCE.doubleComparatorWithPrecision(precision);
    then(comparator.compare(expected, actual)).isOne();
  }

  @ParameterizedTest
  @CsvSource({"0.110, 0.111, 0.00099",
    "0.12345, 0.12346, 0.0000099",
    "0.7654320, 0.7654321, 0.000000099",
    "1.2463, 1.2464, 0.000099"})
  void should_pass_for_expected_equal_to_actual_in_certain_range_min_difference(Double expected, Double actual, Double precision) {
    Comparator<Double> comparator = INSTANCE.doubleComparatorWithPrecision(precision);
    then(comparator.compare(expected, actual)).isEqualTo(-1);
  }


  @ParameterizedTest
  @CsvSource({"0.111, 0.110, 0.000999",
    "0.110, 0.111, 0.000999",
    "0.12345, 0.12346, 0.000009",
    "0.12346, 0.12345, 0.000009",
    "0.7654321, 0.7654320, 9e-8",
    "0.7654320, 0.7654321, 9e-8",
    "1.2463, 1.2464, 9e-5",
    "1.2464, 1.2463, 9e-5"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_max_min(Double expected, Double actual, Double precision) {
    Comparator<Double> comparator = INSTANCE.doubleComparatorWithPrecision(precision);
    Double max = Math.max(expected, actual), min = Math.min(expected, actual);
    expectAssertionError(()-> then(comparator.compare(max, min)).isZero());
  }

  @ParameterizedTest
  @CsvSource({"0.111, 0.110, 0.000999",
    "0.110, 0.111, 0.000999",
    "0.12345, 0.12346, 0.000009",
    "0.12346, 0.12345, 0.000009",
    "0.7654321, 0.7654320, 9e-8",
    "0.7654320, 0.7654321, 9e-8",
    "1.2463, 1.2464, 9e-5",
    "1.2464, 1.2463, 9e-5",})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_min_max(Double expected, Double actual, Double precision) {
    Comparator<Double> comparator  = INSTANCE.doubleComparatorWithPrecision(precision);
    Double max = Math.max(expected, actual), min = Math.min(expected, actual);
    expectAssertionError(() -> then(comparator.compare(min, max)).isZero());
  }

  @ParameterizedTest
  @CsvSource({"0.110999, 0.110, 0.000999",
    "0.12345, 0.12346, 0.000009",
    "0.7654321, 0.7654320, 1e-7",
    "1.2463, 1.2464, 9e-5, 1e-4"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_max_difference(Double expected, Double actual, Double precision) {
    Comparator<Double> comparator = INSTANCE.doubleComparatorWithPrecision(precision);
    expectAssertionError(() -> then(comparator.compare(expected, actual)).isOne());
  }

  @ParameterizedTest
  @CsvSource({"0.111, 0.110001, 0.000999",
    "0.12346, 0.12345, 0.000009",
    "0.7654321, 0.7654320, 9e-8",
    "1.24639, 1.2464, 9e-5"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range_min_difference(Double expected, Double actual, Double precision) {
    Comparator<Double> comparator = INSTANCE.doubleComparatorWithPrecision(precision);
    expectAssertionError(() -> then(comparator.compare(expected, actual)).isEqualTo(-1));
  }


  @Test
  void should_pass_for_infinity_comparing_1() {
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    then(comparator1d.compare(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)).isZero();
  }

  @Test
  void should_pass_for_infinity_comparing_2() {
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    then(comparator1d.compare(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)).isZero();
  }

  @Test
  void should_pass_for_infinity_comparing_3() {
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    then(comparator1d.compare(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)).isOne();
  }

  @Test
  void should_pass_for_infinity_comparing_4() {
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    then(comparator1d.compare(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)).isEqualTo(-1);
  }

  @Test
  void should_pass_for_nan_comparing(){
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    then(comparator1d.compare(Double.NaN, Double.NaN)).isZero();
  }

  @Test
  void should_fail_for_nan_precision() {
    Comparator<Double> comparatorNan = INSTANCE.doubleComparatorWithPrecision(Double.NaN);
    assertThatIllegalArgumentException().isThrownBy(() -> then(comparatorNan.compare(1d, 2d)).isZero());
  }

  @Test
  void should_fail_for_positive_infinity_precision() {
    Comparator<Double> comparatorPositiveInfinity = INSTANCE.doubleComparatorWithPrecision(Double.POSITIVE_INFINITY);
    assertThatIllegalArgumentException().isThrownBy(() -> then(comparatorPositiveInfinity.compare(1d, 2d)).isZero());
  }

  @Test
  void should_fail_for_negative_infinity_precision() {
    Comparator<Double> comparatorNegativeInfinity = INSTANCE.doubleComparatorWithPrecision(Double.NEGATIVE_INFINITY);
    assertThatIllegalArgumentException().isThrownBy(() -> then(comparatorNegativeInfinity.compare(1d, 2d)).isZero());
  }
}
