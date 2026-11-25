/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;
import static java.util.Arrays.stream;
import static java.util.Locale.ROOT;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.error.ShouldBeASCII.shouldBeASCII;
import static org.assertj.core.error.ShouldBeAlphabetic.shouldBeAlphabetic;
import static org.assertj.core.error.ShouldBeAlphanumeric.shouldBeAlphanumeric;
import static org.assertj.core.error.ShouldBeBlank.shouldBeBlank;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeEqualIgnoringNewLineDifferences.shouldBeEqualIgnoringNewLineDifferences;
import static org.assertj.core.error.ShouldBeEqualIgnoringNewLines.shouldBeEqualIgnoringNewLines;
import static org.assertj.core.error.ShouldBeEqualIgnoringWhitespace.shouldBeEqualIgnoringWhitespace;
import static org.assertj.core.error.ShouldBeEqualNormalizingPunctuationAndWhitespace.shouldBeEqualNormalizingPunctuationAndWhitespace;
import static org.assertj.core.error.ShouldBeEqualNormalizingUnicode.shouldBeEqualNormalizingUnicode;
import static org.assertj.core.error.ShouldBeEqualNormalizingWhitespace.shouldBeEqualNormalizingWhitespace;
import static org.assertj.core.error.ShouldBeHexadecimal.shouldBeHexadecimal;
import static org.assertj.core.error.ShouldBeLowerCase.shouldBeLowerCase;
import static org.assertj.core.error.ShouldBeMixedCase.shouldBeMixedCase;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldBePrintable.shouldBePrintable;
import static org.assertj.core.error.ShouldBeSubstring.shouldBeSubstring;
import static org.assertj.core.error.ShouldBeUpperCase.shouldBeUpperCase;
import static org.assertj.core.error.ShouldBeVisible.shouldBeVisible;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldContainCharSequence.containsIgnoringNewLines;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringCase;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringWhitespaces;
import static org.assertj.core.error.ShouldContainCharSequenceOnlyOnce.shouldContainOnlyOnce;
import static org.assertj.core.error.ShouldContainOneOrMoreWhitespaces.shouldContainOneOrMoreWhitespaces;
import static org.assertj.core.error.ShouldContainOnlyDigits.shouldContainOnlyDigits;
import static org.assertj.core.error.ShouldContainOnlyWhitespaces.shouldContainOnlyWhitespaces;
import static org.assertj.core.error.ShouldContainPattern.shouldContainPattern;
import static org.assertj.core.error.ShouldContainSequenceOfCharSequence.shouldContainSequence;
import static org.assertj.core.error.ShouldContainSubsequenceOfCharSequence.shouldContainSubsequence;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldEndWithIgnoringCase.shouldEndWithIgnoringCase;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldHaveSizeGreaterThan.shouldHaveSizeGreaterThan;
import static org.assertj.core.error.ShouldHaveSizeGreaterThanOrEqualTo.shouldHaveSizeGreaterThanOrEqualTo;
import static org.assertj.core.error.ShouldHaveSizeLessThan.shouldHaveSizeLessThan;
import static org.assertj.core.error.ShouldHaveSizeLessThanOrEqualTo.shouldHaveSizeLessThanOrEqualTo;
import static org.assertj.core.error.ShouldMatchPattern.shouldMatch;
import static org.assertj.core.error.ShouldNotBeBlank.shouldNotBeBlank;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringCase.shouldNotBeEqualIgnoringCase;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringWhitespace.shouldNotBeEqualIgnoringWhitespace;
import static org.assertj.core.error.ShouldNotBeEqualNormalizingWhitespace.shouldNotBeEqualNormalizingWhitespace;
import static org.assertj.core.error.ShouldNotContainAnyWhitespaces.shouldNotContainAnyWhitespaces;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContainIgnoringCase;
import static org.assertj.core.error.ShouldNotContainOnlyWhitespaces.shouldNotContainOnlyWhitespaces;
import static org.assertj.core.error.ShouldNotContainPattern.shouldNotContainPattern;
import static org.assertj.core.error.ShouldNotContainSequenceOfCharSequence.shouldNotContainSequence;
import static org.assertj.core.error.ShouldNotContainSubsequenceOfCharSequence.shouldNotContainSubsequence;
import static org.assertj.core.error.ShouldNotEndWith.shouldNotEndWith;
import static org.assertj.core.error.ShouldNotEndWithIgnoringCase.shouldNotEndWithIgnoringCase;
import static org.assertj.core.error.ShouldNotEndWithWhitespaces.shouldNotEndWithWhitespaces;
import static org.assertj.core.error.ShouldNotMatchPattern.shouldNotMatch;
import static org.assertj.core.error.ShouldNotStartWith.shouldNotStartWith;
import static org.assertj.core.error.ShouldNotStartWithIgnoringCase.shouldNotStartWithIgnoringCase;
import static org.assertj.core.error.ShouldNotStartWithWhitespaces.shouldNotStartWithWhitespaces;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.error.ShouldStartWithIgnoringCase.shouldStartWithIgnoringCase;
import static org.assertj.core.internal.Arrays.assertIsArray;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsNull;
import static org.assertj.core.internal.CommonValidations.checkLineCounts;
import static org.assertj.core.internal.CommonValidations.checkSizeBetween;
import static org.assertj.core.internal.CommonValidations.checkSizes;
import static org.assertj.core.internal.CommonValidations.hasSameSizeAsCheck;
import static org.assertj.core.util.IterableUtil.toArray;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.error.ShouldBeEqualIgnoringCase;

/**
 * Base class for all implementations of assertions for {@code CharSequence}s.
 *
 * @param <SELF>   the "self" type of this assertion class. Please read &quot;<a href="https://bit.ly/1IZIRcY"
 *                 target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *                 for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas Francois
 * @author Daniel Weber
 */
