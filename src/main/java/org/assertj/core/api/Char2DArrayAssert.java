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
import org.assertj.core.internal.Char2DArrays;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for two-dimensional arrays of {@code char}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(char[][])}</code>.
 * </p>
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 * @author Maciej Wajcht
 */
public class Char2DArrayAssert extends Abstract2DArrayAssert<Char2DArrayAssert, char[][], Character> {

  private final Failures failures = Failures.instance();

  @VisibleForTesting
  protected Char2DArrays arrays = Char2DArrays.instance();

  public Char2DArrayAssert(char[][] actual) {
    super(actual, Char2DArrayAssert.class);
  }

  public Char2DArrayAssert isDeepEqualTo(char[][] expected) {
    if (actual == expected) {
      return myself;
    }
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      char[] actualSubArray = actual[i];
      char[] expectedSubArray = expected[i];

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
  public Char2DArrayAssert isNotEmpty() {
    arrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Char2DArrayAssert hasSize(int expectedFirstDimensionSize, int expectedSecondDimensionSize) {
    arrays.assertHasSize(info, actual, expectedFirstDimensionSize, expectedSecondDimensionSize);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Char2DArrayAssert hasSameSizeAs(Object other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[][] {{'a', 'b'}, {'c', 'd'} }).contains(new char[] {'a', 'b'}, atIndex(O));
   *
   * // assertion will fail
   * assertThat(new char[][] {{'a', 'b'}, {'c', 'd'} }).contains(new char[] {'a', 'b'}, atIndex(1));</code></pre>
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
  public Char2DArrayAssert contains(char[] value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[][] {{'a', 'b'}, {'c', 'd'} }).doesNotContain(new char[] {'a', 'b'}, atIndex(1));
   *
   * // assertion will fail
   * assertThat(new char[][] {{'a', 'b'}, {'c', 'd'} }).doesNotContain(new char[] {'a', 'b'}, atIndex(O));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public Char2DArrayAssert doesNotContain(char[] value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Use unicode character representation instead of standard representation in error messages.
   * <p>
   * With standard error message:
   * <pre><code class='java'> assertThat("a6c".toCharArray()).isEqualTo("abó".toCharArray());
   *
   * org.junit.ComparisonFailure:
   * Expected :['a', 'b', 'ó']
   * Actual   :[a, 6, c]</code></pre>
   *
   * With unicode based error message:
   * <pre><code class='java'> assertThat("a6c".toCharArray()).inUnicode().isEqualTo("abó".toCharArray());
   *
   * org.junit.ComparisonFailure:
   * Expected :[a, b, \u00f3]
   * Actual   :[a, 6, c]</code></pre>
   *
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public Char2DArrayAssert inUnicode() {
    info.useUnicodeRepresentation();
    return myself;
  }

}
