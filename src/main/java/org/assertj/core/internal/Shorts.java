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
import static org.assertj.core.error.ShouldBeEqualWithinPercentage.shouldBeEqualWithinPercentage;
import static org.assertj.core.internal.CommonValidations.checkNumberIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkOffsetIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkPercentageIsNotNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Short}</code>s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Shorts extends Numbers<Short> {

  private static final Shorts INSTANCE = new Shorts();

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static Shorts instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Shorts() {
    super();
  }

  public Shorts(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected Short zero() {
    return 0;
  }

  @Override
  public void assertIsCloseTo(AssertionInfo info, Short actual, Short expected, Offset<Short> offset) {
    assertNotNull(info, actual);
    checkOffsetIsNotNull(offset);
    checkNumberIsNotNull(expected);
    Short absDiff = (short) abs(expected - actual);
    if (absDiff > offset.value) throw failures.failure(info, shouldBeEqual(actual, expected, offset, absDiff));
  }

  @Override
  public void assertIsCloseToPercentage(AssertionInfo info, Short actual, Short other,
                                        Percentage percentage) {
    assertNotNull(info, actual);
    checkPercentageIsNotNull(percentage);
    checkNumberIsNotNull(other);
    Offset<Double> calculatedOffset = offset(abs(percentage.value * other / 100d));
    short absDiff = (short) abs(other - actual);
    if (absDiff > calculatedOffset.value)
      throw failures.failure(info, shouldBeEqualWithinPercentage(actual, other, percentage, absDiff));
  }
}
