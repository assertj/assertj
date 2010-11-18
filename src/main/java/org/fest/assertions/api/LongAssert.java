/*
 * Created on Oct 20, 2010
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

import org.fest.assertions.core.NumberAssert;
import org.fest.assertions.internal.Longs;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for longs. To create an instance of this class, use the factory methods
 * <code>{@link Assertions#assertThat(Long)}</code> or <code>{@link Assertions#assertThat(long)}</code>.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 */
public class LongAssert extends GenericComparableAssert<LongAssert, Long> implements NumberAssert<Long> {

  @VisibleForTesting Longs longs = Longs.instance();

  protected LongAssert(Long actual) {
    super(actual);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public LongAssert isEqualTo(long expected) {
    longs.assertEqual(info, actual, expected);
    return this;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public LongAssert isNotEqualTo(long other) {
    longs.assertNotEqual(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public LongAssert isZero() {
    longs.assertIsZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public LongAssert isNotZero() {
    longs.assertIsNotZero(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public LongAssert isPositive() {
    longs.assertIsPositive(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public LongAssert isNegative() {
    longs.assertIsNegative(info, actual);
    return this;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public LongAssert isLessThan(long other) {
    longs.assertLessThan(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public LongAssert isLessThanOrEqualTo(long other) {
    longs.assertLessThanOrEqualTo(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public LongAssert isGreaterThan(long other) {
    longs.assertGreaterThan(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public LongAssert isGreaterThanOrEqualTo(long other) {
    longs.assertGreaterThanOrEqualTo(info, actual, other);
    return this;
  }
}
