/*
 * Created on Oct 12, 2010
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
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies a group of elements does
 * not contain a given set of values failed. A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Alex Ruiz
 */
public class ErrorWhenGroupContainsValues implements AssertionErrorFactory {

  @VisibleForTesting final Object actual;
  @VisibleForTesting final Object expected;
  @VisibleForTesting final Object found;

  /**
   * Creates instances of <code>{@link ErrorWhenGroupContainsValues}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the values expected not to be contained in {@code actual}.
   * @param found the values in {@code expected} found in {@code actual}.
   * @return an instance of {@code ErrorWhenGroupContainsValues}.
   */
  public static AssertionErrorFactory errorWhenContains(Object actual, Object expected, Object found) {
    return new ErrorWhenGroupContainsValues(actual, expected, found);
  }

  @VisibleForTesting ErrorWhenGroupContainsValues(Object actual, Object expected, Object found) {
    this.actual = actual;
    this.expected = expected;
    this.found = found;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies a group of elements does
   * not contain a given set of values failed. A group of elements can be a collection, an array or a {@code String}.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    String msg = "%sexpected:<%s> to not contain:<%s> but found:<%s>";
    return Formatter.instance().formatMessage(msg, d, actual, expected, found);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ErrorWhenGroupContainsValues other = (ErrorWhenGroupContainsValues) obj;
    if (!areEqual(actual, other.actual)) return false;
    if (!areEqual(expected, other.expected)) return false;
    return areEqual(found, other.found);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(expected);
    result = HASH_CODE_PRIME * result + hashCodeFor(found);
    return result;
  }
}
