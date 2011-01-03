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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import static java.lang.String.format;
import static org.fest.util.Collections.isEmpty;
import static org.fest.util.Objects.*;
import static org.fest.util.ToString.toStringOf;

import java.util.Collection;
import java.util.Set;

import org.fest.assertions.description.Description;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains only a given set of
 * values and nothing else failed. A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Alex Ruiz
 */
public class DoesNotContainOnly implements ErrorMessage {

  private final Collection<?> actual;
  private final Collection<?> expected;
  private final Set<?> notExpected;
  private final Set<?> notFound;

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

  private DoesNotContainOnly(Collection<?> actual, Collection<?> expected, Set<?> notExpected, Set<?> notFound) {
    this.actual = actual;
    this.expected = expected;
    this.notExpected = notExpected;
    this.notFound = notFound;
  }

  /** {@inheritDoc} */
  public String create(Description d) {
    if (isEmpty(notExpected)) return includeOnlyNotFound(d);
    if (isEmpty(notFound)) return includeOnlyNotExpected(d);
    return defaultMessage(d);
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

  /** {@inheritDoc} */
  @Override public String toString() {
    String format = "%s[actual=%s, expected=%s, notExpected=%s, notFound=%s]";
    return format(format, getClass().getSimpleName(), toStringOf(actual), toStringOf(expected),
        toStringOf(notExpected), toStringOf(notFound));
  }
}
