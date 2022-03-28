package org.assertj.core.api;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


public class ComparatorFactory_doubleComparatorWithPrecision {

  private final ComparatorFactory INSTANCE = ComparatorFactory.INSTANCE;

  @Test
  void should_pass_for_expected_equal_to_actual_in_certain_range1() {
    Comparator<Double> comparator0_1d = INSTANCE.doubleComparatorWithPrecision(0.01);

    assertThat(0).isEqualTo(comparator0_1d.compare(0.111, 0.121));
    assertThat(0).isEqualTo(comparator0_1d.compare(0.121, 0.111));
    assertThat(1).isEqualTo(comparator0_1d.compare(0.1211, 0.111));
    assertThat(-1).isEqualTo(comparator0_1d.compare(0.111, 0.1211));
  }

  @Test
  void should_pass_for_expected_equal_to_actual_in_certain_range2() {
    Comparator<Double> comparator0_1d = INSTANCE.doubleComparatorWithPrecision(1e-9);

    assertThat(0).isEqualTo(comparator0_1d.compare(1 + 1e-9, 1d));
    assertThat(0).isEqualTo(comparator0_1d.compare(1 - 1e-9, 1d));
    assertThat(1).isEqualTo(comparator0_1d.compare(1 + 1e-9 + 1e-10, 1d));
    assertThat(-1).isEqualTo(comparator0_1d.compare(1 - 1e-9 - 1e-10, 1d));
    //assertThat(1).isEqualTo(comparator0_1d.compare(0.1111111111211, 0.111111111111));
    //assertThat(-1).isEqualTo(comparator0_1d.compare(0.111111111111, 0.1111111111211));
  }

  @Test
  void should_pass_for_infinity_comparing() {
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    assertThat(0).isEqualTo(comparator1d.compare(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    assertThat(0).isEqualTo(comparator1d.compare(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
    assertThat(1).isEqualTo(comparator1d.compare(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
    assertThat(-1).isEqualTo(comparator1d.compare(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
  }

  @Test
  void should_pass_for_nan_comparing(){
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    assertThat(0).isEqualTo(comparator1d.compare(Double.NaN, Double.NaN));
  }

  @Test
  void should_pass_for_nan_precision() {
    Comparator<Double> comparatorNan = INSTANCE.doubleComparatorWithPrecision(Double.NaN);
    assertThat(0).isEqualTo(comparatorNan.compare(1d, 2d));
  }

  @Test
  void should_pass_for_infinity_precision() {
    Comparator<Double> comparatorPositiveInfinity = INSTANCE.doubleComparatorWithPrecision(Double.POSITIVE_INFINITY);
    assertThat(0).isEqualTo(comparatorPositiveInfinity.compare(1d, 2d));
    Comparator<Double> comparatorNegativeInfinity = INSTANCE.doubleComparatorWithPrecision(Double.POSITIVE_INFINITY);
    assertThat(0).isEqualTo(comparatorNegativeInfinity.compare(1d, 2d));
  }


  @Test
  void should_fail_for_null_argument() {
    Comparator<Double> comparator1d = INSTANCE.doubleComparatorWithPrecision(1d);
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> assertThat(0).isEqualTo(comparator1d.compare(null, 1d)));
    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> assertThat(0).isEqualTo(comparator1d.compare(1d, null)));
  }
}
