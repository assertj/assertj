/*
 * Created on Sep 17, 2010
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

import static org.fest.assertions.error.WhenDoesNotContainErrorFactory.errorWhenDoesNotContain;
import static org.fest.assertions.error.WhenEmptyErrorFactory.errorWhenEmpty;
import static org.fest.assertions.error.WhenNotEmptyErrorFactory.errorWhenNotEmpty;
import static org.fest.assertions.error.WhenNotNullOrEmptyErrorFactory.errorWhenNotNullOrEmpty;
import static org.fest.assertions.error.WhenSizeNotEqualErrorFactory.errorWhenSizeNotEqual;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Collection}</code>s.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Collections {

  private static final Collections INSTANCE = new Collections();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Collections instance() {
    return INSTANCE;
  }

  private final Failures failures;

  private Collections() {
    this(Failures.instance());
  }

  @VisibleForTesting Collections(Failures failures) {
    this.failures = failures;
  }

  /**
   * Asserts that the given <code>{@link Collection}</code> is {@code null} or empty.
   * @param info contains information about the assertion.
   * @param actual the given {@code Collection}.
   * @throws AssertionError if the given {@code Collection} is not {@code null} *and* contains one or more elements.
   */
  public void assertNullOrEmpty(AssertionInfo info, Collection<?> actual) {
    if (actual == null || actual.isEmpty()) return;
    throw failures.failure(info, errorWhenNotNullOrEmpty(actual));
  }

  /**
   * Asserts that the given {@code Collection} is empty.
   * @param info contains information about the assertion.
   * @param actual the given {@code Collection}.
   * @throws AssertionError if the given {@code Collection} is {@code null}.
   * @throws AssertionError if the given {@code Collection} is not empty.
   */
  public void assertEmpty(AssertionInfo info, Collection<?> actual) {
    assertNotNull(info, actual);
    if (actual.isEmpty()) return;
    throw failures.failure(info, errorWhenNotEmpty(actual));
  }

  /**
   * Asserts that the given {@code Collection} is not empty.
   * @param info contains information about the assertion.
   * @param actual the given {@code Collection}.
   * @throws AssertionError if the given {@code Collection} is {@code null}.
   * @throws AssertionError if the given {@code Collection} is empty.
   */
  public void assertNotEmpty(AssertionInfo info, Collection<?> actual) {
    assertNotNull(info, actual);
    if (!actual.isEmpty()) return;
    throw failures.failure(info, errorWhenEmpty());
  }

  /**
   * Asserts that the number of elements in the given {@code Collection} is equal to the expected one.
   * @param info contains information about the assertion.
   * @param actual the given {@code Collection}.
   * @param expectedSize the expected size of the {@code actual}.
   * @throws AssertionError if the given {@code Collection} is {@code null}.
   * @throws AssertionError if the number of elements in the given {@code Collection} is different than the expected
   * one.
   */
  public void assertHasSize(AssertionInfo info, Collection<?> actual, int expectedSize) {
    assertNotNull(info, actual);
    int actualSize = actual.size();
    if (actualSize == expectedSize) return;
    throw failures.failure(info, errorWhenSizeNotEqual(actual, actualSize, expectedSize));
  }

  /**
   * Asserts that the given {@code Collection} contains the given values, in any order.
   * @param info contains information about the assertion.
   * @param actual the given {@code Collection}.
   * @param values the values that are expected to be in the given {@code Collection}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given {@code Collection} is {@code null}.
   * @throws AssertionError if the given {@code Collection} does not contain the given values.
   */
  public void assertContains(AssertionInfo info, Collection<?> actual, Object[] values) {
    isNotEmptyOrNull(values);
    assertNotNull(info, actual);
    Collection<Object> notFound = new LinkedHashSet<Object>();
    for (Object value : values) if (!actual.contains(value)) notFound.add(value);
    if (notFound.isEmpty()) return;
    throw failures.failure(info, errorWhenDoesNotContain(actual, values, notFound));
  }

  /**
   * Asserts that the given {@code Collection} contains only the given values, in any order.
   * @param info contains information about the assertion.
   * @param actual the given {@code Collection}.
   * @param values the values that are expected to be in the given {@code Collection}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given {@code Collection} is {@code null}.
   * @throws AssertionError if the given {@code Collection} does not contain the given values or if the given
   * {@code Collection} contains values that are not in the given array.
   */
  public void assertContainsOnly(AssertionInfo info, Collection<?> actual, Object[] values) {
    isNotEmptyOrNull(values);
    assertNotNull(info, actual);
    // TODO finish

  }

  private void isNotEmptyOrNull(Object[] values) {
    if (values == null) throw new NullPointerException("The array of values to evaluate should not be null");
    if (values.length == 0) throw new IllegalArgumentException("The array of values to evaluate should not be empty");
  }

  private void assertNotNull(AssertionInfo info, Collection<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
