/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

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

  public AbstractByteArrayAssert(byte[] actual, Class<?> selfType) {
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
   *
   * </p>
   */
  @Override
  public SELF hasSize(int expected) {
    arrays.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual group has the same size as given {@link Iterable}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2 }).hasSameSizeAs(Arrays.asList(2, 3));
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2 }).hasSameSizeAs(Arrays.asList(1, 2, 3));</code></pre>
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
   * </p>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not contain the given values.
   */
  public SELF contains(byte... values) {
    arrays.assertContains(info, actual, values);
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
   * </p>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not contain the given values.
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
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly((byte) 1, (byte) 2, (byte) 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly((byte) 2, (byte) 3, (byte) 1);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly((byte) 1, (byte) 2, (byte) 3, (byte) 4);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly((byte) 4, (byte) 7);</code></pre>
   *
   * </p>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not contain the given values, i.e. the actual array
   *                                  contains some
   *                                  or none of the given values, or the actual array contains more values than the
   *                                  given ones.
   */
  public SELF containsOnly(byte... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(1, 2, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(2, 3, 1);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(1, 2, 3, 4);
   * assertThat(new byte[] { 1, 2, 3 }).containsOnly(4, 7);</code></pre>
   *
   * </p>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not contain the given values, i.e. the actual array
   *                                  contains some
   *                                  or none of the given values, or the actual array contains more values than the
   *                                  given ones.
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
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual group does not contain the given values, i.e. the actual group
   *                                  contains some
   *                                  or none of the given values, or the actual group contains more than once these
   *                                  values.
   */
  public SELF containsOnlyOnce(byte... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
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
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual group does not contain the given values, i.e. the actual group
   *                                  contains some
   *                                  or none of the given values, or the actual group contains more than once these
   *                                  values.
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
   * </p>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
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
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(1, 2);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(1, 2, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(2, 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(1, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSequence(4, 7);</code></pre>
   *
   * </p>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
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
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsubsequence((byte) 1, (byte) 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence((byte) 2, (byte) 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence((byte) 2, (byte) 1);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence((byte) 4, (byte) 7);</code></pre>
   *
   * </p>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
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
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(1, 2, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(1, 2);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsubsequence(1, 3);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(2, 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(2, 1);
   * assertThat(new byte[] { 1, 2, 3 }).containsSubsequence(4, 7);</code></pre>
   *
   * </p>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
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
   * </p>
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
   * </p>
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
   * </p>
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
   * Verifies that the actual array does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(4);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).doesNotContain(2);</code></pre>
   *
   * </p>
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
   * </p>
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
   * </p>
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
   * </p>
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
   * </p>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not start with the given sequence.
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
   * assertThat(new byte[] { 1, 2, 3 }).startsWith(1, 2);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).startsWith(2, 3);</code></pre>
   *
   * </p>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError           if the actual array is {@code null}.
   * @throws AssertionError           if the actual array does not start with the given sequence.
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
   * </p>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
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
   * assertThat(new byte[] { 1, 2, 3 }).endsWith(2, 3);
   *
   * // assertion will fail
   * assertThat(new byte[] { 1, 2, 3 }).endsWith(3, 4);</code></pre>
   *
   * </p>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException     if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
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
   * @throws AssertionError       if the actual group does not contain the given values with same order, i.e. the actual
   *                              group
   *                              contains some or none of the given values, or the actual group contains more values
   *                              than the given ones
   *                              or values are the same but the order is not.
   */
  public SELF containsExactly(byte... values) {
    objects.assertEqual(info, actual, values);
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
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactlyInAnyOrder(byte... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, values);
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
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactlyInAnyOrder(int... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, arrays.toByteArray(values));
    return myself;
  }
  
}
