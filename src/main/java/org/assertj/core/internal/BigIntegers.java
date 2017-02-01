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

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;

import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link BigInteger}</code>s.
 */
public class BigIntegers extends Numbers<BigInteger> {

  private static final BigIntegers INSTANCE = new BigIntegers();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   *
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static BigIntegers instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  BigIntegers() {
    super();
  }

  public BigIntegers(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected BigInteger zero() {
    return ZERO;
  }

  @Override
  protected BigInteger one() {
    return ONE;
  }

  @Override
  protected BigInteger absDiff(BigInteger actual, BigInteger other) {
    return actual.subtract(other).abs();
  }

  @Override
  protected boolean isGreaterThan(BigInteger value, BigInteger other) {
    return value.subtract(other).compareTo(zero()) > 0;
  }
}
