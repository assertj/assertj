/*
 * Created on Dec 22, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldBeEmpty.shouldBeEmpty;
import static org.fest.assertions.error.ShouldBeEqualIgnoringCase.shouldBeEqual;
import static org.fest.assertions.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.fest.assertions.error.ShouldContainString.shouldContain;
import static org.fest.assertions.error.ShouldContainString.shouldContainIgnoringCase;
import static org.fest.assertions.error.ShouldContainStringOnlyOnce.shouldContainOnlyOnce;
import static org.fest.assertions.error.ShouldEndWith.shouldEndWith;
import static org.fest.assertions.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.fest.assertions.error.ShouldHaveSize.shouldHaveSize;
import static org.fest.assertions.error.ShouldMatchPattern.shouldMatch;
import static org.fest.assertions.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.fest.assertions.error.ShouldNotContainString.shouldNotContain;
import static org.fest.assertions.error.ShouldNotMatchPattern.shouldNotMatch;
import static org.fest.assertions.error.ShouldStartWith.shouldStartWith;
import static org.fest.assertions.internal.CommonErrors.arrayOfValuesToLookForIsNull;
import static org.fest.util.Iterables.sizeOf;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link String}</code>s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Nicolas François
 */
public class Strings {

  private static final Strings INSTANCE = new Strings();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * 
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Strings instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Strings() {
    this(StandardComparisonStrategy.instance());
  }

  private final ComparisonStrategy comparisonStrategy;

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
   * Asserts that the given {@code String} is {@code null} or empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @throws AssertionError if the given {@code String} is not {@code null} *and* it is not empty.
   */
  public void assertNullOrEmpty(AssertionInfo info, String actual) {
    if (actual == null || !hasContents(actual)) {
      return;
    }
    throw failures.failure(info, shouldBeNullOrEmpty(actual));
  }

  /**
   * Asserts that the given {@code String} is empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the given {@code String} is not empty.
   */
  public void assertEmpty(AssertionInfo info, String actual) {
    assertNotNull(info, actual);
    if (!hasContents(actual)) {
      return;
    }
    throw failures.failure(info, shouldBeEmpty(actual));
  }

  /**
   * Asserts that the given {@code String} is not empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the given {@code String} is empty.
   */
  public void assertNotEmpty(AssertionInfo info, String actual) {
    assertNotNull(info, actual);
    if (hasContents(actual)) {
      return;
    }
    throw failures.failure(info, shouldNotBeEmpty());
  }

  private static boolean hasContents(String s) {
    return s.length() > 0;
  }

  /**
   * Asserts that the size of the given {@code String} is equal to the expected one.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param expectedSize the expected size of {@code actual}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the size of the given {@code String} is different than the expected one.
   */
  public void assertHasSize(AssertionInfo info, String actual, int expectedSize) {
    assertNotNull(info, actual);
    int sizeOfActual = actual.length();
    if (sizeOfActual == expectedSize) {
      return;
    }
    throw failures.failure(info, shouldHaveSize(actual, sizeOfActual, expectedSize));
  }

  /**
   * Asserts that the number of entries in the given {@code String} has the same size as the other {@code Iterable}.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param other the group to compare
   * @throws AssertionError if the given {@code String}. is {@code null}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code String} does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, String actual, Iterable<?> other) {
    assertNotNull(info, actual);
    if (other == null) {
      throw new NullPointerException("The iterable to look for should not be null");
    }
    int sizeOfActual = actual.length();
    int sizeOfOther = sizeOf(other);
    if (sizeOfActual == sizeOfOther) {
      return;
    }
    throw failures.failure(info, shouldHaveSameSizeAs(actual, sizeOfActual, sizeOfOther));
  }

  /**
   * Asserts that the number of entries in the given {@code String} has the same size as the other array.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param other the group to compare
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of entries in the given {@code String} does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, String actual, Object[] other) {
    assertNotNull(info, actual);
    if (other == null) {
      throw arrayOfValuesToLookForIsNull();
    }
    int sizeOfActual = actual.length();
    int sizeOfOther = Array.getLength(other);
    if (sizeOfActual == sizeOfOther) {
      return;
    }
    throw failures.failure(info, shouldHaveSameSizeAs(actual, sizeOfActual, sizeOfOther));
  }

  /**
   * Verifies that the given {@code String} contains the given sequence.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param sequence the sequence to search for.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not contain the given sequence.
   */
  public void assertContains(AssertionInfo info, String actual, String sequence) {
    checkSequenceIsNotNull(sequence);
    assertNotNull(info, actual);
    if (stringContains(actual, sequence)) {
      return;
    }
    throw failures.failure(info, shouldContain(actual, sequence, comparisonStrategy));
  }

  /**
   * Delegates to {@link ComparisonStrategy#stringContains(String, String)}
   */
  private boolean stringContains(String actual, String sequence) {
    return comparisonStrategy.stringContains(actual, sequence);
  }

  /**
   * Verifies that the given {@code String} contains the given sequence, ignoring case considerations.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param sequence the sequence to search for.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not contain the given sequence.
   */
  public void assertContainsIgnoringCase(AssertionInfo info, String actual, String sequence) {
    checkSequenceIsNotNull(sequence);
    assertNotNull(info, actual);
    if (actual.toLowerCase().contains(sequence.toLowerCase())) {
      return;
    }
    throw failures.failure(info, shouldContainIgnoringCase(actual, sequence));
  }

