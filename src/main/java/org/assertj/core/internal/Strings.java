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
package org.assertj.core.internal;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.error.ShouldBeBase64.shouldBeBase64;
import static org.assertj.core.error.ShouldBeBlank.shouldBeBlank;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeEqualIgnoringCase.shouldBeEqual;
import static org.assertj.core.error.ShouldBeEqualIgnoringNewLineDifferences.shouldBeEqualIgnoringNewLineDifferences;
import static org.assertj.core.error.ShouldBeEqualIgnoringNewLines.shouldBeEqualIgnoringNewLines;
import static org.assertj.core.error.ShouldBeEqualIgnoringWhitespace.shouldBeEqualIgnoringWhitespace;
import static org.assertj.core.error.ShouldBeEqualNormalizingPunctuationAndWhitespace.shouldBeEqualNormalizingPunctuationAndWhitespace;
import static org.assertj.core.error.ShouldBeEqualNormalizingWhitespace.shouldBeEqualNormalizingWhitespace;
import static org.assertj.core.error.ShouldBeLowerCase.shouldBeLowerCase;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldBeSubstring.shouldBeSubstring;
import static org.assertj.core.error.ShouldBeUpperCase.shouldBeUpperCase;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringCase;
import static org.assertj.core.error.ShouldContainCharSequenceOnlyOnce.shouldContainOnlyOnce;
import static org.assertj.core.error.ShouldContainOneOrMoreWhitespaces.shouldContainOneOrMoreWhitespaces;
import static org.assertj.core.error.ShouldContainOnlyDigits.shouldContainOnlyDigits;
import static org.assertj.core.error.ShouldContainOnlyWhitespaces.shouldContainOnlyWhitespaces;
import static org.assertj.core.error.ShouldContainPattern.shouldContainPattern;
import static org.assertj.core.error.ShouldContainSequenceOfCharSequence.shouldContainSequence;
import static org.assertj.core.error.ShouldContainSubsequenceOfCharSequence.shouldContainSubsequence;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldHaveSizeGreaterThan.shouldHaveSizeGreaterThan;
import static org.assertj.core.error.ShouldHaveSizeGreaterThanOrEqualTo.shouldHaveSizeGreaterThanOrEqualTo;
import static org.assertj.core.error.ShouldHaveSizeLessThan.shouldHaveSizeLessThan;
import static org.assertj.core.error.ShouldHaveSizeLessThanOrEqualTo.shouldHaveSizeLessThanOrEqualTo;
import static org.assertj.core.error.ShouldMatchPattern.shouldMatch;
import static org.assertj.core.error.ShouldNotBeBlank.shouldNotBeBlank;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringCase.shouldNotBeEqualIgnoringCase;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringWhitespace.shouldNotBeEqualIgnoringWhitespace;
import static org.assertj.core.error.ShouldNotBeEqualNormalizingWhitespace.shouldNotBeEqualNormalizingWhitespace;
import static org.assertj.core.error.ShouldNotContainAnyWhitespaces.shouldNotContainAnyWhitespaces;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContainIgnoringCase;
import static org.assertj.core.error.ShouldNotContainOnlyWhitespaces.shouldNotContainOnlyWhitespaces;
import static org.assertj.core.error.ShouldNotContainPattern.shouldNotContainPattern;
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
import static org.assertj.core.internal.CommonValidations.checkSizeBetween;
import static org.assertj.core.internal.CommonValidations.checkSizes;
import static org.assertj.core.internal.CommonValidations.hasSameSizeAsCheck;
import static org.assertj.core.util.xml.XmlStringPrettyFormatter.xmlPrettyFormat;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashSet;
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
 * @author Michal Kordas
 */
public class Strings {

