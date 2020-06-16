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
package org.assertj.core.api;

import java.util.Comparator;

/**
 * Base class for all two-dimensional array assertions.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <ACTUAL> the type of the "actual" value which is a two-dimensional Array of ELEMENT.
 * @param <ELEMENT> the type of the "actual" array element.
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public abstract class Abstract2DArrayAssert<SELF extends Abstract2DArrayAssert<SELF, ACTUAL, ELEMENT>, ACTUAL, ELEMENT>
    extends AbstractAssert<SELF, ACTUAL> implements Array2DAssert<SELF, ELEMENT> {

  public Abstract2DArrayAssert(final ACTUAL actual, final Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual 2D array is <b>deeply</b> equal to the given one.
   * <p>
   * Two arrays are considered deeply equal if both are {@code null}
   * or if they refer to arrays that contain the same number of elements and
   * all corresponding pairs of elements in the two arrays are deeply equal.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[][] {{1, 2}, {3, 4}}).isDeepEqualTo(new int[][] {{1, 2}, {3, 4}});
   *
   * // assertions will fail
   * assertThat(new int[][] {{1, 2}, {3, 4}}).isDeepEqualTo(new int[][] {{1, 2}, {9, 10}});
   * assertThat(new int[][] {{1, 2}, {3, 4}}).isDeepEqualTo(new int[][] {{1, 2, 3}, {4}});</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not deeply equal to the given one.
   */
  public abstract SELF isDeepEqualTo(ACTUAL expected);

  /**
   * Verifies that the actual {@code int[][]} is equal to the given one.
   * <p>
   * <b>WARNING!</b> This method will use {@code equals} to compare (it will compare arrays references only).<br>
   * Unless you specify a comparator with {@link #usingComparator(Comparator)}, it is advised to use
   * {@link Abstract2DArrayAssert#isDeepEqualTo(Object)} instead.
   * <p>
   * Example:
   * <pre><code class='java'> int[][] array = {{1, 2}, {3, 4}};
   *
   * // assertion will pass
   * assertThat(array).isEqualTo(array);
   *
   * // assertion will fail as isEqualTo calls equals which compares arrays references only.
   * assertThat(array).isEqualTo(new int[][] {{1, 2}, {3, 4}});</code></pre>
   *
   * @param expected the given value to compare the actual {@code int[][]} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code int[][]} is not equal to the given one.
   */
  @Override
  public SELF isEqualTo(Object expected) {
    return super.isEqualTo(expected);
  }
}
