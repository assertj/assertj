/*
 * Created on Jan 22, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.data;

import static org.fest.util.Objects.HASH_CODE_PRIME;

/**
 * Represents a location in (x, y) coordinate space, specified in integer precision.
 * 
 * @author Yvonne Wang
 */
public class Point {

  /**
   * Creates a new </code>{@link Point}</code>.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @return the created {@code Point}.
   */
  public static Point atPoint(int x, int y) {
    return new Point(x, y);
  }

  /** The x coordinate. */
  public final int x;

  /** The y coordinate. */
  public final int y;

  private Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Point other = (Point) obj;
    if (x != other.x) return false;
    return y == other.y;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + x;
    result = HASH_CODE_PRIME * result + y;
    return result;
  }

  @Override
  public String toString() {
    return String.format("[%d, %d]", x, y);
  }
}
