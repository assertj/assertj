/*
 * Created on Oct 20, 2010
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
 * Assertion methods for shorts.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Short)}</code> or
 * <code>{@link Assertions#assertThat(short)}</code>.
 * </p>
 * 
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public class ShortAssert extends AbstractComparableAssert<ShortAssert, Short> implements NumberAssert<Short> {

  @VisibleForTesting
  Shorts shorts = Shorts.instance();

  protected ShortAssert(Short actual) {
    super(actual, ShortAssert.class);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public ShortAssert isEqualTo(short expected) {
    shorts.assertEqual(info, actual, expected);
    return this;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public ShortAssert isNotEqualTo(short other) {
    shorts.assertNotEqual(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public ShortAssert isZero() {
    shorts.assertIsZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ShortAssert isNotZero() {
    shorts.assertIsNotZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ShortAssert isPositive() {
    shorts.assertIsPositive(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ShortAssert isNegative() {
    shorts.assertIsNegative(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ShortAssert isNotNegative() {
    shorts.assertIsNotNegative(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ShortAssert isNotPositive() {
    shorts.assertIsNotPositive(info, actual);
    return this;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public ShortAssert isLessThan(short other) {
    shorts.assertLessThan(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public ShortAssert isLessThanOrEqualTo(short other) {
    shorts.assertLessThanOrEqualTo(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public ShortAssert isGreaterThan(short other) {
    shorts.assertGreaterThan(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public ShortAssert isGreaterThanOrEqualTo(short other) {
    shorts.assertGreaterThanOrEqualTo(info, actual, other);
    return this;
  }

  @Override
  public ShortAssert usingComparator(Comparator<? super Short> customComparator) {
    super.usingComparator(customComparator);
    this.shorts = new Shorts(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public ShortAssert usingDefaultComparator() {
    super.usingDefaultComparator();
    this.shorts = Shorts.instance();
    return myself;
  }
}
