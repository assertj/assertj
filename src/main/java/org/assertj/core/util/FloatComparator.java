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
  private boolean elaborate;

  /**
   * Handles floating point comparison according to http://floating-point-gui.de/errors/comparison/.
   * <p>
   * This is more elaborate than {@link #basicFloatComparator(float)} which simply use {@code Math.abs(x - y) < epsilon}). 
   * 
   * @param epsilon the precision used when comparing floats.
   * @return an "elaborate" float comparator that follows http://floating-point-gui.de/errors/comparison/ algorithm. 
   */
  public static FloatComparator elaborateFloatComparator(float epsilon) {
    return new FloatComparator(epsilon, true);
  }

  /**
   * Build a new {@link FloatComparator} considering {@code x} to be equal to {@code y} when {@code Math.abs(x - y) < epsilon}.
   * <p>
   * If you need a comparator that handles better case where {@code x} or {@code y} are zero, use {@link #elaborateFloatComparator(float)}. 
   * 
   * @param epsilon the precision under which {@code x} is considered to be equal to {@code y}.
   * @return a float comparator that considers {@code x} to be equal to {@code y} when {@code Math.abs(x - y) < epsilon}. 
   */
  public static FloatComparator basicFloatComparator(float epsilon) {
    return new FloatComparator(epsilon, false);
  }

  /**
   * @deprecated use {@link #basicFloatComparator(float) standardFloatComparator(float epsilon)} instead.
   */
  @Deprecated
  public FloatComparator(float epsilon) {
    this(epsilon, false);
  }

  private FloatComparator(float epsilon, boolean elaborate) {
    this.epsilon = epsilon;
    this.elaborate = elaborate;
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
    return elaborate ? elaborateCloseEnough(x, y, epsilon) : standardCloseEnough(x, y, epsilon);
  }

  private static boolean standardCloseEnough(Float x, Float y, float epsilon) {
    return Math.abs(x - y) < epsilon;
  }

  private static boolean elaborateCloseEnough(float a, float b, float epsilon) {
    final float absA = Math.abs(a);
    final float absB = Math.abs(b);
    final float diff = Math.abs(a - b);

    // shortcut, handles infinities
    if (a == b) return true;
    // a or b is zero or both are extremely close to it, relative error is less meaningful here
    if (a == 0 || b == 0 || diff < Float.MIN_NORMAL) return diff < epsilon * Float.MIN_NORMAL;
    // use relative error
    return diff / Math.min(absA + absB, Float.MAX_VALUE) < epsilon;
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
    return Float.floatToIntBits(epsilon) == Float.floatToIntBits(other.epsilon) && elaborate == other.elaborate;
  }

  @Override
  public String toString() {
    return String.format("FloatComparator [epsilon=%s, elaborate=%s]", epsilon, elaborate);
  }

}
