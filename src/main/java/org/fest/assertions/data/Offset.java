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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.data;

import static org.fest.util.Objects.*;

/**
 * An offset.
 * @param <T> the type of number a offset handles.
 *
 * @author Alex Ruiz
 */
public class Offset<T extends Number> {

  private final T value;

  /**
   * Creates a new double </code>{@link Offset}</code>.
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   */
  public static Offset<Double> offset(Double value) {
    return new Offset<Double>(value);
  }

  /**
   * Creates a new float </code>{@link Offset}</code>.
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   */
  public static Offset<Float> offset(Float value) {
    return new Offset<Float>(value);
  }

  /**
   * Creates a new </code>{@link Offset}</code>.
   * @param value the value of the offset.
   * @throws NullPointerException if the given value is {@code null}.
   */
  public Offset(T value) {
    if (value == null) throw new NullPointerException("The value of the offset to create should not be null");
    this.value = value;
  }

  /**
   * Returns the value of this offset.
   * @return the value of this offset.
   */
  public T value() {
    return value;
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Offset<?> other = (Offset<?>) obj;
    return (areEqual(value, other.value));
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(value);
    return result;
  }

  @Override public String toString() {
    return String.format("%s[value=%s]", getClass().getName(), value);
  }
}
