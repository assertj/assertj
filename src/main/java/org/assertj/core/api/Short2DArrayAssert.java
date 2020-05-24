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
import org.assertj.core.internal.Short2DArrays;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for arrays of {@code short}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(short[][])}</code>.
 * </p>
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 * @author Maciej Wajcht
 */
public class Short2DArrayAssert extends Abstract2DArrayAssert<Short2DArrayAssert, short[][], Short> {

  private final Failures failures = Failures.instance();

  @VisibleForTesting
  protected Short2DArrays arrays = Short2DArrays.instance();

  public Short2DArrayAssert(short[][] actual) {
    super(actual, Short2DArrayAssert.class);
  }

  public Short2DArrayAssert isDeepEqualTo(short[][] expected) {
    if (actual == expected) {
      return myself;
    }
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      short[] actualSubArray = actual[i];
      short[] expectedSubArray = expected[i];

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
  public Short2DArrayAssert isNotEmpty() {
    arrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Short2DArrayAssert hasSize(int expectedFirstDimensionSize, int expectedSecondDimensionSize) {
    arrays.assertHasSize(info, actual, expectedFirstDimensionSize, expectedSecondDimensionSize);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Short2DArrayAssert hasSameSizeAs(Object other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2, 3 }).contains((short) 1, atIndex(O));
   * assertThat(new short[] { 1, 2, 3 }).contains((short) 3, atIndex(2));
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).contains((short) 1, atIndex(1));
   * assertThat(new short[] { 1, 2, 3 }).contains((short) 4, atIndex(2));</code></pre>
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
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2, 3 }).contains(1, atIndex(O));
   * assertThat(new short[] { 1, 2, 3 }).contains(3, atIndex(2));
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).contains(1, atIndex(1));
   * assertThat(new short[] { 1, 2, 3 }).contains(4, atIndex(2));</code></pre>
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
    arrays.assertContains(info, actual, toShort(value), index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
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
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
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
    arrays.assertDoesNotContain(info, actual, toShort(value), index);
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
