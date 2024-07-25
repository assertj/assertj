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

import static java.util.stream.IntStream.range;

import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ShortArrays;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractShortArrayAssert<SELF extends AbstractShortArrayAssert<SELF>>
    extends AbstractArrayAssert<SELF, short[], Short> {

  @VisibleForTesting
  protected ShortArrays arrays = ShortArrays.instance();

  protected AbstractShortArrayAssert(short[] actual, Class<?> selfType) {
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
   * assertThat(new short[] { 1, 2, 3 }).hasSizeGreaterThan(2);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).hasSizeGreaterThan(6);</code></pre>
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
   * assertThat(new short[] { 1, 2, 3 }).hasSizeGreaterThanOrEqualTo(2)
   *                                    .hasSizeGreaterThanOrEqualTo(3);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).hasSizeGreaterThanOrEqualTo(4);</code></pre>
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
   * assertThat(new short[] { 1, 2, 3 }).hasSizeLessThan(5);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).hasSizeLessThan(4);</code></pre>
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
   * assertThat(new short[] { 1, 2, 3 }).hasSizeLessThanOrEqualTo(5)
   *                                    .hasSizeLessThanOrEqualTo(3);
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).hasSizeLessThanOrEqualTo(4);</code></pre>
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
   * assertThat(new short[] { 1, 2, 3 }).hasSizeBetween(0, 4)
   *                                    .hasSizeBetween(3, 3);
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).hasSizeBetween(4, 6);
   * assertThat(new short[] { 1, 2, 3 }).hasSizeBetween(0, 2);</code></pre>
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
   * assertThat(new short[] { 1, 2, 3 }).contains((short) 1, (short) 2);
   * assertThat(new short[] { 1, 2, 3 }).contains((short) 3, (short) 1);
   * assertThat(new short[] { 1, 2, 3 }).contains((short) 1, (short) 3, (short) 2);
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).contains((short) 1, (short) 4);
   * assertThat(new short[] { 1, 2, 3 }).contains((short) 4, (short) 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   * @throws NullPointerException if the given argument is {@code null}.
   */
  public SELF contains(short... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the values of the given array, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2, 3 }).contains(new Short[] { 1,  2});
   * assertThat(new short[] { 1, 2, 3 }).contains(new Short[] { 3,  1});
   * assertThat(new short[] { 1, 2, 3 }).contains(new Short[] { 1,  3,  2});
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).contains(new Short[] { 1,  4});
   * assertThat(new short[] { 1, 2, 3 }).contains(new Short[] { 4,  7});</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.19.0
   */
  public SELF contains(Short[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContains(info, actual, toPrimitiveShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2, 3 }).contains(1, 2);
   * assertThat(new short[] { 1, 2, 3 }).contains(3, 1);
   * assertThat(new short[] { 1, 2, 3 }).contains(1, 3, 2);
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).contains(1, 4);
   * assertThat(new short[] { 1, 2, 3 }).contains(4, 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.16.0
   */
  public SELF contains(int... values) {
    arrays.assertContains(info, actual, toShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2, 3 }).containsOnly((short) 1, (short) 2, (short) 3);
   * assertThat(new short[] { 1, 2, 3 }).containsOnly((short) 2, (short) 3, (short) 1);
   * assertThat(new short[] { 1, 1, 2 }).containsOnly((short) 1, (short) 2);
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).containsOnly((short) 1, (short) 2, (short) 3, (short) 4);
   * assertThat(new short[] { 1, 2, 3 }).containsOnly((short) 4, (short) 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   * @throws NullPointerException if the given argument is {@code null}.
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public SELF containsOnly(short... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the values of the given array and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2, 3 }).containsOnly(new Short[] { 1,  2,  3 });
   * assertThat(new short[] { 1, 2, 3 }).containsOnly(new Short[] { 2,  3,  1 });
   * assertThat(new short[] { 1, 1, 2 }).containsOnly(new Short[] { 1,  2 });
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).containsOnly(new Short[] { 1,  2,  3,  4 });
   * assertThat(new short[] { 1, 2, 3 }).containsOnly(new Short[] { 4,  7 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.19.0
   */
  public SELF containsOnly(Short[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsOnly(info, actual, toPrimitiveShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2, 3 }).containsOnly(1, 2, 3);
   * assertThat(new short[] { 1, 2, 3 }).containsOnly(2, 3, 1);
   * assertThat(new short[] { 1, 1, 2 }).containsOnly(1, 2);
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 3 }).containsOnly(1, 2, 3, 4);
   * assertThat(new short[] { 1, 2, 3 }).containsOnly(4, 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.16.0
   */
  public SELF containsOnly(int... values) {
    arrays.assertContainsOnly(info, actual, toShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).containsOnlyOnce((short) 1,(short) 2);
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 1 }).containsOnlyOnce((short) 1);
   * assertThat(new short[] { 1, 2, 3 }).containsOnlyOnce((short) 4);
   * assertThat(new short[] { 1, 2, 3, 3 }).containsOnlyOnce((short) 0, (short) 1, (short) 2, (short) 3, (short) 4, (short) 5);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   * @throws NullPointerException if the given argument is {@code null}.
   */
  public SELF containsOnlyOnce(short... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the values of the given array only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).containsOnlyOnce(new Short[] { 1, 2 });
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 1 }).containsOnlyOnce(new Short[] { 1 });
   * assertThat(new short[] { 1, 2, 3 }).containsOnlyOnce(new Short[] { 4 });
   * assertThat(new short[] { 1, 2, 3, 3 }).containsOnlyOnce(new Short[] { 0,  1,  2,  3,  4, 5 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.19.0
   */
  public SELF containsOnlyOnce(Short[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsOnlyOnce(info, actual, toPrimitiveShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).containsOnlyOnce(1,2);
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2, 1 }).containsOnlyOnce(1);
   * assertThat(new short[] { 1, 2, 3 }).containsOnlyOnce(4);
   * assertThat(new short[] { 1, 2, 3, 3 }).containsOnlyOnce(0, 1, 2, 3, 4, 5);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.16.0
   */
  public SELF containsOnlyOnce(int... values) {
    arrays.assertContainsOnlyOnce(info, actual, toShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).containsSequence((short) 1, (short) 2);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).containsSequence((short) 1, (short) 3);
   * assertThat(new short[] { 1, 2, 3 }).containsSequence((short) 2, (short) 1);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(short... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).containsSequence(new Short[] { 1,  2 });
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).containsSequence(new Short[] { 1,  3 });
   * assertThat(new short[] { 1, 2, 3 }).containsSequence(new Short[] { 2,  1 });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given sequence.
   * @since 3.19.0
   */
  public SELF containsSequence(Short[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertContainsSequence(info, actual, toPrimitiveShortArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).containsSequence(1, 2);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).containsSequence(1, 3);
   * assertThat(new short[] { 1, 2, 3 }).containsSequence(2, 1);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given sequence.
   * @since 3.16.0
   */
  public SELF containsSequence(int... sequence) {
    arrays.assertContainsSequence(info, actual, toShortArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).containsSubsequence((short) 1, (short) 2);
   * assertThat(new short[] { 1, 2, 3 }).containsSubsequence((short) 1, (short) 3);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).containsSubsequence((short) 2, (short) 1);</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(short... subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).containsSubsequence(new Short[] { 1,  2 });
   * assertThat(new short[] { 1, 2, 3 }).containsSubsequence(new Short[] { 1,  3 });
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).containsSubsequence(new Short[] { 2,  1 });</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   * @since 3.19.0
   */
  public SELF containsSubsequence(Short[] subsequence) {
    requireNonNullParameter(subsequence, "subsequence");
    arrays.assertContainsSubsequence(info, actual, toPrimitiveShortArray(subsequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).containsSubsequence(1, 2);
   * assertThat(new short[] { 1, 2, 3 }).containsSubsequence(1, 3);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).containsSubsequence(2, 1);</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   * @since 3.16.0
   */
  public SELF containsSubsequence(int... subsequence) {
    arrays.assertContainsSubsequence(info, actual, toShortArray(subsequence));
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
   * @throws AssertionError if the actual array does not contain the given value at the given index.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual array.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   */
  public SELF contains(short value, Index index) {
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
   * @throws AssertionError if the actual array does not contain the given value at the given index.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual array.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @since 3.16.0
   */
  public SELF contains(int value, Index index) {
    arrays.assertContains(info, actual, toShort(value), index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain((short) 4);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain((short) 2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws NullPointerException if the given argument is {@code null}.
   */
  public SELF doesNotContain(short... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the values of the given array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain(new Short[] { 4 });
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain(new Short[] { 2 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.19.0
   */
  public SELF doesNotContain(Short[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertDoesNotContain(info, actual, toPrimitiveShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain(4);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).doesNotContain(2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.16.0
   */
  public SELF doesNotContain(int... values) {
    arrays.assertDoesNotContain(info, actual, toShortArray(values));
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
   * @throws AssertionError if the actual array contains the given value at the given index.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   */
  public SELF doesNotContain(short value, Index index) {
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
   * @throws AssertionError if the actual array contains the given value at the given index.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @since 3.16.0
   */
  public SELF doesNotContain(int value, Index index) {
    arrays.assertDoesNotContain(info, actual, toShort(value), index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain duplicates.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 1, 2, 3 }).doesNotHaveDuplicates();</code></pre>
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
   * Similar to <code>{@link #containsSequence(short...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).startsWith((short) 1, (short) 2);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).startsWith((short) 2, (short) 3);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   * @throws NullPointerException if the given argument is {@code null}.
   */
  public SELF startsWith(short... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(Short[])}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).startsWith(new Short[] { 1,  2 });
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).startsWith(new Short[] { 2,  3 });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.19.0
   */
  public SELF startsWith(Short[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertStartsWith(info, actual, toPrimitiveShortArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(short...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).startsWith(1, 2);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).startsWith(2, 3);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.16.0
   */
  public SELF startsWith(int... sequence) {
    arrays.assertStartsWith(info, actual, toShortArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(short...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).endsWith((short) 2, (short) 3);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).endsWith((short) 3, (short) 4);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   * @throws NullPointerException if the given argument is {@code null}.
   */
  public SELF endsWith(short... sequence) {
    arrays.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(short...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).endsWith(new Short[] { 2,  3 });
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).endsWith(new Short[] { 3,  4 });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.19.0
   */
  public SELF endsWith(Short[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertEndsWith(info, actual, toPrimitiveShortArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(short...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new short[] { 1, 2, 3 }).endsWith(2, 3);
   *
   * // assertion will fail
   * assertThat(new short[] { 1, 2, 3 }).endsWith(3, 4);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.16.0
   */
  public SELF endsWith(int... sequence) {
    arrays.assertEndsWith(info, actual, toShortArray(sequence));
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
  public SELF isSortedAccordingTo(Comparator<? super Short> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingElementComparator(Comparator<? super Short> customComparator) {
    this.arrays = new ShortArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    this.arrays = ShortArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> short[] shorts = { 1, 2, 3 };
   *
   * // assertion will pass
   * assertThat(shorts).containsExactly((short) 1, (short) 2, (short) 3);
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(shorts).containsExactly((short) 2, (short) 1, (short) 3);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   * @throws NullPointerException if the given argument is {@code null}.
   */
  public SELF containsExactly(short... values) {
    arrays.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains only the values of the given array and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> short[] shorts = { 1, 2, 3 };
   *
   * // assertion will pass
   * assertThat(shorts).containsExactly(new Short[] { 1,  2,  3 });
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(shorts).containsExactly(new Short[] { 2,  1,  3 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.19.0
   */
  public SELF containsExactly(Short[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsExactly(info, actual, toPrimitiveShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> short[] shorts = { 1, 2, 3 };
   *
   * // assertion will pass
   * assertThat(shorts).containsExactly(1, 2, 3);
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(shorts).containsExactly(2, 1, 3);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.16.0
   */
  public SELF containsExactly(int... values) {
    arrays.assertContainsExactly(info, actual, toShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2 }).containsExactlyInAnyOrder((short) 1, (short) 2);
   * assertThat(new short[] { 1, 2, 1 }).containsExactlyInAnyOrder((short) 1, (short) 1, (short) 2);
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2 }).containsExactlyInAnyOrder((short) 1);
   * assertThat(new short[] { 1 }).containsExactlyInAnyOrder((short) 1, (short) 2);
   * assertThat(new short[] { 1, 2, 1 }).containsExactlyInAnyOrder((short) 1, (short) 2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactlyInAnyOrder(short... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the values of the given array and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2 }).containsExactlyInAnyOrder(new Short[] { 1,  2 });
   * assertThat(new short[] { 1, 2, 1 }).containsExactlyInAnyOrder(new Short[] { 1,  1,  2 });
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2 }).containsExactlyInAnyOrder(new Short[] { 1 });
   * assertThat(new short[] { 1 }).containsExactlyInAnyOrder(new Short[] { 1,  2 });
   * assertThat(new short[] { 1, 2, 1 }).containsExactlyInAnyOrder(new Short[] { 1,  2 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.19.0
   */
  public SELF containsExactlyInAnyOrder(Short[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsExactlyInAnyOrder(info, actual, toPrimitiveShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new short[] { 1, 2 }).containsExactlyInAnyOrder(1, 2);
   * assertThat(new short[] { 1, 2, 1 }).containsExactlyInAnyOrder(1, 1, 2);
   *
   * // assertions will fail
   * assertThat(new short[] { 1, 2 }).containsExactlyInAnyOrder(1);
   * assertThat(new short[] { 1 }).containsExactlyInAnyOrder(1, 2);
   * assertThat(new short[] { 1, 2, 1 }).containsExactlyInAnyOrder(1, 2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @throws NullPointerException if the given argument is {@code null}.
   * @since 3.16.0
   */
  public SELF containsExactlyInAnyOrder(int... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, toShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> short[] oneTwoThree = { 1, 2, 3 };
   *
   * // assertions will pass
   * assertThat(abc).containsAnyOf((short) 2)
   *                .containsAnyOf((short) 2, (short) 3)
   *                .containsAnyOf((short) 1, (short) 2, (short) 3)
   *                .containsAnyOf((short) 1, (short) 2, (short) 3, (short) 4)
   *                .containsAnyOf((short) 5, (short) 6, (short) 7, (short) 2);
   *
   * // assertions will fail
   * assertThat(abc).containsAnyOf((short) 4);
   * assertThat(abc).containsAnyOf((short) 4, (short) 5, (short) 6, (short) 7);</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws AssertionError if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @since 2.9.0 / 3.9.0
   */
  public SELF containsAnyOf(short... values) {
    arrays.assertContainsAnyOf(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the values of the given array.
   * <p>
   * Example :
   * <pre><code class='java'> short[] oneTwoThree = { 1, 2, 3 };
   *
   * // assertions will pass
   * assertThat(abc).containsAnyOf(new Short[] { 2 })
   *                .containsAnyOf(new Short[] { 2,  3 })
   *                .containsAnyOf(new Short[] { 1,  2,  3 })
   *                .containsAnyOf(new Short[] { 1,  2,  3,  4 })
   *                .containsAnyOf(new Short[] { 5,  6,  7,  2 });
   *
   * // assertions will fail
   * assertThat(abc).containsAnyOf(new Short[] { 4 });
   * assertThat(abc).containsAnyOf(new Short[] { 4,  5,  6,  7 });</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws AssertionError if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @since 3.19.0
   */
  public SELF containsAnyOf(Short[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsAnyOf(info, actual, toPrimitiveShortArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> short[] oneTwoThree = { 1, 2, 3 };
   *
   * // assertions will pass
   * assertThat(abc).containsAnyOf(2)
   *                .containsAnyOf(2, 3)
   *                .containsAnyOf(1, 2, 3)
   *                .containsAnyOf(1, 2, 3, 4)
   *                .containsAnyOf(5, 6, 7, 2);
   *
   * // assertions will fail
   * assertThat(abc).containsAnyOf(4);
   * assertThat(abc).containsAnyOf(4, 5, 6, 7);</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws AssertionError if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @since 3.16.0
   */
  public SELF containsAnyOf(int... values) {
    arrays.assertContainsAnyOf(info, actual, toShortArray(values));
    return myself;
  }

  private static short[] toShortArray(int[] ints) {
    if (ints == null) return null;
    short[] shorts = new short[ints.length];
    for (int i = 0; i < shorts.length; i++) {
      shorts[i] = toShort(ints[i]);
    }
    return shorts;
  }

  private static short toShort(int value) {
    return (short) value;
  }

  private static short[] toPrimitiveShortArray(Short[] values) {
    short[] shorts = new short[values.length];
    range(0, values.length).forEach(i -> shorts[i] = values[i]);
    return shorts;
  }

}
