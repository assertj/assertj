/*
 * Created on Sep 16, 2010
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
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that an object is {@code null} fails.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ErrorWhenNotNullFactory implements AssertionErrorFactory {

  @VisibleForTesting final Object value;

  /**
   * Creates a new <code>{@link ErrorWhenNotEqualFactory}</code>.
   * @param o the verified object.
   * @return the created {@code ErrorWhenNotEqualFactory}.
   */
  public static AssertionErrorFactory errorWhenNotNull(Object o) {
    return new ErrorWhenNotNullFactory(o);
  }

  @VisibleForTesting ErrorWhenNotNullFactory(Object value) {
    this.value = value;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that an object is {@code null}
   * fails.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    return Formatter.instance().formatMessage("%sexpecting <null> but got:<%s>", d, value);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ErrorWhenNotNullFactory other = (ErrorWhenNotNullFactory) obj;
    return areEqual(value, other.value);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(value);
    return result;
  }
}
