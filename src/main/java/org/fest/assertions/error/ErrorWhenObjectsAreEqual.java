/*
 * Created on Sep 14, 2010
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
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are not
 * equal failed.
 *
 * @author Alex Ruiz
 */
public class ErrorWhenObjectsAreEqual implements AssertionErrorFactory {

  @VisibleForTesting final Object actual;
  @VisibleForTesting final Object other;

  /**
   * Creates instances of <code>{@link ErrorWhenObjectsAreEqual}</code>.
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @return an instance of {@code ErrorWhenObjectsAreEqual}.
   */
  public static AssertionErrorFactory errorWhenEqual(Object actual, Object other) {
    return new ErrorWhenObjectsAreEqual(actual, other);
  }

  @VisibleForTesting ErrorWhenObjectsAreEqual(Object actual, Object other) {
    this.actual = actual;
    this.other = other;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are not
   * equal failed.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    return Formatter.instance().formatMessage("%s<%s> should not be equal to:<%s>", d, actual, other);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ErrorWhenObjectsAreEqual o = (ErrorWhenObjectsAreEqual) obj;
    if (!areEqual(actual, o.actual)) return false;
    return areEqual(other, o.other);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(other);
    return result;
  }
}
