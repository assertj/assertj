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

  /**
   * Verifies that the actual {@code double[][]} is <b>deeply</b> equal to the given one.
   * <p>
   * Two arrays are considered deeply equal if both are {@code null}
   * or if they refer to arrays that contain the same number of elements and
   * all corresponding pairs of elements in the two arrays are deeply equal.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new double[][] {{1.0, 2.0}, {3.0, 4.0}}).isDeepEqualTo(new double[][] {{1.0, 2.0}, {3.0, 4.0}});
   *
   * // assertions will fail
   * assertThat(new double[][] {{1.0, 2.0}, {3.0, 4.0}}).isDeepEqualTo(new double[][] {{1.0, 2.0}, {9.0, 10.0}});
   * assertThat(new double[][] {{1.0, 2.0}, {3.0, 4.0}}).isDeepEqualTo(new double[][] {{1.0, 2.0, 3.0}, {4.0}});</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not deeply equal to the given one.
   */
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

  /**
   * Verifies that the actual {@code double[][]} is equal to the given one.
   * <p>
   * <b>WARNING!</b> This method will use {@code equals} to compare (it will compare arrays references only).<br>
   * Unless you specify a comparator with {@link #usingComparator(Comparator)}, it is advised to use
   * {@link Double2DArrayAssert#isDeepEqualTo(Object)} instead.
   * <p>
   * Example:
   * <pre><code class='java'> double[][] array = {{1.0, 2.0}, {3.0, 4.0}};
   *
   * // assertion will pass
   * assertThat(array).isEqualTo(array);
   *
   * // assertion will fail as isEqualTo calls equals which compares arrays references only.
   * assertThat(array).isEqualTo(new double[][] {{1.0, 2.0}, {3.0, 4.0}});</code></pre>
   *
   * @param expected the given value to compare the actual {@code double[][]} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code double[][]} is not equal to the given one.
   */
  @Override
  public Double2DArrayAssert isEqualTo(Object expected) {
    return super.isEqualTo(expected);
  }

  /**
   * Verifies that the actual {@code double[][]} is {@code null} or empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * double[][] array = null;
   * assertThat(array).isNullOrEmpty();
   * assertThat(new double[][] { }).isNullOrEmpty();
   * assertThat(new double[][] {{ }}).isNullOrEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new double[][] {{ }, { }, { }}).isNullOrEmpty();
   *
   * // assertion will fail
   * assertThat(new double[][] {{ 1.0 }, { 2.0 }}).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code double[][]} is not {@code null} or not empty.
   */
  @Override
  public void isNullOrEmpty() {
    double2dArrays.assertNullOrEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code double[][]} is empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new double[][] {{}}).isEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new double[][] {{ }, { }, { }}).isEmpty();
   *
   * // assertions will fail
   * assertThat(new double[][] {{ 1.0 }, { 2.0 }}).isEmpty();
   * double[][] array = null;
   * assertThat(array).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code double[][]} is not empty.
   */
  @Override
  public void isEmpty() {
    double2dArrays.assertEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code double[][]} is not empty, not empty means the array has at least one element.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new double[][] {{ 1.0 }, { 2.0 }}).isNotEmpty();
   * assertThat(new double[][] {{ }, { 2.0 }}).isNotEmpty();
   *
   * // assertions will fail
   * assertThat(new double[][] { }).isNotEmpty();
   * assertThat(new double[][] {{ }}).isNotEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new double[][] {{ }, { }, { }}).isNotEmpty();
   * double[][] array = null;
   * assertThat(array).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code double[][]} is empty or null.
   */
  @Override
  public Double2DArrayAssert isNotEmpty() {
    double2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code double[][]} has the given dimensions.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}).hasDimensions(2, 3);
   *
   * // assertions will fail
   * assertThat(new double[][] { }).hasSize(1, 1);
   * assertThat(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}}).hasDimensions(3, 2);
   * assertThat(new double[][] {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0, 7.0}}).hasDimensions(2, 3); </code></pre>
   *
   * @param expectedFirstDimension the expected number of values in first dimension of the actual {@code double[][]}.
   * @param expectedSecondDimension the expected number of values in second dimension of the actual {@code double[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code double[][]}'s dimensions are not equal to the given ones.
   */
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
   * Verifies that the actual {@code double[][]} contains the given double[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> double[][] values = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
   *
   * // assertion will pass
   * assertThat(values).contains(new double[] {1.0, 2.0}, atIndex(0))
   *                   .contains(new double[] {5.0, 6.0}, atIndex(2));
   *
   * // assertions will fail
   * assertThat(values).contains(new double[] {1.0, 2.0}, atIndex(1));
   * assertThat(values).contains(new double[] {6.0, 10.0}, atIndex(2));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual {@code double[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code double[][]} is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual {@code double[][]}.
   * @throws AssertionError if the actual {@code double[][]} does not contain the given value at the given index.
   */
  public Double2DArrayAssert contains(double[] value, Index index) {
    double2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual {@code double[][]} does not contain the given double[] at the given index.
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
   * @param index the index where the value should be stored in the actual {@code double[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code double[][]} is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual {@code double[][]} contains the given value at the given index.
   */
  public Double2DArrayAssert doesNotContain(double[] value, Index index) {
    double2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }
}
