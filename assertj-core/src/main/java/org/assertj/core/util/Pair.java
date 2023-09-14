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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.util;

import java.util.Objects;

/**
 * Class representing a pair of generic elements, a "left" and a "right".
 *
 * @param <L> Type of the left value
 * @param <R> Type of the right value
 * @author Alessandro Modolo
 */
public class Pair<L, R> {

  private final L left;
  private final R right;

  Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  /**
   * Create a new instance of Pair.
   *
   * @param left  The element on the left
   * @param right The element on the right
   * @param <L>   Type of the left value
   * @param <R>   Type of the right value
   * @return A new instance of Pair with the specified values
   */
  public static <L, R> Pair<L, R> pair(L left, R right) {
    return new Pair<>(left, right);
  }

  /**
   * Returns the element on the left side of the pair.
   *
   * @return The element on the left
   */
  public L left() {
    return left;
  }

  /**
   * Returns the element on the right side of the pair.
   *
   * @return The element on the right
   */
  public R right() {
    return right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Pair<?, ?> pair = (Pair<?, ?>) o;

    return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
  }

  @Override
  public int hashCode() {
    int result = left != null ? left.hashCode() : 0;
    result = 31 * result + (right != null ? right.hashCode() : 0);
    return result;
  }
}
