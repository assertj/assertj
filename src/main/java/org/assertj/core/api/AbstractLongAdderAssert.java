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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Longs;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

import java.util.Comparator;
import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;

/**
 * Base class for all implementations of assertions for {@link LongAdder}s.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *               target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *               for more details.
 *
 * @author Grzegorz Piwowarek
 */
class AbstractLongAdderAssert<SELF extends AbstractLongAdderAssert<SELF>> extends AbstractAssert<SELF, LongAdder> implements NumberAssert<SELF, Long> {

  @VisibleForTesting
  Longs longs = Longs.instance();

  public AbstractLongAdderAssert(LongAdder longAdder, Class<?> selfType) {
    super(longAdder, selfType);
  }

  /**
   * Verifies that the actual sum has a value in [start, end] range (start included, end included).
   * <p>
   * Example:
   * <pre><code class='java'>
   * LongAdder actual = new LongAdder();
   * actual.add(5);
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
   * @param endInclusive   the end value (inclusive).
   *
   * @return this assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is not in [start, end] range.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasValueBetween(long startInclusive, long endInclusive) {
    isNotNull();
    longs.assertIsBetween(getWritableAssertionInfo(), actual.sum(), startInclusive, endInclusive);
    return myself;
  }

  /**
   * Verifies that the actual sum has a value strictly less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertions will pass:
   * LongAdder actual1 = new LongAdder();
   * actual1.add(1);
   * assertThat(actual1).hasValueLessThan(2);
   *
   * LongAdder actual2 = new LongAdder();
   * actual2.add(-2);
   * assertThat(actual2).hasValueLessThan(-1);
   *
   * // assertions will fail:
   * assertThat(actual1).hasValueLessThan(0)
   *                    .hasValueLessThan(1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is equal to or greater than the given one.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasValueLessThan(long other) {
    isNotNull();
    longs.assertLessThan(getWritableAssertionInfo(), actual.sum(), other);
    return myself;
  }

  /**
   * Verifies that the actual sum has a value strictly less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertions will pass:
   * LongAdder actual1 = new LongAdder();
   * actual1.add(1);
   * assertThat(actual1).hasValueLessThanOrEqualTo(1)
   *                    .hasValueLessThanOrEqualTo(2);
   *
   * LongAdder actual2 = new LongAdder();
   * actual2.add(-2);
   * assertThat(actual2).hasValueLessThanOrEqualTo(-1);
   *
   * // assertion will fail:
   * assertThat(actual1).hasValueLessThanOrEqualTo(0);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is greater than the given one.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasValueLessThanOrEqualTo(long other) {
    isNotNull();
    longs.assertLessThanOrEqualTo(getWritableAssertionInfo(), actual.sum(), other);
    return myself;
  }

  /**
   * Verifies that the actual sum has a value strictly greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertions will pass:
   * LongAdder actual1 = new LongAdder();
   * actual1.add(1);
   * assertThat(actual1).hasValueGreaterThan(0);
   *
   * LongAdder actual2 = new LongAdder();
   * actual2.add(-1);
   * assertThat(actual2).hasValueGreaterThan(-2);
   *
   * // assertions will fail:
   * assertThat(actual1).hasValueGreaterThan(2)
   *                    .hasValueGreaterThan(1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is equal to or less than the given one.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasValueGreaterThan(long other) {
    isNotNull();
    longs.assertGreaterThan(getWritableAssertionInfo(), actual.sum(), other);
    return myself;
  }

  /**
   * Verifies that the actual sum has a value strictly greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertions will pass:
   * LongAdder actual1 = new LongAdder();
   * actual1.add(1);
   * assertThat(actual1).hasValueGreaterThanOrEqualTo(0)
   *                    .hasValueGreaterThanOrEqualTo(1);
   *
   * LongAdder actual2 = new LongAdder();
   * actual1.add(-1);
   * assertThat(actual2).hasValueGreaterThanOrEqualTo(-2);
   *
   * // assertion will fail:
   * assertThat(actual1).hasValueGreaterThanOrEqualTo(2);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is less than the given one.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasValueGreaterThanOrEqualTo(long other) {
    isNotNull();
    longs.assertGreaterThanOrEqualTo(getWritableAssertionInfo(), actual.sum(), other);
    return myself;
  }

  /**
   * Verifies that the actual sum has a positive value.
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertion will pass
   * LongAdder actual = new LongAdder();
   * actual.add(1);
   * assertThat(actual).hasPositiveValue();
   *
   * // assertions will fail
   * assertThat(new LongAdder()).hasPositiveValue();
   *
   * LongAdder actual1 = new LongAdder();
   * actual1.add(-1);
   * assertThat(actual1).hasPositiveValue();</code></pre>
   *
   * @return this assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is not positive.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasPositiveValue() {
    isNotNull();
    longs.assertIsPositive(getWritableAssertionInfo(), actual.sum());
    return myself;
  }

  /**
   * Verifies that the actual sum has a non positive value (negative or equal zero).
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertions will pass
   * LongAdder actual = new LongAdder();
   * actual.add(-42);
   * assertThat(actual).hasNonPositiveValue();
   * assertThat(new LongAdder()).hasNonPositiveValue();
   *
   * // assertion will fail
   * LongAdder actual1 = new LongAdder();
   * actual1.add(42);
   * assertThat(actual1).hasNonPositiveValue();</code></pre>
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is not non positive.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasNonPositiveValue() {
    isNotNull();
    longs.assertIsNotPositive(getWritableAssertionInfo(), actual.sum());
    return myself;
  }

  /**
   * Verifies that the actual sum has a negative value.
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertion will pass
   * LongAdder actual = new LongAdder();
   * actual.add(-42);
   * assertThat(actual).hasNegativeValue();
   *
   * // assertions will fail
   * assertThat(new LongAdder()).hasNegativeValue();
   * LongAdder actual1 = new LongAdder();
   * actual1.add(42);
   * assertThat(actual1).hasNegativeValue();</code></pre>
   *
   * @return this assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is not negative.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasNegativeValue() {
    isNotNull();
    longs.assertIsNegative(getWritableAssertionInfo(), actual.sum());
    return myself;
  }

  /**
   * Verifies that the actual sum has a non negative value (positive or equal zero).
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertions will pass
   * LongAdder actual = new LongAdder();
   * actual.add(42);
   * assertThat(actual).hasNonNegativeValue();
   * assertThat(new LongAdder()).hasNonNegativeValue();
   *
   * // assertion will fail
   * LongAdder actual1 = new LongAdder();
   * actual1.add(-42);
   * assertThat(actual1).hasNonNegativeValue();</code></pre>
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is not non negative.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasNonNegativeValue() {
    isNotNull();
    longs.assertIsNotNegative(getWritableAssertionInfo(), actual.sum());
    return myself;
  }

  /**
   * Verifies that the actual sum has a value close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with Long:
   * <pre><code class='java'>
   * // assertions will pass:
   * LongAdder actual = new LongAdder();
   * actual.add(11);
   * assertThat(actual).hasValueCloseTo(10, withinPercentage(20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat(actual).hasValueCloseTo(10, withinPercentage(10));
   *
   * // assertion will fail
   * assertThat(actual).hasValueCloseTo(10, withinPercentage(5));</code></pre>
   *
   * @param expected   the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   *
   * @return {@code this} assertion object.
   *
   * @throws NullPointerException if the given {@link Percentage} is {@code null}.
   * @throws AssertionError       if the actual sum value is not close enough to the given one.
   * @throws AssertionError       if the actual adder is {@code null}.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasValueCloseTo(long expected, Percentage percentage) {
    isNotNull();
    longs.assertIsCloseToPercentage(getWritableAssertionInfo(), actual.sum(), expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual sum has a value close to the given one within the given offset.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Long)} or {@link Offset#offset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Long)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * Example with Long:
   * <pre><code class='java'>
   * // assertions will pass:
   * LongAdder actual = new LongAdder();
   * actual.add(5);
   *
   * assertThat(actual).hasValueCloseTo(7L, within(3L))
   *                   .hasValueCloseTo(7L, byLessThan(3L));
   *
   * // if the difference is exactly equals to the offset, it's ok ...
   * assertThat(actual).hasValueCloseTo(7L, within(2L));
   * // ... but not with byLessThan which implies a strict comparison
   * assertThat(actual).hasValueCloseTo(7L, byLessThan(2L)); // FAIL
   *
   * // assertion will fail
   * assertThat(actual).hasValueCloseTo(7L, within(1L));
   * assertThat(actual).hasValueCloseTo(7L, byLessThan(1L));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset   the given allowed {@link Offset}.
   *
   * @return {@code this} assertion object.
   *
   * @throws NullPointerException if the given {@link Offset} is {@code null}.
   * @throws AssertionError       if the actual sum value is not close enough to the given one.
   * @throws AssertionError       if the actual adder is {@code null}.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasValueCloseTo(long expected, Offset<Long> offset) {
    isNotNull();
    longs.assertIsCloseTo(getWritableAssertionInfo(), actual.sum(), expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual sum has the given value.
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertion will pass
   * LongAdder actual = new LongAdder();
   * actual.add(42);
   * assertThat(actual).hasValue(42);
   *
   * // assertion will fail
   * assertThat(actual).hasValue(0);</code></pre>
   *
   * @param expectedValue the value not expected .
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is not non negative.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasValue(long expectedValue) {
    isNotNull();
    long actualValue = actual.sum();
    if (!objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldHaveValue(actual, expectedValue));
    }
    return myself;
  }

  /**
   * Verifies that the actual sum has not the given value.
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertion will pass
   * LongAdder actual = new LongAdder();
   * actual.add(42);
   * assertThat(actual).doesNotHaveValue(0);
   *
   * // assertion will fail
   * assertThat(actual).doesNotHaveValue(42);</code></pre>
   *
   * @param expectedValue the value not expected .
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum value is not non negative.
   * @since 2.16.0 / 3.16.0
   */
  public SELF doesNotHaveValue(long expectedValue) {
    isNotNull();
    long actualValue = actual.sum();
    if (objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldNotContainValue(actual, expectedValue));
    }
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super LongAdder> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super LongAdder> customComparator, String customComparatorDescription) {
    longs = new Longs(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    longs = Longs.instance();
    return super.usingDefaultComparator();
  }

  @Override
  public SELF isZero() {
    longs.assertIsZero(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isNotZero() {
    longs.assertIsNotZero(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isOne() {
    longs.assertIsOne(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isPositive() {
    longs.assertIsPositive(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isNegative() {
    longs.assertIsNegative(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isNotNegative() {
    longs.assertIsNotNegative(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isNotPositive() {
    longs.assertIsNotPositive(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isBetween(Long start, Long end) {
    longs.assertIsBetween(info, actual.longValue(), start, end);
    return myself;
  }

  @Override
  public SELF isStrictlyBetween(Long start, Long end) {
    longs.assertIsStrictlyBetween(info, actual.longValue(), start, end);
    return myself;
  }

  @Override
  public SELF isCloseTo(Long expected, Offset<Long> offset) {
    longs.assertIsCloseTo(info, actual.longValue(), expected, offset);
    return myself;
  }

  @Override
  public SELF isNotCloseTo(Long expected, Offset<Long> offset) {
    longs.assertIsNotCloseTo(info, actual.longValue(), expected, offset);
    return myself;
  }

  @Override
  public SELF isCloseTo(Long expected, Percentage percentage) {
    longs.assertIsCloseToPercentage(info, actual.longValue(), expected, percentage);
    return myself;
  }

  @Override
  public SELF isNotCloseTo(Long expected, Percentage percentage) {
    longs.assertIsNotCloseToPercentage(info, actual.longValue(), expected, percentage);
    return myself;
  }
}
