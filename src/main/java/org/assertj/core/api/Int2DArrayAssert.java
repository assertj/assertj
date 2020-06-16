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

import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.SubarraysShouldHaveSameSize.subarraysShouldHaveSameSize;
import static org.assertj.core.error.array2d.Array2dElementShouldBeDeepEqual.elementShouldBeEqual;

import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Int2DArrays;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for two-dimensional arrays of {@code int}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(int[][])}</code>.
 * </p>
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Int2DArrayAssert extends Abstract2DArrayAssert<Int2DArrayAssert, int[][], Integer> {

  @VisibleForTesting
  protected Int2DArrays int2dArrays = Int2DArrays.instance();

  private final Failures failures = Failures.instance();

  public Int2DArrayAssert(int[][] actual) {
    super(actual, Int2DArrayAssert.class);
  }

  /** {@inheritDoc} */
  @Override
  public Int2DArrayAssert isDeepEqualTo(int[][] expected) {
    if (actual == expected) return myself;
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      int[] actualSubArray = actual[i];
      int[] expectedSubArray = expected[i];

      if (actualSubArray == expectedSubArray) continue;
      if (actualSubArray == null) throw failures.failure(info, shouldNotBeNull("actual[" + i + "]"));
      if (expectedSubArray.length != actualSubArray.length) {
        throw failures.failure(info, subarraysShouldHaveSameSize(actual, expected, actualSubArray, actualSubArray.length,
                                                                 expectedSubArray, expectedSubArray.length, i),
                               info.representation().toStringOf(actual), info.representation().toStringOf(expected));
      }
      for (int j = 0; j < actualSubArray.length; j++) {
        if (actualSubArray[j] != expectedSubArray[j]) {
          throw failures.failure(info, elementShouldBeEqual(actualSubArray[j], expectedSubArray[j], i, j),
                                 info.representation().toStringOf(actual), info.representation().toStringOf(expected));
        }
      }
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code int[][]} is equal to the given one.
   * <p>
   * <b>WARNING!</b> This method will use {@code equals} to compare (it will compare arrays references only).<br>
   * Unless you specify a comparator with {@link #usingComparator(Comparator)}, it is advised to use
   * {@link Int2DArrayAssert#isDeepEqualTo(Object)} instead.
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
  public Int2DArrayAssert isEqualTo(Object expected) {
    return super.isEqualTo(expected);
  }

  /** {@inheritDoc} */
  @Override
  public void isNullOrEmpty() {
    int2dArrays.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public void isEmpty() {
    int2dArrays.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public Int2DArrayAssert isNotEmpty() {
    int2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Int2DArrayAssert hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    int2dArrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /**
   * Verifies that the actual {@code int[][]} has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object and primitive arrays.
   * </p>
   * Example:
   * <pre><code class='java'> int[][] intArray = {{1, 2, 3}, {4, 5, 6}};
   * char[][] charArray = {{'a', 'b', 'c'}, {'d', 'e', 'f'}};
   *
   * // assertion will pass
   * assertThat(intArray).hasSameDimensionsAs(charArray);
   *
   * // assertions will fail
   * assertThat(intArray).hasSameDimensionsAs(new int[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
   * assertThat(intArray).hasSameDimensionsAs(new int[][] {{'a', 'b'}, {'c', 'd', 'e'}});
   * assertThat(intArray).hasSameDimensionsAs(new int[][] {{'a', 'b', 'c'}, {'d', 'e'}});</code></pre>
   *
   * @param array the array to compare dimensions with actual {@code int[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code int[][]} is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual {@code int[][]} and given array don't have the same dimensions.
   */
  @Override
  public Int2DArrayAssert hasSameDimensionsAs(Object array) {
    int2dArrays.assertHasSameDimensionsAs(info, actual, array);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given int[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[][] {{1, 2}, {3, 4}, {5, 6}}).contains(new int[] {3, 4}, atIndex(1));
   *
   * // assertions will fail
   * assertThat(new int[][] {{1, 2}, {3, 4}, {5, 6}}).contains(new int[] {3, 4}, atIndex(0));
   * assertThat(new int[][] {{1, 2}, {3, 4}, {5, 6}}).contains(new int[] {7, 8}, atIndex(2));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual array.
   * @throws AssertionError if the actual array does not contain the given value at the given index.
   */
  public Int2DArrayAssert contains(int[] value, Index index) {
    int2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[][] {{1, 2}, {3, 4}, {5, 6}}).doesNotContain(new int[] {3, 4}, atIndex(0));
   * assertThat(new int[][] {{1, 2}, {3, 4}, {5, 6}}).doesNotContain(new int[] {7, 8}, atIndex(2));
   *
   * // assertion will fail
   * assertThat(new int[][] {{1, 2}, {3, 4}, {5, 6}}).doesNotContain(new int[] {3, 4}, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public Int2DArrayAssert doesNotContain(int[] value, Index index) {
    int2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

}
