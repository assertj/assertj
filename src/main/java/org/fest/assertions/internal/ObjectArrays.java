/*
 * Created on Nov 3, 2010
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

import static org.fest.assertions.error.Contains.contains;
import static org.fest.assertions.error.DoesNotContain.doesNotContain;
import static org.fest.assertions.error.DoesNotContainExclusively.doesNotContainExclusively;
import static org.fest.assertions.error.DoesNotHaveSize.doesNotHaveSize;
import static org.fest.assertions.error.HasDuplicates.hasDuplicates;
import static org.fest.assertions.error.IsEmpty.isEmpty;
import static org.fest.assertions.error.IsNotEmpty.isNotEmpty;
import static org.fest.assertions.error.IsNotNullOrEmpty.isNotNullOrEmpty;
import static org.fest.assertions.internal.Collections.containsExclusively;
import static org.fest.util.Arrays.isEmpty;
import static org.fest.util.Collections.*;
import static org.fest.util.Objects.areEqual;

import java.util.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for arrays of objects.
 *
 * @author Alex Ruiz
 */
public class ObjectArrays {

  private static final ObjectArrays INSTANCE = new ObjectArrays();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static ObjectArrays instance() {
    return INSTANCE;
  }

  private final Failures failures;

  private ObjectArrays() {
    this(Failures.instance());
  }

  @VisibleForTesting ObjectArrays(Failures failures) {
    this.failures = failures;
  }

  /**
   * Asserts that the given array is {@code null} or empty.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is not {@code null} *and* contains one or more elements.
   */
  public void assertNullOrEmpty(AssertionInfo info, Object[] actual) {
    if (actual == null || isArrayEmpty(actual)) return;
    throw failures.failure(info, isNotNullOrEmpty(actual));
  }

  /**
   * Asserts that the given array is empty.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array is not empty.
   */
  public void assertEmpty(AssertionInfo info, Object[] actual) {
    assertNotNull(info, actual);
    if (isArrayEmpty(actual)) return;
    throw failures.failure(info, isNotEmpty(actual));
  }

  /**
   * Asserts that the given array is not empty.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array is empty.
   */
  public void assertNotEmpty(AssertionInfo info, Object[] actual) {
    assertNotNull(info, actual);
    if (!isArrayEmpty(actual)) return;
    throw failures.failure(info, isEmpty());
  }

  private static boolean isArrayEmpty(Object[] actual) {
    return actual.length == 0;
  }

  /**
   * Asserts that the number of elements in the given array is equal to the expected one.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param expectedSize the expected size of the {@code actual}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of elements in the given array is different than the expected
   * one.
   */
  public void assertHasSize(AssertionInfo info, Object[] actual, int expectedSize) {
    assertNotNull(info, actual);
    if (actual.length == expectedSize) return;
    throw failures.failure(info, doesNotHaveSize(actual, expectedSize));
  }

  /**
   * Asserts that the given array contains the given values, in any order.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param values the values that are expected to be in the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not contain the given values.
   */
  public void assertContains(AssertionInfo info, Object[] actual, Object[] values) {
    isNotEmptyOrNull(values);
    assertNotNull(info, actual);
    Set<Object> notFound = new LinkedHashSet<Object>();
    for (Object value : values) if (!arrayContains(actual, value)) notFound.add(value);
    if (notFound.isEmpty()) return;
    throw failures.failure(info, doesNotContain(actual, values, notFound));
  }

  /**
   * Asserts that the given array contains only the given values and nothing else, in any order.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param values the values that are expected to be in the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not contain the given values or if the given
   * array contains values that are not in the given array.
   */
  public void assertContainsExclusively(AssertionInfo info, Object[] actual, Object[] values) {
    isNotEmptyOrNull(values);
    assertNotNull(info, actual);
    Set<Object> notExpected = set(actual);
    Set<Object> notFound = containsExclusively(notExpected, values);
    if (notExpected.isEmpty() && notFound.isEmpty()) return;
    throw failures.failure(info, doesNotContainExclusively(actual, values, notExpected, notFound));
  }

  /**
   * Asserts that the given array does not contain the given values.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param values the values that are expected not to be in the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array contains any of given values.
   */
  public void assertDoesNotContain(AssertionInfo info, Object[] actual, Object[] values) {
    isNotEmptyOrNull(values);
    assertNotNull(info, actual);
    Set<Object> found = new LinkedHashSet<Object>();
    for (Object o: values) if (arrayContains(actual, o)) found.add(o);
    if (found.isEmpty()) return;
    throw failures.failure(info, contains(actual, values, found));
  }

  private static boolean arrayContains(Object[] array, Object o) {
    for (Object value : array) if (areEqual(value, o)) return true;
    return false;
  }

  private void isNotEmptyOrNull(Object[] values) {
    if (values == null) throw new NullPointerException("The array of values to evaluate should not be null");
    if (isEmpty(values)) throw new IllegalArgumentException("The array of values to evaluate should not be empty");
  }

  /**
   * Asserts that the given array does not have duplicate values.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array contains duplicate values.
   */
  public void assertDoesHaveDuplicates(AssertionInfo info, Object[] actual) {
    assertNotNull(info, actual);
    Collection<?> duplicates = duplicatesFrom(list(actual));
    if (isEmpty(duplicates)) return;
    throw failures.failure(info, hasDuplicates(actual, duplicates));
  }

  private void assertNotNull(AssertionInfo info, Object[] actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
