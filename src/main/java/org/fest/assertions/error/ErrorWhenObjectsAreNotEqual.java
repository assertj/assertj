/*
 * Created on Aug 5, 2010
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

import static org.fest.assertions.util.ToString.toStringOf;
import static org.fest.util.Arrays.array;
import static org.fest.util.Objects.*;

import org.fest.assertions.description.Description;
import org.fest.util.VisibleForTesting;

/**
 * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that two objects are equal fails.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ErrorWhenObjectsAreNotEqual implements AssertionErrorFactory {

  @VisibleForTesting static final Class<?>[] MSG_ARG_TYPES = new Class<?>[] { String.class, String.class, String.class };

  private ConstructorInvoker constructorInvoker = new ConstructorInvoker();

  @VisibleForTesting final Object actual;
  @VisibleForTesting final Object expected;

  private Formatter formatter = Formatter.instance();

  /**
   * Creates instances of <code>{@link ErrorWhenObjectsAreNotEqual}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @return an instance of {@code ErrorWhenObjectsAreNotEqual}.
   */
  public static AssertionErrorFactory errorWhenNotEqual(Object actual, Object expected) {
    return new ErrorWhenObjectsAreNotEqual(actual, expected);
  }

  @VisibleForTesting ErrorWhenObjectsAreNotEqual(Object actual, Object expected) {
    this.actual = actual;
    this.expected = expected;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> when an assertion that verifies that two objects are equal fails. If
   * JUnit 4 is in the classpath, this method will instead create a {@code org.junit.ComparisonFailure} that highlights
   * the difference(s) between the expected and actual objects.
   * @param d the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description d) {
    AssertionError error = comparisonFailure(d);
    if (error != null) return error;
    return new AssertionError(defaultErrorMessage(d));
  }

  private String defaultErrorMessage(Description d) {
    return formatter.formatMessage("%sexpected:<%s> but was:<%s>", d, expected, actual);
  }

  private AssertionError comparisonFailure(Description d) {
    try {
      return newComparisonFailure(formatter.format(d).trim());
    } catch (Exception e) {
      return null;
    }
  }

  private AssertionError newComparisonFailure(String description) throws Exception {
    String className = "org.junit.ComparisonFailure";
    Object o = constructorInvoker.newInstance(className, MSG_ARG_TYPES, msgArgs(description));
    if (o instanceof AssertionError) return (AssertionError) o;
    return null;
  }

  private Object[] msgArgs(String description) {
    return array(description, toStringOf(expected), toStringOf(actual));
  }

  @VisibleForTesting void constructorInvoker(ConstructorInvoker newVal) {
    constructorInvoker = newVal;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (getClass() != o.getClass()) return false;
    ErrorWhenObjectsAreNotEqual other = (ErrorWhenObjectsAreNotEqual) o;
    if (!areEqual(actual, other.actual)) return false;
    return areEqual(expected, other.expected);
  }

  @Override public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(expected);
    return result;
  }

  @VisibleForTesting final void updateFormatter(Formatter newFormatter) {
    formatter = newFormatter;
  }
}