public abstract class AbstractCharSequenceAssert<SELF extends AbstractCharSequenceAssert<SELF, ACTUAL>, ACTUAL extends CharSequence>
    extends AbstractAssertWithComparator<SELF, ACTUAL> implements EnumerableAssert<SELF, Character> {

  private static final Set<Character> NON_BREAKING_SPACES = Set.of('\u00A0', '\u2007', '\u202F');
  private static final String PUNCTUATION_REGEX = "\\p{Punct}";

  /**
   * Creates a new character sequence assertion.
   *
   * @param actual the actual value to verify
   * @param selfType the type of the concrete assertion
   */
  protected AbstractCharSequenceAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code CharSequence} is empty, i.e., it has a length of 0, or is {@code null}.
   * <p>
   * If you do not want to accept a {@code null} value, use
   * {@link org.assertj.core.api.AbstractCharSequenceAssert#isEmpty()} instead.
   * <p>
   * Both of these assertions succeeds:
   * <pre><code class='java'> String emptyString = &quot;&quot;
   * assertThat(emptyString).isNullOrEmpty();
   *
   * String nullString = null;
   * assertThat(nullString).isNullOrEmpty();</code></pre>
   * <p>
   * Whereas these assertions fail:
   * <pre><code class='java'> assertThat(&quot;a&quot;).isNullOrEmpty();
   * assertThat(&quot;   &quot;).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code CharSequence} has a non-zero length.
   */
  @Override
  public void isNullOrEmpty() {
    executeAssertion(() -> assertNullOrEmpty());
  }

  /**
   * Verifies that the actual {@code CharSequence} is empty, i.e., it has a length of 0 and is not {@code null}.
   * <p>
   * If you want to accept a {@code null} value as well as a 0 length, use
   * {@link org.assertj.core.api.AbstractCharSequenceAssert#isNullOrEmpty()} instead.
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'> String emptyString = &quot;&quot;
   * assertThat(emptyString).isEmpty();</code></pre>
   * <p>
   * Whereas these assertions fail:
   * <pre><code class='java'> String nullString = null;
   * assertThat(nullString).isEmpty();
   * assertThat(&quot;a&quot;).isEmpty();
   * assertThat(&quot;   &quot;).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual {@code CharSequence} has a non-zero length or is null.
   */
  @Override
  public void isEmpty() {
    executeAssertion(() -> assertEmpty());
  }

  /**
   * Verifies that the actual {@code CharSequence} is not empty, i.e., is not {@code null} and has a length of 1 or
   * more.
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'> String bookName = &quot;A Game of Thrones&quot;
   * assertThat(bookName).isNotEmpty();</code></pre>
   * <p>
   * Whereas these assertions fail:
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
    return executeAssertion(() -> assertNotEmpty());
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
   * <p>
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
    return executeAssertion(this::assertBlank);
  }

  private void assertBlank() {
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
   * <p>
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
    return executeAssertion(this::assertNotBlank);
  }

  private void assertNotBlank() {
    if (isBlank(actual)) throw assertionError(shouldNotBeBlank(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} contains one or more whitespace characters (according to
   * {@link Character#isWhitespace(char)}).
   * <p>
   * These assertions succeed:
   * <pre><code class='java'> assertThat(" ").containsWhitespaces();
   * assertThat("a b").containsWhitespaces();
   * assertThat(" c ").containsWhitespaces();</code></pre>
   * <p>
   * Whereas these assertions fail:
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
    return executeAssertion(this::assertContainsWhitespaces);
  }

  private void assertContainsWhitespaces() {
    if (!containsWhitespaces(actual)) throw assertionError(shouldContainOneOrMoreWhitespaces(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} consists of one or more whitespace characters (according to
   * {@link Character#isWhitespace(char)}).
   * <p>
   * These assertions succeed:
   * <pre><code class='java'> assertThat(" ").containsOnlyWhitespaces();
   * assertThat("    ").containsOnlyWhitespaces();</code></pre>
   * <p>
   * Whereas these assertions fail:
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
    return executeAssertion(this::assertContainsOnlyWhitespaces);
  }

  private void assertContainsOnlyWhitespaces() {
    if (!containsOnlyWhitespaces(actual)) throw assertionError(shouldContainOnlyWhitespaces(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} is either {@code null}, empty or does not contain any whitespace characters (according to {@link Character#isWhitespace(char)}).
   * <p>
   * These assertions succeed:
   * <pre><code class='java'> assertThat("a").doesNotContainAnyWhitespaces();
   * assertThat("").doesNotContainAnyWhitespaces();
   * assertThat("ab").doesNotContainAnyWhitespaces();
   *
   * String nullString = null;
   * assertThat(nullString).doesNotContainAnyWhitespaces();</code></pre>
   * <p>
   * Whereas these assertions fail:
   * <pre><code class='java'> assertThat(" ").doesNotContainAnyWhitespaces();
   * assertThat(" a").doesNotContainAnyWhitespaces();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} contains one or more whitespace characters.
   * @since 3.11.0
   */
  public SELF doesNotContainAnyWhitespaces() {
    return executeAssertion(this::assertDoesNotContainAnyWhitespaces);
  }

  private void assertDoesNotContainAnyWhitespaces() {
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
   * These assertions succeed:
   * <pre><code class='java'> assertThat("a").doesNotContainOnlyWhitespaces();
   * assertThat("").doesNotContainOnlyWhitespaces();
   * assertThat(" b").doesNotContainOnlyWhitespaces();
   * assertThat(" c ").doesNotContainOnlyWhitespaces();
   * String nullString = null;
   * assertThat(nullString).doesNotContainOnlyWhitespaces();</code></pre>
   * <p>
   * Whereas these assertions fail:
   * <pre><code class='java'> assertThat(" ").doesNotContainOnlyWhitespaces();
   * assertThat("    ").doesNotContainOnlyWhitespaces();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is blank.
   * @since 2.9.0 / 3.9.0
   */
  public SELF doesNotContainOnlyWhitespaces() {
    return executeAssertion(this::assertDoesNotContainOnlyWhitespaces);
  }

  private void assertDoesNotContainOnlyWhitespaces() {
    if (containsOnlyWhitespaces(actual)) throw assertionError(shouldNotContainOnlyWhitespaces(actual));
  }

  /**
   * Verifies that the actual {@code CharSequence} has the expected length using the {@code length()} method.
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'> String bookName = &quot;A Game of Thrones&quot;
   * assertThat(bookName).hasSize(17);</code></pre>
   * <p>
   * Whereas this assertion fails:
   * <pre><code class='java'> String bookName = &quot;A Clash of Kings&quot;
   * assertThat(bookName).hasSize(4);</code></pre>
   *
   * @param expected the expected length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is not equal to the expected length.
   */
  @Override
  public SELF hasSize(int expected) {
    return executeAssertion(() -> assertHasSize(expected));
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length less than the given value using the {@code length()} method.
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeLessThan(4);</code></pre>
   * <p>
   * Whereas this assertion fails:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeLessThan(3);</code></pre>
   *
   * @param expected the expected maximum length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is equal or greater than the expected length.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThan(int expected) {
    return executeAssertion(() -> assertHasSizeLessThan(expected));
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length less than or equal to the given value using the {@code length()}  method.
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeLessThanOrEqualTo(3);</code></pre>
   * <p>
   * Whereas this assertion fails:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeLessThanOrEqualTo(2);</code></pre>
   *
   * @param expected the expected maximum length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is greater than the expected length.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThanOrEqualTo(int expected) {
    return executeAssertion(() -> assertHasSizeLessThanOrEqualTo(expected));
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length greater than the given value using the {@code length()}  method.
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'>assertThat(&quot;abcs&quot;).hasSizeGreaterThan(2);</code></pre>
   * <p>
   * Whereas this assertion fails:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeGreaterThan(3);</code></pre>
   *
   * @param expected the expected minimum length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is equal or less than the expected length.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThan(int expected) {
    return executeAssertion(() -> assertHasSizeGreaterThan(expected));
  }

  /**
   * Verifies that the actual {@code CharSequence} has a length greater than or equal to the given value using the {@code length()}  method.
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeGreaterThanOrEqualTo(3);</code></pre>
   * <p>
   * Whereas this assertion fails:
   * <pre><code class='java'>assertThat(&quot;abc&quot;).hasSizeGreaterThanOrEqualTo(3);</code></pre>
   *
   * @param expected the expected minimum length of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is less than the expected length.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThanOrEqualTo(int expected) {
    return executeAssertion(() -> assertHasSizeGreaterThanOrEqualTo(expected));
  }

  /**
   * Verifies that the actual {@code CharSequence} has length between the given boundaries (inclusive)
   * using the {@code length()} method.
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'> String bookName = &quot;A Game of Thrones&quot;
   * assertThat(bookName).hasSizeBetween(5, 25);
   * assertThat(bookName).hasSizeBetween(16, 17);</code></pre>
   * <p>
   * Whereas this assertion fails:
   * <pre><code class='java'> String bookName = &quot;A Clash of Kings&quot;
   * assertThat(bookName).hasSizeBetween(2, 5);</code></pre>
   *
   * @param lowerBoundary  the lower boundary compared to which actual length should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual length should be less than or equal to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual length is not between the boundaries.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeBetween(int lowerBoundary, int higherBoundary) {
    return executeAssertion(() -> assertHasSizeBetween(lowerBoundary, higherBoundary));
  }

  /**
   * Verifies that the actual {@code CharSequence} has the expected line count.
   * <p>
   * A line is considered to be <i>terminated</i> by any one of a line feed ({@code '\n'}), a carriage return ({@code '\r'}),
   * or a carriage return followed immediately by a linefeed (see {@link java.io.LineNumberReader}).
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'> String multiLine = &quot;First line\n&quot; +
   *                    &quot;Last line&quot;;
   * assertThat(multiLine).hasLineCount(2);</code></pre>
   * <p>
   * Whereas this assertion fails:
   * <pre><code class='java'> String bookName = &quot;A Clash of Kings&quot;;
   * assertThat(bookName).hasLineCount(3);</code></pre>
   *
   * @param expectedLineCount the expected line count of the actual {@code CharSequence}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual line count is not equal to the expected one.
   */
  public SELF hasLineCount(int expectedLineCount) {
    return executeAssertion(() -> assertHasLineCount(expectedLineCount));
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
   * @throws AssertionError       if the actual {@code CharSequence} has a length that's different from the length of the
   *                              given {@code CharSequence}.
   * @throws NullPointerException if the given {@code CharSequence} is {@code null}.
   */
  public SELF hasSameSizeAs(CharSequence other) {
    return executeAssertion(() -> assertHasSameSizeAs(other));
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
   * @throws AssertionError       if the actual {@code CharSequence} has a length that's different from the number of elements
   *                              in the array.
   * @throws NullPointerException if the given array is {@code null}.
   */
  @Override
  public SELF hasSameSizeAs(Object other) {
    return executeAssertion(() -> assertHasSameSizeAs(other));
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
   * @throws AssertionError       if the actual {@code CharSequence} has a length that's different from the number of elements
   *                              in the {@code Iterable}.
   * @throws NullPointerException if the given {@code Iterable} is {@code null}.
   */
  @Override
  public SELF hasSameSizeAs(Iterable<?> other) {
    return executeAssertion(() -> assertHasSameSizeAs(other));
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
    return executeAssertion(() -> assertEqualsIgnoringCase(expected));
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
    return executeAssertion(() -> assertNotEqualsIgnoringCase(expected));
  }

  /**
   * Verifies that the actual {@code CharSequence} contains only digits. It fails if it contains non-digit
   * characters or is empty.
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'> assertThat("10").containsOnlyDigits();</code></pre>
   * <p>
   * Whereas these assertions fail:
   * <pre><code class='java'> assertThat("10$").containsOnlyDigits();
   * assertThat("").containsOnlyDigits();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} contains non-digit characters or is {@code null}.
   */
  public SELF containsOnlyDigits() {
    return executeAssertion(() -> assertContainsOnlyDigits());
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
   *                        it more than once.
   */
  public SELF containsOnlyOnce(CharSequence sequence) {
    return executeAssertion(() -> assertContainsOnlyOnce(sequence));
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
   * @throws NullPointerException     if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError           if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the actual {@code CharSequence} does not contain all the given values.
   */
  public SELF contains(CharSequence... values) {
    return executeAssertion(() -> assertContains(values));
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
   * @throws NullPointerException     if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError           if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the actual {@code CharSequence} does not contain any of the given values.
   * @since 3.21.0
   */
  public SELF containsAnyOf(CharSequence... values) {
    return executeAssertion(() -> assertContainsAnyOf(values));
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
   * @throws NullPointerException     if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError           if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the actual {@code CharSequence} does not contain all the given values.
   */
  public SELF contains(Iterable<? extends CharSequence> values) {
    return executeAssertion(() -> assertContains(toArray(values, CharSequence.class)));
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
   * @throws NullPointerException     if the given sequence of charSequence is {@code null}.
   * @throws IllegalArgumentException if the given sequence of charSequence is empty.
   * @throws AssertionError           if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the given {@code CharSequence} does not contain the given sequence of values in the given order without any other values between them.
   */
  public SELF containsSequence(CharSequence... values) {
    return executeAssertion(() -> assertContainsSequence(values));
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
   * @throws NullPointerException     if the given sequence of charSequence is {@code null}.
   * @throws IllegalArgumentException if the given sequence of charSequence is empty.
   * @throws AssertionError           if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the given {@code CharSequence} does not contain the given sequence of values in the given order without any other charvalues between them.
   */
  public SELF containsSequence(Iterable<? extends CharSequence> values) {
    return executeAssertion(() -> assertContainsSequence(toArray(values, CharSequence.class)));
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
   * @throws NullPointerException     if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError           if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the actual {@code CharSequence} does not contain all the given values in the given order.
   */
  public SELF containsSubsequence(CharSequence... values) {
    return executeAssertion(() -> assertContainsSubsequence(values));
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
   * @throws NullPointerException     if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError           if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the actual {@code CharSequence} does not contain all the given values in the given order.
   */
  public SELF containsSubsequence(Iterable<? extends CharSequence> values) {
    return executeAssertion(() -> assertContainsSubsequence(toArray(values, CharSequence.class)));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} does not contain the given one.
   */
  public SELF containsIgnoringCase(CharSequence sequence) {
    return executeAssertion(() -> assertContainsIgnoringCase(sequence));
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
   * @throws NullPointerException     if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError           if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the actual {@code CharSequence} does not contain all the given values.
   */
  public SELF containsIgnoringWhitespaces(CharSequence... values) {
    return executeAssertion(() -> assertContainsIgnoringWhitespaces(values));
  }

  private void assertContainsIgnoringWhitespaces(CharSequence... values) {
    doCommonCheckForCharSequence(values);
    String actualWithoutWhitespace = removeAllWhitespaces(actual);
    Set<CharSequence> notFound = stream(values).map(AbstractCharSequenceAssert::removeAllWhitespaces)
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
   * Verifies that the actual {@code CharSequence} contains all the given values ignoring newline differences.
   * <p>
   * You can use one or several {@code CharSequence}s as in this example:
   *
   * <pre><code class='java'> // assertions succeed:
   * assertThat(&quot;Gandalf\nthe\ngrey&quot;).containsIgnoringNewlines(&quot;alf&quot;)
   *                                 .containsIgnoringNewlines(&quot;alf&quot;, &quot;grey&quot;)
   *                                 .containsIgnoringNewlines(&quot;thegrey&quot;)
   *                                 .containsIgnoringNewlines(&quot;thegr\ney&quot;)
   *                                 .containsIgnoringNewlines(&quot;t\nh\ne\ng\nr\ney&quot;);
   * // assertions fail:
   * assertThat(&quot;Gandalf\nthe\ngrey&quot;).containsIgnoringNewlines(&quot;alF&quot;)
   * assertThat(&quot;Gandalf\nthe\ngrey&quot;).containsIgnoringNewlines(&quot;t\nh\ne\ng\nr\t\r\ney&quot;)</code></pre>
   *
   * @param values the values to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError           if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the actual {@code CharSequence} does not contain all the given values.
   */
  public SELF containsIgnoringNewlines(final CharSequence... values) {
    return executeAssertion(() -> assertContainsIgnoringNewlines(values));
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
   * @throws NullPointerException     if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError           if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the actual {@code CharSequence} contains any of the given values.
   */
  public SELF doesNotContain(CharSequence... values) {
    return executeAssertion(() -> assertDoesNotContain(values));
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
   * @throws NullPointerException     if the given list of values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError           if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the actual {@code CharSequence} contains any of the given values.
   */
  public SELF doesNotContain(Iterable<? extends CharSequence> values) {
    return executeAssertion(() -> assertDoesNotContain(toArray(values, CharSequence.class)));
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain the given sequence;
   * a sequence is defined as a group of values arranged <b>in the given order without any other values
   * between them</b>.
   * <p>
   * Example:
   * <pre><code class='java'>{@literal  String book = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
   *
   * // assertions will pass
   * assertThat(book).doesNotContainSequence("A Game of ", "Shadows");
   * assertThat(book).doesNotContainSequence("A Game of ", "George Martin");
   *
   * // assertions will fail
   * assertThat(book).doesNotContainSequence("A Game of ", "Thrones");}</code></pre>
   *
   * @param sequence the sequence to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError           if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the given {@code CharSequence} contains the given values in the given order
   *                                  without any characters between them.
   */
  public SELF doesNotContainSequence(CharSequence... sequence) {
    return executeAssertion(() -> assertDoesNotContainSequence(sequence));
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain the given sequence;
   * a sequence is defined as a group of values arranged <b>in the given order without any other values
   * between them</b>.
   * <p>
   * Example:
   * <pre><code class='java'>{@literal  String book = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
   *
   * // assertions will pass
   * assertThat(book).doesNotContainSequence(asList("A Game of ", "Shadows"));
   * assertThat(book).doesNotContainSequence(asList("A Game of ", "George Martin"));
   *
   * // assertions will fail
   * assertThat(book).doesNotContainSequence(asList("A Game of ", "Thrones"));}</code></pre>
   *
   * @param sequence the sequence to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError           if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the given {@code CharSequence} contains the given values in the given order
   *                                  without any characters between them.
   */
  public SELF doesNotContainSequence(Iterable<? extends CharSequence> sequence) {
    return executeAssertion(() -> assertDoesNotContainSequence(toArray(sequence, CharSequence.class)));
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain the given subsequence;
   * a subsequence is defined as a group of values arranged <b>in the given order, possibly with some values
   * between them</b>.
   * <p>
   * Example:
   * <pre><code class='java'>{@literal  String book = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
   *
   * // assertions will pass
   * assertThat(book).doesNotContainSubsequence("A Game of ", "Shadows");
   *
   * // assertions will fail
   * assertThat(book).doesNotContainSubsequence("A Game of ", "George Martin");
   * assertThat(book).doesNotContainSubsequence("A Game of ", "Thrones");}</code></pre>
   *
   * @param subsequence the subsequence to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError           if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the given {@code CharSequence} contains the given values in the given order,
   *                                  possibly with other characters between them.
   */
  public SELF doesNotContainSubsequence(CharSequence... subsequence) {
    return executeAssertion(() -> assertDoesNotContainSubsequence(subsequence));
  }

  /**
   * Verifies that the actual {@code CharSequence} does not contain the given subsequence;
   * a subsequence is defined as a group of values arranged <b>in the given order, possibly with some values
   * between them</b>.
   * <p>
   * Example:
   * <pre><code class='java'>{@literal  String book = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";
   *
   * // assertions will pass
   * assertThat(book).doesNotContainSubsequence(asList("A Game of ", "Shadows"));
   *
   * // assertions will fail
   * assertThat(book).doesNotContainSubsequence(asList("A Game of ", "George Martin"));
   * assertThat(book).doesNotContainSubsequence(asList("A Game of ", "Thrones"));}</code></pre>
   *
   * @param subsequence the subsequence to look for.
   * @return {@code this} assertion object.
   * @throws NullPointerException     if the given values is {@code null}.
   * @throws IllegalArgumentException if the given values is empty.
   * @throws AssertionError           if the given {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the given {@code CharSequence} contains the given values in the given order,
   *                                  possibly with other characters between them.
   */
  public SELF doesNotContainSubsequence(Iterable<? extends CharSequence> subsequence) {
    return executeAssertion(() -> assertDoesNotContainSubsequence(toArray(subsequence, CharSequence.class)));
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
   * @throws NullPointerException     if the given list of values is {@code null}.
   * @throws NullPointerException     if any one of the given values is {@code null}.
   * @throws IllegalArgumentException if the list of given values is empty.
   * @throws AssertionError           if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError           if the actual {@code CharSequence} contains any one of the given values, ignoring case considerations.
   * @since 3.17.0
   */
  public SELF doesNotContainIgnoringCase(CharSequence... values) {
    return executeAssertion(() -> assertDoesNotContainIgnoringCase(values));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the given regular expression can be found in the actual {@code CharSequence}.
   * @since 2.7.0 / 3.7.0
   */
  public SELF doesNotContainPattern(CharSequence pattern) {
    return executeAssertion(() -> assertDoesNotContainPattern(pattern));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the given regular expression can be found in the actual {@code CharSequence}.
   * @since 2.7.0 / 3.7.0
   */
  public SELF doesNotContainPattern(Pattern pattern) {
    return executeAssertion(() -> assertDoesNotContainPattern(pattern));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} does not start with the given prefix.
   */
  public SELF startsWith(CharSequence prefix) {
    return executeAssertion(() -> assertStartsWith(prefix));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} does not start with the given prefix, ignoring case.
   * @since 3.23.0
   */
  public SELF startsWithIgnoringCase(CharSequence prefix) {
    return executeAssertion(() -> assertStartsWithIgnoringCase(prefix));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} starts with the given prefix.
   */
  public SELF doesNotStartWith(CharSequence prefix) {
    return executeAssertion(() -> assertDoesNotStartWith(prefix));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} starts with the given prefix, ignoring case.
   * @since 3.23.0
   */
  public SELF doesNotStartWithIgnoringCase(CharSequence prefix) {
    return executeAssertion(() -> assertDoesNotStartWithIgnoringCase(prefix));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} does not end with the given suffix.
   */
  public SELF endsWith(CharSequence suffix) {
    return executeAssertion(() -> assertEndsWith(suffix));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} does not end with the given suffix, ignoring case.
   * @since 3.23.0
   */
  public SELF endsWithIgnoringCase(CharSequence suffix) {
    return executeAssertion(() -> assertEndsWithIgnoringCase(suffix));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} ends with the given suffix.
   */
  public SELF doesNotEndWith(CharSequence suffix) {
    return executeAssertion(() -> assertDoesNotEndWith(suffix));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} ends with the given suffix, ignoring case.
   * @since 3.23.0
   */
  public SELF doesNotEndWithIgnoringCase(CharSequence suffix) {
    return executeAssertion(() -> assertDoesNotEndWithIgnoringCase(suffix));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} does not match the given regular expression.
   */
  public SELF matches(CharSequence regex) {
    return executeAssertion(() -> assertMatches(regex));
  }

  /**
   * Verifies that the actual {@code CharSequence} matches the given regular expression pattern, then accepts the given
   * {@code Consumer<Matcher>} to do further verification on the matcher.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(&quot;Frodo&quot;).matchesSatisfying(&quot;..(o.o)&quot;, matcher -&gt; assertThat(matcher.group(1)).isEqualTo(&quot;odo&quot;)); </code></pre>
   *
   * @param regex          the regular expression to which the actual {@code CharSequence} is to be matched.
   * @param matchSatisfies a consumer of the found match to do further verification
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} does not match the given regular expression.
   */
  public SELF matchesSatisfying(CharSequence regex, Consumer<Matcher> matchSatisfies) {
    return matchesSatisfying(Pattern.compile(regex.toString()), matchSatisfies);
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} matches the given regular expression.
   */
  public SELF doesNotMatch(CharSequence regex) {
    return executeAssertion(() -> assertDoesNotMatch(regex));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} does not match the given regular expression.
   */
  public SELF matches(Pattern pattern) {
    return executeAssertion(() -> assertMatches(pattern));
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
   * @param pattern        the regular expression to which the actual {@code CharSequence} is to be matched.
   * @param matchSatisfies a consumer of the found match to do further verification
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} does not match the given regular expression.
   */
  public SELF matchesSatisfying(Pattern pattern, Consumer<Matcher> matchSatisfies) {
    return executeAssertion(() -> {
      Matcher matcher = pattern.matcher(actual);
      assertMatches(matcher);
      matchSatisfies.accept(matcher);
    });
  }

  // internal method to avoid double proxying if one assertion calls another one

  /// Verifies that the actual `CharSequence` does not match the given regular expression pattern.
  ///
  /// Example:
  /// ```java
  /// // assertion will pass
  /// assertThat("Frodo").doesNotMatch(Pattern.compile(".*d"));
  ///
  /// // assertions will fail
  /// assertThat("Frodo").doesNotMatch(Pattern.compile("..o.o"));
  /// assertThat((String) null).doesNotMatch(Pattern.compile(".*d"));
  /// ```
  ///
  /// @param pattern the regular expression to which the actual `CharSequence` is to be matched.
  /// @return `this` assertion object.
  /// @throws NullPointerException if the given pattern is `null`.
  /// @throws AssertionError if the actual `CharSequence` is `null`.
  /// @throws AssertionError if the actual `CharSequence` matches the given regular expression.
  public SELF doesNotMatch(Pattern pattern) {
    return executeAssertion(() -> assertDoesNotMatch(pattern));
  }

  /**
   * Do not use this method.
   *
   * @throws UnsupportedOperationException if this method is called.
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
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    return super.usingDefaultComparator();
  }

  @Override
  @CheckReturnValue
  public SELF inHexadecimal() {
    return super.inHexadecimal();
  }

  @Override
  @CheckReturnValue
  public SELF inBinary() {
    return super.inBinary();
  }

  /**
   * Use Unicode character representation instead of standard representation in error messages.
   * <p>
   * It can be useful when comparing UNICODE characters - many Unicode chars have duplicate characters assigned, it is
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
   * <p>
   * With Hexadecimal message:
   * <pre><code class='java'> assertThat("µµµ").inUnicode().contains("μμμ");
   *
   * java.lang.AssertionError:
   * Expecting:
   *   &lt;&bsol;u00b5&bsol;u00b5&bsol;u00b5&gt;
   * to contain:
   *   &lt;&bsol;u03bc&bsol;u03bc&bsol;u03bc&gt;</code></pre>
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
   *                        one.
   */
  public SELF isEqualToIgnoringWhitespace(CharSequence expected) {
    return executeAssertion(() -> assertEqualsIgnoringWhitespace(expected));
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
   *                        one.
   */
  public SELF isNotEqualToIgnoringWhitespace(CharSequence expected) {
    return executeAssertion(() -> assertNotEqualsIgnoringWhitespace(expected));
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
   *                        after whitespace has been normalized.
   * @since 2.8.0 / 3.8.0
   */
  public SELF isEqualToNormalizingWhitespace(CharSequence expected) {
    return executeAssertion(() -> assertEqualsNormalizingWhitespace(expected));
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
   *                        after whitespace has been normalized.
   * @since 2.8.0 / 3.8.0
   */
  public SELF isNotEqualToNormalizingWhitespace(CharSequence expected) {
    return executeAssertion(() -> assertNotEqualsNormalizingWhitespace(expected));
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
   *                        after punctuation have been normalized.
   * @since 3.16.0
   */
  public SELF isEqualToNormalizingPunctuationAndWhitespace(CharSequence expected) {
    return executeAssertion(() -> assertEqualsNormalizingPunctuationAndWhitespace(expected));
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
    return executeAssertion(() -> assertIsSubstringOf(sequence));
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the given regular expression cannot be found in the actual {@code CharSequence}.
   */
  public SELF containsPattern(CharSequence regex) {
    return executeAssertion(() -> assertContainsPattern(regex));
  }

  /**
   * Verifies that the actual {@code CharSequence} contains the given regular expression pattern, then accepts the given
   * {@code Consumer<Matcher>} for further verification on the matcher.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(&quot;Frodo&quot;).containsPatternSatisfying(&quot;.o(.o)&quot;, matcher -&gt; assertThat(matcher.group(1)).isEqualTo(&quot;do&quot;)); </code></pre>
   *
   * @param regex          the regular expression to find in the actual {@code CharSequence}.
   * @param matchSatisfies a consumer for further verifying the Matcher.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the given regular expression cannot be found in the actual {@code CharSequence}.
   */
  public SELF containsPatternSatisfying(CharSequence regex, Consumer<Matcher> matchSatisfies) {
    return containsPatternSatisfying(Pattern.compile(regex.toString()), matchSatisfies);
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
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the given regular expression cannot be found in the actual {@code CharSequence}.
   */
  public SELF containsPattern(Pattern pattern) {
    return executeAssertion(() -> assertContainsPattern(pattern));
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
   * @param pattern        the regular expression to find in the actual {@code CharSequence}.
   * @param matchSatisfies a consumer for further verifying the Matcher.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given pattern is {@code null}.
   * @throws AssertionError       if the actual {@code CharSequence} is {@code null}.
   * @throws AssertionError       if the given regular expression cannot be found in the actual {@code CharSequence}.
   */
  public SELF containsPatternSatisfying(Pattern pattern, Consumer<Matcher> matchSatisfies) {
    return executeAssertion(() -> {
      Matcher matcher = pattern.matcher(actual);
      assertContainsPattern(matcher);
      matchSatisfies.accept(matcher);
    });
  }

  /**
   * Verifies that the actual {@code CharSequence} is equals to another
   * {@code CharSequence} after normalizing new line characters
   * (i.e. '\r\n' == '\n').
   * <p>
   * This assertion succeeds:
   * <pre><code class='java'> String bookName = &quot;Lord of the Rings\r\n&quot;;
   * assertThat(bookName).isEqualToNormalizingNewlines(&quot;Lord of the Rings\n&quot;);</code></pre>
   * <p>
   * Whereas this assertion fails:
   * <pre><code class='java'> String singleLine = &quot;\n&quot;;
   * assertThat(singleLine).isEqualToNormalizingNewlines(&quot;&quot;);</code></pre>
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} and the given {@code CharSequence} are different
   *                        after new lines are normalized.
   * @since 2.7.0 / 3.7.0
   */
  public SELF isEqualToNormalizingNewlines(CharSequence expected) {
    return executeAssertion(() -> assertIsEqualToNormalizingNewlines(expected));
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the given one after both strings newlines (\n, \r\n) have been removed.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat("Some textWith new lines").isEqualToIgnoringNewlines("Some text\nWith new lines")
   *                                      .isEqualToIgnoringNewlines("Some text\r\nWith new lines")
   *                                      .isEqualToIgnoringNewlines("Some text\n\nWith new lines");
   *
   * assertThat("Some text\nWith new lines").isEqualToIgnoringNewlines("Some text\nWith new lines")
   *                                        .isEqualToIgnoringNewlines("Some text\r\nWith new lines")
   *                                        .isEqualToIgnoringNewlines("Some text\n\nWith new lines");
   *
   * // assertions will fail
   * assertThat("Some text\nWith new lines").isEqualToIgnoringNewlines("Some text With new lines");
   * assertThat("Some text\r\nWith new lines").isEqualToIgnoringNewlines("Some text With new lines");</code></pre>
   *
   * @param expected the given {@code CharSequence} to compare the actual {@code CharSequence} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not equal to the given one after new lines have been removed.
   */
  public SELF isEqualToIgnoringNewlines(CharSequence expected) {
    return executeAssertion(() -> assertIsEqualToIgnoringNewlines(expected));
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
    return executeAssertion(() -> assertLowerCase());
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
    return executeAssertion(() -> assertMixedCase());
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
    return executeAssertion(() -> assertUpperCase());
  }

  /**
   * Verifies that the actual {@code CharSequence} is equal to the given one after they have been normalized
   * according to the {@link java.text.Normalizer.Form#NFC} form, which is a canonical decomposition followed by canonical composition.
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
   * @throws AssertionError       if the actual {@code CharSequence} is not equal to the given one
   *                              after both strings have been normalized to according to the {@link java.text.Normalizer.Form#NFC} form .
   * @throws NullPointerException if the actual {@code CharSequence} is not null and the given is.
   * @since 3.19.0
   */
  public SELF isEqualToNormalizingUnicode(CharSequence expected) {
    return executeAssertion(() -> assertEqualsToNormalizingUnicode(expected));
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
    return executeAssertion(() -> {
      isNotNull();
      if (!Pattern.matches("\\p{Alpha}+", actual)) throwAssertionError(shouldBeAlphabetic(actual));
    });
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
    return executeAssertion(() -> {
      isNotNull();
      if (!Pattern.matches("\\p{Alnum}+", actual)) throwAssertionError(shouldBeAlphanumeric(actual));
    });
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
   * assertThat(&quot;⌃&quot;).isASCII();
   * assertThat(&quot;L3go123⌃00abc&quot;).isASCII();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code CharSequence} is not ASCII.
   * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html">java.util.regex.Pattern</a>
   */
  public SELF isASCII() {
    return executeAssertion(() -> {
      isNotNull();
      if (!Pattern.matches("\\p{ASCII}+", actual)) throwAssertionError(shouldBeASCII(actual));
    });
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
    return executeAssertion(() -> {
      isNotNull();
      if (!Pattern.matches("\\p{XDigit}+", actual)) throwAssertionError(shouldBeHexadecimal(actual));
    });
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
    return executeAssertion(() -> {
      isNotNull();
      if (!Pattern.matches("\\p{Print}+", actual)) throwAssertionError(shouldBePrintable(actual));
    });
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
    return executeAssertion(() -> {
      isNotNull();
      if (!Pattern.matches("\\p{Graph}+", actual)) throwAssertionError(shouldBeVisible(actual));
    });
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
    return executeAssertion(() -> {
      isNotNull();
      actual.codePoints().boxed().findFirst().filter(Character::isWhitespace)
            .ifPresent(__ -> throwAssertionError(shouldNotStartWithWhitespaces(actual)));
    });
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
    return executeAssertion(() -> {
      isNotNull();
      actual.codePoints().boxed().reduce((v1, v2) -> v2).filter(Character::isWhitespace)
            .ifPresent(__ -> throwAssertionError(shouldNotEndWithWhitespaces(actual)));
    });
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
    return actual == null || actual.isEmpty();
  }

  private static boolean strictlyContainsWhitespaces(CharSequence actual) {
    return actual.chars().allMatch(Character::isWhitespace);
  }

  private static boolean containsOneOrMoreWhitespaces(CharSequence actual) {
    return actual.chars().anyMatch(Character::isWhitespace);
  }

  // internal assertions

  private void assertNullOrEmpty() {
    if (actual != null && hasContent(actual)) throw assertionError(shouldBeNullOrEmpty(actual));
  }

  private void assertEmpty() {
    isNotNull();
    if (hasContent(actual)) throw assertionError(shouldBeEmpty(actual));
  }

  private void assertNotEmpty() {
    isNotNull();
    if (!hasContent(actual)) throw assertionError(shouldNotBeEmpty());
  }

  private static boolean hasContent(CharSequence s) {
    return s.length() > 0;
  }

  private void assertHasSize(int expectedSize) {
    isNotNull();
    checkSizes(actual, actual.length(), expectedSize, info);
  }

  private void assertHasSizeLessThan(int expectedMaxSizeExcluded) {
    isNotNull();
    if (actual.length() >= expectedMaxSizeExcluded)
      throw assertionError(shouldHaveSizeLessThan(actual, actual.length(), expectedMaxSizeExcluded));
  }

  private void assertHasSizeLessThanOrEqualTo(int expectedMaxSizeIncluded) {
    isNotNull();
    if (actual.length() > expectedMaxSizeIncluded)
      throw assertionError(shouldHaveSizeLessThanOrEqualTo(actual, actual.length(), expectedMaxSizeIncluded));
  }

  private void assertHasSizeGreaterThan(int expectedMinSizeExcluded) {
    isNotNull();
    if (actual.length() <= expectedMinSizeExcluded)
      throw assertionError(shouldHaveSizeGreaterThan(actual, actual.length(), expectedMinSizeExcluded));
  }

  private void assertHasSizeGreaterThanOrEqualTo(int expectedMinSizeIncluded) {
    isNotNull();
    if (actual.length() < expectedMinSizeIncluded)
      throw assertionError(shouldHaveSizeGreaterThanOrEqualTo(actual, actual.length(), expectedMinSizeIncluded));
  }

  private void assertHasSizeBetween(int lowerBoundary, int higherBoundary) {
    isNotNull();
    checkSizeBetween(actual, lowerBoundary, higherBoundary, actual.length(), info);
  }

  private void assertHasLineCount(int expectedLineCount) {
    isNotNull();
    LineNumberReader reader = new LineNumberReader(new StringReader(actual.toString()));
    try {
      while (reader.readLine() != null);
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to count lines in `%s`".formatted(actual), e);
    }
    checkLineCounts(actual, reader.getLineNumber(), expectedLineCount, info);
  }

  private void assertHasSameSizeAs(Iterable<?> other) {
    isNotNull();
    hasSameSizeAsCheck(info, actual, other, actual.length());
  }

  private void assertHasSameSizeAs(Object array) {
    isNotNull();
    assertIsArray(info, array);
    hasSameSizeAsCheck(info, actual, array, actual.length());
  }

  private void assertHasSameSizeAs(CharSequence other) {
    isNotNull();
    requireNonNull(other, "The CharSequence or String to compare actual size with should not be null");
    if (actual.length() != other.length())
      throw assertionError(shouldHaveSameSizeAs(actual, other, actual.length(), other.length()));
  }

  private void assertContains(CharSequence... values) {
    doCommonCheckForCharSequence(values);
    Set<CharSequence> notFound = stream(values).filter(value -> !stringContains(actual, value))
                                               .collect(toCollection(LinkedHashSet::new));
    if (notFound.isEmpty()) return;
    if (notFound.size() == 1 && values.length == 1)
      throw assertionError(shouldContain(actual, values[0], getComparisonStrategy()));
    throw assertionError(shouldContain(actual, values, notFound, getComparisonStrategy()));
  }

  private void assertContainsAnyOf(CharSequence[] values) {
    doCommonCheckForCharSequence(values);
    boolean found = stream(values).anyMatch(value -> stringContains(actual, value));
    if (!found) throw assertionError(shouldContainAnyOf(actual, values, getComparisonStrategy()));
  }

  private void assertContainsOnlyDigits() {
    isNotNull();
    if (actual.length() == 0) throw assertionError(shouldContainOnlyDigits(actual));
    for (int index = 0; index < actual.length(); index++) {
      char character = actual.charAt(index);
      if (!isDigit(character)) throw assertionError(shouldContainOnlyDigits(actual, character, index));
    }
  }

  private boolean stringContains(CharSequence text, CharSequence sequence) {
    return getComparisonStrategy().stringContains(text.toString(), sequence.toString());
  }

  private void assertContainsIgnoringCase(CharSequence sequence) {
    checkCharSequenceIsNotNull(sequence);
    isNotNull();
    if (!containsIgnoreCase(actual, sequence)) throw assertionError(shouldContainIgnoringCase(actual, sequence));
  }

  private boolean containsIgnoreCase(CharSequence text, CharSequence sequence) {
    return getComparisonStrategy().stringContains(text.toString().toLowerCase(ROOT), sequence.toString().toLowerCase(ROOT));
  }

  private void assertContainsIgnoringNewlines(CharSequence... values) {
    doCommonCheckForCharSequence(values);
    String actualNoNewLines = removeNewlines(actual);
    Set<CharSequence> notFound = stream(values).filter(value -> !stringContains(actualNoNewLines, removeNewlines(value)))
                                               .collect(toCollection(LinkedHashSet::new));
    if (notFound.isEmpty()) return;
    throw assertionError(containsIgnoringNewLines(actual, values, notFound, getComparisonStrategy()));
  }

  private void assertDoesNotContainIgnoringCase(CharSequence... values) {
    doCommonCheckForCharSequence(values);
    Set<CharSequence> foundValues = stream(values).filter(value -> containsIgnoreCase(actual, value))
                                                  .collect(toCollection(LinkedHashSet::new));
    if (foundValues.isEmpty()) return;
    if (foundValues.size() == 1 && values.length == 1) throw assertionError(shouldNotContainIgnoringCase(actual, values[0]));
    throw assertionError(shouldNotContainIgnoringCase(actual, values, foundValues));
  }

  private void assertDoesNotContain(CharSequence... values) {
    doCommonCheckForCharSequence(values);
    Set<CharSequence> found = stream(values).filter(value -> stringContains(actual, value))
                                            .collect(toCollection(LinkedHashSet::new));
    if (found.isEmpty()) return;
    if (found.size() == 1 && values.length == 1)
      throw assertionError(shouldNotContain(actual, values[0], getComparisonStrategy()));
    throw assertionError(shouldNotContain(actual, values, found, getComparisonStrategy()));
  }

  private static void checkCharSequenceIsNotNull(CharSequence sequence) {
    requireNonNull(sequence, "The char sequence to look for should not be null");
  }

  private void assertEqualsIgnoringCase(CharSequence expected) {
    if (!areEqualIgnoringCase(actual, expected))
      throw assertionError(ShouldBeEqualIgnoringCase.shouldBeEqual(actual, expected), actual, expected);
  }

  private void assertNotEqualsIgnoringCase(CharSequence expected) {
    if (areEqualIgnoringCase(actual, expected)) throw assertionError(shouldNotBeEqualIgnoringCase(actual, expected));
  }

  private static boolean areEqualIgnoringCase(CharSequence actual, CharSequence expected) {
    if (actual == null) return expected == null;
    if (expected == null) return false;
    return actual.toString().equalsIgnoreCase(expected.toString());
  }

  private void assertIsEqualToNormalizingNewlines(CharSequence expected) {
    String normalizedActual = normalizeNewlines(actual);
    String normalizedExpected = normalizeNewlines(expected);
    if (!java.util.Objects.equals(normalizedActual, normalizedExpected))
      throw assertionError(shouldBeEqualIgnoringNewLineDifferences(actual, expected), normalizedActual, normalizedExpected);
  }

  private static String normalizeNewlines(CharSequence charSequence) {
    return charSequence != null ? charSequence.toString().replace("\r\n", "\n") : null;
  }

  private void assertEqualsIgnoringWhitespace(CharSequence expected) {
    if (!areEqualIgnoringWhitespace(actual, expected))
      throw assertionError(shouldBeEqualIgnoringWhitespace(actual, expected), actual, expected);
  }

  private void assertNotEqualsIgnoringWhitespace(CharSequence expected) {
    if (areEqualIgnoringWhitespace(actual, expected)) throw assertionError(shouldNotBeEqualIgnoringWhitespace(actual, expected));
  }

  private static boolean areEqualIgnoringWhitespace(CharSequence actual, CharSequence expected) {
    if (actual == null) return expected == null;
    checkCharSequenceIsNotNull(expected);
    return removeAllWhitespaces(actual).equals(removeAllWhitespaces(expected));
  }

  private static String removeAllWhitespaces(CharSequence toBeStripped) {
    StringBuilder result = new StringBuilder(toBeStripped.length());
    for (int i = 0; i < toBeStripped.length(); i++) {
      char c = toBeStripped.charAt(i);
      if (!isWhitespace(c)) result.append(c);
    }
    return result.toString();
  }

  private void assertEqualsNormalizingWhitespace(CharSequence expected) {
    if (actual != null) checkCharSequenceIsNotNull(expected);
    String normalizedActual = normalizeWhitespace(actual);
    String normalizedExpected = normalizeWhitespace(expected);
    if (!java.util.Objects.equals(normalizedActual, normalizedExpected))
      throw assertionError(shouldBeEqualNormalizingWhitespace(actual, expected), normalizedActual, normalizedExpected);
  }

  private void assertNotEqualsNormalizingWhitespace(CharSequence expected) {
    if (actual != null) checkCharSequenceIsNotNull(expected);
    String normalizedActual = normalizeWhitespace(actual);
    String normalizedExpected = normalizeWhitespace(expected);
    if (java.util.Objects.equals(normalizedActual, normalizedExpected))
      throw assertionError(shouldNotBeEqualNormalizingWhitespace(actual, expected));
  }

  private static String normalizeWhitespace(CharSequence toNormalize) {
    if (toNormalize == null) return null;
    StringBuilder result = new StringBuilder(toNormalize.length());
    boolean lastWasSpace = true;
    for (int i = 0; i < toNormalize.length(); i++) {
      char c = toNormalize.charAt(i);
      if (isWhitespace(c) || NON_BREAKING_SPACES.contains(c)) {
        if (!lastWasSpace) result.append(' ');
        lastWasSpace = true;
      } else {
        result.append(c);
        lastWasSpace = false;
      }
    }
    return result.toString().trim();
  }

  private void assertEqualsNormalizingPunctuationAndWhitespace(CharSequence expected) {
    if (actual != null) checkCharSequenceIsNotNull(expected);
    String normalizedActual = normalizeWhitespaceAndPunctuation(actual);
    String normalizedExpected = normalizeWhitespaceAndPunctuation(expected);
    if (!java.util.Objects.equals(normalizedActual, normalizedExpected))
      throw assertionError(shouldBeEqualNormalizingPunctuationAndWhitespace(actual, expected), normalizedActual,
                           normalizedExpected);
  }

  private static String normalizeWhitespaceAndPunctuation(CharSequence input) {
    return input == null ? null : normalizeWhitespace(input.toString().replaceAll(PUNCTUATION_REGEX, ""));
  }

  private void assertEqualsToNormalizingUnicode(CharSequence expected) {
    if (actual != null) checkCharSequenceIsNotNull(expected);
    String normalizedActual = Normalizer.normalize(actual, Normalizer.Form.NFC);
    String normalizedExpected = Normalizer.normalize(expected, Normalizer.Form.NFC);
    if (!java.util.Objects.equals(normalizedActual, normalizedExpected))
      throw assertionError(shouldBeEqualNormalizingUnicode(actual, expected, normalizedActual, normalizedExpected),
                           normalizedActual, normalizedExpected);
  }

  private void assertContainsOnlyOnce(CharSequence sequence) {
    checkCharSequenceIsNotNull(sequence);
    isNotNull();
    int sequenceOccurrencesInActual = countOccurrences(sequence, actual);
    if (sequenceOccurrencesInActual == 1) return;
    throw assertionError(shouldContainOnlyOnce(actual, sequence, sequenceOccurrencesInActual, getComparisonStrategy()));
  }

  private int countOccurrences(CharSequence sequenceToSearch, CharSequence text) {
    String strToSearch = sequenceToSearch.toString();
    String strText = text.toString();
    int occurrences = 0;
    for (int i = 0; i <= (strText.length() - strToSearch.length()); i++) {
      if (getComparisonStrategy().areEqual(strText.substring(i, i + sequenceToSearch.length()), strToSearch)) occurrences++;
    }
    return occurrences;
  }

  private void assertStartsWith(CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    isNotNull();
    if (!startsWithPrefix(prefix, false)) throw assertionError(shouldStartWith(actual, prefix, getComparisonStrategy()));
  }

  private void assertStartsWithIgnoringCase(CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    isNotNull();
    if (!startsWithPrefix(prefix, true))
      throw assertionError(shouldStartWithIgnoringCase(actual, prefix, getComparisonStrategy()));
  }

  private void assertDoesNotStartWith(CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    isNotNull();
    if (startsWithPrefix(prefix, false)) throw assertionError(shouldNotStartWith(actual, prefix, getComparisonStrategy()));
  }

  private void assertDoesNotStartWithIgnoringCase(CharSequence prefix) {
    failIfPrefixIsNull(prefix);
    isNotNull();
    if (startsWithPrefix(prefix, true))
      throw assertionError(shouldNotStartWithIgnoringCase(actual, prefix, getComparisonStrategy()));
  }

  private static void failIfPrefixIsNull(CharSequence prefix) {
    requireNonNull(prefix, "The given prefix should not be null");
  }

  private boolean startsWithPrefix(CharSequence prefix, boolean ignoreCase) {
    return ignoreCase
        ? getComparisonStrategy().stringStartsWith(actual.toString().toLowerCase(ROOT), prefix.toString().toLowerCase(ROOT))
        : getComparisonStrategy().stringStartsWith(actual.toString(), prefix.toString());
  }

  private void assertEndsWith(CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    isNotNull();
    if (!endsWithSuffix(suffix, false)) throw assertionError(shouldEndWith(actual, suffix, getComparisonStrategy()));
  }

  private void assertEndsWithIgnoringCase(CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    isNotNull();
    if (!endsWithSuffix(suffix, true)) throw assertionError(shouldEndWithIgnoringCase(actual, suffix, getComparisonStrategy()));
  }

  private void assertDoesNotEndWith(CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    isNotNull();
    if (endsWithSuffix(suffix, false)) throw assertionError(shouldNotEndWith(actual, suffix, getComparisonStrategy()));
  }

  private void assertDoesNotEndWithIgnoringCase(CharSequence suffix) {
    failIfSuffixIsNull(suffix);
    isNotNull();
    if (endsWithSuffix(suffix, true))
      throw assertionError(shouldNotEndWithIgnoringCase(actual, suffix, getComparisonStrategy()));
  }

  private static void failIfSuffixIsNull(CharSequence suffix) {
    requireNonNull(suffix, "The given suffix should not be null");
  }

  private boolean endsWithSuffix(CharSequence suffix, boolean ignoreCase) {
    return ignoreCase
        ? getComparisonStrategy().stringEndsWith(actual.toString().toLowerCase(ROOT), suffix.toString().toLowerCase(ROOT))
        : getComparisonStrategy().stringEndsWith(actual.toString(), suffix.toString());
  }

  private void assertMatches(CharSequence regex) {
    checkRegexIsNotNull(regex);
    isNotNull();
    if (!Pattern.matches(regex.toString(), actual)) throw assertionError(shouldMatch(actual, regex));
  }

  private void assertDoesNotMatch(CharSequence regex) {
    checkRegexIsNotNull(regex);
    isNotNull();
    if (Pattern.matches(regex.toString(), actual)) throw assertionError(shouldNotMatch(actual, regex));
  }

  private static void checkRegexIsNotNull(CharSequence regex) {
    if (regex == null) throw patternToMatchIsNull();
  }

  private void assertMatches(Pattern pattern) {
    checkPatternIsNotNull(pattern);
    isNotNull();
    assertMatches(pattern.matcher(actual));
  }

  private void assertMatches(Matcher matcher) {
    checkMatcherIsNotNull(matcher);
    isNotNull();
    if (!matcher.matches()) throw assertionError(shouldMatch(actual, matcher.pattern().pattern()));
  }

  private void assertDoesNotMatch(Pattern pattern) {
    checkPatternIsNotNull(pattern);
    isNotNull();
    if (pattern.matcher(actual).matches()) throw assertionError(shouldNotMatch(actual, pattern.pattern()));
  }

  private static void checkPatternIsNotNull(Pattern pattern) {
    if (pattern == null) throw patternToMatchIsNull();
  }

  private static NullPointerException patternToMatchIsNull() {
    return new NullPointerException("The regular expression pattern to match should not be null");
  }

  private static void checkMatcherIsNotNull(Matcher matcher) {
    if (matcher == null) throw new NullPointerException("The matcher should not be null");
  }

  private void assertContainsSequence(CharSequence[] sequence) {
    doCommonCheckForCharSequence(sequence);
    Set<CharSequence> notFound = stream(sequence).filter(value -> !stringContains(actual, value))
                                                 .collect(toCollection(LinkedHashSet::new));
    if (!notFound.isEmpty()) {
      // don't bother looking for a sequence, some of the sequence elements were not found !
      if (notFound.size() == 1 && sequence.length == 1)
        throw assertionError(shouldContain(actual, sequence[0], getComparisonStrategy()));
      throw assertionError(shouldContain(actual, sequence, notFound, getComparisonStrategy()));
    }
    // we have found all the given values but were they in the expected order ?
    if (sequence.length == 1) return; // no order check needed for a one element sequence
    // convert all values to one char sequence to compare with the actual char sequence
    if (!stringContains(actual.toString(), String.join("", sequence)))
      throw assertionError(shouldContainSequence(actual, sequence, getComparisonStrategy()));
  }

  private void assertContainsSubsequence(CharSequence[] subsequence) {
    doCommonCheckForCharSequence(subsequence);
    Map<CharSequence, Integer> notFound = getNotFoundSubsequence(subsequence);
    handleNotFound(subsequence, notFound);
    // we have found all the given values but were they in the expected order ?
    if (subsequence.length == 1) return; // no order check needed for a one element subsequence
    // the values are in the correct order if after removing the start of actual up to the
    // subsequence element included, we are able to find the next subsequence element, ex:
    // "{ George Martin }" with subsequence ["George", " ", "Martin"]:
    // - remove up to "George" in "{ George Martin }" -> " Martin }", does it contain " " ?
    // - remove up to " " in " Martin }" -> "Martin }", does it contain "Martin" ?
    // ...
    String actualRest = removeUpTo(actual.toString(), subsequence[0]);
    // check the subsequence second element since we already know the first is present
    for (int i = 1; i < subsequence.length; i++) {
      if (stringContains(actualRest, subsequence[i])) actualRest = removeUpTo(actualRest, subsequence[i]);
      else throw assertionError(shouldContainSubsequence(actual, subsequence, i - 1, getComparisonStrategy()));
    }
  }

  // Handles the scenario where some subsequence elements were not found (or not found enough times) in actual.
  private void handleNotFound(CharSequence[] subsequence, Map<CharSequence, Integer> notFound) {
    if (notFound.isEmpty()) return;
    // only one missing element, and we were only looking for one
    if (notFound.size() == 1 && subsequence.length == 1)
      throw assertionError(shouldContain(actual, subsequence[0], getComparisonStrategy()));
    // if every not found element was found at least once, they were found but not as many times as expected
    boolean anyDuplicateSubsequenceFound = notFound.values().stream().allMatch(count -> count > 0);
    if (anyDuplicateSubsequenceFound)
      throw assertionError(shouldContainSubsequence(actual, subsequence, notFound, getComparisonStrategy()));
    // otherwise report the elements that were not found at all
    Set<CharSequence> notFoundAtAll = notFound.entrySet().stream()
                                              .filter(entry -> entry.getValue() == 0)
                                              .map(Map.Entry::getKey)
                                              .collect(Collectors.toSet());
    throw assertionError(shouldContain(actual, subsequence, notFoundAtAll, getComparisonStrategy()));
  }

  // Returns the subsequence elements that appear more times in subsequence than in actual, mapped to their number of
  // occurrences in actual.
  private Map<CharSequence, Integer> getNotFoundSubsequence(CharSequence[] subsequence) {
    Map<CharSequence, Integer> actualCounts = new HashMap<>();
    Map<CharSequence, Long> subsequenceCounts = stream(subsequence).collect(groupingBy(identity(), counting()));
    for (CharSequence value : subsequence) {
      actualCounts.computeIfAbsent(value, key -> countOccurrences(key, actual));
    }
    return subsequenceCounts.entrySet().stream()
                            .filter(entry -> entry.getValue() > actualCounts.getOrDefault(entry.getKey(), 0))
                            .collect(toMap(Map.Entry::getKey, entry -> actualCounts.get(entry.getKey()),
                                           (existing, replacement) -> existing, LinkedHashMap::new));
  }

  private String removeUpTo(String string, CharSequence toRemove) {
    // toRemove is known to be in string at this point
    int index = indexOf(string, toRemove.toString(), 0);
    return string.substring(index + toRemove.length());
  }

  private int indexOf(String str, String toFind, int fromIndex) {
    if (str.isEmpty() && toFind.isEmpty()) return 0;
    for (int i = fromIndex; i < str.length(); i++) {
      if (getComparisonStrategy().stringStartsWith(str.substring(i), toFind)) return i;
    }
    return -1;
  }

  private void assertDoesNotContainSequence(CharSequence[] sequence) {
    doCommonCheckForCharSequence(sequence);
    int index = indexOf(actual.toString(), String.join("", sequence), 0);
    if (index != -1) throw assertionError(shouldNotContainSequence(actual, sequence, index, getComparisonStrategy()));
  }

  private void assertDoesNotContainSubsequence(CharSequence[] subsequence) {
    doCommonCheckForCharSequence(subsequence);
    String strActual = actual.toString();
    int startIndex = 0;
    int[] subsequenceIndexes = new int[subsequence.length];
    for (int i = 0; i < subsequence.length; i++) {
      String strSubsequenceItem = subsequence[i].toString();
      int index = indexOf(strActual, strSubsequenceItem, startIndex);
      if (index == -1) return;
      subsequenceIndexes[i] = index;
      startIndex = index + strSubsequenceItem.length();
    }
    throw assertionError(shouldNotContainSubsequence(actual, subsequence, subsequenceIndexes, getComparisonStrategy()));
  }

  private void assertIsSubstringOf(CharSequence sequence) {
    isNotNull();
    requireNonNull(sequence, "Expecting CharSequence not to be null");
    if (!stringContains(sequence.toString(), actual.toString()))
      throw assertionError(shouldBeSubstring(actual, sequence, getComparisonStrategy()));
  }

  private void assertContainsPattern(CharSequence regex) {
    checkRegexIsNotNull(regex);
    assertContainsPattern(Pattern.compile(regex.toString()));
  }

  private void assertContainsPattern(Matcher matcher) {
    isNotNull();
    checkMatcherIsNotNull(matcher);
    if (!matcher.find()) throw assertionError(shouldContainPattern(actual, matcher.pattern().pattern()));
  }

  private void assertContainsPattern(Pattern pattern) {
    checkPatternIsNotNull(pattern);
    isNotNull();
    if (!pattern.matcher(actual).find()) throw assertionError(shouldContainPattern(actual, pattern.pattern()));
  }

  private void assertDoesNotContainPattern(CharSequence regex) {
    checkRegexIsNotNull(regex);
    assertDoesNotContainPattern(Pattern.compile(regex.toString()));
  }

  private void assertDoesNotContainPattern(Pattern pattern) {
    checkPatternIsNotNull(pattern);
    isNotNull();
    if (pattern.matcher(actual).find()) throw assertionError(shouldNotContainPattern(actual, pattern.pattern()));
  }

  private void assertIsEqualToIgnoringNewlines(CharSequence expected) {
    String actualWithoutNewLines = removeNewlines(actual);
    String expectedWithoutNewLines = removeNewlines(expected);
    if (!actualWithoutNewLines.equals(expectedWithoutNewLines))
      throw assertionError(shouldBeEqualIgnoringNewLines(actual, expected), actual, expected);
  }

  private void assertLowerCase() {
    isNotNull();
    if (!isAllLowerCase(actual)) throw assertionError(shouldBeLowerCase(actual));
  }

  private static boolean isAllLowerCase(CharSequence text) {
    return text.equals(text.toString().toLowerCase());
  }

  private void assertUpperCase() {
    isNotNull();
    if (!isAllUpperCase(actual)) throw assertionError(shouldBeUpperCase(actual));
  }

  private static boolean isAllUpperCase(CharSequence text) {
    return text.equals(text.toString().toUpperCase());
  }

  private void assertMixedCase() {
    isNotNull();
    if (isAllLowerCase(actual) != isAllUpperCase(actual)) throw assertionError(shouldBeMixedCase(actual));
  }

  private static String removeNewlines(CharSequence text) {
    return normalizeNewlines(text).replace("\n", "");
  }

  private void doCommonCheckForCharSequence(CharSequence[] values) {
    isNotNull();
    if (values == null) throw arrayOfValuesToLookForIsNull();
    if (values.length == 0) throw arrayOfValuesToLookForIsEmpty();
    if (values.length == 1) {
      checkCharSequenceIsNotNull(values[0]);
    } else {
      for (int i = 0; i < values.length; i++) {
        requireNonNull(values[i], "Expecting CharSequence elements not to be null but found one at index " + i);
      }
    }
  }

}
