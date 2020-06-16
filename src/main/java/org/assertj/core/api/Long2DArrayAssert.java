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
import org.assertj.core.internal.Long2DArrays;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for two-dimensional arrays of {@code long}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(long[][])}</code>.
 * </p>
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Long2DArrayAssert extends Abstract2DArrayAssert<Long2DArrayAssert, long[][], Long> {

  private final Failures failures = Failures.instance();

  @VisibleForTesting
  protected Long2DArrays long2dArrays = Long2DArrays.instance();

  public Long2DArrayAssert(long[][] actual) {
    super(actual, Long2DArrayAssert.class);
  }

  /**
   * Verifies that the actual 2D array is <b>deeply</b> equal to the given one.
   * <p>
   * Two arrays are considered deeply equal if both are {@code null}
   * or if they refer to arrays that contain the same number of elements and
   * all corresponding pairs of elements in the two arrays are deeply equal.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[][] {{1, 2}, {3, 4}}).isDeepEqualTo(new long[][] {{1, 2}, {3, 4}});
   *
   * // assertions will fail
   * assertThat(new long[][] {{1, 2}, {3, 4}}).isDeepEqualTo(new long[][] {{1, 2}, {9, 10}});
   * assertThat(new long[][] {{1, 2}, {3, 4}}).isDeepEqualTo(new long[][] {{1, 2, 3}, {4}});</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not deeply equal to the given one.
   */
  @Override
  public Long2DArrayAssert isDeepEqualTo(long[][] expected) {
    if (actual == expected) return myself;
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      long[] actualSubArray = actual[i];
      long[] expectedSubArray = expected[i];

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
   * Verifies that the actual {@code long[][]} is equal to the given one.
   * <p>
   * <b>WARNING!</b> This method will use {@code equals} to compare (it will compare arrays references only).<br>
   * Unless you specify a comparator with {@link #usingComparator(Comparator)}, it is advised to use
   * {@link Long2DArrayAssert#isDeepEqualTo(Object)} instead.
   * <p>
   * Example:
   * <pre><code class='java'> long[][] array = {{1, 2}, {3, 4}};
   *
   * // assertion will pass
   * assertThat(array).isEqualTo(array);
   *
   * // assertion will fail as isEqualTo calls equals which compares arrays references only.
   * assertThat(array).isEqualTo(new long[][] {{1, 2}, {3, 4}});</code></pre>
   *
   * @param expected the given value to compare the actual {@code long[][]} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code long[][]} is not equal to the given one.
   */
  @Override
  public Long2DArrayAssert isEqualTo(Object expected) {
    return super.isEqualTo(expected);
  }

  /**
   * Verifies that the actual array is {@code null} or empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * long[][] array = null;
   * assertThat(array).isNullOrEmpty();
   * assertThat(new long[][] { }).isNullOrEmpty();
   * assertThat(new long[][] {{ }}).isNullOrEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new long[][] {{ }, { }, { }}).isNullOrEmpty();
   *
   * // assertion will fail
   * assertThat(new String[][] {{&quot;a&quot;}, {&quot;b&quot;}}).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the actual array is not {@code null} or not empty.
   */
  @Override
  public void isNullOrEmpty() {
    long2dArrays.assertNullOrEmpty(info, actual);
  }

  /**
   * Verifies that the actual array is empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[][] {{}}).isEmpty();
   * assertThat(new long[][] {{ }}).isNullOrEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new long[][] {{ }, { }, { }}).isNullOrEmpty();
   *
   * // assertions will fail
   * assertThat(new long[][] {{ 1 }, { 2 }}).isEmpty();
   * long[][] array = null;
   * assertThat(array).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual array is not empty.
   */
  @Override
  public void isEmpty() {
    long2dArrays.assertEmpty(info, actual);
  }

  /**
   * Verifies that the actual array is not empty, not empty means the array has at least one element.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new long[][] {{ 1 }, { 2 }}).isNotEmpty();
   * assertThat(new long[][] {{ }, { 2 }}).isNotEmpty();
   *
   * // assertions will fail
   * assertThat(new long[][] { }).isNotEmpty();
   * assertThat(new long[][] {{ }}).isNotEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new long[][] {{ }, { }, { }}).isNotEmpty();
   * long[][] array = null;
   * assertThat(array).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is empty or null.
   */
  @Override
  public Long2DArrayAssert isNotEmpty() {
    long2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual 2D array has the given dimensions.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[][] {{1, 2, 3}, {4, 5, 6}}).hasDimensions(2, 3);
   *
   * // assertions will fail
   * assertThat(new long[][] { }).hasSize(1, 1);
   * assertThat(new long[][] {{1, 2, 3}, {4, 5, 6}}).hasDimensions(3, 2);
   * assertThat(new long[][] {{1, 2, 3}, {4, 5, 6, 7}}).hasDimensions(2, 3); </code></pre>
   *
   * @param expectedFirstDimension the expected number of values in first dimension of the actual array.
   * @param expectedSecondDimension the expected number of values in second dimension of the actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array's dimensions are not equal to the given ones.
   */
  @Override
  public Long2DArrayAssert hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    long2dArrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /**
   * Verifies that the actual {@code long[][]} has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object and primitive arrays.
   * </p>
   * Example:
   * <pre><code class='java'> long[][] longArray = {{1, 2, 3}, {4, 5, 6}};
   * char[][] charArray = {{'a', 'b', 'c'}, {'d', 'e', 'f'}};
   *
   * // assertion will pass
   * assertThat(longArray).hasSameDimensionsAs(charArray);
   *
   * // assertions will fail
   * assertThat(longArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
   * assertThat(longArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd', 'e'}});
   * assertThat(longArray).hasSameDimensionsAs(new char[][] {{'a', 'b', 'c'}, {'d', 'e'}});</code></pre>
   *
   * @param array the array to compare dimensions with actual {@code long[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code long[][]} is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual {@code long[][]} and given array don't have the same dimensions.
   */
  @Override
  public Long2DArrayAssert hasSameDimensionsAs(Object array) {
    long2dArrays.assertHasSameDimensionsAs(info, actual, array);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given long[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[][] {{1L, 2L}, {3L, 4L}, {5L, 6L}}).contains(new long[] {3L, 4L}, atIndex(1));
   *
   * // assertions will fail
   * assertThat(new long[][] {{1L, 2L}, {3L, 4L}, {5L, 6L}}).contains(new long[] {3L, 4L}, atIndex(0));
   * assertThat(new long[][] {{1L, 2L}, {3L, 4L}, {5L, 6L}}).contains(new long[] {7L, 8L}, atIndex(2));</code></pre>
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
  public Long2DArrayAssert contains(long[] value, Index index) {
    long2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given long[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new long[][] {{1L, 2L}, {3L, 4L}, {5L, 6L}}).doesNotContain(new long[] {3L, 4L}, atIndex(0));
   * assertThat(new long[][] {{1L, 2L}, {3L, 4L}, {5L, 6L}}).doesNotContain(new long[] {7L, 8L}, atIndex(2));
   *
   * // assertion will fail
   * assertThat(new long[][] {{1L, 2L}, {3L, 4L}, {5L, 6L}}).doesNotContain(new long[] {3L, 4L}, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public Long2DArrayAssert doesNotContain(long[] value, Index index) {
    long2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }
}
