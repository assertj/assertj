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
import org.assertj.core.internal.Short2DArrays;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for arrays of {@code short}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(short[][])}</code>.
 * </p>
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Short2DArrayAssert extends Abstract2DArrayAssert<Short2DArrayAssert, short[][], Short> {

  private final Failures failures = Failures.instance();

  @VisibleForTesting
  protected Short2DArrays short2dArrays = Short2DArrays.instance();

  public Short2DArrayAssert(short[][] actual) {
    super(actual, Short2DArrayAssert.class);
  }

  /** {@inheritDoc} */
  @Override
  public Short2DArrayAssert isDeepEqualTo(short[][] expected) {
    if (actual == expected) return myself;
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }
    for (int i = 0; i < actual.length; i++) {
      short[] actualSubArray = actual[i];
      short[] expectedSubArray = expected[i];

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
    short2dArrays.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public void isEmpty() {
    short2dArrays.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public Short2DArrayAssert isNotEmpty() {
    short2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Short2DArrayAssert hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    short2dArrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /**
   * Verifies that the actual {@code short[][]} has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object and primitive arrays.
   * </p>
   * Example:
   * <pre><code class='java'> short[][] shortArray = {{1, 2, 3}, {4, 5, 6}};
   * char[][] charArray = {{'a', 'b', 'c'}, {'d', 'e', 'f'}};
   *
   * // assertion will pass
   * assertThat(shortArray).hasSameDimensionsAs(charArray);
   *
   * // assertions will fail
   * assertThat(shortArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
   * assertThat(shortArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd', 'e'}});
   * assertThat(shortArray).hasSameDimensionsAs(new char[][] {{'a', 'b', 'c'}, {'d', 'e'}});</code></pre>
   *
   * @param array the array to compare dimensions with actual {@code short[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code short[][]} is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual {@code short[][]} and given array don't have the same dimensions.
   */
  @Override
  public Short2DArrayAssert hasSameDimensionsAs(Object array) {
    short2dArrays.assertHasSameDimensionsAs(info, actual, array);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given short[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[][] {{1, 2}, {3, 4}, {5, 6}}).contains(new short[] {3, 4}, atIndex(1));
   *
   * // assertions will fail
   * assertThat(new short[][] {{1, 2}, {3, 4}, {5, 6}}).contains(new short[] {3, 4}, atIndex(0));
   * assertThat(new short[][] {{1, 2}, {3, 4}, {5, 6}}).contains(new short[] {7, 8}, atIndex(2));</code></pre>
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
  public Short2DArrayAssert contains(short[] value, Index index) {
    short2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given int[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[][] {{1, 2}, {3, 4}, {5, 6}}).doesNotContain(new short[] {3, 4}, atIndex(0));
   * assertThat(new short[][] {{1, 2}, {3, 4}, {5, 6}}).doesNotContain(new short[] {7, 8}, atIndex(2));
   *
   * // assertion will fail
   * assertThat(new short[][] {{1, 2}, {3, 4}, {5, 6}}).doesNotContain(new short[] {3, 4}, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual array.
   * @throws AssertionError if the actual array does not contain the given value at the given index.
   * @since 3.16.0
   */
  public Short2DArrayAssert contains(int[] value, Index index) {
    short2dArrays.assertContains(info, actual, toShort(value), index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given short[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain((short) 1, atIndex(1));
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain((short) 2, atIndex(0));
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain((short) 1, atIndex(0));
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain((short) 2, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public Short2DArrayAssert doesNotContain(short[] value, Index index) {
    short2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given int[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain(1, atIndex(1));
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain(2, atIndex(0));
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain(1, atIndex(0));
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain(2, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   * @since 3.16.0
   */
  public Short2DArrayAssert doesNotContain(int[] value, Index index) {
    short2dArrays.assertDoesNotContain(info, actual, toShort(value), index);
    return myself;
  }

  private static short[] toShort(int[] value) {
    short[] shortArray = new short[value.length];
    for (int i = 0; i < value.length; i++) {
      shortArray[i] = (short) value[i];
    }
    return shortArray;
  }
}
