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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import java.math.BigDecimal;
import java.util.Comparator;

import org.assertj.core.internal.Doubles;
import org.assertj.core.internal.Floats;

public class ComparatorFactory {

  public static final ComparatorFactory INSTANCE = new ComparatorFactory();

  public Comparator<Double> doubleComparatorWithPrecision(double precision) {
    // can't use <> with anonymous class in java 8
    return new Comparator<Double>() {

      @Override
      public int compare(Double double1, Double double2) {
        if (Doubles.instance().isNanOrInfinite(precision)) {
          throw new IllegalArgumentException("Precision should not be Nan or Infinity!");
        }
        // handle NAN or Infinity cases with Java Float behavior (and not BigDecimal that are used afterwards)
        if (Doubles.instance().isNanOrInfinite(double1) || Doubles.instance().isNanOrInfinite(double2)) {
          return Double.compare(double1, double2);
        }
        // if floats are close enough they are considered equal, otherwise we compare as BigDecimal which does exact computation.
        return isWithinPrecision(double1, double2, precision) ? 0 : asBigDecimal(double1).compareTo(asBigDecimal(double2));
      }

      @Override
      public String toString() {
        return "double comparator at precision " + precision + " (values are considered equal if diff == precision)";
      }
    };
  }

  public Comparator<Float> floatComparatorWithPrecision(float precision) {
    // can't use <> with anonymous class in java 8
    return new Comparator<Float>() {

      @Override
      public int compare(Float float1, Float float2) {
        Floats floats = Floats.instance();
        if (floats.isNanOrInfinite(precision)) {
          throw new IllegalArgumentException("Precision should not be Nan or Infinity!");
        }
        // handle NAN or Infinity cases with Java Float behavior (and not BigDecimal that are used afterwards)
        if (floats.isNanOrInfinite(float1) || floats.isNanOrInfinite(float2)) {
          return Float.compare(float1, float2);
        }
        // if floats are close enough they are considered equal, otherwise we compare as BigDecimal which does exact computation.
        return isWithinPrecision(float1, float2, precision) ? 0 : asBigDecimal(float1).compareTo(asBigDecimal(float2));
      }

      @Override
      public String toString() {
        return "float comparator at precision " + precision + " (values are considered equal if diff == precision)";
      }
    };
  }

  /**
   * Convert to a precise BigDecimal object using an intermediate String.
   *
   * @param <T> type of expected and precision, which should be the subclass of java.lang.Number and java.lang.Comparable
   * @param number the Number to convert
   * @return the built BigDecimal
   */
  private static <T extends Number> BigDecimal asBigDecimal(T number) {
    return new BigDecimal(String.valueOf(number));
  }

  /**
   * Returns true if the abs(expected - precision) is &lt;= precision, false otherwise.
   * @param actual    the actual value
   * @param expected  the expected value
   * @param precision the acceptable precision
   *
   * @param <T> type of number to compare including the precision
   * @return whether true if the abs(expected - precision) is &lt;= precision, false otherwise.
   */
  private static <T extends Number> boolean isWithinPrecision(T actual, T expected, T precision) {
    BigDecimal expectedBigDecimal = asBigDecimal(expected);
    BigDecimal actualBigDecimal = asBigDecimal(actual);
    BigDecimal absDifference = expectedBigDecimal.subtract(actualBigDecimal).abs();
    BigDecimal precisionAsBigDecimal = asBigDecimal(precision);
    return absDifference.compareTo(precisionAsBigDecimal) <= 0;
  }

}
