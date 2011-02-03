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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldBeEqual.shouldBeEqual;
import static org.fest.assertions.error.ShouldBeIn.shouldBeIn;
import static org.fest.assertions.error.ShouldBeInstance.shouldBeInstance;
import static org.fest.assertions.error.ShouldBeInstanceOfAny.shouldBeInstanceOfAny;
import static org.fest.assertions.error.ShouldBeSame.shouldBeSame;
import static org.fest.assertions.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.fest.assertions.error.ShouldNotBeNull.shouldNotBeNull;
import static org.fest.assertions.error.ShouldNotBeSame.shouldNotBeSame;
import static org.fest.util.Objects.areEqual;
import static org.fest.util.ToString.toStringOf;

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

  @VisibleForTesting Failures failures = Failures.instance();

  @VisibleForTesting Objects() {}

  /**
   * Verifies that the given object is an instance of the given type.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param type the type to check the given object against.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the given object is {@code null}.
   * @throws AssertionError if the given object is not an instance of the given type.
   */
  public void assertIsInstanceOf(AssertionInfo info, Object actual, Class<?> type) {
    if (type == null) throw new NullPointerException("The given type should not be null");
    assertNotNull(info, actual);
    if (type.isInstance(actual)) return;
    throw failures.failure(info, shouldBeInstance(actual, type));
  }

  /**
   * Verifies that the given object is an instance of any of the given types.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param types the types to check the given object against.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws NullPointerException if the given array has {@code null} elements.
   * @throws AssertionError if the given object is {@code null}.
   * @throws AssertionError if the given object is not an instance of any of the given types.
   */
  public void assertIsInstanceOfAny(AssertionInfo info, Object actual, Class<?>[] types) {
    checkIsNotNullAndIsNotEmpty(types);
    assertNotNull(info, actual);
    boolean found = false;
    for (Class<?> type : types) {
      if (type == null) {
        String format = "The given array of types:<%s> should not have null elements";
        throw new NullPointerException(String.format(format, toStringOf(types)));
      }
      if (type.isInstance(actual)) {
        found = true;
        break;
      }
    }
    if (found) return;
    throw failures.failure(info, shouldBeInstanceOfAny(actual, types));
  }

  private void checkIsNotNullAndIsNotEmpty(Class<?>[] types) {
    if (types == null) throw new NullPointerException("The given array of types should not be null");
    if (types.length == 0) throw new IllegalArgumentException("The given array of types should not be empty");
  }

  /**
   * Asserts that two objects are equal.
   * @param info contains information about the assertion.
   * @param actual the "actual" object.
   * @param expected the "expected" object.
   * @throws AssertionError if {@code actual} is not equal to {@code expected}. This method will throw a
   * {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the given objects are not equal.
   */
  public void assertEqual(AssertionInfo info, Object actual, Object expected) {
    if (areEqual(expected, actual)) return;
    throw failures.failure(info, shouldBeEqual(actual, expected));
  }

  /**
   * Asserts that two objects are not equal.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @throws AssertionError if {@code actual} is equal to {@code other}.
   */
  public void assertNotEqual(AssertionInfo info, Object actual, Object other) {
    if (!areEqual(other, actual)) return;
    throw failures.failure(info, shouldNotBeEqual(actual, other));
  }

  /**
   * Asserts that the given object is {@code null}.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @throws AssertionError if the given object is not {@code null}.
   */
  public void assertNull(AssertionInfo info, Object actual) {
    if (actual == null) return;
    throw failures.failure(info, shouldBeEqual(actual, null));
  }

  /**
   * Asserts that the given object is not {@code null}.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @throws AssertionError if the given object is {@code null}.
   */
  public void assertNotNull(AssertionInfo info, Object actual) {
    if (actual != null) return;
    throw failures.failure(info, shouldNotBeNull());
  }

  /**
   * Asserts that two objects refer to the same object.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param expected the expected object.
   * @throws AssertionError if the given objects do not refer to the same object.
   */
  public void assertSame(AssertionInfo info, Object actual, Object expected) {
    if (actual == expected) return;
    throw failures.failure(info, shouldBeSame(actual, expected));
  }

  /**
   * Asserts that two objects do not refer to the same object.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param other the object to compare {@code actual} to.
   * @throws AssertionError if the given objects refer to the same object.
   */
  public void assertNotSame(AssertionInfo info, Object actual, Object other) {
    if (actual != other) return;
    throw failures.failure(info, shouldNotBeSame(actual));
  }

  /**
   * Asserts that the given object is present in the given array.
   * @param info contains information about the assertion.
   * @param actual the given object.
   * @param values the given array.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws AssertionError if the given object is not in the given array.
   */
  public void assertIsIn(AssertionInfo info, Object actual, Object[] values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, actual);
    if (isActualIn(actual, values)) return;
    throw failures.failure(info, shouldBeIn(actual, values));
  }

  private void checkIsNotNullAndNotEmpty(Object[] values) {
    if (values == null) throw new NullPointerException("The given array should not be null");
    if (values.length == 0) throw new IllegalArgumentException("The given array should not be empty");
  }

  private boolean isActualIn(Object actual, Object[] values) {
    for (Object value : values)
      if (areEqual(actual, value)) return true;
    return false;
  }
}
