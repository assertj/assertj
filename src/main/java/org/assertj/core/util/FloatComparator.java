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

public class FloatComparator extends NullSafeComparator<Float> {

  private float precision;

  public FloatComparator(float epsilon) {
    this.precision = epsilon;
  }
  
  public float getEpsilon() {
    return precision;
  }

  @Override
  public int compareNonNull(Float x, Float y) {
    if (closeEnough(x, y, precision)) return 0;
    return x < y ? -1 : 1;
  }

  private boolean closeEnough(Float x, Float y, float epsilon) {
    return x.floatValue() == y.floatValue() || Math.abs(x - y) <= epsilon;
  }

  @Override
  public int hashCode() {
    return 31 + Float.floatToIntBits(precision);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof FloatComparator)) return false;
    FloatComparator other = (FloatComparator) obj;
    return Float.floatToIntBits(precision) == Float.floatToIntBits(other.precision);
  }

  @Override
  public String toString() {
    return format("FloatComparator[precision=%s]", precision);
  }
}
