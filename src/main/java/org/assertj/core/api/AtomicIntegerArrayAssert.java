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

import static org.assertj.core.util.Arrays.array;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicIntegerArray;

import org.assertj.core.data.Index;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.IntArrays;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public class AtomicIntegerArrayAssert
    extends AbstractEnumerableAssert<AtomicIntegerArrayAssert, AtomicIntegerArray, Integer> {

  @VisibleForTesting
  protected IntArrays arrays = IntArrays.instance();

  private int[] array;

  public AtomicIntegerArrayAssert(AtomicIntegerArray actual) {
    super(actual, AtomicIntegerArrayAssert.class);
    array = array(actual);
  }

  /**
   * Verifies that the AtomicIntegerArray is {@code null} or empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicIntegerArray(new int[0])).isNullOrEmpty();
   * AtomicIntegerArray array = null;
   * assertThat(array).isNullOrEmpty();
   * 
   * // assertion will fail
   * assertThat(new AtomicIntegerArray(new int[] { 1, 2, 3 })).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the AtomicIntegerArray is not {@code null} or not empty.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public void isNullOrEmpty() {
    if (actual == null) return;
    isEmpty();
  }

  /**
   * Verifies that the AtomicIntegerArray is empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicIntegerArray(new int[0])).isEmpty();
   * 
   * // assertion will fail
   * assertThat(new AtomicIntegerArray(new int[] { 1, 2, 3 })).isEmpty();</code></pre>
   *
   * @throws AssertionError if the AtomicIntegerArray is not empty.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public void isEmpty() {
    arrays.assertEmpty(info, array);
  }

  /**
   * Verifies that the AtomicIntegerArray is not empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicIntegerArray(new int[] { 1, 2, 3 })).isNotEmpty();
   * 
   * // assertion will fail
   * assertThat(new AtomicIntegerArray(new int[0])).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the AtomicIntegerArray is empty.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicIntegerArrayAssert isNotEmpty() {
    arrays.assertNotEmpty(info, array);
    return myself;
  }

  /**
   * Verifies that the AtomicIntegerArray has the given array.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertion will pass
   * assertThat(atomicIntegerArray).hasArray(new int[] { 1, 2, 3 });
   * 
   * // assertion will fail
   * assertThat(atomicIntegerArray).hasArray(new int[] { 2, 3, 4 });</code></pre>
   *
   * @param expected the int[] array expected to be in the actual AtomicIntegerArray.
   * @return {@code this} assertion object.
   * @throws AssertionError if the AtomicIntegerArray does not have the given array.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert hasArray(int[] expected) {
    arrays.assertContainsExactly(info, array, expected);
    return myself;
  }
  
  /**
   * Verifies that the number of values in the AtomicIntegerArray is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * assertThat(atomicIntegerArray).hasSize(3);
   * 
   * // assertion will fail
   * assertThat(atomicIntegerArray).hasSize(1);</code></pre>
   *
   * @param expected the expected number of values in the actual AtomicIntegerArray.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the AtomicIntegerArray is not equal to the given one.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicIntegerArrayAssert hasSize(int expected) {
    arrays.assertHasSize(info, array, expected);
    return myself;
  }

  /**
   * Verifies that the AtomicIntegerArray has the same size as given {@link Iterable}.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertion will pass
   * assertThat(atomicIntegerArray).hasSameSizeAs(abc);
   * 
   * // assertions will fail
   * assertThat(atomicIntegerArray).hasSameSizeAs(Arrays.asList(1, 2));
   * assertThat(atomicIntegerArray).hasSameSizeAs(Arrays.asList(1, 2, 3, 4));</code></pre>
   * 
   * @param other the {@code Iterable} to compare size with actual AtomicIntegerArray.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual AtomicIntegerArray is {@code null}.
   * @throws AssertionError if the other {@code Iterable} is {@code null}.
   * @throws AssertionError if actual AtomicIntegerArray and given {@code Iterable} don't have the same size.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicIntegerArrayAssert hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, array, other);
    return myself;
  }

  /**
   * Verifies that the actual {@link AtomicIntegerArray} contains the given values, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertions will pass
   * assertThat(atomicIntegerArray).contains(1, 2)
   *                               .contains(3, 1)
   *                               .contains(1, 3, 2);
   *
   * // assertions will fail
   * assertThat(atomicIntegerArray).contains(2, 3, 4);
   * assertThat(atomicIntegerArray).contains(4, 5, 6);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array does not contain the given values.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert contains(int... values) {
    arrays.assertContains(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual atomic array contains <b>only</b> the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertions will pass
   * assertThat(atomicIntegerArray).containsOnly(1, 2, 3)
   *                               .containsOnly(2, 3, 1);
   *
   * // assertions will fail
   * assertThat(atomicIntegerArray).containsOnly(1, 2, 3, 4);
   * assertThat(atomicIntegerArray).containsOnly(4, 7);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array does not contain the given values, i.e. it contains a subset of
   *           of the given values, or more values than the given ones.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert containsOnly(int... values) {
    arrays.assertContainsOnly(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual atomic array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertion will pass
   * assertThat(atomicIntegerArray).containsOnlyOnce(1, 2);
   * 
   * // assertions will fail
   * assertThat(atomicIntegerArray).containsOnlyOnce(4);
   * assertThat(new AtomicIntegerArray(new int[] { 1, 2, 1 })).containsOnlyOnce(1);
   * assertThat(new AtomicIntegerArray(new int[] { 1, 2, 3, 3 })).containsOnlyOnce(1, 2, 3);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual AtomicIntegerArray does not contain the given values or contains them more than once.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert containsOnlyOnce(int... values) {
    arrays.assertContainsOnlyOnce(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual atomic array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertion will pass
   * assertThat(atomicIntegerArray).containsSequence(1, 2);
   * 
   * // assertion will fail
   * assertThat(atomicIntegerArray).containsSequence(1, 3);
   * assertThat(atomicIntegerArray).containsSequence(2, 1);</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual atomic array does not contain the given sequence.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert containsSequence(int... sequence) {
    arrays.assertContainsSequence(info, array, sequence);
    return myself;
  }

  /**
   * Verifies that the actual atomic array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertions will pass
   * assertThat(atomicIntegerArray).containsSubsequence(1, 2)
   *                               .containsSubsequence(1, 3);
   * 
   * // assertion will fail
   * assertThat(atomicIntegerArray).containsSubsequence(2, 1);</code></pre>
   * 
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual atomic array does not contain the given subsequence.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert containsSubsequence(int... subsequence) {
    arrays.assertContainsSubsequence(info, array, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual atomic array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertions will pass
   * assertThat(atomicIntegerArray).contains(1, atIndex(O))
   *                               .contains(3, atIndex(2));
   *
   * // assertions will fail
   * assertThat(atomicIntegerArray).contains(1, atIndex(1));
   * assertThat(atomicIntegerArray).contains(4, atIndex(2));</code></pre>
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
  public AtomicIntegerArrayAssert contains(int value, Index index) {
    arrays.assertContains(info, array, value, index);
    return myself;
  }

  /**
   * Verifies that the actual atomic array does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertion will pass
   * assertThat(atomicIntegerArray).doesNotContain(4);
   *
   * // assertion will fail
   * assertThat(atomicIntegerArray).doesNotContain(2);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array contains any of the given values.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert doesNotContain(int... values) {
    arrays.assertDoesNotContain(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual atomic array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertions will pass
   * assertThat(atomicIntegerArray).doesNotContain(1, atIndex(1))
   *                               .doesNotContain(2, atIndex(0));
   *
   * // assertions will fail
   * assertThat(atomicIntegerArray).doesNotContain(1, atIndex(0));
   * assertThat(atomicIntegerArray).doesNotContain(2, atIndex(1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual atomic array.
   * @return myself assertion object.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual atomic array contains the given value at the given index.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert doesNotContain(int value, Index index) {
    arrays.assertDoesNotContain(info, array, value, index);
    return myself;
  }

  /**
   * Verifies that the actual atomic array does not contain duplicates.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertion will pass
   * assertThat(atomicIntegerArray).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(new AtomicIntegerArray(new int[] { 1, 1, 2, 3 })).doesNotHaveDuplicates();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array contains duplicates.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert doesNotHaveDuplicates() {
    arrays.assertDoesNotHaveDuplicates(info, array);
    return myself;
  }

  /**
   * Verifies that the actual atomic array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(int...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual atomic array.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertion will pass
   * assertThat(atomicIntegerArray).startsWith(1, 2);
   *
   * // assertion will fail
   * assertThat(atomicIntegerArray).startsWith(2, 3);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array does not start with the given sequence.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert startsWith(int... sequence) {
    arrays.assertStartsWith(info, array, sequence);
    return myself;
  }

  /**
   * Verifies that the actual atomic array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(int...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual atomic array.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertion will pass
   * assertThat(atomicIntegerArray).endsWith(2, 3);
   *
   * // assertion will fail
   * assertThat(atomicIntegerArray).endsWith(3, 4);</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual atomic array is {@code null}.
   * @throws AssertionError if the actual atomic array does not end with the given sequence.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert endsWith(int... sequence) {
    arrays.assertEndsWith(info, array, sequence);
    return myself;
  }

  /**
   * Verifies that the actual AtomicIntegerArray is sorted in ascending order according to the natural ordering of its elements.
   * <p>
   * Empty or one element arrays are considered sorted (unless the array element type is not Comparable).<br><br>
   * 
   * @return {@code this} assertion object.
   * 
   * @throws AssertionError if the actual AtomicIntegerArray is not sorted in ascending order according to the natural ordering of its
   *           elements.
   * @throws AssertionError if the actual AtomicIntegerArray is {@code null}.
   */
  public AtomicIntegerArrayAssert isSorted() {
    arrays.assertIsSorted(info, array);
    return myself;
  }

  /**
   * Verifies that the actual AtomicIntegerArray is sorted according to the given comparator.<br> Empty arrays are considered sorted whatever
   * the comparator is.<br> One element arrays are considered sorted if the element is compatible with comparator, otherwise an
   * AssertionError is thrown.
   * 
   * @param comparator the {@link Comparator} used to compare array elements
   * 
   * @return {@code this} assertion object.
   * 
   * @throws AssertionError if the actual AtomicIntegerArray is not sorted according to the given comparator.
   * @throws AssertionError if the actual AtomicIntegerArray is {@code null}.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  public AtomicIntegerArrayAssert isSortedAccordingTo(Comparator<? super Integer> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, array, comparator);
    return myself;
  }

  /**
   * Use given custom comparator instead of relying on Integer <code>equals</code> method to compare elements for incoming assertion checks.
   * <p>
   * Custom comparator is bound to the current assertion instance, meaning that if a new assertion is created, it will use default
   * comparison strategy.
   * <p>
   * Examples :
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // absolute value comparator 
   * Comparator&lt;Integer&gt; absComparator = ...; 
   * assertThat(invoiceList).usingComparator(absComparator).contains(-1, -2, 3);</code></pre>
   * 
   * @param customComparator the comparator to use for incoming assertion checks.
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
   */
  @Override
  @CheckReturnValue
  public AtomicIntegerArrayAssert usingElementComparator(Comparator<? super Integer> customComparator) {
    this.arrays = new IntArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public AtomicIntegerArrayAssert usingDefaultElementComparator() {
    this.arrays = IntArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual AtomicIntegerArray contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] { 1, 2, 3 });
   * 
   * // assertion will pass
   * assertThat(atomicIntegerArray).containsExactly(1, 2, 3);
   * 
   * // assertion will fail as actual and expected order differ
   * assertThat(atomicIntegerArray).containsExactly(2, 1, 3);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual AtomicIntegerArray is {@code null}.
   * @throws AssertionError if the actual AtomicIntegerArray does not contain the given values with same order, i.e. it
   *           contains some or none of the given values, or more values than the given ones or values are the same but the order is not.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert containsExactly(int... values) {
    arrays.assertContainsExactly(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual AtomicIntegerArray contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicIntegerArray(new int[] { 1, 2 })).containsExactlyInAnyOrder(1, 2);
   * assertThat(new AtomicIntegerArray(new int[] { 1, 2, 1 })).containsExactlyInAnyOrder(1, 1, 2);
   * 
   * // assertions will fail
   * assertThat(new AtomicIntegerArray(new int[] { 1, 2 })).containsExactlyInAnyOrder(1);
   * assertThat(new AtomicIntegerArray(new int[] { 1 })).containsExactlyInAnyOrder(1, 2);
   * assertThat(new AtomicIntegerArray(new int[] { 1, 2, 1 })).containsExactlyInAnyOrder(1, 2);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual AtomicIntegerArray is {@code null}.
   * @throws AssertionError if the actual AtomicIntegerArray does not contain the given values, i.e. it
   *           contains some or none of the given values, or more values than the given ones.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerArrayAssert containsExactlyInAnyOrder(int... values) {
    arrays.assertContainsExactlyInAnyOrder(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicIntegerArray oneTwoThree = new AtomicIntegerArray(new int[] { 1, 2, 3 }); 
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
  public AtomicIntegerArrayAssert containsAnyOf(int... values) {
    arrays.assertContainsAnyOf(info, array, values);
    return myself;
  }

}