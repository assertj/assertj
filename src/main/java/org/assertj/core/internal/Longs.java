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

import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Long}</code>s.
 * 
 * @author Drummond Dawson
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

  @VisibleForTesting
  Longs() {
    super();
  }

  public Longs(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected Long zero() {
    return 0L;
  }

  @Override
  protected Long one() {
    return 1L;
  }

  @Override
  protected Long absDiff(Long actual, Long other) {
    return Math.abs(actual - other);
  }

  @Override
  protected boolean isGreaterThan(Long value, Long other) {
    return value > other;
  }

}