  /**
   * Verifies that the given {@code String} does not contain the given sequence.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param sequence the sequence to search for.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} contains the given sequence.
   */
  public void assertDoesNotContain(AssertionInfo info, String actual, String sequence) {
    checkSequenceIsNotNull(sequence);
    assertNotNull(info, actual);
    if (!stringContains(actual, sequence)) {
      return;
    }
    throw failures.failure(info, shouldNotContain(actual, sequence, comparisonStrategy));
  }

  private void checkSequenceIsNotNull(String sequence) {
    if (sequence == null) {
      throw new NullPointerException("The sequence to look for should not be null");
    }
  }

  /**
   * Verifies that two {@code String}s are equal, ignoring case considerations.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param expected the expected {@code String}.
   * @throws AssertionError if the given {@code String}s are not equal.
   */
  public void assertEqualsIgnoringCase(AssertionInfo info, String actual, String expected) {
    if (areEqualIgnoringCase(actual, expected)) {
      return;
    }
    throw failures.failure(info, shouldBeEqual(actual, expected));
  }

  private boolean areEqualIgnoringCase(String actual, String expected) {
    if (actual == null) {
      return expected == null;
    }
    return actual.equalsIgnoreCase(expected);
  }

  /**
   * Verifies that actual {@code String}s contains only once the given sequence.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param sequence the given {@code String}.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not contains <b>only once</b> the given {@code String}.
   */
  public void assertContainsOnlyOnce(AssertionInfo info, String actual, String sequence) {
    checkSequenceIsNotNull(sequence);
    assertNotNull(info, actual);
    int sequenceOccurencesInActual = countOccurences(sequence, actual);
    if (sequenceOccurencesInActual == 1)
      return;
    throw failures.failure(info,
        shouldContainOnlyOnce(actual, sequence, sequenceOccurencesInActual, comparisonStrategy));
  }

  /**
   * Count occurences of sequenceToSearch in actual {@link String}.
   * 
   * @param sequenceToSearch the sequence to search in in actual {@link String}.
   * @param actual the {@link String} to search occurences in.
   * @return the number of occurences of sequenceToSearch in actual {@link String}.
   */
  private int countOccurences(String sequenceToSearch, String actual) {
    int occurences = 0;
    for (int i = 0; i <= (actual.length() - sequenceToSearch.length()); i++) {
      if (comparisonStrategy.areEqual(actual.substring(i, i + sequenceToSearch.length()), sequenceToSearch)) {
        occurences++;
      }
    }
    return occurences;
  }

  /**
   * Verifies that the given {@code String} starts with the given prefix.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param prefix the given prefix.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not start with the given prefix.
   */
  public void assertStartsWith(AssertionInfo info, String actual, String prefix) {
    if (prefix == null) {
      throw new NullPointerException("The given prefix should not be null");
    }
    assertNotNull(info, actual);
    if (comparisonStrategy.stringStartsWith(actual, prefix)) {
      return;
    }
    throw failures.failure(info, shouldStartWith(actual, prefix, comparisonStrategy));
  }

  /**
   * Verifies that the given {@code String} ends with the given suffix.
   * 
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param suffix the given suffix.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not end with the given suffix.
   */
  public void assertEndsWith(AssertionInfo info, String actual, String suffix) {
    if (suffix == null) {
      throw new NullPointerException("The given suffix should not be null");
    }
    assertNotNull(info, actual);
    if (comparisonStrategy.stringEndsWith(actual, suffix)) {
      return;
    }
    throw failures.failure(info, shouldEndWith(actual, suffix, comparisonStrategy));
  }

  /**
   * Verifies that the given {@code String} matches the given regular expression.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param regex the regular expression to which the actual {@code String} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not match the given regular expression.
   */
  public void assertMatches(AssertionInfo info, String actual, String regex) {
    checkRegexIsNotNull(regex);
    assertNotNull(info, actual);
    if (Pattern.matches(regex, actual)) {
      return;
    }
    throw failures.failure(info, shouldMatch(actual, regex));
  }

  /**
   * Verifies that the given {@code String} does not match the given regular expression.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param regex the regular expression to which the actual {@code String} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code String} matches the given regular expression.
   */
  public void assertDoesNotMatch(AssertionInfo info, String actual, String regex) {
    checkRegexIsNotNull(regex);
    if (actual == null || !Pattern.matches(regex, actual)) {
      return;
    }
    throw failures.failure(info, shouldNotMatch(actual, regex));
  }

  private void checkRegexIsNotNull(String regex) {
    if (regex == null) {
      throw patternToMatchIsNull();
    }
  }

  /**
   * Verifies that the given {@code String} matches the given regular expression.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param pattern the regular expression to which the actual {@code String} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the given {@code String} does not match the given regular expression.
   */
  public void assertMatches(AssertionInfo info, String actual, Pattern pattern) {
    checkIsNotNull(pattern);
    assertNotNull(info, actual);
    if (pattern.matcher(actual).matches()) {
      return;
    }
    throw failures.failure(info, shouldMatch(actual, pattern.pattern()));
  }

  /**
   * Verifies that the given {@code String} does not match the given regular expression.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param pattern the regular expression to which the actual {@code String} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the given {@code String} matches the given regular expression.
   */
  public void assertDoesNotMatch(AssertionInfo info, String actual, Pattern pattern) {
    checkIsNotNull(pattern);
    if (actual == null || !pattern.matcher(actual).matches()) {
      return;
    }
    throw failures.failure(info, shouldNotMatch(actual, pattern.pattern()));
  }

  private void checkIsNotNull(Pattern pattern) {
    if (pattern == null) {
      throw patternToMatchIsNull();
    }
  }

  private NullPointerException patternToMatchIsNull() {
    return new NullPointerException("The regular expression pattern to match should not be null");
  }

  private void assertNotNull(AssertionInfo info, String actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
