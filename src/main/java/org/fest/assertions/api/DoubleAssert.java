/*
 * Created on Oct 25, 2010
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
import org.fest.assertions.data.Offset;
import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for doubles. To create an instance of this class, use the factory methods
 * <code>{@link Assertions#assertThat(Double)}</code> or <code>{@link Assertions#assertThat(double)}</code>.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Ansgar Konermann
 */
public class DoubleAssert implements Assert<Double>, ComparableAssert<Double>, FloatingPointNumberAssert<Double> {

  @VisibleForTesting Objects objects = Objects.instance();
  @VisibleForTesting Comparables comparables = Comparables.instance();
  @VisibleForTesting Doubles doubles = Doubles.instance();
  @VisibleForTesting Conditions conditions = Conditions.instance();

  @VisibleForTesting final Double actual;
  @VisibleForTesting final WritableAssertionInfo info;

  protected DoubleAssert(Double actual) {
    this.actual = actual;
    info = new WritableAssertionInfo();
  }

  /** {@inheritDoc} */
  public DoubleAssert as(String description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public DoubleAssert as(Description description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public DoubleAssert describedAs(String description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert describedAs(Description description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  public DoubleAssert isNotNull() {
    objects.assertNotNull(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isEqualTo(Double expected) {
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
  public DoubleAssert isEqualTo(double expected) {
    doubles.assertEqual(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isEqualTo(Double expected, Offset<Double> offset) {
    doubles.assertEqual(info, actual, expected, offset);
    return this;
  }

  /**
   * Verifies that the actual value is equal to the given one, within a positive offset.
   * @param expected the given value to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public DoubleAssert isEqualTo(double expected, Offset<Double> offset) {
    doubles.assertEqual(info, actual, expected, offset);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isNotEqualTo(Double other) {
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
  public DoubleAssert isNotEqualTo(double other) {
    doubles.assertNotEqual(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isSameAs(Double expected) {
    objects.assertSame(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isNotSameAs(Double other) {
    objects.assertNotSame(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isZero() {
    doubles.assertIsZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isNotZero() {
    doubles.assertIsNotZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isPositive() {
    doubles.assertIsPositive(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isNegative() {
    doubles.assertIsNegative(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isLessThan(Double other) {
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
  public DoubleAssert isLessThan(double other) {
    doubles.assertLessThan(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isLessThanOrEqualTo(Double other) {
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
  public DoubleAssert isLessThanOrEqualTo(double other) {
    doubles.assertLessThanOrEqualTo(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isGreaterThan(Double other) {
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
  public DoubleAssert isGreaterThan(double other) {
    doubles.assertGreaterThan(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isGreaterThanOrEqualTo(Double other) {
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
  public DoubleAssert isGreaterThanOrEqualTo(double other) {
    doubles.assertGreaterThanOrEqualTo(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert satisfies(Condition<Double> condition) {
    conditions.assertSatisfies(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert doesNotSatisfy(Condition<Double> condition) {
    conditions.assertDoesNotSatisfy(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert is(Condition<Double> condition) {
    return satisfies(condition);
  }

  /** {@inheritDoc} */
  public DoubleAssert isNot(Condition<Double> condition) {
    return doesNotSatisfy(condition);
  }

  @VisibleForTesting final String descriptionText() {
    return info.descriptionText();
  }
}
