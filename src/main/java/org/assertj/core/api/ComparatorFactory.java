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

public class ComparatorFactory {

  public static final ComparatorFactory INSTANCE = new ComparatorFactory();

  private static <T extends Number & Comparable<T>> BigDecimal preciseBigDecimal(T number) {
    return new BigDecimal(String.valueOf(number));
  }

  private static <T extends Number & Comparable<T>> boolean compareAsBigDecimalWithPrecision(T expected, T actual, T precision) {
    // use java.math.BigDecimal to fix the problem of float/double precision.
    BigDecimal expectedBigDecimal = preciseBigDecimal(expected);
    BigDecimal actualBigDecimal = preciseBigDecimal(actual);
    BigDecimal absDifference = expectedBigDecimal.subtract(actualBigDecimal).abs();
    BigDecimal precisionBigDecimal = preciseBigDecimal(precision);

    return absDifference.compareTo(precisionBigDecimal) <= 0;
  }

  public Comparator<Double> doubleComparatorWithPrecision(double precision) {
    // can't use <> with anonymous class in java 8
    return new Comparator<Double>() {
      @Override
      public int compare(Double o1, Double o2) {
        if (compareAsBigDecimalWithPrecision(o1, o2, precision)) {
          return 0;
        }
        return o1 - o2 > 0 ? 1 : -1;
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
        if (compareAsBigDecimalWithPrecision(o1, o2, precision)) {
          return 0;
        }
        return o1 - o2 > 0 ? 1 : -1;
      }

      @Override
      public String toString() {
        return "float comparator at precision " + precision;
      }
    };
  }

}
