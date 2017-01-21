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
 * Reusable assertions for <code>{@link Short}</code>s.
 * 
 * @author Drummond Dawson
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

}
