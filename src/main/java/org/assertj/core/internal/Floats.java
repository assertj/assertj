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

import static java.lang.Math.abs;

import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Float}</code>s.
 * 
 * @author Drummond Dawson
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Jin Kwon
 */
public class Floats extends RealNumbers<Float> {

  private static final Floats INSTANCE = new Floats();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * 
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
  protected Float one() {
    return 1.0f;
  }

  @Override
  protected Float NaN() {
    return Float.NaN;
  }

  @Override
  protected Float absDiff(Float actual, Float other) {
    return abs(other.floatValue() - actual.floatValue());
  }

  /**
   * {@inheritDoc}
   * @param value {@inheritDoc}
   * @return {@inheritDoc}
   * @see Float#isFinite(float)
   */
  @Override
  protected boolean isFinite(Float value) {
    return Float.isFinite(value);
  }

  /**
   * {@inheritDoc}
   * @param value {@inheritDoc}
   * @return {@inheritDoc}
   * @see Float#isInfinite(float)
   */
  @Override
  protected boolean isInfinite(Float value) {
    return Float.isInfinite(value);
  }

  /**
   * {@inheritDoc}
   * @return {@link Float#NEGATIVE_INFINITY}.
   */
  @Override
  protected Float NEGATIVE_INFINITY() {
    return Float.NEGATIVE_INFINITY;
  }

  /**
   * {@inheritDoc}
   * @return {@link Float#POSITIVE_INFINITY}.
   */
  @Override
  protected Float POSITIVE_INFINITY() {
    return Float.POSITIVE_INFINITY;
  }

}
