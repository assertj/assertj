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
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Float2DArrays;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for two-dimensional arrays of {@code float}s.
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Float2DArrayAssert extends Abstract2DArrayAssert<Float2DArrayAssert, float[][], Float> {

  @VisibleForTesting
  protected Float2DArrays float2dArrays = Float2DArrays.instance();

  private final Failures failures = Failures.instance();

  public Float2DArrayAssert(float[][] actual) {
    super(actual, Float2DArrayAssert.class);
  }

  /** {@inheritDoc} */
  @Override
  public Float2DArrayAssert isDeepEqualTo(float[][] expected) {
    if (actual == expected) return myself;
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      float[] actualSubArray = actual[i];
      float[] expectedSubArray = expected[i];

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
    float2dArrays.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public void isEmpty() {
    float2dArrays.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public Float2DArrayAssert isNotEmpty() {
    float2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Float2DArrayAssert hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    float2dArrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /**
   * Verifies that the actual {@code float[][]} has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object and primitive arrays.
   * </p>
   * Example:
   * <pre><code class='java'> float[][] floatArray = {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}};
   * char[][] charArray = {{'a', 'b', 'c'}, {'d', 'e', 'f'}};
   *
   * // assertion will pass
   * assertThat(floatArray).hasSameDimensionsAs(charArray);
   *
   * // assertions will fail
   * assertThat(floatArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
   * assertThat(floatArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd', 'e'}});
   * assertThat(floatArray).hasSameDimensionsAs(new char[][] {{'a', 'b', 'c'}, {'d', 'e'}});</code></pre>
   *
   * @param array the array to compare dimensions with actual {@code float[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code float[][]} is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual {@code float[][]} and given array don't have the same dimensions.
   */
  @Override
  public Float2DArrayAssert hasSameDimensionsAs(Object array) {
    float2dArrays.assertHasSameDimensionsAs(info, actual, array);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given float[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> float[][] values = new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}, {5.0f, 6.0f}};
   *
   * // assertion will pass
   * assertThat(values).contains(new float[] {1.0f, 2.0f}, atIndex(O))
   *                   .contains(new float[] {5.0f, 6.0f}, atIndex(2));
   *
   * // assertions will fail
   * assertThat(values).contains(new float[] {1.0f, 2.0f}, atIndex(1));
   * assertThat(values).contains(new float[] {7.0f, 8.0f}, atIndex(2));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual array.
   * @throws AssertionError if the actual array does not contain the given value at the given index.
   */
  public Float2DArrayAssert contains(float[] value, Index index) {
    float2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given float[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> float[][] values = new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}, {5.0f, 6.0f}};
   *
   * // assertion will pass
   * assertThat(values).doesNotContain(new float[] {1.0f, 2.0f}, atIndex(1))
   *                   .doesNotContain(new float[] {3.0f, 4.0f}, atIndex(0));
   *
   * // assertion will fail
   * assertThat(values).doesNotContain(new float[] {1.0f, 2.0f}, atIndex(0));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public Float2DArrayAssert doesNotContain(float[] value, Index index) {
    float2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

}
