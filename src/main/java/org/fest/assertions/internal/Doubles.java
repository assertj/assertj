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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.lang.Double.NaN;
import static java.lang.Math.abs;
import static org.fest.assertions.error.IsEqual.isEqual;
import static org.fest.assertions.error.IsNotEqual.isNotEqual;
import static org.fest.assertions.error.IsNotEqualWithOffset.isNotEqual;
import static org.fest.assertions.error.IsNotGreaterThan.isNotGreaterThan;
import static org.fest.assertions.error.IsNotGreaterThanOrEqualTo.isNotGreaterThanOrEqualTo;
import static org.fest.assertions.error.IsNotLessThan.isNotLessThan;
import static org.fest.assertions.error.IsNotLessThanOrEqualTo.isNotLessThanOrEqualTo;
import static org.fest.util.Objects.areEqual;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Offset;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Double}</code>s.
 *
 * @author Alex Ruiz
 */
public class Doubles {

  private static final Double ZERO = 0d;

  private static final Doubles INSTANCE = new Doubles();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Doubles instance() {
    return INSTANCE;
  }

  @VisibleForTesting Comparables comparables = Comparables.instance();
  @VisibleForTesting Failures failures = Failures.instance();

  @VisibleForTesting Doubles() {}

  /**
   * Verifies that the actual value is equal to {@code NaN}.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is not equal to {@code NaN}.
   */
  public void assertIsNaN(AssertionInfo info, Double actual) {
    comparables.assertEqual(info, actual, NaN);
  }

  /**
   * Verifies that the actual value is not equal to {@code NaN}.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is equal to {@code NaN}.
   */
  public void assertIsNotNaN(AssertionInfo info, Double actual) {
    comparables.assertNotEqual(info, actual, NaN);
  }

  /**
   * Asserts that the actual value is equal to zero.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  public void assertIsZero(AssertionInfo info, Double actual) {
    comparables.assertEqual(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is not equal to zero.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  public void assertIsNotZero(AssertionInfo info, Double actual) {
    comparables.assertNotEqual(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is negative.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not negative: it is either equal to or greater than zero.
   */
  public void assertIsNegative(AssertionInfo info, Double actual) {
    comparables.assertLessThan(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is positive.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive: it is either equal to or less than zero.
   */
  public void assertIsPositive(AssertionInfo info, Double actual) {
    comparables.assertGreaterThan(info, actual, ZERO);
  }

  /**
   * Asserts that two doubles are equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one. This method will throw a
   * {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual values are not
   * equal.
   */
  public void assertEqual(AssertionInfo info, Double actual, double expected) {
    assertNotNull(info, actual);
    if (isEqualTo(actual, expected)) return;
    throw failures.failure(info, isNotEqual(actual, expected));
  }

  /**
   * Verifies that two doubles are equal within a positive offset.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @param offset the given positive offset.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one.
   */
  public void assertEqual(AssertionInfo info, Double actual, Double expected, Offset<Double> offset) {
    checkIsNotNull(offset);
    if (areEqual(actual, expected)) return;
    if (actual != null && expected != null && isEqualTo(actual, expected, offset)) return;
    throw failures.failure(info, isNotEqual(actual, expected, offset));
  }

  /**
   * Verifies that two doubles are equal within a positive offset.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @param offset the given positive offset.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one.
   */
  public void assertEqual(AssertionInfo info, Double actual, double expected, Offset<Double> offset) {
    checkIsNotNull(offset);
    assertNotNull(info, actual);
    if (isEqualTo(actual, expected) || isEqualTo(actual, expected, offset)) return;
    throw failures.failure(info, isNotEqual(actual, expected, offset));
  }

  private void checkIsNotNull(Offset<Double> offset) {
    if (offset == null) throw new NullPointerException("The given offset should not be null");
  }

  private static boolean isEqualTo(Double actual, double expected) {
    return actual.doubleValue() == expected;
  }

  private static boolean isEqualTo(Double actual, double expected, Offset<Double> offset) {
    return abs(expected - actual.doubleValue()) <= offset.value.doubleValue();
  }

  /**
   * Asserts that two doubles are not equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the other one.
   */
  public void assertNotEqual(AssertionInfo info, Double actual, double other) {
    assertNotNull(info, actual);
    if (actual.doubleValue() != other) return;
    throw failures.failure(info, isEqual(actual, other));
  }

  /**
   * Asserts that the actual value is less than the other one.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not less than the other one: this assertion will
   * fail if the actual value is equal to or greater than the other value.
   */
  public void assertLessThan(AssertionInfo info, Double actual, double other) {
    assertNotNull(info, actual);
    if (isLessThan(actual, other)) return;
    throw failures.failure(info, isNotLessThan(actual, other));
  }

  /**
   * Asserts that the actual value is less than or equal to the other one.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the other one.
   */
  public void assertLessThanOrEqualTo(AssertionInfo info, Double actual, double other) {
    assertNotNull(info, actual);
    if (!isGreaterThan(actual, other)) return;
    throw failures.failure(info, isNotLessThanOrEqualTo(actual, other));
  }

  /**
   * Asserts that the actual value is greater than the other one.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not greater than the other one: this assertion will
   * fail if the actual value is equal to or less than the other value.
   */
  public void assertGreaterThan(AssertionInfo info, Double actual, double other) {
    assertNotNull(info, actual);
    if (isGreaterThan(actual, other)) return;
    throw failures.failure(info, isNotGreaterThan(actual, other));
  }

  private static boolean isGreaterThan(Double actual, double other) {
    return actual.doubleValue() > other;
  }

  /**
   * Asserts that the actual value is greater than or equal to the other one.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the other one.
   */
  public void assertGreaterThanOrEqualTo(AssertionInfo info, Double actual, double other) {
    assertNotNull(info, actual);
    if (!isLessThan(actual, other)) return;
    throw failures.failure(info, isNotGreaterThanOrEqualTo(actual, other));
  }

  private static boolean isLessThan(Double actual, double other) {
    return actual.doubleValue() < other;
  }

  private static void assertNotNull(AssertionInfo info, Double actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
