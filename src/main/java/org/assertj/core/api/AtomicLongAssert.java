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

import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Longs;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public class AtomicLongAssert extends AbstractAssert<AtomicLongAssert, AtomicLong> {

  @VisibleForTesting
  Comparables comparables = Comparables.instance();

  @VisibleForTesting
  Longs longs = Longs.instance();

  public AtomicLongAssert(AtomicLong actual) {
    super(actual, AtomicLongAssert.class);
  }

  /**
   * Verifies that the actual atomic has a value in [start, end] range (start included, end included).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicLong actual =  new AtomicLong(5);
   *  
   * // assertions succeed
   * assertThat(actual).hasValueBetween(4, 6)
   *                   .hasValueBetween(4, 5)
   *                   .hasValueBetween(5, 6);
   * 
   * // assertions fail
   * assertThat(actual).hasValueBetween(6, 8)
   *                   .hasValueBetween(0, 4);</code></pre>
   * 
   * @param startInclusive the start value (inclusive).
   * @param endInclusive the end value (inclusive).
   * @return this assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not in [start, end] range.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueBetween(long startInclusive, long endInclusive) {
    isNotNull();
    longs.assertIsBetween(getWritableAssertionInfo(), actual.get(), startInclusive, endInclusive);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(1)).hasValueLessThan(2);
   * assertThat(new AtomicLong(-2)).hasValueLessThan(-1);
   * 
   * // assertions will fail:
   * assertThat(new AtomicLong(1)).hasValueLessThan(0)
   *                              .hasValueLessThan(1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueLessThan(long other) {
    isNotNull();
    longs.assertLessThan(getWritableAssertionInfo(), actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(1)).hasValueLessThanOrEqualTo(1)
   *                              .hasValueLessThanOrEqualTo(2);
   * assertThat(new AtomicLong(-2)).hasValueLessThanOrEqualTo(-1);
   * 
   * // assertion will fail:
   * assertThat(new AtomicLong(1)).hasValueLessThanOrEqualTo(0);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is greater than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueLessThanOrEqualTo(long other) {
    isNotNull();
    longs.assertLessThanOrEqualTo(getWritableAssertionInfo(), actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(1)).hasValueGreaterThan(0);
   * assertThat(new AtomicLong(-1)).hasValueGreaterThan(-2);
   * 
   * // assertions will fail:
   * assertThat(new AtomicLong(1)).hasValueGreaterThan(2)
   *                              .hasValueGreaterThan(1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if the actual atomic value is equal to or less than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueGreaterThan(long other) {
    isNotNull();
    longs.assertGreaterThan(getWritableAssertionInfo(), actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(1)).hasValueGreaterThanOrEqualTo(0)     
   *                              .hasValueGreaterThanOrEqualTo(1);
   * assertThat(new AtomicLong(-1)).hasValueGreaterThanOrEqualTo(-2);
   * 
   * // assertion will fail:
   * assertThat(new AtomicLong(1)).hasValueGreaterThanOrEqualTo(2);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is less than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueGreaterThanOrEqualTo(long other) {
    isNotNull();
    longs.assertGreaterThanOrEqualTo(getWritableAssertionInfo(), actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a positive value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicLong(42)).hasPositiveValue();
   *
   * // assertions will fail
   * assertThat(new AtomicLong(0)).hasPositiveValue();
   * assertThat(new AtomicLong(-1)).hasPositiveValue();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not positive.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasPositiveValue() {
    isNotNull();
    longs.assertIsPositive(getWritableAssertionInfo(), actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a non positive value (negative or equal zero).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicLong(-42)).hasNonPositiveValue();
   * assertThat(new AtomicLong(0)).hasNonPositiveValue();
   *
   * // assertion will fail
   * assertThat(new AtomicLong(42)).hasNonPositiveValue();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not non positive.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasNonPositiveValue() {
    isNotNull();
    longs.assertIsNotPositive(getWritableAssertionInfo(), actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a negative value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicLong(-42)).hasNegativeValue();
   *
   * // assertions will fail
   * assertThat(new AtomicLong(0)).hasNegativeValue();
   * assertThat(new AtomicLong(42)).hasNegativeValue();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not negative.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasNegativeValue() {
    isNotNull();
    longs.assertIsNegative(getWritableAssertionInfo(), actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a non negative value (positive or equal zero).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicLong(42)).hasNonNegativeValue();
   * assertThat(new AtomicLong(0)).hasNonNegativeValue();
   *
   * // assertion will fail
   * assertThat(new AtomicLong(-42)).hasNonNegativeValue();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not non negative.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasNonNegativeValue() {
    isNotNull();
    longs.assertIsNotNegative(getWritableAssertionInfo(), actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with Long:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(11)).hasValueCloseTo(10, withinPercentage(20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat(new AtomicLong(11)).hasValueCloseTo(10, withinPercentage(10));
   *
   * // assertion will fail
   * assertThat(new AtomicLong(11)).hasValueCloseTo(10, withinPercentage(5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@link Percentage} is {@code null}.
   * @throws AssertionError if the actual atomic value is not close enough to the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueCloseTo(long expected, Percentage percentage) {
    isNotNull();
    longs.assertIsCloseToPercentage(getWritableAssertionInfo(), actual.get(), expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value close to the given one within the given offset.<br>
   * If difference is equal to the offset value, assertion is considered valid.
   * <p>
   * Example with Long:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicLong(5)).hasValueCloseTo(7, offset(3));
   *
   * // if the difference is exactly equals to the offset, it's ok
   * assertThat(new AtomicLong(5)).hasValueCloseTo(7, offset(2));
   *
   * // assertion will fail
   * assertThat(new AtomicLong(5)).hasValueCloseTo(7, offset(1));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given allowed {@link Offset}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@link Offset} is {@code null}.
   * @throws AssertionError if the actual atomic value is not close enough to the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValueCloseTo(long expected, Offset<Long> offset) {
    isNotNull();
    longs.assertIsCloseTo(getWritableAssertionInfo(), actual.get(), expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual atomic has the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicLong(42)).hasValue(42);
   *
   * // assertion will fail
   * assertThat(new AtomicLong(42)).hasValue(0);</code></pre>
   * 
   * @param expectedValue the value not expected .
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not non negative.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert hasValue(long expectedValue) {
    isNotNull();
    long actualValue = actual.get();
    if (!objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldHaveValue(actual, expectedValue));
    }
    return myself;
  }

  /**
   * Verifies that the actual atomic has not the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicLong(42)).doesNotHaveValue(0);
   *
   * // assertion will fail
   * assertThat(new AtomicLong(42)).doesNotHaveValue(42);</code></pre>
   * 
   * @param expectedValue the value not expected .
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not non negative.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicLongAssert doesNotHaveValue(long expectedValue) {
    isNotNull();
    long actualValue = actual.get();
    if (objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldNotContainValue(actual, expectedValue));
    }
    return myself;
  }

  @Override
  @CheckReturnValue
  public AtomicLongAssert usingComparator(Comparator<? super AtomicLong> customComparator) {
    super.usingComparator(customComparator);
    longs = new Longs(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  @CheckReturnValue
  public AtomicLongAssert usingDefaultComparator() {
    super.usingDefaultComparator();
    longs = Longs.instance();
    return myself;
  }

}
