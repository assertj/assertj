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

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.error.ShouldBeEqualWithinPercentage;
import org.assertj.core.util.*;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.internal.CommonValidations.checkNumberIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkPercentageIsNotNull;


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
    return ZERO;
  }

  public void assertIsCloseTo(final AssertionInfo info, final BigDecimal actual, final BigDecimal other, final Offset<BigDecimal> offset) {
    assertNotNull(info, actual);
    if (areNotEqual(actual, other, offset))
        throw failures.failure(info, shouldBeEqual(actual, other, offset, actual.subtract(other).abs()));
  }

    private boolean areNotEqual(BigDecimal actual, BigDecimal other,Offset<BigDecimal> offset) {
        return actual.subtract(other).abs().subtract(offset.value).compareTo(ZERO) > 0;
    }

    public void assertIsCloseToPercentage(AssertionInfo info, BigDecimal actual, BigDecimal other,
                                Percentage<BigDecimal> percentage) {
        assertNotNull(info, actual);
        checkPercentageIsNotNull(percentage);
        checkNumberIsNotNull(other);

        if (org.assertj.core.util.Objects.areEqual(actual, other)) return;

        Offset<BigDecimal> calculatedOffset = offset(percentage.value.multiply(other).divide(valueOf(100d)));

        if (areNotEqual(actual, other, calculatedOffset))
            throw failures.failure(info, ShouldBeEqualWithinPercentage
            .shouldBeEqualWithinPercentage(actual, other, percentage, actual.subtract(other).abs()));
    }
}
