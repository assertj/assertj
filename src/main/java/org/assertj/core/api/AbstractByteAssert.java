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
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Byte}s.
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
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractByteAssert<SELF extends AbstractByteAssert<SELF>> extends AbstractComparableAssert<SELF, Byte>
    implements NumberAssert<SELF, Byte> {

  @VisibleForTesting
  Bytes bytes = Bytes.instance();

  public AbstractByteAssert(Byte actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat((byte) 1).isEqualTo((byte) 1);
   * assertThat((byte) 0).isEqualTo(Byte.valueOf("0"));
   * 
   * // assertions will fail
   * assertThat((byte) 1).isEqualTo((byte) 0);
   * assertThat(Byte.valueOf("1")).isEqualTo((byte) 0);</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(byte expected) {
    bytes.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat((byte) 0).isNotEqualTo((byte) 1);
   * assertThat(Byte.valueOf("1")).isNotEqualTo((byte) 0);
   * 
   * // assertions will fail
   * assertThat((byte) 0).isNotEqualTo((byte) 0);
   * assertThat(Byte.valueOf("0")).isNotEqualTo((byte) 0);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public SELF isNotEqualTo(byte other) {
    bytes.assertNotEqual(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isZero() {
    bytes.assertIsZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotZero() {
    bytes.assertIsNotZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isOne() {
    bytes.assertIsOne(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat((byte) 1).isPositive();
   * 
   * // assertion will fail
   * assertThat((byte) -1).isPositive();</code></pre>
   * 
   */
  @Override
  public SELF isPositive() {
    bytes.assertIsPositive(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat((byte) -1).isNegative();
   * 
   * // assertion will fail
   * assertThat((byte) 1).isNegative();</code></pre>
   * 
   */
  @Override
  public SELF isNegative() {
    bytes.assertIsNegative(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat((byte) 1).isNotNegative();
   * 
   * // assertion will fail
   * assertThat((byte) -1).isNotNegative();</code></pre>
   * 
   */
  @Override
  public SELF isNotNegative() {
    bytes.assertIsNotNegative(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat((byte) -1).isNotPositive();
   * 
   * // assertion will fail
   * assertThat((byte) 1).isNotPositive();</code></pre>
   * 
   */
  @Override
  public SELF isNotPositive() {
    bytes.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat((byte) 1).isLessThan((byte) 2);
   * 
   * // assertion will fail
   * assertThat((byte) 1).isLessThan((byte) 0);
   * assertThat((byte) 1).isLessThan((byte) 1);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public SELF isLessThan(byte other) {
    bytes.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat((byte) 1).isLessThanOrEqualTo((byte) 2);
   * assertThat((byte) 1).isLessThanOrEqualTo((byte) 1);
   * 
   * // assertion will fail
   * assertThat((byte) 1).isLessThanOrEqualTo((byte) 0);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public SELF isLessThanOrEqualTo(byte other) {
    bytes.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat((byte) 2).isGreaterThan((byte) 1);
   * 
   * // assertion will fail
   * assertThat((byte) 2).isGreaterThan((byte) 3);
   * assertThat((byte) 2).isGreaterThan((byte) 2);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public SELF isGreaterThan(byte other) {
    bytes.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat((byte) 2).isGreaterThanOrEqualTo((byte) 1);
   * assertThat((byte) 2).isGreaterThanOrEqualTo((byte) 2);
   *
   * // assertion will fail
   * assertThat((byte) 2).isGreaterThanOrEqualTo((byte) 3);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public SELF isGreaterThanOrEqualTo(byte other) {
    bytes.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is in [start, end] range (start included, end included).
   *
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat((byte) 1).isBetween((byte) -1, (byte) 2);
   * assertThat((byte) 1).isBetween((byte) 1, (byte) 2);
   * assertThat((byte) 1).isBetween((byte) 0, (byte) 1);
   * 
   * // assertion will fail
   * assertThat((byte) 1).isBetween((byte) 2, (byte) 3);</code></pre>
   * 
   */
  @Override
  public SELF isBetween(Byte start, Byte end) {
    bytes.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   *
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat((byte) 1).isStrictlyBetween((byte) -1, (byte) 2);
   * 
   * // assertions will fail
   * assertThat((byte) 1).isStrictlyBetween((byte) 1, (byte) 2);
   * assertThat((byte) 1).isStrictlyBetween((byte) 0, (byte) 1);
   * assertThat((byte) 1).isStrictlyBetween((byte) 2, (byte) 3);</code></pre>
   * 
   */
  @Override
  public SELF isStrictlyBetween(Byte start, Byte end) {
    bytes.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual byte is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass:
   * assertThat((byte) 5).isCloseTo((byte) 7, within((byte) 3));
   *
   * // if difference is exactly equals to the offset, it's ok
   * assertThat((byte) 5).isCloseTo((byte) 7, within((byte) 2));
   *
   * // assertion will fail
   * assertThat((byte) 5).isCloseTo((byte) 7, within((byte) 1));</code></pre>
   *
   * @param expected the given byte to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  public SELF isCloseTo(byte expected, Offset<Byte> offset) {
    bytes.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual byte is not close to the given one by less than the given offset.<br>
   * If the difference is equal to the offset value, the assertion fails.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass:
   * assertThat((byte) 5).isNotCloseTo((byte) 7, byLessThan((byte) 1));
   *
   * // assertions will fail
   * assertThat((byte) 5).isNotCloseTo((byte) 7, byLessThan((byte) 2));
   * assertThat((byte) 5).isNotCloseTo((byte) 7, byLessThan((byte) 3));</code></pre>
   *
   * @param expected the given byte to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Byte)
   * @since 2.6.0 / 3.6.0
   */
  public SELF isNotCloseTo(byte expected, Offset<Byte> offset) {
    bytes.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual Byte is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass:
   * assertThat((byte) 5).isCloseTo(new Byte("7"), within((byte) 3));
   *
   * // if difference is exactly equals to the offset, it's ok
   * assertThat((byte) 5).isCloseTo(new Byte("7"), within((byte) 2));
   *
   * // assertion will fail
   * assertThat((byte) 5).isCloseTo(new Byte("7"), within((byte) 1));</code></pre>
   *
   * @param expected the given Byte to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected Byte is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public SELF isCloseTo(Byte expected, Offset<Byte> offset) {
    bytes.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual Byte is not close to the given one by less than the given offset.<br>
   * If the difference is equal to the offset value, the assertion fails.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass:
   * assertThat((byte) 5).isNotCloseTo(new Byte("7"), byLessThan((byte) 1));
   *
   * // assertions will fail
   * assertThat((byte) 5).isNotCloseTo(new Byte("7"), byLessThan((byte) 2));
   * assertThat((byte) 5).isNotCloseTo(new Byte("7"), byLessThan((byte) 3));</code></pre>
   *
   * @param expected the given Byte to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected Byte is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Byte)
   * @since 2.6.0 / 3.6.0
   */
  @Override
  public SELF isNotCloseTo(Byte expected, Offset<Byte> offset) {
    bytes.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with byte:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((byte) 11).isCloseTo(Byte.valueOf(10), withinPercentage((byte) 20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat((byte) 11).isCloseTo(Byte.valueOf(10), withinPercentage((byte) 10));
   *
   * // assertion will fail
   * assertThat((byte) 11).isCloseTo(Byte.valueOf(10), withinPercentage((byte) 5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public SELF isCloseTo(Byte expected, Percentage percentage) {
    bytes.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one b the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with byte:
   * <pre><code class='java'> // assertion will pass:
   * assertThat((byte) 11).isNotCloseTo(Byte.valueOf(10), withinPercentage((byte) 5));
   *
   * // assertions will fail
   * assertThat((byte) 11).isNotCloseTo(Byte.valueOf(10), withinPercentage((byte) 10));
   * assertThat((byte) 11).isNotCloseTo(Byte.valueOf(10), withinPercentage((byte) 20));</code></pre>
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
  public SELF isNotCloseTo(Byte expected, Percentage percentage) {
    bytes.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with byte:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((byte) 11).isCloseTo((byte) 10, withinPercentage((byte) 20));
   *
   * // assertions will fail
   * assertThat((byte) 11).isCloseTo((byte) 10, withinPercentage((byte) 10));
   * assertThat((byte) 11).isCloseTo((byte) 10, withinPercentage((byte) 5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   */
  public SELF isCloseTo(byte expected, Percentage percentage) {
    bytes.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with byte:
   * <pre><code class='java'> // assertion will pass:
   * assertThat((byte) 11).isNotCloseTo((byte) 10, withinPercentage((byte) 5));
   *
   * // assertions will fail
   * assertThat((byte) 11).isNotCloseTo((byte) 10, withinPercentage((byte) 10));
   * assertThat((byte) 11).isNotCloseTo((byte) 10, withinPercentage((byte) 20));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @since 2.6.0 / 3.6.0
   */
  public SELF isNotCloseTo(byte expected, Percentage percentage) {
    bytes.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Byte> customComparator) {
    super.usingComparator(customComparator);
    this.bytes = new Bytes(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    super.usingDefaultComparator();
    this.bytes = Bytes.instance();
    return myself;
  }
}
