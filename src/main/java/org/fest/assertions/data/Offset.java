/*
 * Created on Oct 23, 2010
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.data;

import static org.fest.util.Objects.*;
import static org.fest.util.Preconditions.checkNotNull;

/**
 * A positive offset.
 *
 * @param <T> the type of the offset value.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Offset<T extends Number> {
  public final T value;

  /**
   * Creates a new {@link Offset}.
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Double> offset(Double value) {
    checkNotNull(value);
    if (value.doubleValue() < 0d) {
      throw valueNotPositive();
    }
    return new Offset<Double>(value);
  }

  /**
   * Creates a new {@link Offset}.
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Float> offset(Float value) {
    checkNotNull(value);
    if (value.floatValue() < 0f) {
      throw valueNotPositive();
    }
    return new Offset<Float>(value);
  }

  /**
   * Creates a new {@link Offset}.
   *
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static Offset<Integer> offset(Integer value) {
    checkNotNull(value);
    if (value.intValue() < 0) {
      throw valueNotPositive();
    }
    return new Offset<Integer>(value);
  }

  private static IllegalArgumentException valueNotPositive() {
    return new IllegalArgumentException("The value of the offset should be greater than zero");
  }

  private Offset(T value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Offset<?> other = (Offset<?>) obj;
    return areEqual(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(value);
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s[value=%s]", getClass().getSimpleName(), value);
  }
}
