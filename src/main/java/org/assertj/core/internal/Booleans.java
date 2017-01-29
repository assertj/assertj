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

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;


/**
 * Reusable assertions for <code>{@link Boolean}</code>s.
 * 
 * @author Alex Ruiz
 */
public class Booleans {

  private static final Booleans INSTANCE = new Booleans();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Booleans instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Booleans() {}

  /**
   * Asserts that two booleans are equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one. This method will throw a
   *           {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual values are not
   *           equal.
   */
  public void assertEqual(AssertionInfo info, Boolean actual, boolean expected) {
    assertNotNull(info, actual);
    if (actual == expected) return;
    throw failures.failure(info, shouldBeEqual(actual, expected, info.representation()));
  }

  /**
   * Asserts that two longs are not equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the other one.
   */
  public void assertNotEqual(AssertionInfo info, Boolean actual, boolean other) {
    assertNotNull(info, actual);
    if (actual != other) return;
    throw failures.failure(info, shouldNotBeEqual(actual, other));
  }

  private static void assertNotNull(AssertionInfo info, Boolean actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
