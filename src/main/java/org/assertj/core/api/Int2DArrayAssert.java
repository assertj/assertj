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
import static org.assertj.core.error.array.MultidimensionalArrayShouldBeEqual.shouldBeEqual;

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
  protected Int2DArrays arrays = Int2DArrays.instance();

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
        throw failures.failure(info, subarraysShouldHaveSameSize(actual, expected, actualSubArray, expectedSubArray, i));
      }
      for (int j = 0; j < actualSubArray.length; j++) {
        if (actualSubArray[j] != expectedSubArray[j]) {
          throw failures.failure(info, shouldBeEqual(actualSubArray[j], expectedSubArray[j],
                                                     info.representation(), "[" + i + "][" + j + "]"));
        }
      }
    }
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public void isNullOrEmpty() {
    arrays.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public void isEmpty() {
    arrays.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public Int2DArrayAssert isNotEmpty() {
    arrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Int2DArrayAssert hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    arrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Int2DArrayAssert hasSameSizeAs(Object other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given int[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[][] {{1,2}, {3,4}, {5,6}}).contains(new int[] {3,4}, atIndex(1));
   *
   * // assertions will fail
   * assertThat(new int[][] {{1,2}, {3,4}, {5,6}}).contains(new int[] {3,4}, atIndex(0));
   * assertThat(new int[][] {{1,2}, {3,4}, {5,6}}).contains(new int[] {7,8}, atIndex(2));</code></pre>
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
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[][] {{1,2}, {3,4}, {5,6}}).doesNotContain(new int[] {3,4}, atIndex(0));
   * assertThat(new int[][] {{1,2}, {3,4}, {5,6}}).doesNotContain(new int[] {7,8}, atIndex(2));
   *
   * // assertion will fail
   * assertThat(new int[][] {{1,2}, {3,4}, {5,6}}).doesNotContain(new int[] {3,4}, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public Int2DArrayAssert doesNotContain(int[] value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

}
