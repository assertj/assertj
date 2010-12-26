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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ContainsString.contains;
import static org.fest.assertions.error.DoesNotContainString.*;
import static org.fest.assertions.error.DoesNotMatchPattern.doesNotMatch;
import static org.fest.assertions.error.DoesNotStartWith.doesNotStartWith;
import static org.fest.assertions.error.IsNotEqualIgnoringCase.isNotEqual;
import static org.fest.assertions.error.MatchesPattern.matches;

import java.util.regex.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link String}</code>s.
 *
 * @author Alex Ruiz
 */
public class Strings {

  private static final Strings INSTANCE = new Strings();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Strings instance() {
    return INSTANCE;
  }

  private Strings() {
    this(Failures.instance());
  }

  private final Failures failures;

  @VisibleForTesting Strings(Failures failures) {
    this.failures = failures;
  }

  /**
   * Verifies that the given {@code String} contains the given sequence.
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param sequence the sequence to search for.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not contain the given sequence.
   */
  public void assertContains(AssertionInfo info, String actual, String sequence) {
    validateSequenceNotNull(sequence);
    assertNotNull(info, actual);
    if (actual.contains(sequence)) return;
    throw failures.failure(info, doesNotContain(actual, sequence));
  }

  /**
   * Verifies that the given {@code String} contains the given sequence, ignoring case considerations.
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param sequence the sequence to search for.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not contain the given sequence.
   */
  public void assertContainsIgnoringCase(AssertionInfo info, String actual, String sequence) {
    validateSequenceNotNull(sequence);
    assertNotNull(info, actual);
    if (actual.toLowerCase().contains(sequence.toLowerCase())) return;
    throw failures.failure(info, doesNotContainIgnoringCase(actual, sequence));
  }

  /**
   * Verifies that the given {@code String} does not contain the given sequence.
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param sequence the sequence to search for.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} contains the given sequence.
   */
  public void assertDoesNotContain(AssertionInfo info, String actual, String sequence) {
    validateSequenceNotNull(sequence);
    assertNotNull(info, actual);
    if (!actual.contains(sequence)) return;
    throw failures.failure(info, contains(actual, sequence));
  }

  private void validateSequenceNotNull(String sequence) {
    if (sequence == null) throw new NullPointerException("The sequence to look for should not be null");
  }

  /**
   * Verifies that two {@code String}s are equal, ignoring case considerations.
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param expected the expected {@code String}.
   * @throws AssertionError if the given {@code String}s are not equal.
   */
  public void assertEqualsIgnoringCase(AssertionInfo info, String actual, String expected) {
    if (areEqualIgnoringCase(actual, expected)) return;
    throw failures.failure(info, isNotEqual(actual, expected));
  }

  private boolean areEqualIgnoringCase(String actual, String expected) {
    if (actual == null) return expected == null;
    return actual.equalsIgnoreCase(expected);
  }

  /**
   * Verifies that the given {@code String} starts with the given prefix.
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param prefix the given prefix.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not start with the given prefix.
   */
  public void assertStartsWith(AssertionInfo info, String actual, String prefix) {
    if (prefix == null) throw new NullPointerException("The given prefix should not be null");
    assertNotNull(info, actual);
    if (actual.startsWith(prefix)) return;
    throw failures.failure(info, doesNotStartWith(actual, prefix));
  }

  /**
   * Verifies that the given {@code String} ends with the given suffix.
   * @param info contains information about the assertion.
   * @param actual the actual {@code String}.
   * @param suffix the given suffix.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not end with the given suffix.
   */
  public void assertEndsWith(AssertionInfo info, String actual, String suffix) {
    if (suffix == null) throw new NullPointerException("The given suffix should not be null");
    assertNotNull(info, actual);
    if (actual.endsWith(suffix)) return;
    throw failures.failure(info, doesNotStartWith(actual, suffix));
  }

  /**
   * Verifies that the given {@code String} matches the given regular expression.
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param regex the regular expression to which the actual {@code String} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not match the given regular expression.
   */
  public void assertMatches(AssertionInfo info, String actual, String regex) {
    validateRegexNotNull(regex);
    assertNotNull(info, actual);
    if (Pattern.matches(regex, actual)) return;
    throw failures.failure(info, doesNotMatch(actual, regex));
  }

  /**
   * Verifies that the given {@code String} does not match the given regular expression.
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param regex the regular expression to which the actual {@code String} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code String} matches the given regular expression.
   */
  public void assertDoesNotMatch(AssertionInfo info, String actual, String regex) {
    validateRegexNotNull(regex);
    if (actual == null || !Pattern.matches(regex, actual)) return;
    throw failures.failure(info, matches(actual, regex));
  }

  private void validateRegexNotNull(String regex) {
    if (regex == null) throw patternToMatchIsNull();
  }

  /**
   * Verifies that the given {@code String} matches the given regular expression.
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param pattern the regular expression to which the actual {@code String} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the given {@code String} is {@code null}.
   * @throws AssertionError if the given {@code String} does not match the given regular expression.
   */
  public void assertMatches(AssertionInfo info, String actual, Pattern pattern) {
    validateNotNull(pattern);
    assertNotNull(info, actual);
    if (pattern.matcher(actual).matches()) return;
    throw failures.failure(info, doesNotMatch(actual, pattern.pattern()));
  }

  /**
   * Verifies that the given {@code String} does not match the given regular expression.
   * @param info contains information about the assertion.
   * @param actual the given {@code String}.
   * @param pattern the regular expression to which the actual {@code String} is to be matched.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the given {@code String} matches the given regular expression.
   */
  public void assertDoesNotMatch(AssertionInfo info, String actual, Pattern pattern) {
    validateNotNull(pattern);
    if (actual == null || !pattern.matcher(actual).matches()) return;
    throw failures.failure(info, matches(actual, pattern.pattern()));
  }

  private void validateNotNull(Pattern pattern) {
    if (pattern == null) throw patternToMatchIsNull();
  }

  private NullPointerException patternToMatchIsNull() {
    return new NullPointerException("The regular expression pattern to match should not be null");
  }

  private void assertNotNull(AssertionInfo info, String actual) {
    Objects.instance().assertNotNull(info, actual);
  }

}
