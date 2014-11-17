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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.io.LineNumberReader;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.core.util.VisibleForTesting;

import static org.assertj.core.api.Assertions.contentOf;

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
   * 
   * <pre><code class='java'>
   * String emptyString = &quot;&quot;
   * assertThat(emptyString).isNullOrEmpty();
   * 
   * String nullString = null;
   * assertThat(nullString).isNullOrEmpty();
   * </code></pre>
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
   * 
   * <pre><code class='java'>
   * String emptyString = &quot;&quot;
   * assertThat(emptyString).isEmpty();
   * </code></pre>
   * 
   * Whereas this assertion will fail:
   * 
   * <pre><code class='java'>
   * String nullString = null;
   * assertThat(nullString).isEmpty();
   * </code></pre>
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
   * 
   * <pre><code class='java'>
   * String bookName = &quot;A Game of Thrones&quot;
   * assertThat(bookName).isNotEmpty();
   * </code></pre>
   * 
   * Whereas this assertion will fail:
   * 
   * <pre><code class='java'>
   * String emptyString = &quot;&quot;
   * assertThat(emptyString).isNotEmpty();
   * </code></pre>
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
   * 
   * <pre><code class='java'>
   * String bookName = &quot;A Game of Thrones&quot;
   * assertThat(bookName).hasSize(17);
   * </code></pre>
   * 
   * Whereas this assertion will fail:
   * 
   * <pre><code class='java'>
   * String bookName = &quot;A Clash of Kings&quot;
   * assertThat(bookName).hasSize(4);
   * </code></pre>
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
   * Verifies that the actual {@code CharSequence} has the expected line count.
   * <p/>
   * A line is considered to be <a name="lt">terminated</a> by any one of a line feed ('\n'), a carriage return ('\r'),
   * or a carriage return followed immediately by a linefeed (see {@link LineNumberReader}).
   * <p>
   * This assertion will succeed:
   * 
   * <pre><code class='java'>
   * String multiLine = &quot;First line\n&quot; +
   *                    &quot;Last line&quot;;
   * assertThat(multiLine).hasLineCount(2);
   * </code></pre>
   * Whereas this assertion will fail:
   * <pre><code class='java'>
   * String bookName = &quot;A Clash of Kings&quot;;
   * assertThat(bookName).hasLineCount(3);
   * </code></pre>
   *
   * @param expectedLineCount the expected line count of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual line count is not equal to the expected one.
   */
  public S hasLineCount(int expectedLineCount) {
    strings.assertHasLineCount(info, actual, expectedLineCount);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length that's the same as the length of the given
   * {@code CharSequence}.
   * <p>
   * Examples :
   * 
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;C-3PO&quot;).hasSameSizeAs(&quot;R2-D2&quot;);
   * 
   * // assertion will fail as actual and expected sizes differ
   * assertThat(&quot;C-3PO&quot;).hasSameSizeAs(&quot;B1 battle droid&quot;);
   * </code></pre>
   * 
   * @param other the given {@code CharSequence} to be used for size comparison.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} has a length that's different from the length of the
   *           given {@code CharSequence}.
   * @throws NullPointerException if the given {@code CharSequence} is {@code null}.
   */
  public S hasSameSizeAs(CharSequence other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length that's the same as the number of elements in the given
   * array.
   *
   * @param other the given array to be used for size comparison.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} has a length that's different from the number of elements
   *           in the array.
   * @throws NullPointerException if the given array is {@code null}.
   */
  public S hasSameSizeAs(Object other) {
    strings.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length that's the same as the number of elements in the given
   * Iterable.
   *
   * @param other the given {@code Iterable} to be used for size comparison.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} has a length that's different from the number of elements
   *           in the {@code Iterable}.
   * @throws NullPointerException if the given {@code Iterable} is {@code null}.
   */
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
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;Gandalf the grey&quot;).isEqualToIgnoringCase(&quot;GaNdAlF tHe GREY&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).isEqualToIgnoringCase(&quot;Gandalf the grey&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Gandalf the grey&quot;).isEqualToIgnoringCase(&quot;Gandalf the white&quot;);
   * </code></pre>
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
   * Verifies that the actual {@code CharSequence} contains the given sequence <b>only once</b>.
   * <p>
   * Example :
   * 
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).containsOnlyOnce(&quot;do&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).containsOnlyOnce(&quot;o&quot;);
   * </code></pre>
   * 
   * </p>
   * 
   * @param sequence the sequence to search for.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} either does not contain the given one at all, or contains
   *           it more than once.
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
   * <pre><code class='java'>
   * assertThat(&quot;Gandalf the grey&quot;).contains(&quot;alf&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).contains(&quot;alf&quot;, &quot;grey&quot;);
   * </code></pre>
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
   * <pre><code class='java'>
   * String book = &quot;{ 'title':'A Game of Thrones', 'author':'George Martin'}&quot;;
   * 
   * // this assertion succeeds ...
   * assertThat(book).containsSequence(&quot;{&quot;, &quot;title&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;);
   * 
   * // ... but this one fails as &quot;author&quot; must come after &quot;A Game of Thrones&quot;
   * assertThat(book).containsSequence(&quot;{&quot;, &quot;author&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;);
   * </code></pre>
   *
   * @param values the Strings to look for, in order.
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
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;Gandalf the grey&quot;).containsIgnoringCase(&quot;gandalf&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Gandalf the grey&quot;).containsIgnoringCase(&quot;white&quot;);
   * </code></pre>
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
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;fro&quot;);
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;gandalf&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;Fro&quot;);
   * </code></pre>
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
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).startsWith(&quot;Fro&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).startsWith(&quot;Gandalf&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).startsWith(&quot;fro&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).startsWith(&quot;grey&quot;);
   * </code></pre>
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
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).endsWith(&quot;do&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).endsWith(&quot;Fro&quot;);
   * </code></pre>
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
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).matches(&quot;..o.o&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).matches(&quot;.*d&quot;);
   * </code></pre>
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
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotMatch(&quot;.*d&quot;);
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotMatch(&quot;..o.o&quot;);
   * </code></pre>
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
   * Verifies that the actual {@code CharSequence} matches the given regular expression pattern.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).matches(Pattern.compile(&quot;..o.o&quot;));
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).matches(Pattern.compile(&quot;.*d&quot;));
   * </code></pre>
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
   * Verifies that the actual {@code CharSequence} does not match the given regular expression pattern.
   * <p>
   * Example :
   *
   * <pre><code class='java'>
   * // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotMatch(Pattern.compile(&quot;.*d&quot;));
   * 
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotMatch(Pattern.compile(&quot;..o.o&quot;));
   * </code></pre>
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
   * Verifies that the actual {@code CharSequence} is equal to the given XML {@code CharSequence} after both have been
   * formatted the same way.
   * <p>
   * Example :
   * </p>
   *
   * <pre><code class='java'>
   * String expectedXml =
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
   * // Whatever how formatted your xml string is, isXmlEqualTo assertion is able to compare it with another xml String.
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
   * assertThat(oneLineXml).isXmlEqualToContentOf(new File(&quot;src/test/resources/formatted.xml&quot;));
   * </code></pre>
   *
   * @param expectedXml the XML {@code CharSequence} to which the actual {@code CharSequence} is to be compared to.
   * @return {@code this} assertion object to chain other assertions.
   * @throws NullPointerException if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null} or is not the same XML as the given XML
   *           {@code CharSequence}.
   */
  public S isXmlEqualTo(CharSequence expectedXml) {
    strings.assertXmlEqualsTo(info, actual, expectedXml);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the content of the given file.
   * <p/>
   * This is an handy shortcut that calls : {@code isXmlEqualTo(contentOf(xmlFile))}
   * </p>
   * <p>
   * Example :
   * </p>
   *
   * <pre><code class='java'>
   * // You can easily compare your XML String to the content of an XML file, whatever how formatted thay are.
   * String oneLineXml = &quot;&lt;rings&gt;&lt;bearer&gt;&lt;name&gt;Frodo&lt;/name&gt;&lt;ring&gt;&lt;name&gt;one ring&lt;/name&gt;&lt;createdBy&gt;Sauron&lt;/createdBy&gt;&lt;/ring&gt;&lt;/bearer&gt;&lt;/rings&gt;&quot;;
   * assertThat(oneLineXml).isXmlEqualToContentOf(new File(&quot;src/test/resources/formatted.xml&quot;));
   * </code></pre>
   * 
   * @param xmlFile the file to read the expected XML String to compare with actual {@code CharSequence}
   * @return {@code this} assertion object to chain other assertions.
   * @throws NullPointerException if the given {@code File} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null} or is not the same XML as the content of
   *           given {@code File}.
   */
  public S isXmlEqualToContentOf(File xmlFile) {
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

  @Override
  public S inHexadecimal() {
    return super.inHexadecimal();
  }

  /**
   * Use unicode character representation instead of standard representation in error messages.
   * <p/>
   * It can be useful when comparing UNICODE characters - many unicode chars have duplicate characters assigned, it is
   * thus impossible to find differences from the standard error message:
   * <p/>
   * With standard message:
   * 
   * <pre><code class='java'>
   * assertThat("µµµ").contains("μμμ");
   * 
   * java.lang.AssertionError:
   * Expecting:
   *   <"µµµ">
   * to contain:
   *   <"μμμ">
   * </code></pre>
   *
   * With Hexadecimal message:
   * 
   * <pre><code class='java'>
   * assertThat("µµµ").inUnicode().contains("μμμ");
   * 
   * java.lang.AssertionError:
   * Expecting:
   *   <\u00b5\u00b5\u00b5>
   * to contain:
   *   <\u03bc\u03bc\u03bc>
   * </code></pre>
   *
   * @return {@code this} assertion object.
   */
  public S inUnicode() {
    info.useUnicodeRepresentation();
    return myself;
  }

}
