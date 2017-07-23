/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.contentOf;

import java.io.File;
import java.io.LineNumberReader;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@code CharSequence}s.
 * 
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas Francois
 */
public abstract class AbstractCharSequenceAssert<SELF extends AbstractCharSequenceAssert<SELF, ACTUAL>, ACTUAL extends CharSequence>
    extends AbstractAssert<SELF, ACTUAL> implements EnumerableAssert<SELF, Character> {

  @VisibleForTesting
  Strings strings = Strings.instance();

  public AbstractCharSequenceAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code CharSequence} is empty, i.e., it has a length of 0, or is {@code null}.
   * <p>
   * If you do not want to accept a {@code null} value, use
   * {@link org.assertj.core.api.AbstractCharSequenceAssert#isEmpty()} instead.
   * <p>
   * Both of these assertions will succeed:
   * <pre><code class='java'> String emptyString = &quot;&quot;
   * assertThat(emptyString).isNullOrEmpty();
   * 
   * String nullString = null;
   * assertThat(nullString).isNullOrEmpty();</code></pre>
   * 
   * Whereas these assertions will fail:
   * <pre><code class='java'> assertThat(&quot;a&quot;).isNullOrEmpty();
   * assertThat(&quot;   &quot;).isNullOrEmpty();</code></pre>
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
   * <pre><code class='java'> String emptyString = &quot;&quot;
   * assertThat(emptyString).isEmpty();</code></pre>
   * 
   * Whereas these assertions will fail:
   * <pre><code class='java'> String nullString = null;
   * assertThat(nullString).isEmpty();
   * assertThat(&quot;a&quot;).isEmpty();
   * assertThat(&quot;   &quot;).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code CharSequence} has a non-zero length or is null.
   */
  @Override
  public void isEmpty() {
    strings.assertEmpty(info, actual);
  }

  /**
   * Verifies that the actual {@code CharSequence} is not empty, i.e., is not {@code null} and has a length of 1 or
   * more.
   * <p>
   * This assertion will succeed:
   * <pre><code class='java'> String bookName = &quot;A Game of Thrones&quot;
   * assertThat(bookName).isNotEmpty();</code></pre>
   * 
   * Whereas these assertions will fail:
   * <pre><code class='java'> String emptyString = &quot;&quot;
   * assertThat(emptyString).isNotEmpty();
   * 
   * String nullString = null;
   * assertThat(nullString).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is empty (has a length of 0).
   */
  @Override
  public SELF isNotEmpty() {
    strings.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is blank, i.e. consists of one or more whitespace characters.
   * <p>
   * The whitespace definition used by this assertion follows the latest Unicode standard (which is not the same as Java whitespace definition) 
   * and is based on Guava <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/base/CharMatcher.html#whitespace()"> CharMatcher#whitespace</a>.
   * <p>
   * If you want to stick with the Java whitespace definition, use {@link #isJavaBlank()}.
   * <p>
   * These assertions will succeed:
   * <pre><code class='java'> assertThat(" ").isBlank();
   * assertThat("     ").isBlank();</code></pre>
   * 
   * Whereas these assertions will fail:
   * <pre><code class='java'> assertThat("a").isBlank();
   * assertThat(" b").isBlank();
   * assertThat("").isBlank();
   * String nullString = null;
   * assertThat(nullString).isNotBlank();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not blank.
   * @since 2.6.0 / 3.6.0
   */
  public SELF isBlank() {
    strings.assertBlank(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is not blank, i.e. either is {@code null}, empty or
   * contains at least one non-whitespace characters.
   * <p>
   * It uses the same whitespace definition as the {@link #isBlank()} assertion.
   * <p>
   * These assertions will succeed:
   * <pre><code class='java'> assertThat("a").isNotBlank();
   * assertThat(" b").isNotBlank();
   * assertThat(" c ").isNotBlank();
   * assertThat("").isNotBlank();
   * String nullString = null;
   * assertThat(nullString).isNotBlank();</code></pre>
   * 
   * Whereas these assertions will fail:
   * <pre><code class='java'> assertThat(" ").isNotBlank();
   * assertThat("    ").isNotBlank();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is blank.
   * @since 2.6.0 / 3.6.0
   */
  public SELF isNotBlank() {
    strings.assertNotBlank(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is blank, i.e. consists of one or more whitespace characters
   * (according to {@link Character#isWhitespace(char)}).
   * <p>
   * If you want to use the latest Unicode standard whitespace definition (as in Guava), use {@link #isBlank()}, 
   * see Guava <a href="http://google.github.io/guava/releases/19.0/api/docs/com/google/common/base/CharMatcher.html#whitespace()">explanation</a> for more details.
   * <p>
   * These assertions will succeed:
   * <pre><code class='java'> assertThat(" ").isJavaBlank();
   * assertThat("     ").isJavaBlank();</code></pre>
   * 
   * Whereas these assertions will fail:
   * <pre><code class='java'> assertThat("a").isJavaBlank();
   * assertThat(" b").isJavaBlank();
   * assertThat("").isJavaBlank(); 
   * String nullString = null;
   * assertThat(nullString).isJavaBlank(); </code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not blank.
   * @since 2.6.0 / 3.6.0
   */
  public SELF isJavaBlank() {
    strings.assertJavaBlank(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is not blank, i.e. either is {@code null}, empty or
   * contains at least one non-whitespace character (according to {@link Character#isWhitespace(char)}).
   * <p>
   * It uses the same whitespace definition as the {@link #isJavaBlank()} assertion.
   * <p>
   * These assertions will succeed:
   * <pre><code class='java'> assertThat("a").isNotJavaBlank();
   * assertThat(" b").isNotJavaBlank();
   * assertThat(" c ").isNotJavaBlank();
   * assertThat("").isNotJavaBlank();
   * String nullString = null;
   * assertThat(nullString).isNotJavaBlank();</code></pre>
   * 
   * Whereas these assertions will fail:
   * <pre><code class='java'> assertThat(" ").isNotJavaBlank();
   * assertThat("   ").isNotJavaBlank();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is blank.
   * @since 2.6.0 / 3.6.0
   */
  public SELF isNotJavaBlank() {
    strings.assertNotJavaBlank(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has the expected length using the {@code length()} method.
   * <p>
   * This assertion will succeed:
   * <pre><code class='java'> String bookName = &quot;A Game of Thrones&quot;
   * assertThat(bookName).hasSize(17);</code></pre>
   * 
   * Whereas this assertion will fail:
   * <pre><code class='java'> String bookName = &quot;A Clash of Kings&quot;
   * assertThat(bookName).hasSize(4);</code></pre>
   *
   * @param expected the expected length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is not equal to the expected length.
   */
  @Override
  public SELF hasSize(int expected) {
    strings.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has the expected line count.
   * <p>
   * A line is considered to be <a name="lt">terminated</a> by any one of a line feed ('\n'), a carriage return ('\r'),
   * or a carriage return followed immediately by a linefeed (see {@link LineNumberReader}).
   * <p>
   * This assertion will succeed:
   * <pre><code class='java'> String multiLine = &quot;First line\n&quot; +
   *                    &quot;Last line&quot;;
   * assertThat(multiLine).hasLineCount(2);</code></pre>
   * 
   * Whereas this assertion will fail:
   * <pre><code class='java'> String bookName = &quot;A Clash of Kings&quot;;
   * assertThat(bookName).hasLineCount(3);</code></pre>
   *
   * @param expectedLineCount the expected line count of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual line count is not equal to the expected one.
   */
  public SELF hasLineCount(int expectedLineCount) {
    strings.assertHasLineCount(info, actual, expectedLineCount);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length that's the same as the length of the given
   * {@code CharSequence}.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;C-3PO&quot;).hasSameSizeAs(&quot;R2-D2&quot;);
   * 
   * // assertion will fail as actual and expected sizes differ
   * assertThat(&quot;C-3PO&quot;).hasSameSizeAs(&quot;B1 battle droid&quot;);</code></pre>
   * 
   * @param other the given {@code CharSequence} to be used for size comparison.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} has a length that's different from the length of the
   *           given {@code CharSequence}.
   * @throws NullPointerException if the given {@code CharSequence} is {@code null}.
   */
  public SELF hasSameSizeAs(CharSequence other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length that's the same as the number of elements in the given
   * array.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;12&quot;).hasSameSizeAs(new char[] { 'a', 'b' });
   * 
   * // assertion will fail
   * assertThat(&quot;12&quot;).hasSameSizeAs(new char[] { 'a', 'b', 'c' });</code></pre>
   * 
   * @param other the given array to be used for size comparison.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} has a length that's different from the number of elements
   *           in the array.
   * @throws NullPointerException if the given array is {@code null}.
   */
  @Override
  public SELF hasSameSizeAs(Object other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length that's the same as the number of elements in the given
   * Iterable.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;abc&quot;).hasSameSizeAs(Arrays.asList(1, 2, 3));
   * 
   * // assertion will fail
   * assertThat(&quot;ab&quot;).hasSameSizeAs(Arrays.asList(1, 2, 3));</code></pre>
   *
   * @param other the given {@code Iterable} to be used for size comparison.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} has a length that's different from the number of elements
   *           in the {@code Iterable}.
   * @throws NullPointerException if the given {@code Iterable} is {@code null}.
   */
  @Override
  public SELF hasSameSizeAs(Iterable<?> other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the given one, ignoring case considerations.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Gandalf the grey&quot;).isEqualToIgnoringCase(&quot;GaNdAlF tHe GREY&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).isEqualToIgnoringCase(&quot;Gandalf the grey&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Gandalf the grey&quot;).isEqualToIgnoringCase(&quot;Gandalf the white&quot;);</code></pre>
   * 
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not equal to the given one.
   */
  public SELF isEqualToIgnoringCase(CharSequence expected) {
    strings.assertEqualsIgnoringCase(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is not equal to the given one, ignoring case considerations.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat("Gandalf").isNotEqualToIgnoringCase("Hobbit");
   * assertThat("Gandalf").isNotEqualToIgnoringCase("HOBit");
   * assertThat((String) null).isNotEqualToIgnoringCase("Gandalf");
   * assertThat("Gandalf").isNotEqualToIgnoringCase(null);
   *
   * // assertions will fail
   * assertThat("Gandalf").isNotEqualToIgnoringCase("Gandalf");
   * assertThat("Gandalf").isNotEqualToIgnoringCase("GaNDalf");
   * assertThat((String) null).isNotEqualToIgnoringCase(null);</code></pre>
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not equal to the given one.
   */
  public SELF isNotEqualToIgnoringCase(CharSequence expected) {
    strings.assertNotEqualsIgnoringCase(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains only digits. It fails if it contains non-digit
   * characters or is empty.
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'> assertThat("10").containsOnlyDigits();</code></pre>
   *
   * Whereas this assertion fails:
   * <pre><code class='java'> assertThat("10$").containsOnlyDigits();
   * assertThat("").containsOnlyDigits();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} contains non-digit characters or is {@code null}.
   */
  public SELF containsOnlyDigits() {
    strings.assertContainsOnlyDigits(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains the given sequence <b>only once</b>.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).containsOnlyOnce(&quot;do&quot;);
   * 
   * // assertions will fail
   * assertThat(&quot;Frodo&quot;).containsOnlyOnce(&quot;o&quot;);
   * assertThat(&quot;Frodo&quot;).containsOnlyOnce(&quot;y&quot;);</code></pre>
   * 
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} either does not contain the given one at all, or contains
   *           it more than once.
   */
  public SELF containsOnlyOnce(CharSequence sequence) {
    strings.assertContainsOnlyOnce(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the given values.
   * <p>
   * You can use one or several {@code CharSequence}s as in this example:
   * <pre><code class='java'> assertThat(&quot;Gandalf the grey&quot;).contains(&quot;alf&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).contains(&quot;alf&quot;, &quot;grey&quot;);</code></pre>
   *
   * @param values the Strings to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given strings.
   */
  public SELF contains(CharSequence... values) {
    strings.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the {@code CharSequence}s of the given Iterable.
   * <p>
   * Examples:
   * <pre><code class='java'> assertThat(&quot;Gandalf the grey&quot;).contains(Arrays.asList(&quot;alf&quot;));
   * assertThat(&quot;Gandalf the grey&quot;).contains(Arrays.asList(&quot;alf&quot;, &quot;grey&quot;));</code></pre>
   *
   * @param values the Strings to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given strings.
   */
  public SELF contains(Iterable<? extends CharSequence> values) {
    strings.assertContains(info, actual, IterableUtil.toArray(values, CharSequence.class));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the given values <b>in the given order</b>.
   * <p>
   * Note that <b>unlike</b> {@link IterableAssert#containsSequence(Object...)}, the assertion will succeed when there are values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> String book = &quot;{ 'title':'A Game of Thrones', 'author':'George Martin'}&quot;;
   * 
   * // this assertions succeeds
   * assertThat(book).containsSequence(&quot;'title'&quot;, &quot;:&quot;, &quot;'A Game of Thrones'&quot;);
   * 
   * // this one too even if there are values between the expected sequence (e.g &quot;'title':'&quot;) 
   * assertThat(book).containsSequence(&quot;{&quot;, &quot;A Game of Thrones&quot;, &quot;George Martin&quot;, &quot;}&quot;);
   * 
   * // this one fails as &quot;author&quot; must come after &quot;A Game of Thrones&quot;
   * assertThat(book).containsSequence(&quot;{&quot;, &quot;author&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;);</code></pre>
   *
   * @param values the Strings to look for, in order.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given strings <b>in the given
   *           order</b>.
   */
  public SELF containsSequence(CharSequence... values) {
    strings.assertContainsSequence(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the values of the given Iterable <b>in the Iterable
   * iteration order</b>.
   * <p>
   * Note that <b>unlike</b> {@link IterableAssert#containsSequence(Object...)}, the assertion will succeed when there are values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> String book = &quot;{ 'title':'A Game of Thrones', 'author':'George Martin'}&quot;;
   *
   * // this assertion succeeds
   * assertThat(book).containsSequence(asList(&quot;{&quot;, &quot;title&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;));
   * 
   * // this one too even if there are values between the expected sequence (e.g &quot;'title':'&quot;) 
   * assertThat(book).containsSequence(asList(&quot;{&quot;, &quot;A Game of Thrones&quot;, &quot;George Martin&quot;, &quot;}&quot;));
   *
   * // but this one fails as &quot;author&quot; must come after &quot;A Game of Thrones&quot;
   * assertThat(book).containsSequence(asList(&quot;{&quot;, &quot;author&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;));</code></pre>
   *
   * @param values the Strings to look for, in order.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given strings <b>in the given
   *           order</b>.
   */
  public SELF containsSequence(Iterable<? extends CharSequence> values) {
    strings.assertContainsSequence(info, actual, IterableUtil.toArray(values, CharSequence.class));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains the given sequence, ignoring case considerations.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Gandalf the grey&quot;).containsIgnoringCase(&quot;gandalf&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Gandalf the grey&quot;).containsIgnoringCase(&quot;white&quot;);</code></pre>
   * 
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain the given one.
   */
  public SELF containsIgnoringCase(CharSequence sequence) {
    strings.assertContainsIgnoringCase(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain the given sequence.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;fro&quot;);
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;gandalf&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;Fro&quot;);</code></pre>
   * 
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} contains the given one.
   */
  public SELF doesNotContain(CharSequence sequence) {
    strings.assertDoesNotContain(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain the given regular expression.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotContainPattern(&quot;Fr.ud&quot;);
   *
   * // assertion will fail
   * assertThat(&quot;Freud&quot;).doesNotContainPattern(&quot;Fr.ud&quot;);</code></pre>
   *
   * @param pattern the regular expression to find in the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given regular expression can be found in the actual {@code CharSequence}.
   * @since 2.7.0 / 3.7.0
   */
  public SELF doesNotContainPattern(CharSequence pattern) {
    strings.assertDoesNotContainPattern(info, actual, pattern);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain the given regular expression.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotContainPattern(Pattern.compile(&quot;Fr.ud&quot;));
   *
   * // assertion will fail
   * assertThat(&quot;Freud&quot;).doesNotContainPattern(Pattern.compile(&quot;Fr.ud&quot;));</code></pre>
   *
   * @param pattern the regular expression to find in the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given regular expression can be found in the actual {@code CharSequence}.
   * @since 2.7.0 / 3.7.0
   */
  public SELF doesNotContainPattern(Pattern pattern) {
    strings.assertDoesNotContainPattern(info, actual, pattern);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} starts with the given prefix.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;Frodo&quot;).startsWith(&quot;Fro&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).startsWith(&quot;Gandalf&quot;);
   * 
   * // assertions will fail
   * assertThat(&quot;Frodo&quot;).startsWith(&quot;fro&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).startsWith(&quot;grey&quot;);</code></pre>
   * 
   * @param prefix the given prefix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given prefix is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not start with the given prefix.
   */
  public SELF startsWith(CharSequence prefix) {
    strings.assertStartsWith(info, actual, prefix);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not start with the given prefix.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;Frodo&quot;).doesNotStartWith(&quot;fro&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).doesNotStartWith(&quot;grey&quot;);
   *
   * // assertions will fail
   * assertThat(&quot;Gandalf the grey&quot;).doesNotStartWith(&quot;Gandalf&quot;);
   * assertThat(&quot;Frodo&quot;).doesNotStartWith(&quot;&quot;);</code></pre>
   *
   * @param prefix the given prefix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given prefix is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} starts with the given prefix.
   */
  public SELF doesNotStartWith(CharSequence prefix) {
    strings.assertDoesNotStartWith(info, actual, prefix);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} ends with the given suffix.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).endsWith(&quot;do&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).endsWith(&quot;Fro&quot;);</code></pre>
   * 
   * @param suffix the given suffix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given suffix is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not end with the given suffix.
   */
  public SELF endsWith(CharSequence suffix) {
    strings.assertEndsWith(info, actual, suffix);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not end with the given suffix.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotEndWith(&quot;Fro&quot;);
   *
   * // assertions will fail
   * assertThat(&quot;Frodo&quot;).doesNotEndWith(&quot;do&quot;);
   * assertThat(&quot;Frodo&quot;).doesNotEndWith(&quot;&quot;);</code></pre>
   *
   * @param suffix the given suffix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given suffix is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} ends with the given suffix.
   */
  public SELF doesNotEndWith(CharSequence suffix) {
    strings.assertDoesNotEndWith(info, actual, suffix);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} matches the given regular expression.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).matches(&quot;..o.o&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).matches(&quot;.*d&quot;);</code></pre>
   * 
   * @param regex the regular expression to which the actual {@code CharSequence} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not match the given regular expression.
   */
  public SELF matches(CharSequence regex) {
    strings.assertMatches(info, actual, regex);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not match the given regular expression.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotMatch(&quot;.*d&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotMatch(&quot;..o.o&quot;);</code></pre>
   *
   * @param regex the regular expression to which the actual {@code CharSequence} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} matches the given regular expression.
   */
  public SELF doesNotMatch(CharSequence regex) {
    strings.assertDoesNotMatch(info, actual, regex);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} matches the given regular expression pattern.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).matches(Pattern.compile(&quot;..o.o&quot;));
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).matches(Pattern.compile(&quot;.*d&quot;));</code></pre>
   *
   * @param pattern the regular expression to which the actual {@code CharSequence} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not match the given regular expression.
   */
  public SELF matches(Pattern pattern) {
    strings.assertMatches(info, actual, pattern);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not match the given regular expression pattern.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotMatch(Pattern.compile(&quot;.*d&quot;));
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotMatch(Pattern.compile(&quot;..o.o&quot;));</code></pre>
   *
   * @param pattern the regular expression to which the actual {@code CharSequence} is to be matched.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not match the given regular expression.
   */
  public SELF doesNotMatch(Pattern pattern) {
    strings.assertDoesNotMatch(info, actual, pattern);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the given XML {@code CharSequence} after both have been
   * formatted the same way.
   * <p>
   * Example :
   * <pre><code class='java'> String expectedXml =
   *     &quot;&lt;rings&gt;\n&quot; +
   *         &quot;  &lt;bearer&gt;\n&quot; +
   *         &quot;    &lt;name&gt;Frodo&lt;/name&gt;\n&quot; +
   *         &quot;    &lt;ring&gt;\n&quot; +
   *         &quot;      &lt;name&gt;one ring&lt;/name&gt;\n&quot; +
   *         &quot;      &lt;createdBy&gt;Sauron&lt;/createdBy&gt;\n&quot; +
   *         &quot;    &lt;/ring&gt;\n&quot; +
   *         &quot;  &lt;/bearer&gt;\n&quot; +
   *         &quot;&lt;/rings&gt;&quot;;
   * 
   * // No matter how your xml string is formated, isXmlEqualTo is able to compare it's content with another xml String.
   * String oneLineXml = &quot;&lt;rings&gt;&lt;bearer&gt;&lt;name&gt;Frodo&lt;/name&gt;&lt;ring&gt;&lt;name&gt;one ring&lt;/name&gt;&lt;createdBy&gt;Sauron&lt;/createdBy&gt;&lt;/ring&gt;&lt;/bearer&gt;&lt;/rings&gt;&quot;;
   * assertThat(oneLineXml).isXmlEqualTo(expectedXml);
   * 
   * String xmlWithNewLine =
   *     &quot;&lt;rings&gt;\n&quot; +
   *         &quot;&lt;bearer&gt;   \n&quot; +
   *         &quot;  &lt;name&gt;Frodo&lt;/name&gt;\n&quot; +
   *         &quot;  &lt;ring&gt;\n&quot; +
   *         &quot;    &lt;name&gt;one ring&lt;/name&gt;\n&quot; +
   *         &quot;    &lt;createdBy&gt;Sauron&lt;/createdBy&gt;\n&quot; +
   *         &quot;  &lt;/ring&gt;\n&quot; +
   *         &quot;&lt;/bearer&gt;\n&quot; +
   *         &quot;&lt;/rings&gt;&quot;;
   * assertThat(xmlWithNewLine).isXmlEqualTo(expectedXml);
   * 
   * // You can compare it with oneLineXml
   * assertThat(xmlWithNewLine).isXmlEqualTo(oneLineXml);
   * 
   * // Tip : use isXmlEqualToContentOf assertion to compare your XML String with the content of an XML file :
   * assertThat(oneLineXml).isXmlEqualToContentOf(new File(&quot;src/test/resources/formatted.xml&quot;));</code></pre>
   *
   * @param expectedXml the XML {@code CharSequence} to which the actual {@code CharSequence} is to be compared to.
   * @return {@code this} assertion object to chain other assertions.
   * @throws NullPointerException if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null} or is not the same XML as the given XML
   *           {@code CharSequence}.
   */
  public SELF isXmlEqualTo(CharSequence expectedXml) {
    strings.assertXmlEqualsTo(info, actual, expectedXml);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the content of the given file.
   * <p>
   * This is an handy shortcut that calls : {@code isXmlEqualTo(contentOf(xmlFile))}
   * </p>
   * Example :
   * <pre><code class='java'> // You can easily compare your XML String to the content of an XML file, whatever how formatted they are.
   * String oneLineXml = &quot;&lt;rings&gt;&lt;bearer&gt;&lt;name&gt;Frodo&lt;/name&gt;&lt;ring&gt;&lt;name&gt;one ring&lt;/name&gt;&lt;createdBy&gt;Sauron&lt;/createdBy&gt;&lt;/ring&gt;&lt;/bearer&gt;&lt;/rings&gt;&quot;;
   * assertThat(oneLineXml).isXmlEqualToContentOf(new File(&quot;src/test/resources/formatted.xml&quot;));</code></pre>
   * 
   * @param xmlFile the file to read the expected XML String to compare with actual {@code CharSequence}
   * @return {@code this} assertion object to chain other assertions.
   * @throws NullPointerException if the given {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null} or is not the same XML as the content of
   *           given {@code File}.
   */
  public SELF isXmlEqualToContentOf(File xmlFile) {
    isXmlEqualTo(contentOf(xmlFile));
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
  public final SELF usingElementComparator(Comparator<? super Character> customComparator) {
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
  public final SELF usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for CharSequence comparison");
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator) {
    super.usingComparator(customComparator);
    this.strings = new Strings(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    super.usingDefaultComparator();
    this.strings = Strings.instance();
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF inHexadecimal() {
    return super.inHexadecimal();
  }

  /**
   * Use unicode character representation instead of standard representation in error messages.
   * <p>
   * It can be useful when comparing UNICODE characters - many unicode chars have duplicate characters assigned, it is
   * thus impossible to find differences from the standard error message:
   * <p>
   * With standard message:
   * <pre><code class='java'> assertThat("µµµ").contains("μμμ");
   * 
   * java.lang.AssertionError:
   * Expecting:
   *   &lt;"µµµ"&gt;
   * to contain:
   *   &lt;"μμμ"&gt;</code></pre>
   *
   * With Hexadecimal message:
   * <pre><code class='java'> assertThat("µµµ").inUnicode().contains("μμμ");
   * 
   * java.lang.AssertionError:
   * Expecting:
   *   &lt;\u00b5\u00b5\u00b5&gt;
   * to contain:
   *   &lt;\u03bc\u03bc\u03bc&gt;</code></pre>
   *
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public SELF inUnicode() {
    info.useUnicodeRepresentation();
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the given one, ignoring whitespace differences
   * <p>
   * Examples :
   * <pre><code class='java'> // assertions will pass
   * assertThat("Game of Thrones").isEqualToIgnoringWhitespace("Game   of   Thrones")
   *                              .isEqualToIgnoringWhitespace("  Game of   Thrones  ")
   *                              .isEqualToIgnoringWhitespace("  Game of Thrones  ")
   *                              .isEqualToIgnoringWhitespace("Gameof      Thrones")
   *                              .isEqualToIgnoringWhitespace("Game of\tThrones")
   *                              .isEqualToIgnoringWhitespace("GameofThrones");
   *
   * // assertion will fail
   * assertThat("Game of Thrones").isEqualToIgnoringWhitespace("Game OF Thrones");</code></pre>
   * <p>
   * This assertion behavior has changed in 2.8.0 to really ignore all whitespaces, 
   * the old behaviour has been kept in the better named {@link #isEqualToNormalizingWhitespace(CharSequence)}.  
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not equal ignoring whitespace differences to the given
   *           one.
   */
  public SELF isEqualToIgnoringWhitespace(CharSequence expected) {
    strings.assertEqualsIgnoringWhitespace(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is not equal to the given one, ignoring whitespace differences.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot; my\tfoo bar &quot;).isNotEqualToIgnoringWhitespace(&quot;myfoo&quot;);
   * assertThat(&quot; my\tfoo&quot;).isNotEqualToIgnoringWhitespace(&quot; my bar&quot;);
   *
   * // assertions will fail
   * assertThat(&quot;my      foo bar&quot;).isNotEqualToIgnoringWhitespace(&quot;my foo bar&quot;);
   * assertThat(&quot;  my foo bar  &quot;).isNotEqualToIgnoringWhitespace(&quot;my foo bar&quot;);
   * assertThat(&quot; my     foo bar &quot;).isNotEqualToIgnoringWhitespace(&quot;my foo bar&quot;);
   * assertThat(&quot; my\tfoo bar &quot;).isNotEqualToIgnoringWhitespace(&quot; my foo bar&quot;);
   * assertThat(&quot;my foo bar&quot;).isNotEqualToIgnoringWhitespace(&quot;   my foo bar   &quot;);
   * </code></pre>
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is equal ignoring whitespace differences to the given
   *           one.
   */
  public SELF isNotEqualToIgnoringWhitespace(CharSequence expected) {
    strings.assertNotEqualsIgnoringWhitespace(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the given one, after the whitespace
   * of both strings has been normalized.<br>
   * To be exact, the following rules are applied:
   * <ul>
   * <li>all leading and trailing whitespace of both actual and expected strings are ignored</li>
   * <li>any remaining whitespace, appearing within either string, is collapsed to a single space before comparison</li>
   * </ul>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat("Game of Thrones").isEqualToNormalizingWhitespace("Game   of   Thrones")
   *                              .isEqualToNormalizingWhitespace("Game of     Thrones")
   *                              .isEqualToNormalizingWhitespace("Game     of Thrones")
   *                              .isEqualToNormalizingWhitespace("  Game of Thrones  ")
   *                              .isEqualToNormalizingWhitespace("  Game of   Thrones  ")
   *                              .isEqualToNormalizingWhitespace("Game of\tThrones")
   *                              .isEqualToNormalizingWhitespace("Game of Thrones");
   *
   * // assertions will fail
   * assertThat("Game of Thrones").isEqualToNormalizingWhitespace("Game ofThrones");
   * assertThat("Game of Thrones").isEqualToNormalizingWhitespace("Gameo fThrones");
   * assertThat("Game of Thrones").isEqualToNormalizingWhitespace("Gameof Thrones");</code></pre>
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not equal to the given one
   *           after whitespace has been normalized.
   * @since 2.8.0 / 3.8.0
   */
  public SELF isEqualToNormalizingWhitespace(CharSequence expected) {
    strings.assertEqualsNormalizingWhitespace(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is not equal to the given one, after the whitespace
   * of both strings has been normalized.<br>
   * To be exact, the following rules are applied:
   * <ul>
   * <li>all leading and trailing whitespace of both actual and expected strings are ignored</li>
   * <li>any remaining whitespace, appearing within either string, is collapsed to a single space before comparison</li>
   * </ul>
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot; my\tfoo&quot;).isNotEqualToNormalizingWhitespace(&quot; my bar&quot;);
   * assertThat(&quot; my\tfoo bar &quot;).isNotEqualToNormalizingWhitespace(&quot; my foobar&quot;);
   *
   * // assertions will fail
   * assertThat(&quot;my      foo bar&quot;).isNotEqualToNormalizingWhitespace(&quot;my foo bar&quot;);
   * assertThat(&quot;  my foo bar  &quot;).isNotEqualToNormalizingWhitespace(&quot;my foo bar&quot;);
   * assertThat(&quot; my     foo bar &quot;).isNotEqualToNormalizingWhitespace(&quot;my foo bar&quot;);
   * assertThat(&quot; my\tfoo bar &quot;).isNotEqualToNormalizingWhitespace(&quot; my foo bar&quot;);
   * assertThat(&quot;my foo bar&quot;).isNotEqualToNormalizingWhitespace(&quot;   my foo bar   &quot;);
   * </code></pre>
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is equal to the given one
   *           after whitespace has been normalized.
   * @since 2.8.0 / 3.8.0
   */
  public SELF isNotEqualToNormalizingWhitespace(CharSequence expected) {
    strings.assertNotEqualsNormalizingWhitespace(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is a substring of the given one (opposite assertion of {@link #contains(CharSequence...) contains(CharSequence cs)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;Lego&quot;).isSubstringOf(&quot;Legolas&quot;);
   * assertThat(&quot;Legolas&quot;).isSubstringOf(&quot;Legolas&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).isSubstringOf(&quot;Frod&quot;);</code></pre>
   * 
   * @param sequence the sequence that is expected to contain actual.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not a substring of the given parameter.
   */
  public SELF isSubstringOf(CharSequence sequence) {
    strings.assertIsSubstringOf(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains the given regular expression.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).containsPattern(&quot;Fr.d&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).containsPattern(&quot;Frodod&quot;);</code></pre>
   * 
   * @param regex the regular expression to find in the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given regular expression cannot be found in the actual {@code CharSequence}.
   */
  public SELF containsPattern(CharSequence regex) {
    strings.assertContainsPattern(info, actual, regex);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains the given regular expression pattern.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).containsPattern(Pattern.compile(&quot;Fr.d&quot;));
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).containsPattern(Pattern.compile(&quot;Frodod&quot;));</code></pre>
   *
   * @param pattern the regular expression to find in the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given regular expression cannot be found in the actual {@code CharSequence}.
   */
  public SELF containsPattern(Pattern pattern) {
    strings.assertContainsPattern(info, actual, pattern);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equals to another
   * {@code CharSequence} after normalizing new line characters
   * (i.e. '\r\n' == '\n').
   * <p>
   * This assertion will succeed:
   * <pre><code class='java'> String bookName = &quot;Lord of the Rings\r\n&quot;;
   * assertThat(bookName).isEqualToNormalizingNewlines(&quot;Lord of the Rings\n&quot;);</code></pre>
   * 
   * Whereas this assertion will fail:
   * <pre><code class='java'> String singleLine = &quot;\n&quot;;
   * assertThat(singleLine).isEqualToNormalizingNewlines(&quot;&quot;);</code></pre>
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} and the given {@code CharSequence} are different
   *         after new lines are normalized.
   * @since 2.7.0 / 3.7.0
   */
  public SELF isEqualToNormalizingNewlines(CharSequence expected) {
    strings.assertIsEqualToNormalizingNewlines(info, actual, expected);
    return myself;
  }
}
