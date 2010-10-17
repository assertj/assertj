/*
 * Created on Oct 1, 2010
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
package org.fest.assertions.error;

import static org.fest.util.Objects.*;

import org.fest.assertions.description.Description;
import org.fest.assertions.group.IsNullOrEmptyChecker;
import org.fest.util.VisibleForTesting;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies a group of elements
 * contains only a given set of values and nothing else failed. A group of elements can be a collection, an array or a
 * {@code String}.
 *
 * @author Alex Ruiz
 */
public class ErrorWhenGroupDoesNotContainValuesExclusively implements AssertionErrorFactory {

  @VisibleForTesting Object actual;
  @VisibleForTesting Object expected;
  @VisibleForTesting Object notFound;
  @VisibleForTesting Object notExpected;

  /**
   * Creates instances of <code>{@link ErrorWhenGroupDoesNotContainValuesExclusively}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the values expected to be contained in {@code actual}. It is an {@code Object} in order to support
   * arrays of primitives as well.
   * @param notFound the values in {@code expected} not found in {@code actual}. It is an {@code Object} in order to
   * support arrays of primitives as well.
   * @param notExpected the values in {@code actual} that were not in {@code expected}.
   * @return an instance of {@code ErrorWhenGroupDoesNotContainValuesExclusively}.
   */
  public static AssertionErrorFactory errorWhenDoesNotContainExclusively(Object actual, Object expected,
      Object notFound, Object notExpected) {
    return new ErrorWhenGroupDoesNotContainValuesExclusively(actual, expected, notFound, notExpected);
  }

  @VisibleForTesting
  ErrorWhenGroupDoesNotContainValuesExclusively(Object actual, Object expected, Object notFound, Object notExpected) {
    this.actual = actual;
    this.expected = expected;
    this.notFound = notFound;
    this.notExpected = notExpected;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies a group of elements
   * contains only a given set of values and nothing else failed. A group of elements can be a collection, an array or a
   * {@code String}.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(errorMessage(d));
  }

  private String errorMessage(Description d) {
    IsNullOrEmptyChecker checker = IsNullOrEmptyChecker.instance();
    if (checker.isNullOrEmpty(notFound)) return errorMessageWithNotExpectedOnly(d);
    if (checker.isNullOrEmpty(notExpected)) return errorMessageWithNotFoundOnly(d);
    return defaultErrorMessage(d);
  }

  private String defaultErrorMessage(Description d) {
    String msg = "%sexpected:<%s> to contain only:<%s>; could not find:<%s> and got unexpected:<%s>";
    return Formatter.instance().formatMessage(msg, d, actual, expected, notFound, notExpected);
  }

  private String errorMessageWithNotFoundOnly(Description d) {
    String msg = "%sexpected:<%s> to contain only:<%s> but could not find:<%s>";
    return Formatter.instance().formatMessage(msg, d, actual, expected, notFound);
  }

  private String errorMessageWithNotExpectedOnly(Description d) {
    String msg = "%sexpected:<%s> to contain only:<%s> but got unexpected:<%s>";
    return Formatter.instance().formatMessage(msg, d, actual, expected, notExpected);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ErrorWhenGroupDoesNotContainValuesExclusively other = (ErrorWhenGroupDoesNotContainValuesExclusively) obj;
    if (!areEqual(actual, other.actual)) return false;
    if (!areEqual(expected, other.expected)) return false;
    if (!areEqual(notFound, other.notFound)) return false;
    return areEqual(notExpected, other.notExpected);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(expected);
    result = HASH_CODE_PRIME * result + hashCodeFor(notFound);
    result = HASH_CODE_PRIME * result + hashCodeFor(notExpected);
    return result;
  }
}
