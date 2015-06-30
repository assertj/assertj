/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.Math.abs;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldBeEqualWithinPercentage.shouldBeEqualWithinPercentage;
import static org.assertj.core.internal.CommonValidations.checkNumberIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkPercentageIsNotNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;

/**
 * Base class of reusable assertions for numbers.
 * 
 * @author Joel Costigliola
 * @author Nicolas François
 */
public abstract class Numbers<NUMBER extends Number & Comparable<NUMBER>> extends Comparables {

  public Numbers() {
    super();
  }

  public Numbers(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  protected abstract NUMBER zero();

  /**
   * Asserts that the actual value is equal to zero.<br>
   * It does not rely on the custom comparisonStrategy (if one is set).
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  public void assertIsZero(AssertionInfo info, NUMBER actual) {
    assertEqualByComparison(info, actual, zero());
  }

  /**
   * Asserts that the actual value is not equal to zero.<br>
   * It does not rely on the custom comparisonStrategy (if one is set).
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  public void assertIsNotZero(AssertionInfo info, NUMBER actual) {
    assertNotEqualByComparison(info, actual, zero());
  }

  /**
   * Asserts that the actual value is negative.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not negative: it is either equal to or greater than zero.
   */
  public void assertIsNegative(AssertionInfo info, NUMBER actual) {
    assertLessThan(info, actual, zero());
  }

  /**
   * Asserts that the actual value is positive.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive: it is either equal to or less than zero.
   */
  public void assertIsPositive(AssertionInfo info, NUMBER actual) {
    assertGreaterThan(info, actual, zero());
  }

  /**
   * Asserts that the actual value is not negative.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is negative.
   */
  public void assertIsNotNegative(AssertionInfo info, NUMBER actual) {
    assertGreaterThanOrEqualTo(info, actual, zero());
  }

  /**
   * Asserts that the actual value is not positive.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is positive.
   */
  public void assertIsNotPositive(AssertionInfo info, NUMBER actual) {
    assertLessThanOrEqualTo(info, actual, zero());
  }

  /**
   * Asserts that the actual value is in [start, end] range (start included, end included).
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is positive.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in [start, end] range.
   */
  public void assertIsBetween(AssertionInfo info, NUMBER actual, NUMBER start, NUMBER end) {
    assertIsBetween(info, actual, start, end, true, true);
  }

  /**
   * Asserts that the actual value is in ]start, end[ range (start excluded, end excluded).
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param start the start value (exclusive), expected not to be null.
   * @param end the end value (exclusive), expected not to be null.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ range.
   */
  public void assertIsStrictlyBetween(AssertionInfo info, NUMBER actual, NUMBER start, NUMBER end) {
    assertIsBetween(info, actual, start, end, false, false);
  }

  /**
   * Asserts that the actual value is close to the offset.
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the expected value.
   * @param offset the given positive offset.
   */
  public abstract void assertIsCloseTo(final AssertionInfo info, final NUMBER actual, final NUMBER other,
                                       final Offset<NUMBER> offset);

  /**
   * Asserts that the actual value is close to the an offset expressed as an percentage value.
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the expected value.
   * @param percentage the given percentage between 0 and 100.
   */
  public void assertIsCloseToPercentage(final AssertionInfo info, final NUMBER actual, final NUMBER other,
                                        final Percentage percentage) {
    isCloseToPercentageCommonChecks(info, actual, other, percentage);
    Offset<Double> offset = computeOffset(other, percentage);
    double absDiff = absDiff(actual, other);
    if (absDiff > offset.value)
      throw failures.failure(info, shouldBeEqualWithinPercentage(actual, other, percentage, absDiff));
  }

  protected void isCloseToPercentageCommonChecks(AssertionInfo info, NUMBER actual, NUMBER other, Percentage percentage) {
    assertNotNull(info, actual);
    checkPercentageIsNotNull(percentage);
    checkNumberIsNotNull(other);
  }

  protected double absDiff(NUMBER actual, NUMBER expected) {
    return abs(expected.doubleValue() - actual.doubleValue());
  }

  private Offset<Double> computeOffset(NUMBER referenceValue, Percentage percentage) {
    return offset(percentage.value * referenceValue.doubleValue() / 100d);
  }

}