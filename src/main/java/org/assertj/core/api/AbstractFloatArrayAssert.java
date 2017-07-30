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
import org.assertj.core.internal.FloatArrays;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractFloatArrayAssert<SELF extends AbstractFloatArrayAssert<SELF>>
    extends AbstractArrayAssert<SELF, float[], Float> {

  @VisibleForTesting
  protected FloatArrays arrays = FloatArrays.instance();

  private final ComparatorFactory floatComparator = ComparatorFactory.INSTANCE;

  public AbstractFloatArrayAssert(float[] actual, Class<?> selfType) {
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
   * Examples:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new float[] { 1.0f, 2.0f, 3.0f }).hasSize(3);
   * 
   * // assertions will fail
   * assertThat(new float[] { 1.0f, 2.0f, 1.0f }).hasSize(2);</code></pre>
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
   * assertThat(new float[] { 1.0f, 2.0f, 3.0f }).hasSameSizeAs(Arrays.asList(1, 2, 3));
   * 
   * // assertion will fail
   * assertThat(new float[] { 1.0f, 2.0f, 1.0f }).hasSameSizeAs(Arrays.asList(1, 2));</code></pre>
   */
  @Override
  public SELF hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order.
   * <p>
   * If you want to set a precision for the comparison either use {@link #contains(float[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Examples :
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertions will pass
   * assertThat(values).contains(1.0f, 3.0f, 2.0f)
   *                   .contains(3.0f, 1.0f)
   *                   .usingComparatorWithPrecision(0.5f)
   *                   .contains(1.1f, 2.1f);
   * 
   * // assertions will fail
   * assertThat(values).contains(1.0f, 4.0f);
   * assertThat(values).usingComparatorWithPrecision(0.01f)
   *                   .contains(1.1f, 2.1f);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  public SELF contains(float... values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order, 
   * the comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
   * <p>
   * Examples :
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertion will pass
   * assertThat(values).contains(new float[] {1.01f, 3.01f, 2.0f}, withPrecision(0.02f));
   *
   * // assertions will fail
   * assertThat(values).contains(new float[] {1.0f, 4.0f}, withPrecision(0.5f));
   * assertThat(values).contains(new float[] {4.0f, 7.0f}, withPrecision(2f));</code></pre>
   *
   * @param values the given values.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  public SELF contains(float[] values, Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    return contains(values);
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * <p>
   * If you want to set a precision for the comparison either use {@link #containsOnly(float[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Examples :
   * <pre><code class='java'> float[] values = new double[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertions will pass
   * assertThat(values).containsOnly(1.0f, 2.0f, 3.0f)
   *                   .containsOnly(2.0f, 3.0f, 1.0f)
   *                   .usingComparatorWithPrecision(0.5f)
   *                   .containsOnly(1.1f, 3.1f, 2.1f);
  
   * // assertions will fail
   * assertThat(values).containsOnly(1.0f, 4.0f, 2.0f, 3.0f);
   * assertThat(values).containsOnly(4.0f, 7.0f);
   * assertThat(values).containsOnly(1.1f, 2.1f, 3.1f);
   * assertThat(values).usingComparatorWithPrecision(0.01f)
   *                   .containsOnly(1.1f, 2.1f, 3.1f);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public SELF containsOnly(float... values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
   * <p>
   * Examples :
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertion will pass
   * assertThat(values).containsOnly(new float[] {1.0f, 2.0f, 3.0f }, withPrecision(0.00001f))
   *                   .containsOnly(new float[] {2.0,f 3.0f, 0.7f}, withPrecision(0.5f));
   *
   * // assertions will fail
   * assertThat(values).containsOnly(new float[] {1.0f, 4.0f, 2.0f, 3.0f}, withPrecision(0.5f));
   * assertThat(values).containsOnly(new float[] {4.0f, 7.0f}, withPrecision(0.2f));</code></pre>
   *
   * @param values the given values.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  public SELF containsOnly(float[] values, Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    return containsOnly(values);
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * If you want to set a precision for the comparison either use {@link #containsOnlyOnce(float[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Examples :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new float[] { 1.0f, 2.0f, 3.0f }).containsOnlyOnce(1.0f, 2.0f)
   *                                           .usingComparatorWithPrecision(0.5f)
   *                                           .containsOnlyOnce(1.1f, 3.1f, 2.1f);
   * 
   * // assertions will fail
   * assertThat(new float[] { 1.0f, 2.0f, 1.0f }).containsOnlyOnce(1.0f);
   * assertThat(new float[] { 1.0f, 2.0f, 1.0f }).containsOnlyOnce(1.0f, 2.0f);
   * assertThat(new float[] { 1.0f, 2.0f, 3.0f }).containsOnlyOnce(4.0f);
   * assertThat(new float[] { 1.0f, 2.0f, 3.0f }).usingComparatorWithPrecision(0.05f)
   *                                           .containsOnlyOnce(1.1f, 2.1f);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more than once these values.
   */
  public SELF containsOnlyOnce(float... values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
  * Verifies that the actual array contains the given values only once.
  * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
  * <p>
  * Examples :
  * <pre><code class='java'> // assertion will pass
  * assertThat(new float[] { 1.0f, 2.0f, 3.0f }).containsOnlyOnce(new float[] {1.1f, 2.0f}, withPrecision(0.2f));
  *
  * // assertions will fail
  * assertThat(new float[] { 1.0f, 2.0f, 1.0f }).containsOnlyOnce(new float[] {1.05f}, withPrecision(0.1f));
  * assertThat(new float[] { 1.0f, 2.0f, 3.0f }).containsOnlyOnce(new float[] {4.0f}, withPrecision(0.1f));
  * assertThat(new float[] { 1.0f, 2.0f, 3.0f, 3.0f }).containsOnlyOnce(new float[] {0.1f, 0.9f, 2.0f, 3.11f, 4.0f, 5.0f}, withPrecision(0.2f));</code></pre>
  *
  * @param values the given values.
  * @param precision the precision under which the values may vary.
  * @return {@code this} assertion object.
  * @throws NullPointerException if the given argument is {@code null}.
  * @throws IllegalArgumentException if the given argument is an empty array.
  * @throws AssertionError if the actual array is {@code null}.
  * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
  *           or none of the given values, or the actual group contains more than once these values.
  */
  public SELF containsOnlyOnce(float[] values, Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    return containsOnlyOnce(values);
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * <p>
   * If you want to set a precision for the comparison either use {@link #containsSequence(float[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Examples :
   * <pre><code class='java'> float[] values = new float[] { 1.0f, 2.0f, 3.0f };
   * 
   * // assertion will pass
   * assertThat(values).containsSequence(1.0f, 2.0f)
   *                   .containsSequence(1.0f, 2.0f, 3.0f)
   *                   .containsSequence(2.0f, 3.0f)
   *                   .usingComparatorWithPrecision(0.5f)
   *                   .containsSequence(1.1f, 2.1f);
   * 
   * // assertions will fail
   * assertThat(values).containsSequence(1.0f, 3.0f);
   * assertThat(values).containsSequence(4.0f, 7.0f);
   * assertThat(values).usingComparatorWithPrecision(0.01f)
   *                   .containsSequence(1.1f, 2.0f, 3.0f);</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(float... sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence, without any other values between them.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
   * <p>
   * Examples :
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertions will pass
   * assertThat(values).containsSequence(new float[] {1.07f, 2.0f}, withPrecision(0.1f))
   *                   .containsSequence(new float[] {1.1f, 2.1f, 3.0f}, withPrecision(0.2f))
   *                   .containsSequence(new float[] {2.2f, 3.0f}, withPrecision(0.3f));
   *
   * // assertions will fail
   * assertThat(values).containsSequence(new float[] {1.0f, 3.0f}, withPrecision(0.2f));
   * assertThat(values).containsSequence(new float[] {4.0f, 7.0f}, withPrecision(0.1f));</code></pre>
   *
   * @param sequence the sequence of values to look for.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  public SELF containsSequence(float[] sequence, Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    return containsSequence(sequence);
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * <p>
   * If you want to set a precision for the comparison either use {@link #containsSubsequence(float[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Examples :
   * <pre><code class='java'> float[] values = new float[] { 1.0f, 2.0f, 3.0f };
   * 
   * // assertion will pass
   * assertThat(values).containsSubsequence(1.0f, 2.0f);
   *                   .containsSubsequence(1.0f, 2.0f, 3.0f)
   *                   .containsSubsequence(1.0f, 3.0f)
   *                   .usingComparatorWithPrecision(0.5f)
   *                   .containsSubsequence(1.1f, 2.1f);
   * 
   * // assertions will fail
   * assertThat(values).containsSubsequence(3.0f, 1.0f);
   * assertThat(values).containsSubsequence(4.0f, 7.0f);
   * assertThat(values).usingComparatorWithPrecision(0.01f)
   *                   .containsSubsequence(1.1f, 2.0f);</code></pre>
   * 
   * @param subsequence the subsequence of values to look for.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(float... subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence (possibly with other values between them).
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
   * <p>
   * Examples :
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertions will pass
   * assertThat(values).containsSubsequence(new float[] {1.0f, 2.0f}, withPrecision(0.1f))
   *                   .containsSubsequence(new float[] {1.0f, 2.07f, 3.0f}, withPrecision(0.1f))
   *                   .containsSubsequence(new float[] {2.1f, 2.9f}, withPrecision(0.2f));
   *
   * // assertions will fail
   * assertThat(values).containsSubsequence(new float[] {1.0f, 3.0f}, withPrecision(0.1f));
   * assertThat(values).containsSubsequence(new float[] {4.0f, 7.0f}, withPrecision(0.1f));</code></pre>
   *
   * @param subsequence the subsequence of values to look for.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  public SELF containsSubsequence(float[] subsequence, Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    return containsSubsequence(subsequence);
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * <p>
   * If you want to set a precision for the comparison either use {@link #contains(float, Index, Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> float[] values = new float[] { 1.0f, 2.0f, 3.0f };
   * 
   * // assertion will pass
   * assertThat(values).contains(1.0f, atIndex(O))
   *                   .contains(3.0f, atIndex(2))
   *                   .usingComparatorWithPrecision(0.5f)
   *                   .contains(3.1f, atIndex(2));
   * 
   * // assertions will fail
   * assertThat(values).contains(1.0f, atIndex(1));
   * assertThat(values).contains(4.0f, atIndex(2));
   * assertThat(values).usingComparatorWithPrecision(0.01f)
   *                   .contains(3.1f, atIndex(2));</code></pre>
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
  public SELF contains(float value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given value at the given index.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
   * <p>
   * Example:
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertions will pass
   * assertThat(values).contains(1.0f, atIndex(O), withPrecision(0.01f))
   *                   .contains(3.3f, atIndex(2), withPrecision(0.5f));
   *
   * // assertions will fail
   * assertThat(values).contains(1.0f, atIndex(1), withPrecision(0.2f));
   * assertThat(values).contains(4.5f, atIndex(2), withPrecision(0.1f));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @param precision the precision which the value may vary.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the actual array.
   * @throws AssertionError if the actual array does not contain the given value at the given index.
   */
  public SELF contains(float value, Index index, Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    return contains(value, index);
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * If you want to set a precision for the comparison either use {@link #doesNotContain(float[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> float[] values = new float[] { 1.0f, 2.0f, 3.0f };
   * 
   * // assertion will pass
   * assertThat(values).doesNotContain(4.0f, 8.0f)
   *                   .usingComparatorWithPrecision(0.0001f)
   *                   .doesNotContain(1.01f, 2.01f);
   *                   
   * // assertions will fail
   * assertThat(values).doesNotContain(1.0f, 4.0f, 8.0f);
   * assertThat(values).usingComparatorWithPrecision(0.1f)
   *                   .doesNotContain(1.001f, 2.001f);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public SELF doesNotContain(float... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
   * <p>
   * Example:
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertion will pass
   * assertThat(values).doesNotContain(new float[] {4.0f, 8.0f}, withPrecision(0.5f));
   *
   * // assertion will fail
   * assertThat(values).doesNotContain(new float[] {1.05f, 4.0f, 8.0f}, withPrecision(0.1f));</code></pre>
   *
   * @param values the given values.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  public SELF doesNotContain(float[] values, Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * <p>
   * If you want to set a precision for the comparison either use {@link #doesNotContain(float, Index, Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> float[] values = new float[] { 1.0f, 2.0f, 3.0f };
   * 
   * // assertion will pass
   * assertThat(values).doesNotContain(1.0f, atIndex(1))
   *                   .doesNotContain(2.0f, atIndex(0))
   *                   .usingComparatorWithPrecision(0.001)
   *                   .doesNotContain(1.1f, atIndex(0));
   * 
   * // assertions will fail
   * assertThat(values).doesNotContain(1.0f, atIndex(0));
   * assertThat(values).usingComparatorWithPrecision(0.1)
   *                   .doesNotContain(1.001f, atIndex(0));</code></pre>
   * 
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public SELF doesNotContain(float value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given value at the given index.
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
   * <p>
   * Example:
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertions will pass
   * assertThat(values).doesNotContain(1.01f, atIndex(1), withPrecision(0.0001f))
   *                   .doesNotContain(2.05f, atIndex(0), withPrecision(0.1f));
   *
   * // assertion will fail
   * assertThat(values).doesNotContain(1.01f, atIndex(0), withPrecision(0.1f));</code></pre>
   *
   * @param value the value to look for.
   * @param index the index where the value should be stored in the actual array.
   * @param precision the precision under which the value may vary.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given value at the given index.
   */
  public SELF doesNotContain(float value, Index index, Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    return doesNotContain(value, index);
  }

  /**
   * Verifies that the actual array does not contain duplicates.
   * <p>
   * If you want to set a precision for the comparison either use {@link #doesNotHaveDuplicates(Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new float[] { 1.0f, 2.0f, 3.0f }).doesNotHaveDuplicates();
   * assertThat(new float[] { 1.0f, 1.1f }).usingComparatorWithPrecision(0.01f)
   *                                      .doesNotHaveDuplicates();
   * 
   * // assertions will fail
   * assertThat(new float[] { 1.0f, 1.0f, 2.0f, 3.0f }).doesNotHaveDuplicates();
   * assertThat(new float[] { 1.0f, 1.1f }).usingComparatorWithPrecision(0.5f)
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
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new float[] {1.0f, 2.0f, 3.0f}).doesNotHaveDuplicates(withPrecision(0.1f));
   * assertThat(new float[] {1.1f, 1.2f, 1.3f}).doesNotHaveDuplicates(withPrecision(0.05f));
   * 
   * // assertion will fail
   * assertThat(new float[] {1.0f, 1.01f, 2.0f}).doesNotHaveDuplicates(withPrecision(0.1f));</code></pre>
   * 
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains duplicates.
   */
  public SELF doesNotHaveDuplicates(Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    return doesNotHaveDuplicates();
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(float...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * If you want to set a precision for the comparison either use {@link #startsWith(float[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> float[] values = new float[] { 1.0f, 2.0f, 3.0f };
   * 
   * // assertion will pass
   * assertThat(values).startsWith(1.0f, 2.0f)
   *                   .usingComparatorWithPrecision(0.5f)
   *                   .startsWith(1.1f, 2.1f);
   * 
   * // assertion will fail
   * assertThat(values).startsWith(2.0f, 3.0f);</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence.
   */
  public SELF startsWith(float... sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(float...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual array.
   * <p>
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
   * <p>
   * Example:
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertion will pass
   * assertThat(values).startsWith(new float[] {1.01f, 2.01f}, withPrecision(0.1f));
   * 
   * // assertions will fail
   * assertThat(values).startsWith(new float[] {2.0f, 1.0f}, withPrecision(0.1f))
   * assertThat(values).startsWith(new float[] {1.1f, 2.1f}, withPrecision(0.5f))</code></pre>
   * 
   * @param values the sequence of values to look for.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF startsWith(float[] values, Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    return startsWith(values);
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(float...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * If you want to set a precision for the comparison either use {@link #endsWith(float[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Example:
   * <pre><code class='java'> float[] values = new float[] { 1.0f, 2.0f, 3.0f };
   * 
   * // assertion will pass
   * assertThat(values).endsWith(2.0f, 3.0f)
   *                   .usingComparatorWithPrecision(0.5f)
   *                   .endsWith(2.1f, 3.1f);
   * 
   * // assertion will fail
   * assertThat(values).endsWith(1.0f, 3.0f);</code></pre>
   * 
   * @param sequence the sequence of values to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF endsWith(float... sequence) {
    arrays.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #containsSequence(float...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * The comparison is done at the given precision/offset set with {@link Assertions#withPrecision(Float)}.
   * <p>
   * Example:
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertion will pass
   * assertThat(values).endsWith(new float[] {2.01f, 3.01f}, withPrecision(0.1f));
   * 
   * // assertions will fail
   * assertThat(values).endsWith(new float[] {3.0f, 2.0f}, withPrecision(0.1f))
   * assertThat(values).endsWith(new float[] {2.1f, 3.1f}, withPrecision(0.5f))</code></pre>
   * 
   * @param values the sequence of values to look for.
   * @param precision the precision under which the values may vary.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence.
   */
  public SELF endsWith(float[] values, Offset<Float> precision) {
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
  public SELF isSortedAccordingTo(Comparator<? super Float> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingElementComparator(Comparator<? super Float> customComparator) {
    this.arrays = new FloatArrays(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    this.arrays = FloatArrays.instance();
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * <p>
   * If you want to set a precision for the comparison either use {@link #containsExactly(float[], Offset)} 
   * or {@link #usingComparatorWithPrecision(Float)} before calling the assertion. 
   * <p>
   * Example :
   * <pre><code class='java'> float[] values = new float[] { 1.0f, 2.0f, 3.0f };
   * 
   * // assertion will pass
   * assertThat(values).containsExactly(1.0f, 2.0f, 3.0f)
   *                   .usingComparatorWithPrecision(0.2f)
   *                   .containsExactly(1.1f, 2.1f, 2.9f);
   * 
   * // assertion will fail as actual and expected order differ
   * assertThat(values).containsExactly(2.0f, 1.0f, 3.0f);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values with same order, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones
   *           or values are the same but the order is not.
   */
  public SELF containsExactly(float... values) {
    arrays.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual group contains only the given values and nothing else, <b>in order</b>.
   * The values may vary with a specified precision.
   * <p>
   * Example :
   * <pre><code class='java'> float[] values = new float[] {1.0f, 2.0f, 3.0f};
   * 
   * // assertion will pass
   * assertThat(values).containsExactly(new float[] {1.0f, 1.98f, 3.01f}, withPrecision(0.05f));
   *
   * // assertion fails because |1.0 - 1.1| &gt; 0.05 (precision)
   * assertThat(values).containsExactly(new float[] {1.1f, 2.0f, 3.01f}, withPrecision(0.05f));
   * 
   * // assertion will fail as actual and expected order differ
   * assertThat(values).containsExactly(new float[] {1.98f, 1.0f, 3.01f}, withPrecision(0.05f));</code></pre>
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
  public SELF containsExactly(float[] values, Offset<Float> precision) {
    usingComparatorWithPrecision(precision.value);
    return containsExactly(values);
  }

  /**
   * Verifies that the actual group contains exactly the given values and nothing else, <b>in any order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(new float[] { 1.0F, 2.0F }).containsExactlyInAnyOrder(1.0F, 2.0F);
   * assertThat(new float[] { 1.0F, 2.0F, 1.0F }).containsExactlyInAnyOrder(1.0F, 1.0F, 2.0F);
   * 
   * // assertions will fail
   * assertThat(new float[] { 1.0F, 2.0F }).containsExactlyInAnyOrder(1.0F);
   * assertThat(new float[] { 1.0F }).containsExactlyInAnyOrder(1.0F, 2.0F);
   * assertThat(new float[] { 1.0F, 2.0F, 1.0F }).containsExactlyInAnyOrder(1.0F, 2.0F);</code></pre>
   * 
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 2.6.0 / 3.6.0
   */
  public SELF containsExactlyInAnyOrder(float... values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, values);
    return myself;
  }

  /**
   * Create a {@link Float} comparator which compares floats at the given precision and pass it to {@link #usingElementComparator(Comparator)}. 
   * All the following assertions will use this comparator to compare float[] elements.
   *  
   * @param precision precision used to compare {@link Float}.
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public SELF usingComparatorWithPrecision(Float precision) {
    return usingElementComparator(floatComparator.floatComparatorWithPrecision(precision));
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> float[] oneTwoThree = { 1.0f, 2.0f, 3.0f }; 
   *
   * // assertions will pass
   * assertThat(oneTwoThree).containsAnyOf(2.0f)
   *                        .containsAnyOf(2.0f, 3.0f)
   *                        .containsAnyOf(1.0f, 2.0f, 3.0f)
   *                        .containsAnyOf(1.0f, 2.0f, 3.0f, 4.0f)
   *                        .containsAnyOf(5.0f, 6.0f, 7.0f, 2.0f);
   *
   * // assertions will fail
   * assertThat(oneTwoThree).containsAnyOf(4.0f);
   * assertThat(oneTwoThree).containsAnyOf(4.0f, 5.0f, 6.0f, 7.0f);</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  public SELF containsAnyOf(float... values) {
    arrays.assertContainsAnyOf(info, actual, values);
    return myself;
  }

}
