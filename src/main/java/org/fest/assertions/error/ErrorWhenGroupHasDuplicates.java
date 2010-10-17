/*
 * Created on Oct 17, 2010
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
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies a group of elements is does
 * not have duplicates failed. A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Alex Ruiz
 */
public class ErrorWhenGroupHasDuplicates implements AssertionErrorFactory {

  @VisibleForTesting final Object actual;
  @VisibleForTesting final Object duplicates;

  /**
   * Creates instances of <code>{@link ErrorWhenGroupHasDuplicates}</code>.
   * @param actual the actual value in the failed assertion.
   * @param duplicates the duplicate values found in {@code actual}.
   * @return an instance of {@code ErrorWhenGroupHasDuplicates}.
   */
  public static AssertionErrorFactory errorWhenHavingDuplicates(Object actual, Object duplicates) {
    return new ErrorWhenGroupHasDuplicates(actual, duplicates);
  }

  @VisibleForTesting ErrorWhenGroupHasDuplicates(Object actual, Object duplicates) {
    this.actual = actual;
    this.duplicates = duplicates;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies a group of elements is
   * does not have duplicates failed. A group of elements can be a collection, an array or a {@code String}.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    String format = "%sfound duplicate(s):<%s> in:<%s>";
    return Formatter.instance().formatMessage(format, d, duplicates, actual);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ErrorWhenGroupHasDuplicates other = (ErrorWhenGroupHasDuplicates) obj;
    if (!areEqual(actual, other.actual)) return false;
    return areEqual(duplicates, other.duplicates);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(duplicates);
    return result;
  }
}
