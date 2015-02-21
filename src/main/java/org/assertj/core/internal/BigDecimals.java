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

import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.util.*;


/**
 * Reusable assertions for <code>{@link BigDecimal}</code>s.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class BigDecimals extends Numbers<BigDecimal> {

  private static final BigDecimals INSTANCE = new BigDecimals();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
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
    return BigDecimal.ZERO;
  }

  public void assertIsCloseTo(final AssertionInfo info, final BigDecimal actual, final BigDecimal other, final Offset<BigDecimal> offset) {
    assertNotNull(info, actual);
    final BigDecimal differenceAbsoluteValue = abs(actual.subtract(other));
    if (differenceAbsoluteValue.subtract(offset.value).compareTo(BigDecimal.ZERO) <= 0) return;
    throw failures.failure(info, shouldBeEqual(actual, other, offset, differenceAbsoluteValue));
  }

  // borrowed from java 7 API ... to remove when we will be using Java 7 instead of java 6.
  private BigDecimal abs(final BigDecimal bigDecimal) {
    return (bigDecimal.signum() < 0 ? bigDecimal.negate() : bigDecimal);
  }


}
