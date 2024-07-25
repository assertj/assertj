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
 * Assertions applicable to two-dimensional arrays.
 *
 * @author Maciej Wajcht
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @since 3.17.0
 */
public interface Array2DAssert<SELF extends Array2DAssert<SELF, ELEMENT>, ELEMENT> {

  /**
   * Verifies that the actual array is {@code null} or empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * int[][] array = null;
   * assertThat(array).isNullOrEmpty();
   * assertThat(new int[][] { }).isNullOrEmpty();
   * assertThat(new int[][] {{ }}).isNullOrEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new int[][] {{ }, { }, { }}).isNullOrEmpty();
   *
   * // assertion will fail
   * assertThat(new int[][] {{ 1 }, { 2 }}).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the actual array is not {@code null} or not empty.
   */
  void isNullOrEmpty();

  /**
   * Verifies that the actual array is empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[][] {{}}).isEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new int[][] {{ }, { }, { }}).isEmpty();
   *
   * // assertions will fail
   * assertThat(new int[][] {{ 1 }, { 2 }}).isEmpty();
   * int[][] array = null;
   * assertThat(array).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual array is not empty.
   */
  void isEmpty();

  /**
   * Verifies that the actual array is not empty, not empty means the array has at least one element.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[][] {{ 1 }, { 2 }}).isNotEmpty();
   * assertThat(new int[][] {{ }, { 2 }}).isNotEmpty();
   *
   * // assertions will fail
   * assertThat(new int[][] { }).isNotEmpty();
   * assertThat(new int[][] {{ }}).isNotEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new int[][] {{ }, { }, { }}).isNotEmpty();
   * int[][] array = null;
   * assertThat(array).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is empty or null.
   */
  SELF isNotEmpty();

  /**
   * Verifies that the actual 2D array has the given dimensions.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[][] {{1, 2, 3}, {4, 5, 6}}).hasDimensions(2, 3);
   *
   * // assertions will fail
   * assertThat(new int[][] { }).hasSize(1, 1);
   * assertThat(new int[][] {{1, 2, 3}, {4, 5, 6}}).hasDimensions(3, 2);
   * assertThat(new int[][] {{1, 2, 3}, {4, 5, 6, 7}}).hasDimensions(2, 3); </code></pre>
   *
   * @param expectedFirstDimension the expected number of values in first dimension of the actual array.
   * @param expectedSecondDimension the expected number of values in second dimension of the actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array's dimensions are not equal to the given ones.
   */
  SELF hasDimensions(int expectedFirstDimension, int expectedSecondDimension);

  /**
   * Verifies that the actual two-dimensional array has the given number of rows.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[][] {{1, 2, 3}, {4, 5, 6}}).hasNumberOfRows(2);
   * assertThat(new int[][] {{1}, {1, 2}, {1, 2, 3}}).hasNumberOfRows(3);
   *
   * // assertions will fail
   * assertThat(new int[][] { }).hasNumberOfRows(1);
   * assertThat(new int[][] {{1, 2, 3}, {4, 5, 6}}).hasNumberOfRows(3);
   * assertThat(new int[][] {{1, 2, 3}, {4, 5, 6, 7}}).hasNumberOfRows(1); </code></pre>
   *
   * @param expected the expected number of rows of the two-dimensional array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual number of rows are not equal to the given one.
   */
  SELF hasNumberOfRows(int expected);

  /**
   * Verifies that the actual array has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object[] and primitive arrays (e.g. int[]).
   * </p>
   * Example:
   * <pre><code class='java'> String[][] stringArray = {{&quot;a&quot;, &quot;b&quot;, &quot;c&quot;}, {&quot;d&quot;, &quot;e&quot;, &quot;f&quot;}};
   * int[][] intArray = {{1, 2, 3}, {4, 5, 6}};
   *
   * // assertion will pass
   * assertThat(stringArray).hasSameDimensionsAs(intArray);
   *
   * // assertions will fail
   * assertThat(stringArray).hasSameDimensionsAs(new int[][] {{1, 2}, {3, 4}, {5, 6}});
   * assertThat(stringArray).hasSameDimensionsAs(new int[][] {{1, 2}, {3, 4, 5}});
   * assertThat(stringArray).hasSameDimensionsAs(new int[][] {{1, 2, 3}, {4, 5}});</code></pre>
   *
   * @param array the array to compare dimensions with actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual array and given array don't have the same dimensions.
   */
  SELF hasSameDimensionsAs(Object array);
}
