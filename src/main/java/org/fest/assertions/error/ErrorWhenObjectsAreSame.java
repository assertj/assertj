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
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that two objects do not refer to the
 * same object fails.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ErrorWhenObjectsAreSame implements AssertionErrorFactory {

  @VisibleForTesting final Object actual;

  /**
   * Creates instances of <code>{@link ErrorWhenObjectsAreSame}</code>.
   * @param actual the actual value in the failed assertion.
   * @return an instance of {@code WhenSameErrorFactory}.
   */
  public static AssertionErrorFactory errorWhenSame(Object actual) {
    return new ErrorWhenObjectsAreSame(actual);
  }

  @VisibleForTesting ErrorWhenObjectsAreSame(Object actual) {
    this.actual = actual;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that two objects do not refer to the
   * same object fails.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    return Formatter.instance().formatMessage("%sexpected not same:<%s>", d, actual);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ErrorWhenObjectsAreSame o = (ErrorWhenObjectsAreSame) obj;
    return areEqual(actual, o.actual);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    return result;
  }
}
