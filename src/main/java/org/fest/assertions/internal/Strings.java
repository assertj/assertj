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

import static org.fest.assertions.error.DoesNotMatchPattern.doesNotMatch;
import static org.fest.assertions.internal.CommonErrors.patternToMatchIsNull;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
    isNotNull(regex);
    assertNotNull(info, actual);
    if (Pattern.matches(regex, actual)) return;
    throw failures.failure(info, doesNotMatch(actual, regex));
  }

  private void isNotNull(String regex) {
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
    isNotNull(pattern);
    assertNotNull(info, actual);
    if (pattern.matcher(actual).matches()) return;
    throw failures.failure(info, doesNotMatch(actual, pattern.pattern()));
  }

  private void isNotNull(Pattern pattern) {
    if (pattern == null) throw patternToMatchIsNull();
  }

  private void assertNotNull(AssertionInfo info, String actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  /**
   * @param info
   * @param actual
   * @param pattern
   */
  public void assertDoesNotMatch(AssertionInfo info, String actual, Pattern pattern) {
    // TODO Auto-generated method stub

  }
}
