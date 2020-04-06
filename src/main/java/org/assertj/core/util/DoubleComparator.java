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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.util;

import static java.lang.String.format;

public class DoubleComparator extends NullSafeComparator<Double> {

  private double precision;

  public DoubleComparator(double epsilon) {
    this.precision = epsilon;
  }

  @Override
  protected int compareNonNull(Double x, Double y) {
    if (closeEnough(x, y, precision)) return 0;
    return x < y ? -1 : 1;
  }

  public double getEpsilon() {
    return precision;
  }

  private static boolean closeEnough(Double x, Double y, double epsilon) {
    return x.doubleValue() == y.doubleValue() || Math.abs(x - y) <= epsilon;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp = Double.doubleToLongBits(precision);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof DoubleComparator)) return false;
    DoubleComparator other = (DoubleComparator) obj;
    return Double.doubleToLongBits(precision) == Double.doubleToLongBits(other.precision);
  }

  @Override
  public String toString() {
    return format("DoubleComparator[precision=%s]", precision);
  }

}
