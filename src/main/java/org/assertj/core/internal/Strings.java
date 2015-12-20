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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;
import static java.lang.String.format;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeEqualIgnoringCase.shouldBeEqual;
import static org.assertj.core.error.ShouldBeEqualIgnoringWhitespace.shouldBeEqualIgnoringWhitespace;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldBeSubstring.shouldBeSubstring;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringCase;
import static org.assertj.core.error.ShouldContainCharSequenceOnlyOnce.shouldContainOnlyOnce;
import static org.assertj.core.error.ShouldContainOnlyDigits.shouldContainOnlyDigits;
import static org.assertj.core.error.ShouldContainPattern.shouldContainPattern;
import static org.assertj.core.error.ShouldContainSequence.shouldContainSequence;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldMatchPattern.shouldMatch;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringCase.shouldNotBeEqualIgnoringCase;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringWhitespace.shouldNotBeEqualIgnoringWhitespace;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.error.ShouldNotEndWith.shouldNotEndWith;
import static org.assertj.core.error.ShouldNotMatchPattern.shouldNotMatch;
import static org.assertj.core.error.ShouldNotStartWith.shouldNotStartWith;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.Arrays.assertIsArray;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsNull;
import static org.assertj.core.internal.CommonValidations.checkLineCounts;
import static org.assertj.core.internal.CommonValidations.checkOtherIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkSameSizes;
import static org.assertj.core.internal.CommonValidations.checkSizes;
import static org.assertj.core.internal.CommonValidations.hasSameSizeAsCheck;
import static org.assertj.core.util.Preconditions.checkNotNull;
import static org.assertj.core.util.xml.XmlStringPrettyFormatter.xmlPrettyFormat;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link CharSequence}</code>s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class Strings {

  private static final Strings INSTANCE = new Strings();
  private final ComparisonStrategy comparisonStrategy;
  @VisibleForTesting
  Failures failures = Failures.instance();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * 
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Strings instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Strings() {
    this(StandardComparisonStrategy.instance());
  }

  public Strings(ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
  }

  @VisibleForTesting
  public Comparator<?> getComparator() {
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy) {
      return ((ComparatorBasedComparisonStrategy) comparisonStrategy).getComparator();
    }
    return null;
  }

  /**
   * Asserts that the given {@code CharSequence} is {@code null} or empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} is not {@code null} *and* it is not empty.
   */
  public void assertNullOrEmpty(AssertionInfo info, CharSequence actual) {
    if (actual != null && hasContent(actual)) throw failures.failure(info, shouldBeNullOrEmpty(actual));
  }

  /**
   * Asserts that the given {@code CharSequence} is empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is not empty.
   */
  public void assertEmpty(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (hasContent(actual)) throw failures.failure(info, shouldBeEmpty(actual));
  }

  /**
   * Asserts that the given {@code CharSequence} is not empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is empty.
   */
  public void assertNotEmpty(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (!hasContent(actual)) throw failures.failure(info, shouldNotBeEmpty());
  }

  private static boolean hasContent(CharSequence s) {
    return s.length() > 0;
  }

  /**
   * Asserts that the size of the given {@code CharSequence} is equal to the expected one.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param expectedSize the expected size of {@code actual}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the size of the given {@code CharSequence} is different than the expected one.
   */
  public void assertHasSize(AssertionInfo info, CharSequence actual, int expectedSize) {
    assertNotNull(info, actual);
    checkSizes(actual, actual.length(), expectedSize, info);
  }

  /**
   * Asserts that the line count of the given {@code CharSequence} is equal to the expected one.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param expectedLineCount the expected line count of {@code actual}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the line count of the given {@code CharSequence} is different than the expected one.
   */
  public void assertHasLineCount(AssertionInfo info, CharSequence actual, int expectedLineCount) {
    assertNotNull(info, actual);
    LineNumberReader reader = new LineNumberReader(new StringReader(actual.toString()));
    try {
      while (reader.readLine() != null);
    } catch (IOException e) {
      throw new InputStreamsException(format("Unable to count lines in `%s`", actual), e);
    }
    checkLineCounts(actual, reader.getLineNumber(), expectedLineCount, info);
  }

  /**
   * Asserts that the number of entries in the given {@code CharSequence} has the same size as the other
   * {@code Iterable}.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param other the group to compare
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code CharSequence} does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, CharSequence actual, Iterable<?> other) {
    assertNotNull(info, actual);
    hasSameSizeAsCheck(info, actual, other, actual.length());
  }

  /**
   * Asserts that the number of entries in the given {@code CharSequence} has the same size as the other array.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param array the array to compare
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code CharSequence} does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, CharSequence actual, Object array) {
    Objects.instance().assertNotNull(info, actual);
    assertIsArray(info, array);
    hasSameSizeAsCheck(info, actual, array, actual.length());
  }

  public void assertHasSameSizeAs(AssertionInfo info, CharSequence actual, CharSequence other) {
    Objects.instance().assertNotNull(info, actual);
    checkOtherIsNotNull(other, "CharSequence or String");
    checkSameSizes(info, actual, actual.length(), other.length());
  }

  /**
   * Verifies that the given {@code CharSequence} contains the given strings.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param values the values to look for.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain the given sequence.
   */
  public void assertContains(AssertionInfo info, CharSequence actual, CharSequence... values) {
    assertNotNull(info, actual);
    checkIsNotNull(values);
    checkIsNotEmpty(values);
    checkCharSequenceArrayDoesNotHaveNullElements(values);
    Set<CharSequence> notFound = new LinkedHashSet<>();
    for (CharSequence value : values) {
      if (!stringContains(actual, value)) {
        notFound.add(value);
      }
    }
    if (notFound.isEmpty()) return;
    if (notFound.size() == 1 && values.length == 1) {
      throw failures.failure(info, shouldContain(actual, values[0], comparisonStrategy));
    }
    throw failures.failure(info, shouldContain(actual, values, notFound, comparisonStrategy));
  }

  /**
   * Verifies that the given {@code CharSequence} contains only digits.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws NullPointerException if {@code actual} is {@code null}.
   * @throws AssertionError if {@code actual} contains non-digit characters or contains no digits at all.
   */
  public void assertContainsOnlyDigits(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (actual.length() == 0) throw failures.failure(info, shouldContainOnlyDigits(actual));
    for (int index = 0; index < actual.length(); index++) {
      char character = actual.charAt(index);
      if (!isDigit(character)) throw failures.failure(info, shouldContainOnlyDigits(actual, character, index));
    }
  }

  private void checkIsNotNull(CharSequence... values) {
    if (values == null) throw arrayOfValuesToLookForIsNull();
  }

  private void checkIsNotEmpty(CharSequence... values) {
    if (values.length == 0) throw arrayOfValuesToLookForIsEmpty();
  }

  /**
   * Delegates to {@link ComparisonStrategy#stringContains(String, String)}
   */
  private boolean stringContains(CharSequence actual, CharSequence sequence) {
    return comparisonStrategy.stringContains(actual.toString(), sequence.toString());
  }

  /**
   * Verifies that the given {@code CharSequence} contains the given sequence, ignoring case considerations.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param sequence the sequence to search for.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain the given sequence.
   */
  public void assertContainsIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence sequence) {
    checkCharSequenceIsNotNull(sequence);
    assertNotNull(info, actual);
    if (!actual.toString().toLowerCase().contains(sequence.toString().toLowerCase()))
      throw failures.failure(info, shouldContainIgnoringCase(actual, sequence));
  }

  /**
   * Verifies that the given {@code CharSequence} does not contain the given sequence.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param sequence the sequence to search for.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} contains the given sequence.
   */
  public void assertDoesNotContain(AssertionInfo info, CharSequence actual, CharSequence sequence) {
    checkCharSequenceIsNotNull(sequence);
    assertNotNull(info, actual);
    if (stringContains(actual, sequence))
      throw failures.failure(info, shouldNotContain(actual, sequence, comparisonStrategy));
  }

  private void checkCharSequenceIsNotNull(CharSequence sequence) {
    if (sequence == null) throw new NullPointerException("The char sequence to look for should not be null");
  }

  /**
   * Verifies that two {@code CharSequence}s are equal, ignoring case considerations.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param expected the expected {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence}s are not equal.
   */
  public void assertEqualsIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (!areEqualIgnoringCase(actual, expected)) throw failures.failure(info, shouldBeEqual(actual, expected));
  }

  /**
   * Verifies that two {@code CharSequence}s are not equal, ignoring case considerations.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param expected the expected {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence}s are equal ignoring case considerations.
   */
  public void assertNotEqualsIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (areEqualIgnoringCase(actual, expected))
      throw failures.failure(info, shouldNotBeEqualIgnoringCase(actual, expected));
  }

  private boolean areEqualIgnoringCase(CharSequence actual, CharSequence expected) {
    if (actual == null) return expected == null;
    if (expected == null) return false;
    return actual.toString().equalsIgnoreCase(expected.toString());
  }

  /**
   * Verifies that two {@code CharSequence}s are equal, ignoring any changes in whitespace.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param expected the expected {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence}s are not equal.
   */
  public void assertEqualsIgnoringWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (!areEqualIgnoringWhitespace(actual, expected))
      throw failures.failure(info, shouldBeEqualIgnoringWhitespace(actual, expected));
  }

  /**
   * Verifies that two {@code CharSequence}s are not equal, ignoring any changes in whitespace.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param expected the expected {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence}s are equal.
   */
  public void assertNotEqualsIgnoringWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (areEqualIgnoringWhitespace(actual, expected))
      throw failures.failure(info, shouldNotBeEqualIgnoringWhitespace(actual, expected));
  }

  private boolean areEqualIgnoringWhitespace(CharSequence actual, CharSequence expected) {
    if (actual == null) return expected == null;
    checkCharSequenceIsNotNull(expected);
    return removeAllWhitespaces(actual).equals(removeAllWhitespaces(expected));
  }

  // same implementation as Hamcrest's IsEqualIgnoringWhiteSpace
  private String removeAllWhitespaces(CharSequence toBeStripped) {
    final StringBuilder result = new StringBuilder();
    boolean lastWasSpace = true;
    for (int i = 0; i < toBeStripped.length(); i++) {
      char c = toBeStripped.charAt(i);
      if (isWhitespace(c)) {
        if (!lastWasSpace) result.append(' ');
        lastWasSpace = true;
      } else {
        result.append(c);
        lastWasSpace = false;
      }
    }
    return result.toString().trim();
  }

  /**
   * Verifies that actual {@code CharSequence}s contains only once the given sequence.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param sequence the given {@code CharSequence}.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contains <b>only once</b> the given
   *           {@code CharSequence}.
   */
  public void assertContainsOnlyOnce(AssertionInfo info, CharSequence actual, CharSequence sequence) {
    checkCharSequenceIsNotNull(sequence);
    assertNotNull(info, actual);
    int sequenceOccurencesInActual = countOccurences(sequence, actual);
    if (sequenceOccurencesInActual == 1) return;
    throw failures.failure(info,
                           shouldContainOnlyOnce(actual, sequence, sequenceOccurencesInActual, comparisonStrategy));
  }

  /**
   * Count occurrences of sequenceToSearch in actual {@link CharSequence}.
   * 
   * @param sequenceToSearch the sequence to search in in actual {@link CharSequence}.
   * @param actual the {@link CharSequence} to search occurrences in.
   * @return the number of occurrences of sequenceToSearch in actual {@link CharSequence}.
   */
  private int countOccurences(CharSequence sequenceToSearch, CharSequence actual) {
    String strToSearch = sequenceToSearch.toString();
    String strActual = actual.toString();
    int occurences = 0;
    for (int i = 0; i <= (strActual.length() - strToSearch.length()); i++) {
      if (comparisonStrategy.areEqual(strActual.substring(i, i + sequenceToSearch.length()), strToSearch)) {
        occurences++;
      }
    }
    return occurences;
  }

  /**
   * Verifies that the given {@code CharSequence} starts with the given prefix.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param prefix the given prefix.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not start with the given prefix.
   */
  public void assertStartsWith(AssertionInfo info, CharSequence actual, CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    assertNotNull(info, actual);
    if (!comparisonStrategy.stringStartsWith(actual.toString(), prefix.toString()))
      throw failures.failure(info, shouldStartWith(actual, prefix, comparisonStrategy));
  }

  /**
   * Verifies that the given {@code CharSequence} does not start with the given prefix.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param prefix the given prefix.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} starts with the given prefix.
   * @author Michal Kordas
   */
  public void assertDoesNotStartWith(AssertionInfo info, CharSequence actual, CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    assertNotNull(info, actual);
    if (comparisonStrategy.stringStartsWith(actual.toString(), prefix.toString()))
      throw failures.failure(info, shouldNotStartWith(actual, prefix, comparisonStrategy));
  }

  private static void failIfPrefixIsNull(CharSequence prefix) {
    if (prefix == null) throw new NullPointerException("The given prefix should not be null");
  }

  /**
   * Verifies that the given {@code CharSequence} ends with the given suffix.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param suffix the given suffix.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not end with the given suffix.
   */
  public void assertEndsWith(AssertionInfo info, CharSequence actual, CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    assertNotNull(info, actual);
    if (!comparisonStrategy.stringEndsWith(actual.toString(), suffix.toString()))
      throw failures.failure(info, shouldEndWith(actual, suffix, comparisonStrategy));
  }

  /**
   * Verifies that the given {@code CharSequence} does not end with the given suffix.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param suffix the given suffix.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} ends with the given suffix.
   * @author Michal Kordas
   */
  public void assertDoesNotEndWith(AssertionInfo info, CharSequence actual, CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    assertNotNull(info, actual);
    if (comparisonStrategy.stringEndsWith(actual.toString(), suffix.toString()))
      throw failures.failure(info, shouldNotEndWith(actual, suffix, comparisonStrategy));
  }

  private static void failIfSuffixIsNull(CharSequence suffix) {
    if (suffix == null) throw new NullPointerException("The given suffix should not be null");
  }

  /**
   * Verifies that the given {@code CharSequence} matches the given regular expression.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param regex the regular expression to which the actual {@code CharSequence} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not match the given regular expression.
   */
  public void assertMatches(AssertionInfo info, CharSequence actual, CharSequence regex) {
    checkRegexIsNotNull(regex);
    assertNotNull(info, actual);
    if (!Pattern.matches(regex.toString(), actual)) throw failures.failure(info, shouldMatch(actual, regex));
  }

  /**
   * Verifies that the given {@code CharSequence} does not match the given regular expression.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param regex the regular expression to which the actual {@code CharSequence} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} matches the given regular expression.
   */
  public void assertDoesNotMatch(AssertionInfo info, CharSequence actual, CharSequence regex) {
    checkRegexIsNotNull(regex);
    assertNotNull(info, actual);
    if (Pattern.matches(regex.toString(), actual)) throw failures.failure(info, shouldNotMatch(actual, regex));
  }

  private void checkRegexIsNotNull(CharSequence regex) {
    if (regex == null) throw patternToMatchIsNull();
  }

  /**
   * Verifies that the given {@code CharSequence} matches the given regular expression.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param pattern the regular expression to which the actual {@code CharSequence} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} does not match the given regular expression.
   */
  public void assertMatches(AssertionInfo info, CharSequence actual, Pattern pattern) {
    checkIsNotNull(pattern);
    assertNotNull(info, actual);
    if (!pattern.matcher(actual).matches()) throw failures.failure(info, shouldMatch(actual, pattern.pattern()));
  }

  /**
   * Verifies that the given {@code CharSequence} does not match the given regular expression.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param pattern the regular expression to which the actual {@code CharSequence} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} matches the given regular expression.
   */
  public void assertDoesNotMatch(AssertionInfo info, CharSequence actual, Pattern pattern) {
    checkIsNotNull(pattern);
    if (!(actual == null || !pattern.matcher(actual).matches()))
      throw failures.failure(info, shouldNotMatch(actual, pattern.pattern()));
  }

  private void checkIsNotNull(Pattern pattern) {
    if (pattern == null) throw patternToMatchIsNull();
  }

  private NullPointerException patternToMatchIsNull() {
    return new NullPointerException("The regular expression pattern to match should not be null");
  }

  private void assertNotNull(AssertionInfo info, CharSequence actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  public void assertContainsSequence(AssertionInfo info, CharSequence actual, CharSequence[] sequence) {
    assertNotNull(info, actual);
    checkIsNotNull(sequence);
    checkIsNotEmpty(sequence);
    checkCharSequenceArrayDoesNotHaveNullElements(sequence);

    Set<CharSequence> notFound = new LinkedHashSet<>();
    for (CharSequence value : sequence) {
      if (!stringContains(actual, value)) notFound.add(value);
    }

    if (!notFound.isEmpty()) {
      // don't bother looking for a sequence, some of the sequence elements were not found !
      if (notFound.size() == 1 && sequence.length == 1) {
        throw failures.failure(info, shouldContain(actual, sequence[0], comparisonStrategy));
      }
      throw failures.failure(info, shouldContain(actual, sequence, notFound, comparisonStrategy));
    }

    // we have found all the given values but were they in the expected sequence ?
    if (sequence.length == 1) return; // no order chekec needed for a one element sequence

    // convert all to one char CharSequence list to ease comparison
    List<CharSequence> splitActual = split(actual);
    List<CharSequence> splitSequence = flatSplit(sequence);
    for (int i = 0; i < splitActual.size(); i++) {
      // look for given sequence in actual starting from current index (i)
      if (containsSequenceAtGivenIndex(splitActual, splitSequence, i)) return;
    }
    throw failures.failure(info, shouldContainSequence(actual, sequence, comparisonStrategy));
  }

  private List<CharSequence> flatSplit(CharSequence[] sequence) {
    List<CharSequence> flatSplitCharSequence = new ArrayList<>();
    for (int i = 0; i < sequence.length; i++) {
      flatSplitCharSequence.addAll(split(sequence[i]));
    }
    return flatSplitCharSequence;
  }

  private List<CharSequence> split(CharSequence charSequence) {
    checkNotNull(charSequence, "Expecting CharSequence not to be null");
    int length = charSequence.length();
    List<CharSequence> splitCharSequence = new ArrayList<>(length);
    for (int i = 0; i < length; i++) {
      splitCharSequence.add(String.valueOf(charSequence.charAt(i)));
    }
    return splitCharSequence;
  }

  private boolean containsSequenceAtGivenIndex(List<CharSequence> actualAsList, List<CharSequence> sequence,
                                               int startingIndex) {
    // check that, starting from given index, actualAsList has enough remaining elements to contain sequence
    if (actualAsList.size() - startingIndex < sequence.size()) return false;
    for (int i = 0; i < sequence.size(); i++) {
      if (!comparisonStrategy.areEqual(actualAsList.get(startingIndex + i), sequence.get(i))) return false;
    }
    return true;
  }

  public void assertXmlEqualsTo(AssertionInfo info, CharSequence actualXml, CharSequence expectedXml) {
    // check that actual and expected XML CharSequence are not null.
    // we consider that null values don't make much sense when you want to compare XML document as String/CharSequence.
    checkCharSequenceIsNotNull(expectedXml);
    assertNotNull(info, actualXml);
    // we only use default comparison strategy, it does not make sense to use a specific comparison strategy
    final String formattedActualXml = xmlPrettyFormat(actualXml.toString());
    final String formattedExpectedXml = xmlPrettyFormat(expectedXml.toString());
    if (!comparisonStrategy.areEqual(formattedActualXml, formattedExpectedXml))
      throw failures.failure(info, shouldBeEqual(formattedActualXml, formattedExpectedXml, comparisonStrategy,
                                                 info.representation()));
  }

  public void assertIsSubstringOf(AssertionInfo info, CharSequence actual, CharSequence sequence) {
    assertNotNull(info, actual);
    checkNotNull(sequence, "Expecting CharSequence not to be null");
    if (stringContains(sequence.toString(), actual.toString())) return;
    throw failures.failure(info, shouldBeSubstring(actual, sequence, comparisonStrategy));
  }

  /**
   * Verifies that the given {@code CharSequence} contains the given regular expression.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param regex the regular expression to find in the actual {@code CharSequence}.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain the given regular expression.
   */
  public void assertContainsPattern(AssertionInfo info, CharSequence actual, CharSequence regex) {
    checkRegexIsNotNull(regex);
    assertNotNull(info, actual);
    Pattern pattern = Pattern.compile(regex.toString());
    Matcher matcher = pattern.matcher(actual);
    if (!matcher.find()) throw failures.failure(info, shouldContainPattern(actual, pattern.pattern()));
  }

  /**
   * Verifies that the given {@code CharSequence} contains the given regular expression.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param pattern the regular expression to find in the actual {@code CharSequence}.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} does not contain the given regular expression.
   */
  public void assertContainsPattern(AssertionInfo info, CharSequence actual, Pattern pattern) {
    checkIsNotNull(pattern);
    assertNotNull(info, actual);
    Matcher matcher = pattern.matcher(actual);
    if (!matcher.find()) throw failures.failure(info, shouldContainPattern(actual, pattern.pattern()));
  }

  private void checkCharSequenceArrayDoesNotHaveNullElements(CharSequence[] values) {
    if (values.length == 1) {
      checkCharSequenceIsNotNull(values[0]);
    } else {
      for (int i = 0; i < values.length; i++) {
        checkNotNull(values[i], "Expecting CharSequence elements not to be null but found one at index " + i);
      }
    }
  }

}
