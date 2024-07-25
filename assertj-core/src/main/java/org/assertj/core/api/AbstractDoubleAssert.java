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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;

import java.util.Comparator;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Doubles;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Double}s.
 *
 * @author Drummond Dawson
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Ansgar Konermann
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 * @author Jack Gough
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 */
public abstract class AbstractDoubleAssert<SELF extends AbstractDoubleAssert<SELF>> extends
    AbstractComparableAssert<SELF, Double> implements FloatingPointNumberAssert<SELF, Double> {

  private static final Double NEGATIVE_ZERO = -0.0;

  @VisibleForTesting
  Doubles doubles = Doubles.instance();

  private final boolean isPrimitive;

  protected AbstractDoubleAssert(Double actual, Class<?> selfType) {
    super(actual, selfType);
    this.isPrimitive = false;
  }

  public AbstractDoubleAssert(double actual, Class<?> selfType) {
    super(actual, selfType);
    this.isPrimitive = true;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNaN() {
    doubles.assertIsNaN(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotNaN() {
    doubles.assertIsNotNaN(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is equal to zero.
   * <p>
   * Although {@code 0.0 == -0.0} (primitives), {@code Double(-0.0)} is not zero as {@code Double.doubleToRawLongBits(0.0) == Double.doubleToRawLongBits(-0.0)} is false.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(0.0).isZero();
   * assertThat(-0.0).isZero();
   *
   * // assertions will fail
   * assertThat(new Double(-0.0)).isZero();
   * assertThat(3.142).isZero();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  @Override
  public SELF isZero() {
    if (isPrimitive) assertIsPrimitiveZero();
    else doubles.assertIsZero(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to zero.
   * <p>
   * Although {@code 0.0 == -0.0} (primitives), {@code Double(-0.0)} is not zero as {@code Double.doubleToRawLongBits(0.0) == Double.doubleToRawLongBits(-0.0)} is false.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(3.142).isNotZero();
   * assertThat(new Double(-0.0)).isNotZero();
   *
   * // assertions will fail
   * assertThat(0.0).isNotZero();
   * assertThat(new Double(0.0)).isNotZero();
   * assertThat(-0.0).isNotZero();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  @Override
  public SELF isNotZero() {
    if (isPrimitive) assertIsPrimitiveNonZero();
    else if (NEGATIVE_ZERO.equals(actual)) return myself;
    else doubles.assertIsNotZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isOne() {
    doubles.assertIsOne(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isPositive() {
    doubles.assertIsPositive(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNegative() {
    doubles.assertIsNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotNegative() {
    doubles.assertIsNotNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotPositive() {
    doubles.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Double)} or {@link Assertions#offset(Double)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Double)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Double)} implies a <b>strict</b> comparison,
   * use {@link Assertions#within(Double)} to get the old behavior.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(8.1).isCloseTo(8.0, within(0.2));
   * assertThat(8.1).isCloseTo(8.0, offset(0.2)); // alias of within
   * assertThat(8.1).isCloseTo(8.0, byLessThan(0.2)); // strict
   *
   * // assertions succeed when the difference == offset value ...
   * assertThat(8.1).isCloseTo(8.0, within(0.1));
   * assertThat(8.1).isCloseTo(8.0, offset(0.1));
   * // ... except when using byLessThan which implies a strict comparison
   * assertThat(0.1).isCloseTo(0.0, byLessThan(0.1)); // strict =&gt; fail
   *
   * // this assertion also fails
   * assertThat(8.1).isCloseTo(8.0, within(0.001));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  // duplicate javadoc of isCloseTo(double other, Offset<Double> offset but can't define it in super class
  public SELF isCloseTo(final double expected, final Offset<Double> offset) {
    doubles.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by less than the given offset.<br>
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#byLessThan(Double)} or {@link Offset#strictOffset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#within(Double)} or {@link Assertions#offset(Double)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Double)} implies a <b>strict</b> comparison,
   * use {@link Assertions#within(Double)} to get the old behavior.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(8.1).isNotCloseTo(8.0, byLessThan(0.01));
   * assertThat(8.1).isNotCloseTo(8.0, within(0.01));
   * assertThat(8.1).isNotCloseTo(8.0, offset(0.01));
   * // diff == offset but isNotCloseTo succeeds as we use byLessThan
   * assertThat(0.1).isNotCloseTo(0.0, byLessThan(0.1));
   *
   * // assertions fail
   * assertThat(0.1).isNotCloseTo(0.0, within(0.1));
   * assertThat(0.1).isNotCloseTo(0.0, offset(0.1));
   * assertThat(8.1).isNotCloseTo(8.0, within(0.2));
   * assertThat(8.1).isNotCloseTo(8.0, offset(0.2));
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
  public SELF isNotCloseTo(final double expected, final Offset<Double> offset) {
    doubles.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Double)} or {@link Assertions#offset(Double)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Double)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Double)} implies a <b>strict</b> comparison,
   * use {@link Assertions#within(Double)} to get the old behavior.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(8.1).isCloseTo(8.0, within(0.2));
   * assertThat(8.1).isCloseTo(8.0, offset(0.2)); // alias of within
   * assertThat(8.1).isCloseTo(8.0, byLessThan(0.2)); // strict
   *
   * // assertions succeed when the difference == offset value ...
   * assertThat(8.1).isCloseTo(8.0, within(0.1));
   * assertThat(8.1).isCloseTo(8.0, offset(0.1));
   * // ... except when using byLessThan which implies a strict comparison
   * assertThat(0.1).isCloseTo(0.0, byLessThan(0.1)); // strict =&gt; fail
   *
   * // this assertion also fails
   * assertThat(8.1).isCloseTo(8.0, within(0.001));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public SELF isCloseTo(Double expected, Offset<Double> offset) {
    doubles.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by less than the given offset.<br>
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#byLessThan(Double)} or {@link Offset#strictOffset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#within(Double)} or {@link Assertions#offset(Double)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Double)} implies a <b>strict</b> comparison,
   * use {@link Assertions#within(Double)} to get the old behavior.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(8.1).isNotCloseTo(8.0, byLessThan(0.01));
   * assertThat(8.1).isNotCloseTo(8.0, within(0.01));
   * assertThat(8.1).isNotCloseTo(8.0, offset(0.01));
   * // diff == offset but isNotCloseTo succeeds as we use byLessThan
   * assertThat(0.1).isNotCloseTo(0.0, byLessThan(0.1));
   *
   * // assertions fail
   * assertThat(8.1).isNotCloseTo(8.0, within(0.1));
   * assertThat(8.1).isNotCloseTo(8.0, offset(0.1));
   * assertThat(8.1).isNotCloseTo(8.0, within(0.2));
   * assertThat(8.1).isNotCloseTo(8.0, offset(0.2));
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
  @Override
  public SELF isNotCloseTo(Double expected, Offset<Double> offset) {
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
  public SELF isCloseTo(Double expected, Percentage percentage) {
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
  public SELF isNotCloseTo(Double expected, Percentage percentage) {
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
  public SELF isCloseTo(double expected, Percentage percentage) {
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
  public SELF isNotCloseTo(double expected, Percentage percentage) {
    doubles.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Unless a specific comparator has been set (with {@link #usingComparator(Comparator) usingComparator}) the equality is performed
   * with {@code ==} which is slightly different from {@link Double#equals(Object)} - notably:
   * <ul>
   * <li>{@code 0.0 == -0.0} but {@code Double.valueOf(0.0).equals(-0.0) == false}</li>
   * <li>{@code Double.NaN != Double.NaN} but {@code Double.valueOf(Double.NaN).equals(Double.NaN) == true}</li>
   * </ul>
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1.0).isEqualTo(1.0);
   * assertThat(1D).isEqualTo(1.0);
   * assertThat(-0.0).isEqualTo(0.0);
   *
   * // assertions will fail:
   * assertThat(0.0).isEqualTo(1.0);
   * assertThat(Double.NaN).isEqualTo(Double.NaN);
   * assertThat(-1.0).isEqualTo(1.0);</code></pre>
   * <p>
   * Note that this assertion behaves slightly differently from {@link #isEqualTo(Double)}.
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(double expected) {
    if (noCustomComparatorSet()) {
      // check for null first to avoid casting a null to primitive
      isNotNull();
      // use primitive comparison since the parameter is a primitive.
      if (expected == actual) return myself;
      // At this point we know that the assertion failed, if actual and expected are Double.NaN we want to
      // give a clear error message (we need to use equals to check that as Double.NaN != Double.NaN)
      if (Double.valueOf(expected).equals(Double.NaN) && actual.equals(Double.NaN))
        throw new AssertionError("Actual and expected values were compared with == because expected was a primitive double, the assertion failed as both were Double.NaN and Double.NaN != Double.NaN (as per Double#equals javadoc)");
      // standard error message
      throw Failures.instance().failure(info, shouldBeEqual(actual, expected, info.representation()));
    }
    // doubles.assertEqual honors the custom comparator
    doubles.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual value is equal to the given one using {@link Double#equals(Object)} semantics where:
   * <ul>
   * <li>{@code Double.valueOf(Double.NaN).equals(Double.NaN) == true} but {@code Double.NaN != Double.NaN}</li>
   * <li>{@code Double.valueOf(0.0).equals(-0.0) == false} but {@code 0.0 == -0.0}</li>
   * </ul>
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1.0).isEqualTo(Double.valueOf(1.0));
   * assertThat(1D).isEqualTo(Double.valueOf(1.0));
   * assertThat(Double.NaN).isEqualTo(Double.valueOf(Double.NaN));
   *
   * // assertions will fail:
   * assertThat(0.0).isEqualTo(Double.valueOf(1.0));
   * assertThat(-1.0).isEqualTo(Double.valueOf(1.0));
   * assertThat(-0.0).isEqualTo(Double.valueOf(0.0));</code></pre>
   * <p>
   * Note that this assertion behaves slightly differently from {@link #isEqualTo(double)}.
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(Double expected) {
    // overloaded for javadoc
    return super.isEqualTo(expected);
  }

  /** {@inheritDoc} */
  @Override
  public SELF isEqualTo(Double expected, Offset<Double> offset) {
    return isCloseTo(expected, offset);
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * This assertion is the same as {@link #isCloseTo(double, Offset)}.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Double)} or {@link Assertions#offset(Double)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Double)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(8.1).isEqualTo(8.0, within(0.2));
   * assertThat(8.1).isEqualTo(8.0, offset(0.2)); // alias of within
   * assertThat(8.1).isEqualTo(8.0, byLessThan(0.2)); // strict
   *
   * // assertions succeed when the difference == offset value ...
   * assertThat(8.1).isEqualTo(8.0, within(0.1));
   * assertThat(8.1).isEqualTo(8.0, offset(0.1));
   * // ... except when using byLessThan which implies a strict comparison
   * assertThat(0.1).isEqualTo(0.0, byLessThan(0.1)); // strict =&gt; fail
   *
   * // this assertion also fails
   * assertThat(8.1).isEqualTo(8.0, within(0.001));</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(double expected, Offset<Double> offset) {
    return isCloseTo(expected, offset);
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Unless a specific comparator has been set (with {@link #usingComparator(Comparator) usingComparator}) the equality is performed
   * with {@code !=} which is slightly different from {@link Double#equals(Object)} - notably:
   * <ul>
   * <li>{@code Double.NaN != Double.NaN} but {@code Double.valueOf(Double.NaN).equals(Double.NaN) == true}</li>
   * <li>{@code 0.0 == -0.0} but {@code Double.valueOf(0.0).equals(-0.0) == false}</li>
   * </ul>
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(0.0).isNotEqualTo(1.0);
   * assertThat(-1.0).isNotEqualTo(1.0);
   * assertThat(Double.NaN).isNotEqualTo(Double.NaN);
   *
   * // assertions will fail:
   * assertThat(1.0).isNotEqualTo(1.0);
   * assertThat(0.0).isNotEqualTo(-0.0);
   * assertThat(1D).isNotEqualTo(1.0);</code></pre>
   * <p>
   * Note that this assertion behaves slightly differently from {@link #isNotEqualTo(Double)}.
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is == to the given one.
   */
  public SELF isNotEqualTo(double other) {
    if (noCustomComparatorSet()) {
      // check for null first to avoid casting a null to primitive
      isNotNull();
      // use primitive comparison since the parameter is a primitive.
      if (other != actual) return myself;
      throw Failures.instance().failure(info, shouldNotBeEqual(actual, other));
    }
    doubles.assertNotEqual(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given {@link Double} using {@link Double#equals(Object)} semantics where
   * <ul>
   * <li>{@code Double.valueOf(Double.NaN).equals(Double.NaN) == true} but {@code Double.NaN != Double.NaN}</li>
   * <li>{@code Double.valueOf(0.0).equals(-0.0) == false} but {@code 0.0 == -0.0}</li>
   * </ul>
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(0.0).isNotEqualTo(Double.valueOf(1.0));
   * assertThat(-1.0).isNotEqualTo(Double.valueOf(1.0));
   * assertThat(0.0).isNotEqualTo(Double.valueOf(-0.0));
   *
   * // assertions will fail:
   * assertThat(1.0).isNotEqualTo(Double.valueOf(1.0));
   * assertThat(0.0).isNotEqualTo(Double.valueOf(0.0));
   * assertThat(Double.NaN).isNotEqualTo(Double.valueOf(Double.NaN));</code></pre>
   * <p>
   * Note that this assertion behaves slightly differently from {@link #isNotEqualTo(double)}.
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public SELF isNotEqualTo(Double other) {
    // overloaded for javadoc
    return super.isNotEqualTo(other);
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(1.0).isLessThan(2.0);
   *
   * // assertions will fail:
   * assertThat(2.0).isLessThan(1.0);
   * assertThat(1.0).isLessThan(1.0);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public SELF isLessThan(double other) {
    doubles.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Unless a specific comparator has been set (with {@link #usingComparator(Comparator) usingComparator})
   * this assertion will use {@code <=} semantics where notably {@code 0.0} == {@code -0.0}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(-1.0).isLessThanOrEqualTo(1.0);
   * assertThat(1.0).isLessThanOrEqualTo(1.0);
   * // 0.0 == -0.0
   * assertThat(-0.0).isLessThanOrEqualTo(0.0);
   * assertThat(0.0).isLessThanOrEqualTo(-0.0);
   *
   * // assertion will fail:
   * assertThat(2.0).isLessThanOrEqualTo(1.0);</code></pre>
   * <p>
   * Note that this assertion behaves differently from {@link #isLessThanOrEqualTo(Double)} which uses {@link Double#compareTo(Double)} semantics.
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public SELF isLessThanOrEqualTo(double other) {
    if (noCustomComparatorSet()) {
      // use primitive comparison since the parameter is a primitive.
      if (actual <= other) return myself;
      throw Failures.instance().failure(info, shouldBeLessOrEqual(actual, other));
    }
    doubles.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one using {@link Double#compareTo(Double)} semantics where notably {@code -0.0} is <b>strictly</b> less than {@code 0.0}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(-1.0).isLessThanOrEqualTo(Double.valueOf(1.0));
   * assertThat(1.0).isLessThanOrEqualTo(Double.valueOf(1.0));
   * assertThat(-0.0).isLessThanOrEqualTo(Double.valueOf(0.0));
   *
   * // assertions will fail:
   * assertThat(2.0).isLessThanOrEqualTo(Double.valueOf(1.0));
   * // 0.0 is not considered equal to -0.0
   * assertThat(0.0).isLessThanOrEqualTo(Double.valueOf(-0.0));</code></pre>
   * <p>
   * Note that this assertion behaves differently from {@link #isLessThanOrEqualTo(double)} which uses {@link Double#compareTo(Double)} semantics.
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  @Override
  public SELF isLessThanOrEqualTo(Double other) {
    // overridden for javadoc
    return super.isLessThanOrEqualTo(other);
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(2.0).isGreaterThan(1.0);
   *
   * // assertions will fail:
   * assertThat(1.0).isGreaterThan(1.0);
   * assertThat(1.0).isGreaterThan(2.0);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public SELF isGreaterThan(double other) {
    doubles.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Unless a specific comparator has been set (with {@link #usingComparator(Comparator) usingComparator})
   * this assertion will use {@code >=} semantics where notably {@code 0.0} == {@code -0.0}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(2.0).isGreaterThanOrEqualTo(1.0);
   * assertThat(1.0).isGreaterThanOrEqualTo(1.0);
   * // 0.0 == -0.0
   * assertThat(-0.0).isGreaterThanOrEqualTo(0.0);
   * assertThat(0.0).isGreaterThanOrEqualTo(-0.0);
   *
   * // assertion will fail:
   * assertThat(1.0).isGreaterThanOrEqualTo(2.0);</code></pre>
   * <p>
   * Note that this assertion behaves differently from {@link #isGreaterThanOrEqualTo(Double)} which uses {@link Double#compareTo(Double)} semantics.
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public SELF isGreaterThanOrEqualTo(double other) {
    if (noCustomComparatorSet()) {
      // use primitive comparison since the parameter is a primitive.
      if (actual >= other) return myself;
      throw Failures.instance().failure(info, shouldBeGreaterOrEqual(actual, other));
    }
    doubles.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one using {@link Double#compareTo(Double)} semantics where notably {@code 0.0} is <b>strictly</b> greater than {@code -0.0}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(2.0).isGreaterThanOrEqualTo(Double.valueOf(1.0));
   * assertThat(1.0).isGreaterThanOrEqualTo(Double.valueOf(1.0));
   * assertThat(0.0).isGreaterThanOrEqualTo(Double.valueOf(-0.0));
   *
   * // assertions will fail:
   * assertThat(1.0).isGreaterThanOrEqualTo(Double.valueOf(2.0));
   * // 0.0 is not considered equal to -0.0
   * assertThat(-0.0).isGreaterThanOrEqualTo(Double.valueOf(0.0));</code></pre>
   * <p>
   * Note that this assertion behaves differently from {@link #isGreaterThanOrEqualTo(double)} which uses {@code >=} semantics.
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  @Override
  public SELF isGreaterThanOrEqualTo(Double other) {
    // overridden for javadoc
    return super.isGreaterThanOrEqualTo(other);
  }

  /**
   * Verifies that the actual value is in [start, end] range (start included, end included).
   *
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(1d).isBetween(-1d, 2d);
   * assertThat(1d).isBetween(1d, 2d);
   * assertThat(1d).isBetween(0d, 1d);
   *
   * // assertion will fail
   * assertThat(1d).isBetween(2d, 3d);</code></pre>
   */
  @Override
  public SELF isBetween(Double start, Double end) {
    doubles.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   *
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion will pass
   * assertThat(1d).isStrictlyBetween(-1d, 2d);
   *
   * // assertions will fail
   * assertThat(1d).isStrictlyBetween(1d, 2d);
   * assertThat(1d).isStrictlyBetween(0d, 1d);
   * assertThat(1d).isStrictlyBetween(2d, 3d);</code></pre>
   *
   */
  @Override
  public SELF isStrictlyBetween(Double start, Double end) {
    doubles.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Double> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Double> customComparator, String customComparatorDescription) {
    doubles = new Doubles(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    doubles = Doubles.instance();
    return super.usingDefaultComparator();
  }

  private void assertIsPrimitiveZero() {
    if (actual == 0.0) return;
    throw Failures.instance().failure(info, shouldBeEqual(actual, 0.0, info.representation()));
  }

  private void assertIsPrimitiveNonZero() {
    if (actual != 0.0) return;
    throw Failures.instance().failure(info, shouldNotBeEqual(actual, 0.0));
  }

  private boolean noCustomComparatorSet() {
    return doubles.getComparator() == null;
  }

  /**
   * Verifies that the double value is a finite floating-point value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(1.0d).isFinite();
   *
   * // assertions fail
   * assertThat(Double.NaN).isFinite();
   * assertThat(Double.NEGATIVE_INFINITY).isFinite();
   * assertThat(Double.POSITIVE_INFINITY).isFinite();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is not a finite floating-point value.
   * @throws AssertionError if the actual value is null.
   * @see #isNotFinite()
   * @see #isInfinite()
   * @see #isNaN()
   * @see java.lang.Double#isFinite(double)
   * @since 3.19.0
   */
  @Override
  public SELF isFinite() {
    doubles.assertIsFinite(info, actual);
    return myself;
  }

  /**
   * Verifies that the double value is not a finite floating-point value.
   * <p>
   * Note that 'not finite' is not equivalent to infinite as `NaN` is neither finite or infinite.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(Double.POSITIVE_INFINITY).isNotFinite();
   * assertThat(Double.NEGATIVE_INFINITY).isNotFinite();
   * assertThat(Double.NaN).isNotFinite();
   *
   * // assertion fails
   * assertThat(1.0).isNotFinite();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is a finite floating-point value.
   * @throws AssertionError if the actual value is null.
   * @see #isFinite()
   * @see #isInfinite()
   * @see #isNaN()
   * @see java.lang.Double#isFinite(double)
   * @since 3.20.0
   */
  @Override
  public SELF isNotFinite() {
    doubles.assertIsNotFinite(info, actual);
    return myself;
  }

  /**
   * Verifies that the double value represents positive infinity or negative infinity.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(Double.NEGATIVE_INFINITY).isInfinite();
   * assertThat(Double.POSITIVE_INFINITY).isInfinite();
   *
   * // assertions fail
   * assertThat(1.0d).isInfinite();
   * assertThat(Double.NaN).isInfinite();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value represents neither positive infinity nor negative infinity.
   * @throws AssertionError if the actual value is null.
   * @see #isNotInfinite()
   * @see #isFinite()
   * @see #isNaN()
   * @see java.lang.Double#isInfinite(double)
   * @since 3.19.0
   */
  @Override
  public SELF isInfinite() {
    doubles.assertIsInfinite(info, actual);
    return myself;
  }

  /**
   * Verifies that the double value represents neither positive infinity nor negative infinity.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1.0).isNotInfinite();
   * assertThat(Double.NaN).isNotInfinite();
   *
   * // assertions fail
   * assertThat(Double.POSITIVE_INFINITY).isNotInfinite();
   * assertThat(Double.NEGATIVE_INFINITY).isNotInfinite();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value represents positive infinity or negative infinity.
   * @throws AssertionError if the actual value is null.
   * @see #isInfinite()
   * @see #isFinite()
   * @see #isNaN()
   * @see java.lang.Double#isInfinite(double)
   * @since 3.20.0
   */
  @Override
  public SELF isNotInfinite() {
    doubles.assertIsNotInfinite(info, actual);
    return myself;
  }
}
