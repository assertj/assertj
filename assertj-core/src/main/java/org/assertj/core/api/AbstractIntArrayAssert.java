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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Arrays;
import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.IntArrays;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractIntArrayAssert<SELF extends AbstractIntArrayAssert<SELF>>
    extends AbstractArrayAssert<SELF, int[], Integer> {

  @VisibleForTesting
  protected IntArrays arrays = IntArrays.instance();

  protected AbstractIntArrayAssert(int[] actual, Class<?> selfType) {
    super(actual, selfType);
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
  public SELF isNotEmpty() {
    arrays.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF hasSize(int expected) {
    arrays.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is greater than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).hasSizeGreaterThan(2);
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).hasSizeGreaterThan(6);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not greater than the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThan(int boundary) {
    arrays.assertHasSizeGreaterThan(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is greater than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[] { 1, 2, 3 }).hasSizeGreaterThanOrEqualTo(2)
   *                                  .hasSizeGreaterThanOrEqualTo(3);
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).hasSizeGreaterThanOrEqualTo(4);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not greater than or equal to the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThanOrEqualTo(int boundary) {
    arrays.assertHasSizeGreaterThanOrEqualTo(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is less than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).hasSizeLessThan(5);
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).hasSizeLessThan(4);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not less than the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThan(int boundary) {
    arrays.assertHasSizeLessThan(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is less than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[] { 1, 2, 3 }).hasSizeLessThanOrEqualTo(5)
   *                                  .hasSizeLessThanOrEqualTo(3);
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2, 3 }).hasSizeLessThanOrEqualTo(4);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not less than or equal to the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThanOrEqualTo(int boundary) {
    arrays.assertHasSizeLessThanOrEqualTo(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is between the given boundaries (inclusive).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[] { 1, 2, 3 }).hasSizeBetween(0, 4)
   *                                  .hasSizeBetween(3, 3);
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2, 3 }).hasSizeBetween(4, 6);
   * assertThat(new int[] { 1, 2, 3 }).hasSizeBetween(0, 2);</code></pre>
   *
   * @param lowerBoundary the lower boundary compared to which actual size should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual size should be less than or equal to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not between the boundaries.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeBetween(int lowerBoundary, int higherBoundary) {
    arrays.assertHasSizeBetween(info, actual, lowerBoundary, higherBoundary);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[] { 1, 2, 3 }).contains(1, 2);
   * assertThat(new int[] { 1, 2, 3 }).contains(3, 1);
   * assertThat(new int[] { 1, 2, 3 }).contains(1, 3, 2);
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2, 3 }).contains(1, 4);
   * assertThat(new int[] { 1, 2, 3 }).contains(4, 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  public SELF contains(int... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the values of the given array, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).contains(new Integer[] { 1, 2 });
   * assertThat(new int[] { 1, 2, 3 }).contains(new Integer[] { 3, 1 });
   * assertThat(new int[] { 1, 2, 3 }).contains(new Integer[] { 1, 3, 2 });
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).contains(new Integer[] { 1, 4 });
   * assertThat(new int[] { 1, 2, 3 }).contains(new Integer[] { 4, 7 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   * @since 3.19.0
   */
  public SELF contains(Integer[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContains(info, actual, toPrimitiveIntArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[] { 1, 2, 3 }).containsOnly(1, 2, 3);
   * assertThat(new int[] { 1, 2, 3 }).containsOnly(2, 3, 1);
   * assertThat(new int[] { 1, 1, 2 }).containsOnly(1, 2);
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2, 3 }).containsOnly(1, 2, 3, 4);
   * assertThat(new int[] { 1, 2, 3 }).containsOnly(4, 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public SELF containsOnly(int... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the values of the given array and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[] { 1, 2, 3 }).containsOnly(new Integer[] { 1, 2, 3 });
   * assertThat(new int[] { 1, 2, 3 }).containsOnly(new Integer[] { 2, 3, 1 });
   * assertThat(new int[] { 1, 1, 2 }).containsOnly(new Integer[] { 1, 2 });
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2, 3 }).containsOnly(new Integer[] { 1, 2, 3, 4 });
   * assertThat(new int[] { 1, 2, 3 }).containsOnly(new Integer[] { 4, 7 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   * @since 3.19.0
   */
  public SELF containsOnly(Integer[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsOnly(info, actual, toPrimitiveIntArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).containsOnlyOnce(1, 2);
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2, 1 }).containsOnlyOnce(1);
   * assertThat(new int[] { 1, 2, 3 }).containsOnlyOnce(4);
   * assertThat(new int[] { 1, 2, 3, 3 }).containsOnlyOnce(0, 1, 2, 3, 4, 5);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more than once these values.
   */
  public SELF containsOnlyOnce(int... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the values of the given array only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).containsOnlyOnce(new Integer[] { 1, 2 });
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2, 1 }).containsOnlyOnce(new Integer[] { 1 });
   * assertThat(new int[] { 1, 2, 3 }).containsOnlyOnce(new Integer[] { 4 });
   * assertThat(new int[] { 1, 2, 3, 3 }).containsOnlyOnce(new Integer[] { 0, 1, 2, 3, 4, 5 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   * @since 3.19.0
   */
  public SELF containsOnlyOnce(Integer[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsOnlyOnce(info, actual, toPrimitiveIntArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).containsSequence(1, 2);
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).containsSequence(1, 3);
   * assertThat(new int[] { 1, 2, 3 }).containsSequence(2, 1);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(int... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).containsSequence(new Integer[] { 1, 2 });
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).containsSequence(new Integer[] { 1, 3 });
   * assertThat(new int[] { 1, 2, 3 }).containsSequence(new Integer[] { 2, 1 });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   * @since 3.19.0
   */
  public SELF containsSequence(Integer[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertContainsSequence(info, actual, toPrimitiveIntArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).containsSubsequence(1, 2);
   * assertThat(new int[] { 1, 2, 3 }).containsSubsequence(1, 3);
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).containsSubsequence(2, 1);</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(int... subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).containsSubsequence(new Integer[] { 1, 2 });
   * assertThat(new int[] { 1, 2, 3 }).containsSubsequence(new Integer[] { 1, 3 });
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).containsSubsequence(new Integer[] { 2, 1 });</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   * @since 3.19.0
   */
  public SELF containsSubsequence(Integer[] subsequence) {
    requireNonNullParameter(subsequence, "subsequence");
    arrays.assertContainsSubsequence(info, actual, toPrimitiveIntArray(subsequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[] { 1, 2, 3 }).contains(1, atIndex(O));
   * assertThat(new int[] { 1, 2, 3 }).contains(3, atIndex(2));
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2, 3 }).contains(1, atIndex(1));
   * assertThat(new int[] { 1, 2, 3 }).contains(4, atIndex(2));</code></pre>
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
  public SELF contains(int value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).doesNotContain(4);
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).doesNotContain(2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public SELF doesNotContain(int... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the values of the given array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).doesNotContain(new Integer[] { 4 });
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).doesNotContain(new Integer[] { 2 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   * @since 3.19.0
   */
  public SELF doesNotContain(Integer[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertDoesNotContain(info, actual, toPrimitiveIntArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[] { 1, 2, 3 }).doesNotContain(1, atIndex(1));
   * assertThat(new int[] { 1, 2, 3 }).doesNotContain(2, atIndex(0));
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2, 3 }).doesNotContain(1, atIndex(0));
   * assertThat(new int[] { 1, 2, 3 }).doesNotContain(2, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public SELF doesNotContain(int value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain duplicates.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 1, 2, 3 }).doesNotHaveDuplicates();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains duplicates.
   */
  public SELF doesNotHaveDuplicates() {
    arrays.assertDoesNotHaveDuplicates(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(int...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).startsWith(1, 2);
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).startsWith(2, 3);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   */
  public SELF startsWith(int... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(int...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).startsWith(new Integer[] { 1, 2 });
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).startsWith(new Integer[] { 2, 3 });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   * @since 3.19.0
   */
  public SELF startsWith(Integer[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertStartsWith(info, actual, toPrimitiveIntArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(int...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).endsWith(2, 3);
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).endsWith(3, 4);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF endsWith(int... sequence) {
    arrays.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(int...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).endsWith(new Integer[] { 2, 3 });
   *
   * // assertion will fail
   * assertThat(new int[] { 1, 2, 3 }).endsWith(new Integer[] { 3, 4 });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   * @since 3.19.0
   */
  public SELF endsWith(Integer[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertEndsWith(info, actual, toPrimitiveIntArray(sequence));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isSorted() {
    arrays.assertIsSorted(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isSortedAccordingTo(Comparator<? super Integer> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingElementComparator(Comparator<? super Integer> customComparator) {
    this.arrays = new IntArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    this.arrays = IntArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> int[] ints = { 1, 2, 3 };
   *
   * // assertion will pass
   * assertThat(ints).containsExactly(1, 2, 3);
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(ints).containsExactly(2, 1, 3);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values with same order, i.e. the actual array
   *           contains some or none of the given values, or the actual array contains more values than the given ones
   *           or values are the same but the order is not.
   */
  public SELF containsExactly(int... values) {
    arrays.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains only the values of the given array and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new int[] { 1, 2, 3 }).containsExactly(new Integer[] { 1, 2, 3 });
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(new int[] { 1, 2, 3 }).containsExactly(new Integer[] { 1, 3, 2 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   * @since 3.19.0
   */
  public SELF containsExactly(Integer[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsExactly(info, actual, toPrimitiveIntArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[] { 1, 2 }).containsExactlyInAnyOrder(1, 2);
   * assertThat(new int[] { 1, 2, 1 }).containsExactlyInAnyOrder(1, 1, 2);
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2 }).containsExactlyInAnyOrder(1);
   * assertThat(new int[] { 1 }).containsExactlyInAnyOrder(1, 2);
   * assertThat(new int[] { 1, 2, 1 }).containsExactlyInAnyOrder(1, 2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array
   *           contains some or none of the given values, or the actual array contains more values than the given ones.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactlyInAnyOrder(int... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the values of the given array and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new int[] { 1, 2 }).containsExactlyInAnyOrder(new Integer[] { 2, 1 });
   * assertThat(new int[] { 1, 2, 3 }).containsExactlyInAnyOrder(new Integer[] { 3, 2, 1 });
   *
   * // assertions will fail
   * assertThat(new int[] { 1, 2 }).containsExactlyInAnyOrder(new Integer[] { 1, 4 });
   * assertThat(new int[] { 1 }).containsExactlyInAnyOrder(new Integer[] { 4, 1 });
   * assertThat(new int[] { 1, 2, 3 }).containsExactlyInAnyOrder(new Integer[] { 1, 2 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 3.19.0
   */
  public SELF containsExactlyInAnyOrder(Integer[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsExactlyInAnyOrder(info, actual, toPrimitiveIntArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> int[] oneTwoThree = { 1, 2, 3 };
   *
   * // assertions will pass
   * assertThat(oneTwoThree).containsAnyOf(2)
   *                        .containsAnyOf(2, 3)
   *                        .containsAnyOf(1, 2, 3)
   *                        .containsAnyOf(1, 2, 3, 4)
   *                        .containsAnyOf(5, 6, 7, 2);
   *
   * // assertions will fail
   * assertThat(oneTwoThree).containsAnyOf(4);
   * assertThat(oneTwoThree).containsAnyOf(4, 5, 6, 7);</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  public SELF containsAnyOf(int... values) {
    arrays.assertContainsAnyOf(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the values of the given array.
   * <p>
   * Example :
   * <pre><code class='java'> int[] oneTwoThree = { 1, 2, 3 };
   *
   * // assertions will pass
   * assertThat(oneTwoThree).containsAnyOf(new Integer[] { 2 })
   *                        .containsAnyOf(new Integer[] { 2, 3 })
   *                        .containsAnyOf(new Integer[] { 1, 2, 3 })
   *                        .containsAnyOf(new Integer[] { 1, 2, 3, 4 })
   *                        .containsAnyOf(new Integer[] { 5, 6, 7, 2 });
   *
   * // assertions will fail
   * assertThat(oneTwoThree).containsAnyOf(new Integer[] { 4 });
   * assertThat(oneTwoThree).containsAnyOf(new Integer[] { 4, 5, 6, 7 });</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 3.19.0
   */
  public SELF containsAnyOf(Integer[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsAnyOf(info, actual, toPrimitiveIntArray(values));
    return myself;
  }

  private static int[] toPrimitiveIntArray(Integer[] values) {
    return Arrays.stream(values).mapToInt(Integer::intValue).toArray();
  }

}
