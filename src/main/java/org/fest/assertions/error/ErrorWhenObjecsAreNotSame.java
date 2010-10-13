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
package org.fest.assertions.error;
import static org.fest.util.Objects.*;

import org.fest.assertions.description.Description;
import org.fest.util.VisibleForTesting;

/**
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that two object refer to same object
 * fails.
 *
 * @author Alex Ruiz
 */
public class ErrorWhenObjecsAreNotSame implements AssertionErrorFactory {

  @VisibleForTesting final Object actual;
  @VisibleForTesting final Object expected;

  /**
   * Creates instances of <code>{@link ErrorWhenObjecsAreNotSame}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @return an instance of {@code ErrorWhenObjecsAreNotSame}.
   */
  public static AssertionErrorFactory errorWhenNotSame(Object actual, Object expected) {
    return new ErrorWhenObjecsAreNotSame(actual, expected);
  }

  @VisibleForTesting ErrorWhenObjecsAreNotSame(Object actual, Object expected) {
    this.actual = actual;
    this.expected = expected;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that two object refer to same object
   * fails.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    String format = "%sexpected:<%s> and actual:<%s> should refer to the same object";
    return Formatter.instance().formatMessage(format, d, expected, actual);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ErrorWhenObjecsAreNotSame other = (ErrorWhenObjecsAreNotSame) obj;
    if (!areEqual(actual, other.actual)) return false;
    return areEqual(expected, other.expected);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(expected);
    return result;
  }
}
