/*
 * Created on Dec 22, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.fest.assertions.core.EnumerableAssert;
import org.fest.assertions.internal.*;
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
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas Francois
 */
public class StringAssert extends AbstractAssert<StringAssert, String> implements EnumerableAssert<StringAssert, String> {

  @VisibleForTesting
  Strings strings = Strings.instance();

  protected StringAssert(String actual) {
    super(actual, StringAssert.class);
  }

  /** {@inheritDoc} */
  public void isNullOrEmpty() {
    strings.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public void isEmpty() {
    strings.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public StringAssert isNotEmpty() {
    strings.assertNotEmpty(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public StringAssert hasSize(int expected) {
    strings.assertHasSize(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public StringAssert hasSameSizeAs(Object[] other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public StringAssert hasSameSizeAs(Iterable<?> other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return this;
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
   * Verifies that the actual {@code String} contains only once the given sequence.
   * 
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code String} does not contain the given one, or contain it multiple times.
   */
  public StringAssert containsOnlyOnce(String sequence) {
    strings.assertContainsOnlyOnce(info, actual, sequence);
    return this;
  }

  /**
   * Verifies that the actual {@code String} contains the given sequence.
   * 
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
    strings.assertContainsIgnoringCase(info, actual, sequence);
    return this;
  }

  /**
   * Verifies that the actual {@code String} does not contain the given sequence.
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} contains the given one.
   */
  public StringAssert doesNotContain(String sequence) {
    strings.assertDoesNotContain(info, actual, sequence);
    return this;
  }

  /**
   * Verifies that the actual {@code String} starts with the given prefix.
   * @param prefix the given prefix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given prefix is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not start with the given prefix.
   */
  public StringAssert startsWith(String prefix) {
    strings.assertStartsWith(info, actual, prefix);
    return this;
  }

  /**
   * Verifies that the actual {@code String} ends with the given suffix.
   * @param suffix the given suffix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given suffix is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not end with the given suffix.
   */
  public StringAssert endsWith(String suffix) {
    strings.assertEndsWith(info, actual, suffix);
    return this;
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
  
  /** {@inheritDoc} */
  public StringAssert usingElementComparator(Comparator<? super String> customComparator) {
    // TODO maybe use Comparator<? super Character>
    throw new UnsupportedOperationException("custom element Comparator is not supported for String comparison");
  }

  /** {@inheritDoc} */
  public StringAssert usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for String comparison");
  }

  @Override
  public StringAssert usingComparator(Comparator<? super String> customComparator) {
    super.usingComparator(customComparator);
    this.strings = new Strings(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public StringAssert usingDefaultComparator() {
    super.usingDefaultComparator();
    this.strings = Strings.instance();
    return myself;
  }
}
