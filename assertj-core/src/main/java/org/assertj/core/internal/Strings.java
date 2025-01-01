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
package org.assertj.core.internal;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Locale.ROOT;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.error.ShouldBeBase64.shouldBeBase64;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeEqualIgnoringCase.shouldBeEqual;
import static org.assertj.core.error.ShouldBeEqualIgnoringNewLineDifferences.shouldBeEqualIgnoringNewLineDifferences;
import static org.assertj.core.error.ShouldBeEqualIgnoringNewLines.shouldBeEqualIgnoringNewLines;
import static org.assertj.core.error.ShouldBeEqualIgnoringWhitespace.shouldBeEqualIgnoringWhitespace;
import static org.assertj.core.error.ShouldBeEqualNormalizingPunctuationAndWhitespace.shouldBeEqualNormalizingPunctuationAndWhitespace;
import static org.assertj.core.error.ShouldBeEqualNormalizingUnicode.shouldBeEqualNormalizingUnicode;
import static org.assertj.core.error.ShouldBeEqualNormalizingWhitespace.shouldBeEqualNormalizingWhitespace;
import static org.assertj.core.error.ShouldBeLowerCase.shouldBeLowerCase;
import static org.assertj.core.error.ShouldBeMixedCase.shouldBeMixedCase;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldBeSubstring.shouldBeSubstring;
import static org.assertj.core.error.ShouldBeUpperCase.shouldBeUpperCase;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldContainCharSequence.containsIgnoringNewLines;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringCase;
import static org.assertj.core.error.ShouldContainCharSequenceOnlyOnce.shouldContainOnlyOnce;
import static org.assertj.core.error.ShouldContainOnlyDigits.shouldContainOnlyDigits;
import static org.assertj.core.error.ShouldContainPattern.shouldContainPattern;
import static org.assertj.core.error.ShouldContainSequenceOfCharSequence.shouldContainSequence;
import static org.assertj.core.error.ShouldContainSubsequenceOfCharSequence.shouldContainSubsequence;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldEndWithIgnoringCase.shouldEndWithIgnoringCase;
import static org.assertj.core.error.ShouldHaveSizeGreaterThan.shouldHaveSizeGreaterThan;
import static org.assertj.core.error.ShouldHaveSizeGreaterThanOrEqualTo.shouldHaveSizeGreaterThanOrEqualTo;
import static org.assertj.core.error.ShouldHaveSizeLessThan.shouldHaveSizeLessThan;
import static org.assertj.core.error.ShouldHaveSizeLessThanOrEqualTo.shouldHaveSizeLessThanOrEqualTo;
import static org.assertj.core.error.ShouldMatchPattern.shouldMatch;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringCase.shouldNotBeEqualIgnoringCase;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringWhitespace.shouldNotBeEqualIgnoringWhitespace;
import static org.assertj.core.error.ShouldNotBeEqualNormalizingWhitespace.shouldNotBeEqualNormalizingWhitespace;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContainIgnoringCase;
import static org.assertj.core.error.ShouldNotContainPattern.shouldNotContainPattern;
import static org.assertj.core.error.ShouldNotEndWith.shouldNotEndWith;
import static org.assertj.core.error.ShouldNotEndWithIgnoringCase.shouldNotEndWithIgnoringCase;
import static org.assertj.core.error.ShouldNotMatchPattern.shouldNotMatch;
import static org.assertj.core.error.ShouldNotStartWith.shouldNotStartWith;
import static org.assertj.core.error.ShouldNotStartWithIgnoringCase.shouldNotStartWithIgnoringCase;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.error.ShouldStartWithIgnoringCase.shouldStartWithIgnoringCase;
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
import java.io.UncheckedIOException;
import java.text.Normalizer;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Michal Kordas
 */
public class Strings {

  private static final Set<Character> NON_BREAKING_SPACES;

  static {
    Set<Character> nonBreakingSpaces = new HashSet<>();
    nonBreakingSpaces.add('\u00A0');
    nonBreakingSpaces.add('\u2007');
    nonBreakingSpaces.add('\u202F');
    NON_BREAKING_SPACES = Collections.unmodifiableSet(nonBreakingSpaces);
  }

  private static final String EMPTY_STRING = "";
  private static final Strings INSTANCE = new Strings();
  private static final String PUNCTUATION_REGEX = "\\p{Punct}";
  private final ComparisonStrategy comparisonStrategy;
  @VisibleForTesting
  Failures failures = Failures.instance();

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

