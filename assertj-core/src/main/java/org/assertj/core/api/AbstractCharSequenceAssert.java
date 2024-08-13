/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.Character.isWhitespace;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.Assertions.contentOf;
import static org.assertj.core.error.ShouldBeASCII.shouldBeASCII;
import static org.assertj.core.error.ShouldBeAlphabetic.shouldBeAlphabetic;
import static org.assertj.core.error.ShouldBeAlphanumeric.shouldBeAlphanumeric;
import static org.assertj.core.error.ShouldBeBlank.shouldBeBlank;
import static org.assertj.core.error.ShouldBeHexadecimal.shouldBeHexadecimal;
import static org.assertj.core.error.ShouldBePrintable.shouldBePrintable;
import static org.assertj.core.error.ShouldBeVisible.shouldBeVisible;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringWhitespaces;
import static org.assertj.core.error.ShouldContainOneOrMoreWhitespaces.shouldContainOneOrMoreWhitespaces;
import static org.assertj.core.error.ShouldContainOnlyWhitespaces.shouldContainOnlyWhitespaces;
import static org.assertj.core.error.ShouldNotBeBlank.shouldNotBeBlank;
import static org.assertj.core.error.ShouldNotContainAnyWhitespaces.shouldNotContainAnyWhitespaces;
import static org.assertj.core.error.ShouldNotContainOnlyWhitespaces.shouldNotContainOnlyWhitespaces;
import static org.assertj.core.error.ShouldNotEndWithWhitespaces.shouldNotEndWithWhitespaces;
import static org.assertj.core.error.ShouldNotStartWithWhitespaces.shouldNotStartWithWhitespaces;
import static org.assertj.core.internal.Strings.doCommonCheckForCharSequence;
import static org.assertj.core.internal.Strings.removeAllWhitespaces;
import static org.assertj.core.util.IterableUtil.toArray;

