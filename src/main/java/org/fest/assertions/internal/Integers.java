/*
 * Created on Oct 18, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Integer}</code>s.
 *
 * @author Alex Ruiz
 */
public class Integers {

  private static final Integer ZERO = 0;

  private static final Integers INSTANCE = new Integers();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Integers instance() {
    return INSTANCE;
  }

  private final Comparables comparables;

  private Integers() {
    this(Comparables.instance());
  }

  @VisibleForTesting Integers(Comparables comparables) {
    this.comparables = comparables;
  }

  /**
   * Asserts that the actual value is equal to zero.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  public void assertIsZero(AssertionInfo info, Integer actual) {
    comparables.assertEqual(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is not equal to zero.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  public void assertIsNotZero(AssertionInfo info, Integer actual) {
    comparables.assertNotEqual(info, actual, ZERO);
  }

  /**
   * Asserts that the actual value is negative.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not negative: it is either equal to or greater than zero.
   */
  public void assertIsNegative(AssertionInfo info, Integer actual) {
    comparables.assertLessThan(info, actual, ZERO);
  }
}
