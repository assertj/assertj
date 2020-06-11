/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error.array;

import static java.util.Objects.deepEquals;
import static java.util.Objects.hash;

import org.assertj.core.description.Description;
import org.assertj.core.error.AssertionErrorFactory;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.VisibleForTesting;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two objects are equal
 * failed.
 * <p>
 * This class is used for multidimensional arrays and gives the opportunity to show the index of the not equal element.
 * <p>
 * The built {@link AssertionError}'s message differentiates {@link #actual} and {@link #expected} description if their
 * string representation are the same (e.g. 42 float and 42 double). It also mentions the comparator in case of a custom
 * comparator is used (instead of equals method).
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class MultidimensionalArrayShouldBeEqual extends org.assertj.core.error.ShouldBeEqual {

  private static final String EXPECTED_BUT_WAS_MESSAGE = "%n" +
                                                         "Expecting %s value to be equal to:%n" +
                                                         " <%s>%n" +
                                                         "but " +
                                                         "was%n <%s>%n.";
  private static final String EXPECTED_BUT_WAS_MESSAGE_USING_COMPARATOR = "%n" +
                                                                          "Expecting %s value to be equal to:%n" +
                                                                          " <%s>%n" +
                                                                          "%s%n" +
                                                                          "but was%n" +
                                                                          " <%s>%n.";
  private final String index;

  /**
   * Creates a new <code>{@link MultidimensionalArrayShouldBeEqual}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param representation the {@link Representation} used to format values.
   * @param index Index of value in multidimensional array, e.g. {@code [2][4]}
   * @return the created {@code AssertionErrorFactory}.
   */
  public static AssertionErrorFactory shouldBeEqual(Object actual, Object expected,
                                                    Representation representation,
                                                    String index) {
    return new MultidimensionalArrayShouldBeEqual(actual, expected, StandardComparisonStrategy.instance(), representation, index);
  }

  /**
   * Creates a new <code>{@link MultidimensionalArrayShouldBeEqual}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to compare actual with expected.
   * @param representation the {@link Representation} used to format values.
   * @param index Index of value in multidimensional array, e.g. {@code [2][4]}
   * @return the created {@code AssertionErrorFactory}.
   */
  public static AssertionErrorFactory shouldBeEqual(Object actual, Object expected,
                                                    ComparisonStrategy comparisonStrategy,
                                                    Representation representation,
                                                    String index) {
    return new MultidimensionalArrayShouldBeEqual(actual, expected, comparisonStrategy, representation, index);
  }

  @VisibleForTesting
  MultidimensionalArrayShouldBeEqual(Object actual, Object expected, ComparisonStrategy comparisonStrategy,
                                     Representation representation,
                                     String index) {
    super(actual, expected, comparisonStrategy, representation);
    this.index = index;
  }

  @Override
  protected String smartErrorMessage(Description description, Representation representation) {
    if (actualAndExpectedHaveSameStringRepresentation()) {
      // This happens for example when actual = 42f and expected = 42d, which will give this error:
      // actual : "42" and expected : "42".
      // JUnit 4 manages this case even worst, it will output something like :
      // "java.lang.String expected:java.lang.String<42.0> but was: java.lang.String<42.0>"
      // which makes things even more confusing since we lost the fact that 42 was a float or a double.
      // It is therefore better to built our own description without using ComparisonFailure, the
      // only drawback is that it won't look nice in IDEs.
      return defaultDetailedErrorMessage(description, representation);
    }
    return comparisonStrategy.isStandard()
        ? messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE, "actual" + index, actual, expected)
        : messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE_USING_COMPARATOR,
                                  "actual" + index, actual, expected, comparisonStrategy);
  }

  @Override
  protected String defaultDetailedErrorMessage(Description description, Representation representation) {
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy)
      return messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE_USING_COMPARATOR,
                                     index, detailedActual(), detailedExpected(), comparisonStrategy);
    return messageFormatter.format(description, representation, EXPECTED_BUT_WAS_MESSAGE, index, detailedActual(),
                                   detailedExpected());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (getClass() != o.getClass()) return false;
    MultidimensionalArrayShouldBeEqual other = (MultidimensionalArrayShouldBeEqual) o;
    if (!deepEquals(actual, other.actual)) return false;
    if (!deepEquals(expected, other.expected)) return false;
    return index == other.index;
  }

  @Override
  public int hashCode() {
    return hash(actual, expected, index);
  }

}
