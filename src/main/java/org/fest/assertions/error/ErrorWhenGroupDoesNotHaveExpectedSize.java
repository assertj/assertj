/*
 * Created on Sep 26, 2010
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
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that the number of elements in a group
 * of elements is equal to the expected one fails. A group of elements can be a collection, an array or a
 * {@code String}.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ErrorWhenGroupDoesNotHaveExpectedSize implements AssertionErrorFactory {

  @VisibleForTesting final Object actual;
  @VisibleForTesting final int actualSize;
  @VisibleForTesting final int expectedSize;

  /**
   * Creates instances of <code>{@link ErrorWhenGroupDoesNotHaveExpectedSize}</code>.
   * @param actual the actual value in the failed assertion.
   * @param actualSize the size of {@code actual}.
   * @param expectedSize the expected size.
   * @return an instance of {@code ErrorWhenGroupDoesNotHaveExpectedSize}.
   */
  public static AssertionErrorFactory errorWhenSizeNotEqual(Object actual, int actualSize, int expectedSize) {
    return new ErrorWhenGroupDoesNotHaveExpectedSize(actual, actualSize, expectedSize);
  }

  @VisibleForTesting ErrorWhenGroupDoesNotHaveExpectedSize(Object actual, int actualSize, int expectedSize) {
    this.actual = actual;
    this.actualSize = actualSize;
    this.expectedSize = expectedSize;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that the number of elements in a
   * group of elements is equal to the expected one fails. A group of elements can be a collection, an array or a
   * {@code String}.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    String format = "%sexpecting size:<%s> but was:<%s> in:<%s>";
    return Formatter.instance().formatMessage(format, d, expectedSize, actualSize, actual);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ErrorWhenGroupDoesNotHaveExpectedSize other = (ErrorWhenGroupDoesNotHaveExpectedSize) obj;
    if (!areEqual(actual, other.actual)) return false;
    if (actualSize != other.actualSize) return false;
    return expectedSize == other.expectedSize;
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + actualSize;
    result = HASH_CODE_PRIME * result + expectedSize;
    return result;
  }
}
