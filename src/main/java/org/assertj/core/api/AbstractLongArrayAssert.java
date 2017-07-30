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
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.LongArrays;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractLongArrayAssert<SELF extends AbstractLongArrayAssert<SELF>>
  extends AbstractArrayAssert<SELF, long[], Long> {

  @VisibleForTesting
  protected LongArrays arrays = LongArrays.instance();

  public AbstractLongArrayAssert(long[] actual, Class<?> selfType) {
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
   * assertThat(new long[] { 1L, 2L, 3L }).contains(1L, 2L);
   * assertThat(new long[] { 1L, 2L, 3L }).contains(3L, 1L);
   * assertThat(new long[] { 1L, 2L, 3L }).contains(1L, 3L, 2L);
   *
   * // assertions will fail
   * assertThat(new long[] { 1L, 2L, 3L }).contains(1L, 4L);
   * assertThat(new long[] { 1L, 2L, 3L }).contains(4L, 7L);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  public SELF contains(long... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new long[] { 1L, 2L, 3L }).containsOnly(1L, 2L, 3L);
   * assertThat(new long[] { 1L, 2L, 3L }).containsOnly(2L, 3L, 1L);
   *
   * // assertions will fail
   * assertThat(new long[] { 1L, 2L, 3L }).containsOnly(1L, 2L, 3L, 4L);
   * assertThat(new long[] { 1L, 2L, 3L }).containsOnly(4L, 7L);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public SELF containsOnly(long... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[] { 1, 2, 3 }).containsOnlyOnce(1, 2);
   * 
   * // assertions will fail
   * assertThat(new long[] { 1, 2, 1 }).containsOnlyOnce(1);
   * assertThat(new long[] { 1, 2, 3 }).containsOnlyOnce(4);
   * assertThat(new long[] { 1, 2, 3, 3 }).containsOnlyOnce(0, 1, 2, 3, 4, 5);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   */
  public SELF containsOnlyOnce(long... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[] { 1, 2, 3 }).containsSequence(1, 2);
   * 
   * // assertion will fail
   * assertThat(new long[] { 1, 2, 3 }).containsSequence(1, 3);
   * assertThat(new long[] { 1, 2, 3 }).containsSequence(2, 1);</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(long... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[] { 1, 2, 3 }).containsSubsequence(1, 2);
   * assertThat(new long[] { 1, 2, 3 }).containsSubsequence(1, 3);
   * 
   * // assertion will fail
   * assertThat(new long[] { 1, 2, 3 }).containsSubsequence(2, 1);</code></pre>
   * 
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(long... subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new long[] { 1L, 2L, 3L }).contains(1L, atIndex(O));
   * assertThat(new long[] { 1L, 2L, 3L }).contains(3L, atIndex(2));
   *
   * // assertions will fail
   * assertThat(new long[] { 1L, 2L, 3L }).contains(1L, atIndex(1));
   * assertThat(new long[] { 1L, 2L, 3L }).contains(4L, atIndex(2));</code></pre>
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
  public SELF contains(long value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[] { 1L, 2L, 3L }).doesNotContain(4L);
   *
   * // assertion will fail
   * assertThat(new long[] { 1L, 2L, 3L }).doesNotContain(2L);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public SELF doesNotContain(long... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new long[] { 1L, 2L, 3L }).doesNotContain(1L, atIndex(1));
   * assertThat(new long[] { 1L, 2L, 3L }).doesNotContain(2L, atIndex(0));
   *
   * // assertions will fail
   * assertThat(new long[] { 1L, 2L, 3L }).doesNotContain(1L, atIndex(0));
   * assertThat(new long[] { 1L, 2L, 3L }).doesNotContain(2L, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public SELF doesNotContain(long value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain duplicates.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[] { 1L, 2L, 3L }).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(new long[] { 1L, 1L, 2L, 3L }).doesNotHaveDuplicates();</code></pre>
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
   * Similar to <code>{@link #containsSequence(long...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[] { 1L, 2L, 3L }).startsWith(1L, 2L);
   *
   * // assertion will fail
   * assertThat(new long[] { 1L, 2L, 3L }).startsWith(2L, 3L);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   */
  public SELF startsWith(long... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(long...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new long[] { 1L, 2L, 3L }).endsWith(2L, 3L);
   *
   * // assertion will fail
   * assertThat(new long[] { 1L, 2L, 3L }).endsWith(3L, 4L);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF endsWith(long... sequence) {
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
  public SELF isSortedAccordingTo(Comparator<? super Long> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingElementComparator(Comparator<? super Long> customComparator) {
    this.arrays = new LongArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    this.arrays = LongArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> long[] longs = { 1, 2, 3 };
   * 
   * // assertion will pass
   * assertThat(longs).containsExactly(1, 2, 3);
   * 
   * // assertion will fail as actual and expected order differ
   * assertThat(longs).containsExactly(2, 1, 3);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   */
  public SELF containsExactly(long... values) {
    arrays.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new long[] { 1L, 2L }).containsExactlyInAnyOrder(1L, 2L);
   * assertThat(new long[] { 1L, 2L, 1L }).containsExactlyInAnyOrder(1L, 1L, 2L);
   * 
   * // assertions will fail
   * assertThat(new long[] { 1L, 2L }).containsExactlyInAnyOrder(1L);
   * assertThat(new long[] { 1L }).containsExactlyInAnyOrder(1L, 2L);
   * assertThat(new long[] { 1L, 2L, 1L }).containsExactlyInAnyOrder(1L, 2L);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactlyInAnyOrder(long... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> long[] oneTwoThree = { 1L, 2L, 3L }; 
   *
   * // assertions will pass
   * assertThat(oneTwoThree).containsAnyOf(2L)
   *                        .containsAnyOf(2L, 3L)
   *                        .containsAnyOf(1L, 2L, 3L)
   *                        .containsAnyOf(1L, 2L, 3L, 4L)
   *                        .containsAnyOf(5L, 6L, 7L, 2L);
   *
   * // assertions will fail
   * assertThat(oneTwoThree).containsAnyOf(4L);
   * assertThat(oneTwoThree).containsAnyOf(4L, 5L, 6L, 7L);</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  public SELF containsAnyOf(long... values) {
    arrays.assertContainsAnyOf(info, actual, values);
    return myself;
  }

}
