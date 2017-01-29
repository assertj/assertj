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
 * Reusable assertions for <code>{@link Byte}</code>s.
 *
 * @author Drummond Dawson
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Bytes extends Numbers<Byte> {

  private static final Bytes INSTANCE = new Bytes();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static Bytes instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Bytes() {
    super();
  }

  public Bytes(ComparisonStrategy comparisonStrategy) {
    super(comparisonStrategy);
  }

  @Override
  protected Byte zero() {
    return 0;
  }

  @Override
  protected Byte one() {
    return 1;
  }

  @Override
  protected Byte absDiff(Byte actual, Byte other) {
    return (byte) Math.abs(actual - other); // TODO check corner case when diff > max byte
  }

  @Override
  protected boolean isGreaterThan(Byte value, Byte other) {
    return value > other;
  }

}
