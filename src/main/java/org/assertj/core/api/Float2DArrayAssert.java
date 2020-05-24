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
import static org.assertj.core.error.ShouldHaveSameSizeOfSubArrayAs.shouldHaveSameSizeOfSubarrayAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.array.ShouldBeEqual.shouldBeEqual;

import org.assertj.core.data.Index;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Float2DArrays;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for two-dimensional arrays of {@code float}s.
 *
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 * @author Maciej Wajcht
 */
public class Float2DArrayAssert extends Abstract2DArrayAssert<Float2DArrayAssert, float[][], Float> {

  @VisibleForTesting
  protected Float2DArrays arrays = Float2DArrays.instance();

  private final Failures failures = Failures.instance();

  public Float2DArrayAssert(float[][] actual) {
    super(actual, Float2DArrayAssert.class);
  }

  public Float2DArrayAssert isDeepEqualTo(float[][] expected) {
    if (actual == expected) {
      return myself;
    }
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      float[] actualSubArray = actual[i];
      float[] expectedSubArray = expected[i];

      if (actualSubArray == expectedSubArray) {
        continue;
      }
      if (actualSubArray == null) {
        throw failures.failure(info, shouldNotBeNull("actual[" + i + "]"));
      }
      if (expectedSubArray.length != actualSubArray.length) {
        throw failures.failure(info, shouldHaveSameSizeOfSubarrayAs(actual, expected, actualSubArray, expectedSubArray, actualSubArray.length,
          expectedSubArray.length, i));
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
  public Float2DArrayAssert isNotEmpty() {
    arrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Float2DArrayAssert hasSize(int expectedFirstDimensionSize, int expectedSecondDimensionSize) {
    arrays.assertHasSize(info, actual, expectedFirstDimensionSize, expectedSecondDimensionSize);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Float2DArrayAssert hasSameSizeAs(Object other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
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
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> float[][] values = new float[][] {{1.0f, 2.0f}, {3.0f, 4.0f}, {5.0f, 6.0f}};
   *
   * // assertion will pass
   * assertThat(values).doesNotContain(new float[] {1.0f, 2.0f}, atIndex(1))
   *                   .doesNotContain(new float[] {3.0f, 4.0f}, atIndex(0));
   *
   * // assertions will fail
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
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

}
