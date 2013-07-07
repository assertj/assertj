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
 * Base class for all implementations of assertions for {@code CharSequence}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas Francois
 */
public abstract class AbstractCharSequenceAssert<S extends AbstractCharSequenceAssert<S, A>, A extends CharSequence>
    extends AbstractAssert<S, A> implements EnumerableAssert<S, Character> {

  @VisibleForTesting
  Strings strings = Strings.instance();

  protected AbstractCharSequenceAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public void isNullOrEmpty() {
    strings.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public void isEmpty() {
    strings.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public S isNotEmpty() {
    strings.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Gandalf the grey&quot;).hasSize(16);
   * 
   * // assertion will fail
   * assertThat(&quot;Gandalf the grey&quot;).hasSize(1);
   * </pre>
   * 
   * </p>
   */
  @Override
  public S hasSize(int expected) {
    strings.assertHasSize(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasSameSizeAs(Object[] other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasSameSizeAs(Iterable<?> other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the given one, ignoring case considerations.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Gandalf the grey&quot;).isEqualToIgnoringCase(&quot;GaNdAlF tHe GREY&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).isEqualToIgnoringCase(&quot;Gandalf the grey&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Gandalf the grey&quot;).isEqualToIgnoringCase(&quot;Gandalf the white&quot;);
   * </pre>
   * 
   * </p>
   * 
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not equal to the given one.
   */
  public S isEqualToIgnoringCase(CharSequence expected) {
    strings.assertEqualsIgnoringCase(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains only once the given sequence.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).containsOnlyOnce(&quot;do&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).containsOnlyOnce(&quot;o&quot;);
   * </pre>
   * 
   * </p>
   * 
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} does not contain the given one, or contain it multiple
   *           times.
   */
  public S containsOnlyOnce(CharSequence sequence) {
    strings.assertContainsOnlyOnce(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the given strings.
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
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given strings.
   */
  public S contains(CharSequence... values) {
    strings.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the given strings <b>in the given order</b>.
   * <p>
   * Example :
   * 
   * <pre>
   * String book = &quot;{ 'title':'A Game of Thrones', 'author':'George Martin'}&quot;;
   * 
   * // this assertion succeeds ...
   * assertThat(book).containsSequence(&quot;{&quot;, &quot;title&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;);
   * 
   * // ... but this one fails as &quot;author&quot; must come after &quot;A Game of Thrones&quot;
   * assertThat(book).containsSequence(&quot;{&quot;, &quot;author&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;);
   * </pre>
   * 
   * @param values the Strings to look for in order.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given strings <b>in the given
   *           order</b>.
   */
  public S containsSequence(CharSequence... values) {
    strings.assertContainsSequence(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains the given sequence, ignoring case considerations.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Gandalf the grey&quot;).containsIgnoringCase(&quot;gandalf&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Gandalf the grey&quot;).containsIgnoringCase(&quot;white&quot;);
   * </pre>
   * 
   * </p>
   * 
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain the given one.
   */
  public S containsIgnoringCase(CharSequence sequence) {
    strings.assertContainsIgnoringCase(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain the given sequence.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;fro&quot;);
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;gandalf&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;Fro&quot;);
   * </pre>
   * 
   * </p>
   * 
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} contains the given one.
   */
  public S doesNotContain(CharSequence sequence) {
    strings.assertDoesNotContain(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} starts with the given prefix.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).startsWith(&quot;Fro&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).startsWith(&quot;Gandalf&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).startsWith(&quot;fro&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).startsWith(&quot;grey&quot;);
   * </pre>
   * 
   * </p>
   * 
   * @param prefix the given prefix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given prefix is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not start with the given prefix.
   */
  public S startsWith(CharSequence prefix) {
    strings.assertStartsWith(info, actual, prefix);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} ends with the given suffix.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).endsWith(&quot;do&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).endsWith(&quot;Fro&quot;);
   * </pre>
   * 
   * </p>
   * 
   * @param suffix the given suffix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given suffix is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not end with the given suffix.
   */
  public S endsWith(CharSequence suffix) {
    strings.assertEndsWith(info, actual, suffix);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} matches the given regular expression.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).matches(&quot;..o.o&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).matches(&quot;.*d&quot;);
   * </pre>
   * 
   * </p>
   * 
   * @param regex the regular expression to which the actual {@code CharSequence} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not match the given regular expression.
   */
  public S matches(CharSequence regex) {
    strings.assertMatches(info, actual, regex);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not match the given regular expression.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotMatch(&quot;.*d&quot;);
   *
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotMatch(&quot;..o.o&quot;);
   * </pre>
   * 
   * </p>
   * 
   * @param regex the regular expression to which the actual {@code CharSequence} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} matches the given regular expression.
   */
  public S doesNotMatch(CharSequence regex) {
    strings.assertDoesNotMatch(info, actual, regex);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} matches the given regular expression.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).matches(Pattern.compile(&quot;..o.o&quot;));
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).matches(Pattern.compile(&quot;.*d&quot;));
   * </pre>
   * 
   * </p>
   * 
   * @param pattern the regular expression to which the actual {@code CharSequence} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not match the given regular expression.
   */
  public S matches(Pattern pattern) {
    strings.assertMatches(info, actual, pattern);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not match the given regular expression.
   * <p>
   * Example :
   * 
   * <pre>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotMatch(Pattern.compile(&quot;.*d&quot;));
   *
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotMatch(Pattern.compile(&quot;..o.o&quot;));
   * </pre>
   * 
   * </p>
   * 
   * @param pattern the regular expression to which the actual {@code CharSequence} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not match the given regular expression.
   */
  public S doesNotMatch(Pattern pattern) {
    strings.assertDoesNotMatch(info, actual, pattern);
    return myself;
  }

  /**
   * Do not use this method.
   * 
   * @deprecated Custom element Comparator is not supported for CharSequence comparison.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  public final S usingElementComparator(Comparator<? super Character> customComparator) {
    throw new UnsupportedOperationException("custom element Comparator is not supported for CharSequence comparison");
  }

  /**
   * Do not use this method.
   * 
   * @deprecated Custom element Comparator is not supported for CharSequence comparison.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  public final S usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for CharSequence comparison");
  }

  @Override
  public S usingComparator(Comparator<? super A> customComparator) {
    super.usingComparator(customComparator);
    this.strings = new Strings(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public S usingDefaultComparator() {
    super.usingDefaultComparator();
    this.strings = Strings.instance();
    return myself;
  }
}
