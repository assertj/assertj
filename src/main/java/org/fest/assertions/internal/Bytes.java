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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.IsEqual.isEqual;
import static org.fest.assertions.error.IsNotEqual.isNotEqual;
import static org.fest.assertions.error.IsNotGreaterThan.isNotGreaterThan;
import static org.fest.assertions.error.IsNotGreaterThanOrEqualTo.isNotGreaterThanOrEqualTo;
import static org.fest.assertions.error.IsNotLessThan.isNotLessThan;
import static org.fest.assertions.error.IsNotLessThanOrEqualTo.isNotLessThanOrEqualTo;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Byte}</code>s.
 *
 * @author Alex Ruiz
 */
public class Bytes {

  private static final Byte ZERO = 0;

  private static final Bytes INSTANCE = new Bytes();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Bytes instance() {
    return INSTANCE;
  }

  @VisibleForTesting Comparables comparables = Comparables.instance();
  @VisibleForTesting Failures failures = Failures.instance();

  @VisibleForTesting Bytes() {}

  /**
   * Asserts that the actual value is equal to zero.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  public void assertIsZero(AssertionInfo info, Byte actual) {
    comparables.assertEqual(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is not equal to zero.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  public void assertIsNotZero(AssertionInfo info, Byte actual) {
    comparables.assertNotEqual(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is negative.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not negative: it is either equal to or greater than zero.
   */
  public void assertIsNegative(AssertionInfo info, Byte actual) {
    comparables.assertLessThan(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is positive.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive: it is either equal to or less than zero.
   */
  public void assertIsPositive(AssertionInfo info, Byte actual) {
    comparables.assertGreaterThan(info, actual, ZERO);
  }

  /**
   * Asserts that two bytes are equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one. This method will throw a
   * {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual values are not
   * equal.
   */
  public void assertEqual(AssertionInfo info, Byte actual, byte expected) {
    assertNotNull(info, actual);
    if (actual.byteValue() == expected) return;
    failures.failure(info, isNotEqual(actual, expected));
  }

  /**
   * Asserts that two bytes are not equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the other one.
   */
  public void assertNotEqual(AssertionInfo info, Byte actual, byte other) {
    assertNotNull(info, actual);
    if (actual.byteValue() != other) return;
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
  public void assertLessThan(AssertionInfo info, Byte actual, byte other) {
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
  public void assertLessThanOrEqualTo(AssertionInfo info, Byte actual, byte other) {
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
  public void assertGreaterThan(AssertionInfo info, Byte actual, byte other) {
    assertNotNull(info, actual);
    if (isGreaterThan(actual, other)) return;
    failures.failure(info, isNotGreaterThan(actual, other));
  }

  private static boolean isGreaterThan(Byte actual, byte other) {
    return actual.byteValue() > other;
  }

  /**
   * Asserts that the actual value is greater than or equal to the other one.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the other one.
   */
  public void assertGreaterThanOrEqualTo(AssertionInfo info, Byte actual, byte other) {
    assertNotNull(info, actual);
    if (!isLessThan(actual, other)) return;
    failures.failure(info, isNotGreaterThanOrEqualTo(actual, other));
  }

  private static boolean isLessThan(Byte actual, byte other) {
    return actual.byteValue() < other;
  }

  private static void assertNotNull(AssertionInfo info, Byte actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
