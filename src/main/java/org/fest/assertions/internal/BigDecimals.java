/*
 * Created on Feb 8, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link BigDecimal}</code>s.
 *
 * @author Yvonne Wang
 */
public class BigDecimals {

  private static final BigDecimals INSTANCE = new BigDecimals();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static BigDecimals instance() {
    return INSTANCE;
  }

  @VisibleForTesting Comparables comparables = Comparables.instance();
  @VisibleForTesting Failures failures = Failures.instance();

  @VisibleForTesting BigDecimals() {}

  /**
   * Asserts that the actual value is equal to zero.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  public void assertIsZero(AssertionInfo info, BigDecimal actual) {
    comparables.assertEqualByComparison(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is not equal to zero.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  public void assertIsNotZero(AssertionInfo info, BigDecimal actual) {
    comparables.assertNotEqualByComparison(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is negative.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not negative: it is either equal to or greater than zero.
   */
  public void assertIsNegative(AssertionInfo info, BigDecimal actual) {
    comparables.assertLessThan(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is positive.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive: it is either equal to or less than zero.
   */
  public void assertIsPositive(AssertionInfo info, BigDecimal actual) {
    comparables.assertGreaterThan(info, actual, ZERO);
  }
}
