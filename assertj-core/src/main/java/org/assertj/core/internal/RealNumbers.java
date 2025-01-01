/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.error.ShouldBeFinite.shouldBeFinite;
import static org.assertj.core.error.ShouldBeInfinite.shouldBeInfinite;
import static org.assertj.core.error.ShouldNotBeFinite.shouldNotBeFinite;
import static org.assertj.core.error.ShouldNotBeInfinite.shouldNotBeInfinite;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;

/**
 * Base class of reusable assertions for real numbers (float and double).
 *
 * @author Joel Costigliola
 */
public abstract class RealNumbers<NUMBER extends Number & Comparable<NUMBER>> extends Numbers<NUMBER> {

  protected RealNumbers() {
    super();
  }

  protected RealNumbers(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  /**
   * Verifies that the actual value is equal to {@code NaN}.<br>
   * It does not rely on the custom comparisonStrategy (if one is set).
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is not equal to {@code NaN}.
   */
  public void assertIsNaN(AssertionInfo info, NUMBER actual) {
    assertEqualByComparison(info, actual, NaN());
  }

  protected BigDecimal absBigDecimalDiff(NUMBER number1, NUMBER number2) {
    BigDecimal number1AsbigDecimal = new BigDecimal(String.valueOf(number1));
    BigDecimal number2AsbigDecimal = new BigDecimal(String.valueOf(number2));
    return number1AsbigDecimal.subtract(number2AsbigDecimal).abs();
  }

  protected abstract NUMBER NaN();

  /**
   * Verifies that the actual value is not equal to {@code NaN}.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is equal to {@code NaN}.
   */
  public void assertIsNotNaN(AssertionInfo info, NUMBER actual) {
    assertNotEqualByComparison(info, actual, NaN());
  }

  @Override
  protected boolean isGreaterThan(NUMBER value, NUMBER other) {
    return value.compareTo(other) > 0;
  }

  public void assertIsFinite(AssertionInfo info, NUMBER actual) {
    assertNotNull(info, actual);
    if (isFinite(actual)) return;
    throw failures.failure(info, shouldBeFinite(actual));
  }

  protected abstract boolean isFinite(NUMBER value);

  public void assertIsNotFinite(AssertionInfo info, NUMBER actual) {
    assertNotNull(info, actual);
    if (isNotFinite(actual)) return;
    throw failures.failure(info, shouldNotBeFinite(actual));
  }

  protected abstract boolean isNotFinite(NUMBER value);

  public void assertIsInfinite(AssertionInfo info, NUMBER actual) {
    assertNotNull(info, actual);
    if (isInfinite(actual)) return;
    throw failures.failure(info, shouldBeInfinite(actual));
  }

  protected abstract boolean isInfinite(NUMBER value);

  public void assertIsNotInfinite(AssertionInfo info, NUMBER actual) {
    assertNotNull(info, actual);
    if (isNotInfinite(actual)) return;
    throw failures.failure(info, shouldNotBeInfinite(actual));
  }

  /**
   * Returns true is if the given value is Nan or Infinite, false otherwise.
   * 
   * @param value the value to check
   * @return true is if the given value is Nan or Infinite, false otherwise.
   */
  public boolean isNanOrInfinite(NUMBER value) {
    return isNaN(value) || isInfinite(value);
  }

  protected abstract boolean isNaN(NUMBER value);

  protected abstract boolean isNotInfinite(NUMBER value);
}
