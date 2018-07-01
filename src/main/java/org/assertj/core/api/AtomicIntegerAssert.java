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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Integers;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public class AtomicIntegerAssert extends AbstractAssert<AtomicIntegerAssert, AtomicInteger> {

  @VisibleForTesting
  Comparables comparables = new Comparables();

  @VisibleForTesting
  Integers integers = Integers.instance();

  public AtomicIntegerAssert(AtomicInteger actual) {
    super(actual, AtomicIntegerAssert.class);
  }

  /**
   * Verifies that the actual atomic has a value in [start, end] range (start included, end included).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicInteger actual =  new AtomicInteger(5);
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
  public AtomicIntegerAssert hasValueBetween(int startInclusive, int endInclusive) {
    isNotNull();
    integers.assertIsBetween(getWritableAssertionInfo(), actual.get(), startInclusive, endInclusive);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicInteger(1)).hasValueLessThan(2);
   * assertThat(new AtomicInteger(-2)).hasValueLessThan(-1);
   * 
   * // assertions will fail:
   * assertThat(new AtomicInteger(1)).hasValueLessThan(0);
   * assertThat(new AtomicInteger(1)).hasValueLessThan(1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasValueLessThan(int other) {
    isNotNull();
    integers.assertLessThan(getWritableAssertionInfo(), actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicInteger(1)).hasValueLessThanOrEqualTo(1)
   *                                 .hasValueLessThanOrEqualTo(2);
   * assertThat(new AtomicInteger(-2)).hasValueLessThanOrEqualTo(-1);
   * 
   * // assertion will fail:
   * assertThat(new AtomicInteger(1)).hasValueLessThanOrEqualTo(0);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is greater than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasValueLessThanOrEqualTo(int other) {
    isNotNull();
    integers.assertLessThanOrEqualTo(getWritableAssertionInfo(), actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicInteger(1)).hasValueGreaterThan(0);
   * assertThat(new AtomicInteger(-1)).hasValueGreaterThan(-2);
   * 
   * // assertions will fail:
   * assertThat(new AtomicInteger(1)).hasValueGreaterThan(2)
   *                                 .hasValueGreaterThan(1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if the actual atomic value is equal to or less than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasValueGreaterThan(int other) {
    isNotNull();
    integers.assertGreaterThan(getWritableAssertionInfo(), actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value strictly greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicInteger(1)).hasValueGreaterThanOrEqualTo(0);
   * assertThat(new AtomicInteger(1)).hasValueGreaterThanOrEqualTo(1);
   * assertThat(new AtomicInteger(-1)).hasValueGreaterThanOrEqualTo(-2);
   * 
   * // assertion will fail:
   * assertThat(new AtomicInteger(1)).hasValueGreaterThanOrEqualTo(2);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is less than the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasValueGreaterThanOrEqualTo(int other) {
    isNotNull();
    integers.assertGreaterThanOrEqualTo(getWritableAssertionInfo(), actual.get(), other);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a positive value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicInteger(42)).hasPositiveValue();
   *
   * // assertions will fail
   * assertThat(new AtomicInteger(0)).hasPositiveValue();
   * assertThat(new AtomicInteger(-1)).hasPositiveValue();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not positive.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasPositiveValue() {
    isNotNull();
    integers.assertIsPositive(getWritableAssertionInfo(), actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a non positive value (negative or equal zero).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicInteger(-42)).hasNonPositiveValue();
   * assertThat(new AtomicInteger(0)).hasNonPositiveValue();
   *
   * // assertion will fail
   * assertThat(new AtomicInteger(42)).hasNonPositiveValue();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not non positive.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasNonPositiveValue() {
    isNotNull();
    integers.assertIsNotPositive(getWritableAssertionInfo(), actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a negative value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicInteger(-42)).hasNegativeValue();
   *
   * // assertions will fail
   * assertThat(new AtomicInteger(0)).hasNegativeValue();
   * assertThat(new AtomicInteger(42)).hasNegativeValue();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not negative.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasNegativeValue() {
    isNotNull();
    integers.assertIsNegative(getWritableAssertionInfo(), actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a non negative value (positive or equal zero).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicInteger(42)).hasNonNegativeValue();
   * assertThat(new AtomicInteger(0)).hasNonNegativeValue();
   *
   * // assertion will fail
   * assertThat(new AtomicInteger(-42)).hasNonNegativeValue();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is not non negative.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasNonNegativeValue() {
    isNotNull();
    integers.assertIsNotNegative(getWritableAssertionInfo(), actual.get());
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with integer:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicInteger(11)).hasValueCloseTo(10, withinPercentage(20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat(new AtomicInteger(11)).hasValueCloseTo(10, withinPercentage(10));
   *
   * // assertion will fail
   * assertThat(new AtomicInteger(11)).hasValueCloseTo(10, withinPercentage(5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@link Percentage} is {@code null}.
   * @throws AssertionError if the actual atomic value is not close enough to the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasValueCloseTo(int expected, Percentage percentage) {
    isNotNull();
    integers.assertIsCloseToPercentage(getWritableAssertionInfo(), actual.get(), expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual atomic has a value close to the given one within the given offset.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion: 
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Integer)} or {@link Offset#offset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Integer)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Integer)} implies a <b>strict</b> comparison, 
   * use {@link Assertions#within(Integer)} to get the old behavior. 
   * <p>
   * Example with Integer:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(new AtomicInteger(5)).hasValueCloseTo(7, within(3))
   *                                 .hasValueCloseTo(7, byLessThan(3));
   *
   * // if the difference is exactly equals to the offset, it's ok ...
   * assertThat(new AtomicInteger(5)).hasValueCloseTo(7, within(2));
   * // ... but not with byLessThan which implies a strict comparison
   * assertThat(new AtomicInteger(5)).hasValueCloseTo(7, byLessThan(2)); // FAIL
   *
   * // assertion will fail
   * assertThat(new AtomicInteger(5)).hasValueCloseTo(7, within(1));
   * assertThat(new AtomicInteger(5)).hasValueCloseTo(7, byLessThan(1));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given allowed {@link Offset}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@link Offset} is {@code null}.
   * @throws AssertionError if the actual atomic value is not close enough to the given one.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasValueCloseTo(int expected, Offset<Integer> offset) {
    isNotNull();
    integers.assertIsCloseTo(getWritableAssertionInfo(), actual.get(), expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual atomic has the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicInteger(42)).hasValue(42);
   *
   * // assertion will fail
   * assertThat(new AtomicInteger(42)).hasValue(0);</code></pre>
   * 
   * @param expectedValue the expected value.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic does not have the given value.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert hasValue(int expectedValue) {
    isNotNull();
    int actualValue = actual.get();
    if (!objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldHaveValue(actual, expectedValue));
    }
    return myself;
  }

  /**
   * Verifies that the actual atomic does not have the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicInteger(42)).doesNotHaveValue(0);
   *
   * // assertion will fail
   * assertThat(new AtomicInteger(42)).doesNotHaveValue(42);</code></pre>
   * 
   * @param expectedValue the value not expected .
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic has the given value.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicIntegerAssert doesNotHaveValue(int expectedValue) {
    isNotNull();
    int actualValue = actual.get();
    if (objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldNotContainValue(actual, expectedValue));
    }
    return myself;
  }

  @Override
  @CheckReturnValue
  public AtomicIntegerAssert usingComparator(Comparator<? super AtomicInteger> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public AtomicIntegerAssert usingComparator(Comparator<? super AtomicInteger> customComparator, String customComparatorDescription) {
    integers = new Integers(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public AtomicIntegerAssert usingDefaultComparator() {
    integers = Integers.instance();
    return super.usingDefaultComparator();
  }

}
