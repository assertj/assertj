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
import java.util.Objects;

import org.assertj.core.data.Index;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Object2DArrays;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for two-dimensional arrays of objects.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Object[][])}</code>.
 *
 * @author Maciej Wajcht
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @since 3.17.0
 */
public class Object2DArrayAssert<ELEMENT> extends
    Abstract2DArrayAssert<Object2DArrayAssert<ELEMENT>, ELEMENT[][], ELEMENT> {

  @VisibleForTesting
  protected Object2DArrays<ELEMENT> object2dArrays = Object2DArrays.instance();

  private final Failures failures = Failures.instance();

  public Object2DArrayAssert(ELEMENT[][] actual) {
    super(actual, Object2DArrayAssert.class);
  }

  /**
   * Verifies that the actual {@code ELEMENT[][]} is <b>deeply</b> equal to the given one.
   * <p>
   * Two array references are considered deeply equal if both
   * are {@code null} or if they refer to arrays that contain the same
   * number of elements and all corresponding pairs of elements in the two
   * arrays are deeply equal.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new String[][] {{"1", "2"}, {"3", "4"}}).isDeepEqualTo(new String[][] {{"1", "2"}, {"3", "4"}});
   *
   * // assertions will fail
   * assertThat(new String[][] {{"1", "2"}, {"3", "4"}}).isDeepEqualTo(new String[][] {{"1", "2"}, {"9", "0"}});
   * assertThat(new String[][] {{"1", "2"}, {"3", "4"}}).isDeepEqualTo(new String[][] {{"1", "2", "3"}, {"4"}});</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not deeply equal to the given one.
   */
  @Override
  public Object2DArrayAssert<ELEMENT> isDeepEqualTo(ELEMENT[][] expected) {
    if (actual == expected) return myself;
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      ELEMENT[] actualSubArray = actual[i];
      ELEMENT[] expectedSubArray = expected[i];

      if (actualSubArray == expectedSubArray) continue;
      if (actualSubArray == null) throw failures.failure(info, shouldNotBeNull("actual[" + i + "]"));
      if (expectedSubArray.length != actualSubArray.length) {
        throw failures.failure(info, subarraysShouldHaveSameSize(actual, expected, actualSubArray, actualSubArray.length,
                                                                 expectedSubArray, expectedSubArray.length, i),
                               info.representation().toStringOf(actual), info.representation().toStringOf(expected));
      }
      for (int j = 0; j < actualSubArray.length; j++) {
        if (!Objects.deepEquals(actualSubArray[j], expectedSubArray[j])) {
          throw failures.failure(info, elementShouldBeEqual(actualSubArray[j], expectedSubArray[j], i, j),
                                 info.representation().toStringOf(actual), info.representation().toStringOf(expected));
        }
      }
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code ELEMENT[][]} is equal to the given one.
   * <p>
   * <b>WARNING!</b> This method will use {@code equals} to compare (it will compare arrays references only).<br>
   * Unless you specify a comparator with {@link #usingComparator(Comparator)}, it is advised to use
   * {@link #isDeepEqualTo(Object[][])} instead.
   * <p>
   * Example:
   * <pre><code class='java'> String[][] array = {{"1", "2"}, {"3", "4"}};
   *
   * // assertion will pass
   * assertThat(array).isEqualTo(array);
   *
   * // assertion will fail as isEqualTo calls equals which compares arrays references only.
   * assertThat(array).isEqualTo(new String[][] {{"1", "2"}, {"3", "4"}});</code></pre>
   *
   * @param expected the given value to compare the actual {@code ELEMENT[][]} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ELEMENT[][]} is not equal to the given one.
   */
  @Override
  public Object2DArrayAssert<ELEMENT> isEqualTo(Object expected) {
    return super.isEqualTo(expected);
  }

  /**
   * Verifies that the actual {@code ELEMENT[][]} is {@code null} or empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * String[][] array = null;
   * assertThat(array).isNullOrEmpty();
   * assertThat(new String[][] { }).isNullOrEmpty();
   * assertThat(new String[][] { { } }).isNullOrEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new String[][] { { }, { }, { } }).isNullOrEmpty();
   *
   * // assertion will fail
   * assertThat(new String[][] { {'a'}, {'b'} }).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code ELEMENT[][]} is not {@code null} or not empty.
   */
  @Override
  public void isNullOrEmpty() {
    object2dArrays.assertNullOrEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code ELEMENT[][]} is empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[][] { {} }).isEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new String[][] { { }, { }, { } }).isEmpty();
   *
   * // assertions will fail
   * assertThat(new String[][] { {'a'}, {'b'} }).isEmpty();
   * String[][] array = null;
   * assertThat(array).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code ELEMENT[][]} is not empty.
   */
  @Override
  public void isEmpty() {
    object2dArrays.assertEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code ELEMENT[][]} is not empty, not empty means the array has at least one char element.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new String[][] { {'a'}, {'b'} }).isNotEmpty();
   * assertThat(new String[][] { { }, {'b'} }).isNotEmpty();
   *
   * // assertions will fail
   * assertThat(new String[][] { }).isNotEmpty();
   * assertThat(new String[][] { { } }).isNotEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new String[][] { { }, { }, { } }).isNotEmpty();
   * String[][] array = null;
   * assertThat(array).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ELEMENT[][]} is empty or null.
   */
  @Override
  public Object2DArrayAssert<ELEMENT> isNotEmpty() {
    object2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code ELEMENT[][]} has the given dimensions.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new String[][] { {"1", "2", "3"}, {"4", "5", "6"} }).hasDimensions(2, 3);
   *
   * // assertions will fail
   * assertThat(new String[][] { }).hasDimensions(1, 1);
   * assertThat(new String[][] { {"1", "2", "3"}, {"4", "5", "6"} }).hasDimensions(3, 2);
   * assertThat(new String[][] { {"1", "2", "3"}, {"4", "5", "6", "7"} }).hasDimensions(2, 3); </code></pre>
   *
   * @param expectedFirstDimension the expected number of values in first dimension of the actual {@code ELEMENT[][]}.
   * @param expectedSecondDimension the expected number of values in second dimension of the actual {@code ELEMENT[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual {@code ELEMENT[][]} is not equal to the given one.
   */
  @Override
  public Object2DArrayAssert<ELEMENT> hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    object2dArrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /**
   * Verifies that the actual two-dimensional array has the given number of rows.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new String[][] {{"1", "2", "3"}, {"4", "5", "6"}}).hasNumberOfRows(2);
   * assertThat(new String[][] {{"1"}, {"1", "2"}, {"1", "2", "3"}}).hasNumberOfRows(3);
   *
   * // assertions will fail
   * assertThat(new String[][] { }).hasNumberOfRows(1);
   * assertThat(new String[][] {{"1", "2", "3"}, {"4", "5", "6"}}).hasNumberOfRows(3);
   * assertThat(new String[][] {{"1", "2", "3"}, {"4", "5", "6", "7"}}).hasNumberOfRows(1); </code></pre>
   *
   * @param expected the expected number of rows of the two-dimensional array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual number of rows are not equal to the given one.
   */
  @Override
  public Object2DArrayAssert<ELEMENT> hasNumberOfRows(int expected) {
    object2dArrays.assertNumberOfRows(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code ELEMENT[][]} has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object and primitive arrays.
   * </p>
   * Example:
   * <pre><code class='java'> String[][] stringArray = {{"a", "b", "c""}, {"d", "e", "f""}};
   * char[][] charArray = {{'a', 'b', 'c'}, {'d', 'e', 'f'}};
   *
   * // assertion will pass
   * assertThat(stringArray).hasSameDimensionsAs(charArray);
   *
   * // assertions will fail
   * assertThat(stringArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
   * assertThat(stringArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd', 'e'}});
   * assertThat(stringArray).hasSameDimensionsAs(new char[][] {{'a', 'b', 'c'}, {'d', 'e'}});</code></pre>
   *
   * @param array the array to compare dimensions with actual {@code ELEMENT[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ELEMENT[][]} is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual {@code ELEMENT[][]} and given array don't have the same dimensions.
   */
  @Override
  public Object2DArrayAssert<ELEMENT> hasSameDimensionsAs(Object array) {
    object2dArrays.assertHasSameDimensionsAs(info, actual, array);
    return myself;
  }

  /**
   * Verifies that the actual ELEMENT[][] contains the given ELEMENT[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new String[][] {{"a", "b"}, {"c", "d"}}).contains(new String[] {"a", "b"}, atIndex(0));
   *
   * // assertion will fail
   * assertThat(new String[][] {{"a", "b"}, {"c", "d"}}).contains(new String[] {"a", "b"}, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual ELEMENT[][].
   * @return myself assertion object.
   * @throws AssertionError if the actual ELEMENT[][] is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual ELEMENT[][].
   * @throws AssertionError if the actual ELEMENT[][] does not contain the given value at the given index.
   */
  public Object2DArrayAssert<ELEMENT> contains(ELEMENT[] value, Index index) {
    object2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual ELEMENT[][] does not contain the given ELEMENT[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new String[][] {{"a", "b"}, {"c", "d"}}).doesNotContain(new String[] {"a", "b"}, atIndex(1));
   *
   * // assertion will fail
   * assertThat(new String[][] {{"a", "b"}, {"c", "d"}}).doesNotContain(new String[] {"a", "b"}, atIndex(0));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual ELEMENT[][].
   * @return myself assertion object.
   * @throws AssertionError if the actual ELEMENT[][] is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual ELEMENT[][] contains the given value at the given index.
   */
  public Object2DArrayAssert<ELEMENT> doesNotContain(ELEMENT[] value, Index index) {
    object2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }
}
