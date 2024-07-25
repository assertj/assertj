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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Hexadecimals.toHexString;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Comparator;

import org.assertj.core.data.Index;
import org.assertj.core.internal.ByteArrays;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractByteArrayAssert<SELF extends AbstractByteArrayAssert<SELF>>
    extends AbstractArrayAssert<SELF, byte[], Byte> {

  @VisibleForTesting
  protected ByteArrays arrays = ByteArrays.instance();

  protected AbstractByteArrayAssert(byte[] actual, Class<?> selfType) {
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

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).hasSize(3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3, 4 }).hasSize(3);</code></pre>
   */
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
   * assertThat(new byte[] { 2, 3 }).hasSizeGreaterThan(1);
   *
   * // assertion will fail
   * assertThat(new byte[] { 8 }).hasSizeGreaterThan(1);</code></pre>
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
   * Verifies that the number of values in the actual group is greater than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 17, 1 }).hasSizeGreaterThanOrEqualTo(1)
   *                                 .hasSizeGreaterThanOrEqualTo(2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 3 }).hasSizeGreaterThanOrEqualTo(2);</code></pre>
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
   * assertThat(new byte[] { 2, 9 }).hasSizeLessThan(3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 100, 101 }).hasSizeLessThan(1);</code></pre>
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
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 56, 57 }).hasSizeLessThanOrEqualTo(3)
   *                                  .hasSizeLessThanOrEqualTo(2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 42, 43 }).hasSizeLessThanOrEqualTo(1);</code></pre>
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
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 22, 23 }).hasSizeBetween(1, 3)
   *                                  .hasSizeBetween(2, 2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 37, 38 }).hasSizeBetween(4, 5);</code></pre>
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

  /**
   * Verifies that the actual group has the same size as given {@link Iterable}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 95, 96 }).hasSameSizeAs(Arrays.asList(2, 3));
   *
   * // assertion will fail
   * assertThat(new byte[] { 126, 127 }).hasSameSizeAs(Arrays.asList(1, 2, 3));</code></pre>
   */
  @Override
  public SELF hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).contains((byte) 1, (byte) 2);
   * assertThat(new byte[] { 1, 2, 3 }).contains((byte) 3, (byte) 1);
   * assertThat(new byte[] { 1, 2, 3 }).contains((byte) 1, (byte) 3, (byte) 2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).contains((byte) 1, (byte) 4);
   * assertThat(new byte[] { 1, 2, 3 }).contains((byte) 4, (byte) 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual array does not contain the given values.
   */
  public SELF contains(byte... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the values of the given array, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).contains(new Byte[] { 1,  2 });
   * assertThat(new byte[] { 1, 2, 3 }).contains(new Byte[] { 3,  1 });
   * assertThat(new byte[] { 1, 2, 3 }).contains(new Byte[] { 1,  3,  2 });
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).contains(new Byte[] { 1,  4 });
   * assertThat(new byte[] { 1, 2, 3 }).contains(new Byte[] { 4,  7 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual array does not contain the given values.
   * @since 3.19.0
   */
  public SELF contains(Byte[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContains(info, actual, toPrimitiveByteArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).contains(1, 2);
   * assertThat(new byte[] { 1, 2, 3 }).contains(3, 1);
   * assertThat(new byte[] { 1, 2, 3 }).contains(1, 3, 2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).contains(1, 4);
   * assertThat(new byte[] { 1, 2, 3 }).contains(4, 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual array does not contain the given values.
   * @since 2.6.0 / 3.6.0
   */
  public SELF contains(int... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly((byte) 1, (byte) 2, (byte) 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly((byte) 2, (byte) 3, (byte) 1);
   * assertThat(new byte[] { 1, 1, 2 }).containsOnly((byte) 1, (byte) 2);
   *
   * // assertions will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly((byte) 1, (byte) 2, (byte) 3, (byte) 4);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly((byte) 4, (byte) 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual array does not contain the given values, i.e. the actual array contains
   *                              some or none of the given values, or the actual array contains more values than the
   *                              given ones.
   */
  public SELF containsOnly(byte... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the values of the given array and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(new Byte[] { 1,  2,  3 });
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(new Byte[] { 2,  3,  1 });
   * assertThat(new byte[] { 1, 1, 2 }).containsOnly(new Byte[] { 1,  2 });
   *
   * // assertions will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(new Byte[] { 1,  2,  3,  4 });
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(new Byte[] { 4,  7 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual array does not contain the given values, i.e. the actual array contains
   *                              some or none of the given values, or the actual array contains more values than the
   *                              given ones.
   * @since 3.19.0
   */
  public SELF containsOnly(Byte[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsOnly(info, actual, toPrimitiveByteArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(1, 2, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(2, 3, 1);
   * assertThat(new byte[] { 1, 1, 2 }).containsOnly(1, 2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(1, 2, 3, 4);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(4, 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual array does not contain the given values, i.e. the actual array contains
   *                              some or none of the given values, or the actual array contains more values than the
   *                              given ones.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsOnly(int... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsOnlyOnce((byte) 1, (byte) 2);
   *
   * // assertions will fail
   * assertThat(new byte[] { 1, 2, 1 }).containsOnlyOnce((byte) 1);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnlyOnce((byte) 4);
   * assertThat(new byte[] { 1, 2, 3, 3 }).containsOnlyOnce((byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual group does not contain the given values, i.e. the actual group contains
   *                              some or none of the given values, or the actual group contains more than once these
   *                              values.
   */
  public SELF containsOnlyOnce(byte... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the values of the given array only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsOnlyOnce(new Byte[] { 1, 2 });
   *
   * // assertions will fail
   * assertThat(new byte[] { 1, 2, 1 }).containsOnlyOnce(new Byte[] { 1 });
   * assertThat(new byte[] { 1, 2, 3 }).containsOnlyOnce(new Byte[] { 4 });
   * assertThat(new byte[] { 1, 2, 3, 3 }).containsOnlyOnce(new Byte[] { 0,  1,  2,  3,  4,  5 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual group does not contain the given values, i.e. the actual group contains
   *                              some or none of the given values, or the actual group contains more than once these
   *                              values.
   * @since 3.19.0
   */
  public SELF containsOnlyOnce(Byte[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsOnlyOnce(info, actual, toPrimitiveByteArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsOnlyOnce(1, 2);
   *
   * // assertions will fail
   * assertThat(new byte[] { 1, 2, 1 }).containsOnlyOnce(1);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnlyOnce(4);
   * assertThat(new byte[] { 1, 2, 3, 3 }).containsOnlyOnce(0, 1, 2, 3, 4, 5);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual group does not contain the given values, i.e. the actual group contains
   *                              some or none of the given values, or the actual group contains more than once these
   *                              values.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsOnlyOnce(int... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence((byte) 1, (byte) 2);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence((byte) 1, (byte) 2, (byte) 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence((byte) 2, (byte) 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence((byte) 1, (byte) 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence((byte) 4, (byte) 7);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(byte... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(new Byte[] { 1,  2 });
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(new Byte[] { 1,  2,  3 });
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(new Byte[] { 2,  3 });
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(new Byte[] { 1,  3 });
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(new Byte[] { 4,  7 });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given sequence.
   * @since 3.19.0
   */
  public SELF containsSequence(Byte[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertContainsSequence(info, actual, toPrimitiveByteArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(1, 2);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(1, 2, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(2, 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(1, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(4, 7);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given sequence.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsSequence(int... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence((byte) 1, (byte) 2, (byte) 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence((byte) 1, (byte) 2);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence((byte) 1, (byte) 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence((byte) 2, (byte) 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence((byte) 2, (byte) 1);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence((byte) 4, (byte) 7);</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(byte... subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(new Byte[] { 1,  2,  3 });
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(new Byte[] { 1,  2 });
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(new Byte[] { 1,  3 });
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(new Byte[] { 2,  3 });
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(new Byte[] { 2,  1 });
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(new Byte[] { 4,  7 });</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   * @since 3.19.0
   */
  public SELF containsSubsequence(Byte[] subsequence) {
    requireNonNullParameter(subsequence, "subsequence");
    arrays.assertContainsSubsequence(info, actual, toPrimitiveByteArray(subsequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(1, 2, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(1, 2);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(1, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(2, 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(2, 1);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(4, 7);</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsSubsequence(int... subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).contains((byte) 1, atIndex(O));
   * assertThat(new byte[] { 1, 2, 3 }).contains((byte) 3, atIndex(2));
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).contains((byte) 1, atIndex(1));
   * assertThat(new byte[] { 1, 2, 3 }).contains((byte) 4, atIndex(2));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError            if the actual array is {@code null} or empty.
   * @throws NullPointerException      if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *                                   the actual array.
   * @throws AssertionError            if the actual array does not contain the given value at the given index.
   */
  public SELF contains(byte value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).contains(1, atIndex(O));
   * assertThat(new byte[] { 1, 2, 3 }).contains(3, atIndex(2));
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).contains(1, atIndex(1));
   * assertThat(new byte[] { 1, 2, 3 }).contains(4, atIndex(2));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError            if the actual array is {@code null} or empty.
   * @throws NullPointerException      if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *                                   the actual array.
   * @throws AssertionError            if the actual array does not contain the given value at the given index.
   * @since 2.6.0 / 3.6.0
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
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain((byte) 4);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain((byte) 2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array contains any of the given values.
   */
  public SELF doesNotContain(byte... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the values of the given array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(new Byte[] { 4 });
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(new Byte[] { 2 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array contains any of the given values.
   * @since 3.19.0
   */
  public SELF doesNotContain(Byte[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertDoesNotContain(info, actual, toPrimitiveByteArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(4);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array contains any of the given values.
   * @since 2.6.0 / 3.6.0
   */
  public SELF doesNotContain(int... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain((byte) 1, atIndex(1));
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain((byte) 2, atIndex(0));
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain((byte) 1, atIndex(0));
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain((byte) 2, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError       if the actual array contains the given value at the given index.
   */
  public SELF doesNotContain(byte value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(1, atIndex(1));
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(2, atIndex(0));
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(1, atIndex(0));
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(2, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError       if the actual array contains the given value at the given index.
   * @since 2.6.0 / 3.6.0
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
   * assertThat(new byte[] { 1, 2, 3 }).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 1, 2, 3 }).doesNotHaveDuplicates();</code></pre>
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
   * Similar to <code>{@link #containsSequence(byte...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).startsWith((byte) 1, (byte) 2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).startsWith((byte) 2, (byte) 3);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual array does not start with the given sequence.
   */
  public SELF startsWith(byte... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(byte...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).startsWith(new Byte[] { 1,  2 });
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).startsWith(new Byte[] { 2,  3 });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual array does not start with the given sequence.
   * @since 3.19.0
   */
  public SELF startsWith(Byte[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertStartsWith(info, actual, toPrimitiveByteArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(byte...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).startsWith(1, 2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).startsWith(2, 3);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual array is {@code null}.
   * @throws AssertionError       if the actual array does not start with the given sequence.
   * @since 2.6.0 / 3.6.0
   */
  public SELF startsWith(int... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(byte...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).endsWith((byte) 2, (byte) 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).endsWith((byte) 3, (byte) 4);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not end with the given sequence.
   */
  public SELF endsWith(byte... sequence) {
    arrays.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(byte...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).endsWith(new Byte[] { 2,  3 });
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).endsWith(new Byte[] { 3,  4 });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not end with the given sequence.
   * @since 3.19.0
   */
  public SELF endsWith(Byte[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertEndsWith(info, actual, toPrimitiveByteArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(byte...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).endsWith(2, 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).endsWith(3, 4);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not end with the given sequence.
   * @since 2.6.0 / 3.6.0
   */
  public SELF endsWith(int... sequence) {
    arrays.assertEndsWith(info, actual, sequence);
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
  public SELF isSortedAccordingTo(Comparator<? super Byte> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingElementComparator(Comparator<? super Byte> customComparator) {
    this.arrays = new ByteArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    this.arrays = ByteArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * <b>Warning</b>: for performance reason, this assertion compares arrays directly meaning that <b>it does not honor element
   * comparator</b> set with {@link #usingElementComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsExactly((byte) 1, (byte) 2, (byte) 3);
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(new byte[] { 1, 2, 3 }).containsExactly((byte) 2, (byte) 1, (byte) 3);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the actual group is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual group does not contain the given values with same order, i.e. the actual
   *                              group
   *                              contains some or none of the given values, or the actual group contains more values
   *                              than the given ones
   *                              or values are the same but the order is not.
   */
  public SELF containsExactly(byte... values) {
    // In #1801 we changed objects.assertEqual to arrays.assertContainsExactly to get a better error message but it came with
    // significant performance degradation as #1898 showed.
    // We can't get the best of both approaches even if we call assertContainsExactly only when assertEqual, assertContainsExactly
    // would take a long time to compute the diff between both arrays.
    // We can at least solve the representation of byte[] arrays so that they show the bytes
    objects.assertEqual(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains only the values of the given array and nothing else, <b>in order</b>.
   * <p>
   * <b>Warning</b>: for performance reason, this assertion compares arrays directly meaning that <b>it does not honor element
   * comparator</b> set with {@link #usingElementComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsExactly(new Byte[] { 1,  2,  3 });
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(new byte[] { 1, 2, 3 }).containsExactly(new Byte[] { 2,  1,  3 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the actual group is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual group does not contain the given values with same order, i.e. the actual
   *                              group
   *                              contains some or none of the given values, or the actual group contains more values
   *                              than the given ones
   *                              or values are the same but the order is not.
   * @since 3.19.0
   */
  public SELF containsExactly(Byte[] values) {
    // In #1801 we changed objects.assertEqual to arrays.assertContainsExactly to get a better error message but it came with
    // significant performance degradation as #1898 showed.
    // We can't get the best of both approaches even if we call assertContainsExactly only when assertEqual, assertContainsExactly
    // would take a long time to compute the diff between both arrays.
    // We can at least solve the representation of byte[] arrays so that they show the bytes

    requireNonNullParameter(values, "values");
    objects.assertEqual(info, actual, toPrimitiveByteArray(values));
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsExactly(1, 2, 3);
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(new byte[] { 1, 2, 3 }).containsExactly(2, 1, 3);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError       if the actual group is {@code null}.
   * @throws AssertionError       if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError       if the actual group does not contain the given values with same order, i.e. the actual
   *                              group
   *                              contains some or none of the given values, or the actual group contains more values
   *                              than the given ones
   *                              or values are the same but the order is not.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactly(int... values) {
    arrays.assertContainsExactly(info, actual, arrays.toByteArray(values));
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new byte[] { 1, 2 }).containsExactlyInAnyOrder((byte) 1, (byte) 2);
   * assertThat(new byte[] { 1, 2, 1 }).containsExactlyInAnyOrder((byte) 1, (byte) 1, (byte) 2);
   *
   * // assertions will fail
   * assertThat(new byte[] { 1, 2 }).containsExactlyInAnyOrder((byte) 1);
   * assertThat(new byte[] { 1 }).containsExactlyInAnyOrder((byte) 1, (byte) 2);
   * assertThat(new byte[] { 1, 2, 1 }).containsExactlyInAnyOrder((byte) 1, (byte) 2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactlyInAnyOrder(byte... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the values of the given array and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new byte[] { 1, 2 }).containsExactlyInAnyOrder(new Byte[] { 1,  2 });
   * assertThat(new byte[] { 1, 2, 1 }).containsExactlyInAnyOrder(new Byte[] { 1,  1,  2 });
   *
   * // assertions will fail
   * assertThat(new byte[] { 1, 2 }).containsExactlyInAnyOrder(new Byte[] { 1 });
   * assertThat(new byte[] { 1 }).containsExactlyInAnyOrder(new Byte[] { 1,  2 });
   * assertThat(new byte[] { 1, 2, 1 }).containsExactlyInAnyOrder(new Byte[] { 1,  2 });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 3.19.0
   */
  public SELF containsExactlyInAnyOrder(Byte[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsExactlyInAnyOrder(info, actual, toPrimitiveByteArray(values));
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new byte[] { 1, 2 }).containsExactlyInAnyOrder(1, 2);
   * assertThat(new byte[] { 1, 2, 1 }).containsExactlyInAnyOrder(1, 1, 2);
   *
   * // assertions will fail
   * assertThat(new byte[] { 1, 2 }).containsExactlyInAnyOrder(1);
   * assertThat(new byte[] { 1 }).containsExactlyInAnyOrder(1, 2);
   * assertThat(new byte[] { 1, 2, 1 }).containsExactlyInAnyOrder(1, 2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactlyInAnyOrder(int... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, arrays.toByteArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> byte[] oneTwoThree = { 1, 2, 3 };
   *
   * // assertions will pass
   * assertThat(oneTwoThree).containsAnyOf((byte)2)
   *                        .containsAnyOf((byte)2, (byte)3)
   *                        .containsAnyOf((byte)1, (byte)2, (byte)3)
   *                        .containsAnyOf((byte)1, (byte)2, (byte)3, (byte)4)
   *                        .containsAnyOf((byte)5, (byte)6, (byte)7, (byte)2);
   *
   * // assertions will fail
   * assertThat(oneTwoThree).containsAnyOf((byte)4);
   * assertThat(oneTwoThree).containsAnyOf((byte)4, (byte)5, (byte)6, (byte)7);</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws AssertionError if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  public SELF containsAnyOf(byte... values) {
    arrays.assertContainsAnyOf(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the values of the given array.
   * <p>
   * Example :
   * <pre><code class='java'> byte[] oneTwoThree = { 1, 2, 3 };
   *
   * // assertions will pass
   * assertThat(oneTwoThree).containsAnyOf(new Byte[] { 2 })
   *                        .containsAnyOf(new Byte[] { 2, 3 })
   *                        .containsAnyOf(new Byte[] { 1, 2, 3 })
   *                        .containsAnyOf(new Byte[] { 1, 2, 3, 4 })
   *                        .containsAnyOf(new Byte[] { 5, 6, 7, 2 });
   *
   * // assertions will fail
   * assertThat(oneTwoThree).containsAnyOf(new Byte[] { 4 });
   * assertThat(oneTwoThree).containsAnyOf(new Byte[] { 4, 5, 6, 7 });</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws AssertionError if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 3.19.0
   */
  public SELF containsAnyOf(Byte[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsAnyOf(info, actual, toPrimitiveByteArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> byte[] oneTwoThree = { 1, 2, 3 };
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
   * @throws AssertionError if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  public SELF containsAnyOf(int... values) {
    arrays.assertContainsAnyOf(info, actual, arrays.toByteArray(values));
    return myself;
  }

  /**
   * Converts the actual byte array under test to an hexadecimal String and returns assertions for the computed String
   * allowing String specific assertions from this call.
   * <p>
   * The Hex String representation is in upper case.
   * <p>
   * Example :
   * <pre><code class='java'> byte[] bytes = new byte[] { -1, 0, 1 };
   *
   * // assertions will pass
   * assertThat(bytes).asHexString()
   *                  .startsWith("FF")
   *                  .isEqualTo("FF0001");
   *
   * // assertion will fail
   * assertThat(bytes).asHexString()
   *                  .isEqualTo("FF0000");</code></pre>
   *
   * @return a String assertion object
   *
   * @since 3.16.0
   */
  @CheckReturnValue
  public AbstractStringAssert<?> asHexString() {
    objects.assertNotNull(info, actual);
    return assertThat(toHexString(actual));
  }

  /**
   * Converts the actual byte[] under test to a String and returns assertions for the computed String
   * allowing String specific assertions from this call.
   * <p>
   * The byte[] conversion to a String by decoding the specified bytes using the platform's default charset.
   * <p>
   * Example :
   * <pre><code class='java'> byte[] bytes = new byte[] { -1, 0, 1 };
   *
   * // assertions will pass
   * assertThat(bytes).asString()
   *                  .startsWith("FF")
   *                  .isEqualTo("FF0001");
   *
   * // assertion will fail
   * assertThat(bytes).asString()
   *                  .isEqualTo("FF0000");</code></pre>
   *
   * @return a String assertion object
   *
   * @since 3.17.0
   */
  @Override
  @CheckReturnValue
  public AbstractStringAssert<?> asString() {
    objects.assertNotNull(info, actual);
    String actualAsString = new String(actual);
    return assertThat(actualAsString);
  }

  /**
   * Converts the actual byte[] under test to a String by decoding the specified bytes using the given charset
   * and returns assertions for the computed String
   * allowing String specific assertions from this call.
   * <p>
   * The byte[] conversion to a String by decoding the specified bytes using the platform's default charset.
   * <p>
   * Example :
   * <pre><code class='java'> byte[] bytes = new byte[] { -1, 0, 1 };
   *
   * // assertions will pass
   * assertThat(bytes).asString()
   *                  .startsWith("FF")
   *                  .isEqualTo("FF0001");
   *
   * // assertion will fail
   * assertThat(bytes).asString()
   *                  .isEqualTo("FF0000");</code></pre>
   *
   * @param charset the {@link Charset} to interpret the bytes to a String
   * @return a String assertion object
   *
   * @since 3.17.0
   */
  @CheckReturnValue
  public AbstractStringAssert<?> asString(Charset charset) {
    objects.assertNotNull(info, actual);
    String actualAsString = new String(actual, charset);
    return assertThat(actualAsString);
  }

  /**
   * Encodes the actual array into a Base64 string, the encoded string becoming the new object under test.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion succeeds
   * assertThat("AssertJ".getBytes()).asBase64Encoded().isEqualTo(&quot;QXNzZXJ0Sg==&quot;);</code></pre>
   *
   * @return a new {@link StringAssert} instance whose string under test is the result of the encoding.
   * @throws AssertionError if the actual value is {@code null}.
   *
   * @since 3.22.0
   */
  @CheckReturnValue
  public AbstractStringAssert<?> asBase64Encoded() {
    objects.assertNotNull(info, actual);
    return new StringAssert(Base64.getEncoder().encodeToString(actual)).withAssertionState(myself);
  }

  /**
   * <p>
   * Encodes the actual array into a Base64 string, the encoded string becoming the new object under test.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion succeeds
   * assertThat("AssertJ".getBytes()).encodedAsBase64().isEqualTo(&quot;QXNzZXJ0Sg==&quot;);</code></pre>
   *
   * @return a new {@link StringAssert} instance whose string under test is the result of the encoding.
   * @throws AssertionError if the actual value is {@code null}.
   *
   * @since 3.16.0
   *
   * @deprecated use {@link #asBase64Encoded()} instead.
   */
  @Deprecated
  @CheckReturnValue
  public AbstractStringAssert<?> encodedAsBase64() {
    return asBase64Encoded();
  }

  private static byte[] toPrimitiveByteArray(Byte[] values) {
    byte[] bytes = new byte[values.length];
    range(0, values.length).forEach(i -> bytes[i] = values[i]);
    return bytes;
  }

}
