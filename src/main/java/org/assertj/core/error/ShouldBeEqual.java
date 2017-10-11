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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Objects.HASH_CODE_PRIME;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Objects.hashCodeFor;

import org.assertj.core.description.Description;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.VisibleForTesting;

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
  private static final Class<?>[] MSG_ARG_TYPES = array(String.class, String.class, String.class);
  private static final Class<?>[] MSG_ARG_TYPES_FOR_ASSERTION_FAILED_ERROR = array(String.class, Object.class,
                                                                                   Object.class);
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
   * @param representation the {@link Representation} used to format values.
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
   * @param representation the {@link Representation} used to format values.
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
   * <p>
   *   If opentest4j is on the classpath then {@code org.opentest4j.AssertionFailedError} would be used.
   * </p>
   * {@link AssertionError} stack trace won't show AssertJ related elements if {@link Failures} is configured to filter
   * them (see {@link Failures#setRemoveAssertJRelatedElementsFromStackTrace(boolean)}).
   *
   * @param description the description of the failed assertion.
   * @param representation the {@link Representation} used to format values.
   * @return the created {@code AssertionError}.
   */
  @Override
  public AssertionError newAssertionError(Description description, Representation representation) {
    // only use JUnit error message if the comparison strategy used was standard, otherwise we need to mention
    // comparison strategy in the assertion error message to make it clear to the user it was used.
    if (comparisonStrategy.isStandard() && !actualAndExpectedHaveSameStringRepresentation()) {
      // comparison strategy is standard -> try to build a JUnit ComparisonFailure that is nicely displayed in IDEs.
      AssertionError error = comparisonFailure(description);
      // error != null means that JUnit 4 was in the classpath and we were to build a ComparisonFailure.
      if (error != null) return error;
    }
    String message = smartErrorMessage(description, representation);
    AssertionError assertionFailedError = assertionFailedError(message);
    if (assertionFailedError != null) return assertionFailedError;
    // No JUnit in the classpath => fall back to default error message
    return Failures.instance().failure(message);
  }

  private boolean actualAndExpectedHaveSameStringRepresentation() {
    return areEqual(representation.toStringOf(actual), representation.toStringOf(expected));
  }

  /**
   * Builds and returns an error message from the given description using {@link #expected} and {@link #actual} basic
   * representation if their description differ otherwise use 
   * {@link #defaultDetailedErrorMessage(Description, Representation)} to represent them differently.
   *
   * @param description the {@link Description} used to build the returned error message
   * @param representation the {@link org.assertj.core.presentation.Representation} used to build String representation
   *          of object
   * @return the error message from description using {@link #expected} and {@link #actual} "smart" representation.
   */
  private String smartErrorMessage(Description description, Representation representation) {
    if (actualAndExpectedHaveSameStringRepresentation()) {
      // This happens for example when actual = 42f and expected = 42d, which will give this error:
      // actual : "42" and expected : "42".
      // JUnit 4 manages this case even worst, it will output something like :
      // "java.lang.String expected:java.lang.String<42.0> but was: java.lang.String<42.0>"
      // which makes things even more confusing since we lost the fact that 42 was a float or a double.
      // It is therefore better to built our own description without using ComparisonFailure, the
      // only drawbacj is that it won't look nice in IDEs.
      return defaultDetailedErrorMessage(description, representation);
    }
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

  private AssertionError assertionFailedError(String message) {
    try {
      Object o = constructorInvoker.newInstance("org.opentest4j.AssertionFailedError",
                                                MSG_ARG_TYPES_FOR_ASSERTION_FAILED_ERROR,
                                                message,
                                                expected,
                                                actual);
      if (o instanceof AssertionError) return (AssertionError) o;
      return null;
    } catch (Throwable e) {
      return null;
    }
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

  private String detailedActual() {
    return representation.unambiguousToStringOf(actual);
  }

  private String detailedExpected() {
    return representation.unambiguousToStringOf(expected);
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
