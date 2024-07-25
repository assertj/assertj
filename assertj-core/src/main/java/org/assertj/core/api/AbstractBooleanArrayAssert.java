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
import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractBooleanArrayAssert<SELF extends AbstractBooleanArrayAssert<SELF>>
    extends AbstractArrayAssert<SELF, boolean[], Boolean> {

  @VisibleForTesting
  protected BooleanArrays arrays = BooleanArrays.instance();

  protected AbstractBooleanArrayAssert(boolean[] actual, Class<?> selfType) {
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
   * assertThat(new boolean[] { true, false }).hasSize(2);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true }).hasSize(2);</code></pre>
   */
  @Override
  public SELF hasSize(int expected) {
    arrays.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual group is greater than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).hasSizeGreaterThan(1);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true }).hasSizeGreaterThan(1);</code></pre>
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
   * assertThat(new boolean[] { true, false }).hasSizeGreaterThanOrEqualTo(1)
   *                                          .hasSizeGreaterThanOrEqualTo(2);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true }).hasSizeGreaterThanOrEqualTo(2);</code></pre>
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
   * Verifies that the number of values in the actual group is less than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).hasSizeLessThan(3);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).hasSizeLessThan(1);</code></pre>
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
   * Verifies that the number of values in the actual group is less than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).hasSizeLessThanOrEqualTo(3)
   *                                          .hasSizeLessThanOrEqualTo(2);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).hasSizeLessThanOrEqualTo(1);</code></pre>
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
   * Verifies that the number of values in the actual group is between the given boundaries (inclusive).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).hasSizeBetween(1, 3)
   *                                          .hasSizeBetween(2, 2);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).hasSizeBetween(4, 5);</code></pre>
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
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).hasSameSizeAs(Arrays.asList(1, 2));
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).hasSameSizeAs(Arrays.asList(1, 2, 3));</code></pre>
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
   * assertThat(new boolean[] { true, false }).contains(true, false);
   * assertThat(new boolean[] { false, true }).contains(true, false);
   * assertThat(new boolean[] { true, false }).contains(true);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, true }).contains(false);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  public SELF contains(boolean... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the values of the given array, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).contains(new Boolean[] { true, false });
   * assertThat(new boolean[] { false, true }).contains(new Boolean[] { true, false });
   * assertThat(new boolean[] { true, false }).contains(new Boolean[] { true });
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, true }).contains(new Boolean[] { false });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   * @since 3.19.0
   */
  public SELF contains(Boolean[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContains(info, actual, toPrimitiveBooleanArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new boolean[] { true, false }).containsOnly(true, false);
   * assertThat(new boolean[] { false, true }).containsOnly(true, false);
   * assertThat(new boolean[] { true, true, false }).containsOnly(true, false);
   *
   * // assertions will fail
   * assertThat(new boolean[] { true, false }).containsOnly(false);
   * assertThat(new boolean[] { true }).containsOnly(true, false);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public SELF containsOnly(boolean... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the values of the given array and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new boolean[] { true, false }).containsOnly(new Boolean[] { true, false });
   * assertThat(new boolean[] { false, true }).containsOnly(new Boolean[] { true, false });
   * assertThat(new boolean[] { true, true, false }).containsOnly(new Boolean[] { true, false });
   *
   * // assertions will fail
   * assertThat(new boolean[] { true, false }).containsOnly(new Boolean[] { false });
   * assertThat(new boolean[] { true }).containsOnly(new Boolean[] { true, false });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public SELF containsOnly(Boolean[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsOnly(info, actual, toPrimitiveBooleanArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).containsOnlyOnce(true, false);
   *
   * // assertions will fail
   * assertThat(new boolean[] { true, false, true }).containsOnlyOnce(true);
   * assertThat(new boolean[] { true }).containsOnlyOnce(false);
   * assertThat(new boolean[] { true }).containsOnlyOnce(true, false);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   */
  public SELF containsOnlyOnce(boolean... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the values of the given array only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).containsOnlyOnce(new Boolean[] { true, false });
   *
   * // assertions will fail
   * assertThat(new boolean[] { true, false, true }).containsOnlyOnce(new Boolean[] { true });
   * assertThat(new boolean[] { true }).containsOnlyOnce(new Boolean[] { false });
   * assertThat(new boolean[] { true }).containsOnlyOnce(new Boolean[] { true, false });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   */
  public SELF containsOnlyOnce(Boolean[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsOnlyOnce(info, actual, toPrimitiveBooleanArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).containsSequence(true, false);
   * assertThat(new boolean[] { true, false, false, true }).containsSequence(false, true);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, true, false }).containsSequence(false, true);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(boolean... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).containsSequence(new Boolean[] { true, false });
   * assertThat(new boolean[] { true, false, false, true }).containsSequence(new Boolean[] { false, true });
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, true, false }).containsSequence(new Boolean[] { false, true });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(Boolean[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertContainsSequence(info, actual, toPrimitiveBooleanArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).containsSubsequence(true, false);
   * assertThat(new boolean[] { true, false, false, true }).containsSubsequence(true, true);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, true, false }).containsSubsequence(false, true);</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(boolean... subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).containsSubsequence(new Boolean[] { true, false });
   * assertThat(new boolean[] { true, false, false, true }).containsSubsequence(new Boolean[] { true, true });
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, true, false }).containsSubsequence(new Boolean[] { false, true });</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(Boolean[] subsequence) {
    requireNonNullParameter(subsequence, "subsequence");
    arrays.assertContainsSubsequence(info, actual, toPrimitiveBooleanArray(subsequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).contains(true, atIndex(O));
   * assertThat(new boolean[] { true, false }).contains(false, atIndex(1));
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).contains(false, atIndex(0));
   * assertThat(new boolean[] { true, false }).contains(true, atIndex(1));</code></pre>
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
  public SELF contains(boolean value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, true }).doesNotContain(false);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, true, false }).doesNotContain(false);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public SELF doesNotContain(boolean... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the values of the given array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, true }).doesNotContain(new Boolean[] { false });
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, true, false }).doesNotContain(new Boolean[] { false });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public SELF doesNotContain(Boolean[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertDoesNotContain(info, actual, toPrimitiveBooleanArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).doesNotContain(true, atIndex(1));
   * assertThat(new boolean[] { true, false }).doesNotContain(false, atIndex(0));
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false }).doesNotContain(false, atIndex(1));
   * assertThat(new boolean[] { true, false }).doesNotContain(true, atIndex(0));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public SELF doesNotContain(boolean value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain duplicates.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false }).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, true, false }).doesNotHaveDuplicates();</code></pre>
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
   * Similar to <code>{@link #containsSequence(boolean...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false, false, true }).startsWith(true, false);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false, false, true }).startsWith(false, false, true);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   */
  public SELF startsWith(boolean... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(boolean...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false, false, true }).startsWith(new Boolean[] { true, false });
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false, false, true }).startsWith(new Boolean[] { false, false, true });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   */
  public SELF startsWith(Boolean[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertStartsWith(info, actual, toPrimitiveBooleanArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(boolean...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false, false, true }).endsWith(false, false, true);
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false, false, true }).endsWith(true, false);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF endsWith(boolean... sequence) {
    arrays.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(boolean...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false, false, true }).endsWith(new Boolean[] { false, false, true });
   *
   * // assertion will fail
   * assertThat(new boolean[] { true, false, false, true }).endsWith(new Boolean[] { true, false });</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF endsWith(Boolean[] sequence) {
    requireNonNullParameter(sequence, "sequence");
    arrays.assertEndsWith(info, actual, toPrimitiveBooleanArray(sequence));
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
  public SELF isSortedAccordingTo(Comparator<? super Boolean> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /**
   * Do not use this method.
   *
   * @throws UnsupportedOperationException if this method is called.
   *
   * @deprecated Custom element Comparator is not supported for Boolean array comparison.
   */
  @Override
  @Deprecated
  public final SELF usingElementComparator(Comparator<? super Boolean> customComparator) {
    throw new UnsupportedOperationException("custom element Comparator is not supported for Boolean array comparison");
  }

  /**
   * Do not use this method.
   *
   * @throws UnsupportedOperationException if this method is called.
   *
   * @deprecated Custom element Comparator is not supported for Boolean array comparison.
   */
  @Override
  @Deprecated
  public final SELF usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for Boolean array comparison");
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false, true }).containsExactly(true, false, true);
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(new boolean[] { true, false, true }).containsExactly(false, true, true);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   */
  public SELF containsExactly(boolean... values) {
    arrays.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains only the values of the given array and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new boolean[] { true, false, true }).containsExactly(new Boolean[] { true, false, true });
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(new boolean[] { true, false, true }).containsExactly(new Boolean[] { false, true, true });</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   * @since 3.19.0
   */
  public SELF containsExactly(Boolean[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsExactly(info, actual, toPrimitiveBooleanArray(values));
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new boolean[] { true, false }).containsExactlyInAnyOrder(false, true);
   * assertThat(new boolean[] { true, false, true }).containsExactlyInAnyOrder(true, true, false);
   *
   * // assertions will fail
   * assertThat(new boolean[] { true, false }).containsExactlyInAnyOrder(true);
   * assertThat(new boolean[] { true }).containsExactlyInAnyOrder(false, true);
   * assertThat(new boolean[] { true, true, false }).containsExactlyInAnyOrder(false, true);</code></pre>
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
  public SELF containsExactlyInAnyOrder(boolean... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the values of the given array and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new boolean[] { true, false }).containsExactlyInAnyOrder(new Boolean[] { false, true });
   * assertThat(new boolean[] { true, false, true }).containsExactlyInAnyOrder(new Boolean[] { true, true, false });
   *
   * // assertions will fail
   * assertThat(new boolean[] { true, false }).containsExactlyInAnyOrder(new Boolean[] { true });
   * assertThat(new boolean[] { true }).containsExactlyInAnyOrder(new Boolean[] { false, true });
   * assertThat(new boolean[] { true, true, false }).containsExactlyInAnyOrder(new Boolean[] { false, true });</code></pre>
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
  public SELF containsExactlyInAnyOrder(Boolean[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsExactlyInAnyOrder(info, actual, toPrimitiveBooleanArray(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> boolean[] soTrue = { true, true, true };
   *
   * // assertions will pass
   * assertThat(soTrue).containsAnyOf(true)
   *                   .containsAnyOf(false, false, false, true);
   *
   * // assertions will fail
   * assertThat(oneTwoThree).containsAnyOf(false);
   * assertThat(oneTwoThree).containsAnyOf(false, false, false);</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws AssertionError if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  public SELF containsAnyOf(boolean... values) {
    arrays.assertContainsAnyOf(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the values of the given array.
   * <p>
   * Example :
   * <pre><code class='java'> boolean[] soTrue = { true, true, true };
   *
   * // assertions will pass
   * assertThat(soTrue).containsAnyOf(new Boolean[] { true })
   *                   .containsAnyOf(new Boolean[] { false, false, false, true });
   *
   * // assertions will fail
   * assertThat(oneTwoThree).containsAnyOf(new Boolean[] { false });
   * assertThat(oneTwoThree).containsAnyOf(new Boolean[] { false, false, false });</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws AssertionError if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 3.19.0
   */
  public SELF containsAnyOf(Boolean[] values) {
    requireNonNullParameter(values, "values");
    arrays.assertContainsAnyOf(info, actual, toPrimitiveBooleanArray(values));
    return myself;
  }

  private static boolean[] toPrimitiveBooleanArray(Boolean[] values) {
    boolean[] booleans = new boolean[values.length];
    range(0, values.length).forEach(i -> booleans[i] = values[i]);
    return booleans;
  }

}
