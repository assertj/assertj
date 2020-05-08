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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Integer}</code>s.
 * 
 * @author Drummond Dawson
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Cal027
 */
public class Integers extends Numbers<Integer> {

  private static final Integers INSTANCE = new Integers();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * 
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Integers instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Integers() {
    super();
  }

  public Integers(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected Integer zero() {
    return 0;
  }

  @Override
  protected Integer one() {
    return 1;
  }

  @Override
  protected Integer absDiff(Integer actual, Integer other) {
    return Math.abs(actual - other);
  }

  @Override
  protected boolean isGreaterThan(Integer value, Integer other) {
    return value > other;
  }

  /**
   * Asserts that the actual Integer value is even.
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is negative.
   */
  public void assertIsEven(AssertionInfo info, Integer actual) {
    Integer lastDigit = actual & one();
    assertIsZero(info, lastDigit);
  }

  /**
   * Asserts that the actual Integer value is odd.
   *
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is negative.
   */
  public void assertIsOdd(AssertionInfo info, Integer actual) {
    Integer lastDigit = actual & one();
    assertIsNotZero(info, lastDigit);
  }
}
