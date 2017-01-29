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
import org.assertj.core.internal.Doubles;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Double}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author Drummond Dawson
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Ansgar Konermann
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractDoubleAssert<S extends AbstractDoubleAssert<S>> extends
    AbstractComparableAssert<S, Double> implements FloatingPointNumberAssert<S, Double> {

  @VisibleForTesting
  Doubles doubles = Doubles.instance();

  public AbstractDoubleAssert(Double actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public S isNaN() {
    doubles.assertIsNaN(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotNaN() {
    doubles.assertIsNotNaN(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isZero() {
    doubles.assertIsZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotZero() {
    doubles.assertIsNotZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isOne() {
    doubles.assertIsOne(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isPositive() {
    doubles.assertIsPositive(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNegative() {
    doubles.assertIsNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotNegative() {
    doubles.assertIsNotNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotPositive() {
    doubles.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(8.1).isCloseTo(8.0, within(0.2));
   *
   * // you can use offset if you prefer
   * assertThat(8.1).isCloseTo(8.0, offset(0.2));
   *
   * // if difference is exactly equals to 0.1, it's ok
   * assertThat(8.1).isCloseTo(8.0, within(0.1));
   *
   * // assertion will fail
   * assertThat(8.1).isCloseTo(8.0, within(0.01));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  // duplicate javadoc of isCloseTo(double other, Offset<Double> offset but can't define it in super class
  public S isCloseTo(final double expected, final Offset<Double> offset) {
    doubles.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by less than the given offset.<br>
   * If the difference is equal to the offset value, the assertion fails.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(8.1).isNotCloseTo(8.0, byLessThan(0.01));
   *
   * // you can use offset if you prefer
   * assertThat(8.1).isNotCloseTo(8.0, offset(0.01));
   *
   * // assertions will fail
   * assertThat(8.1).isNotCloseTo(8.0, byLessThan(0.1));
   * assertThat(8.1).isNotCloseTo(8.0, byLessThan(0.2));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Double)
   * @since 2.6.0 / 3.6.0
   */
  // duplicate javadoc of isNotCloseTo(double other, Offset<Double> offset but can't define it in super class
  public S isNotCloseTo(final double expected, final Offset<Double> offset) {
    doubles.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(8.1).isCloseTo(Double.valueOf(8.0), within(0.2));
   *
   * // you can use offset if you prefer
   * assertThat(8.1).isCloseTo(Double.valueOf(8.0), offset(0.2));
   *
   * // if difference is exactly equals to 0.1, it's ok
   * assertThat(8.1).isCloseTo(Double.valueOf(8.0), within(0.1));
   *
   * // assertion will fail
   * assertThat(8.1).isCloseTo(Double.valueOf(8.0), within(0.01));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public S isCloseTo(Double expected, Offset<Double> offset) {
    doubles.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one by less than the given offset.<br>
   * If the difference is equal to the offset value, the assertion fails.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(8.1).isNotCloseTo(Double.valueOf(8.0), byLessThan(0.01));
   *
   * // you can use offset if you prefer
   * assertThat(8.1).isNotCloseTo(Double.valueOf(8.0), offset(0.01));
   *
   * // assertions will fail
   * assertThat(8.1).isNotCloseTo(Double.valueOf(8.0), byLessThan(0.1));
   * assertThat(8.1).isNotCloseTo(Double.valueOf(8.0), byLessThan(0.2));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Double)
   * @since 2.6.0 / 3.6.0
   */
  @Override
  public S isNotCloseTo(Double expected, Offset<Double> offset) {
    doubles.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with double:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(11.0).isCloseTo(Double.valueOf(10.0), withinPercentage(20d));
   *
   * // if difference is exactly equals to the computed offset (1.0), it's ok
   * assertThat(11.0).isCloseTo(Double.valueOf(10.0), withinPercentage(10d));
   *
   * // assertion will fail
   * assertThat(11.0).isCloseTo(Double.valueOf(10.0), withinPercentage(5d));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */

  @Override
  public S isCloseTo(Double expected, Percentage percentage) {
    doubles.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with double:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(11.0).isNotCloseTo(Double.valueOf(10.0), withinPercentage(5d));
   *
   * // assertions will fail
   * assertThat(11.0).isNotCloseTo(Double.valueOf(10.0), withinPercentage(10d));
   * assertThat(11.0).isNotCloseTo(Double.valueOf(10.0), withinPercentage(20d));</code></pre>
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
  public S isNotCloseTo(Double expected, Percentage percentage) {
    doubles.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with double:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(11.0).isCloseTo(10.0, withinPercentage(20d));
   *
   * // if difference is exactly equals to the computed offset (1.0), it's ok
   * assertThat(11.0).isCloseTo(10.0, withinPercentage(10d));
   *
   * // assertion will fail
   * assertThat(11.0).isCloseTo(10.0, withinPercentage(5d));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  public S isCloseTo(double expected, Percentage percentage) {
    doubles.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with double:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(11.0).isNotCloseTo(10.0, withinPercentage(5d));
   *
   * // assertions will fail
   * assertThat(11.0).isNotCloseTo(10.0, withinPercentage(10d));
   * assertThat(11.0).isNotCloseTo(10.0, withinPercentage(20d));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @since 2.6.0 / 3.6.0
   */
  public S isNotCloseTo(double expected, Percentage percentage) {
    doubles.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
	 * assertThat(1.0).isEqualTo(1.0);
	 * assertThat(1D).isEqualTo(1.0);
	 * 
	 * // assertions will fail:
	 * assertThat(0.0).isEqualTo(1.0);
	 * assertThat(-1.0).isEqualTo(1.0);</code></pre>
   * </p>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public S isEqualTo(double expected) {
    doubles.assertEqual(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isEqualTo(Double expected, Offset<Double> offset) {
    doubles.assertEqual(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual value is close to the given one by less than the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example with double:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(8.1).isEqualTo(8.0, offset(0.2));
   *
   * // if difference is exactly equals to the offset (0.1), it's ok
   * assertThat(8.1).isEqualTo(8.0, offset(0.1));
   *
   * // within is an alias of offset
   * assertThat(8.1).isEqualTo(8.0, within(0.1));
   *
   * // assertion will fail
   * assertThat(8.1).isEqualTo(8.0, offset(0.01));</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public S isEqualTo(double expected, Offset<Double> offset) {
    doubles.assertEqual(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
	 * assertThat(0.0).isNotEqualTo(1.0);
	 * assertThat(-1.0).isNotEqualTo(1.0);
	 * 
	 * // assertions will fail:
	 * assertThat(1.0).isNotEqualTo(1.0);
	 * assertThat(1D).isNotEqualTo(1.0);</code></pre>
   * </p>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public S isNotEqualTo(double other) {
    doubles.assertNotEqual(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
	 * assertThat(1.0).isLessThan(2.0);
	 * 
	 * // assertions will fail:
	 * assertThat(2.0).isLessThan(1.0);
	 * assertThat(1.0).isLessThan(1.0);</code></pre>
   * </p>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public S isLessThan(double other) {
    doubles.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
	 * assertThat(-1.0).isLessThanOrEqualTo(1.0);
	 * assertThat(1.0).isLessThanOrEqualTo(1.0);
	 * 
	 * // assertion will fail:
	 * assertThat(2.0).isLessThanOrEqualTo(1.0);</code></pre>
   * </p>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public S isLessThanOrEqualTo(double other) {
    doubles.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
	 * assertThat(2.0).isGreaterThan(1.0);
	 * 
	 * // assertions will fail:
	 * assertThat(1.0).isGreaterThan(1.0);
	 * assertThat(1.0).isGreaterThan(2.0);</code></pre>
   * </p>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public S isGreaterThan(double other) {
    doubles.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
	 * assertThat(2.0).isGreaterThanOrEqualTo(1.0);
	 * assertThat(1.0).isGreaterThanOrEqualTo(1.0);
	 * 
	 * // assertion will fail:
	 * assertThat(1.0).isGreaterThanOrEqualTo(2.0);</code></pre>
   * </p>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public S isGreaterThanOrEqualTo(double other) {
    doubles.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isBetween(Double start, Double end) {
    doubles.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isStrictlyBetween(Double start, Double end) {
    doubles.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  @Override
  public S usingComparator(Comparator<? super Double> customComparator) {
    super.usingComparator(customComparator);
    doubles = new Doubles(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public S usingDefaultComparator() {
    super.usingDefaultComparator();
    doubles = Doubles.instance();
    return myself;
  }
}
