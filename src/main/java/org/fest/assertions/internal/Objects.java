/*
 * Created on Aug 4, 2010
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

import static org.fest.assertions.error.ErrorWhenObjectsAreEqual.errorWhenEqual;
import static org.fest.assertions.error.ErrorWhenObjectsAreNotEqual.errorWhenNotEqual;
import static org.fest.assertions.error.ErrorWhenObjecsAreNotSame.errorWhenNotSame;
import static org.fest.assertions.error.ErrorWhenObjectIsNull.errorWhenNull;
import static org.fest.assertions.error.ErrorWhenObjectsAreSame.errorWhenSame;
import static org.fest.util.Objects.areEqual;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for {@code Object}s.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Objects {

  private static final Objects INSTANCE = new Objects();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Objects instance() {
    return INSTANCE;
  }

  private final Failures failures;

  private Objects() {
    this(Failures.instance());
  }

  @VisibleForTesting Objects(Failures failures) {
    this.failures = failures;
  }

  /**
   * Asserts that two objects are equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @throws AssertionError if the actual value is not equal to the expected one. This method will throw a
   * {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual values are not
   * equal.
   */
  public void assertEqual(AssertionInfo info, Object actual, Object expected) {
    if (areEqual(expected, actual)) return;
    throw failures.failure(info, errorWhenNotEqual(actual, expected));
  }

  /**
   * Asserts that two objects are not equal.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is equal to the other one.
   */
  public void assertNotEqual(AssertionInfo info, Object actual, Object other) {
    if (!areEqual(other, actual)) return;
    throw failures.failure(info, errorWhenEqual(actual, other));
  }

  /**
   * Asserts that the given object is {@code null}.
   * @param info contains information about the assertion.
   * @param obj the given object.
   * @throws AssertionError if the given value is not {@code null}.
   */
  public void assertNull(AssertionInfo info, Object obj) {
    if (obj == null) return;
    throw failures.failure(info, errorWhenNotEqual(obj, null));
  }

  /**
   * Asserts that the given object is not {@code null}.
   * @param info contains information about the assertion.
   * @param obj the given object.
   * @throws AssertionError if the given value is {@code null}.
   */
  public void assertNotNull(AssertionInfo info, Object obj) {
    if (obj != null) return;
    throw failures.failure(info, errorWhenNull());
  }

  /**
   * Asserts that two objects refer to the same object.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @throws AssertionError if the given objects do not refer to the same object.
   */
  public void assertSame(AssertionInfo info, Object actual, Object expected) {
    if (actual == expected) return;
    throw failures.failure(info, errorWhenNotSame(actual, expected));
  }

  /**
   * Asserts that two objects do not refer to the same object.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the given objects refer to the same object.
   */
  public void assertNotSame(AssertionInfo info, Object actual, Object other) {
    if (actual != other) return;
    throw failures.failure(info, errorWhenSame(actual));
  }
}
