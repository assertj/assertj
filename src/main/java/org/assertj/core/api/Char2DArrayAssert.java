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
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Char2DArrayAssert extends Abstract2DArrayAssert<Char2DArrayAssert, char[][], Character> {

  private final Failures failures = Failures.instance();

  @VisibleForTesting
  protected Char2DArrays char2dArrays = Char2DArrays.instance();

  public Char2DArrayAssert(char[][] actual) {
    super(actual, Char2DArrayAssert.class);
  }

  /**
   * Verifies that the actual {@code char[][]} is <b>deeply</b> equal to the given one.
   * <p>
   * Two array references are considered deeply equal if both
   * are {@code null} or if they refer to arrays that contain the same
   * number of elements and all corresponding pairs of elements in the two
   * arrays are deeply equal.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[][] {{'1', '2'}, {'3', '4'}}).isDeepEqualTo(new char[][] {{'1', '2'}, {'3', '4'}});
   *
   * // assertions will fail
   * assertThat(new char[][] {{'1', '2'}, {'3', '4'}}).isDeepEqualTo(new char[][] {{'1', '2'}, {'9', '0'}});
   * assertThat(new char[][] {{'1', '2'}, {'3', '4'}}).isDeepEqualTo(new char[][] {{'1', '2', '3'}, {'4'}});</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not deeply equal to the given one.
   */
  @Override
  public Char2DArrayAssert isDeepEqualTo(char[][] expected) {
    if (actual == expected) return myself;
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      char[] actualSubArray = actual[i];
      char[] expectedSubArray = expected[i];

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
   * Verifies that the actual {@code char[][]} is equal to the given one.
   * <p>
   * <b>WARNING!</b> This method will use {@code equals} to compare (it will compare arrays references only).<br>
   * Unless you specify a comparator with {@link #usingComparator(Comparator)}, it is advised to use
   * {@link Char2DArrayAssert#isDeepEqualTo(Object)} instead.
   * <p>
   * Example:
   * <pre><code class='java'> char[][] array = {{'1', '2'}, {'3', '4'}}
   *
   * // assertion will pass
   * assertThat(array).isEqualTo(array);
   *
   * // assertion will fail as isEqualTo calls equals which compares arrays references only.
   * assertThat(array).isEqualTo(new char[][] {{'1', '2'}, {'3', '4'}});</code></pre>
   *
   * @param expected the given value to compare the actual {@code char[][]} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code char[][]} is not equal to the given one.
   */
  @Override
  public Char2DArrayAssert isEqualTo(Object expected) {
    return super.isEqualTo(expected);
  }

  /**
   * Verifies that the actual {@code char[][]} is {@code null} or empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * char[][] array = null;
   * assertThat(array).isNullOrEmpty();
   * assertThat(new char[][] { }).isNullOrEmpty();
   * assertThat(new char[][] { { } }).isNullOrEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new char[][] { { }, { }, { } }).isNullOrEmpty();
   *
   * // assertion will fail
   * assertThat(new char[][] { {'a'}, {'b'} }).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code char[][]} is not {@code null} or not empty.
   */
  @Override
  public void isNullOrEmpty() {
    char2dArrays.assertNullOrEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code char[][]} is empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new char[][] { {} }).isEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new char[][] { { }, { }, { } }).isEmpty();
   *
   * // assertions will fail
   * assertThat(new char[][] { {'a'}, {'b'} }).isEmpty();
   * char[][] array = null;
   * assertThat(array).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code char[][]} is not empty.
   */
  @Override
  public void isEmpty() {
    char2dArrays.assertEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code char[][]} is not empty, not empty means the array has at least one char element.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[][] { {'a'}, {'b'} }).isNotEmpty();
   * assertThat(new char[][] { { }, {'b'} }).isNotEmpty();
   *
   * // assertions will fail
   * assertThat(new char[][] { }).isNotEmpty();
   * assertThat(new char[][] { { } }).isNotEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new char[][] { { }, { }, { } }).isNotEmpty();
   * char[][] array = null;
   * assertThat(array).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code char[][]} is empty or null.
   */
  @Override
  public Char2DArrayAssert isNotEmpty() {
    char2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code char[][]} has the the given dimensions.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[][] { {'1', '2', '3'}, {'4', '5', '6'} }).hasDimensions(2, 3);
   *
   * // assertions will fail
   * assertThat(new char[][] { }).hasDimensions(1, 1);
   * assertThat(new char[][] { {'1', '2', '3'}, {'4', '5', '6'} }).hasDimensions(3, 2);
   * assertThat(new char[][] { {'1', '2', '3'}, {'4', '5', '6', '7'} }).hasDimensions(2, 3); </code></pre>
   *
   * @param expectedFirstDimension the expected number of values in first dimension of the actual {@code char[][]}.
   * @param expectedSecondDimension the expected number of values in second dimension of the actual {@code char[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual {@code char[][]} is not equal to the given one.
   */
  @Override
  public Char2DArrayAssert hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    char2dArrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /**
   * Verifies that the actual {@code char[][]} has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object[] and primitive arrays (e.g. int[]).
   * </p>
   * Example:
   * <pre><code class='java'> char[][] charArray = {{'a', 'b', 'c'}, {'d', 'e', 'f'}};
   * int[][] intArray = {{1, 2, 3}, {4, 5, 6}};
   *
   * // assertion will pass
   * assertThat(charArray).hasSameDimensionsAs(intArray);
   *
   * // assertions will fail
   * assertThat(charArray).hasSameDimensionsAs(new int[][] {{1, 2}, {3, 4}, {5, 6}});
   * assertThat(charArray).hasSameDimensionsAs(new int[][] {{1, 2}, {3, 4, 5}});
   * assertThat(charArray).hasSameDimensionsAs(new int[][] {{1, 2, 3}, {4, 5}});</code></pre>
   *
   * @param array the array to compare dimensions with actual {@code char[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code char[][]} is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual {@code char[][]} and given array don't have the same dimensions.
   */
  @Override
  public Char2DArrayAssert hasSameDimensionsAs(Object array) {
    char2dArrays.assertHasSameDimensionsAs(info, actual, array);
    return myself;
  }

  /**
   * Verifies that the actual {@code char[][]} contains the given char[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[][] {{'a', 'b'}, {'c', 'd'} }).contains(new char[] {'a', 'b'}, atIndex(0));
   *
   * // assertion will fail
   * assertThat(new char[][] {{'a', 'b'}, {'c', 'd'} }).contains(new char[] {'a', 'b'}, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual {@code char[][]}.
   * @return myself assertion object.
   * @throws AssertionError if the actual {@code char[][]} is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual {@code char[][]}.
   * @throws AssertionError if the actual {@code char[][]} does not contain the given value at the given index.
   */
  public Char2DArrayAssert contains(char[] value, Index index) {
    char2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual {@code char[][]} does not contain the given char[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[][] {{'a', 'b'}, {'c', 'd'} }).doesNotContain(new char[] {'a', 'b'}, atIndex(1));
   *
   * // assertion will fail
   * assertThat(new char[][] {{'a', 'b'}, {'c', 'd'} }).doesNotContain(new char[] {'a', 'b'}, atIndex(0));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual {@code char[][]}.
   * @return myself assertion object.
   * @throws AssertionError if the actual {@code char[][]} is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual {@code char[][]} contains the given value at the given index.
   */
  public Char2DArrayAssert doesNotContain(char[] value, Index index) {
    char2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Use unicode character representation instead of standard representation in error messages.
   * <p>
   * With standard error message:
   * <pre><code class='java'> assertThat(new char[][] {{'a', 'b'}, {'ć', 'd'}}).isDeepEqualTo(new char[][] {{'a', 'b'}, {'c', 'd'}});
   *
   * org.opentest4j.AssertionFailedError:
   * Expecting "actual[1][0]" value to be equal to:
   *  &lt;'c'&gt;
   * but was
   *  &lt;'ć'&gt;</code></pre>
   *
   * With unicode based error message:
   * <pre><code class='java'> assertThat(new char[][] {{'a', 'b'}, {'ć', 'd'}}).inUnicode().isDeepEqualTo(new char[][] {{'a', 'b'}, {'c', 'd'}});
   *
   * org.opentest4j.AssertionFailedError:
   * Expecting actual[1][0] value to be equal to:
   *  &lt;c&gt;
   * but was
   *  &lt;\u0107&gt;</code></pre>
   *
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public Char2DArrayAssert inUnicode() {
    info.useUnicodeRepresentation();
    return myself;
  }

}
