/*
 * Created on Oct 21, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Byte}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
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
public abstract class AbstractByteAssert<S extends AbstractByteAssert<S>> extends AbstractComparableAssert<S, Byte>
    implements NumberAssert<S, Byte> {

  @VisibleForTesting
  Bytes bytes = Bytes.instance();

  protected AbstractByteAssert(Byte actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * 
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public S isEqualTo(byte expected) {
    bytes.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public S isNotEqualTo(byte other) {
    bytes.assertNotEqual(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isZero() {
    bytes.assertIsZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotZero() {
    bytes.assertIsNotZero(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat((byte) 1).isPositive();
   * 
   * // assertion will fail
   * assertThat((byte) -1).isPositive();
   * </pre>
   * 
   * </p>
   */
  @Override
  public S isPositive() {
    bytes.assertIsPositive(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat((byte) -1).isNegative();
   * 
   * // assertion will fail
   * assertThat((byte) 1).isNegative();
   * </pre>
   * 
   * </p>
   */
  @Override
  public S isNegative() {
    bytes.assertIsNegative(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat((byte) 1).isNotNegative();
   * 
   * // assertion will fail
   * assertThat((byte) -1).isNotNegative();
   * </pre>
   * 
   * </p>
   */
  @Override
  public S isNotNegative() {
    bytes.assertIsNotNegative(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat((byte) -1).isNotPositive();
   * 
   * // assertion will fail
   * assertThat((byte) 1).isNotPositive();
   * </pre>
   * 
   * </p>
   */
  @Override
  public S isNotPositive() {
    bytes.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat((byte) 1).isLessThan(2);
   * 
   * // assertion will fail
   * assertThat((byte) 1).isLessThan(0);
   * assertThat((byte) 1).isLessThan(1);
   * </pre>
   * 
   * </p>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public S isLessThan(byte other) {
    bytes.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat((byte) 1).isLessThanOrEqualTo(2);
   * assertThat((byte) 1).isLessThanOrEqualTo(1);
   * 
   * // assertion will fail
   * assertThat((byte) 1).isLessThanOrEqualTo(0);
   * </pre>
   * 
   * </p>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public S isLessThanOrEqualTo(byte other) {
    bytes.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat((byte) 2).isGreaterThan(1);
   * 
   * // assertion will fail
   * assertThat((byte) 2).isGreaterThan(3);
   * assertThat((byte) 2).isGreaterThan(2);
   * </pre>
   * 
   * </p>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public S isGreaterThan(byte other) {
    bytes.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat((byte) 2).isGreaterThanOrEqualTo(1);
   * assertThat((byte) 2).isGreaterThanOrEqualTo(2);
   *
   * // assertion will fail
   * assertThat((byte) 2).isGreaterThanOrEqualTo(3);
   * </pre>
   * 
   * </p>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public S isGreaterThanOrEqualTo(byte other) {
    bytes.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is in [start, end] range (start included, end included).
   *
   * <p>
   * Example:
   * 
   * <pre>
   * // assertions will pass
   * assertThat((byte) 1).isBetween((byte) -1, (byte) 2);
   * assertThat((byte) 1).isBetween((byte) 1, (byte) 2);
   * assertThat((byte) 1).isBetween((byte) 0, (byte) 1);
   * 
   * // assertion will fail
   * assertThat((byte) 1).isBetween((byte) 2, (byte) 3);
   * </pre>
   * 
   * </p>
   */
  @Override
  public S isBetween(Byte start, Byte end) {
    bytes.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   *
   * <p>
   * Example:
   * 
   * <pre>
   * // assertion will pass
   * assertThat((byte) 1).isStrictlyBetween((byte) -1, (byte) 2);
   * 
   * // assertions will fail
   * assertThat((byte) 1).isStrictlyBetween((byte) 1, (byte) 2);
   * assertThat((byte) 1).isStrictlyBetween((byte) 0, (byte) 1);
   * assertThat((byte) 1).isStrictlyBetween((byte) 2, (byte) 3);
   * </pre>
   * 
   * </p>
   */
  @Override
  public S isStrictlyBetween(Byte start, Byte end) {
    bytes.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  @Override
  public S usingComparator(Comparator<? super Byte> customComparator) {
    super.usingComparator(customComparator);
    this.bytes = new Bytes(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public S usingDefaultComparator() {
    super.usingDefaultComparator();
    this.bytes = Bytes.instance();
    return myself;
  }
}
