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

  /**
   * Verifies that the actual {@code CharSequence} is empty, i.e., it has a length of 0, or is {@code null}.
   * <p>
   * If you do not want to accept a {@code null} value, use
   * {@link org.assertj.core.api.AbstractCharSequenceAssert#isEmpty()} instead.
   * <p>
   * Both of these assertions will succeed:
   * <pre>
   * String emptyString = &quot;&quot;
   * assertThat(emptyString).isNullOrEmpty();
   *
   * String nullString = null;
   * assertThat(nullString).isNullOrEmpty();
   * </pre>
   *
   * @throws AssertionError if the actual {@code CharSequence} has a non-zero length.
   */
  @Override
  public void isNullOrEmpty() {
    strings.assertNullOrEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code CharSequence} is empty, i.e., it has a length of 0 and is not {@code null}.
   * <p>
   * If you want to accept a {@code null} value as well as a 0 length, use
   * {@link org.assertj.core.api.AbstractCharSequenceAssert#isNullOrEmpty()} instead.
   * <p>
   * This assertion will succeed:
   * <pre>
   * String emptyString = &quot;&quot;
   * assertThat(emptyString).isEmpty();
   * </pre>
   * Whereas this assertion will fail:
   * <pre>
   * String nullString = null;
   * assertThat(nullString).isEmpty();
   * </pre>
   *
   * @throws AssertionError if the actual {@code CharSequence} has a non-zero length or is null.
   */
  @Override
  public void isEmpty() {
    strings.assertEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code CharSequence} is not empty, i.e., is not {@code null} and has a length of 1 or more.
   * <p>
   * This assertion will succeed:
   * <pre>
   * String bookName = &quot;A Game of Thrones&quot;
   * assertThat(bookName).isNotEmpty();
   * </pre>
   * Whereas this assertion will fail:
   * <pre>
   * String emptyString = &quot;&quot;
   * assertThat(emptyString).isNotEmpty();
   * </pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is empty (has a length of 0).
   */
  @Override
  public S isNotEmpty() {
    strings.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has the expected length using the {@code length()} method.
   * <p>
   * This assertion will succeed:
   * <pre>
   * String bookName = &quot;A Game of Thrones&quot;
   * assertThat(bookName).hasSize(17);
   * </pre>
   * Whereas this assertion will fail:
   * <pre>
   * String bookName = &quot;A Clash of Kings&quot;
   * assertThat(bookName).hasSize(4);
   * </pre>
   *
   * @param expected the expected length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is not equal to the expected length.
   */
  @Override
  public S hasSize(int expected) {
    strings.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length that's the same as the number of elements in the given
   * array.
   *
   * @param other the given array to be used for size comparison.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} has a length that's different from the number of elements in the array.
   * @throws NullPointerException if the given array is {@code null}.
   */
  @Override
  public S hasSameSizeAs(Object[] other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length that's the same as the number of elements in the given
   * Iterable.
   *
   * @param other the given {@code Iterable} to be used for size comparison.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} has a length that's different from the number of elements in the {@code Iterable}.
   * @throws NullPointerException if the given {@code Iterable} is {@code null}.
   */
  @Override
  public S hasSameSizeAs(Iterable<?> other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the given one, ignoring case considerations.
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
   * Verifies that the actual {@code CharSequence} contains the given sequence <b>only once</b>.
   *
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} either does not contain the given one at all, or contains it more than once.
   */
  public S containsOnlyOnce(CharSequence sequence) {
    strings.assertContainsOnlyOnce(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the given strings.
   * <p>
   * You can use one or several strings as in this example:
   *
   * <pre>
   * assertThat(&quot;Gandalf the grey&quot;).contains(&quot;alf&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).contains(&quot;alf&quot;, &quot;grey&quot;);
   * </pre>
   *
   * @param values the Strings to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
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
   * Example:
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
   * @param values the Strings to look for, in order.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given strings <b>in the given order</b>.
   */
  public S containsSequence(CharSequence... values) {
    strings.assertContainsSequence(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains the given sequence, ignoring case considerations.
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
   * Verifies that the actual {@code CharSequence} matches the given regular expression pattern.
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
   * Verifies that the actual {@code CharSequence} does not match the given regular expression pattern.
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
