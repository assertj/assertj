/*
 * Created on Oct 25, 2010
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

import org.fest.assertions.core.FloatingPointNumberAssert;
import org.fest.assertions.data.Offset;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for doubles.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Double)}</code> or
 * <code>{@link Assertions#assertThat(double)}</code>.
 * </p>
 * 
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Ansgar Konermann
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public class DoubleAssert extends AbstractComparableAssert<DoubleAssert, Double> implements FloatingPointNumberAssert<Double> {

  @VisibleForTesting
  Doubles doubles = Doubles.instance();

  protected DoubleAssert(Double actual) {
    super(actual, DoubleAssert.class);
  }

  /** {@inheritDoc} */
  public DoubleAssert isNaN() {
    doubles.assertIsNaN(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isNotNaN() {
    doubles.assertIsNotNaN(info, actual);
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
  public DoubleAssert isNotNegative() {
    doubles.assertIsNotNegative(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public DoubleAssert isNotPositive() {
    doubles.assertIsNotPositive(info, actual);
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

  @Override
  public DoubleAssert usingComparator(Comparator<? super Double> customComparator) {
    super.usingComparator(customComparator);
    this.doubles = new Doubles(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public DoubleAssert usingDefaultComparator() {
    super.usingDefaultComparator();
    this.doubles = Doubles.instance();
    return myself;
  }
}
