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

import org.fest.assertions.core.Condition;
import org.fest.assertions.description.Description;
import org.fest.util.VisibleForTesting;

/**
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that a value does not satisfy a
 * <code>{@link Condition}</code> fails.
 *
 * @author Alex Ruiz
 */
public class ErrorWhenConditionIsMet implements AssertionErrorFactory {

  @VisibleForTesting final Object actual;
  @VisibleForTesting final Condition<?> condition;

  /**
   * Creates instances of <code>{@link ErrorWhenConditionIsMet}</code>.
   * @param actual the actual value in the failed assertion.
   * @param condition the {@code Condition}.
   * @return an instance of {@code ErrorWhenConditionIsMet}.
   */
  public static AssertionErrorFactory errorWhenConditionMet(Object actual, Condition<?> condition) {
    return new ErrorWhenConditionIsMet(actual, condition);
  }

  @VisibleForTesting ErrorWhenConditionIsMet(Object actual, Condition<?> condition) {
    this.actual = actual;
    this.condition = condition;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that a value does not satisfy a
   * <code>{@link Condition}</code> fails.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    String format = "%sactual value:<%s> should not satisfy condition:<%s>";
    return Formatter.instance().formatMessage(format, d, actual, condition);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ErrorWhenConditionIsMet other = (ErrorWhenConditionIsMet) obj;
    if (!areEqual(actual, other.actual)) return false;
    return areEqual(condition, other.condition);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(condition);
    return result;
  }
}
