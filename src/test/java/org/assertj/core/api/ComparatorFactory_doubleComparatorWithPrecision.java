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
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

public class ComparatorFactory_doubleComparatorWithPrecision {

  private final ComparatorFactory INSTANCE = ComparatorFactory.INSTANCE;

  @ParameterizedTest
  @CsvSource({"0.111, 0.110, 0.001, 0.000001",
              "0.12345, 0.12346, 0.00001, 1e-10",
              "0.7654321, 0.7654320, 0.0000001, 1e-9",
              "1.2463, 1.2464, 0.0001, 1e-8"})
  void should_pass_for_expected_equal_to_actual_in_certain_range(Double expected, Double actual, Double precision, Double smallDifference) {
    Comparator<Double> comparator0_1d = INSTANCE.doubleComparatorWithPrecision(precision);
    Double max = Math.max(expected, actual), min = Math.min(expected, actual);

    assertThat(comparator0_1d.compare(max, min)).isEqualTo(0);
    assertThat(comparator0_1d.compare(min, max)).isEqualTo(0);
    assertThat(comparator0_1d.compare(max + smallDifference, min)).isEqualTo(1);
    assertThat(comparator0_1d.compare(min - smallDifference, max)).isEqualTo(-1);
  }

  @ParameterizedTest
  @CsvSource({"0.111, 0.110, 0.000999, 0.000001",
              "0.12345, 0.12346, 0.000009, 0.00001",
              "0.7654321, 0.7654320, 9e-8, 1e-7",
              "1.2463, 1.2464, 9e-5, 1e-4"})
  void should_fail_for_expected_equal_to_actual_not_in_certain_range(Double expected, Double actual, Double precision, Double smallDifference) {
    Comparator<Double> comparator0_1d = INSTANCE.doubleComparatorWithPrecision(precision);
    Double max = Math.max(expected, actual), min = Math.min(expected, actual);

    expectAssertionError(()-> assertThat(comparator0_1d.compare(max, min)).isEqualTo(0));
    expectAssertionError(() -> assertThat(comparator0_1d.compare(min, max)).isEqualTo(0));
    expectAssertionError(() -> assertThat(comparator0_1d.compare(max - smallDifference, min)).isEqualTo(1));
    expectAssertionError(() -> assertThat(comparator0_1d.compare(min + smallDifference, max)).isEqualTo(-1));

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
  void should_fail_for_nan_precision() {
    Comparator<Double> comparatorNan = INSTANCE.doubleComparatorWithPrecision(Double.NaN);
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(comparatorNan.compare(1d, 2d)).isEqualTo(0));
  }

  @Test
  void should_fail_for_infinity_precision() {
    Comparator<Double> comparatorPositiveInfinity = INSTANCE.doubleComparatorWithPrecision(Double.POSITIVE_INFINITY);
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(comparatorPositiveInfinity.compare(1d, 2d)).isEqualTo(0));
    Comparator<Double> comparatorNegativeInfinity = INSTANCE.doubleComparatorWithPrecision(Double.NEGATIVE_INFINITY);
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(comparatorNegativeInfinity.compare(1d, 2d)).isEqualTo(0));
  }


  @Test
  void should_fail_for_null_argument() {
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    assertThatNullPointerException().isThrownBy(() -> assertThat(comparator1d.compare(null, 1d)).isEqualTo(0));
    assertThatNullPointerException().isThrownBy(() -> assertThat(comparator1d.compare(1d, null)).isEqualTo(0));
  }
}
