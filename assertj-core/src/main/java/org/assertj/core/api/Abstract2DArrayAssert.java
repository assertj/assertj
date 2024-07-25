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
package org.assertj.core.api;

/**
 * Base class for all two-dimensional array assertions.
 *
 * @author Maciej Wajcht
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <ACTUAL> the type of the "actual" value which is a two-dimensional Array of ELEMENT.
 * @param <ELEMENT> the type of the "actual" array element.
 *
 * @since 3.17.0
 */
public abstract class Abstract2DArrayAssert<SELF extends Abstract2DArrayAssert<SELF, ACTUAL, ELEMENT>, ACTUAL, ELEMENT>
    extends AbstractAssert<SELF, ACTUAL> implements Array2DAssert<SELF, ELEMENT> {

  protected Abstract2DArrayAssert(final ACTUAL actual, final Class<?> selfType) {
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
   * @throws AssertionError if the actual 2D array is not deeply equal to the given one.
   */
  public abstract SELF isDeepEqualTo(ACTUAL expected);

}
