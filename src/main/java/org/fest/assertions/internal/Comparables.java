/*
 * Created on Oct 17, 2010
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

import static org.fest.assertions.error.ErrorWhenObjectsAreNotEqual.errorWhenNotEqual;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Comparable}</code>s.
 *
 * @author Alex Ruiz
 */
public class Comparables {

  public static final Comparables INSTANCE = new Comparables();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Comparables instance() {
    return INSTANCE;
  }

  private final Failures failures;

  private Comparables() {
    this(Failures.instance());
  }

  @VisibleForTesting Comparables(Failures failures) {
    this.failures = failures;
  }

  /**
   * Asserts that two <code>{@link Comparable}</code>s are equal by invoking
   * <code>{@link Comparable#compareTo(Object)}</code>.
   * @param <T> used to guarantee that two objects of the same type are being compared against each other.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one. This method will throw a
   * {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual
   * values are not equal.
   */
  public <T extends Comparable<T>> void assertEqual(AssertionInfo info, T actual, T expected) {
    Objects.instance().assertNotNull(info, actual);
    if (actual.compareTo(expected) == 0) return;
    throw failures.failure(info, errorWhenNotEqual(actual, expected));
  }
}
