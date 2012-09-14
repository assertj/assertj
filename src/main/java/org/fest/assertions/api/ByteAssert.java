/*
 * Created on Oct 21, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import java.util.Comparator;

import org.fest.assertions.core.NumberAssert;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for bytes.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Byte)}</code> or
 * <code>{@link Assertions#assertThat(byte)}</code>.
 * </p>
 * 
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public class ByteAssert extends AbstractComparableAssert<ByteAssert, Byte> implements NumberAssert<Byte> {

  @VisibleForTesting
  Bytes bytes = Bytes.instance();

  protected ByteAssert(Byte actual) {
    super(actual, ByteAssert.class);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public ByteAssert isEqualTo(byte expected) {
    bytes.assertEqual(info, actual, expected);
    return this;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public ByteAssert isNotEqualTo(byte other) {
    bytes.assertNotEqual(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert isZero() {
    bytes.assertIsZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert isNotZero() {
    bytes.assertIsNotZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert isPositive() {
    bytes.assertIsPositive(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert isNegative() {
    bytes.assertIsNegative(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert isNotNegative() {
    bytes.assertIsNotNegative(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert isNotPositive() {
    bytes.assertIsNotPositive(info, actual);
    return this;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public ByteAssert isLessThan(byte other) {
    bytes.assertLessThan(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public ByteAssert isLessThanOrEqualTo(byte other) {
    bytes.assertLessThanOrEqualTo(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public ByteAssert isGreaterThan(byte other) {
    bytes.assertGreaterThan(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public ByteAssert isGreaterThanOrEqualTo(byte other) {
    bytes.assertGreaterThanOrEqualTo(info, actual, other);
    return this;
  }

  @Override
  public ByteAssert usingComparator(Comparator<? super Byte> customComparator) {
    super.usingComparator(customComparator);
    this.bytes = new Bytes(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public ByteAssert usingDefaultComparator() {
    super.usingDefaultComparator();
    this.bytes = Bytes.instance();
    return myself;
  }
}
