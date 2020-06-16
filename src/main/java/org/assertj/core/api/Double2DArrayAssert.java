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
import org.assertj.core.internal.Double2DArrays;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for two-dimensional arrays of {@code double}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(double[][])}</code>.
 * </p>
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Double2DArrayAssert extends Abstract2DArrayAssert<Double2DArrayAssert, double[][], Double> {

  private final Failures failures = Failures.instance();

  @VisibleForTesting
  protected Double2DArrays double2dArrays = Double2DArrays.instance();

  public Double2DArrayAssert(double[][] actual) {
    super(actual, Double2DArrayAssert.class);
  }

  /** {@inheritDoc} */
  @Override
  public Double2DArrayAssert isDeepEqualTo(double[][] expected) {
    if (actual == expected) return myself;
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      double[] actualSubArray = actual[i];
      double[] expectedSubArray = expected[i];

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
    double2dArrays.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public void isEmpty() {
    double2dArrays.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public Double2DArrayAssert isNotEmpty() {
    double2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Double2DArrayAssert hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    double2dArrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /**
   * Verifies that the actual {@code double[][]} has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object and primitive arrays.
   * </p>
   * Example:
   * <pre><code class='java'> double[][] doubleArray = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
   * char[][] charArray = {{'a', 'b', 'c'}, {'d', 'e', 'f'}};
   *
   * // assertion will pass
   * assertThat(doubleArray).hasSameDimensionsAs(charArray);
   *
   * // assertions will fail
   * assertThat(doubleArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
   * assertThat(doubleArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd', 'e'}});
   * assertThat(doubleArray).hasSameDimensionsAs(new char[][] {{'a', 'b', 'c'}, {'d', 'e'}});</code></pre>
   *
   * @param array the array to compare dimensions with actual {@code double[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code double[][]} is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual {@code double[][]} and given array don't have the same dimensions.
   */
  @Override
  public Double2DArrayAssert hasSameDimensionsAs(Object array) {
    double2dArrays.assertHasSameDimensionsAs(info, actual, array);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given double[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> double[][] values = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
   *
   * // assertion will pass
   * assertThat(values).contains(new double[] {1.0, 2.0}, atIndex(O))
   *                   .contains(new double[] {5.0, 6.0}, atIndex(2));
   *
   * // assertions will fail
   * assertThat(values).contains(new double[] {1.0, 2.0}, atIndex(1));
   * assertThat(values).contains(new double[] {6.0, 10.0}, atIndex(2));</code></pre>
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
  public Double2DArrayAssert contains(double[] value, Index index) {
    double2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given double[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> double[][] values = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
   *
   * // assertion will pass
   * assertThat(values).doesNotContain(new double[] {1.0, 2.0}, atIndex(1))
   *                   .doesNotContain(new double[] {3.0, 4.0}, atIndex(0));
   *
   * // assertion will fail
   * assertThat(values).doesNotContain(new double[] {1.0, 2.0}, atIndex(0));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public Double2DArrayAssert doesNotContain(double[] value, Index index) {
    double2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }
}
