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
package org.fest.assertions.internal;

import static java.lang.Math.abs;
import static org.fest.assertions.error.IsEqual.isEqual;
import static org.fest.assertions.error.IsNotEqual.isNotEqual;
import static org.fest.assertions.error.IsNotEqualWithDelta.isNotEqual;
import static org.fest.assertions.error.IsNotGreaterThan.isNotGreaterThan;
import static org.fest.assertions.error.IsNotGreaterThanOrEqualTo.isNotGreaterThanOrEqualTo;
import static org.fest.assertions.error.IsNotLessThan.isNotLessThan;
import static org.fest.assertions.error.IsNotLessThanOrEqualTo.isNotLessThanOrEqualTo;
import static org.fest.util.Objects.areEqual;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Delta;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Float}</code>s.
 *
 * @author Alex Ruiz
 */
public class Floats {

  private static final Float ZERO = 0f;

  private static final Floats INSTANCE = new Floats();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Floats instance() {
    return INSTANCE;
  }

  @VisibleForTesting Comparables comparables = Comparables.instance();
  @VisibleForTesting Failures failures = Failures.instance();

  @VisibleForTesting Floats() {}

  /**
   * Asserts that the actual value is equal to zero.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  public void assertIsZero(AssertionInfo info, Float actual) {
    comparables.assertEqual(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is not equal to zero.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  public void assertIsNotZero(AssertionInfo info, Float actual) {
    comparables.assertNotEqual(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is negative.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not negative: it is either equal to or greater than zero.
   */
  public void assertIsNegative(AssertionInfo info, Float actual) {
    comparables.assertLessThan(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is positive.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive: it is either equal to or less than zero.
   */
  public void assertIsPositive(AssertionInfo info, Float actual) {
    comparables.assertGreaterThan(info, actual, ZERO);
  }

  /**
   * Asserts that two floats are equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one. This method will throw a
   * {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual values are not
   * equal.
   */
  public void assertEqual(AssertionInfo info, Float actual, float expected) {
    assertNotNull(info, actual);
    if (isEqualTo(actual, expected)) return;
    failures.failure(info, isNotEqual(actual, expected));
  }

  /**
   * Verifies that two floats are equal within a positive delta.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @param delta the given delta.
   * @throws NullPointerException if the given delta is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one.
   */
  public void assertEqual(AssertionInfo info, Float actual, Float expected, Delta<Float> delta) {
    validateIsNotNull(delta);
    if (areEqual(actual, expected)) return;
    if (actual != null && expected != null && isEqualTo(actual, expected, delta)) return;
    failures.failure(info, isNotEqual(actual, expected, delta));
  }

  /**
   * Verifies that two floats are equal within a positive delta.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @param delta the given delta.
   * @throws NullPointerException if the given delta is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one.
   */
  public void assertEqual(AssertionInfo info, Float actual, float expected, Delta<Float> delta) {
    validateIsNotNull(delta);
    assertNotNull(info, actual);
    if (isEqualTo(actual, expected) || isEqualTo(actual, expected, delta)) return;
    failures.failure(info, isNotEqual(actual, expected, delta));
  }

  private void validateIsNotNull(Delta<Float> delta) {
    if (delta == null) throw new NullPointerException("The given delta should not be null");
  }

  private static boolean isEqualTo(Float actual, float expected) {
    return actual.floatValue() == expected;
  }

  private static boolean isEqualTo(Float actual, float expected, Delta<Float> delta) {
    return abs(expected - actual.floatValue()) <= delta.value().floatValue();
  }

  /**
   * Asserts that two floats are not equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the other one.
   */
  public void assertNotEqual(AssertionInfo info, Float actual, float other) {
    assertNotNull(info, actual);
    if (actual.floatValue() != other) return;
    failures.failure(info, isEqual(actual, other));
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
  public void assertLessThan(AssertionInfo info, Float actual, float other) {
    assertNotNull(info, actual);
    if (isLessThan(actual, other)) return;
    failures.failure(info, isNotLessThan(actual, other));
  }

  /**
   * Asserts that the actual value is less than or equal to the other one.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the other one.
   */
  public void assertLessThanOrEqualTo(AssertionInfo info, Float actual, float other) {
    assertNotNull(info, actual);
    if (!isGreaterThan(actual, other)) return;
    failures.failure(info, isNotLessThanOrEqualTo(actual, other));
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
  public void assertGreaterThan(AssertionInfo info, Float actual, float other) {
    assertNotNull(info, actual);
    if (isGreaterThan(actual, other)) return;
    failures.failure(info, isNotGreaterThan(actual, other));
  }

  private static boolean isGreaterThan(Float actual, float other) {
    return actual.floatValue() > other;
  }

  /**
   * Asserts that the actual value is greater than or equal to the other one.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the other one.
   */
  public void assertGreaterThanOrEqualTo(AssertionInfo info, Float actual, float other) {
    assertNotNull(info, actual);
    if (!isLessThan(actual, other)) return;
    failures.failure(info, isNotGreaterThanOrEqualTo(actual, other));
  }

  private static boolean isLessThan(Float actual, float other) {
    return actual.floatValue() < other;
  }

  private static void assertNotNull(AssertionInfo info, Float actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