  private static final Strings INSTANCE = new Strings();
  private static final String PUNCTUATION_REGEX = "\\p{Punct}";
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
   * Asserts that the given {@code CharSequence} is {@code Null}, empty or consists of one or more whitespace characters.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} is not blank.
   */
  public void assertBlank(AssertionInfo info, CharSequence actual) {
    if (!isBlank(actual)) throw failures.failure(info, shouldBeBlank(actual));
  }

  /**
   * Asserts that the given {@code CharSequence} contains at least one non-whitespace character.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} is blank.
   */
  public void assertNotBlank(AssertionInfo info, CharSequence actual) {
    if (isBlank(actual)) throw failures.failure(info, shouldNotBeBlank(actual));
  }

  private boolean isBlank(CharSequence actual) {
    return isNullOrEmpty(actual) || strictlyContainsWhitespaces(actual);
  }

  private boolean containsWhitespaces(CharSequence actual) {
    return !isNullOrEmpty(actual) && containsOneOrMoreWhitespaces(actual);
  }

  private boolean containsOnlyWhitespaces(CharSequence actual) {
    return !isNullOrEmpty(actual) && strictlyContainsWhitespaces(actual);
  }

  private boolean isNullOrEmpty(CharSequence actual) {
    return actual == null || actual.length() == 0;
  }

  private boolean containsOneOrMoreWhitespaces(CharSequence actual) {
    return actual.chars().anyMatch(Character::isWhitespace);
  }

  private boolean strictlyContainsWhitespaces(CharSequence actual) {
    return actual.chars().allMatch(Character::isWhitespace);
  }

  /**
   * Asserts that the given {@code CharSequence} contains one or more whitespace characters.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} does not contain any whitespace characters.
   */
  public void assertContainsWhitespaces(AssertionInfo info, CharSequence actual) {
    if (!containsWhitespaces(actual)) throw failures.failure(info, shouldContainOneOrMoreWhitespaces(actual));
  }

  /**
   * Asserts that the given {@code CharSequence} consists of one or more whitespace characters.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} is not blank.
   */
  public void assertContainsOnlyWhitespaces(AssertionInfo info, CharSequence actual) {
    if (!containsOnlyWhitespaces(actual)) throw failures.failure(info, shouldContainOnlyWhitespaces(actual));
  }

  /**
   * Asserts that the given {@code CharSequence} is {@code Null}, empty or contains only non-whitespace characters.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} contains one or more whitespace characters.
   */
  public void assertDoesNotContainAnyWhitespaces(AssertionInfo info, CharSequence actual) {
    if (containsWhitespaces(actual)) throw failures.failure(info, shouldNotContainAnyWhitespaces(actual));
  }

  /**
   * Asserts that the given {@code CharSequence} is {@code Null}, empty or contains at least one non-whitespace character.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} is blank.
   */
  public void assertDoesNotContainOnlyWhitespaces(AssertionInfo info, CharSequence actual) {
    if (containsOnlyWhitespaces(actual)) throw failures.failure(info, shouldNotContainOnlyWhitespaces(actual));
  }

  /**
   * Asserts that the given {@code CharSequence} consists of one or more whitespace characters
   * according to {@link Character#isWhitespace(char)}.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} is not blank.
   */
  public void assertJavaBlank(AssertionInfo info, CharSequence actual) {
    if (!isJavaBlank(actual)) throw failures.failure(info, shouldBeBlank(actual));
  }

  /**
   * Asserts that the given {@code CharSequence} is {@code Null}, empty or contains at least one non-whitespace character
   * according to {@link Character#isWhitespace(char)}.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence} is blank.
   */
  public void assertNotJavaBlank(AssertionInfo info, CharSequence actual) {
    if (isJavaBlank(actual)) throw failures.failure(info, shouldNotBeBlank(actual));
  }

  private boolean isJavaBlank(CharSequence actual) {
    if (actual == null || actual.length() == 0) return false;
    for (int i = 0; i < actual.length(); i++) {
      if (!isWhitespace(actual.charAt(i))) return false;
    }
    return true;
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
   * Asserts that the size of the given {@code CharSequence} is less than the expected size
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param expectedMaxSizeExcluded the expected max size of {@code actual}
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the size of the given {@code CharSequence} is equal to or greater than the expected max size
   */
  public void assertHasSizeLessThan(AssertionInfo info, CharSequence actual, int expectedMaxSizeExcluded) {
    assertNotNull(info, actual);

    if (actual.length() >= expectedMaxSizeExcluded) {
      throw failures.failure(info, shouldHaveSizeLessThan(actual, actual.length(), expectedMaxSizeExcluded));
    }
  }

  /**
   * Asserts that the size of the given {@code CharSequence} is less than or equal to the expected size
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param expectedMaxSizeIncluded the expected max size of {@code actual}
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the size of the given {@code CharSequence} is greater than the expected max size
   */
  public void assertHasSizeLessThanOrEqualTo(AssertionInfo info, CharSequence actual, int expectedMaxSizeIncluded) {
    assertNotNull(info, actual);

    if (actual.length() > expectedMaxSizeIncluded) {
      throw failures.failure(info, shouldHaveSizeLessThanOrEqualTo(actual, actual.length(), expectedMaxSizeIncluded));
    }
  }

  /**
   * Asserts that the size of the given {@code CharSequence} is greater than the expected size
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param expectedMinSizeExcluded the expected min size of {@code actual}
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the size of the given {@code CharSequence} is equal to or less than the expected max size
   */
  public void assertHasSizeGreaterThan(AssertionInfo info, CharSequence actual, int expectedMinSizeExcluded) {
    assertNotNull(info, actual);

    if (actual.length() <= expectedMinSizeExcluded) {
      throw failures.failure(info, shouldHaveSizeGreaterThan(actual, actual.length(), expectedMinSizeExcluded));
    }
  }

  /**
   * Asserts that the size of the given {@code CharSequence} is greater than or equal to the expected size
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param expectedMinSizeIncluded the expected min size of {@code actual}
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the size of the given {@code CharSequence} is greater than the expected max size
   */
  public void assertHasSizeGreaterThanOrEqualTo(AssertionInfo info, CharSequence actual, int expectedMinSizeIncluded) {
    assertNotNull(info, actual);

    if (actual.length() < expectedMinSizeIncluded) {
      throw failures.failure(info, shouldHaveSizeGreaterThanOrEqualTo(actual, actual.length(), expectedMinSizeIncluded));
    }
  }

  /**
   * Asserts that the size of the given {@code CharSequence} is between the given lower and higher boundary (inclusive).
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param lowerBoundary the lower boundary compared to which actual size should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual size should be less than or equal to.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of elements in the given array is not between the boundaries.
   */
  public void assertHasSizeBetween(AssertionInfo info, CharSequence actual, int lowerBoundary, int higherBoundary) {
    assertNotNull(info, actual);
    checkSizeBetween(actual, lowerBoundary, higherBoundary, actual.length(), info);
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
    checkSameSizes(info, actual, other, actual.length(), other.length());
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
    doCommonCheckForCharSequence(info, actual, values);
    Set<CharSequence> notFound = stream(values).filter(value -> !stringContains(actual, value))
                                               .collect(toCollection(LinkedHashSet::new));
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
   * Verifies that the given {@code CharSequence} does not contain any one of the given values, ignoring case considerations.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param values the sequences to search for.
   *
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws NullPointerException if any one of the given values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} contains any one of the given values, ignoring case considerations.
   */
  public void assertDoesNotContainIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence... values) {
    doCommonCheckForCharSequence(info, actual, values);

    String actualLowerCase = actual.toString().toLowerCase();
    Set<CharSequence> foundValues = stream(values).filter(value -> actualLowerCase.contains(value.toString().toLowerCase()))
                                                  .collect(toCollection(LinkedHashSet::new));
    if (foundValues.isEmpty()) return;
    if (foundValues.size() == 1 && values.length == 1) {
      throw failures.failure(info, shouldNotContainIgnoringCase(actual, values[0]));
    }
    throw failures.failure(info, shouldNotContainIgnoringCase(actual, values, foundValues));
  }

  /**
   * Verifies that the given {@code CharSequence} does not contain any one of the given values.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param values the values to search for.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws NullPointerException if any one of the given values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} contains any one of the given values.
   */
  public void assertDoesNotContain(AssertionInfo info, CharSequence actual, CharSequence... values) {
    doCommonCheckForCharSequence(info, actual, values);
    Set<CharSequence> found = stream(values).filter(value -> stringContains(actual, value))
                                            .collect(toCollection(LinkedHashSet::new));
    if (found.isEmpty()) return;
    if (found.size() == 1 && values.length == 1) {
      throw failures.failure(info, shouldNotContain(actual, values[0], comparisonStrategy));
    }
    throw failures.failure(info, shouldNotContain(actual, values, found, comparisonStrategy));
  }

  private static void checkCharSequenceIsNotNull(CharSequence sequence) {
    requireNonNull(sequence, "The char sequence to look for should not be null");
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
    if (!areEqualIgnoringCase(actual, expected)) throw failures.failure(info, shouldBeEqual(actual, expected), actual, expected);
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
   * Verifies that two {@code CharSequence}s are not equal, normalizing newlines.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence} (newlines will be normalized).
   * @param expected the expected {@code CharSequence} (newlines will be normalized)..
   * @throws AssertionError if the given {@code CharSequence}s are equal after normalizing newlines.
   */
  public void assertIsEqualToNormalizingNewlines(AssertionInfo info, CharSequence actual, CharSequence expected) {
    String actualNormalized = normalizeNewlines(actual);
    String expectedNormalized = normalizeNewlines(expected);
    if (!actualNormalized.equals(expectedNormalized))
      throw failures.failure(info, shouldBeEqualIgnoringNewLineDifferences(actual, expected), actual, expected);
  }

  private static String normalizeNewlines(CharSequence actual) {
    return actual.toString().replace("\r\n", "\n");
  }

  /**
   * Verifies that two {@code CharSequence}s are equal, ignoring any differences in whitespace.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param expected the expected {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence}s are not equal.
   */
  public void assertEqualsIgnoringWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (!areEqualIgnoringWhitespace(actual, expected))
      throw failures.failure(info, shouldBeEqualIgnoringWhitespace(actual, expected), actual, expected);
  }

  /**
   * Verifies that two {@code CharSequence}s are not equal, ignoring any differences in whitespace.
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

  private String removeAllWhitespaces(CharSequence toBeStripped) {
    final StringBuilder result = new StringBuilder(toBeStripped.length());
    for (int i = 0; i < toBeStripped.length(); i++) {
      char c = toBeStripped.charAt(i);
      if (isWhitespace(c)) {
        continue;
      }
      result.append(c);
    }
    return result.toString();
  }

  /**
   * Verifies that two {@code CharSequence}s are equal, after the whitespace of both strings
   * has been normalized.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param expected the expected {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence}s are not equal.
   * @since 2.8.0 / 3.8.0
   */
  public void assertEqualsNormalizingWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (!areEqualNormalizingWhitespace(actual, expected))
      throw failures.failure(info, shouldBeEqualNormalizingWhitespace(actual, expected), actual, expected);
  }

  /**
   * Verifies that two {@code CharSequence}s are not equal, after the whitespace of both strings
   * has been normalized.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param expected the expected {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence}s are equal.
   * @since 2.8.0 / 3.8.0
   */
  public void assertNotEqualsNormalizingWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (areEqualNormalizingWhitespace(actual, expected))
      throw failures.failure(info, shouldNotBeEqualNormalizingWhitespace(actual, expected));
  }

  private boolean areEqualNormalizingWhitespace(CharSequence actual, CharSequence expected) {
    if (actual == null) return expected == null;
    checkCharSequenceIsNotNull(expected);
    return normalizeWhitespace(actual).equals(normalizeWhitespace(expected));
  }

  private static String normalizeWhitespace(CharSequence toNormalize) {
    final StringBuilder result = new StringBuilder(toNormalize.length());
    boolean lastWasSpace = true;
    for (int i = 0; i < toNormalize.length(); i++) {
      char c = toNormalize.charAt(i);
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
   * Verifies that two {@code CharSequence}s are equal, after the punctuation of both strings
   * have been normalized.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence}.
   * @param expected the expected {@code CharSequence}.
   * @throws AssertionError if the given {@code CharSequence}s are not equal.
   * @since 3.16.0
   */
  public void assertEqualsNormalizingPunctuationAndWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (!areEqualNormalizingPunctuationAndWhitespace(actual, expected))
      throw failures.failure(info, shouldBeEqualNormalizingPunctuationAndWhitespace(actual, expected), actual, expected);
  }

  private static boolean areEqualNormalizingPunctuationAndWhitespace(CharSequence actual, CharSequence expected) {
    if (actual == null) return expected == null;
    checkCharSequenceIsNotNull(expected);
    return normalizeWhitespaceAndPunctuation(actual).equals(normalizeWhitespaceAndPunctuation(expected));
  }

  private static String normalizeWhitespaceAndPunctuation(CharSequence toNormalize) {
    return normalizeWhitespace(toNormalize.toString().replaceAll(PUNCTUATION_REGEX, ""));
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
    int sequenceOccurrencesInActual = countOccurrences(sequence, actual);
    if (sequenceOccurrencesInActual == 1) return;
    throw failures.failure(info,
                           shouldContainOnlyOnce(actual, sequence, sequenceOccurrencesInActual, comparisonStrategy));
  }

  /**
   * Count occurrences of sequenceToSearch in actual {@link CharSequence}.
   *
   * @param sequenceToSearch the sequence to search in in actual {@link CharSequence}.
   * @param actual the {@link CharSequence} to search occurrences in.
   * @return the number of occurrences of sequenceToSearch in actual {@link CharSequence}.
   */
  private int countOccurrences(CharSequence sequenceToSearch, CharSequence actual) {
    String strToSearch = sequenceToSearch.toString();
    String strActual = actual.toString();
    int occurrences = 0;
    for (int i = 0; i <= (strActual.length() - strToSearch.length()); i++) {
      if (comparisonStrategy.areEqual(strActual.substring(i, i + sequenceToSearch.length()), strToSearch)) {
        occurrences++;
      }
    }
    return occurrences;
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
   */
  public void assertDoesNotStartWith(AssertionInfo info, CharSequence actual, CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    assertNotNull(info, actual);
    if (comparisonStrategy.stringStartsWith(actual.toString(), prefix.toString()))
      throw failures.failure(info, shouldNotStartWith(actual, prefix, comparisonStrategy));
  }

  private static void failIfPrefixIsNull(CharSequence prefix) {
    requireNonNull(prefix, "The given prefix should not be null");
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
   */
  public void assertDoesNotEndWith(AssertionInfo info, CharSequence actual, CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    assertNotNull(info, actual);
    if (comparisonStrategy.stringEndsWith(actual.toString(), suffix.toString()))
      throw failures.failure(info, shouldNotEndWith(actual, suffix, comparisonStrategy));
  }

  private static void failIfSuffixIsNull(CharSequence suffix) {
    requireNonNull(suffix, "The given suffix should not be null");
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

  /**
   * Verifies that the given charSequence contains the given sequence of charSequence, without any other charSequences between them.
   * @param info contains information about the assertion.
   * @param actual the given charSequence.
   * @param sequence the sequence of charSequence to look for.
   * @throws NullPointerException if the given sequence of charSequence is {@code null}.
   * @throws IllegalArgumentException if the given sequence of charSequence is empty.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} does not contain the given sequence of charSequence.
   */
  public void assertContainsSequence(AssertionInfo info, CharSequence actual, CharSequence[] sequence) {
    doCommonCheckForCharSequence(info, actual, sequence);

    Set<CharSequence> notFound = stream(sequence).filter(value -> !stringContains(actual, value))
                                                 .collect(toCollection(LinkedHashSet::new));

    if (!notFound.isEmpty()) {
      // don't bother looking for a sequence, some of the sequence elements were not found !
      if (notFound.size() == 1 && sequence.length == 1) {
        throw failures.failure(info, shouldContain(actual, sequence[0], comparisonStrategy));
      }
      throw failures.failure(info, shouldContain(actual, sequence, notFound, comparisonStrategy));
    }

    // we have found all the given values but were they in the expected order ?
    if (sequence.length == 1) return; // no order check needed for a one element sequence

    // convert all values to one char sequence to compare with the actual char sequence
    String strActual = actual.toString();
    String strSequence = String.join("", sequence);
    if (!stringContains(strActual, strSequence)) {
      throw failures.failure(info, shouldContainSequence(actual, sequence, comparisonStrategy));
    }
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the given values <b>in the given order
   * (possibly with other values between them)</b>.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param subsequence the Strings to look for, in order.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws NullPointerException if the given subsequence is {@code null}.
   * @throws IllegalArgumentException if the given subsequence is empty.
   * @throws AssertionError if the given {@code CharSequence} does not contain the given subsequence of charSequence.
   */
  public void assertContainsSubsequence(AssertionInfo info, CharSequence actual, CharSequence[] subsequence) {
    doCommonCheckForCharSequence(info, actual, subsequence);

    Set<CharSequence> notFound = stream(subsequence).filter(value -> !stringContains(actual, value))
                                                    .collect(toCollection(LinkedHashSet::new));

    if (!notFound.isEmpty()) {
      // don't bother looking for a subsequence, some of the subsequence elements were not found !
      if (notFound.size() == 1 && subsequence.length == 1) {
        throw failures.failure(info, shouldContain(actual, subsequence[0], comparisonStrategy));
      }
      throw failures.failure(info, shouldContain(actual, subsequence, notFound, comparisonStrategy));
    }

    // we have found all the given values but were they in the expected order ?
    if (subsequence.length == 1) return; // no order check needed for a one element subsequence

    // the values are in the correct order if after removing the start of actual up to the
    // subsequence element included, we are able to find the next subsequence element, ex:
    // "{ George Martin }" with subsequence ["George", " ", "Martin"]:
    // - remove up to "George" in "{ George Martin }" -> " Martin }", does it contain " " ?
    // - remove up to " " in " Martin }" -> "Martin }", does it contain "Martin" ?
    // ...
    String actualRest = removeUpTo(actual.toString(), subsequence[0]);
    // check the subsequence second element since we already know the first is present
    for (int i = 1; i < subsequence.length; i++) {
      if (stringContains(actualRest, subsequence[i])) actualRest = removeUpTo(actualRest, subsequence[i]);
      else throw failures.failure(info, shouldContainSubsequence(actual, subsequence, i - 1, comparisonStrategy));
    }
  }

  private String removeUpTo(String string, CharSequence toRemove) {
    int index = indexOf(string, toRemove);
    // remove the start of string up to toRemove included
    return string.substring(index + toRemove.length());
  }

  private int indexOf(String string, CharSequence toFind) {
    for (int i = 0; i < string.length(); i++) {
      if (comparisonStrategy.stringStartsWith(string.substring(i), toFind.toString())) return i;
    }
    return -1;
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
    requireNonNull(sequence, "Expecting CharSequence not to be null");
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

  /**
   * Verifies that the given {@code CharSequence} does not contain the given regular expression.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param regex the regular expression to find in the actual {@code CharSequence}.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} contains the given regular expression.
   */
  public void assertDoesNotContainPattern(AssertionInfo info, CharSequence actual, CharSequence regex) {
    checkRegexIsNotNull(regex);
    Pattern pattern = Pattern.compile(regex.toString());
    assertDoesNotContainPattern(info, actual, pattern);
  }

  /**
   * Verifies that the given {@code CharSequence} does not contain the given regular expression.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code CharSequence}.
   * @param pattern the regular expression to find in the actual {@code CharSequence}.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} contains the given regular expression.
   */
  public void assertDoesNotContainPattern(AssertionInfo info, CharSequence actual, Pattern pattern) {
    checkIsNotNull(pattern);
    assertNotNull(info, actual);
    Matcher matcher = pattern.matcher(actual);
    if (matcher.find()) throw failures.failure(info, shouldNotContainPattern(actual, pattern.pattern()));
  }

  private void checkCharSequenceArrayDoesNotHaveNullElements(CharSequence[] values) {
    if (values.length == 1) {
      checkCharSequenceIsNotNull(values[0]);
    } else {
      for (int i = 0; i < values.length; i++) {
        requireNonNull(values[i], "Expecting CharSequence elements not to be null but found one at index " + i);
      }
    }
  }

  /***
   * Verifies that actual is equal to expected ignoring new lines
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence} (new lines will be ignored).
   * @param expected the expected {@code CharSequence} (new lines will be ignored).
   */
  public void assertIsEqualToIgnoringNewLines(AssertionInfo info, CharSequence actual, CharSequence expected) {
    String actualWithoutNewLines = removeNewLines(actual);
    String expectedWithoutNewLines = removeNewLines(expected);
    if (!actualWithoutNewLines.equals(expectedWithoutNewLines))
      throw failures.failure(info, shouldBeEqualIgnoringNewLines(actual, expected), actual, expected);
  }

  public void assertLowerCase(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (actual.equals(actual.toString().toLowerCase())) return;
    throw failures.failure(info, shouldBeLowerCase(actual));
  }

  public void assertUpperCase(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (actual.equals(actual.toString().toUpperCase())) return;
    throw failures.failure(info, shouldBeUpperCase(actual));
  }

  /***
   * Verifies that actual is a valid Base64 encoded string.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code CharSequence} (new lines will be ignored).
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if {@code actual} is not a valid Base64 encoded string.
   */
  public void assertIsBase64(AssertionInfo info, String actual) {
    assertNotNull(info, actual);
    try {
      Base64.getDecoder().decode(actual);
    } catch (IllegalArgumentException e) {
      throw failures.failure(info, shouldBeBase64(actual));
    }
  }

  private static String removeNewLines(CharSequence text) {
    String normalizedText = normalizeNewlines(text);
    return normalizedText.replace("\n", "");
  }

  private void doCommonCheckForCharSequence(AssertionInfo info, CharSequence actual, CharSequence[] sequence) {
    assertNotNull(info, actual);
    checkIsNotNull(sequence);
    checkIsNotEmpty(sequence);
    checkCharSequenceArrayDoesNotHaveNullElements(sequence);
  }
}
