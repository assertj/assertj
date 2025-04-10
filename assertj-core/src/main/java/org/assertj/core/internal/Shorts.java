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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal;

import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;

import static java.lang.Math.abs;

/**
 * Reusable assertions for <code>{@link Short}</code>s.
 * 
 * @author Drummond Dawson
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Cal027
 */
public class Shorts extends Numbers<Short> implements WholeNumbers<Short> {

  private static final Shorts INSTANCE = new Shorts(StandardComparisonStrategy.instance());

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static Shorts instance() {
    return INSTANCE;
  }

  public Shorts(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected Short zero() {
    return 0;
  }

  @Override
  protected Short one() {
    return 1;
  }

  @Override
  protected Short absDiff(Short actual, Short other) {
    return (short) abs(other - actual);
  }

  @Override
  protected boolean isGreaterThan(Short value, Short other) {
    return value > other;
  }

  @Override
  public boolean isEven(Short number) {
    return (number & one()) == zero();
  }
}
