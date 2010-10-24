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

/**
 * A finite increment in a variable.
 * @param <T> the type of number a delta handles.
 *
 * @author Alex Ruiz
 */
public final class Delta<T extends Number> {

  private final T value;

  /**
   * Creates a new </code>{@link Delta}</code> with a {@code Double} value.
   * @param value the value of the delta.
   * @return the created {@code Delta}.
   * @throws NullPointerException if the given value is {@code null}.
   */
  public static Delta<Double> delta(Double value) {
    return new Delta<Double>(value);
  }

  /**
   * Creates a new </code>{@link Delta}</code> with a {@code Float} value.
   * @param value the value of the delta.
   * @return the created {@code Delta}.
   * @throws NullPointerException if the given value is {@code null}.
   */
  public static Delta<Float> delta(Float value) {
    return new Delta<Float>(value);
  }

  /**
   * Creates a new </code>{@link Delta}</code>.
   * @param value the value of the delta.
   * @throws NullPointerException if the given value is {@code null}.
   */
  public Delta(T value) {
    if (value == null) throw new NullPointerException("The value of the delta to create should not be null");
    this.value = value;
  }

  /**
   * Returns the value of this delta.
   * @return the value of this delta.
   */
  public T value() {
    return value;
  }
}
