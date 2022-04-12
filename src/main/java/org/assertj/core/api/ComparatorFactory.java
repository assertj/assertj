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

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

import static java.lang.Math.abs;

public class ComparatorFactory {

  public static final ComparatorFactory INSTANCE = new ComparatorFactory();

  /**
   * Judge whether the input number is Nan or Infinity
   *
   * @param number Object of Float or Double class
   * @param <T>    type of expected and precision, which should be the subclass of java.lang.Number and java.lang.Comparable
   * @return whether the object is Nan or Infinity
   * @throws java.lang.NullPointerException if number is null
   */
  public static <T extends Number & Comparable<T>> boolean isNanOrInfinity(T number) {
    // avoid null argument
    if (number instanceof Float) {
      return ((Float) number).isNaN() || ((Float) number).isInfinite();
    } else if (number instanceof Double) {
      return ((Double) number).isNaN() || ((Double) number).isInfinite();
    }

    return false;

  }

  /**
   * Use the Combination of java.math.BigDecimal and String.valueOf to create precise BigDecimal object.
   *
   * @param number Object of Float or Double class
   * @param <T>    type of expected and precision, which should be the subclass of java.lang.Number and java.lang.Comparable
   * @return the BigDecimalObject
   */

  public static <T extends Number & Comparable<T>> BigDecimal asBigDecimal(T number) {
    return new BigDecimal(String.valueOf(number));
  }

  /**
   * Use java.math.BigDecimal to fix the problem of float/double precision.
   *
   * @param expected  the expected value
   * @param actual    the actual value
   * @param precision the acceptable precision
   * @param <T>       type of expected and precision, which should be the subclass of java.lang.Number and java.lang.Comparable
   * @return whether the abs(expected - precisioon) is less or equal to precision
   */

  private static <T extends Number & Comparable<T>> boolean compareAsBigDecimalWithPrecision(T expected, T actual, T precision) {
    // java.math.BigDecimal cannot handle NAN or Infinity cases.
    BigDecimal expectedBigDecimal = asBigDecimal(expected);
    BigDecimal actualBigDecimal = asBigDecimal(actual);
    BigDecimal absDifference = expectedBigDecimal.subtract(actualBigDecimal).abs();
    BigDecimal precisionBigDecimal = asBigDecimal(precision);

    return absDifference.compareTo(precisionBigDecimal) <= 0;
  }

  public Comparator<Double> doubleComparatorWithPrecision(double precision) {
    // can't use <> with anonymous class in java 8
    return new Comparator<Double>() {
      @Override
      public int compare(Double o1, Double o2) {
        if(isNanOrInfinity(precision)){
          throw new IllegalArgumentException("Precision should not be Nan or Infinity!");
        }

        if(isNanOrInfinity(o1) || isNanOrInfinity(o2)){
          return Double.compare(o1, o2);
        }
        if (compareAsBigDecimalWithPrecision(o1, o2, precision)) {
          return 0;
        }
        return Double.compare(o1, o2);
      }

      @Override
      public String toString() {
        return "double comparator at precision " + precision;
      }
    };
  }

  public Comparator<Float> floatComparatorWithPrecision(float precision) {
    // can't use <> with anonymous class in java 8
    return new Comparator<Float>() {
      @Override
      public int compare(Float o1, Float o2) {
        if(isNanOrInfinity(precision)){
          throw new IllegalArgumentException("Precision should not be Nan or Infinity!");
        }
        if(isNanOrInfinity(o1) || isNanOrInfinity(o2)){
          return Float.compare(o1, o2);
        }
        if (compareAsBigDecimalWithPrecision(o1, o2, precision)) {
          return 0;
        }
        return Float.compare(o1, o2);
      }

      @Override
      public String toString() {
        return "float comparator at precision " + precision;
      }
    };
  }

}
