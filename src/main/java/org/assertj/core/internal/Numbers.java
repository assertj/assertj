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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.Math.abs;
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.error.ShouldBeEqualWithinPercentage.shouldBeEqualWithinPercentage;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.error.ShouldNotBeEqualWithinPercentage.shouldNotBeEqualWithinPercentage;
import static org.assertj.core.internal.CommonValidations.checkNumberIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkOffsetIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkPercentageIsNotNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.util.Objects;

/**
 * Base class of reusable assertions for numbers.
 * 
 * @author Drummond Dawson
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

  protected abstract NUMBER one();

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
   * Asserts that the actual value is equal to one.<br>
   * It does not rely on the custom comparisonStrategy (if one is set).
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to one.
   */
  public void assertIsOne(AssertionInfo info, NUMBER actual) {
    assertEqualByComparison(info, actual, one());
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
   * @param start range start value
   * @param end range end value
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
   * @param expected the value to compare actual too.
   * @param offset the given positive offset.
   */
  public void assertIsCloseTo(final AssertionInfo info, final NUMBER actual, final NUMBER expected,
                              final Offset<NUMBER> offset) {
    assertNotNull(info, actual);
    checkOffsetIsNotNull(offset);
    checkNumberIsNotNull(expected);

    if (Objects.areEqual(actual, expected)) return; // handles correctly NaN comparison
    if (isGreaterThan(absDiff(actual, expected), offset.value))
      throw failures.failure(info, shouldBeEqual(actual, expected, offset, absDiff(actual, expected)));
  }

  /**
   * Asserts that the actual value is not close to the offset.
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the value to compare actual too.
   * @param offset the given positive offset.
   */
  public void assertIsNotCloseTo(final AssertionInfo info, final NUMBER actual, final NUMBER expected,
                                 final Offset<NUMBER> offset) {
    assertNotNull(info, actual);
    checkOffsetIsNotNull(offset);
    checkNumberIsNotNull(expected);

    NUMBER diff = absDiff(actual, expected);
    if (!isGreaterThan(diff, offset.value) || Objects.areEqual(actual, expected))
      throw failures.failure(info, shouldNotBeEqual(actual, expected, offset, diff));
  }

  /**
   * Asserts that the actual value is close to the an offset expressed as an percentage value.
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the expected value.
   * @param percentage the given positive percentage.
   */
  public void assertIsCloseToPercentage(final AssertionInfo info, final NUMBER actual, final NUMBER other,
                                        final Percentage percentage) {
    assertNotNull(info, actual);
    checkPercentageIsNotNull(percentage);
    checkNumberIsNotNull(other);

    if (Objects.areEqual(actual, other)) return;
    double acceptableDiff = abs(percentage.value * other.doubleValue() / 100d);
    double actualDiff = absDiff(actual, other).doubleValue();
    if (actualDiff > acceptableDiff || Double.isNaN(actualDiff) || Double.isInfinite(actualDiff))
      throw failures.failure(info, shouldBeEqualWithinPercentage(actual, other, percentage, absDiff(actual, other)));
  }

  /**
   * Asserts that the actual value is not close to the an offset expressed as an percentage value.
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the expected value.
   * @param percentage the given positive percentage.
   */
  public void assertIsNotCloseToPercentage(final AssertionInfo info, final NUMBER actual, final NUMBER other,
                                           final Percentage percentage) {
    assertNotNull(info, actual);
    checkPercentageIsNotNull(percentage);
    checkNumberIsNotNull(other);

    double diff = abs(percentage.value * other.doubleValue() / 100d);
    boolean areEqual = Objects.areEqual(actual, other);
    if (!areEqual && Double.isInfinite(diff)) return;
    if (absDiff(actual, other).doubleValue() <= diff || areEqual)
      throw failures.failure(info, shouldNotBeEqualWithinPercentage(actual, other, percentage, absDiff(actual, other)));
  }

  protected abstract NUMBER absDiff(final NUMBER actual, final NUMBER other);

  protected abstract boolean isGreaterThan(final NUMBER value, final NUMBER other);

}