  public void assertNullOrEmpty(AssertionInfo info, CharSequence actual) {
    if (actual != null && hasContent(actual)) throw failures.failure(info, shouldBeNullOrEmpty(actual));
  }

  public void assertEmpty(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (hasContent(actual)) throw failures.failure(info, shouldBeEmpty(actual));
  }

  public void assertNotEmpty(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (!hasContent(actual)) throw failures.failure(info, shouldNotBeEmpty());
  }

  private static boolean hasContent(CharSequence s) {
    return s.length() > 0;
  }

  public void assertHasSize(AssertionInfo info, CharSequence actual, int expectedSize) {
    assertNotNull(info, actual);
    checkSizes(actual, actual.length(), expectedSize, info);
  }

  public void assertHasSizeLessThan(AssertionInfo info, CharSequence actual, int expectedMaxSizeExcluded) {
    assertNotNull(info, actual);

    if (actual.length() >= expectedMaxSizeExcluded) {
      throw failures.failure(info, shouldHaveSizeLessThan(actual, actual.length(), expectedMaxSizeExcluded));
    }
  }

  public void assertHasSizeLessThanOrEqualTo(AssertionInfo info, CharSequence actual, int expectedMaxSizeIncluded) {
    assertNotNull(info, actual);

    if (actual.length() > expectedMaxSizeIncluded) {
      throw failures.failure(info, shouldHaveSizeLessThanOrEqualTo(actual, actual.length(), expectedMaxSizeIncluded));
    }
  }

  public void assertHasSizeGreaterThan(AssertionInfo info, CharSequence actual, int expectedMinSizeExcluded) {
    assertNotNull(info, actual);

    if (actual.length() <= expectedMinSizeExcluded) {
      throw failures.failure(info, shouldHaveSizeGreaterThan(actual, actual.length(), expectedMinSizeExcluded));
    }
  }

  public void assertHasSizeGreaterThanOrEqualTo(AssertionInfo info, CharSequence actual, int expectedMinSizeIncluded) {
    assertNotNull(info, actual);

    if (actual.length() < expectedMinSizeIncluded) {
      throw failures.failure(info, shouldHaveSizeGreaterThanOrEqualTo(actual, actual.length(), expectedMinSizeIncluded));
    }
  }

  public void assertHasSizeBetween(AssertionInfo info, CharSequence actual, int lowerBoundary, int higherBoundary) {
    assertNotNull(info, actual);
    checkSizeBetween(actual, lowerBoundary, higherBoundary, actual.length(), info);
  }

  public void assertHasLineCount(AssertionInfo info, CharSequence actual, int expectedLineCount) {
    assertNotNull(info, actual);
    LineNumberReader reader = new LineNumberReader(new StringReader(actual.toString()));
    try {
      while (reader.readLine() != null);
    } catch (IOException e) {
      throw new UncheckedIOException(format("Unable to count lines in `%s`", actual), e);
    }
    checkLineCounts(actual, reader.getLineNumber(), expectedLineCount, info);
  }

  public void assertHasSameSizeAs(AssertionInfo info, CharSequence actual, Iterable<?> other) {
    assertNotNull(info, actual);
    hasSameSizeAsCheck(info, actual, other, actual.length());
  }

  public void assertHasSameSizeAs(AssertionInfo info, CharSequence actual, Object array) {
    assertNotNull(info, actual);
    assertIsArray(info, array);
    hasSameSizeAsCheck(info, actual, array, actual.length());
  }

  public void assertHasSameSizeAs(AssertionInfo info, CharSequence actual, CharSequence other) {
    assertNotNull(info, actual);
    checkOtherIsNotNull(other, "CharSequence or String");
    checkSameSizes(info, actual, other, actual.length(), other.length());
  }

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

  public void assertContainsAnyOf(AssertionInfo info, CharSequence actual, CharSequence[] values) {
    doCommonCheckForCharSequence(info, actual, values);
    boolean found = stream(values).anyMatch(value -> stringContains(actual, value));
    if (!found) throw failures.failure(info, shouldContainAnyOf(actual, values, comparisonStrategy));
  }

