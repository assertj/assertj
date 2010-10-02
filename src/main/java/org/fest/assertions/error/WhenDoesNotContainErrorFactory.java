/*
 * Created on Sep 30, 2010
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
import org.fest.util.VisibleForTesting;

/**
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies a group of elements contains a given
 * set of values. A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Alex Ruiz
 */
public class WhenDoesNotContainErrorFactory implements AssertionErrorFactory {

  @VisibleForTesting Object actual;
  @VisibleForTesting Object expected;
  @VisibleForTesting Object notFound;

  /**
   * Creates instances of <code>{@link WhenDoesNotContainErrorFactory}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the values expected to be contained in {@code actual}. It is an {@code Object} in order to support
   * arrays of primitives as well.
   * @param notFound the values in {@code expected} not found in {@code actual}. It is an {@code Object} in order to
   * support arrays of primitives as well.
   * @return an instance of {@code WhenDoesNotContainErrorFactory}.
   */
  public static AssertionErrorFactory errorWhenDoesNotContain(Object actual, Object expected, Object notFound) {
    return new WhenDoesNotContainErrorFactory(actual, expected, notFound);
  }

  @VisibleForTesting WhenDoesNotContainErrorFactory(Object actual, Object expected, Object notFound) {
    this.actual = actual;
    this.expected = expected;
    this.notFound = notFound;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies a group of elements contains a given
   * set of values. A group of elements can be a collection, an array or a {@code String}.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    String msg = "%sexpected:<%s> to contain:<%s> but could not find:<%s>";
    return Formatter.instance().formatMessage(msg, d, actual, expected, notFound);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    WhenDoesNotContainErrorFactory other = (WhenDoesNotContainErrorFactory) obj;
    if (!areEqual(actual, other.actual)) return false;
    if (!areEqual(expected, other.expected)) return false;
    return areEqual(notFound, other.notFound);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(expected);
    result = HASH_CODE_PRIME * result + hashCodeFor(notFound);
    return result;
  }
}
