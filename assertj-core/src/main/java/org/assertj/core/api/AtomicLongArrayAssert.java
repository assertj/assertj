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

import static org.assertj.core.util.Arrays.array;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLongArray;

import org.assertj.core.data.Index;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.LongArrays;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public class AtomicLongArrayAssert
    extends AbstractEnumerableAssert<AtomicLongArrayAssert, AtomicLongArray, Long> {

  @VisibleForTesting
  protected LongArrays arrays = LongArrays.instance();

  private long[] array;

  public AtomicLongArrayAssert(AtomicLongArray actual) {
    super(actual, AtomicLongArrayAssert.class);
    array = array(actual);
  }

  /**
   * Verifies that the AtomicLongArray is {@code null} or empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicLongArray(new long[0])).isNullOrEmpty();
   * AtomicLongArray array = null;
   * assertThat(array).isNullOrEmpty();
   *
   * // assertion will fail
   * assertThat(new AtomicLongArray(new long[] { 1, 2, 3 })).isNullOrEmpty();</code></pre>
  
   * @throws AssertionError if the AtomicLongArray is not {@code null} or not empty.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public void isNullOrEmpty() {
    if (actual == null) return;
    isEmpty();
  }

  /**
   * Verifies that the AtomicLongArray is empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicLongArray(new long[0])).isEmpty();
   *
   * // assertion will fail
   * assertThat(new AtomicLongArray(new long[] { 1, 2, 3 })).isEmpty();</code></pre>
  
   * @throws AssertionError if the AtomicLongArray is not empty.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public void isEmpty() {
    arrays.assertEmpty(info, array);
  }

  /**
   * Verifies that the AtomicLongArray is not empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicLongArray(new long[] { 1, 2, 3 })).isNotEmpty();
   *
   * // assertion will fail
   * assertThat(new AtomicLongArray(new long[0])).isNotEmpty();</code></pre>
  
   * @return {@code this} assertion object.
   * @throws AssertionError if the AtomicLongArray is empty.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicLongArrayAssert isNotEmpty() {
    arrays.assertNotEmpty(info, array);
    return myself;
  }

  /**
   * Verifies that the AtomicLongArray has the given array.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).hasArray(new long[] { 1, 2, 3 });
   *
   * // assertion will fail
   * assertThat(atomicLongArray).hasArray(new long[] { 2, 3, 4 });</code></pre>
  
   * @param expected the long[] array expected to be in the actual AtomicLongArray.
   * @return {@code this} assertion object.
   * @throws AssertionError if the AtomicLongArray does not have the given array.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert hasArray(long[] expected) {
    arrays.assertContainsExactly(info, array, expected);
    return myself;
  }

  /**
   * Verifies that the number of values in the AtomicLongArray is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * assertThat(atomicLongArray).hasSize(3);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).hasSize(1);</code></pre>
  
   * @param expected the expected number of values in the actual AtomicLongArray.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the AtomicLongArray is not equal to the given one.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicLongArrayAssert hasSize(int expected) {
    arrays.assertHasSize(info, array, expected);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is greater than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).hasSizeGreaterThan(1);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).hasSizeGreaterThan(3);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not greater than the boundary.
   * @since 3.12.0
   */
  @Override
  public AtomicLongArrayAssert hasSizeGreaterThan(int boundary) {
    arrays.assertHasSizeGreaterThan(info, array, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is greater than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).hasSizeGreaterThanOrEqualTo(3);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).hasSizeGreaterThanOrEqualTo(5);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not greater than or equal to the boundary.
   * @since 3.12.0
   */
  @Override
  public AtomicLongArrayAssert hasSizeGreaterThanOrEqualTo(int boundary) {
    arrays.assertHasSizeGreaterThanOrEqualTo(info, array, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is less than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).hasSizeLessThan(4);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).hasSizeLessThan(2);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not less than the boundary.
   * @since 3.12.0
   */
  @Override
  public AtomicLongArrayAssert hasSizeLessThan(int boundary) {
    arrays.assertHasSizeLessThan(info, array, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is less than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).hasSizeLessThanOrEqualTo(3);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).hasSizeLessThanOrEqualTo(2);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not less than or equal to the boundary.
   * @since 3.12.0
   */
  @Override
  public AtomicLongArrayAssert hasSizeLessThanOrEqualTo(int boundary) {
    arrays.assertHasSizeLessThanOrEqualTo(info, array, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is between the given boundaries (inclusive).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).hasSizeBetween(3, 4);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).hasSizeBetween(4, 6);</code></pre>
   *
   * @param lowerBoundary the lower boundary compared to which actual size should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual size should be less than or equal to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not between the boundaries.
   * @since 3.12.0
   */
  @Override
  public AtomicLongArrayAssert hasSizeBetween(int lowerBoundary, int higherBoundary) {
    arrays.assertHasSizeBetween(info, array, lowerBoundary, higherBoundary);
    return myself;
  }

  /**
   * Verifies that the AtomicLongArray has the same size as given {@link Iterable}.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).hasSameSizeAs(abc);
   *
   * // assertions will fail
   * assertThat(atomicLongArray).hasSameSizeAs(Arrays.asList(1, 2));
   * assertThat(atomicLongArray).hasSameSizeAs(Arrays.asList(1, 2, 3, 4));</code></pre>
   *
   * @param other the {@code Iterable} to compare size with actual AtomicLongArray.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual AtomicLongArray is {@code null}.
   * @throws AssertionError if the other {@code Iterable} is {@code null}.
   * @throws AssertionError if actual AtomicLongArray and given {@code Iterable} don't have the same size.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicLongArrayAssert hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, array, other);
    return myself;
  }

  /**
   * Verifies that the actual {@link AtomicLongArray} contains the given values, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertions will pass
   * assertThat(atomicLongArray).contains(1, 2)
   *                            .contains(3, 1)
   *                            .contains(1, 3, 2);
   *
   * // assertions will fail
   * assertThat(atomicLongArray).contains(2, 3, 4);
   * assertThat(atomicLongArray).contains(4, 5, 6);</code></pre>
  
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array does not contain the given values.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert contains(long... values) {
    arrays.assertContains(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual atomic array contains <b>only</b> the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertions will pass
   * assertThat(atomicLongArray).containsOnly(1, 2, 3)
   *                            .containsOnly(2, 3, 1);
   * assertThat(new AtomicLongArray(new long[] { 1, 1, 2 })).containsOnly(1, 2);
   *
   * // assertions will fail
   * assertThat(atomicLongArray).containsOnly(1, 2, 3, 4);
   * assertThat(atomicLongArray).containsOnly(4, 7);</code></pre>
  
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array does not contain the given values, i.e. it contains a subset of
   *           of the given values, or more values than the given ones.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert containsOnly(long... values) {
    arrays.assertContainsOnly(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual atomic array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).containsOnlyOnce(1, 2);
   *
   * // assertions will fail
   * assertThat(atomicLongArray).containsOnlyOnce(4);
   * assertThat(new AtomicLongArray(new long[] { 1, 2, 1 })).containsOnlyOnce(1);
   * assertThat(new AtomicLongArray(new long[] { 1, 2, 3, 3 })).containsOnlyOnce(1, 2, 3);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual AtomicLongArray does not contain the given values or contains them more than once.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert containsOnlyOnce(long... values) {
    arrays.assertContainsOnlyOnce(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual atomic array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).containsSequence(1, 2);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).containsSequence(1, 3);
   * assertThat(atomicLongArray).containsSequence(2, 1);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual atomic array does not contain the given sequence.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert containsSequence(long... sequence) {
    arrays.assertContainsSequence(info, array, sequence);
    return myself;
  }

  /**
   * Verifies that the actual atomic array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertions will pass
   * assertThat(atomicLongArray).containsSubsequence(1, 2)
   *                            .containsSubsequence(1, 3);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).containsSubsequence(2, 1);</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual atomic array does not contain the given subsequence.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert containsSubsequence(long... subsequence) {
    arrays.assertContainsSubsequence(info, array, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual atomic array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertions will pass
   * assertThat(atomicLongArray).contains(1, atIndex(O))
   *                            .contains(3, atIndex(2));
   *
   * // assertions will fail
   * assertThat(atomicLongArray).contains(1, atIndex(1));
   * assertThat(atomicLongArray).contains(4, atIndex(2));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual atomic array.
   * @return myself assertion object.
   * @throws AssertionError if the actual atomic array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual atomic array.
   * @throws AssertionError if the actual atomic array does not contain the given value at the given index.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert contains(long value, Index index) {
    arrays.assertContains(info, array, value, index);
    return myself;
  }

  /**
   * Verifies that the actual atomic array does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).doesNotContain(4);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).doesNotContain(2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array contains any of the given values.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert doesNotContain(long... values) {
    arrays.assertDoesNotContain(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual atomic array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertions will pass
   * assertThat(atomicLongArray).doesNotContain(1, atIndex(1))
   *                            .doesNotContain(2, atIndex(0));
   *
   * // assertions will fail
   * assertThat(atomicLongArray).doesNotContain(1, atIndex(0));
   * assertThat(atomicLongArray).doesNotContain(2, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual atomic array.
   * @return myself assertion object.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual atomic array contains the given value at the given index.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert doesNotContain(long value, Index index) {
    arrays.assertDoesNotContain(info, array, value, index);
    return myself;
  }

  /**
   * Verifies that the actual atomic array does not contain duplicates.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(new AtomicLongArray(new long[] { 1, 1, 2, 3 })).doesNotHaveDuplicates();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array contains duplicates.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert doesNotHaveDuplicates() {
    arrays.assertDoesNotHaveDuplicates(info, array);
    return myself;
  }

  /**
   * Verifies that the actual atomic array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(long...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual atomic array.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).startsWith(1, 2);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).startsWith(2, 3);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array does not start with the given sequence.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert startsWith(long... sequence) {
    arrays.assertStartsWith(info, array, sequence);
    return myself;
  }

  /**
   * Verifies that the actual atomic array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(long...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual atomic array.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).endsWith(2, 3);
   *
   * // assertion will fail
   * assertThat(atomicLongArray).endsWith(3, 4);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array does not end with the given sequence.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert endsWith(long... sequence) {
    arrays.assertEndsWith(info, array, sequence);
    return myself;
  }

  /**
   * Verifies that the actual AtomicLongArray is sorted in ascending order according to the natural ordering of its elements.
   * <p>
   * Empty or one element arrays are considered sorted (unless the array element type is not Comparable).<br><br>
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual AtomicLongArray is not sorted in ascending order according to the natural ordering of its
   *           elements.
   * @throws AssertionError if the actual AtomicLongArray is {@code null}.
   */
  public AtomicLongArrayAssert isSorted() {
    arrays.assertIsSorted(info, array);
    return myself;
  }

  /**
   * Verifies that the actual AtomicLongArray is sorted according to the given comparator.<br> Empty arrays are considered sorted whatever
   * the comparator is.<br> One element arrays are considered sorted if the element is compatible with comparator, otherwise an
   * AssertionError is thrown.
   *
   * @param comparator the {@link Comparator} used to compare array elements
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual AtomicLongArray is not sorted according to the given comparator.
   * @throws AssertionError if the actual AtomicLongArray is {@code null}.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  public AtomicLongArrayAssert isSortedAccordingTo(Comparator<? super Long> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, array, comparator);
    return myself;
  }

  /**
   * Use given custom comparator instead of relying on Long <code>equals</code> method to compare elements for incoming assertion checks.
   * <p>
   * Custom comparator is bound to the current assertion instance, meaning that if a new assertion is created, it will use default
   * comparison strategy.
   * <p>
   * Examples :
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // absolute value comparator
   * Comparator&lt;Long&gt; absComparator = ...;
   * assertThat(invoiceList).usingComparator(absComparator).contains(-1, -2, 3);</code></pre>
   *
   * @param customComparator the comparator to use for incoming assertion checks.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  @Override
  @CheckReturnValue
  public AtomicLongArrayAssert usingElementComparator(Comparator<? super Long> customComparator) {
    this.arrays = new LongArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public AtomicLongArrayAssert usingDefaultElementComparator() {
    this.arrays = LongArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual AtomicLongArray contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicLongArray atomicLongArray = new AtomicLongArray(new long[] { 1, 2, 3 });
   *
   * // assertion will pass
   * assertThat(atomicLongArray).containsExactly(1, 2, 3);
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(atomicLongArray).containsExactly(2, 1, 3);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual AtomicLongArray is {@code null}.
   * @throws AssertionError if the actual AtomicLongArray does not contain the given values with same order, i.e. it
   *           contains some or none of the given values, or more values than the given ones or values are the same but the order is not.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert containsExactly(long... values) {
    arrays.assertContainsExactly(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual AtomicLongArray contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicLongArray(new long[] { 1, 2 })).containsExactlyInAnyOrder(1, 2);
   * assertThat(new AtomicLongArray(new long[] { 1, 2, 1 })).containsExactlyInAnyOrder(1, 1, 2);
   *
   * // assertions will fail
   * assertThat(new AtomicLongArray(new long[] { 1, 2 })).containsExactlyInAnyOrder(1);
   * assertThat(new AtomicLongArray(new long[] { 1 })).containsExactlyInAnyOrder(1, 2);
   * assertThat(new AtomicLongArray(new long[] { 1, 2, 1 })).containsExactlyInAnyOrder(1, 2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual AtomicLongArray is {@code null}.
   * @throws AssertionError if the actual AtomicLongArray does not contain the given values, i.e. it
   *           contains some or none of the given values, or more values than the given ones.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongArrayAssert containsExactlyInAnyOrder(long... values) {
    arrays.assertContainsExactlyInAnyOrder(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicLongArray oneTwoThree = new AtomicLongArray(new long[] { 1L, 2L, 3L });
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
  public AtomicLongArrayAssert containsAnyOf(long... values) {
    arrays.assertContainsAnyOf(info, array, values);
    return myself;
  }

}