  public void assertContainsOnlyDigits(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (actual.length() == 0) throw failures.failure(info, shouldContainOnlyDigits(actual));
    for (int index = 0; index < actual.length(); index++) {
      char character = actual.charAt(index);
      if (!isDigit(character)) throw failures.failure(info, shouldContainOnlyDigits(actual, character, index));
    }
  }

  private static void checkIsNotNull(CharSequence... values) {
    if (values == null) throw arrayOfValuesToLookForIsNull();
  }

  private static void checkIsNotEmpty(CharSequence... values) {
    if (values.length == 0) throw arrayOfValuesToLookForIsEmpty();
  }

  private boolean stringContains(CharSequence actual, CharSequence sequence) {
    return comparisonStrategy.stringContains(actual.toString(), sequence.toString());
  }

  public void assertContainsIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence sequence) {
    checkCharSequenceIsNotNull(sequence);
    assertNotNull(info, actual);
    if (!containsIgnoreCase(actual, sequence))
      throw failures.failure(info, shouldContainIgnoringCase(actual, sequence));
  }

  private boolean containsIgnoreCase(CharSequence actual, CharSequence sequence) {
    return comparisonStrategy.stringContains(actual.toString().toLowerCase(ROOT), sequence.toString().toLowerCase(ROOT));
  }

  public void assertContainsIgnoringNewLines(final AssertionInfo info, final CharSequence actual, final CharSequence... values) {
    doCommonCheckForCharSequence(info, actual, values);
    final String actualNoNewLines = removeNewLines(actual);
    Set<CharSequence> notFound = stream(values).filter(value -> !stringContains(actualNoNewLines, removeNewLines(value)))
                                               .collect(toCollection(LinkedHashSet::new));
    if (notFound.isEmpty()) return;
    throw failures.failure(info, containsIgnoringNewLines(actual, values, notFound, comparisonStrategy));
  }

  public void assertDoesNotContainIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence... values) {
    doCommonCheckForCharSequence(info, actual, values);

    Set<CharSequence> foundValues = stream(values).filter(value -> containsIgnoreCase(actual, value))
                                                  .collect(toCollection(LinkedHashSet::new));
    if (foundValues.isEmpty()) return;
    if (foundValues.size() == 1 && values.length == 1) {
      throw failures.failure(info, shouldNotContainIgnoringCase(actual, values[0]));
    }
    throw failures.failure(info, shouldNotContainIgnoringCase(actual, values, foundValues));
  }

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

  public void assertEqualsIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (!areEqualIgnoringCase(actual, expected))
      throw failures.failure(info, shouldBeEqual(actual, expected), actual, expected);
  }

  public void assertNotEqualsIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (areEqualIgnoringCase(actual, expected))
      throw failures.failure(info, shouldNotBeEqualIgnoringCase(actual, expected));
  }

  private static boolean areEqualIgnoringCase(CharSequence actual, CharSequence expected) {
    if (actual == null) return expected == null;
    if (expected == null) return false;
    return actual.toString().equalsIgnoreCase(expected.toString());
  }

  public void assertIsEqualToNormalizingNewlines(AssertionInfo info, CharSequence actual, CharSequence expected) {
    String normalizedActual = normalizeNewlines(actual);
    String normalizedExpected = normalizeNewlines(expected);
    if (!java.util.Objects.equals(normalizedActual, normalizedExpected))
      throw failures.failure(info, shouldBeEqualIgnoringNewLineDifferences(actual, expected), normalizedActual,
                             normalizedExpected);
  }

  private static String normalizeNewlines(CharSequence charSequence) {
    return charSequence != null ? charSequence.toString().replace("\r\n", "\n") : null;
  }

  public void assertEqualsIgnoringWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (!areEqualIgnoringWhitespace(actual, expected))
      throw failures.failure(info, shouldBeEqualIgnoringWhitespace(actual, expected), actual, expected);
  }

  public void assertNotEqualsIgnoringWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (areEqualIgnoringWhitespace(actual, expected))
      throw failures.failure(info, shouldNotBeEqualIgnoringWhitespace(actual, expected));
  }

  private boolean areEqualIgnoringWhitespace(CharSequence actual, CharSequence expected) {
    if (actual == null) return expected == null;
    checkCharSequenceIsNotNull(expected);
    return removeAllWhitespaces(actual).equals(removeAllWhitespaces(expected));
  }

  public static String removeAllWhitespaces(CharSequence toBeStripped) {
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

  public void assertEqualsNormalizingWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (actual != null) checkCharSequenceIsNotNull(expected);
    String normalizedActual = normalizeWhitespace(actual);
    String normalizedExpected = normalizeWhitespace(expected);
    if (!java.util.Objects.equals(normalizedActual, normalizedExpected))
      throw failures.failure(info, shouldBeEqualNormalizingWhitespace(actual, expected), normalizedActual, normalizedExpected);
  }

  public void assertNotEqualsNormalizingWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (actual != null) checkCharSequenceIsNotNull(expected);
    String normalizedActual = normalizeWhitespace(actual);
    String normalizedExpected = normalizeWhitespace(expected);
    if (java.util.Objects.equals(normalizedActual, normalizedExpected))
      throw failures.failure(info, shouldNotBeEqualNormalizingWhitespace(actual, expected));
  }

  private static String normalizeWhitespace(CharSequence toNormalize) {
    if (toNormalize == null) return null;
    final StringBuilder result = new StringBuilder(toNormalize.length());
    boolean lastWasSpace = true;
    for (int i = 0; i < toNormalize.length(); i++) {
      char c = toNormalize.charAt(i);
      if (isWhitespace(c) || NON_BREAKING_SPACES.contains(c)) {
        if (!lastWasSpace) result.append(' ');
        lastWasSpace = true;
      } else {
        result.append(c);
        lastWasSpace = false;
      }
    }
    return result.toString().trim();
  }

  public void assertEqualsNormalizingPunctuationAndWhitespace(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (actual != null) checkCharSequenceIsNotNull(expected);
    String normalizedActual = normalizeWhitespaceAndPunctuation(actual);
    String normalizedExpected = normalizeWhitespaceAndPunctuation(expected);
    if (!java.util.Objects.equals(normalizedActual, normalizedExpected))
      throw failures.failure(info, shouldBeEqualNormalizingPunctuationAndWhitespace(actual, expected), normalizedActual,
                             normalizedExpected);
  }

  private static String normalizeWhitespaceAndPunctuation(CharSequence input) {
    return input == null ? null : normalizeWhitespace(input.toString().replaceAll(PUNCTUATION_REGEX, EMPTY_STRING));
  }

  public void assertEqualsToNormalizingUnicode(AssertionInfo info, CharSequence actual, CharSequence expected) {
    if (actual != null) checkCharSequenceIsNotNull(expected);
    String normalizedActual = Normalizer.normalize(actual, Normalizer.Form.NFC);
    String normalizedExpected = Normalizer.normalize(expected, Normalizer.Form.NFC);
    if (!java.util.Objects.equals(normalizedActual, normalizedExpected))
      throw failures.failure(info, shouldBeEqualNormalizingUnicode(actual, expected, normalizedActual, normalizedExpected),
                             normalizedActual, normalizedExpected);
  }

  public void assertContainsOnlyOnce(AssertionInfo info, CharSequence actual, CharSequence sequence) {
    checkCharSequenceIsNotNull(sequence);
    assertNotNull(info, actual);
    int sequenceOccurrencesInActual = countOccurrences(sequence, actual);
    if (sequenceOccurrencesInActual == 1) return;
    throw failures.failure(info,
                           shouldContainOnlyOnce(actual, sequence, sequenceOccurrencesInActual, comparisonStrategy));
  }

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

  public void assertStartsWith(AssertionInfo info, CharSequence actual, CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    assertNotNull(info, actual);
    if (!startsWith(actual, prefix, false))
      throw failures.failure(info, shouldStartWith(actual, prefix, comparisonStrategy));
  }

  public void assertStartsWithIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    assertNotNull(info, actual);
    if (!startsWith(actual, prefix, true))
      throw failures.failure(info, shouldStartWithIgnoringCase(actual, prefix, comparisonStrategy));
  }

  public void assertDoesNotStartWith(AssertionInfo info, CharSequence actual, CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    assertNotNull(info, actual);
    if (startsWith(actual, prefix, false))
      throw failures.failure(info, shouldNotStartWith(actual, prefix, comparisonStrategy));
  }

  public void assertDoesNotStartWithIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    assertNotNull(info, actual);
    if (startsWith(actual, prefix, true))
      throw failures.failure(info, shouldNotStartWithIgnoringCase(actual, prefix, comparisonStrategy));
  }

  private static void failIfPrefixIsNull(CharSequence prefix) {
    requireNonNull(prefix, "The given prefix should not be null");
  }

  private boolean startsWith(CharSequence actual, CharSequence prefix, boolean ignoreCase) {
    return ignoreCase
        ? comparisonStrategy.stringStartsWith(actual.toString().toLowerCase(ROOT), prefix.toString().toLowerCase(ROOT))
        : comparisonStrategy.stringStartsWith(actual.toString(), prefix.toString());
  }

  public void assertEndsWith(AssertionInfo info, CharSequence actual, CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    assertNotNull(info, actual);
    if (!endsWith(actual, suffix, false))
      throw failures.failure(info, shouldEndWith(actual, suffix, comparisonStrategy));
  }

  public void assertEndsWithIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    assertNotNull(info, actual);
    if (!endsWith(actual, suffix, true))
      throw failures.failure(info, shouldEndWithIgnoringCase(actual, suffix, comparisonStrategy));
  }

  public void assertDoesNotEndWith(AssertionInfo info, CharSequence actual, CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    assertNotNull(info, actual);
    if (endsWith(actual, suffix, false))
      throw failures.failure(info, shouldNotEndWith(actual, suffix, comparisonStrategy));
  }

  public void assertDoesNotEndWithIgnoringCase(AssertionInfo info, CharSequence actual, CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    assertNotNull(info, actual);
    if (endsWith(actual, suffix, true))
      throw failures.failure(info, shouldNotEndWithIgnoringCase(actual, suffix, comparisonStrategy));
  }

  private static void failIfSuffixIsNull(CharSequence suffix) {
    requireNonNull(suffix, "The given suffix should not be null");
  }

  private boolean endsWith(CharSequence actual, CharSequence suffix, boolean ignoreCase) {
    return ignoreCase
        ? comparisonStrategy.stringEndsWith(actual.toString().toLowerCase(ROOT), suffix.toString().toLowerCase(ROOT))
        : comparisonStrategy.stringEndsWith(actual.toString(), suffix.toString());
  }

  public void assertMatches(AssertionInfo info, CharSequence actual, CharSequence regex) {
    checkRegexIsNotNull(regex);
    assertNotNull(info, actual);
    if (!Pattern.matches(regex.toString(), actual)) throw failures.failure(info, shouldMatch(actual, regex));
  }

  public void assertDoesNotMatch(AssertionInfo info, CharSequence actual, CharSequence regex) {
    checkRegexIsNotNull(regex);
    assertNotNull(info, actual);
    if (Pattern.matches(regex.toString(), actual)) throw failures.failure(info, shouldNotMatch(actual, regex));
  }

  private static void checkRegexIsNotNull(CharSequence regex) {
    if (regex == null) throw patternToMatchIsNull();
  }

  public void assertMatches(AssertionInfo info, CharSequence actual, Pattern pattern) {
    checkIsNotNull(pattern);
    assertNotNull(info, actual);
    assertMatches(info, actual, pattern.matcher(actual));
  }

  public void assertMatches(AssertionInfo info, CharSequence actual, Matcher matcher) {
    checkIsNotNull(matcher);
    assertNotNull(info, actual);
    if (!matcher.matches()) throw failures.failure(info, shouldMatch(actual, matcher.pattern().pattern()));
  }

  public void assertDoesNotMatch(AssertionInfo info, CharSequence actual, Pattern pattern) {
    checkIsNotNull(pattern);
    if (!(actual == null || !pattern.matcher(actual).matches()))
      throw failures.failure(info, shouldNotMatch(actual, pattern.pattern()));
  }

  private static void checkIsNotNull(Pattern pattern) {
    if (pattern == null) throw patternToMatchIsNull();
  }

  private static NullPointerException patternToMatchIsNull() {
    return new NullPointerException("The regular expression pattern to match should not be null");
  }

  private void checkIsNotNull(Matcher matcher) {
    if (matcher == null) throw new NullPointerException("The matcher should not be null");
  }

  private static void assertNotNull(AssertionInfo info, CharSequence actual) {
    Objects.instance().assertNotNull(info, actual);
  }

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
    String strSequence = String.join(EMPTY_STRING, sequence);
    if (!stringContains(strActual, strSequence)) {
      throw failures.failure(info, shouldContainSequence(actual, sequence, comparisonStrategy));
    }
  }

  public void assertContainsSubsequence(AssertionInfo info, CharSequence actual, CharSequence[] subsequence) {
    doCommonCheckForCharSequence(info, actual, subsequence);

    Map<CharSequence, Integer> notFound = getNotFoundSubsequence(actual, subsequence);
    handleNotFound(info, actual, subsequence, notFound);

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

  /**
   * Handles the scenario where certain subsequences were not found in the actual CharSequence.
   * Depending on the exact mismatch details, it throws appropriate assertion failures.
   *
   * @param info        Assertion metadata.
   * @param actual      The actual CharSequence being checked.
   * @param subsequence The expected subsequence to be found in the actual CharSequence.
   * @param notFound    A map containing subsequences that were not found (or not found enough times) and their respective counts.
   */
  private void handleNotFound(AssertionInfo info, CharSequence actual,
                              CharSequence[] subsequence, Map<CharSequence, Integer> notFound) {

    // If there are no missing subsequences, there's nothing to handle, so return.
    if (notFound.isEmpty()) return;

    // Special case: If there's only one missing subsequence, and we were only looking for one,
    // throw a specific failure for that.
    if (notFound.size() == 1 && subsequence.length == 1) {
      throw failures.failure(info, shouldContain(actual, subsequence[0], comparisonStrategy));
    }

    // Check if all the missing subsequences are due to not finding duplicates.
    // If every value in 'notFound' map is greater than 0, this indicates that the corresponding
    // subsequences were found, but not as many times as expected.
    boolean anyDuplicateSubsequenceFound = notFound.values().stream().allMatch(count -> count > 0);

    // If the above is true, throw a failure specifying the subsequence mismatch details.
    if (anyDuplicateSubsequenceFound) {
      throw failures.failure(info, shouldContainSubsequence(actual, subsequence, notFound, comparisonStrategy));
    }

    // Otherwise, filter the 'notFound' map to get the keys (subsequences) that were not found at all (value is 0).
    Set<CharSequence> notFoundKeysWithZeroValue = notFound.entrySet().stream()
                                                          .filter(entry -> entry.getValue() == 0)
                                                          .map(Map.Entry::getKey)
                                                          .collect(Collectors.toSet());
    // Throw a failure specifying the completely missing subsequences.
    throw failures.failure(info, shouldContain(actual, subsequence, notFoundKeysWithZeroValue, comparisonStrategy));
  }

  /**
   * Computes and returns a map of subsequence elements that were not found (or not found enough times) in actual.
   *
   * @param actual      The actual CharSequence being checked.
   * @param subsequence The expected subsequence to be found in the actual CharSequence.
   * @return A map where the key represents the missing subsequence and the value represents the number of times it appears in 'actual'.
   */
  private Map<CharSequence, Integer> getNotFoundSubsequence(CharSequence actual, CharSequence[] subsequence) {
    // Create a map to store how many times each element appears in the 'actual' sequence.
    // We use a HashMap for efficient look-ups and modifications.
    Map<CharSequence, Integer> actualCounts = new HashMap<>();

    // Create a map to store how many times each element appears in the 'subsequence' array.
    // We use the Java Streams API to group the elements by their identity and then count their occurrences.
    Map<CharSequence, Long> subseqCounts = stream(subsequence).collect(groupingBy(identity(), counting()));

    // For each element in the 'subsequence', compute its occurrences in the 'actual' sequence.
    // If the element is not yet in the actualCounts map (v is null), then count its occurrences in 'actual'.
    // If the element is already in the actualCounts map (v is not null), then keep its current count.
    for (CharSequence value : subsequence) {
      actualCounts.compute(value, (k, v) -> v == null ? countOccurrences(k, actual) : v);
    }
    // Return a map that contains only the elements from the 'subsequence' that appear more times in 'subsequence' than
    // in 'actual'. The map's keys are the elements and the values are the number of times they appear in 'actual'.
    return subseqCounts.entrySet().stream()
                       .filter(entry -> entry.getValue() > actualCounts.getOrDefault(entry.getKey(), 0))
                       .collect(toMap(// The key of the output map entry is the same as the subsequence entry key.
                                      Map.Entry::getKey,
                                      // The value of the output map entry is the number of times the key appears in
                                      // 'actual'.
                                      entry -> actualCounts.get(entry.getKey()),
                                      // If there are duplicate keys when collecting (which shouldn't happen in this
                                      // case), prefer the existing key.
                                      (existing, replacement) -> existing,
                                      // Use a LinkedHashMap to maintain the insertion order.
                                      LinkedHashMap::new));
  }

  private String removeUpTo(String string, CharSequence toRemove) {
    // we have already checked that toRemove was not null in doCommonCheckForCharSequence and this point string is not neither
    int index = indexOf(string, toRemove);
    // remove the start of string up to toRemove included
    return string.substring(index + toRemove.length());
  }

  private int indexOf(String string, CharSequence toFind) {
    if (EMPTY_STRING.equals(string) && EMPTY_STRING.equals(toFind.toString())) return 0;
    for (int i = 0; i < string.length(); i++) {
      if (comparisonStrategy.stringStartsWith(string.substring(i), toFind.toString())) return i;
    }
    // should not arrive here since we this method is used from assertContainsSubsequence at a step where we know that toFind
    // was found and we are checking whether it was at the right place/order.
    throw new IllegalStateException(format("%s should have been found in %s, please raise an assertj-core issue", toFind,
                                           string));
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

  public void assertContainsPattern(AssertionInfo info, CharSequence actual, CharSequence regex) {
    checkRegexIsNotNull(regex);
    assertContainsPattern(info, actual, Pattern.compile(regex.toString()));
  }

  public void assertContainsPattern(AssertionInfo info, CharSequence actual, Matcher matcher) {
    assertNotNull(info, actual);
    checkIsNotNull(matcher);
    if (!matcher.find()) throw failures.failure(info, shouldContainPattern(actual, matcher.pattern().pattern()));
  }

  public void assertContainsPattern(AssertionInfo info, CharSequence actual, Pattern pattern) {
    checkIsNotNull(pattern);
    assertNotNull(info, actual);
    Matcher matcher = pattern.matcher(actual);
    if (!matcher.find()) throw failures.failure(info, shouldContainPattern(actual, pattern.pattern()));
  }

  public void assertDoesNotContainPattern(AssertionInfo info, CharSequence actual, CharSequence regex) {
    checkRegexIsNotNull(regex);
    Pattern pattern = Pattern.compile(regex.toString());
    assertDoesNotContainPattern(info, actual, pattern);
  }

  public void assertDoesNotContainPattern(AssertionInfo info, CharSequence actual, Pattern pattern) {
    checkIsNotNull(pattern);
    assertNotNull(info, actual);
    Matcher matcher = pattern.matcher(actual);
    if (matcher.find()) throw failures.failure(info, shouldNotContainPattern(actual, pattern.pattern()));
  }

  private static void checkCharSequenceArrayDoesNotHaveNullElements(CharSequence[] values) {
    if (values.length == 1) {
      checkCharSequenceIsNotNull(values[0]);
    } else {
      for (int i = 0; i < values.length; i++) {
        requireNonNull(values[i], "Expecting CharSequence elements not to be null but found one at index " + i);
      }
    }
  }

  public void assertIsEqualToIgnoringNewLines(AssertionInfo info, CharSequence actual, CharSequence expected) {
    String actualWithoutNewLines = removeNewLines(actual);
    String expectedWithoutNewLines = removeNewLines(expected);
    if (!actualWithoutNewLines.equals(expectedWithoutNewLines))
      throw failures.failure(info, shouldBeEqualIgnoringNewLines(actual, expected), actual, expected);
  }

  public void assertLowerCase(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (!isLowerCase(actual)) throw failures.failure(info, shouldBeLowerCase(actual));
  }

  private boolean isLowerCase(CharSequence actual) {
    return actual.equals(actual.toString().toLowerCase());
  }

  public void assertUpperCase(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (!isUpperCase(actual)) throw failures.failure(info, shouldBeUpperCase(actual));
  }

  private boolean isUpperCase(CharSequence actual) {
    return actual.equals(actual.toString().toUpperCase());
  }

  public void assertMixedCase(AssertionInfo info, CharSequence actual) {
    assertNotNull(info, actual);
    if (isLowerCase(actual) != isUpperCase(actual)) throw failures.failure(info, shouldBeMixedCase(actual));
  }

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
    return normalizedText.replace("\n", EMPTY_STRING);
  }

  public static void doCommonCheckForCharSequence(AssertionInfo info, CharSequence actual, CharSequence[] sequence) {
    assertNotNull(info, actual);
    checkIsNotNull(sequence);
    checkIsNotEmpty(sequence);
    checkCharSequenceArrayDoesNotHaveNullElements(sequence);
  }

}
