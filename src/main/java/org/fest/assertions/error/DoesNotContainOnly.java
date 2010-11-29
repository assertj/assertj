/*
 * Created on Oct 18, 2010
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

import java.util.Collection;
import java.util.Set;

import org.fest.assertions.description.Description;
import org.fest.assertions.group.IsNullOrEmptyChecker;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains only a given set of
 * values and nothing else failed. A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Alex Ruiz
 */
public class DoesNotContainOnly implements ErrorMessage {

  private final Object actual;
  private final Object expected;
  private final Object notExpected;
  private final Object notFound;

  /**
   * Creates a new </code>{@link DoesNotContainOnly}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notExpected the values in {@code actual} that were not in {@code expected}.
   * @param notFound the values in {@code expected} not found in {@code actual}.
   * @return the created {@code ErrorMessage}.
   */
  public static ErrorMessage doesNotContainOnly(Collection<?> actual, Collection<?> expected, Set<?> notExpected,
      Set<?> notFound) {
    return new DoesNotContainOnly(actual, expected, notExpected, notFound);
  }

  private DoesNotContainOnly(Object actual, Object expected, Object notExpected, Object notFound) {
    this.actual = actual;
    this.expected = expected;
    this.notExpected = notExpected;
    this.notFound = notFound;
  }

  /** {@inheritDoc} */
  public String create(Description d) {
    if (isNullOrEmpty(notExpected)) return includeOnlyNotFound(d);
    if (isNullOrEmpty(notFound)) return includeOnlyNotExpected(d);
    return defaultMessage(d);
  }

  private static boolean isNullOrEmpty(Object o) {
    return IsNullOrEmptyChecker.instance().isNullOrEmpty(o);
  }

  private String includeOnlyNotFound(Description d) {
    String format = "%sexpected:<%s> to contain only:<%s>, but could not find:<%s>";
    return formatMessage(format, d, actual, expected, notFound);
  }

  private String includeOnlyNotExpected(Description d) {
    String format = "%sexpected:<%s> to contain only:<%s>, but got unexpected:<%s>";
    return formatMessage(format, d, actual, expected, notExpected);
  }

  private String defaultMessage(Description d) {
    String format = "%sexpected:<%s> to contain only:<%s>; could not find:<%s> and got unexpected:<%s>";
    return formatMessage(format, d, actual, expected, notFound, notExpected);
  }

  private static String formatMessage(String format, Description d, Object... args) {
    return Formatter.instance().formatMessage(format, d, args);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    DoesNotContainOnly other = (DoesNotContainOnly) obj;
    if (!areEqual(actual, other.actual)) return false;
    if (!areEqual(expected, other.expected)) return false;
    if (!areEqual(notExpected, other.notExpected)) return false;
    return areEqual(notFound, other.notFound);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(expected);
    result = HASH_CODE_PRIME * result + hashCodeFor(notExpected);
    result = HASH_CODE_PRIME * result + hashCodeFor(notFound);
    return result;
  }
}
