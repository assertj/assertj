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

/**
 * Assertions applicable to two-dimensional arrays,
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public interface Array2DAssert<SELF extends Array2DAssert<SELF, ELEMENT>, ELEMENT> {

  /**
   * Verifies that the actual array is {@code null} or empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[][] { }).isNullOrEmpty();
   * int[][] array = null;
   * assertThat(array).isNullOrEmpty();
   *
   * // assertion will fail
   * assertThat(new String[][] { {&quot;a&quot;}, {&quot;b&quot;} }).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the actual array is not {@code null} or not empty.
   */
  void isNullOrEmpty();

  /**
   * Verifies that the actual array is empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[][] { {} }).isEmpty();
   *
   * // assertions will fail
   * assertThat(new String[][] { {&quot;a&quot;}, {&quot;b&quot;} }).isEmpty();
   * int[][] array = null;
   * assertThat(array).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual array is not empty.
   */
  void isEmpty();

  /**
   * Verifies that the actual array is not empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new String[][] { {&quot;a&quot;}, {&quot;b&quot;} }).isNotEmpty();
   *
   * // assertions will fail
   * assertThat(new int[][] { }).isNotEmpty();
   * int[][] array = null;
   * assertThat(array).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is empty or null.
   */
  SELF isNotEmpty();

  /**
   * Verifies that the actual 2D array has the same dimensions as the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[][] { {1, 2, 3}, {4, 5, 6} }).hasDimensions(2, 3);
   *
   * // assertions will fail
   * assertThat(new int[][] { }).hasSize(1, 1);
   * assertThat(new int[][] { {1, 2, 3}, {4, 5, 6} }).hasDimensions(3, 2);
   * assertThat(new int[][] { {1, 2, 3}, {4, 5, 6, 7} }).hasDimensions(2, 3);
   * </code></pre>
   *
   * @param expectedFirstDimension the expected number of values in first dimension of the actual array.
   * @param expectedSecondDimension the expected number of values in second dimension of the actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array's dimensions are not equal to the given ones.
   */
  SELF hasDimensions(int expectedFirstDimension, int expectedSecondDimension);

  /**
   * Verifies that the actual array has the same size as given array.
   * <p>
   * Parameter is declared as Object to accept both Object[] and primitive arrays (e.g. int[]).
   * </p>
   * Example:
   * <pre><code class='java'> int[][] intArray = {{1, 2, 3}, {4, 5, 6}};
   * String[][] stringArray = new String[][] {{&quot;a&quot;, &quot;b&quot;, &quot;c&quot;}, {&quot;d&quot;, &quot;e&quot;, &quot;f&quot;}};
   *
   * // assertion will pass
   * assertThat(stringArray).hasSameSizeAs(intArray);
   *
   * // assertions will fail
   * assertThat(stringArray).hasSameSizeAs(new int[][] {{1, 2}, {3, 4}, {5, 6}});
   * assertThat(stringArray).hasSameSizeAs(new int[][] {{1, 2, 3}, {4, 5}});</code></pre>
   *
   * @param array the array to compare size with actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual array and given array don't have the same size.
   */
  SELF hasSameSizeAs(Object array);
}
