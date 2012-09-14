/*
 * Created on Oct 17, 2010
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
 * Assertion methods for integers.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Integer)}</code> or
 * <code>{@link Assertions#assertThat(int)}</code>.
 * </p>
 * 
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public class IntegerAssert extends AbstractComparableAssert<IntegerAssert, Integer> implements NumberAssert<Integer> {

  @VisibleForTesting
  Integers integers = Integers.instance();

  protected IntegerAssert(Integer actual) {
    super(actual, IntegerAssert.class);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public IntegerAssert isEqualTo(int expected) {
    integers.assertEqual(info, actual, expected);
    return this;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public IntegerAssert isNotEqualTo(int other) {
    integers.assertNotEqual(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public IntegerAssert isZero() {
    integers.assertIsZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public IntegerAssert isNotZero() {
    integers.assertIsNotZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public IntegerAssert isPositive() {
    integers.assertIsPositive(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public IntegerAssert isNegative() {
    integers.assertIsNegative(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public IntegerAssert isNotNegative() {
    integers.assertIsNotNegative(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public IntegerAssert isNotPositive() {
    integers.assertIsNotPositive(info, actual);
    return this;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public IntegerAssert isLessThan(int other) {
    integers.assertLessThan(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public IntegerAssert isLessThanOrEqualTo(int other) {
    integers.assertLessThanOrEqualTo(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public IntegerAssert isGreaterThan(int other) {
    integers.assertGreaterThan(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public IntegerAssert isGreaterThanOrEqualTo(int other) {
    integers.assertGreaterThanOrEqualTo(info, actual, other);
    return this;
  }

  @Override
  public IntegerAssert usingComparator(Comparator<? super Integer> customComparator) {
    super.usingComparator(customComparator);
    this.integers = new Integers(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public IntegerAssert usingDefaultComparator() {
    super.usingDefaultComparator();
    this.integers = Integers.instance();
    return myself;
  }
}
