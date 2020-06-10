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
package org.assertj.core.error;

import java.util.Set;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code CharSequence} does not contain another
 * {@code CharSequence} failed.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldNotContainCharSequence extends BasicErrorMessageFactory {

  private ShouldNotContainCharSequence(String format, CharSequence actual, CharSequence sequence,
                                       ComparisonStrategy comparisonStrategy) {
    super(format, actual, sequence, comparisonStrategy);
  }

  private ShouldNotContainCharSequence(String format, CharSequence actual, CharSequence[] values,
                                       Set<? extends CharSequence> found,
                                       ComparisonStrategy comparisonStrategy) {
    super(format, actual, values, found, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldNotContainCharSequence}</code>.
   * @param actual the actual value in the failed assertion.
   * @param sequence the charsequence expected not to be in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContain(CharSequence actual, CharSequence sequence,
                                                     ComparisonStrategy comparisonStrategy) {
    return new ShouldNotContainCharSequence("%n" +
                                            "Expecting:%n" +
                                            " <%s>%n" +
                                            "not to contain:%n" +
                                            " <%s>%n" +
                                            "%s",
                                            actual, sequence, comparisonStrategy);
  }

  public static ErrorMessageFactory shouldNotContain(CharSequence actual, CharSequence sequence) {
    return shouldNotContain(actual, sequence, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldNotContainCharSequence}</code>
   * @param actual the actual value in the failed assertion.
   * @param values the charsequences expected not to be in {@code actual}.
   * @param found the charsequences unexpectedly in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContain(CharSequence actual, CharSequence[] values,
                                                     Set<? extends CharSequence> found,
                                                     ComparisonStrategy comparisonStrategy) {
    return new ShouldNotContainCharSequence("%n" +
                                            "Expecting:%n" +
                                            " <%s>%n" +
                                            "not to contain:%n" +
                                            " <%s>%n" +
                                            "but found:%n" +
                                            " <%s>%n" +
                                            "%s",
                                            actual, values, found, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainCharSequence}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param sequence the sequence of values expected to be in {@code actual}, ignoring case
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainIgnoringCase(CharSequence actual, CharSequence sequence) {
    return new ShouldNotContainCharSequence("%n" +
                                            "Expecting:%n" +
                                            " <%s>%n" +
                                            "not to contain (ignoring case):%n" +
                                            " <%s>%n" +
                                            "%s",
                                            actual, sequence, StandardComparisonStrategy.instance());
  }

  public static ErrorMessageFactory shouldNotContainIgnoringCase(CharSequence actual, CharSequence[] sequences,
                                                                 Set<CharSequence> foundSequences) {
    return new ShouldNotContainCharSequence("%n" +
                                            "Expecting:%n" +
                                            " <%s>%n" +
                                            "not to contain (ignoring case):%n" +
                                            " <%s>%n" +
                                            "but found:%n" +
                                            " <%s>%n" +
                                            "%s",
                                            actual, sequences, foundSequences, StandardComparisonStrategy.instance());
  }

}
