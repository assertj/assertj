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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.api;

import org.fest.assertions.core.*;
import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for bytes. To create an instance of this class, use the factory methods
 * <code>{@link Assertions#assertThat(Byte)}</code> or <code>{@link Assertions#assertThat(byte)}</code>.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 */
public class ByteAssert implements Assert<Byte>, ComparableAssert<Byte>, NumberAssert<Byte> {

  @VisibleForTesting Objects objects = Objects.instance();
  @VisibleForTesting Comparables comparables = Comparables.instance();
  @VisibleForTesting Bytes bytes = Bytes.instance();
  @VisibleForTesting Conditions conditions = Conditions.instance();

  @VisibleForTesting final Byte actual;
  @VisibleForTesting final WritableAssertionInfo info;

  protected ByteAssert(Byte actual) {
    this.actual = actual;
    info = new WritableAssertionInfo();
  }

  /** {@inheritDoc} */
  public ByteAssert as(String description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public ByteAssert as(Description description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public ByteAssert describedAs(String description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert describedAs(Description description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  public ByteAssert isNotNull() {
    objects.assertNotNull(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert isEqualTo(Byte expected) {
    objects.assertEqual(info, actual, expected);
    return this;
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

  /** {@inheritDoc} */
  public ByteAssert isNotEqualTo(Byte other) {
    objects.assertNotEqual(info, actual, other);
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
  public ByteAssert isSameAs(Byte expected) {
    objects.assertSame(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert isNotSameAs(Byte other) {
    objects.assertNotSame(info, actual, other);
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
  public ByteAssert isLessThan(Byte other) {
    comparables.assertLessThan(info, actual, other);
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

  /** {@inheritDoc} */
  public ByteAssert isLessThanOrEqualTo(Byte other) {
    comparables.assertLessThanOrEqualTo(info, actual, other);
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

  /** {@inheritDoc} */
  public ByteAssert isGreaterThan(Byte other) {
    comparables.assertGreaterThan(info, actual, other);
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

  /** {@inheritDoc} */
  public ByteAssert isGreaterThanOrEqualTo(Byte other) {
    comparables.assertGreaterThanOrEqualTo(info, actual, other);
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

  /** {@inheritDoc} */
  public ByteAssert satisfies(Condition<Byte> condition) {
    conditions.assertSatisfies(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert doesNotSatisfy(Condition<Byte> condition) {
    conditions.assertDoesNotSatisfy(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public ByteAssert is(Condition<Byte> condition) {
    return satisfies(condition);
  }

  /** {@inheritDoc} */
  public ByteAssert isNot(Condition<Byte> condition) {
    return doesNotSatisfy(condition);
  }

  @VisibleForTesting final String descriptionText() {
    return info.descriptionText();
  }
}
