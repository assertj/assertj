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

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Longs;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Long}s.
 * 
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author Drummond Dawson
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractLongAssert<SELF extends AbstractLongAssert<SELF>> extends AbstractComparableAssert<SELF, Long>
    implements NumberAssert<SELF, Long> {

  @VisibleForTesting
  Longs longs = Longs.instance();

  public AbstractLongAssert(Long actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1L).isEqualTo(1L);
   * assertThat(-1L).isEqualTo(-1L);
   * 
   * // assertions will fail:
   * assertThat(1L).isEqualTo(2L);
   * assertThat(1L).isEqualTo(-1L);</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(long expected) {
    longs.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1L).isNotEqualTo(2L);
   * assertThat(1L).isNotEqualTo(-1L);
   * 
   * // assertion will fail:
   * assertThat(1L).isNotEqualTo(1L);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public SELF isNotEqualTo(long other) {
    longs.assertNotEqual(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isZero() {
    longs.assertIsZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotZero() {
    longs.assertIsNotZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isOne() {
    longs.assertIsOne(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isPositive() {
    longs.assertIsPositive(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNegative() {
    longs.assertIsNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotNegative() {
    longs.assertIsNotNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotPositive() {
    longs.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1L).isLessThan(2L);
   * assertThat(-2L).isLessThan(-1L);
   * 
   * // assertions will fail:
   * assertThat(1L).isLessThan(0L);
   * assertThat(1L).isLessThan(1L);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public SELF isLessThan(long other) {
    longs.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1L).isLessThanOrEqualTo(2L);
   * assertThat(-1L).isLessThanOrEqualTo(-2L);
   * assertThat(1L).isLessThanOrEqualTo(1L);
   * 
   * // assertions will fail:
   * assertThat(1L).isLessThanOrEqualTo(2L);
   * assertThat(-1L).isLessThanOrEqualTo(-2L);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public SELF isLessThanOrEqualTo(long other) {
    longs.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1L).isGreaterThan(0L);
   * assertThat(-1L).isGreaterThan(-2L);
   * 
   * // assertions will fail:
   * assertThat(1L).isGreaterThan(2L);
   * assertThat(1L).isGreaterThan(1L);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public SELF isGreaterThan(long other) {
    longs.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(2L).isGreaterThanOrEqualTo(1L);
   * assertThat(1L).isGreaterThanOrEqualTo(1L);
   * 
   * // assertions will fail:
   * assertThat(1L).isGreaterThanOrEqualTo(2L);
   * assertThat(-1L).isGreaterThanOrEqualTo(1L);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public SELF isGreaterThanOrEqualTo(long other) {
    longs.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isBetween(Long start, Long end) {
    longs.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isStrictlyBetween(Long start, Long end) {
    longs.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual long is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(5L).isCloseTo(7L, within(3L));
   *
   * // if difference is exactly equals to the offset, it's ok
   * assertThat(5L).isCloseTo(7L, within(2L));
   *
   * // assertion will fail
   * assertThat(5L).isCloseTo(7L, within(1L));</code></pre>
   *
   * @param expected the given long to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  public SELF isCloseTo(long expected, Offset<Long> offset) {
    longs.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual long is not close to the given one by less than the given offset.<br>
   * If the difference is equal to the offset value, the assertion fails.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(5L).isNotCloseTo(7L, byLessThan(1L));
   *
   * // assertions will fail
   * assertThat(5L).isNotCloseTo(7L, byLessThan(2L));
   * assertThat(5L).isNotCloseTo(7L, byLessThan(3L));</code></pre>
   *
   * @param expected the given long to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Long)
   * @since 2.6.0 / 3.6.0
   */
  public SELF isNotCloseTo(long expected, Offset<Long> offset) {
    longs.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual long is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(5L).isCloseTo(Long.valueOf(7L), within(3L));
   *
   * // if difference is exactly equals to the offset, it's ok
   * assertThat(5L).isCloseTo(Long.valueOf(7L), within(2L));
   *
   * // assertion will fail
   * assertThat(5L).isCloseTo(Long.valueOf(7L), within(1L));</code></pre>
   *
   * @param expected the given long to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public SELF isCloseTo(Long expected, Offset<Long> offset) {
    longs.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual long is not close to the given one by less than the given offset.<br>
   * If the difference is equal to the offset value, the assertion fails.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(5L).isNotCloseTo(Long.valueOf(7L), byLessThan(1L));
   *
   * // assertions will fail
   * assertThat(5L).isNotCloseTo(Long.valueOf(7L), byLessThan(2L));
   * assertThat(5L).isNotCloseTo(Long.valueOf(7L), byLessThan(3L));</code></pre>
   *
   * @param expected the given long to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Long)
   * @since 2.6.0 / 3.6.0
   */
  @Override
  public SELF isNotCloseTo(Long expected, Offset<Long> offset) {
    longs.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with long:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(11L).isCloseTo(Long.valueOf(10L), withinPercentage(20L));
   *
   * // if difference is exactly equals to the computed offset (1L), it's ok
   * assertThat(11L).isCloseTo(Long.valueOf(10L), withinPercentage(10L));
   *
   * // assertion will fail
   * assertThat(11L).isCloseTo(Long.valueOf(10L), withinPercentage(5L));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public SELF isCloseTo(Long expected, Percentage percentage) {
    longs.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with long:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(11L).isNotCloseTo(Long.valueOf(10L), withinPercentage(5L));
   *
   * // assertions will fail
   * assertThat(11L).isNotCloseTo(Long.valueOf(10L), withinPercentage(10L));
   * assertThat(11L).isNotCloseTo(Long.valueOf(10L), withinPercentage(20L));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @since 2.6.0 / 3.6.0
   */
  @Override
  public SELF isNotCloseTo(Long expected, Percentage percentage) {
    longs.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with long:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(11L).isCloseTo(10L, withinPercentage(20L));
   *
   * // if difference is exactly equals to the computed offset (1L), it's ok
   * assertThat(11L).isCloseTo(10L, withinPercentage(10L));
   *
   * // assertion will fail
   * assertThat(11L).isCloseTo(10L, withinPercentage(5L));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  public SELF isCloseTo(long expected, Percentage percentage) {
    longs.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with long:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(11L).isNotCloseTo(10L, withinPercentage(5L));
   *
   * // assertions will fail
   * assertThat(11L).isNotCloseTo(10L, withinPercentage(10L));
   * assertThat(11L).isNotCloseTo(10L, withinPercentage(20L));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @since 2.6.0 / 3.6.0
   */
  public SELF isNotCloseTo(long expected, Percentage percentage) {
    longs.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Long> customComparator) {
    super.usingComparator(customComparator);
    longs = new Longs(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    super.usingDefaultComparator();
    longs = Longs.instance();
    return myself;
  }
}
