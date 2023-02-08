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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.error.ShouldHaveScale.shouldHaveScale;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link BigDecimal}</code>s.
 *
 * @author Drummond Dawson
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class BigDecimals extends Numbers<BigDecimal> {

  private static final BigDecimals INSTANCE = new BigDecimals();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * 
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static BigDecimals instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  BigDecimals() {
    super();
  }

  public BigDecimals(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected BigDecimal zero() {
    return ZERO;
  }

  @Override
  protected BigDecimal one() {
    return ONE;
  }

  @Override
  protected BigDecimal absDiff(BigDecimal actual, BigDecimal other) {
    return actual.subtract(other).abs();
  }

  @Override
  protected boolean isGreaterThan(BigDecimal value, BigDecimal other) {
    return value.subtract(other).compareTo(ZERO) > 0;
  }

  @Override
  protected boolean areEqual(BigDecimal value1, BigDecimal value2) {
    if (value1 == null) return value2 == null;
    // we know value1 is not null
    if (value2 == null) return false;
    return value1.compareTo(value2) == 0;
  }

  public void assertHasScale(AssertionInfo info, BigDecimal actual, int expectedScale) {
    assertNotNull(info, actual);
    if (areEqual(actual.scale(), expectedScale)) return;
    throw failures.failure(info, shouldHaveScale(actual, expectedScale));
  }

}
