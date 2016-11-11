/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.description.Description;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.VisibleForTesting;

import static java.lang.Integer.toHexString;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Objects.*;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are equal
 * failed.
 * <p>
 * The built {@link AssertionError}'s message differentiates {@link #actual} and {@link #expected} description if their
 * string representation are the same (e.g. 42 float and 42 double). It also mentions the comparator in case of a custom
 * comparator is used (instead of equals method).
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldBeEqual implements AssertionErrorFactory {

  private static final String EXPECTED_BUT_WAS_MESSAGE = "%nExpecting:%n <%s>%nto be equal to:%n <%s>%nbut was not.";
  private static final String EXPECTED_BUT_WAS_MESSAGE_USING_COMPARATOR = "%nExpecting:%n <%s>%nto be equal to:%n " +
                                                                          "<%s>%n%s but was not.";
  private static final Class<?>[] MSG_ARG_TYPES = new Class<?>[] { String.class, String.class, String.class };
  protected final Object actual;
  protected final Object expected;
  @VisibleForTesting
  final MessageFormatter messageFormatter = MessageFormatter.instance();
  private final ComparisonStrategy comparisonStrategy;
  private Representation representation;
  @VisibleForTesting
  ConstructorInvoker constructorInvoker = new ConstructorInvoker();
  @VisibleForTesting
  DescriptionFormatter descriptionFormatter = DescriptionFormatter.instance();

  /**
   * Creates a new <code>{@link ShouldBeEqual}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @return the created {@code AssertionErrorFactory}.
   */
  public static AssertionErrorFactory shouldBeEqual(Object actual, Object expected, Representation representation) {
    return new ShouldBeEqual(actual, expected, StandardComparisonStrategy.instance(), representation);
  }

  /**
   * Creates a new <code>{@link ShouldBeEqual}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to compare actual with expected.
   * @return the created {@code AssertionErrorFactory}.
   */
  public static AssertionErrorFactory shouldBeEqual(Object actual, Object expected,
                                                    ComparisonStrategy comparisonStrategy,
                                                    Representation representation) {
    return new ShouldBeEqual(actual, expected, comparisonStrategy, representation);
  }

  @VisibleForTesting
  ShouldBeEqual(Object actual, Object expected, ComparisonStrategy comparisonStrategy, Representation representation) {
    this.actual = actual;
    this.expected = expected;
    this.comparisonStrategy = comparisonStrategy;
    this.representation = representation;
  }

  /**
   * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are
   * equal failed.<br>
   * The <code>{@link AssertionError}</code> message is built so that it differentiates {@link #actual} and
   * {@link #expected} description in case their string representation are the same (like 42 float and 42 double).
   * <p>
   * If JUnit 4 is in the classpath and the description is standard (no comparator was used and {@link #actual} and
   * {@link #expected} string representation were different), this method will instead create a
   * org.junit.ComparisonFailure that highlights the difference(s) between the expected and actual objects.
   * </p>
   * {@link AssertionError} stack trace won't show AssertJ related elements if {@link Failures} is configured to filter
   * them (see {@link Failures#setRemoveAssertJRelatedElementsFromStackTrace(boolean)}).
   *
   * @param description the description of the failed assertion.
   * @param representation
   * @return the created {@code AssertionError}.
   */
  @Override
  public AssertionError newAssertionError(Description description, Representation representation) {
    if (actualAndExpectedHaveSameStringRepresentation()) {
      // Example : actual = 42f and expected = 42d gives actual : "42" and expected : "42" and
      // JUnit 4 manages this case even worst, it will output something like :
      // "java.lang.String expected:java.lang.String<42.0> but was: java.lang.String<42.0>"
      // which does not solve the problem and makes things even more confusing since we lost the fact that 42 was a
      // float or a double, it is then better to built our own description, with the drawback of not using a
      // ComparisonFailure (which looks nice in eclipse)
      return Failures.instance().failure(defaultDetailedErrorMessage(description, representation));
    }
    // only use JUnit error message if comparison strategy was standard, otherwise we need to mention it in the
    // assertion error message to make it clear to the user it was used.
    if (comparisonStrategy.isStandard()) {
      // comparison strategy is standard -> try to build a JUnit ComparisonFailure that is nicely dispayed in IDE.
      AssertionError error = comparisonFailure(description);
      // error ==null means that JUnit was not in the classpath
      if (error != null) return error;
    }
    // No JUnit in the classpath => fall back to default error message
    return Failures.instance().failure(defaultErrorMessage(description, representation));
  }

  private boolean actualAndExpectedHaveSameStringRepresentation() {
    return areEqual(representation.toStringOf(actual), representation.toStringOf(expected));
  }

  /**
   * Builds and returns an error message from description using {@link #expected} and {@link #actual} basic
   * representation.
   *
   * @param description the {@link Description} used to build the returned error message
   * @param representation the {@link org.assertj.core.presentation.Representation} used to build String representation
   *          of object
   * @return the error message from description using {@link #expected} and {@link #actual} basic representation.
   */
  private String defaultErrorMessage(Description description, Representation representation) {
    return comparisonStrategy.isStandard()
        ? messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE, actual, expected)
        : messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE_USING_COMPARATOR,
                                  actual, expected, comparisonStrategy);
  }

  /**
   * Builds and returns an error message from description using {@link #detailedExpected()} and
   * {@link #detailedActual()} detailed representation.
   *
   * @param description the {@link Description} used to build the returned error message
   * @param representation the {@link org.assertj.core.presentation.Representation} used to build String representation
   *          of object
   * @return the error message from description using {@link #detailedExpected()} and {@link #detailedActual()}
   *         <b>detailed</b> representation.
   */
  private String defaultDetailedErrorMessage(Description description, Representation representation) {
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy)
      return messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE_USING_COMPARATOR,
                                     detailedActual(),
                                     detailedExpected(), comparisonStrategy);
    return messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE, detailedActual(),
                                   detailedExpected());
  }

  private AssertionError comparisonFailure(Description description) {
    try {
      AssertionError comparisonFailure = newComparisonFailure(descriptionFormatter.format(description).trim());
      Failures.instance().removeAssertJRelatedElementsFromStackTraceIfNeeded(comparisonFailure);
      return comparisonFailure;
    } catch (Throwable e) {
      return null;
    }
  }

  private AssertionError newComparisonFailure(String description) throws Exception {
    Object o = constructorInvoker.newInstance("org.junit.ComparisonFailure", MSG_ARG_TYPES, msgArgs(description));
    if (o instanceof AssertionError) return (AssertionError) o;
    return null;
  }

  private Object[] msgArgs(String description) {
    return array(description, representation.toStringOf(expected), representation.toStringOf(actual));
  }

  private String detailedToStringOf(Object obj) {
    return representation.toStringOf(obj) + " (" + obj.getClass().getSimpleName() + "@" + toHexString(obj.hashCode())
           + ")";
  }

  private String detailedActual() {
    return detailedToStringOf(actual);
  }

  private String detailedExpected() {
    return detailedToStringOf(expected);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (getClass() != o.getClass()) return false;
    ShouldBeEqual other = (ShouldBeEqual) o;
    if (!areEqual(actual, other.actual)) return false;
    return areEqual(expected, other.expected);
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(expected);
    return result;
  }
}
