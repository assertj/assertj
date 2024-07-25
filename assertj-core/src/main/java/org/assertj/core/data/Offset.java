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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.data;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.Objects;

/**
 * A positive offset.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 *
 * @param <T> the type of the offset value.
 */
public final class Offset<T extends Number> {

  public final T value;
  /**
   * When |actual-expected|=offset and strict is true the assertThat(actual).isCloseTo(expected, offset); assertion will fail.
   */
  public final boolean strict;

  /**
   * Creates a new {@link Offset} that let {@code isCloseTo} assertions pass when {@code |actual-expected| <= offset value}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(8.1).isCloseTo(8.0, offset(0.2));
   * assertThat(8.1).isCloseTo(8.0, offset(0.1));
   *
   * // assertion fails
   * assertThat(8.1).isCloseTo(8.0, offset(0.01));</code></pre>
   *
   * @param <T> the type of value of the {@link Offset}.
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static <T extends Number> Offset<T> offset(T value) {
    requireNonNull(value);
    checkArgument(value.doubleValue() >= 0d, "An offset value should be greater than or equal to zero");
    return new Offset<>(value, false);
  }

  /**
   * Creates a new strict {@link Offset} that let {@code isCloseTo} assertion pass when {@code |actual-expected| < offset value}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(8.1).isCloseTo(8.0, offset(0.2));
   *
   * // assertions fail
   * assertThat(8.1).isCloseTo(8.0, offset(0.1));
   * assertThat(8.1).isCloseTo(8.0, offset(0.01));</code></pre>
   *
   * @param <T> the type of value of the {@link Offset}.
   * @param value the value of the offset.
   * @return the created {@code Offset}.
   * @throws NullPointerException if the given value is {@code null}.
   * @throws IllegalArgumentException if the given value is negative.
   */
  public static <T extends Number> Offset<T> strictOffset(T value) {
    requireNonNull(value);
    checkArgument(value.doubleValue() > 0d, "A strict offset value should be greater than zero");
    return new Offset<>(value, true);
  }

  private Offset(T value, boolean strict) {
    this.value = value;
    this.strict = strict;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Offset)) return false;
    Offset<?> other = (Offset<?>) obj;
    return strict == other.strict && Objects.equals(value, other.value);
  }

  @Override
  public int hashCode() {
    return hash(value, strict);
  }

  @Override
  public String toString() {
    return format("%s%s[value=%s]", strict ? "strict " : "", getClass().getSimpleName(), value);
  }

}
