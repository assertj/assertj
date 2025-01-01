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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.Map;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a {@code CharSequence} contains a Subsequence of
 * several {@code CharSequence}s in order failed.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldContainSubsequenceOfCharSequence extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainSubsequenceOfCharSequence}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @param firstBadOrderIndex first index failing the subsequence.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSubsequence(CharSequence actual, CharSequence[] strings,
                                                             int firstBadOrderIndex) {
    return shouldContainSubsequence(actual, strings, firstBadOrderIndex, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldContainSubsequenceOfCharSequence}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @param badOrderIndex index failing the subsequence.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSubsequence(CharSequence actual, CharSequence[] strings,
                                                             int badOrderIndex, ComparisonStrategy comparisonStrategy) {

    return new ShouldContainSubsequenceOfCharSequence("%nExpecting actual:%n" +
                                                      "  %s%n" +
                                                      "to contain the following CharSequences in this order (possibly with other values between them):%n"
                                                      +
                                                      "  %s%n" +
                                                      "but %s was found before %s%n%s",
                                                      actual, strings, strings[badOrderIndex + 1],
                                                      strings[badOrderIndex],
                                                      comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainSubsequenceOfCharSequence}</code> with detailed error messages about missing subsequences.
   *
   * @param actual the actual value in the failed assertion.
   * @param strings the sequence of values expected to be in {@code actual}.
   * @param notFoundRepeatedSubsequence a map where each key is a subsequence of {@code strings}
   *        that was expected to be found in {@code actual} and the corresponding value is
   *        the number of times it was expected but not found.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainSubsequence(CharSequence actual, CharSequence[] strings,
                                                             Map<CharSequence, Integer> notFoundRepeatedSubsequence,
                                                             ComparisonStrategy comparisonStrategy) {

    String detailedErrorMessage;
    if (notFoundRepeatedSubsequence.size() == 1) {
      Map.Entry<CharSequence, Integer> singleEntry = notFoundRepeatedSubsequence.entrySet().iterator().next();
      detailedErrorMessage = format("But the %s occurrence of \"%s\" was not found", ordinal(singleEntry.getValue() + 1),
                                    singleEntry.getKey());
    } else {
      detailedErrorMessage = notFoundRepeatedSubsequence.entrySet().stream()
                                                        .map(entry -> format("- the %s occurrence of \"%s\" was not found",
                                                                             ordinal(entry.getValue() + 1), entry.getKey()))
                                                        .collect(joining("%n"));
      detailedErrorMessage = "But:%n" + detailedErrorMessage;
    }

    return new ShouldContainSubsequenceOfCharSequence("%nExpecting actual:%n" +
                                                      "  %s%n" +
                                                      "to contain the following CharSequences in this order (possibly with other values between them):%n"
                                                      +
                                                      "  %s%n" +
                                                      detailedErrorMessage + "%n%s",
                                                      actual, strings, comparisonStrategy);
  }

  /**
   * Returns the ordinal representation of a given integer.
   * <p>
   * This method converts integers to their ordinal form (e.g., 1 to "1st", 2 to "2nd", etc.).
   * Special cases for numbers ending in 11, 12, and 13 are handled to return "th" instead of
   * "st", "nd", or "rd".
   * </p>
   *
   * @param i the integer to convert
   * @return the ordinal representation of {@code i}
   */
  private static String ordinal(int i) {
    int mod100 = i % 100;
    int mod10 = i % 10;

    if (mod10 == 1 && mod100 != 11) return i + "st";
    if (mod10 == 2 && mod100 != 12) return i + "nd";
    if (mod10 == 3 && mod100 != 13) return i + "rd";

    return i + "th";
  }

  private ShouldContainSubsequenceOfCharSequence(String format, CharSequence actual, CharSequence[] strings,
                                                 CharSequence foundButBadOrder,
                                                 CharSequence foundButBadOrder2,
                                                 ComparisonStrategy comparisonStrategy) {
    super(format, actual, strings, foundButBadOrder, foundButBadOrder2, comparisonStrategy);
  }

  private ShouldContainSubsequenceOfCharSequence(String format, CharSequence actual, CharSequence[] strings,
                                                 ComparisonStrategy comparisonStrategy) {
    super(format, actual, strings, comparisonStrategy);
  }
}
