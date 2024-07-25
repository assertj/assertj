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

import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.SubarraysShouldHaveSameSize.subarraysShouldHaveSameSize;
import static org.assertj.core.error.array2d.Array2dElementShouldBeDeepEqual.elementShouldBeEqual;

import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.internal.Byte2DArrays;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for two-dimensional arrays of {@code byte}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(byte[][])}</code>.
 * </p>
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Byte2DArrayAssert extends Abstract2DArrayAssert<Byte2DArrayAssert, byte[][], Byte> {

  private final Failures failures = Failures.instance();

  @VisibleForTesting
  protected Byte2DArrays byte2dArrays = Byte2DArrays.instance();

  public Byte2DArrayAssert(byte[][] actual) {
    super(actual, Byte2DArrayAssert.class);
  }

  /**
   * Verifies that the actual {@code byte[][]} is <b>deeply</b> equal to the given one.
   * <p>
   * Two arrays are considered deeply equal if both are {@code null}
   * or if they refer to arrays that contain the same number of elements and
   * all corresponding pairs of elements in the two arrays are deeply equal.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[][] {{1, 2}, {3, 4}}).isDeepEqualTo(new byte[][] {{1, 2}, {3, 4}});
   *
   * // assertions will fail
   * assertThat(new byte[][] {{1, 2}, {3, 4}}).isDeepEqualTo(new byte[][] {{1, 2}, {9, 10}});
   * assertThat(new byte[][] {{1, 2}, {3, 4}}).isDeepEqualTo(new byte[][] {{1, 2, 3}, {4}});</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not deeply equal to the given one.
   */
  @Override
  public Byte2DArrayAssert isDeepEqualTo(byte[][] expected) {
    if (actual == expected) return myself;
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      byte[] actualSubArray = actual[i];
      byte[] expectedSubArray = expected[i];

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
   * Verifies that the actual {@code byte[][]} is equal to the given one.
   * <p>
   * <b>WARNING!</b> This method will use {@code equals} to compare (it will compare arrays references only).<br>
   * Unless you specify a comparator with {@link #usingComparator(Comparator)}, it is advised to use
   * {@link #isDeepEqualTo(byte[][])} instead.
   * <p>
   * Example:
   * <pre><code class='java'> byte[][] array = {{1, 2}, {3, 4}};
   *
   * // assertion will pass
   * assertThat(array).isEqualTo(array);
   *
   * // assertion will fail as isEqualTo calls equals which compares arrays references only.
   * assertThat(array).isEqualTo(new byte[][] {{1, 2}, {3, 4}});</code></pre>
   *
   * @param expected the given value to compare the actual {@code byte[][]} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code byte[][]} is not equal to the given one.
   */
  @Override
  public Byte2DArrayAssert isEqualTo(Object expected) {
    return super.isEqualTo(expected);
  }

  /**
   * Verifies that the actual {@code byte[][]} is {@code null} or empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * byte[][] array = null;
   * assertThat(array).isNullOrEmpty();
   * assertThat(new byte[][] { }).isNullOrEmpty();
   * assertThat(new byte[][] {{ }}).isNullOrEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new byte[][] {{ }, { }, { }}).isNullOrEmpty();
   *
   * // assertion will fail
   * assertThat(new byte[][] {{ 1 }, { 2 }}).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code byte[][]} is not {@code null} or not empty.
   */
  @Override
  public void isNullOrEmpty() {
    byte2dArrays.assertNullOrEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code byte[][]} is empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new byte[][] {{}}).isEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new byte[][] {{ }, { }, { }}).isEmpty();
   *
   * // assertions will fail
   * assertThat(new byte[][] {{ 1 }, { 2 }}).isEmpty();
   * byte[][] array = null;
   * assertThat(array).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code byte[][]} is not empty.
   */
  @Override
  public void isEmpty() {
    byte2dArrays.assertEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code byte[][]} is not empty, not empty means the array has at least one element.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new byte[][] {{ 1 }, { 2 }}).isNotEmpty();
   * assertThat(new byte[][] {{ }, { 2 }}).isNotEmpty();
   *
   * // assertions will fail
   * assertThat(new byte[][] { }).isNotEmpty();
   * assertThat(new byte[][] {{ }}).isNotEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new byte[][] {{ }, { }, { }}).isNotEmpty();
   * byte[][] array = null;
   * assertThat(array).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code byte[][]} is empty or null.
   */
  @Override
  public Byte2DArrayAssert isNotEmpty() {
    byte2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code byte[][]} has the given dimensions.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[][] {{1, 2, 3}, {4, 5, 6}}).hasDimensions(2, 3);
   *
   * // assertions will fail
   * assertThat(new byte[][] { }).hasSize(1, 1);
   * assertThat(new byte[][] {{1, 2, 3}, {4, 5, 6}}).hasDimensions(3, 2);
   * assertThat(new byte[][] {{1, 2, 3}, {4, 5, 6, 7}}).hasDimensions(2, 3); </code></pre>
   *
   * @param expectedFirstDimension the expected number of values in first dimension of the actual {@code byte[][]}.
   * @param expectedSecondDimension the expected number of values in second dimension of the actual {@code byte[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code byte[][]}'s dimensions are not equal to the given ones.
   */
  @Override
  public Byte2DArrayAssert hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    byte2dArrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /**
   * Verifies that the actual two-dimensional array has the given number of rows.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[][] {{1, 2, 3}, {4, 5, 6}}).hasNumberOfRows(2);
   * assertThat(new byte[][] {{1}, {1, 2}, {1, 2, 3}}).hasNumberOfRows(3);
   *
   * // assertions will fail
   * assertThat(new byte[][] { }).hasNumberOfRows(1);
   * assertThat(new byte[][] {{1, 2, 3}, {4, 5, 6}}).hasNumberOfRows(3);
   * assertThat(new byte[][] {{1, 2, 3}, {4, 5, 6, 7}}).hasNumberOfRows(1); </code></pre>
   *
   * @param expected the expected number of rows of the two-dimensional array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual number of rows are not equal to the given one.
   */
  @Override
  public Byte2DArrayAssert hasNumberOfRows(int expected) {
    byte2dArrays.assertNumberOfRows(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code byte[][]} has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object and primitive arrays.
   * </p>
   * Example:
   * <pre><code class='java'> byte[][] byteArray = {{1, 2, 3}, {4, 5, 6}};
   * char[][] charArray = {{'a', 'b', 'c'}, {'d', 'e', 'f'}};
   *
   * // assertion will pass
   * assertThat(byteArray).hasSameDimensionsAs(charArray);
   *
   * // assertions will fail
   * assertThat(byteArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
   * assertThat(byteArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd', 'e'}});
   * assertThat(byteArray).hasSameDimensionsAs(new char[][] {{'a', 'b', 'c'}, {'d', 'e'}});</code></pre>
   *
   * @param array the array to compare dimensions with actual {@code byte[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code byte[][]} is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual {@code byte[][]} and given array don't have the same dimensions.
   */
  @Override
  public Byte2DArrayAssert hasSameDimensionsAs(Object array) {
    byte2dArrays.assertHasSameDimensionsAs(info, actual, array);
    return myself;
  }

  /**
   * Verifies that the actual {@code byte[][]} contains the given byte[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[][] {{1, 2}, {3, 4}, {5, 6}}).contains(new byte[] {3, 4}, atIndex(1));
   *
   * // assertions will fail
   * assertThat(new byte[][] {{1, 2}, {3, 4}, {5, 6}}).contains(new byte[] {3, 4}, atIndex(0));
   * assertThat(new byte[][] {{1, 2}, {3, 4}, {5, 6}}).contains(new byte[] {7, 8}, atIndex(2));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual {@code byte[][]}.
   * @return myself assertion object.
   * @throws AssertionError            if the actual {@code byte[][]} is {@code null} or empty.
   * @throws AssertionError            if the actual {@code byte[][]} does not contain the given value at the given index.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *                                   the actual {@code byte[][]}.
   * @throws NullPointerException      if the given {@code Index} is {@code null}.
   */
  public Byte2DArrayAssert contains(byte[] value, Index index) {
    byte2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual {@code byte[][]} does not contain the given byte[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new byte[][] {{1, 2}, {3, 4}, {5, 6}}).doesNotContain(new byte[] {3, 4}, atIndex(0));
   * assertThat(new byte[][] {{1, 2}, {3, 4}, {5, 6}}).doesNotContain(new byte[] {7, 8}, atIndex(2));
   *
   * // assertion will fail
   * assertThat(new byte[][] {{1, 2}, {3, 4}, {5, 6}}).doesNotContain(new byte[] {3, 4}, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual {@code byte[][]}.
   * @return myself assertion object.
   * @throws AssertionError       if the actual {@code byte[][]} is {@code null}.
   * @throws AssertionError       if the actual {@code byte[][]} contains the given value at the given index.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   */
  public Byte2DArrayAssert doesNotContain(byte[] value, Index index) {
    byte2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }
}
