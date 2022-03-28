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

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ComparatorFactory_floatComparatorWithPrecision {

  private final ComparatorFactory INSTANCE = ComparatorFactory.INSTANCE;

  @Test
  void should_pass_for_expected_equal_to_actual_in_certain_range1() {
    Comparator<Float> comparator0_01f = INSTANCE.floatComparatorWithPrecision(0.01f);

    assertThat(0).isEqualTo(comparator0_01f.compare(0.111f, 0.121f));
    assertThat(0).isEqualTo(comparator0_01f.compare(0.121f, 0.111f));
    assertThat(1).isEqualTo(comparator0_01f.compare(0.1211f, 0.111f));
    assertThat(-1).isEqualTo(comparator0_01f.compare(0.111f, 0.1211f));
  }

  @Test
  void should_pass_for_expected_equal_to_actual_in_certain_range2() {
    Comparator<Float> comparator0_1_6f = INSTANCE.floatComparatorWithPrecision(0.00001f);

    assertThat(0).isEqualTo(comparator0_1_6f.compare(1.00001f, 1f));
    assertThat(0).isEqualTo(comparator0_1_6f.compare(0.99999f, 1f));
    assertThat(1).isEqualTo(comparator0_1_6f.compare(1.0000101f, 1f));
    assertThat(-1).isEqualTo(comparator0_1_6f.compare(0.9999899f, 1f));
    //assertThat(1).isEqualTo(comparator0_1_6f.compare(0.1111111111211, 0.111111111111));
    //assertThat(-1).isEqualTo(comparator0_1_6f.compare(0.111111111111, 0.1111111111211));
  }

  @Test
  void should_pass_for_infinity_comparing() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    assertThat(0).isEqualTo(comparator1f.compare(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
    assertThat(0).isEqualTo(comparator1f.compare(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
    assertThat(1).isEqualTo(comparator1f.compare(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY));
    assertThat(-1).isEqualTo(comparator1f.compare(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY));
  }

  @Test
  void should_pass_for_nan_comparing(){
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    assertThat(0).isEqualTo(comparator1f.compare(Float.NaN, Float.NaN));
  }

  @Test
  void should_pass_for_nan_precision() {
    Comparator<Float> comparatorNan = INSTANCE.floatComparatorWithPrecision(Float.NaN);
    assertThat(0).isEqualTo(comparatorNan.compare(1f, 2f));
  }

  @Test
  void should_pass_for_infinity_precision() {
    Comparator<Float> comparatorPositiveInfinity = INSTANCE.floatComparatorWithPrecision(Float.POSITIVE_INFINITY);
    assertThat(0).isEqualTo(comparatorPositiveInfinity.compare(1f, 2f));
    Comparator<Float> comparatorNegativeInfinity = INSTANCE.floatComparatorWithPrecision(Float.NEGATIVE_INFINITY);
    assertThat(0).isEqualTo(comparatorNegativeInfinity.compare(1f, 2f));
  }


  @Test
  void should_fail_for_null_argument() {
    Comparator<Float> comparator1f = INSTANCE.floatComparatorWithPrecision(1f);
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> assertThat(0).isEqualTo(comparator1f.compare(null, 1f)));
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> assertThat(0).isEqualTo(comparator1f.compare(1f, null)));
  }
}
