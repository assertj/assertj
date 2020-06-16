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
import org.assertj.core.internal.Boolean2DArrays;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion methods for two-dimensional arrays of {@code boolean}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(boolean[][])}</code>.
 * </p>
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Boolean2DArrayAssert extends Abstract2DArrayAssert<Boolean2DArrayAssert, boolean[][], Boolean> {

  private final Failures failures = Failures.instance();

  @VisibleForTesting
  protected Boolean2DArrays boolean2dArrays = Boolean2DArrays.instance();

  public Boolean2DArrayAssert(boolean[][] actual) {
    super(actual, Boolean2DArrayAssert.class);
  }

  /**
   * Verifies that the actual {@code boolean[][]} is <b>deeply</b> equal to the given one.
   * <p>
   * Two arrays are considered deeply equal if both are {@code null}
   * or if they refer to arrays that contain the same number of elements and
   * all corresponding pairs of elements in the two arrays are deeply equal.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[][] {{true, false}, {false, true}}).isDeepEqualTo(new boolean[][] {{true, false}, {false, true}});
   *
   * // assertions will fail
   * assertThat(new boolean[][] {{true, false}, {false, true}}).isDeepEqualTo(new boolean[][] {{true, false}, {true, true}});
   * assertThat(new boolean[][] {{true, false}, {false, true}}).isDeepEqualTo(new boolean[][] {{true}, {false, false, true}});</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not deeply equal to the given one.
   */
  @Override
  public Boolean2DArrayAssert isDeepEqualTo(boolean[][] expected) {
    // boolean[][] actual = new boolean[][] { { true, false }, { false, true } };
    if (actual == expected) return myself;
    isNotNull();
    if (expected.length != actual.length) {
      throw failures.failure(info, shouldHaveSameSizeAs(actual, expected, actual.length, expected.length));
    }

    for (int i = 0; i < actual.length; i++) {
      boolean[] actualSubArray = actual[i];
      boolean[] expectedSubArray = expected[i];

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
   * Verifies that the actual {@code boolean[][]} is equal to the given one.
   * <p>
   * <b>WARNING!</b> This method will use {@code equals} to compare (it will compare arrays references only).<br>
   * Unless you specify a comparator with {@link #usingComparator(Comparator)}, it is advised to use
   * {@link Boolean2DArrayAssert#isDeepEqualTo(Object)} instead.
   * <p>
   * Example:
   * <pre><code class='java'> boolean[][] array = {{true, true}, {false, false}};
   *
   * // assertion will pass
   * assertThat(array).isEqualTo(array);
   *
   * // assertion will fail as isEqualTo calls equals which compares arrays references only.
   * assertThat(array).isEqualTo(new boolean[][] {{true, true}, {false, false}});</code></pre>
   *
   * @param expected the given value to compare the actual {@code boolean[][]} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code boolean[][]} is not equal to the given one.
   */
  @Override
  public Boolean2DArrayAssert isEqualTo(Object expected) {
    return super.isEqualTo(expected);
  }

  /**
   * Verifies that the actual {@code boolean[][]}is {@code null} or empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * boolean[][] array = null;
   * assertThat(array).isNullOrEmpty();
   * assertThat(new boolean[][] { }).isNullOrEmpty();
   * assertThat(new boolean[][] {{ }}).isNullOrEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new boolean[][] {{ }, { }, { }}).isNullOrEmpty();
   *
   * // assertion will fail
   * assertThat(new String[][] {{&quot;a&quot;}, {&quot;b&quot;}}).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code boolean[][]}is not {@code null} or not empty.
   */
  @Override
  public void isNullOrEmpty() {
    boolean2dArrays.assertNullOrEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code boolean[][]}is empty, empty means the array has no elements,
   * said otherwise it can have any number of rows but all rows must be empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new boolean[][] {{ }}).isEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new boolean[][] {{ }, { }, { }}).isEmpty();
   *
   * // assertions will fail
   * assertThat(new boolean[][] {{ true }, { false }}).isEmpty();
   * boolean[][] array = null;
   * assertThat(array).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code boolean[][]}is not empty.
   */
  @Override
  public void isEmpty() {
    boolean2dArrays.assertEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code boolean[][]}is not empty, not empty means the array has at least one element.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new boolean[][] {{ true }, { false }}).isNotEmpty();
   * assertThat(new boolean[][] {{ }, { false }}).isNotEmpty();
   *
   * // assertions will fail
   * assertThat(new boolean[][] { }).isNotEmpty();
   * assertThat(new boolean[][] {{ }}).isNotEmpty();
   * // this is considered empty as there are no elements in the 2d array which is comprised of 3 empty rows.
   * assertThat(new boolean[][] {{ }, { }, { }}).isNotEmpty();
   * boolean[][] array = null;
   * assertThat(array).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code boolean[][]}is empty or null.
   */
  @Override
  public Boolean2DArrayAssert isNotEmpty() {
    boolean2dArrays.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual 2D array has the given dimensions.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[][] {{true, true, true}, {false, false, false}}).hasDimensions(2, 3);
   *
   * // assertions will fail
   * assertThat(new boolean[][] { }).hasSize(1, 1);
   * assertThat(new boolean[][] {{true, true, true}, {false, false, false}}).hasDimensions(3, 2);
   * assertThat(new boolean[][] {{true, true, true}, {false, false, false, false}}).hasDimensions(2, 3); </code></pre>
   *
   * @param expectedFirstDimension the expected number of values in first dimension of the actual array.
   * @param expectedSecondDimension the expected number of values in second dimension of the actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array's dimensions are not equal to the given ones.
   */
  @Override
  public Boolean2DArrayAssert hasDimensions(int expectedFirstDimension, int expectedSecondDimension) {
    boolean2dArrays.assertHasDimensions(info, actual, expectedFirstDimension, expectedSecondDimension);
    return myself;
  }

  /**
   * Verifies that the actual {@code boolean[][]} has the same dimensions as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object and primitive arrays.
   * </p>
   * Example:
   * <pre><code class='java'> boolean[][] booleanArray = {{true, true, false}, {false, false, true}};
   * char[][] charArray = {{'a', 'b', 'c'}, {'d', 'e', 'f'}};
   *
   * // assertion will pass
   * assertThat(booleanArray).hasSameDimensionsAs(charArray);
   *
   * // assertions will fail
   * assertThat(booleanArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd'}, {'e', 'f'}});
   * assertThat(booleanArray).hasSameDimensionsAs(new char[][] {{'a', 'b'}, {'c', 'd', 'e'}});
   * assertThat(booleanArray).hasSameDimensionsAs(new char[][] {{'a', 'b', 'c'}, {'d', 'e'}});</code></pre>
   *
   * @param array the array to compare dimensions with actual {@code boolean[][]}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code boolean[][]} is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual {@code boolean[][]} and given array don't have the same dimensions.
   */
  @Override
  public Boolean2DArrayAssert hasSameDimensionsAs(Object array) {
    boolean2dArrays.assertHasSameDimensionsAs(info, actual, array);
    return myself;
  }

  /**
   * Verifies that the actual {@code boolean[][]}contains the given boolean[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[][] {{true, false}, {false, true}}).contains(new boolean[] {true, false}, info);
   *
   * // assertion will fail
   * assertThat(new boolean[][] {{true, false}, {false, true}}).contains(new boolean[] {true, false}, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual {@code boolean[][]}is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual array.
   * @throws AssertionError if the actual {@code boolean[][]}does not contain the given value at the given index.
   */
  public Boolean2DArrayAssert contains(boolean[] value, Index index) {
    boolean2dArrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual {@code boolean[][]}does not contain the given boolean[] at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[][] {{true, false}, {false, true}}).doesNotContain(new boolean[] {true, false}, atIndex(1));
   *
   * // assertion will fail
   * assertThat(new boolean[][] {{true, false}, {false, true}}).doesNotContain(new boolean[] {true, false}, atIndex(0));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual {@code boolean[][]}is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual {@code boolean[][]}contains the given value at the given index.
   */
  public Boolean2DArrayAssert doesNotContain(boolean[] value, Index index) {
    boolean2dArrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }
}
