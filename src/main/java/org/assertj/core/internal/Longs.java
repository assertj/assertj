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
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.internal.CommonValidations.checkNumberIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkOffsetIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkPercentageIsNotNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.error.ShouldBeEqualWithinPercentage;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Long}</code>s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Longs extends Numbers<Long> {

  private static final Longs INSTANCE = new Longs();

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static Longs instance() {
    return INSTANCE;
  }

  @Override
  protected Long zero() {
    return 0L;
  }

  @VisibleForTesting
  Longs() {
    super();
  }

  public Longs(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  public void assertIsCloseTo(AssertionInfo info, Long actual, Long expected, Offset<Long> offset) {
    assertNotNull(info, actual);
    checkOffsetIsNotNull(offset);
    checkNumberIsNotNull(expected);
    long absDiff = abs(expected - actual);
    if (absDiff > offset.value) throw failures.failure(info, shouldBeEqual(actual, expected, offset, absDiff));
  }

    @Override
    public void assertIsCloseToPercentage(AssertionInfo info, Long actual, Long other,
                                                    Percentage<Long> percentage) {
        assertNotNull(info, actual);
        checkPercentageIsNotNull(percentage);
        checkNumberIsNotNull(other);

        if (org.assertj.core.util.Objects.areEqual(actual, other)) return;

        Offset<Double> calculatedOffset = offset(percentage.value * other / 100d);

        Long absDiff = abs(other - actual);
        if (absDiff > calculatedOffset.value) throw failures.failure(info, ShouldBeEqualWithinPercentage
            .shouldBeEqualWithinPercentage(actual, other, percentage, absDiff));
    }

}
