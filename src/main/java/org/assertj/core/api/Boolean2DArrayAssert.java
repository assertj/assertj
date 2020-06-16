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

import org.assertj.core.data.Index;
import org.assertj.core.internal.Boolean2DArrays;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for two-dimensional arrays of {@code boolean}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(boolean[][])}</code>.
 * </p>
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Boolean2DArrayAssert extends Abstract2DArrayAssert<Boolean2DArrayAssert, boolean[][], Boolean> {

  private final Failures failures = Failures.instance();

  @VisibleForTesting
  protected Boolean2DArrays boolean2dArrays = Boolean2DArrays.instance();

  public Boolean2DArrayAssert(boolean[][] actual) {
    super(actual, Boolean2DArrayAssert.class);
  }

  /** {@inheritDoc} */
  @Override
  public Boolean2DArrayAssert isDeepEqualTo(boolean[][] expected) {
    if (actual == expected) return myself;
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      boolean[] actualSubArray = actual[i];
      boolean[] expectedSubArray = expected[i];

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

  /** {@inheritDoc} */
  @Override
  public void isNullOrEmpty() {
    boolean2dArrays.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public void isEmpty() {
    boolean2dArrays.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public Boolean2DArrayAssert isNotEmpty() {
    boolean2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Boolean2DArrayAssert hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    boolean2dArrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /**
   * Verifies that the actual {@code boolean[][]} has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object and primitive arrays.
   * </p>
   * Example:
   * <pre><code class='java'> boolean[][] booleanArray = {{true, true, false}, {false, false, true}};
   * char[][] charArray = {{'a', 'b', 'c'}, {'d', 'e', 'f'}};
   *
   * // assertion will pass
   * assertThat(booleanArray).hasSameDimensionsAs(charArray);
   *
   * // assertions will fail
   * assertThat(booleanArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
   * assertThat(booleanArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd', 'e'}});
   * assertThat(booleanArray).hasSameDimensionsAs(new char[][] {{'a', 'b', 'c'}, {'d', 'e'}});</code></pre>
   *
   * @param array the array to compare dimensions with actual {@code boolean[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code boolean[][]} is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual {@code boolean[][]} and given array don't have the same dimensions.
   */
  @Override
  public Boolean2DArrayAssert hasSameDimensionsAs(Object array) {
    boolean2dArrays.assertHasSameDimensionsAs(info, actual, array);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given boolean[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[][] {{true, false}, {false, true}}).contains(new boolean[] {true, false}, atIndex(O));
   *
   * // assertion will fail
   * assertThat(new boolean[][] {{true, false}, {false, true}}).contains(new boolean[] {true, false}, atIndex(1));</code></pre>
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
  public Boolean2DArrayAssert contains(boolean[] value, Index index) {
    boolean2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given boolean[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[][] {{true, false}, {false, true}}).doesNotContain(new boolean[] {true, false}, atIndex(1));
   *
   * // assertion will fail
   * assertThat(new boolean[][] {{true, false}, {false, true}}).doesNotContain(new boolean[] {true, false}, atIndex(O));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public Boolean2DArrayAssert doesNotContain(boolean[] value, Index index) {
    boolean2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }
}
