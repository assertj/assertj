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
import org.assertj.core.internal.Object2DArrays;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for two-dimensional arrays of objects.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Object[][])}</code>.
 * </p>
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Mateusz Haligowski
 * @author Lovro Pandzic
 * @author Maciej Wajcht
 */
public class Object2DArrayAssert<ELEMENT> extends
  Abstract2DArrayAssert<Object2DArrayAssert<ELEMENT>, ELEMENT[][], ELEMENT> {

  @VisibleForTesting
  protected Object2DArrays<ELEMENT> arrays = Object2DArrays.instance();

  private final Failures failures = Failures.instance();

  public Object2DArrayAssert(ELEMENT[][] actual) {
    super(actual, Object2DArrayAssert.class);
  }

  public Object2DArrayAssert<ELEMENT> isDeepEqualTo(ELEMENT[][] expected) {
    if (actual == expected) {
      return myself;
    }
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      ELEMENT[] actualSubArray = actual[i];
      ELEMENT[] expectedSubArray = expected[i];

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
  public Object2DArrayAssert<ELEMENT> isNotEmpty() {
    arrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Object2DArrayAssert<ELEMENT> hasSize(int expectedFirstDimensionSize, int expectedSecondDimensionSize) {
    arrays.assertHasSize(info, actual, expectedFirstDimensionSize, expectedSecondDimensionSize);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public Object2DArrayAssert<ELEMENT> hasSameSizeAs(Object other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[][] {{"a", "b"}, {"c", "d"}}).contains(new String[] {"a", "b"}, atIndex(O));
   *
   * // assertion will fail
   * assertThat(new String[][] {{"a", "b"}, {"c", "d"}}).contains(new String[] {"a", "b"}, atIndex(1));</code></pre>
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
  public Object2DArrayAssert<ELEMENT> contains(ELEMENT[] value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[][] {{"a", "b"}, {"c", "d"}}).doesNotContain(new String[] {"a", "b"}, atIndex(1));
   *
   * // assertion will fail
   * assertThat(new String[][] {{"a", "b"}, {"c", "d"}}).doesNotContain(new String[] {"a", "b"}, atIndex(0));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public Object2DArrayAssert<ELEMENT> doesNotContain(ELEMENT[] value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }
}
