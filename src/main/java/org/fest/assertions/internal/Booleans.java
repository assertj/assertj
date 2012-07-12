/*
 * Created on Oct 22, 2010
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
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.fest.assertions.error.ShouldBeEqual.shouldBeEqual;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

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
    if (actual.booleanValue() == expected) return;
    throw failures.failure(info, shouldBeEqual(actual, expected));
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
    if (actual.booleanValue() != other) return;
    throw failures.failure(info, shouldNotBeEqual(actual, other));
  }

  private static void assertNotNull(AssertionInfo info, Boolean actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
