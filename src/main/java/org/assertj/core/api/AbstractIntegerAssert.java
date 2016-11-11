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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Integers;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Integer}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * 
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractIntegerAssert<S extends AbstractIntegerAssert<S>> extends
    AbstractComparableAssert<S, Integer> implements NumberAssert<S, Integer> {

  @VisibleForTesting
  Integers integers = Integers.instance();

  public AbstractIntegerAssert(Integer actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1).isEqualTo(1);
   * assertThat(-1).isEqualTo(-1);
   * 
   * // assertions will fail:
   * assertThat(1).isEqualTo(2);
   * assertThat(1).isEqualTo(-1);</code></pre>
   * </p>
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public S isEqualTo(int expected) {
    integers.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1).isNotEqualTo(2);
   * assertThat(1).isNotEqualTo(-1);
   * 
   * // assertion will fail:
   * assertThat(1).isNotEqualTo(1);</code></pre>
   * </p>
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public S isNotEqualTo(int other) {
    integers.assertNotEqual(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isZero() {
    integers.assertIsZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotZero() {
    integers.assertIsNotZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isPositive() {
    integers.assertIsPositive(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNegative() {
    integers.assertIsNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotNegative() {
    integers.assertIsNotNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotPositive() {
    integers.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1).isLessThan(2);
   * assertThat(-2).isLessThan(-1);
   * 
   * // assertions will fail:
   * assertThat(1).isLessThan(0);
   * assertThat(1).isLessThan(1);</code></pre>
   * </p>
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public S isLessThan(int other) {
    integers.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1).isLessThanOrEqualTo(2);
   * assertThat(-1).isLessThanOrEqualTo(-2);
   * assertThat(1).isLessThanOrEqualTo(1);
   * 
   * // assertions will fail:
   * assertThat(1).isLessThanOrEqualTo(2);
   * assertThat(-1).isLessThanOrEqualTo(-2);</code></pre>
   * </p>
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public S isLessThanOrEqualTo(int other) {
    integers.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1).isGreaterThan(0);
   * assertThat(-1).isGreaterThan(-2);
   * 
   * // assertions will fail:
   * assertThat(1).isGreaterThan(2);
   * assertThat(1).isGreaterThan(1);</code></pre>
   * </p>
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public S isGreaterThan(int other) {
    integers.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(2).isGreaterThanOrEqualTo(1);
   * assertThat(1).isGreaterThanOrEqualTo(1);
   * 
   * // assertions will fail:
   * assertThat(1).isGreaterThanOrEqualTo(2);
   * assertThat(-1).isGreaterThanOrEqualTo(1);</code></pre>
   * </p>
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public S isGreaterThanOrEqualTo(int other) {
    integers.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S isBetween(Integer start, Integer end) {
    integers.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S isStrictlyBetween(Integer start, Integer end) {
    integers.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual int is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(5).isCloseTo(7, within(3));
   *
   * // if difference is exactly equals to the offset, it's ok
   * assertThat(5).isCloseTo(7, within(2));
   *
   * // assertion will fail
   * assertThat(5).isCloseTo(7, within(1));</code></pre>
   *
   * @param expected the given int to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  public S isCloseTo(int expected, Offset<Integer> offset) {
    integers.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual int is not close to the given one by less than the given offset.<br>
   * If the difference is equal to the offset value, the assertion fails.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(5).isNotCloseTo(7, byLessThan(1));
   *
   * // assertions will fail
   * assertThat(5).isNotCloseTo(7, byLessThan(2));
   * assertThat(5).isNotCloseTo(7, byLessThan(3));</code></pre>
   *
   * @param expected the given int to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Integer)
   * @since 2.6.0 / 3.6.0
   */
  public S isNotCloseTo(int expected, Offset<Integer> offset) {
    integers.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual int is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(5).isCloseTo(Integer.valueOf(7), within(3));
   *
   * // if difference is exactly equals to the offset, it's ok
   * assertThat(5).isCloseTo(Integer.valueOf(7), within(2));
   *
   * // assertion will fail
   * assertThat(5).isCloseTo(Integer.valueOf(7), within(1));</code></pre>
   *
   * @param expected the given int to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public S isCloseTo(Integer expected, Offset<Integer> offset) {
    integers.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual int is not close to the given one by less than the given offset.<br>
   * If the difference is equal to the offset value, the assertion fails.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(5).isNotCloseTo(Integer.valueOf(7), byLessThan(1));
   *
   * // assertions will fail
   * assertThat(5).isNotCloseTo(Integer.valueOf(7), byLessThan(2));
   * assertThat(5).isNotCloseTo(Integer.valueOf(7), byLessThan(3));</code></pre>
   *
   * @param expected the given int to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Integer)
   * @since 2.6.0 / 3.6.0
   */
  @Override
  public S isNotCloseTo(Integer expected, Offset<Integer> offset) {
    integers.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with integer:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(11).isCloseTo(Integer.valueOf(10), withinPercentage(20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat(11).isCloseTo(Integer.valueOf(10), withinPercentage(10));
   *
   * // assertion will fail
   * assertThat(11).isCloseTo(Integer.valueOf(10), withinPercentage(5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  @Override
  public S isCloseTo(Integer expected, Percentage percentage) {
    integers.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with integer:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(11).isNotCloseTo(Integer.valueOf(10), withinPercentage(5));
   *
   * // assertions will fail
   * assertThat(11).isNotCloseTo(Integer.valueOf(10), withinPercentage(10));
   * assertThat(11).isNotCloseTo(Integer.valueOf(10), withinPercentage(20));</code></pre>
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
  public S isNotCloseTo(Integer expected, Percentage percentage) {
    integers.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with integer:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(11).isCloseTo(10, withinPercentage(20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat(11).isCloseTo(10, withinPercentage(10));
   *
   * // assertion will fail
   * assertThat(11).isCloseTo(10, withinPercentage(5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close enough to the given one.
   */
  public S isCloseTo(int expected, Percentage percentage) {
    integers.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with integer:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(11).isNotCloseTo(10, withinPercentage(5));
   *
   * // assertions will fail
   * assertThat(11).isNotCloseTo(10, withinPercentage(10));
   * assertThat(11).isNotCloseTo(10, withinPercentage(20));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@link Percentage} is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @since 2.6.0 / 3.6.0
   */
  public S isNotCloseTo(int expected, Percentage percentage) {
    integers.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  @Override
  public S usingComparator(Comparator<? super Integer> customComparator) {
    super.usingComparator(customComparator);
    integers = new Integers(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public S usingDefaultComparator() {
    super.usingDefaultComparator();
    integers = Integers.instance();
    return myself;
  }
}
