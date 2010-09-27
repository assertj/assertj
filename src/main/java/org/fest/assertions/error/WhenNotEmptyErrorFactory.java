/*
 * Created on Sep 22, 2010
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
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies a group of elements is empty fails.
 * A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Alex Ruiz
 */
public class WhenNotEmptyErrorFactory implements AssertionErrorFactory {

  @VisibleForTesting final Object actual;

  /**
   * Creates instances of <code>{@link WhenNotEmptyErrorFactory}</code>.
   * @param actual the actual value in the failed assertion.
   * @return an instance of {@code WhenNotEmptyErrorFactory}.
   */
  public static AssertionErrorFactory errorWhenNotEmpty(Object actual) {
    return new WhenNotEmptyErrorFactory(actual);
  }

  @VisibleForTesting WhenNotEmptyErrorFactory(Object actual) {
    this.actual = actual;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies a group of elements is empty fails.
   * A group of elements can be a collection, an array or a {@code String}.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    return Formatter.instance().formatMessage("%sexpecting empty but was:<%s>", d, actual);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    WhenNotEmptyErrorFactory other = (WhenNotEmptyErrorFactory) obj;
    return areEqual(actual, other.actual);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    return result;
  }
}
