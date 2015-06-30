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
import static org.assertj.core.internal.CommonValidations.checkNumberIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkOffsetIsNotNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.util.Objects;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Double}</code>s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Doubles extends RealNumbers<Double> {

  private static final Doubles INSTANCE = new Doubles();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * 
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Doubles instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Doubles() {
    super();
  }

  public Doubles(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected Double zero() {
    return 0.0d;
  }

  @Override
  protected Double NaN() {
    return Double.NaN;
  }

  /**
   * Verifies that two floats are equal within a positive offset.<br>
   * It does not rely on the custom comparisonStrategy (if one is set) because using an offset is already a specific
   * comparison
   * strategy.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @param offset the given positive offset.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one.
   */
  // can't be defined in RealNumbers because Offset parameter must inherits from Number
  // while RealNumber parameter must inherits from Comparable (sadly Number is not Comparable)
  public void assertEqual(AssertionInfo info, Double actual, Double expected, Offset<Double> offset) {
    checkOffsetIsNotNull(offset);
    checkNumberIsNotNull(expected);
    assertNotNull(info, actual);
    // doesn't use areEqual method relying on comparisonStrategy attribute
    if (Objects.areEqual(actual, expected)) return;
    if (isEqualTo(actual, expected, offset)) return;
    throw failures.failure(info, shouldBeEqual(actual, expected, offset, absDiff(actual, expected)));
  }

  @Override
  protected boolean isEqualTo(Double actual, Double expected, Offset<?> offset) {
    return absDiff(actual, expected) <= offset.value.doubleValue();
  }

  @Override
  public void assertIsCloseTo(final AssertionInfo info, final Double actual, final Double other,
                              final Offset<Double> offset) {
    assertEqual(info, actual, other, offset);
  }

}
