/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.System.lineSeparator;
import static java.util.Objects.deepEquals;
import static org.assertj.core.util.Objects.HASH_CODE_PRIME;
import static org.assertj.core.util.Objects.hashCodeFor;
import static org.assertj.core.util.introspection.ClassUtils.haveSameClassNameInDifferentPackages;

import java.util.Objects;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are equal
 * failed.
 * <p>
 * The built {@link AssertionError}'s message differentiates {@link #actual} and {@link #expected} description if their
 * string representation is the same (e.g. 42 float and 42 double). It also mentions the comparator in case of a custom
 * comparator is used (instead of equals method).
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldBeEqual {

  private static final String EXPECTED_BUT_WAS_MESSAGE = "%nexpected: %s%n but was: %s";
  private static final String EXPECTED_BUT_WAS_MESSAGE_USING_COMPARATOR = EXPECTED_BUT_WAS_MESSAGE + "%n%s";
  /** The actual value. */
  protected final Object actual;
  /** The expected value. */
  protected final Object expected;
  /** Whether the values have matching class names from different packages. */
  protected final boolean haveSameClassNameInDifferentPackages;
  /** Formatter used to build the assertionError message. */
  protected final MessageFormatter messageFormatter = MessageFormatter.instance();
  /** Strategy used to compare the values. */
  protected final ComparisonStrategy comparisonStrategy;
  private final Representation representation;

  /**
   * Creates a new <code>{@link ShouldBeEqual}</code>.
   *
   * @param actual         the actual value in the failed assertion.
   * @param expected       the expected value in the failed assertion.
   * @param representation the {@link Representation} used to format values.
   * @return the created {@code AssertionErrorFactory}.
   */
  public static ShouldBeEqual shouldBeEqual(Object actual, Object expected, Representation representation) {
    return new ShouldBeEqual(actual, expected, StandardComparisonStrategy.instance(), representation);
  }

  /**
   * Creates a new <code>{@link ShouldBeEqual}</code>.
   *
   * @param actual             the actual value in the failed assertion.
   * @param expected           the expected value in the failed assertion.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to compare actual with expected.
   * @param representation     the {@link Representation} used to format values.
   * @return the created {@code AssertionErrorFactory}.
   */
  public static ShouldBeEqual shouldBeEqual(Object actual, Object expected,
                                            ComparisonStrategy comparisonStrategy,
                                            Representation representation) {
    return new ShouldBeEqual(actual, expected, comparisonStrategy, representation);
  }

  /**
   * Creates an equality error factory.
   *
   * @param actual the actual value
   * @param expected the expected value
   * @param comparisonStrategy the comparison strategy
   * @param representation the value representation
   */
  protected ShouldBeEqual(Object actual, Object expected, ComparisonStrategy comparisonStrategy, Representation representation) {
    this.actual = actual;
    this.expected = expected;
    this.comparisonStrategy = comparisonStrategy;
    this.representation = representation;
    this.haveSameClassNameInDifferentPackages = haveSameClassNameInDifferentPackages(actual, expected);
  }

  /**
   * Checks whether actual and expected have the same string representation.
   *
   * @return whether both representations are equal
   */
  protected boolean actualAndExpectedHaveSameStringRepresentation() {
    return Objects.equals(representation.toStringOf(actual), representation.toStringOf(expected));
  }

  /**
   * Builds and returns an error message from the given description using {@link #expected} and {@link #actual} basic
   * representation if their description differs otherwise use
   * {@link #defaultDetailedErrorMessage(Description, Representation)} to represent them differently.
   *
   * @param description    the {@link Description} used to build the returned error message
   * @param representation the {@link org.assertj.core.presentation.Representation} used to build String representation
   *                       of actual and expected.
   * @return the error message from description using {@link #expected} and {@link #actual} "smart" representation.
   */
  protected String smartErrorMessage(Description description, Representation representation) {
    if (actualAndExpectedHaveSameStringRepresentation()) {
      // This happens, for example, when actual = 42f and expected = 42d, which will give this error:
      // actual: "42" and expected: "42".
      // JUnit 4 manages this case even worse, it will output something like:
      // "java.lang.String expected:java.lang.String<42.0> but was: java.lang.String<42.0>",
      // this is even more confusing as we lost the fact that 42 was a float or a double.
      return defaultDetailedErrorMessage(description, representation);
    }
    String actualRepresentation = representation.toStringOf(actual);
    String expectedRepresentation = representation.toStringOf(expected);
    if (hasMultilineValue(actualRepresentation, expectedRepresentation)) {
      return errorMessageForMultilineValues(description, actualRepresentation, expectedRepresentation);
    }

    return comparisonStrategy.isStandard()
        ? messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE, expected, actual)
        : messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE_USING_COMPARATOR,
                                  expected, actual, comparisonStrategy);
  }

  private String errorMessageForMultilineValues(Description description,
                                                String actualRepresentation,
                                                String expectedRepresentation) {
    String desc = DescriptionFormatter.instance().format(description);
    return desc + messageForMultilineValues(actualRepresentation, expectedRepresentation);
  }

  private boolean hasMultilineValue(String actualRepresentation, String expectedRepresentation) {
    return (actualRepresentation != null && actualRepresentation.contains(lineSeparator())) ||
           (expectedRepresentation != null && expectedRepresentation.contains(lineSeparator()));
  }

  private String messageForMultilineValues(String actualRepresentation, String expectedRepresentation) {
    return comparisonStrategy.isStandard()
        ? EXPECTED_BUT_WAS_MESSAGE.formatted(indent(expectedRepresentation), indent(actualRepresentation))
        : EXPECTED_BUT_WAS_MESSAGE_USING_COMPARATOR.formatted(indent(expectedRepresentation), indent(actualRepresentation),
                                                              comparisonStrategy.asText());
  }

  /**
   * Indents a multiline value representation.
   *
   * @param valueRepresentation the representation to indent
   * @return the indented representation
   */
  protected String indent(String valueRepresentation) {
    return "%n%s".formatted(valueRepresentation).replace(lineSeparator(), lineSeparator() + "  ");
  }

  /**
   * Builds and returns an error message from description using {@link #detailedExpected()} and
   * {@link #detailedActual()} detailed representation.
   *
   * @param description    the {@link Description} used to build the returned error message
   * @param representation the {@link org.assertj.core.presentation.Representation} used to build String representation
   *                       of actual and expected
   * @return the error message from description using {@link #detailedExpected()} and {@link #detailedActual()}
   * <b>detailed</b> representation.
   */
  protected String defaultDetailedErrorMessage(Description description, Representation representation) {
    String actualRepresentation = detailedActual();
    String expectedRepresentation = detailedExpected();
    if (hasMultilineValue(actualRepresentation, expectedRepresentation)) {
      return errorMessageForMultilineValues(description, actualRepresentation, expectedRepresentation);
    }

    return comparisonStrategy instanceof ComparatorBasedComparisonStrategy
        ? messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE_USING_COMPARATOR, expectedRepresentation,
                                  actualRepresentation, comparisonStrategy)
        : messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE, expectedRepresentation,
                                  actualRepresentation);
  }

  /**
   * Returns an unambiguous actual value representation.
   *
   * @return the detailed actual representation
   */
  protected String detailedActual() {
    return representation.unambiguousToStringOf(actual, haveSameClassNameInDifferentPackages);
  }

  /**
   * Returns an unambiguous expected value representation.
   *
   * @return the detailed expected representation
   */
  protected String detailedExpected() {
    return representation.unambiguousToStringOf(expected, haveSameClassNameInDifferentPackages);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (getClass() != o.getClass()) return false;
    ShouldBeEqual other = (ShouldBeEqual) o;
    if (!deepEquals(actual, other.actual)) return false;
    return deepEquals(expected, other.expected);
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(actual);
    result = HASH_CODE_PRIME * result + hashCodeFor(expected);
    return result;
  }
}
