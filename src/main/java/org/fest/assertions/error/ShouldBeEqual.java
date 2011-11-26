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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import static java.lang.Integer.toHexString;

import static org.fest.util.Arrays.array;
import static org.fest.util.Objects.*;
import static org.fest.util.ToString.toStringOf;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.Failures;
import org.fest.util.VisibleForTesting;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are equal
 * failed.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldBeEqual implements AssertionErrorFactory {

  private static final String EXPECTED_BUT_WAS_MESSAGE = "expected:<%s> but was:<%s>";

  private static final Class<?>[] MSG_ARG_TYPES = new Class<?>[] { String.class, String.class, String.class };

  @VisibleForTesting ConstructorInvoker constructorInvoker = new ConstructorInvoker();
  @VisibleForTesting MessageFormatter messageFormatter = MessageFormatter.instance();
  @VisibleForTesting DescriptionFormatter descriptionFormatter = DescriptionFormatter.instance();

  private final Object actual;
  private final Object expected;

  /**
   * Creates a new <code>{@link ShouldBeEqual}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @return the created {@code AssertionErrorFactory}.
   */
  public static AssertionErrorFactory shouldBeEqual(Object actual, Object expected) {
    return new ShouldBeEqual(actual, expected);
  }

  @VisibleForTesting ShouldBeEqual(Object actual, Object expected) {
    this.actual = actual;
    this.expected = expected;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are
   * equal failed.
   * <p>
   * If JUnit 4 is in the classpath, this method will instead create a {@code org.junit.ComparisonFailure}
   * that highlights the difference(s) between the expected and actual objects.
   * </p>
   * @param description the description of the failed assertion.
   * @return the created {@code AssertionError}.
   */
  public AssertionError newAssertionError(Description description) {
    if (actualAndExpectedHaveSameStringRepresentation()) {
      // Example : actual = 42f and expected = 42d gives actual : "42" and expected : "42" and 
      // JUnit 4 manage this case ... weirdly, it will something like java.lang.String expected: java.lang.String<42.0> but was: java.lang.String<42.0>
      // which does not solve the problem and makes things even more confusing since we lost the fact that 42 was a float or a double
      // This is why it is better to built our own description, with the drawback of not using a ComparisonFailure (which looks nice in eclipse)
      return Failures.instance().failure(defaultDetailedErrorMessage(description));
    }
    AssertionError error = comparisonFailure(description);
    if (error != null) {
      Failures.instance().removeFestRelatedElementsFromStackTraceIfNeeded(error);
      return error;
    }
    return Failures.instance().failure(defaultErrorMessage(description));
  }

  private boolean actualAndExpectedHaveSameStringRepresentation() {
    return areEqual(toStringOf(actual),toStringOf(expected));
  }

  private String defaultErrorMessage(Description description) {
    return messageFormatter.format(description, EXPECTED_BUT_WAS_MESSAGE, expected, actual);
  }

  private String defaultDetailedErrorMessage(Description description) {
    return messageFormatter.format(description, EXPECTED_BUT_WAS_MESSAGE, expectedDetailedToString(), actualDetailedToString());
  }
  
  private AssertionError comparisonFailure(Description description) {
    try {
      return newComparisonFailure(descriptionFormatter.format(description).trim());
    } catch (Throwable e) {
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

  private static String detailedToStringOf(Object obj) { 
    return toStringOf(obj) + " (" + obj.getClass().getSimpleName() + "@" + toHexString(obj.hashCode()) + ")";
  }
  
  private String actualDetailedToString() {
    return detailedToStringOf(actual);
  }
  
  private String expectedDetailedToString() {
    return detailedToStringOf(expected);
  }
  
  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (getClass() != o.getClass()) return false;
    ShouldBeEqual other = (ShouldBeEqual) o;
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
