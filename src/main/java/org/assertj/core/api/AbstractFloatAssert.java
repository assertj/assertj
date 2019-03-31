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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;

import java.util.Comparator;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Floats;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Float}s.
 * 
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author Drummond Dawson
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Ansgar Konermann
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractFloatAssert<SELF extends AbstractFloatAssert<SELF>> extends AbstractComparableAssert<SELF, Float>
    implements FloatingPointNumberAssert<SELF, Float> {

  private static final Float NEGATIVE_ZERO = Float.valueOf(-0.0f);

  @VisibleForTesting
  Floats floats = Floats.instance();

  private boolean isPrimitive;

  public AbstractFloatAssert(Float actual, Class<?> selfType) {
    super(actual, selfType);
    this.isPrimitive = false;
  }

  public AbstractFloatAssert(float actual, Class<?> selfType) {
    super(actual, selfType);
    this.isPrimitive = true;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNaN() {
    floats.assertIsNaN(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotNaN() {
    floats.assertIsNotNaN(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is equal to zero.
   * <p>
   * Although {@code 0.0f == -0.0f} (primitives), {@code Float(-0.0f)} is not zero as {@code Float.floatToIntBits(0.0f) == Float.floatToIntBits(-0.0f)} is false.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(0.0f).isZero();
   * assertThat(-0.0f).isZero();
   *
   * // assertions will fail
   * assertThat(new Float(-0.0)).isZero();
   * assertThat(3.142f).isZero();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  @Override
  public SELF isZero() {
    if (isPrimitive) assertIsPrimitiveZero();
    else floats.assertIsZero(info, actual);
    return myself;
  }

  private void assertIsPrimitiveZero() {
    if (actual.floatValue() == 0.0f) return;
    throw Failures.instance().failure(info, shouldBeEqual(actual, 0.0f, info.representation()));
  }

  private void assertIsPrimitiveNonZero() {
    if (actual.floatValue() != 0.0) return;
    throw Failures.instance().failure(info, shouldNotBeEqual(actual, 0.0));
  }

  /**
   * Verifies that the actual value is not equal to zero.
   * <p>
   * Although {@code 0.0f == -0.0f} (primitives), {@code Float(-0.0f)} is not zero as {@code Float.floatToIntBits(0.0f) == Float.floatToIntBits(-0.0f)} is false.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(3.142f).isNotZero();
   * assertThat(new Float(-0.0f)).isNotZero();
   *
   * // assertions will fail
   * assertThat(0.0f).isNotZero();
   * assertThat(new Float(0.0f)).isNotZero();
   * assertThat(-0.0f).isNotZero();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  @Override
  public SELF isNotZero() {
    if (isPrimitive) assertIsPrimitiveNonZero();
    else if (NEGATIVE_ZERO.equals(actual)) return myself;
    else floats.assertIsNotZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isOne() {
    floats.assertIsOne(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isPositive() {
    floats.assertIsPositive(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNegative() {
    floats.assertIsNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotNegative() {
    floats.assertIsNotNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotPositive() {
    floats.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1.0f).isEqualTo(1.0f);
   * assertThat(1f).isEqualTo(1.0f);
   * 
   * // assertions will fail:
   * assertThat(0.0f).isEqualTo(1.0f);
   * assertThat(-1.0f).isEqualTo(1.0f);</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(float expected) {
    floats.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion: 
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Float)} or {@link Assertions#offset(Float)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Float)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Float)} implies a <b>strict</b> comparison, 
   * use {@link Assertions#within(Float)} to get the old behavior. 
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(8.1f).isCloseTo(8.0f, within(0.2f));
   * assertThat(8.1f).isCloseTo(8.0f, offset(0.2f)); // alias of within 
   * assertThat(8.1f).isCloseTo(8.0f, byLessThan(0.2f)); // strict
   *
   * // assertions succeed when the difference == offset value ...  
   * assertThat(0.1f).isCloseTo(0.0f, within(0.1f));
   * assertThat(0.1f).isCloseTo(0.0f, offset(0.1f));
   * // ... except when using byLessThan which implies a strict comparison
   * assertThat(0.1f).isCloseTo(0.0f, byLessThan(0.1f)); // strict =&gt; fail
   * 
   * // this assertion also fails
   * assertThat(8.1f).isCloseTo(8.0f, within(0.001f));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  // duplicate javadoc of isCloseTo(Float other, Offset<Float> offset but can't define it in super class
  public SELF isCloseTo(final float expected, final Offset<Float> offset) {
    floats.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by less than the given offset.<br>
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion: 
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#byLessThan(Float)} or {@link Offset#strictOffset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#within(Float)} or {@link Assertions#offset(Float)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Float)} implies a <b>strict</b> comparison, 
   * use {@link Assertions#within(Float)} to get the old behavior. 
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(8.1f).isNotCloseTo(8.0f, byLessThan(0.01f));
   * assertThat(8.1f).isNotCloseTo(8.0f, within(0.01f));
   * assertThat(8.1f).isNotCloseTo(8.0f, offset(0.01f));
   * // diff == offset but isNotCloseTo succeeds as we use byLessThan
   * assertThat(0.1f).isNotCloseTo(0.0f, byLessThan(0.1f));   
   *
   * // assertions fail
   * assertThat(0.1f).isNotCloseTo(0.0f, within(0.1f));
   * assertThat(0.1f).isNotCloseTo(0.0f, offset(0.1f));
   * assertThat(8.1f).isNotCloseTo(8.0f, within(0.2f));
   * assertThat(8.1f).isNotCloseTo(8.0f, offset(0.2f));
   * assertThat(8.1f).isNotCloseTo(8.0f, byLessThan(0.2f));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Float)
   * @since 2.6.0 / 3.6.0
   */
  // duplicate javadoc of isNotCloseTo(Float other, Offset<Float> offset but can't define it in super class
  public SELF isNotCloseTo(final float expected, final Offset<Float> offset) {
    floats.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion: 
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Float)} or {@link Assertions#offset(Float)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Float)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Float)} implies a <b>strict</b> comparison, 
   * use {@link Assertions#within(Float)} to get the old behavior. 
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(8.1f).isCloseTo(8.0f, within(0.2f));
   * assertThat(8.1f).isCloseTo(8.0f, offset(0.2f)); // alias of within 
   * assertThat(8.1f).isCloseTo(8.0f, byLessThan(0.2f)); // strict
   *
   * // assertions succeed when the difference == offset value ...  
   * assertThat(0.1f).isCloseTo(0.0f, within(0.1f));
   * assertThat(0.1f).isCloseTo(0.0f, offset(0.1f));
   * // ... except when using byLessThan which implies a strict comparison
   * assertThat(0.1f).isCloseTo(0.0f, byLessThan(0.1f)); // strict =&gt; fail
   * 
   * // this assertion also fails
   * assertThat(8.1f).isCloseTo(8.0f, within(0.001f));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public SELF isCloseTo(Float expected, Offset<Float> offset) {
    floats.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by less than the given offset.<br>
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion: 
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#byLessThan(Float)} or {@link Offset#strictOffset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#within(Float)} or {@link Assertions#offset(Float)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Float)} implies a <b>strict</b> comparison, 
   * use {@link Assertions#within(Float)} to get the old behavior. 
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(8.1f).isNotCloseTo(8.0f, byLessThan(0.01f));
   * assertThat(8.1f).isNotCloseTo(8.0f, within(0.01f));
   * assertThat(8.1f).isNotCloseTo(8.0f, offset(0.01f));
   * // diff == offset but isNotCloseTo succeeds as we use byLessThan
   * assertThat(0.1f).isNotCloseTo(0.0f, byLessThan(0.1f));   
   *
   * // assertions fail
   * assertThat(0.1f).isNotCloseTo(0.0f, within(0.1f));
   * assertThat(0.1f).isNotCloseTo(0.0f, offset(0.1f));
   * assertThat(8.1f).isNotCloseTo(8.0f, within(0.2f));
   * assertThat(8.1f).isNotCloseTo(8.0f, offset(0.2f));
   * assertThat(8.1f).isNotCloseTo(8.0f, byLessThan(0.2f));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Float)
   * @since 2.6.0 / 3.6.0
   */
  @Override
  public SELF isNotCloseTo(Float expected, Offset<Float> offset) {
    floats.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with float:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(11.0f).isCloseTo(new Float(10.0f), withinPercentage(20f));
   *
   * // if difference is exactly equals to the computed offset (1.0), it's ok
   * assertThat(11.0f).isCloseTo(new Float(10.0f), withinPercentage(10f));
   *
   * // assertion will fail
   * assertThat(11.0f).isCloseTo(new Float(10.0f), withinPercentage(5f));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public SELF isCloseTo(Float expected, Percentage percentage) {
    floats.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with float:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(11.0f).isNotCloseTo(new Float(10.0f), withinPercentage(5f));
   *
   * // assertions will fail
   * assertThat(11.0f).isNotCloseTo(new Float(10.0f), withinPercentage(10f));
   * assertThat(11.0f).isNotCloseTo(new Float(10.0f), withinPercentage(20f));</code></pre>
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
  public SELF isNotCloseTo(Float expected, Percentage percentage) {
    floats.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with float:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(11.0f).isCloseTo(10.0f, withinPercentage(20f));
   *
   * // if difference is exactly equals to the computed offset (1.0), it's ok
   * assertThat(11.0f).isCloseTo(10.0f, withinPercentage(10f));
   *
   * // assertion will fail
   * assertThat(11.0f).isCloseTo(10.0f, withinPercentage(5f));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  public SELF isCloseTo(float expected, Percentage percentage) {
    floats.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with float:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(11.0f).isNotCloseTo(10.0f, withinPercentage(5f));
   *
   * // assertions will fail
   * assertThat(11.0f).isNotCloseTo(10.0f, withinPercentage(10f));
   * assertThat(11.0f).isNotCloseTo(10.0f, withinPercentage(20f));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @since 2.6.0 / 3.6.0
   */
  public SELF isNotCloseTo(float expected, Percentage percentage) {
    floats.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /** 
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * This assertion is the same as {@link #isCloseTo(float, Offset)}.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Float)} or {@link Assertions#offset(Float)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Float)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(8.1f).isEqualTo(8.0f, within(0.2f));
   * assertThat(8.1f).isEqualTo(8.0f, offset(0.2f)); // alias of within 
   * assertThat(8.1f).isEqualTo(8.0f, byLessThan(0.2f)); // strict
   *
   * // assertions succeed when the difference == offset value ...
   * assertThat(0.1f).isEqualTo(0.0f, within(0.1f));
   * assertThat(0.1f).isEqualTo(0.0f, offset(0.1f));
   * // ... except when using byLessThan which implies a strict comparison
   * assertThat(0.1f).isEqualTo(0.0f, byLessThan(0.1f)); // strict =&gt; fail
   * 
   * // this assertion also fails
   * assertThat(0.1f).isEqualTo(0.0f, within(0.001f));</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  @Override
  public SELF isEqualTo(Float expected, Offset<Float> offset) {
    floats.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * This assertion is the same as {@link #isCloseTo(float, Offset)}.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Float)} or {@link Assertions#offset(Float)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Float)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(8.1f).isEqualTo(8.0f, within(0.2f));
   * assertThat(8.1f).isEqualTo(8.0f, offset(0.2f)); // alias of within 
   * assertThat(8.1f).isEqualTo(8.0f, byLessThan(0.2f)); // strict
   *
   * // assertions succeed when the difference == offset value ...
   * assertThat(0.1f).isEqualTo(0.0f, within(0.1f));
   * assertThat(0.1f).isEqualTo(0.0f, offset(0.1f));
   * // ... except when using byLessThan which implies a strict comparison
   * assertThat(0.1f).isEqualTo(0.0f, byLessThan(0.1f)); // strict =&gt; fail
   * 
   * // this assertion also fails
   * assertThat(0.1f).isEqualTo(0.0f, within(0.001f));</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(float expected, Offset<Float> offset) {
    floats.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(0.0f).isNotEqualTo(1.0f);
   * assertThat(-1.0f).isNotEqualTo(1.0f);
   * 
   * // assertions will fail:
   * assertThat(1.0f).isNotEqualTo(1.0f);
   * assertThat(1f).isNotEqualTo(1.0f);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public SELF isNotEqualTo(float other) {
    floats.assertNotEqual(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(1.0f).isLessThan(2.0f);
   * assertThat(1.0f).isLessThan(1.01f);
   * 
   * // assertions will fail:
   * assertThat(2.0f).isLessThan(1.0f);
   * assertThat(1.0f).isLessThan(1.0f);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public SELF isLessThan(float other) {
    floats.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(-1.0f).isLessThanOrEqualTo(1.0f);
   * assertThat(1.0f).isLessThanOrEqualTo(1.0f);
   * 
   * // assertion will fail:
   * assertThat(2.0f).isLessThanOrEqualTo(1.0f);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public SELF isLessThanOrEqualTo(float other) {
    floats.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(2.0f).isGreaterThan(1.0f);
   * assertThat(2.0f).isGreaterThan(1.99f);
   * 
   * // assertions will fail:
   * assertThat(1.0f).isGreaterThan(1.0f);
   * assertThat(1.0f).isGreaterThan(2.0f);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public SELF isGreaterThan(float other) {
    floats.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(2.0f).isGreaterThanOrEqualTo(1.0f);
   * assertThat(1.0f).isGreaterThanOrEqualTo(1.0f);
   * 
   * // assertions will fail:
   * assertThat(1.0f).isGreaterThanOrEqualTo(2.0f);
   * assertThat(1.0f).isGreaterThanOrEqualTo(0.99f);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public SELF isGreaterThanOrEqualTo(float other) {
    floats.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is in [start, end] range (start included, end included).
   *
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(1f).isBetween(-1f, 2f);
   * assertThat(1f).isBetween(1f, 2f);
   * assertThat(1f).isBetween(0f, 1f);
   *
   * // assertion will fail
   * assertThat(1f).isBetween(2f, 3f);</code></pre>
   */
  @Override
  public SELF isBetween(Float start, Float end) {
    floats.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   *
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(1f).isStrictlyBetween(-1f, 2f);
   *
   * // assertions will fail
   * assertThat(1f).isStrictlyBetween(1f, 2f);
   * assertThat(1f).isStrictlyBetween(0f, 1f);
   * assertThat(1f).isStrictlyBetween(2f, 3f);</code></pre>
   *
   */
  @Override
  public SELF isStrictlyBetween(Float start, Float end) {
    floats.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Float> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Float> customComparator, String customComparatorDescription) {
    floats = new Floats(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    floats = Floats.instance();
    return super.usingDefaultComparator();
  }

}
