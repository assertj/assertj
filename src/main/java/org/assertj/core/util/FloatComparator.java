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

public class FloatComparator extends NullSafeComparator<Float> {

  private float epsilon;

  public FloatComparator(float epsilon) {
    this.epsilon = epsilon;
  }
  
  public float getEpsilon() {
    return epsilon;
  }

  @Override
  public int compareNonNull(Float x, Float y) {
    if (closeEnough(x, y, epsilon)) return 0;
    return x < y ? -1 : 1;
  }

  private boolean closeEnough(Float x, Float y, float epsilon) {
    return Math.abs(x - y) < epsilon;
  }

  /**
   * handles floating point comparison according to http://floating-point-gui.de/errors/comparison/
   */
  @SuppressWarnings("unused")
  private static boolean complexCloseEnough(float a, float b, float epsilon) {
    final float absA = Math.abs(a);
    final float absB = Math.abs(b);
    final float diff = Math.abs(a - b);

    // shortcut, handles infinities
    if (a == b) return true;
    if (a == 0 || b == 0 || diff < Float.MIN_NORMAL) {
      // a or b is zero or both are extremely close to it
      // relative error is less meaningful here
      return diff < (epsilon * Float.MIN_NORMAL);
    }
    // use relative error
    return diff / Math.min((absA + absB), Float.MAX_VALUE) < epsilon;
  }

  @Override
  public int hashCode() {
    return 31 + Float.floatToIntBits(epsilon);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof FloatComparator)) return false;
    FloatComparator other = (FloatComparator) obj;
    return Float.floatToIntBits(epsilon) == Float.floatToIntBits(other.epsilon) ? true : false;
  }
}
