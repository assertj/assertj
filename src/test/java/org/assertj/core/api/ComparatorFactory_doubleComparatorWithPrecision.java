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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ComparatorFactory_doubleComparatorWithPrecision {

  private final ComparatorFactory INSTANCE = ComparatorFactory.INSTANCE;

  @ParameterizedTest
  @CsvSource({"0.111, 0.01, 100"})
  void should_pass_for_expected_equal_to_actual_in_certain_range1(Double expected, Double precision, Double deltaParameter) {
    Double actual = expected + precision;
    Comparator<Double> comparator0_1d = INSTANCE.doubleComparatorWithPrecision(precision);

    assertThat(comparator0_1d.compare(expected, actual)).isEqualTo(0);
    assertThat(comparator0_1d.compare(actual, expected)).isEqualTo(0);
    assertThat(comparator0_1d.compare(actual + precision / deltaParameter, expected)).isEqualTo(1);
    assertThat(comparator0_1d.compare(expected, actual + precision / deltaParameter)).isEqualTo(-1);
  }

  @ParameterizedTest
  @CsvSource({"1d, 1e-9, 10"})
  void should_pass_for_expected_equal_to_actual_in_certain_range2(Double expected, Double precision, Double deltaParameter) {
    Comparator<Double> comparator0_1d = INSTANCE.doubleComparatorWithPrecision(precision);

    assertThat(comparator0_1d.compare(expected + precision, expected)).isEqualTo(0);
    assertThat(comparator0_1d.compare(expected - precision, 1d)).isEqualTo(0);
    assertThat(comparator0_1d.compare(expected + precision + precision / deltaParameter, 1d)).isEqualTo(1);
    assertThat(comparator0_1d.compare(expected - precision - precision / deltaParameter, 1d)).isEqualTo(-1);
  }

  @Test
  void should_pass_for_infinity_comparing() {
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    assertThat(comparator1d.compare(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)).isEqualTo(0);
    assertThat(comparator1d.compare(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)).isEqualTo(0);
    assertThat(comparator1d.compare(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)).isEqualTo(1);
    assertThat(comparator1d.compare(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)).isEqualTo(-1);
  }

  @Test
  void should_pass_for_nan_comparing(){
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    assertThat(comparator1d.compare(Double.NaN, Double.NaN)).isEqualTo(0);
  }

  @Test
  void should_pass_for_nan_precision() {
    Comparator<Double> comparatorNan = INSTANCE.doubleComparatorWithPrecision(Double.NaN);
    assertThat(comparatorNan.compare(1d, 2d)).isEqualTo(0);
  }

  @Test
  void should_pass_for_infinity_precision() {
    Comparator<Double> comparatorPositiveInfinity = INSTANCE.doubleComparatorWithPrecision(Double.POSITIVE_INFINITY);
    assertThat(comparatorPositiveInfinity.compare(1d, 2d)).isEqualTo(0);
    Comparator<Double> comparatorNegativeInfinity = INSTANCE.doubleComparatorWithPrecision(Double.NEGATIVE_INFINITY);
    assertThat(comparatorNegativeInfinity.compare(1d, 2d)).isEqualTo(0);
  }


  @Test
  void should_fail_for_null_argument() {
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> assertThat(comparator1d.compare(null, 1d)).isEqualTo(0));
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> assertThat(comparator1d.compare(1d, null)).isEqualTo(0));
  }
}
