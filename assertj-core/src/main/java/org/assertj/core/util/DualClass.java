/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util;

import static java.util.Objects.hash;

import java.util.Objects;

/**
 * Class representing a pair of two generic class reference.
 *
 * @param <A> Type of the actual value
 * @param <E> Type of the expected value
 * @author Alessandro Modolo
 */
public class DualClass<A, E> {

  private final Class<A> actual;
  private final Class<E> expected;

  DualClass(Class<A> actual, Class<E> expected) {
    this.actual = actual;
    this.expected = expected;
  }

  /**
   * Create a new instance of {@link DualClass}.
   *
   * @param actual  The class of the actual value
   * @param expected The class of the expected value
   * @param <A>   Type of the actual value
   * @param <E>   Type of the expected value
   * @return A new instance of {@link DualClass} with the specified classes reference
   */
  public static <A, E> DualClass<A, E> dualClass(Class<A> actual, Class<E> expected) {
    return new DualClass<>(actual, expected);
  }

  /**
   * Returns the class of the actual value.
   *
   * @return The class whose actual value belongs to
   */
  public Class<A> actual() {
    return actual;
  }

  /**
   * Returns the class of the expected value.
   *
   * @return The class whose expected value belongs to
   */
  public Class<E> expected() {
    return expected;
  }

  public boolean hasNoExpected() {
    return expected == null;
  }

  public String actualDescription() {
    return actual == null ? "" : actual.getName();
  }

  public String expectedDescription() {
    return expected == null ? "" : expected.getName();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DualClass<?, ?> dualClass = (DualClass<?, ?>) o;

    return Objects.equals(actual, dualClass.actual) && Objects.equals(expected, dualClass.expected);
  }

  @Override
  public int hashCode() {
    return hash(actual, expected);
  }
}
