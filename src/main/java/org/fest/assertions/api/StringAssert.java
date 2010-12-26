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
package org.fest.assertions.api;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.fest.assertions.core.EnumerableAssert;
import org.fest.assertions.internal.Strings;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for {@code String}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(String)}</code>.
 * </p>
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Alex Ruiz
 */
public class StringAssert extends AbstractAssert<StringAssert, String> implements EnumerableAssert<StringAssert> {

  @VisibleForTesting Strings strings = Strings.instance();

  protected StringAssert(String actual) {
    super(actual, StringAssert.class);
  }

  /** {@inheritDoc} */
  public void isNullOrEmpty() {
    // TODO Auto-generated method stub

  }

  /** {@inheritDoc} */
  public void isEmpty() {
    // TODO Auto-generated method stub

  }

  /** {@inheritDoc} */
  public StringAssert isNotEmpty() {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public StringAssert hasSize(int expected) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Verifies that the actual {@code String} is equal to the given one, ignoring case considerations.
   * @param expected the given {@code String} to compare the actual {@code String} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code String} is not equal to the given one.
   */
  public StringAssert isEqualToIgnoringCase(String expected) {
    strings.assertEqualsIgnoringCase(info, actual, expected);
    return this;
  }

  /**
   * Verifies that the actual {@code String} contains the given sequence.
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not contain the given one.
   */
  public StringAssert contains(String sequence) {
    strings.assertContains(info, actual, sequence);
    return this;
  }

  /**
   * Verifies that the actual {@code String} contains the given sequence, ignoring case considerations.
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not contain the given one.
   */
  public StringAssert containsIgnoringCase(String sequence) {
    // TODO implement
    return null;
  }

  public StringAssert doesNotContain(String sequence) {
    // TODO implement
    return null;
  }

  public StringAssert startsWith(String prefix) {
    // TODO implement
    return null;
  }

  public StringAssert endsWith(String suffix) {
    // TODO implement
    return null;
  }

  /**
   * Verifies that the actual {@code String} matches the given regular expression.
   * @param regex the regular expression to which the actual {@code String} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not match the given regular expression.
   */
  public StringAssert matches(String regex) {
    strings.assertMatches(info, actual, regex);
    return this;
  }

  /**
   * Verifies that the actual {@code String} does not match the given regular expression.
   * @param regex the regular expression to which the actual {@code String} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} matches the given regular expression.
   */
  public StringAssert doesNotMatch(String regex) {
    strings.assertDoesNotMatch(info, actual, regex);
    return this;
  }

  /**
   * Verifies that the actual {@code String} matches the given regular expression.
   * @param pattern the regular expression to which the actual {@code String} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not match the given regular expression.
   */
  public StringAssert matches(Pattern pattern) {
    strings.assertMatches(info, actual, pattern);
    return this;
  }

  /**
   * Verifies that the actual {@code String} does not match the given regular expression.
   * @param pattern the regular expression to which the actual {@code String} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code String} does not match the given regular expression.
   */
  public StringAssert doesNotMatch(Pattern pattern) {
    strings.assertDoesNotMatch(info, actual, pattern);
    return this;
  }
}
