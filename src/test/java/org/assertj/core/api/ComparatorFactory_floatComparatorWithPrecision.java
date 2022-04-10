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

public class ComparatorFactory_floatComparatorWithPrecision {

  private final ComparatorFactory INSTANCE = ComparatorFactory.INSTANCE;

  @ParameterizedTest
  @CsvSource({"0.111f, 0.01f, 100"})
  void should_pass_for_expected_equal_to_actual_in_certain_range1(Float expected, Float precision, Float deltaParameter) {
    Float actual = expected + precision;
    Comparator<Float> comparator0_01f = INSTANCE.floatComparatorWithPrecision(precision);

    assertThat(comparator0_01f.compare(expected, actual)).isEqualTo(0);
    assertThat(comparator0_01f.compare(actual, expected)).isEqualTo(0);
    assertThat(comparator0_01f.compare(actual + precision / deltaParameter, expected)).isEqualTo(1);
    assertThat(comparator0_01f.compare(expected, actual + precision / deltaParameter)).isEqualTo(-1);
  }

  @ParameterizedTest
  @CsvSource({"1f, 0.00001f, 100"})
  void should_pass_for_expected_equal_to_actual_in_certain_range2(Float expected, Float precision, Float deltaParameter) {
    Comparator<Float> comparator0_1_6f = INSTANCE.floatComparatorWithPrecision(precision);

    assertThat(comparator0_1_6f.compare(expected + precision, expected)).isEqualTo(0);
    assertThat(comparator0_1_6f.compare(expected - precision, expected)).isEqualTo(0);
    assertThat(comparator0_1_6f.compare(expected + precision + precision / deltaParameter, expected)).isEqualTo(1);
    assertThat(comparator0_1_6f.compare(expected - precision - precision / deltaParameter, expected)).isEqualTo(-1);
  }

  @Test
  void should_pass_for_infinity_comparing() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    assertThat(comparator1f.compare(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)).isEqualTo(0);
    assertThat(comparator1f.compare(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)).isEqualTo(0);
    assertThat(comparator1f.compare(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY)).isEqualTo(1);
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
    assertThat(comparatorNan.compare(1f, 2f)).isEqualTo(0);
  }

  @Test
  void should_pass_for_infinity_precision() {
    Comparator<Float> comparatorPositiveInfinity = INSTANCE.floatComparatorWithPrecision(Float.POSITIVE_INFINITY);
    assertThat(comparatorPositiveInfinity.compare(1f, 2f)).isEqualTo(0);
    Comparator<Float> comparatorNegativeInfinity = INSTANCE.floatComparatorWithPrecision(Float.NEGATIVE_INFINITY);
    assertThat(comparatorNegativeInfinity.compare(1f, 2f)).isEqualTo(0);
  }


  @Test
  void should_fail_for_null_argument() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> assertThat(comparator1f.compare(null, 1f)).isEqualTo(0));
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> assertThat(comparator1f.compare(1f, null)).isEqualTo(0));
  }
}
