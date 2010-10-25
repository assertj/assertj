/*
 * Created on Oct 24, 2010
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
import org.fest.assertions.data.Delta;
import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for floats. To create an instance of this class, use the factory methods
 * <code>{@link Assertions#assertThat(Float)}</code> or <code>{@link Assertions#assertThat(float)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Ansgar Konermann
 */
public class FloatAssert implements Assert<Float>, ComparableAssert<Float>, FloatingPointNumberAssert<Float> {

  @VisibleForTesting Objects objects = Objects.instance();
  @VisibleForTesting Comparables comparables = Comparables.instance();
  @VisibleForTesting Floats floats = Floats.instance();
  @VisibleForTesting Conditions conditions = Conditions.instance();

  @VisibleForTesting final Float actual;
  @VisibleForTesting final WritableAssertionInfo info;

  protected FloatAssert(Float actual) {
    this.actual = actual;
    info = new WritableAssertionInfo();
  }

  /** {@inheritDoc} */
  public FloatAssert as(String description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public FloatAssert as(Description description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public FloatAssert describedAs(String description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert describedAs(Description description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  public FloatAssert isNotNull() {
    objects.assertNotNull(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isEqualTo(Float expected) {
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
  public FloatAssert isEqualTo(float expected) {
    floats.assertEqual(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isEqualTo(Float expected, Delta<Float> delta) {
    floats.assertEqual(info, actual, expected, delta);
    return this;
  }

  /**
   * Verifies that the actual value is equal to the given one, within a positive delta.
   * @param expected the given value to compare the actual value to.
   * @param delta the given delta.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given delta is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public FloatAssert isEqualTo(float expected, Delta<Float> delta) {
    floats.assertEqual(info, actual, expected, delta);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isNotEqualTo(Float other) {
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
  public FloatAssert isNotEqualTo(float other) {
    floats.assertNotEqual(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isSameAs(Float expected) {
    objects.assertSame(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isNotSameAs(Float other) {
    objects.assertNotSame(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isZero() {
    floats.assertIsZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isNotZero() {
    floats.assertIsNotZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isPositive() {
    floats.assertIsPositive(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isNegative() {
    floats.assertIsNegative(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isLessThan(Float other) {
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
  public FloatAssert isLessThan(float other) {
    floats.assertLessThan(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isLessThanOrEqualTo(Float other) {
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
  public FloatAssert isLessThanOrEqualTo(float other) {
    floats.assertLessThanOrEqualTo(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isGreaterThan(Float other) {
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
  public FloatAssert isGreaterThan(float other) {
    floats.assertGreaterThan(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert isGreaterThanOrEqualTo(Float other) {
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
  public FloatAssert isGreaterThanOrEqualTo(float other) {
    floats.assertGreaterThanOrEqualTo(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert satisfies(Condition<Float> condition) {
    conditions.assertSatisfies(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert doesNotSatisfy(Condition<Float> condition) {
    conditions.assertDoesNotSatisfy(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public FloatAssert is(Condition<Float> condition) {
    return satisfies(condition);
  }

  /** {@inheritDoc} */
  public FloatAssert isNot(Condition<Float> condition) {
    return doesNotSatisfy(condition);
  }

  @VisibleForTesting final String descriptionText() {
    return info.descriptionText();
  }
}
