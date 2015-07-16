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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Shorts;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Short}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractShortAssert<S extends AbstractShortAssert<S>> extends AbstractComparableAssert<S, Short>
    implements NumberAssert<S, Short> {

  @VisibleForTesting
  Shorts shorts = Shorts.instance();

  protected AbstractShortAssert(Short actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf(&quot;1&quot;)).isEqualTo((short) 1);
   * 
   * // assertion will fail:
   * assertThat(Short.valueOf(&quot;-1&quot;)).isEqualTo((short) 1);</code></pre>
   * </p>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public S isEqualTo(short expected) {
    shorts.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf((&quot;-1&quot;)).isNotEqualTo((short) 1);
   * 
   * // assertion will fail:
   * assertThat(Short.valueOf(&quot;1&quot;)).isNotEqualTo((short) 1);</code></pre>
   * </p>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public S isNotEqualTo(short other) {
    shorts.assertNotEqual(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isZero() {
    shorts.assertIsZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotZero() {
    shorts.assertIsNotZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isPositive() {
    shorts.assertIsPositive(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNegative() {
    shorts.assertIsNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotNegative() {
    shorts.assertIsNotNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotPositive() {
    shorts.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf(&quot;1&quot;)).isLessThan((short) 2);
   * 
   * // assertion will fail:
   * assertThat(Short.valueOf(&quot;1&quot;)).isLessThan((short) 0);
   * assertThat(Short.valueOf(&quot;1&quot;)).isLessThan((short) 1);</code></pre>
   * </p>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public S isLessThan(short other) {
    shorts.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf(&quot;1&quot;)).isLessThanOrEqualTo((short) 1);
   * 
   * // assertion will fail:
   * assertThat(Short.valueOf(&quot;1&quot;)).isLessThanOrEqualTo((short) 0);</code></pre>
   * </p>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public S isLessThanOrEqualTo(short other) {
    shorts.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf(&quot;1&quot;)).isGreaterThan((short) 0);
   * 
   * // assertions will fail:
   * assertThat(Short.valueOf(&quot;0&quot;)).isGreaterThan((short) 1);
   * assertThat(Short.valueOf(&quot;0&quot;)).isGreaterThan((short) 0);</code></pre>
   * </p>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public S isGreaterThan(short other) {
    shorts.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf(&quot;1&quot;)).isGreaterThanOrEqualTo((short) 1);
   * 
   * // assertion will fail:
   * assertThat(Short.valueOf(&quot;0&quot;)).isGreaterThanOrEqualTo((short) 1);</code></pre>
   * </p>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public S isGreaterThanOrEqualTo(short other) {
    shorts.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isBetween(Short start, Short end) {
    shorts.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isStrictlyBetween(Short start, Short end) {
    shorts.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual short is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((short)5).isCloseTo((short)7, within((short)3));
   *
   * // if difference is exactly equals to the offset, it's ok
   * assertThat((short)5).isCloseTo((short)7, within((short)2));
   *
   * // assertion will fail
   * assertThat((short)5).isCloseTo((short)7, within((short)1));</code></pre>
   *
   * @param expected the given short to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public S isCloseTo(short expected, Offset<Short> offset) {
    shorts.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual short is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((short)5).isCloseTo(Short.valueOf(7), within((short)3));
   *
   * // if difference is exactly equals to the offset, it's ok
   * assertThat((short)5).isCloseTo(Short.valueOf(7), within((short)2));
   *
   * // assertion will fail
   * assertThat((short)5).isCloseTo(Short.valueOf(7), within((short)1));</code></pre>
   *
   * @param expected the given short to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  @Override
  public S isCloseTo(Short expected, Offset<Short> offset) {
    shorts.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with short:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((short)11).isCloseTo(Short.valueOf(10), withinPercentage((short)20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat((short)11).isCloseTo(Short.valueOf(10), withinPercentage((short)10));
   *
   * // assertion will fail
   * assertThat((short)11).isCloseTo(Short.valueOf(10), withinPercentage((short)5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage between 0 and 100.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  @Override
  public S isCloseTo(Short expected, Percentage percentage) {
    shorts.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with short:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((short)11).isCloseTo((short)10, withinPercentage((short)20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat((short)11).isCloseTo((short)10, withinPercentage((short)10));
   *
   * // assertion will fail
   * assertThat((short)11).isCloseTo((short)10, withinPercentage((short)5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage between 0 and 100.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public S isCloseTo(short expected, Percentage percentage) {
    shorts.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  @Override
  public S usingComparator(Comparator<? super Short> customComparator) {
    super.usingComparator(customComparator);
    shorts = new Shorts(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public S usingDefaultComparator() {
    super.usingDefaultComparator();
    shorts = Shorts.instance();
    return myself;
  }
}
