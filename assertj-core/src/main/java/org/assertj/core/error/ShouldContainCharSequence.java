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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.util.Strings.escapePercent;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.util.Set;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code CharSequence} contains another
 * {@code CharSequence} failed.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldContainCharSequence extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(CharSequence actual, CharSequence sequence) {
    return new ShouldContainCharSequence("%nExpecting actual:%n  %s%nto contain:%n  %s %s", actual, sequence,
                                         StandardComparisonStrategy.instance());
  }

  public static ErrorMessageFactory shouldContain(Throwable actual, CharSequence sequence) {
    String format = "%n" +
                    "Expecting throwable message:%n" +
                    "  %s%n" +
                    "to contain:%n" +
                    "  %s%n" +
                    "but did not.%n" +
                    "%n" +
                    "Throwable that failed the check:%n" +
                    "%n" + escapePercent(getStackTrace(actual)); // to avoid AssertJ default String formatting

    return new ShouldContainCharSequence(format, actual.getMessage(), sequence, StandardComparisonStrategy.instance());
  }

  public static ErrorMessageFactory shouldContain(Throwable actual, CharSequence[] sequence,
                                                  Set<? extends CharSequence> notFound) {
    String format = "%n" +
                    "Expecting throwable message:%n" +
                    "  %s%n" +
                    "to contain:%n" +
                    "  %s%n" +
                    "but could not find:%n" +
                    "  %s%n" +
                    "%n" +
                    "Throwable that failed the check:%n" +
                    "%n" + escapePercent(getStackTrace(actual)); // to avoid AssertJ default String formatting
    return new ShouldContainCharSequence(format, actual.getMessage(), sequence, notFound, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(CharSequence actual, CharSequence sequence,
                                                  ComparisonStrategy comparisonStrategy) {
    return new ShouldContainCharSequence("%nExpecting actual:%n  %s%nto contain:%n  %s %s", actual, sequence, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @param notFound the values not found.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(CharSequence actual, CharSequence[] strings,
                                                  Set<? extends CharSequence> notFound,
                                                  ComparisonStrategy comparisonStrategy) {
    return new ShouldContainCharSequence("%nExpecting actual:%n  %s%nto contain:%n  %s%nbut could not find:%n  %s%n %s", actual,
                                         strings, notFound, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @param notFound the values not found.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContain(CharSequence actual, CharSequence[] strings,
                                                  Set<? extends CharSequence> notFound) {
    return shouldContain(actual, strings, notFound, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainIgnoringCase(CharSequence actual, CharSequence sequence) {
    return new ShouldContainCharSequence("%nExpecting actual:%n  %s%nto contain:%n  %s%n (ignoring case)", actual, sequence,
                                         StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   *
   * @param actual             the actual value in the failed assertion.
   * @param sequence           the sequence of values expected to be in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainIgnoringWhitespaces(CharSequence actual, CharSequence sequence,
                                                                     ComparisonStrategy comparisonStrategy) {
    return new ShouldContainCharSequence("%n" +
                                         "Expecting actual:%n" +
                                         "  %s%n" +
                                         "to contain (ignoring whitespaces):%n" +
                                         "  %s %s",
                                         actual, sequence, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   *
   * @param actual             the actual value in the failed assertion.
   * @param strings            the sequence of values expected to be in {@code actual}.
   * @param notFound           the values not found.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainIgnoringWhitespaces(CharSequence actual, CharSequence[] strings,
                                                                     Set<? extends CharSequence> notFound,
                                                                     ComparisonStrategy comparisonStrategy) {
    return new ShouldContainCharSequence("%n" +
                                         "Expecting actual:%n" +
                                         "  %s%n" +
                                         "to contain (ignoring whitespaces):%n" +
                                         "  %s%n" +
                                         "but could not find:%n" +
                                         "  %s%n" +
                                         " %s",
                                         actual, strings, notFound, comparisonStrategy);
  }

  /** 
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   * 
   * @param actual  the actual value in the failed assertion.
   * @param expectedValues the sequence of values expected to be in {@code actual}.
   * @param notFound the values not found.
   * @param comparisonWay the {@link ComparisonStrategy} to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory containsIgnoringNewLines(final CharSequence actual,
                                                             final CharSequence[] expectedValues,
                                                             final Set<? extends CharSequence> notFound,
                                                             final ComparisonStrategy comparisonWay) {
    final String start = "%n" +
                         "Expecting actual:%n" +
                         "  %s%n" +
                         "to contain (ignoring new lines):%n";
    if (notFound.size() > 1) {
      return new ShouldContainCharSequence(start +
                                           "  %s%n" +
                                           "but could not find:%n" +
                                           "  %s%n" +
                                           " %s",
                                           actual, expectedValues, notFound, comparisonWay);
    }
    // notFound.size() == 1 since it's not empty and not > 1
    return new ShouldContainCharSequence(start +
                                         "  %s %s",
                                         actual, notFound.iterator().next(), comparisonWay);

  }

  private ShouldContainCharSequence(String format, CharSequence actual, CharSequence sequence,
                                    ComparisonStrategy comparisonStrategy) {
    super(format, actual, sequence, comparisonStrategy);
  }

  private ShouldContainCharSequence(String format, CharSequence actual, CharSequence[] values,
                                    Set<? extends CharSequence> notFound,
                                    ComparisonStrategy comparisonStrategy) {
    super(format, actual, values, notFound, comparisonStrategy);
  }
}
