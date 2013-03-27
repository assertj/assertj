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
package org.assertj.core.api;

import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.core.util.VisibleForTesting;

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
public class StringAssert<S extends StringAssert<?,?>, A extends String> extends AbstractAssert<S,A> implements
    EnumerableAssert<S,A> {

  @VisibleForTesting
  Strings strings = Strings.instance();

  protected StringAssert(A actual) {
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
  public S isNotEmpty() {
    strings.assertNotEmpty(info, actual);
    return (S) this;
  }

  /** {@inheritDoc} */
  public S hasSize(int expected) {
    strings.assertHasSize(info, actual, expected);
    return (S) this;
  }

  /** {@inheritDoc} */
  public S hasSameSizeAs(Object[] other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return (S) this;
  }

  /** {@inheritDoc} */
  public S hasSameSizeAs(Iterable<?> other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} is equal to the given one, ignoring case considerations.
   *
   * @param expected the given {@code String} to compare the actual {@code String} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code String} is not equal to the given one.
   */
  public S isEqualToIgnoringCase(String expected) {
    strings.assertEqualsIgnoringCase(info, actual, expected);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} contains only once the given sequence.
   *
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code String} does not contain the given one, or contain it multiple times.
   */
  public S containsOnlyOnce(String sequence) {
    strings.assertContainsOnlyOnce(info, actual, sequence);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} contains all the given strings.
   * <p>
   * You can use one or several strings as in the example below
   *
   * <pre>
   * assertThat(&quot;Gandalf the grey&quot;).contains(&quot;alf&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).contains(&quot;alf&quot;, &quot;grey&quot;);
   * </pre>
   *
   * @param values the Strings to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not contain all the given strings.
   */
  public S contains(String... values) {
    strings.assertContains(info, actual, values);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} contains all the given strings <b>in the given order</b>.
   * <p>
   * Example :
   *
   * <pre>
   * String book = &quot;{ 'title':'A Game of Thrones', 'author':'George Martin'}&quot;;
   *
   * // this assertion succeeds ...
   * assertThat(book).containsSequence(&quot;{&quot;, &quot;title&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;);
   *
   * // ... but this one fails as "author" must come after "A Game of Thrones"
   * assertThat(book).containsSequence(&quot;{&quot;, &quot;author&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;);
   * </pre>
   *
   * @param values the Strings to look for in order.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not contain all the given strings <b>in the given order</b>.
   */
  public S containsSequence(String... values) {
    strings.assertContainsSequence(info, actual, values);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} contains the given sequence, ignoring case considerations.
   *
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not contain the given one.
   */
  public S containsIgnoringCase(String sequence) {
    strings.assertContainsIgnoringCase(info, actual, sequence);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} does not contain the given sequence.
   *
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} contains the given one.
   */
  public S doesNotContain(String sequence) {
    strings.assertDoesNotContain(info, actual, sequence);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} starts with the given prefix.
   *
   * @param prefix the given prefix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given prefix is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not start with the given prefix.
   */
  public S startsWith(String prefix) {
    strings.assertStartsWith(info, actual, prefix);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} ends with the given suffix.
   *
   * @param suffix the given suffix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given suffix is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not end with the given suffix.
   */
  public S endsWith(String suffix) {
    strings.assertEndsWith(info, actual, suffix);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} matches the given regular expression.
   *
   * @param regex the regular expression to which the actual {@code String} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not match the given regular expression.
   */
  public S matches(String regex) {
    strings.assertMatches(info, actual, regex);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} does not match the given regular expression.
   *
   * @param regex the regular expression to which the actual {@code String} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} matches the given regular expression.
   */
  public S doesNotMatch(String regex) {
    strings.assertDoesNotMatch(info, actual, regex);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} matches the given regular expression.
   *
   * @param pattern the regular expression to which the actual {@code String} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code String} is {@code null}.
   * @throws AssertionError if the actual {@code String} does not match the given regular expression.
   */
  public S matches(Pattern pattern) {
    strings.assertMatches(info, actual, pattern);
    return (S) this;
  }

  /**
   * Verifies that the actual {@code String} does not match the given regular expression.
   *
   * @param pattern the regular expression to which the actual {@code String} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code String} does not match the given regular expression.
   */
  public S doesNotMatch(Pattern pattern) {
    strings.assertDoesNotMatch(info, actual, pattern);
    return (S) this;
  }

  /** {@inheritDoc} */
  public S usingElementComparator(Comparator<? super A> customComparator) {
    // TODO maybe use Comparator<? super Character>
    throw new UnsupportedOperationException("custom element Comparator is not supported for String comparison");
  }

  /** {@inheritDoc} */
  public S usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for String comparison");
  }

  @Override
  public S usingComparator(Comparator<? super A> customComparator) {
    super.usingComparator(customComparator);
    this.strings = new Strings(new ComparatorBasedComparisonStrategy(customComparator));
    return (S) this;
  }

  @Override
  public S usingDefaultComparator() {
    super.usingDefaultComparator();
    this.strings = Strings.instance();
    return (S) this;
  }
}
