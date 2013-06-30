/*
 * Created on Oct 24, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.Math.abs;

import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.internal.CommonValidations.*;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.util.Objects;
import org.assertj.core.util.VisibleForTesting;


/**
 * Reusable assertions for <code>{@link Float}</code>s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class Floats extends RealNumbers<Float> {

  private static final Floats INSTANCE = new Floats();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Floats instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Floats() {
    super();
  }

  public Floats(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected Float zero() {
    return 0.0f;
  }

  @Override
  protected Float NaN() {
    return Float.NaN;
  }

  @Override
  protected boolean isEqualTo(Float actual, Float expected, Offset<?> offset) {
    return abs(expected - actual) <= offset.value.floatValue();
  }

  /**
   * Verifies that two floats are equal within a positive offset.<br>
   * It does not rely on the custom comparisonStrategy (if one is set) because using an offset is already a specific comparison
   * strategy.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @param offset the given positive offset.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one.
   */
  // can't be defined in RealNumbers because Offset parameter must inherits from Number
  // while RealNumber parameter must inherits from Comparable (sadly Number is not Comparable)
  public void assertEqual(AssertionInfo info, Float actual, Float expected, Offset<Float> offset) {
    assertNotNull(info, actual);
    checkOffsetIsNotNull(offset);
    checkNumberIsNotNull(expected);
    // doesn't use areEqual method relying on comparisonStrategy attribute
    if (Objects.areEqual(actual, expected)) return;
    if (isEqualTo(actual, expected, offset)) return;
    throw failures.failure(info, shouldBeEqual(actual, expected, offset, abs(expected - actual)));
  }

}