import java.io.File;
import java.io.LineNumberReader;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@code CharSequence}s.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas Francois
 * @author Daniel Weber
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 */
public abstract class AbstractCharSequenceAssert<SELF extends AbstractCharSequenceAssert<SELF, ACTUAL>, ACTUAL extends CharSequence>
    extends AbstractAssert<SELF, ACTUAL> implements EnumerableAssert<SELF, Character> {

  @VisibleForTesting
  Strings strings = Strings.instance();

  protected AbstractCharSequenceAssert(ACTUAL actual, Class<?> selfType) {
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
   * Verifies that the actual {@code CharSequence} is blank, i.e. is {@code null}, empty or consists of one or more
   * whitespace characters (according to {@link Character#isWhitespace(char)}).
   * <p>
   * The definition of this method has changed, the old behaviour is now under {@link #containsOnlyWhitespaces()}.
   * <p>
   * These assertions succeed:
   * <pre><code class='java'> assertThat(" ").isBlank();
   * assertThat("").isBlank();
   * assertThat("    ").isBlank();
   * String nullString = null;
   * assertThat(nullString).isBlank();</code></pre>
   *
   * Whereas these assertions fail:
   * <pre><code class='java'> assertThat("a").isBlank();
   * assertThat(" b").isBlank();
   * assertThat(" c ").isBlank();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not blank.
   * @since 2.6.0 / 3.6.0
   */
  public SELF isBlank() {
    assertBlank(actual);
    return myself;
  }

  private void assertBlank(CharSequence actual) {
    if (!isBlank(actual)) throw assertionError(shouldBeBlank(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} is:
   * <ul>
   *   <li><b>not</b> {@code null}</li>
   *   <li><b>not</b> empty</li>
   *   <li>contains at least one non-whitespace character (according to {@link Character#isWhitespace(char)})</li>
   * </ul>
   * <p>
   * The definition of this method has changed, the old behaviour is now under {@link #doesNotContainOnlyWhitespaces()}.
   * <p>
   * These assertions succeed:
   * <pre><code class='java'> assertThat("a").isNotBlank();
   * assertThat(" b").isNotBlank();
   * assertThat(" c ").isNotBlank();</code></pre>
   *
   * Whereas these assertions fail:
   * <pre><code class='java'> assertThat(" ").isNotBlank();
   * assertThat("").isNotBlank();
   * assertThat("    ").isNotBlank();
   * String nullString = null;
   * assertThat(nullString).isNotBlank();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is blank.
   * @since 2.6.0 / 3.6.0
   */
  public SELF isNotBlank() {
    assertNotBlank(actual);
    return myself;
  }

  private void assertNotBlank(CharSequence actual) {
    if (isBlank(actual)) throw assertionError(shouldNotBeBlank(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} contains one or more whitespace characters (according to
   * {@link Character#isWhitespace(char)}).
   * <p>
   * These assertions will succeed:
   * <pre><code class='java'> assertThat(" ").containsWhitespaces();
   * assertThat("a b").containsWhitespaces();
   * assertThat(" c ").containsWhitespaces();</code></pre>
   *
   * Whereas these assertions will fail:
   * <pre><code class='java'> assertThat("").containsWhitespaces();
   * assertThat("a").containsWhitespaces();
   * String nullString = null;
   * assertThat(nullString).containsWhitespaces();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} does not contain any whitespace characters.
   * @since 3.11.0
   */
  public SELF containsWhitespaces() {
    assertContainsWhitespaces(actual);
    return myself;
  }

  private void assertContainsWhitespaces(CharSequence actual) {
    if (!containsWhitespaces(actual)) throw assertionError(shouldContainOneOrMoreWhitespaces(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} consists of one or more whitespace characters (according to
   * {@link Character#isWhitespace(char)}).
   * <p>
   * These assertions will succeed:
   * <pre><code class='java'> assertThat(" ").containsOnlyWhitespaces();
   * assertThat("    ").containsOnlyWhitespaces();</code></pre>
   *
   * Whereas these assertions will fail:
   * <pre><code class='java'> assertThat("a").containsOnlyWhitespaces();
   * assertThat("").containsOnlyWhitespaces();
   * assertThat(" b").containsOnlyWhitespaces();
   * assertThat(" c ").containsOnlyWhitespaces();
   *
   * String nullString = null;
   * assertThat(nullString).containsOnlyWhitespaces();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not blank.
   * @since 2.9.0 / 3.9.0
   */
  public SELF containsOnlyWhitespaces() {
    assertContainsOnlyWhitespaces(actual);
    return myself;
  }

  private void assertContainsOnlyWhitespaces(CharSequence actual) {
    if (!containsOnlyWhitespaces(actual)) throw assertionError(shouldContainOnlyWhitespaces(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} is either {@code null}, empty or does not contain any whitespace characters (according to {@link Character#isWhitespace(char)}).
   * <p>
   * These assertions will succeed:
   * <pre><code class='java'> assertThat("a").doesNotContainAnyWhitespaces();
   * assertThat("").doesNotContainAnyWhitespaces();
   * assertThat("ab").doesNotContainAnyWhitespaces();
   *
   * String nullString = null;
   * assertThat(nullString).doesNotContainAnyWhitespaces();</code></pre>
   *
   * Whereas these assertions will fail:
   * <pre><code class='java'> assertThat(" ").doesNotContainAnyWhitespaces();
   * assertThat(" a").doesNotContainAnyWhitespaces();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} contains one or more whitespace characters.
   * @since 3.11.0
   */
  public SELF doesNotContainAnyWhitespaces() {
    assertDoesNotContainAnyWhitespaces(actual);
    return myself;
  }

  private void assertDoesNotContainAnyWhitespaces(CharSequence actual) {
    if (containsWhitespaces(actual)) throw assertionError(shouldNotContainAnyWhitespaces(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} is either:
   * <ul>
   *   <li>{@code null}</li>
   *   <li>empty</li>
   *   <li>contains at least one non-whitespace character (according to {@link Character#isWhitespace(char)}).</li>
   * </ul>
   * <p>
   * The main difference with {@link #isNotBlank()} is that it accepts null or empty {@code CharSequence}.
   * <p>
   * These assertions will succeed:
   * <pre><code class='java'> assertThat("a").doesNotContainOnlyWhitespaces();
   * assertThat("").doesNotContainOnlyWhitespaces();
   * assertThat(" b").doesNotContainOnlyWhitespaces();
   * assertThat(" c ").doesNotContainOnlyWhitespaces();
   * String nullString = null;
   * assertThat(nullString).doesNotContainOnlyWhitespaces();</code></pre>
   *
   * Whereas these assertions will fail:
   * <pre><code class='java'> assertThat(" ").doesNotContainOnlyWhitespaces();
   * assertThat("    ").doesNotContainOnlyWhitespaces();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is blank.
   * @since 2.9.0 / 3.9.0
   */
  public SELF doesNotContainOnlyWhitespaces() {
    assertDoesNotContainOnlyWhitespaces(actual);
    return myself;
  }

  private void assertDoesNotContainOnlyWhitespaces(CharSequence actual) {
    if (containsOnlyWhitespaces(actual)) throw assertionError(shouldNotContainOnlyWhitespaces(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} is blank, i.e. consists of one or more whitespace characters
   * (according to {@link Character#isWhitespace(char)}).
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
   * @deprecated Use {@link #isBlank()} instead.
   */
  @Deprecated
  public SELF isJavaBlank() {
    assertJavaBlank(actual);
    return myself;
  }

  private void assertJavaBlank(CharSequence actual) {
    if (!containsOnlyWhitespaces(actual)) throw assertionError(shouldBeBlank(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} is not blank, i.e. either is {@code null}, empty or
   * contains at least one non-whitespace character (according to {@link Character#isWhitespace(char)}).
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
   * @deprecated Use {@link #isNotBlank()} instead.
   */
  @Deprecated
  public SELF isNotJavaBlank() {
    assertNotJavaBlank(actual);
    return myself;
  }

  private void assertNotJavaBlank(CharSequence actual) {
    if (containsOnlyWhitespaces(actual)) throw assertionError(shouldNotBeBlank(actual));
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
   * Verifies that the actual {@code CharSequence} has has a length less than the given value using the {@code length()} method.
   * <p>
   * This assertion will succeed:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeLessThan(4);</code></pre>
   *
   * Whereas this assertion will fail:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeLessThan(3);</code></pre>
   *
   * @param expected the expected maximum length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is equal or greater than the expected length.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThan(int expected) {
    strings.assertHasSizeLessThan(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length less than or equal to the given value using the {@code length()}  method.
   * <p>
   * This assertion will succeed:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeLessThanOrEqualTo(3);</code></pre>
   *
   * Whereas this assertion will fail:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeLessThanOrEqualTo(2);</code></pre>
   *
   * @param expected the expected maximum length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is greater than the expected length.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThanOrEqualTo(int expected) {
    strings.assertHasSizeLessThanOrEqualTo(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length greater than the given value using the {@code length()}  method.
   * <p>
   * This assertion will succeed:
   * <pre><code class='java'>assertThat(&quot;abcs&quot;).hasSizeGreaterThan(2);</code></pre>
   *
   * Whereas this assertion will fail:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeGreaterThan(3);</code></pre>
   *
   * @param expected the expected minimum length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is equal or less than the expected length.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThan(int expected) {
    strings.assertHasSizeGreaterThan(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length greater than or equal to the given value using the {@code length()}  method.
   * <p>
   * This assertion will succeed:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeGreaterThanOrEqualTo(3);</code></pre>
   *
   * Whereas this assertion will fail:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeGreaterThanOrEqualTo(3);</code></pre>
   *
   * @param expected the expected minimum length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is less than the expected length.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThanOrEqualTo(int expected) {
    strings.assertHasSizeGreaterThanOrEqualTo(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has length between the given boundaries (inclusive)
   * using the {@code length()} method.
   * <p>
   * This assertion will succeed:
   * <pre><code class='java'> String bookName = &quot;A Game of Thrones&quot;
   * assertThat(bookName).hasSizeBetween(5, 25);
   * assertThat(bookName).hasSizeBetween(16, 17);</code></pre>
   *
   * Whereas this assertion will fail:
   * <pre><code class='java'> String bookName = &quot;A Clash of Kings&quot;
   * assertThat(bookName).hasSizeBetween(2, 5);</code></pre>
   *
   * @param lowerBoundary the lower boundary compared to which actual length should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual length should be less than or equal to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is not between the boundaries.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeBetween(int lowerBoundary, int higherBoundary) {
    strings.assertHasSizeBetween(info, actual, lowerBoundary, higherBoundary);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} has the expected line count.
   * <p>
   * A line is considered to be <i>terminated</i> by any one of a line feed ({@code '\n'}), a carriage return ({@code '\r'}),
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
   * Example:
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
   * Example:
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
   * Whereas these assertions fail:
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
   * Example:
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
   * @param values the values to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given values.
   */
  public SELF contains(CharSequence... values) {
    strings.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains any of the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(&quot;Gandalf the grey&quot;).containsAnyOf(&quot;grey&quot;, &quot;black&quot;);
   *
   * // assertion fails
   * assertThat(&quot;Gandalf the grey&quot;).containsAnyOf(&quot;white&quot;, &quot;black&quot;);</code></pre>
   *
   * @param values the values to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain any of the given values.
   * @since 3.21.0
   */
  public SELF containsAnyOf(CharSequence... values) {
    strings.assertContainsAnyOf(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the {@code CharSequence}s of the given Iterable.
   * <p>
   * Examples:
   * <pre><code class='java'> assertThat(&quot;Gandalf the grey&quot;).contains(Arrays.asList(&quot;alf&quot;));
   * assertThat(&quot;Gandalf the grey&quot;).contains(Arrays.asList(&quot;alf&quot;, &quot;grey&quot;));</code></pre>
   *
   * @param values the values to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given values.
   */
  public SELF contains(Iterable<? extends CharSequence> values) {
    strings.assertContains(info, actual, toArray(values, CharSequence.class));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains the given sequence of values <b>in the given order without any other values between them</b>.
   * <p>
   * <b>Breaking change since 2.9.0</b>: in previous versions this assertion behaved like {@link #containsSubsequence(CharSequence...) containsSubsequence}
   * and allowed other values between the sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> String book = &quot;{ 'title':'A Game of Thrones', 'author':'George Martin'}&quot;;
   *
   * // this assertion succeeds
   * assertThat(book).containsSequence(&quot;'title'&quot;, &quot;:&quot;, &quot;'A Game of Thrones'&quot;);
   *
   * // this assertion will fail because there are values between the expected sequence (e.g &quot;'title':'&quot;)
   * assertThat(book).containsSequence(&quot;{&quot;, &quot;A Game of Thrones&quot;, &quot;George Martin&quot;, &quot;}&quot;);
   *
   * // this one fails as &quot;:&quot; must come after &quot;'title'&quot;
   * assertThat(book).containsSequence(&quot;:&quot;, &quot;'title'&quot;, &quot;'A Game of Thrones'&quot;);</code></pre>
   *
   * @param values the sequence of charSequence to look for, in order.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence of charSequence is {@code null}.
   * @throws IllegalArgumentException if the given sequence of charSequence is empty.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} does not contain the given sequence of values in the given order without any other values between them.
   */
  public SELF containsSequence(CharSequence... values) {
    strings.assertContainsSequence(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the values of the given Iterable <b>in the Iterable
   * iteration order without any other values between them</b>.
   * <p>
   * <b>Breaking change since 2.9.0</b>: in previous versions this assertion behaved like {@link #containsSubsequence(Iterable) containsSubsequence}
   * and allowed other values between the sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> String book = &quot;{ 'title':'A Game of Thrones', 'author':'George Martin'}&quot;;
   *
   * // this assertion succeeds
   * assertThat(book).containsSequence(asList(&quot;'title'&quot;, &quot;:&quot;, &quot;'A Game of Thrones'&quot;));
   *
   * // this assertion will fail because there are values between the expected sequence (e.g &quot;'title':'&quot;)
   * assertThat(book).containsSequence(asList(&quot;{&quot;, &quot;A Game of Thrones&quot;, &quot;George Martin&quot;, &quot;}&quot;));
   *
   * // this one fails as &quot;author&quot; must come after &quot;A Game of Thrones&quot;
   * assertThat(book).containsSequence(asList(&quot;author&quot;, &quot;A Game of Thrones&quot;));</code></pre>
   *
   * @param values the sequence of charSequence to look for, in order.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given sequence of charSequence is {@code null}.
   * @throws IllegalArgumentException if the given sequence of charSequence is empty.
   * @throws AssertionError if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given {@code CharSequence} does not contain the given sequence of values in the given order without any other charvalues between them.
   */
  public SELF containsSequence(Iterable<? extends CharSequence> values) {
    strings.assertContainsSequence(info, actual, toArray(values, CharSequence.class));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the given values <b>in the given order,
   * possibly with other values between them</b>.
   * <p>
   * Example:
   * <pre><code class='java'> String book = &quot;{ 'title':'A Game of Thrones', 'author':'George Martin'}&quot;;
   *
   * // this assertion succeeds
   * assertThat(book).containsSubsequence(&quot;'title'&quot;, &quot;:&quot;, &quot;'A Game of Thrones'&quot;);
   *
   * // these ones succeed even if there are values between the given values
   * assertThat(book).containsSubsequence(&quot;{&quot;, &quot;A Game of Thrones&quot;, &quot;George Martin&quot;, &quot;}&quot;);
   * assertThat(book).containsSubsequence(&quot;A&quot;, &quot;Game&quot;, &quot;of&quot;, &quot;George&quot;);
   *
   * // this one fails as &quot;author&quot; must come after &quot;A Game of Thrones&quot;
   * assertThat(book).containsSubsequence(&quot;{&quot;, &quot;author&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;);</code></pre>
   *
   * @param values the Strings to look for, in order.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given values in the given order.
   */
  public SELF containsSubsequence(CharSequence... values) {
    strings.assertContainsSubsequence(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the values of the given Iterable <b>in the Iterable
   * iteration order (possibly with other values between them)</b>.
   * <p>
   * Example:
   * <pre><code class='java'> String book = &quot;{ 'title':'A Game of Thrones', 'author':'George Martin'}&quot;;
   *
   * // this assertion succeeds
   * assertThat(book).containsSubsequence(asList(&quot;'title'&quot;, &quot;:&quot;, &quot;'A Game of Thrones'&quot;));
   *
   * // these ones succeed even if there are values between the given values
   * assertThat(book).containsSubsequence(asList(&quot;{&quot;, &quot;A Game of Thrones&quot;, &quot;George Martin&quot;, &quot;}&quot;));
   * assertThat(book).containsSubsequence(asList(&quot;A&quot;, &quot;Game&quot;, &quot;of&quot;, &quot;George&quot;));
   *
   * // but this one fails as &quot;author&quot; must come after &quot;A Game of Thrones&quot;
   * assertThat(book).containsSubsequence(asList(&quot;{&quot;, &quot;author&quot;, &quot;A Game of Thrones&quot;, &quot;}&quot;));</code></pre>
   *
   * @param values the Strings to look for, in order.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given values in the given order.
   */
  public SELF containsSubsequence(Iterable<? extends CharSequence> values) {
    strings.assertContainsSubsequence(info, actual, toArray(values, CharSequence.class));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} contains the given sequence, ignoring case considerations.
   * <p>
   * Example:
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
   * Verifies that the actual {@code CharSequence} contains all the given values, ignoring whitespace differences.
   * <p>
   * You can use one or several {@code CharSequence}s as in this example:
   * <pre><code class='java'> // assertions succeed:
   * assertThat(&quot;Gandalf the grey&quot;).containsIgnoringWhitespaces(&quot;alf&quot;)
   *                               .containsIgnoringWhitespaces(&quot;alf&quot;, &quot;grey&quot;)
   *                               .containsIgnoringWhitespaces(&quot;thegrey&quot;)
   *                               .containsIgnoringWhitespaces(&quot;thegr  ey&quot;)
   *                               .containsIgnoringWhitespaces(&quot;t h e g r\t\r\n ey&quot;);
   * // assertion fails:
   * assertThat(&quot;Gandalf the grey&quot;).containsIgnoringWhitespaces(&quot;alF&quot;)</code></pre>
   *
   * @param values the values to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given values.
   */
  public SELF containsIgnoringWhitespaces(CharSequence... values) {
    assertContainsIgnoringWhitespaces(values);
    return myself;
  }

  private void assertContainsIgnoringWhitespaces(CharSequence... values) {
    doCommonCheckForCharSequence(info, actual, values);
    String actualWithoutWhitespace = removeAllWhitespaces(actual);
    Set<CharSequence> notFound = stream(values).map(Strings::removeAllWhitespaces)
                                               .filter(value -> !objects.getComparisonStrategy()
                                                                        .stringContains(actualWithoutWhitespace, value))
                                               .collect(toCollection(LinkedHashSet::new));
    if (notFound.isEmpty()) return;
    if (values.length == 1) {
      throw assertionError(shouldContainIgnoringWhitespaces(actual, values[0], objects.getComparisonStrategy()));
    }
    throw assertionError(shouldContainIgnoringWhitespaces(actual, values, notFound, objects.getComparisonStrategy()));
  }

  /**
   * Verifies that the actual {@code CharSequence} contains all the given values ignoring new line differences.
   * <p>
   * You can use one or several {@code CharSequence}s as in this example:
   *
   * <pre><code class='java'> // assertions succeed:
   * assertThat(&quot;Gandalf\nthe\ngrey&quot;).containsIgnoringNewLines(&quot;alf&quot;)
   *                                 .containsIgnoringNewLines(&quot;alf&quot;, &quot;grey&quot;)
   *                                 .containsIgnoringNewLines(&quot;thegrey&quot;)
   *                                 .containsIgnoringNewLines(&quot;thegr\ney&quot;)
   *                                 .containsIgnoringNewLines(&quot;t\nh\ne\ng\nr\ney&quot;);
   * // assertions fail:
   * assertThat(&quot;Gandalf\nthe\ngrey&quot;).containsIgnoringNewLines(&quot;alF&quot;)
   * assertThat(&quot;Gandalf\nthe\ngrey&quot;).containsIgnoringNewLines(&quot;t\nh\ne\ng\nr\t\r\ney&quot;)</code></pre>
   * 
   * @param values the values to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not contain all the given values.
   */
  public SELF containsIgnoringNewLines(final CharSequence... values) {
    strings.assertContainsIgnoringNewLines(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain any of the given values.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;pippin&quot;)
   *                    .doesNotContain(&quot;fro&quot;, &quot;sam&quot;);
   *
   *
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotContain(&quot;Fro&quot;, &quot;Gimli&quot;, &quot;Legolas&quot;);</code></pre>
   *
   * @param values the CharSequences to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} contains any of the given values.
   */
  public SELF doesNotContain(CharSequence... values) {
    strings.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain any of the given Iterable.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotContain(Arrays.asList(&quot;&quot;))
   *                    .doesNotContain(Arrays.asList(&quot;fro&quot;, &quot;sam&quot;));
   *
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).doesNotContain(Arrays.asList(&quot;Fro&quot;, &quot;Gimli&quot;, &quot;Legolas&quot;));</code></pre>
   *
   * @param values the CharSequences to search for.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} contains any of the given values.
   */
  public SELF doesNotContain(Iterable<? extends CharSequence> values) {
    strings.assertDoesNotContain(info, actual, toArray(values, CharSequence.class));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain any of the given values, ignoring case considerations.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;Frodo&quot;).doesNotContainIgnoringCase(&quot;pippin&quot;)
   *                    .doesNotContainIgnoringCase(&quot;Merry&quot;, &quot;sam&quot;);
   *
   * // assertions will fail
   * assertThat(&quot;Frodo&quot;).doesNotContainIgnoringCase(&quot;Fro&quot;, &quot;Gimli&quot;, &quot;Legolas&quot;);
   * assertThat(&quot;Frodo&quot;).doesNotContainIgnoringCase(&quot;fro&quot;); </code></pre>
   *
   * @param values the CharSequences to search for.
   * @return {@code this} assertion object.
   *
   * @throws NullPointerException if the given list of values is {@code null}.
   * @throws NullPointerException if any one of the given values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} contains any one of the given values, ignoring case considerations.
   *
   * @since 3.17.0
   */
  public SELF doesNotContainIgnoringCase(CharSequence... values) {
    strings.assertDoesNotContainIgnoringCase(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain the given regular expression.
   * <p>
   * Example:
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
   * Example:
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
   * Example:
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
   * Verifies that the actual {@code CharSequence} starts with the given prefix, ignoring case considerations.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;Gandalf the grey&quot;).startsWithIgnoringCase(&quot;Gandalf&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).startsWithIgnoringCase(&quot;gandalf&quot;);
   *
   * // assertion will fail
   * assertThat(&quot;Gandalf the grey&quot;).startsWithIgnoringCase(&quot;grey&quot;);</code></pre>
   *
   * @param prefix the given prefix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given prefix is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not start with the given prefix, ignoring case.
   * @since 3.23.0
   */
  public SELF startsWithIgnoringCase(CharSequence prefix) {
    strings.assertStartsWithIgnoringCase(info, actual, prefix);
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
   * Verifies that the actual {@code CharSequence} does not start with the given prefix, ignoring case considerations.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;Gandalf the grey&quot;).doesNotStartWithIgnoringCase(&quot;fro&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).doesNotStartWithIgnoringCase(&quot;grey&quot;);
   *
   * // assertions will fail
   * assertThat(&quot;Gandalf the grey&quot;).doesNotStartWithIgnoringCase(&quot;Gandalf&quot;);
   * assertThat(&quot;Gandalf the grey&quot;).doesNotStartWithIgnoringCase(&quot;gandalf&quot;);</code></pre>
   *
   * @param prefix the given prefix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given prefix is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} starts with the given prefix, ignoring case.
   * @since 3.23.0
   */
  public SELF doesNotStartWithIgnoringCase(CharSequence prefix) {
    strings.assertDoesNotStartWithIgnoringCase(info, actual, prefix);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} ends with the given suffix, ignoring case considerations.
   * <p>
   * Example:
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
   * Verifies that the actual {@code CharSequence} ends with the given suffix, ignoring case considerations.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;Frodo&quot;).endsWithIgnoringCase(&quot;do&quot;);
   * assertThat(&quot;Frodo&quot;).endsWithIgnoringCase(&quot;DO&quot;);
   *
   * // assertion will fail
   * assertThat(&quot;Frodo&quot;).endsWithIgnoringCase(&quot;Fro&quot;);</code></pre>
   *
   * @param suffix the given suffix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given suffix is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not end with the given suffix, ignoring case.
   * @since 3.23.0
   */
  public SELF endsWithIgnoringCase(CharSequence suffix) {
    strings.assertEndsWithIgnoringCase(info, actual, suffix);
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
   * Verifies that the actual {@code CharSequence} does not end with the given suffix, ignoring case considerations.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;Frodo&quot;).doesNotEndWithIgnoringCase(&quot;Fro&quot;);
   *
   * // assertions will fail
   * assertThat(&quot;Frodo&quot;).doesNotEndWithIgnoringCase(&quot;do&quot;);
   * assertThat(&quot;Frodo&quot;).doesNotEndWithIgnoringCase(&quot;DO&quot;);</code></pre>
   *
   * @param suffix the given suffix.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given suffix is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} ends with the given suffix, ignoring case.
   * @since 3.23.0
   */
  public SELF doesNotEndWithIgnoringCase(CharSequence suffix) {
    strings.assertDoesNotEndWithIgnoringCase(info, actual, suffix);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} matches the given regular expression.
   * <p>
   * Example:
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
   * Verifies that the actual {@code CharSequence} matches the given regular expression pattern, then accepts the given
   * {@code Consumer<Matcher>} to do further verification on the matcher.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(&quot;Frodo&quot;).matchesSatisfying(&quot;..(o.o)&quot;, matcher -&gt; assertThat(matcher.group(1)).isEqualTo(&quot;odo&quot;)); </code></pre>
   *
   * @param regex the regular expression to which the actual {@code CharSequence} is to be matched.
   * @param matchSatisfies a consumer of the found match to do further verification
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not match the given regular expression.
   */
  public SELF matchesSatisfying(CharSequence regex, Consumer<Matcher> matchSatisfies) {
    return internalMatchesSatisfying(Pattern.compile(regex.toString()), matchSatisfies);
  }

  /**
   * Verifies that the actual {@code CharSequence} does not match the given regular expression.
   * <p>
   * Example:
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
   * Example:
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
   * Verifies that the actual {@code CharSequence} matches the given regular expression pattern, then accepts the given
   * {@code Consumer<Matcher>} to do further verification on the matcher.
   * <p>
   * Example:
   * <pre><code class='java'> Pattern pattern = Pattern.compile("..(o.o)");
   *
   * // assertion succeeds:
   * assertThat(&quot;Frodo&quot;).matchesSatisfying(pattern, matcher -&gt; assertThat(matcher.group(1)).isEqualTo(&quot;odo&quot;)); </code></pre>
   *
   * @param pattern the regular expression to which the actual {@code CharSequence} is to be matched.
   * @param matchSatisfies a consumer of the found match to do further verification
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} does not match the given regular expression.
   */
  public SELF matchesSatisfying(Pattern pattern, Consumer<Matcher> matchSatisfies) {
    return internalMatchesSatisfying(pattern, matchSatisfies);
  }

  // internal method to avoid double proxying if one assertion calls another one
  private SELF internalMatchesSatisfying(Pattern pattern, Consumer<Matcher> matchSatisfies) {
    Matcher matcher = pattern.matcher(actual);
    strings.assertMatches(info, actual, matcher);
    matchSatisfies.accept(matcher);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not match the given regular expression pattern.
   * <p>
   * Example:
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
   * This assertion has some limitations, for example it does not handle tab vs space and would fail if elements are the same but
   * in a different order.<br>
   * The recommended approach is <a href="https://github.com/xmlunit/user-guide/wiki">XML Unit</a> which is able to deal with
   * these limitations and provides many more features like XPath support and schema validation.
   * <p>
   * Original javadoc:
   * <p>
   * Verifies that the actual {@code CharSequence} is equal to the given XML {@code CharSequence} after both have been
   * formatted the same way.
   * <p>
   * Example:
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
   * // No matter how your xml string is formatted, isXmlEqualTo is able to compare it's content with another xml String.
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
   * @see <a href="https://github.com/xmlunit/user-guide/wiki">XML Unit</a>
   * @see <a href="https://github.com/xmlunit/user-guide/wiki/Providing-Input-to-XMLUnit">XML Unit XML source input</a>
   *
   * @deprecated
   */
  @Deprecated
  public SELF isXmlEqualTo(CharSequence expectedXml) {
    strings.assertXmlEqualsTo(info, actual, expectedXml);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the content of the given file.
   * <p>
   * This is an handy shortcut that calls : {@code isXmlEqualTo(contentOf(xmlFile))}
   * </p>
   * Example:
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
  @Deprecated
  public SELF isXmlEqualToContentOf(File xmlFile) {
    isXmlEqualTo(contentOf(xmlFile));
    return myself;
  }

  /**
   * Do not use this method.
   *
   * @throws UnsupportedOperationException if this method is called.
   *
   * @deprecated Custom element Comparator is not supported for CharSequence comparison.
   */
  @Override
  @Deprecated
  public final SELF usingElementComparator(Comparator<? super Character> customComparator) {
    throw new UnsupportedOperationException("custom element Comparator is not supported for CharSequence comparison");
  }

  /**
   * Do not use this method.
   *
   * @throws UnsupportedOperationException if this method is called.
   *
   * @deprecated Custom element Comparator is not supported for CharSequence comparison.
   */
  @Override
  @Deprecated
  public final SELF usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for CharSequence comparison");
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator, String customComparatorDescription) {
    this.strings = new Strings(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    this.strings = Strings.instance();
    return super.usingDefaultComparator();
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
   * Example:
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
   * <li>any remaining whitespace (including non-breaking spaces), appearing within either string, is collapsed to a single space before comparison</li>
   * </ul>
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat("Game of Thrones").isEqualToNormalizingWhitespace("Game   of   Thrones")
   *                              .isEqualToNormalizingWhitespace("Game of     Thrones")
   *                              .isEqualToNormalizingWhitespace("Game     of Thrones")
   *                              .isEqualToNormalizingWhitespace("  Game of Thrones  ")
   *                              .isEqualToNormalizingWhitespace("  Game of   Thrones  ")
   *                              .isEqualToNormalizingWhitespace("Game of\tThrones")
   *                              .isEqualToNormalizingWhitespace("Game of Thrones");
   *                              .isEqualToNormalizingWhitespace("Game\u00A0of Thrones");
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
   * <li>any remaining whitespace (including non-breaking spaces), appearing within either string, is collapsed to a single space before comparison</li>
   * </ul>
   * <p>
   * Example:
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
   * Verifies that the actual {@code CharSequence} is equal to the given one, after the punctuation
   * of both strings has been normalized.
   * <p>
   * To be exact, the following rules are applied:
   * <ul>
   * <li>All punctuation of actual and expected strings are ignored and whitespaces (including non-breaking spaces) are normalized</li>
   * <li>Punctuation is any of the following character <b>{@code !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~}</b></li>
   * </ul>
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat("Game'of'Thrones").isEqualToNormalizingPunctuationAndWhitespace("GameofThrones")
   * assertThat("Game of Throne's").isEqualToNormalizingPunctuationAndWhitespace("Game of Thrones")
   * assertThat(":Game of Thrones:").isEqualToNormalizingPunctuationAndWhitespace("Game of Thrones")
   * assertThat(":Game-of-Thrones:").isEqualToNormalizingPunctuationAndWhitespace("Game of Thrones")
   * assertThat("Game of Thrones?").isEqualToNormalizingPunctuationAndWhitespace("Game of Thrones")
   * assertThat("Game of Thrones!!!").isEqualToNormalizingPunctuationAndWhitespace("Game of Thrones")
   * assertThat("Game of  {{(!)}}    Thrones!!!").isEqualToNormalizingPunctuationAndWhitespace("Game of Thrones")
   * assertThat("{(Game)-(of)-(Thrones)!!!}").isEqualToNormalizingPunctuationAndWhitespace("GameofThrones");
   *
   * // assertions will fail
   * assertThat("Game-of-Thrones").isEqualToNormalizingPunctuationAndWhitespace("Game of Thrones");
   * assertThat("{Game:of:Thrones}").isEqualToNormalizingPunctuationAndWhitespace("Game of Thrones");
   * assertThat("{(Game)-(of)-(Thrones)!!!}").isEqualToNormalizingPunctuationAndWhitespace("Game of Thrones");</code></pre>
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not equal to the given one
   *           after punctuation have been normalized.
   * @since 3.16.0
   */
  public SELF isEqualToNormalizingPunctuationAndWhitespace(CharSequence expected) {
    strings.assertEqualsNormalizingPunctuationAndWhitespace(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is a substring of the given one (opposite assertion of {@link #contains(CharSequence...) contains(CharSequence cs)}.
   * <p>
   * Example:
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
   * Example:
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
   * Verifies that the actual {@code CharSequence} contains the given regular expression pattern, then accepts the given
   * {@code Consumer<Matcher>} for further verification on the matcher.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(&quot;Frodo&quot;).containsPatternSatisfying(&quot;.o(.o)&quot;, matcher -&gt; assertThat(matcher.group(1)).isEqualTo(&quot;do&quot;)); </code></pre>
   *
   * @param regex the regular expression to find in the actual {@code CharSequence}.
   * @param matchSatisfies a consumer for further verifying the Matcher.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws PatternSyntaxException if the regular expression's syntax is invalid.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given regular expression cannot be found in the actual {@code CharSequence}.
   */
  public SELF containsPatternSatisfying(CharSequence regex, Consumer<Matcher> matchSatisfies) {
    return internalContainsPatternSatisfying(Pattern.compile(regex.toString()), matchSatisfies);
  }

  /**
   * Verifies that the actual {@code CharSequence} contains the given regular expression pattern.
   * <p>
   * Example:
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
   * Verifies that the actual {@code CharSequence} contains the given regular expression pattern, then accepts the given
   * {@code Consumer<Matcher>} for further verification on the matcher.
   * <p>
   * Example :
   * <pre><code class='java'> Pattern pattern = Pattern.compile(&quot;.o(.o)&quot;);
   *
   * // assertion succeeds:
   * assertThat(&quot;Frodo&quot;).containsPatternSatisfying(pattern, matcher -&gt; assertThat(matcher.group(1)).isEqualTo(&quot;do&quot;)); </code></pre>
   *
   * @param pattern the regular expression to find in the actual {@code CharSequence}.
   * @param matchSatisfies a consumer for further verifying the Matcher.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError if the given regular expression cannot be found in the actual {@code CharSequence}.
   */
  public SELF containsPatternSatisfying(Pattern pattern, Consumer<Matcher> matchSatisfies) {
    return internalContainsPatternSatisfying(pattern, matchSatisfies);
  }

  private SELF internalContainsPatternSatisfying(Pattern pattern, Consumer<Matcher> matchSatisfies) {
    Matcher matcher = pattern.matcher(actual);
    strings.assertContainsPattern(info, actual, matcher);
    matchSatisfies.accept(matcher);
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

  /**
   * Verifies that the actual {@code CharSequence} is equal to the given one after both strings new lines (\n, \r\n) have been removed.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat("Some textWith new lines").isEqualToIgnoringNewLines("Some text\nWith new lines")
   *                                      .isEqualToIgnoringNewLines("Some text\r\nWith new lines")
   *                                      .isEqualToIgnoringNewLines("Some text\n\nWith new lines");
   *
   * assertThat("Some text\nWith new lines").isEqualToIgnoringNewLines("Some text\nWith new lines")
   *                                        .isEqualToIgnoringNewLines("Some text\r\nWith new lines")
   *                                        .isEqualToIgnoringNewLines("Some text\n\nWith new lines");
   *
   * // assertions will fail
   * assertThat("Some text\nWith new lines").isEqualToIgnoringNewLines("Some text With new lines");
   * assertThat("Some text\r\nWith new lines").isEqualToIgnoringNewLines("Some text With new lines");</code></pre>
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not equal to the given one after new lines have been removed.
   */
  public SELF isEqualToIgnoringNewLines(CharSequence expected) {
    strings.assertIsEqualToIgnoringNewLines(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is a lowercase {@code CharSequence} by comparing it to
   * a lowercase {@code actual} built with {@link String#toLowerCase()}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;lego&quot;).isLowerCase();
   * assertThat("").isLowerCase();
   * assertThat(" ").isLowerCase();
   * assertThat(".").isLowerCase();
   * assertThat("7").isLowerCase();
   * assertThat("a.7").isLowerCase();
   *
   * // assertions will fail
   * assertThat(&quot;Lego&quot;).isLowerCase();
   * assertThat(&quot;LEGO&quot;).isLowerCase();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not lowercase.
   * @see #isMixedCase()
   * @see #isUpperCase()
   */
  public SELF isLowerCase() {
    strings.assertLowerCase(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is a mixed case {@code CharSequence}, i.e.,
   * neither uppercase nor lowercase.
   * <p>
   * If actual is empty or contains only case-independent characters, the assertion will pass.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;Capitalized&quot;).isMixedCase();
   * assertThat(&quot;camelCase&quot;).isMixedCase();
   * assertThat(&quot;rAndOMcAse1234&quot;).isMixedCase();
   * assertThat(&quot;1@3$567&quot;).isMixedCase();
   * assertThat(&quot;&quot;).isMixedCase();
   *
   * // assertions will fail
   * assertThat(&quot;I AM GROOT!&quot;).isMixedCase();
   * assertThat(&quot;please be quiet&quot;).isMixedCase();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not mixed case.
   * @see #isLowerCase()
   * @see #isUpperCase()
   * @since 3.21.0
   */
  public SELF isMixedCase() {
    strings.assertMixedCase(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is an uppercase {@code CharSequence} by comparing it to
   * an uppercase {@code actual} built with {@link String#toUpperCase()}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;LEGO&quot;).isUpperCase();
   * assertThat("").isUpperCase();
   * assertThat(" ").isUpperCase();
   * assertThat(".").isUpperCase();
   * assertThat("7").isUpperCase();
   * assertThat("A.7").isUpperCase();
   *
   * // assertions will fail
   * assertThat(&quot;Lego&quot;).isUpperCase();
   * assertThat(&quot;lego&quot;).isUpperCase();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not uppercase.
   * @see #isLowerCase()
   * @see #isMixedCase()
   */
  public SELF isUpperCase() {
    strings.assertUpperCase(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the given one after they have been normalized
   * according to the {@link Normalizer.Form#NFC} form, which is a canonical decomposition followed by canonical composition.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed:
   *
   * // Ä = &#92;u00C4 - Ä = &#92;u0041&#92;u0308
   * assertThat(&quot;Ä&quot;).isEqualToNormalizingUnicode(&quot;Ä&quot;);
   * assertThat(&quot;&#92;u00C4&quot;).isEqualToNormalizingUnicode(&quot;&#92;u0041&#92;u0308&quot;);
   *
   * // Amélie = u0041&#92;u006d&#92;u00e9&#92;u006c&#92;u0069&#92;u0065 - Amélie = &#92;u0041&#92;u006d&#92;u0065&#92;u0301&#92;u006c&#92;u0069&#92;u0065
   * assertThat(&quot;Amélie&quot;).isEqualToNormalizingUnicode(&quot;Amélie&quot;);
   *
   * // assertions fail:
   * assertThat(&quot;ñ&quot;).isEqualToNormalizingUnicode(&quot;n&quot;);
   * assertThat(&quot;Ä&quot;).isEqualToNormalizingUnicode(&quot;b&quot;);</code></pre>
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not equal to the given one
   *           after both strings have been normalized to according to the {@link Normalizer.Form#NFC} form .
   * @throws NullPointerException if the actual {@code CharSequence} is not null and the given is.
   * @since 3.19.0
   */
  public SELF isEqualToNormalizingUnicode(CharSequence expected) {
    strings.assertEqualsToNormalizingUnicode(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is alphabetic by checking it against the {@code \p{Alpha}+} regex pattern
   * POSIX character classes (US-ASCII only).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;lego&quot;).isAlphabetic();
   * assertThat(&quot;a&quot;).isAlphabetic();
   * assertThat(&quot;Lego&quot;).isAlphabetic();
   *
   * // assertions will fail
   * assertThat(&quot;1&quot;).isAlphabetic();
   * assertThat(&quot; &quot;).isAlphabetic();
   * assertThat(&quot;&quot;).isAlphabetic();
   * assertThat(&quot;L3go&quot;).isAlphabetic();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not alphabetic.
   * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html">java.util.regex.Pattern</a>
   */
  public SELF isAlphabetic() {
    isNotNull();
    if (!Pattern.matches("\\p{Alpha}+", actual)) throwAssertionError(shouldBeAlphabetic(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is alphanumeric by checking it against the {@code \p{Alnum}+} regex pattern
   * POSIX character classes (US-ASCII only).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;lego&quot;).isAlphanumeric();
   * assertThat(&quot;a1&quot;).isAlphanumeric();
   * assertThat(&quot;L3go&quot;).isAlphanumeric();
   *
   * // assertions will fail
   * assertThat(&quot;!&quot;).isAlphanumeric();
   * assertThat(&quot;&quot;).isAlphanumeric();
   * assertThat(&quot; &quot;).isAlphanumeric();
   * assertThat(&quot;L3go!&quot;).isAlphanumeric();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not alphanumeric.
   * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html">java.util.regex.Pattern</a>
   */
  public SELF isAlphanumeric() {
    isNotNull();
    if (!Pattern.matches("\\p{Alnum}+", actual)) throwAssertionError(shouldBeAlphanumeric(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is ASCII by checking it against the {@code \p{ASCII}+} regex pattern
   * POSIX character classes (US-ASCII only).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;lego&quot;).isASCII();
   * assertThat(&quot;a1&quot;).isASCII();
   * assertThat(&quot;L3go&quot;).isASCII();
   *
   * // assertions will fail
   * assertThat(&quot;&quot;).isASCII();
   * assertThat(&quot;♪&quot;).isASCII();
   * assertThat(&quot;\u2303&quot;).isASCII();
   * assertThat(&quot;L3go123\u230300abc&quot;).isASCII();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not ASCII.
   * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html">java.util.regex.Pattern</a>
   */
  public SELF isASCII() {
    isNotNull();
    if (!Pattern.matches("\\p{ASCII}+", actual)) throwAssertionError(shouldBeASCII(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is hexadecimal by checking it against the {@code \p{XDigit}+} regex pattern
   * POSIX character classes (US-ASCII only).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;A&quot;).isHexadecimal();
   * assertThat(&quot;2&quot;).isHexadecimal();
   *
   * // assertions will fail
   * assertThat(&quot;!&quot;).isHexadecimal();
   * assertThat(&quot;&quot;).isHexadecimal();
   * assertThat(&quot; &quot;).isHexadecimal();
   * assertThat(&quot;Z&quot;).isHexadecimal();
   * assertThat(&quot;L3go!&quot;).isHexadecimal();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not hexadecimal.
   * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html">java.util.regex.Pattern</a>
   */
  public SELF isHexadecimal() {
    isNotNull();
    if (!Pattern.matches("\\p{XDigit}+", actual)) throwAssertionError(shouldBeHexadecimal(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is printable by checking it against the {@code \p{Print}+} regex pattern
   * POSIX character classes (US-ASCII only).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;2&quot;).isPrintable();
   * assertThat(&quot;a&quot;).isPrintable();
   * assertThat(&quot;~&quot;).isPrintable();
   * assertThat(&quot;&quot;).isPrintable();
   *
   * // assertions will fail
   * assertThat(&quot;\t&quot;).isPrintable();
   * assertThat(&quot;§&quot;).isPrintable();
   * assertThat(&quot;©&quot;).isPrintable();
   * assertThat(&quot;\n&quot;).isPrintable();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not printable.
   * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html">java.util.regex.Pattern</a>
   */
  public SELF isPrintable() {
    isNotNull();
    if (!Pattern.matches("\\p{Print}+", actual)) throwAssertionError(shouldBePrintable(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} is visible by checking it against the {@code \p{Graph}+} regex pattern
   * POSIX character classes (US-ASCII only).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;2&quot;).isVisible();
   * assertThat(&quot;a&quot;).isVisible();
   * assertThat(&quot;.&quot;).isVisible();
   *
   * // assertions will fail
   * assertThat(&quot;\t&quot;).isVisible();
   * assertThat(&quot;\n&quot;).isVisible();
   * assertThat(&quot;&quot;).isVisible();
   * assertThat(&quot; &quot;).isVisible();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not visible.
   * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html">java.util.regex.Pattern</a>
   */
  public SELF isVisible() {
    isNotNull();
    if (!Pattern.matches("\\p{Graph}+", actual)) throwAssertionError(shouldBeVisible(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not start with whitespaces (as per
   * {@link Character#isWhitespace(int)} definition), it also checks it is not null as a prerequisite.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed:
   * assertThat(&quot;abc&quot;).doesNotStartWithWhitespaces();
   * assertThat(&quot;abc  &quot;).doesNotStartWithWhitespaces();
   * assertThat(&quot;abc\t\t&quot;).doesNotStartWithWhitespaces();
   * assertThat(&quot;&quot;).doesNotStartWithWhitespaces();
   *
   * // assertions fail:
   * assertThat(&quot;  abc&quot;).doesNotStartWithWhitespaces();
   * assertThat(&quot;  abc  &quot;).doesNotStartWithWhitespaces();
   * assertThat(&quot;\r\nabc&quot;).doesNotStartWithWhitespaces();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} starts with whitespace or is null.
   * @see Character#isWhitespace(int)
   * @since 3.26.0
   */
  public SELF doesNotStartWithWhitespaces() {
    isNotNull();
    if (actual.length() > 0 && isWhitespace(actual.codePoints().findFirst().getAsInt()))
      throwAssertionError(shouldNotStartWithWhitespaces(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code CharSequence} does not end with whitespaces (as per
   * {@link Character#isWhitespace(int)} definition), it also checks it is not null as a prerequisite.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed:
   * assertThat(&quot;abc&quot;).doesNotEndWithWhitespaces();
   * assertThat(&quot;  abc&quot;).doesNotEndWithWhitespaces();
   * assertThat(&quot;\t\tabc&quot;).doesNotEndWithWhitespaces();
   * assertThat(&quot;&quot;).doesNotEndWithWhitespaces();
   *
   * // assertions fail:
   * assertThat(&quot;abc  &quot;).doesNotEndWithWhitespaces();
   * assertThat(&quot;  abc  &quot;).doesNotEndWithWhitespaces();
   * assertThat(&quot;abc\r\n&quot;).doesNotEndWithWhitespaces();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} ends with whitespace or is null.
   * @see Character#isWhitespace(int)
   * @since 3.26.0
   */
  public SELF doesNotEndWithWhitespaces() {
    isNotNull();
    if (actual.length() > 0 && Character.isWhitespace(actual.codePoints().reduce((v1, v2) -> v2).getAsInt()))
      throwAssertionError(shouldNotEndWithWhitespaces(actual));
    return myself;
  }

  private static boolean isBlank(CharSequence actual) {
    return isNullOrEmpty(actual) || strictlyContainsWhitespaces(actual);
  }

  private static boolean containsOnlyWhitespaces(CharSequence actual) {
    return !isNullOrEmpty(actual) && strictlyContainsWhitespaces(actual);
  }

  private static boolean containsWhitespaces(CharSequence actual) {
    return !isNullOrEmpty(actual) && containsOneOrMoreWhitespaces(actual);
  }

  private static boolean isNullOrEmpty(CharSequence actual) {
    return actual == null || actual.length() == 0;
  }

  private static boolean strictlyContainsWhitespaces(CharSequence actual) {
    return actual.chars().allMatch(Character::isWhitespace);
  }

  private static boolean containsOneOrMoreWhitespaces(CharSequence actual) {
    return actual.chars().anyMatch(Character::isWhitespace);
  }

}
