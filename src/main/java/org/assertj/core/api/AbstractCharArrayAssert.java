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
import org.assertj.core.internal.CharArrays;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractCharArrayAssert<SELF extends AbstractCharArrayAssert<SELF>>
  extends AbstractArrayAssert<SELF, char[], Character> {

  @VisibleForTesting
  protected CharArrays arrays = CharArrays.instance();

  public AbstractCharArrayAssert(char[] actual, Class<?> selfType) {
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
   * assertThat(new char[] { 'a', 'b', 'c' }).hasSize(3);
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c', 'd' }).hasSize(3);</code></pre>
   * 
   */
  @Override
  public SELF hasSize(int expected) {
    arrays.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual group has the same size as given {@link Iterable}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b' }).hasSameSizeAs(Arrays.asList(1, 2));
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b' }).hasSameSizeAs(Arrays.asList(1, 2, 3));</code></pre>
   * 
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
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('a', 'b');
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('c', 'a');
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('a', 'c', 'b');
   *
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('a', 'd');
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('d', 'f');</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  public SELF contains(char... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).containsOnly('a', 'b', 'c');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsOnly('b', 'c', 'a');
   *
   * // assertion will fail
   * * assertThat(new char[] { 'a', 'b', 'c' }).containsOnly('a', 'b', 'c', 'd');
   * * assertThat(new char[] { 'a', 'b', 'c' }).containsOnly('d', 'f');</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public SELF containsOnly(char... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).containsOnlyOnce('a', 'b');
   * 
   * // assertions will fail
   * assertThat(new char[] { 'a', 'b', 'a' }).containsOnlyOnce('a');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsOnlyOnce('d');
   * assertThat(new char[] { 'a', 'b', 'c', 'c' }).containsOnlyOnce('0', 'a', 'b', 'c', 'd', 'e');</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   */
  public SELF containsOnlyOnce(char... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSequence('a', 'b');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSequence('a', 'b', 'c');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSequence('b', 'c');
   *
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSequence('c', 'a');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSequence('d', 'f');</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(char... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSubsequence('a', 'b');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSubsequence('a', 'c');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSubsequence('b', 'c');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSubsequence('a', 'b', 'c');
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSubsequence('c', 'a');
   * assertThat(new char[] { 'a', 'b', 'c' }).containsSubsequence('d', 'f');</code></pre>
   * 
   * @param subsequence the subsequence of values to look for.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(char... subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }
  
  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('a', atIndex(O));
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('c', atIndex(2));
   *
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('a', atIndex(1));
   * assertThat(new char[] { 'a', 'b', 'c' }).contains('d', atIndex(2));</code></pre>
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
  public SELF contains(char value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('d');
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('b');</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public SELF doesNotContain(char... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('a', atIndex(1));
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('b', atIndex(0));
   *
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('a', atIndex(0));
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotContain('b', atIndex(1));</code></pre>
   * 
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return myself assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public SELF doesNotContain(char value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain duplicates.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).doesNotHaveDuplicates();
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'a', 'b', 'c' }).doesNotHaveDuplicates();</code></pre>
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
   * Similar to <code>{@link #containsSequence(char...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).startsWith('a', 'b');
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).startsWith('b', 'c');</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   */
  public SELF startsWith(char... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(char...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).endsWith('b', 'c');
   * 
   * // assertion will fail
   * assertThat(new char[] { 'a', 'b', 'c' }).endsWith('c', 'd');</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return myself assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF endsWith(char... sequence) {
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
  public SELF isSortedAccordingTo(Comparator<? super Character> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingElementComparator(Comparator<? super Character> customComparator) {
    this.arrays = new CharArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    this.arrays = CharArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new char[] { 'a', 'b', 'c' }).containsExactly('a', 'b', 'c');
   * 
   * // assertion will fail as actual and expected order differ
   * assertThat(new char[] { 'a', 'b', 'c' }).containsExactly('b', 'a', 'c');</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   */
  public SELF containsExactly(char... values) {
    arrays.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new char[] { 'a', 'b' }).containsExactlyInAnyOrder('b', 'a');
   * assertThat(new char[] { 'a', 'b', 'a' }).containsExactlyInAnyOrder('a', 'a', 'b');
   * 
   * // assertions will fail
   * assertThat(new char[] { 'a', 'b' }).containsExactlyInAnyOrder('a');
   * assertThat(new char[] { 'a' }).containsExactlyInAnyOrder('a', 'b');
   * assertThat(new char[] { 'a', 'b', 'a' }).containsExactlyInAnyOrder('a', 'b');</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactlyInAnyOrder(char... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, values);
    return myself;
  }

  /**
   * Use unicode character representation instead of standard representation in error messages.
   * <p>
   * With standard error message:
   * <pre><code class='java'> assertThat("a6c".toCharArray()).isEqualTo("abó".toCharArray());
   *
   * org.junit.ComparisonFailure:
   * Expected :['a', 'b', 'ó']
   * Actual   :[a, 6, c]</code></pre>
   *
   * With unicode based error message:
   * <pre><code class='java'> assertThat("a6c".toCharArray()).inUnicode().isEqualTo("abó".toCharArray());
   *
   * org.junit.ComparisonFailure:
   * Expected :[a, b, \u00f3]
   * Actual   :[a, 6, c]</code></pre>
   *
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public SELF inUnicode() {
    info.useUnicodeRepresentation();
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> char[] abc = { 'a', 'b', 'c' }; 
   *
   * // assertions will pass
   * assertThat(abc).containsAnyOf('b')
   *                .containsAnyOf('b', 'c')
   *                .containsAnyOf('a', 'b', 'c')
   *                .containsAnyOf('a', 'b', 'c', 'd')
   *                .containsAnyOf('e', 'f', 'g', 'b');
   *
   * // assertions will fail
   * assertThat(abc).containsAnyOf('d');
   * assertThat(abc).containsAnyOf('d', 'e', 'f', 'g');</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  public SELF containsAnyOf(char... values) {
    arrays.assertContainsAnyOf(info, actual, values);
    return myself;
  }

}
