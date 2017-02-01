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

import static java.lang.Math.abs;

import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Double}</code>s.
 * 
 * @author Drummond Dawson
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
  protected Double one() {
    return 1.0d;
  }

  @Override
  protected Double NaN() {
    return Double.NaN;
  }

  @Override
  protected Double absDiff(Double actual, Double other) {
    return abs(other.doubleValue() - actual.doubleValue());
  }

}
