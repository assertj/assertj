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
import org.assertj.core.data.Offset;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.DoubleArrays;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractDoubleArrayAssert<SELF extends AbstractDoubleArrayAssert<SELF>>
    extends AbstractArrayAssert<SELF, double[], Double> {

  @VisibleForTesting
  protected DoubleArrays arrays = DoubleArrays.instance();

  private final ComparatorFactory doubleComparator = ComparatorFactory.INSTANCE;

  public AbstractDoubleArrayAssert(double[] actual, Class<?> selfType) {
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
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).hasSize(3);
   * 
   * // assertion will fail
   * assertThat(new double[] { 1.0, 2.0, 1.0 }).hasSize(2);</code></pre>
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
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).hasSameSizeAs(Arrays.asList(1, 2, 3));
   * 
   * // assertion will fail
   * assertThat(new double[] { 1.0, 2.0, 1.0 }).hasSameSizeAs(Arrays.asList(1, 2));</code></pre>
   */
  @Override
  public SELF hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order.
   * <p>
   * If you want to set a precision for the comparison either use {@link #contains(double[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Examples :
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).contains(1.0, 3.0, 2.0)
   *                   .contains(3.0, 1.0)
   *                   .usingComparatorWithPrecision(0.5)
   *                   .contains(1.1, 2.1);
   * 
   * // assertions will fail
   * assertThat(values).contains(1.0, 4.0);
   * assertThat(values).usingComparatorWithPrecision(0.01)
   *                   .contains(1.1, 2.1);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  public SELF contains(double... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order, 
   * the comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
   * <p>
   * Examples :
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).contains(new double[] {1.01, 3.01, 2.0}, withPrecision(0.02));
   *
   * // assertions will fail
   * assertThat(values).contains(new double[] {1.0, 4.0}, withPrecision(0.5));
   * assertThat(values).contains(new double[] {4.0, 7.0}, withPrecision(2));</code></pre>
   *
   * @param values the given values.
   * @param precision the precision under which the value may vary
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  public SELF contains(double[] values, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return contains(values);
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * If you want to set a precision for the comparison either use {@link #containsOnly(double[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Examples :
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).containsOnly(1.0, 2.0, 3.0)
   *                   .containsOnly(2.0, 3.0, 1.0)
   *                   .usingComparatorWithPrecision(0.5)
   *                   .containsOnly(1.1, 3.1, 2.1);
  
   * // assertions will fail
   * assertThat(values).containsOnly(1.0, 4.0, 2.0, 3.0);
   * assertThat(values).containsOnly(4.0, 7.0);
   * assertThat(values).containsOnly(1.1, 2.1, 3.1);
   * assertThat(values).usingComparatorWithPrecision(0.01)
   *                   .containsOnly(1.1, 2.1, 3.1);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public SELF containsOnly(double... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
   * <p>
   * Examples :
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).containsOnly(new double[] {1.0, 2.0, 3.0}, withPrecision(0.00001))
   *                   .containsOnly(new double[] {2.0, 3.0, 0.7}, withPrecision(0.5));
   *
   * // assertions will fail
   * assertThat(values).containsOnly(new double[] {1.0, 4.0, 2.0, 3.0}, withPrecision(0.5));
   * assertThat(values).containsOnly(new double[] {4.0, 7.0}, withPrecision(0.2));</code></pre>
   *
   * @param values the given values.
   * @param precision the precision under which the value may vary
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public SELF containsOnly(double[] values, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return containsOnly(values);
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * If you want to set a precision for the comparison either use {@link #containsOnlyOnce(double[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsOnlyOnce(1.0, 2.0)
   *                                           .usingComparatorWithPrecision(0.5)
   *                                           .containsOnlyOnce(1.1, 3.1, 2.1);
   * 
   * // assertions will fail
   * assertThat(new double[] { 1.0, 2.0, 1.0 }).containsOnlyOnce(1.0);
   * assertThat(new double[] { 1.0, 2.0, 1.0 }).containsOnlyOnce(1.0, 2.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsOnlyOnce(4.0);
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).usingComparatorWithPrecision(0.05)
   *                                           .containsOnlyOnce(1.1, 2.1);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   */
  public SELF containsOnlyOnce(double... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
  * Verifies that the actual array contains the given values only once.
  * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
  * <p>
  * Examples :
  * <pre><code class='java'> // assertion will pass
  * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsOnlyOnce(new double[] {1.1, 2.0}, withPrecision(0.2));
  *
  * // assertions will fail
  * assertThat(new double[] { 1.0, 2.0, 1.0 }).containsOnlyOnce(new double[] {1.05}, withPrecision(0.1));
  * assertThat(new double[] { 1.0, 2.0, 3.0 }).containsOnlyOnce(new double[] {4.0}, withPrecision(0.1));
  * assertThat(new double[] { 1.0, 2.0, 3.0, 3.0 }).containsOnlyOnce(new double[] {0.1, 0.9, 2.0, 3.11, 4.0, 5.0}, withPrecision(0.2));</code></pre>
  *
  * @param values the given values.
  * @param precision the precision under which the value may vary
  * @return {@code this} assertion object.
  * @throws NullPointerException if the given argument is {@code null}.
  * @throws IllegalArgumentException if the given argument is an empty array.
  * @throws AssertionError if the actual array is {@code null}.
  * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
  *           or none of the given values, or the actual group contains more than once these values.
  */
  public SELF containsOnlyOnce(double[] values, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return containsOnlyOnce(values);
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * If you want to set a precision for the comparison either use {@link #containsSequence(double[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Examples :
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).containsSequence(1.0, 2.0)
   *                   .containsSequence(1.0, 2.0, 3.0)
   *                   .containsSequence(2.0, 3.0)
   *                   .usingComparatorWithPrecision(0.5)
   *                   .containsSequence(1.1, 2.1);
   * 
   * // assertions will fail
   * assertThat(values).containsSequence(1.0, 3.0);
   * assertThat(values).containsSequence(4.0, 7.0);
   * assertThat(values).usingComparatorWithPrecision(0.01)
   *                   .containsSequence(1.1, 2.0, 3.0);</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(double... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
   * <p>
   * Examples :
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).containsSequence(new double[] {1.07, 2.0}, withPrecision(0.1))
   *                   .containsSequence(new double[] {1.1, 2.1, 3.0}, withPrecision(0.2))
   *                   .containsSequence(new double[] {2.2, 3.0}, withPrecision(0.3));
   *
   * // assertions will fail
   * assertThat(values).containsSequence(new double[] {1.0, 3.0}, withPrecision(0.2));
   * assertThat(values).containsSequence(new double[] {4.0, 7.0}, withPrecision(0.1));</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @param precision the precision under which the value may vary
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(double[] sequence, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return containsSequence(sequence);
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * If you want to set a precision for the comparison either use {@link #containsSubsequence(double[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Examples :
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).containsSubsequence(1.0, 2.0);
   *                   .containsSubsequence(1.0, 2.0, 3.0)
   *                   .containsSubsequence(1.0, 3.0)
   *                   .usingComparatorWithPrecision(0.5)
   *                   .containsSubsequence(1.1, 2.1);
   * 
   * // assertions will fail
   * assertThat(values).containsSubsequence(3.0, 1.0);
   * assertThat(values).containsSubsequence(4.0, 7.0);
   * assertThat(values).usingComparatorWithPrecision(0.01)
   *                   .containsSubsequence(1.1, 2.0);</code></pre>
   * 
   * @param subsequence the subsequence of values to look for.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(double... subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
   * <p>
   * Examples :
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).containsSubsequence(new double[] {1.0, 2.0}, withPrecision(0.1))
   *                   .containsSubsequence(new double[] {1.0, 2.07, 3.0}, withPrecision(0.1))
   *                   .containsSubsequence(new double[] {2.1, 2.9}, withPrecision(0.2));
   *
   * // assertions will fail
   * assertThat(values).containsSubsequence(new double[] {1.0, 3.0}, withPrecision(0.1));
   * assertThat(values).containsSubsequence(new double[] {4.0, 7.0}, withPrecision(0.1));</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @param precision the precision under which the value may vary.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(double[] subsequence, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return containsSubsequence(subsequence);
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * If you want to set a precision for the comparison either use {@link #contains(double, Index, Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).contains(1.0, atIndex(O))
   *                   .contains(3.0, atIndex(2))
   *                   .usingComparatorWithPrecision(0.5)
   *                   .contains(3.1, atIndex(2));
   * 
   * // assertions will fail
   * assertThat(values).contains(1.0, atIndex(1));
   * assertThat(values).contains(4.0, atIndex(2));
   * assertThat(values).usingComparatorWithPrecision(0.01)
   *                   .contains(3.1, atIndex(2));</code></pre>
   * 
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual array.
   * @throws AssertionError if the actual array does not contain the given value at the given index.
   */
  public SELF contains(double value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
   * <p>
   * Example:
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).contains(1.0, atIndex(O), withPrecision(0.01))
   *                   .contains(3.3, atIndex(2), withPrecision(0.5));
   *
   * // assertions will fail
   * assertThat(values).contains(1.0, atIndex(1), withPrecision(0.2));
   * assertThat(values).contains(4.5, atIndex(2), withPrecision(0.1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @param precision the precision under which the value may vary.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual array.
   * @throws AssertionError if the actual array does not contain the given value at the given index.
   */
  public SELF contains(double value, Index index, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return contains(value, index);
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * If you want to set a precision for the comparison either use {@link #doesNotContain(double[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).doesNotContain(4.0, 8.0)
   *                   .usingComparatorWithPrecision(0.0001)
   *                   .doesNotContain(1.01, 2.01);
   *                   
   * // assertions will fail
   * assertThat(values).doesNotContain(1.0, 4.0, 8.0);
   * assertThat(values).usingComparatorWithPrecision(0.1)
   *                   .doesNotContain(1.001, 2.001);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public SELF doesNotContain(double... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
   * <p>
   * Example:
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).doesNotContain(new double[] {4.0, 8.0}, withPrecision(0.5));
   *
   * // assertion will fail
   * assertThat(values).doesNotContain(new double[] {1.05, 4.0, 8.0}, withPrecision(0.1));</code></pre>
   *
   * @param values the given values.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public SELF doesNotContain(double[] values, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * If you want to set a precision for the comparison either use {@link #doesNotContain(double, Index, Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).doesNotContain(1.0, atIndex(1))
   *                   .doesNotContain(2.0, atIndex(0))
   *                   .usingComparatorWithPrecision(0.001)
   *                   .doesNotContain(1.1, atIndex(0));
   * 
   * // assertions will fail
   * assertThat(values).doesNotContain(1.0, atIndex(0));
   * assertThat(values).usingComparatorWithPrecision(0.1)
   *                   .doesNotContain(1.001, atIndex(0));</code></pre>
   * 
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public SELF doesNotContain(double value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
   * <p>
   * Example:
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).doesNotContain(1.01, atIndex(1), withPrecision(0.0001))
   *                   .doesNotContain(2.05, atIndex(0), withPrecision(0.1));
   *
   * // assertion will fail
   * assertThat(values).doesNotContain(1.01, atIndex(0), withPrecision(0.1));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public SELF doesNotContain(double value, Index index, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return doesNotContain(value, index);
  }

  /**
   * Verifies that the actual array does not contain duplicates.
   * <p>
   * If you want to set a precision for the comparison either use {@link #doesNotHaveDuplicates(Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).doesNotHaveDuplicates();
   * assertThat(new double[] { 1.0, 1.1 }).usingComparatorWithPrecision(0.01)
   *                                      .doesNotHaveDuplicates();
   * 
   * // assertion will fail
   * assertThat(new double[] { 1.0, 1.0, 2.0, 3.0 }).doesNotHaveDuplicates();
   * assertThat(new double[] { 1.0, 1.1 }).usingComparatorWithPrecision(0.5)
   *                                      .doesNotHaveDuplicates();</code></pre>
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
   * Verifies that the actual array does not contain duplicates.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new double[] { 1.0, 2.0, 3.0 }).doesNotHaveDuplicates(withPrecision(0.1));
   * assertThat(new double[] { 1.1, 1.2, 1.3 }).doesNotHaveDuplicates(withPrecision(0.05));
   * 
   * // assertion will fail
   * assertThat(new double[] { 1.0, 1.01, 2.0 }).doesNotHaveDuplicates(withPrecision(0.1));</code></pre>
   * 
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains duplicates.
   */
  public SELF doesNotHaveDuplicates(Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return doesNotHaveDuplicates();
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(double...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * If you want to set a precision for the comparison either use {@link #startsWith(double[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).startsWith(1.0, 2.0)
   *                   .usingComparatorWithPrecision(0.5)
   *                   .startsWith(1.1, 2.1);
   * 
   * // assertion will fail
   * assertThat(values).startsWith(2.0, 3.0);</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   */
  public SELF startsWith(double... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(double...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
   * <p>
   * Example:
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).startsWith(new double[] {1.01, 2.01}, withPrecision(0.1));
   * 
   * // assertions will fail
   * assertThat(values).startsWith(new double[] {2.0, 1.0}, withPrecision(0.1))
   * assertThat(values).startsWith(new double[] {1.1, 2.1}, withPrecision(0.5))</code></pre>
   * 
   * @param values the sequence of values to look for.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF startsWith(double[] values, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return startsWith(values);
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(double...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * If you want to set a precision for the comparison either use {@link #endsWith(double[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).endsWith(2.0, 3.0)
   *                   .usingComparatorWithPrecision(0.5)
   *                   .endsWith(2.1, 3.1);
   * 
   * // assertion will fail
   * assertThat(values).endsWith(1.0, 3.0);</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF endsWith(double... sequence) {
    arrays.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(double...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Double)}.
   * <p>
   * Example:
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).endsWith(new double[] {2.01, 3.01}, withPrecision(0.1));
   * 
   * // assertion will fail
   * assertThat(values).endsWith(new double[] {3.0, 2.0}, withPrecision(0.1))
   * assertThat(values).endsWith(new double[] {2.1, 3.1}, withPrecision(0.5))</code></pre>
   * 
   * @param values the sequence of values to look for.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF endsWith(double[] values, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return endsWith(values);
  }

  /** {@inheritDoc} */
  @Override
  public SELF isSorted() {
    arrays.assertIsSorted(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isSortedAccordingTo(Comparator<? super Double> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingElementComparator(Comparator<? super Double> customComparator) {
    this.arrays = new DoubleArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    this.arrays = DoubleArrays.instance();
    return myself;
  }

  /**
   * <p>
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * If you want to set a precision for the comparison either use {@link #containsExactly(double[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Double)} before calling the assertion. 
   * <p>
   * Example :
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertions will pass
   * assertThat(values).containsExactly(1.0, 2.0, 3.0)
   *                   .usingComparatorWithPrecision(0.2)
   *                   .containsExactly(1.1, 2.1, 2.9);
   * 
   * // assertion will fail as actual and expected order differ
   * assertThat(values).containsExactly(2.0, 1.0, 3.0);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   */
  public SELF containsExactly(double... values) {
    arrays.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new double[] { 1.0, 2.0 }).containsExactlyInAnyOrder(1.0, 2.0);
   * assertThat(new double[] { 1.0, 2.0, 1.0 }).containsExactlyInAnyOrder(1.0, 1.0, 2.0);
   * 
   * // assertions will fail
   * assertThat(new double[] { 1.0, 2.0 }).containsExactlyInAnyOrder(1.0);
   * assertThat(new double[] { 1.0 }).containsExactlyInAnyOrder(1.0, 2.0);
   * assertThat(new double[] { 1.0, 2.0, 1.0 }).containsExactlyInAnyOrder(1.0, 2.0);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactlyInAnyOrder(double... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * The values may vary with a specified precision.
   * <p>
   * Example :
   * <pre><code class='java'> double[] values = new double[] { 1.0, 2.0, 3.0 };
   * 
   * // assertion will pass
   * assertThat(values).containsExactly(new double[] {1.0, 1.98, 3.01}, withPrecision(0.05));
   *
   * // assertion fails because |1.0 -1.1| &gt; 0.05 (precision).
   * assertThat(values).containsExactly(new double[] {1.1, 2.0, 3.01}, withPrecision(0.05));
   * 
   * // assertion will fail as actual and expected order differ
   * assertThat(values).containsExactly(new double[] {1.98, 1.0, 3.01}, withPrecision(0.05));</code></pre>
   *
   * @param values the given values.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values within the specified precision 
   *           with same order, i.e. the actual group contains some or none of the given values, or the actual group contains 
   *           more values than the given ones or values are the same but the order is not.
   */
  public SELF containsExactly(double[] values, Offset<Double> precision) {
    usingComparatorWithPrecision(precision.value);
    return containsExactly(values);
  }

  /**
   * Create a {@link Double} comparator which compares double at the given precision and pass it to {@link #usingElementComparator(Comparator)}. 
   * All the following assertions will use this comparator to compare double[] elements.
   *  
   * @param precision precision used to compare {@link Double}.
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public SELF usingComparatorWithPrecision(Double precision) {
    return usingElementComparator(doubleComparator.doubleComparatorWithPrecision(precision));
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> double[] oneTwoThree = { 1.0, 2.0, 3.0 }; 
   *
   * // assertions will pass
   * assertThat(oneTwoThree).containsAnyOf(2.0)
   *                        .containsAnyOf(2.0, 3.0)
   *                        .containsAnyOf(1.0, 2.0, 3.0)
   *                        .containsAnyOf(1.0, 2.0, 3.0, 4.0)
   *                        .containsAnyOf(5.0, 6.0, 7.0, 2.0);
   *
   * // assertions will fail
   * assertThat(oneTwoThree).containsAnyOf(4.0);
   * assertThat(oneTwoThree).containsAnyOf(4.0, 5.0, 6.0, 7.0);</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  public SELF containsAnyOf(double... values) {
    arrays.assertContainsAnyOf(info, actual, values);
    return myself;
  }

}
