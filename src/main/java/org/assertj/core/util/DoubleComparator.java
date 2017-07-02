/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

public class DoubleComparator extends NullSafeComparator<Double> {

  private double epsilon;

  public DoubleComparator(double epsilon) {
    this.epsilon = epsilon;
  }

  @Override
  protected int compareNonNull(Double x, Double y) {
    if (closeEnough(x, y, epsilon)) return 0;
    return x < y ? -1 : 1;
  }

  public double getEpsilon() {
    return epsilon;
  }

  /**
   * handles floating point comparison according to http://floating-point-gui.de/errors/comparison/
   */
  @SuppressWarnings("unused")
  private static boolean complexCloseEnough(double a, double b, double epsilon) {
    final double absA = Math.abs(a);
    final double absB = Math.abs(b);
    final double diff = Math.abs(a - b);

    // shortcut, handles infinities
    if (a == b) return true;
    if (a == 0 || b == 0 || diff < Double.MIN_NORMAL) {
      // a or b is zero or both are extremely close to it
      // relative error is less meaningful here
      return diff < (epsilon * Double.MIN_NORMAL);
    }
    // use relative error
    return diff / Math.min((absA + absB), Double.MAX_VALUE) < epsilon;
  }

  private static boolean closeEnough(Double x, Double y, double epsilon) {
    return Math.abs(x - y) < epsilon;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp = Double.doubleToLongBits(epsilon);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof DoubleComparator)) return false;
    DoubleComparator other = (DoubleComparator) obj;
    return Double.doubleToLongBits(epsilon) == Double.doubleToLongBits(other.epsilon) ? true : false;
  }

}
