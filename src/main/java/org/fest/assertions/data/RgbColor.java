/*
 * Created on Oct 24, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.data;

import static java.lang.Math.abs;
import static org.fest.util.Objects.HASH_CODE_PRIME;

/**
 * A color.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public final class RgbColor {

  /** The red component of this color. */
  public final int r;

  /** The green component of this color. */
  public final int g;

  /** The blue component of this color. */
  public final int b;

  /**
   * Creates a new </code>{@link RgbColor}</code>.
   * @param rgb a value representing a RGB combination.
   * @return the created color.
   */
  public static RgbColor color(int rgb) {
    return new RgbColor(extract(rgb, 16), extract(rgb, 8), extract(rgb, 0));
  }

  private static int extract(int rgb, int value) {
    return (rgb >> value) & 0xFF;
  }

  private RgbColor(int r, int g, int b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  /**
   * Indicates whether the given <code>{@link RgbColor}</code> is equal to this one.
   * @param color the {@code RgbColor} to compare this one to.
   * @param offset used to tolerate a difference between the individual components of the {@code RgbColor}s to compare.
   * @return {@code true} if the given {@code RgbColor} is equal to this one; {@code false} otherwise.
   * @throws NullPointerException if the given offset is {@code null}.
   */
  public boolean isEqualTo(RgbColor color, Offset<Integer> offset) {
    if (offset == null) throw new NullPointerException("The given offset should not be null");
    if (equals(color)) return true;
    if (color == null) return false;
    int offsetValue = offset.value.intValue();
    if (abs(r - color.r) > offsetValue) return false;
    if (abs(g - color.g) > offsetValue) return false;
    return abs(b - color.b) <= offsetValue;
  }

  /** {@inheritDoc} */
  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + r;
    result = HASH_CODE_PRIME * result + g;
    result = HASH_CODE_PRIME * result + b;
    return result;
  }

  /** {@inheritDoc} */
  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    RgbColor other = (RgbColor) obj;
    if (r != other.r) return false;
    if (g != other.g) return false;
    return b == other.b;
  }

  @Override public String toString() {
    return String.format("color[r=%d,g=%d,b=%d]", r, g, b);
  }
}
