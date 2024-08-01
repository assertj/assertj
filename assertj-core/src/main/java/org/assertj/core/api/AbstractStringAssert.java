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

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ShouldBeNumeric.shouldBeNumeric;
import static org.assertj.core.error.ShouldBeNumeric.NumericType.BYTE;
import static org.assertj.core.error.ShouldBeNumeric.NumericType.DOUBLE;
import static org.assertj.core.error.ShouldBeNumeric.NumericType.FLOAT;
import static org.assertj.core.error.ShouldBeNumeric.NumericType.INTEGER;
import static org.assertj.core.error.ShouldBeNumeric.NumericType.LONG;
import static org.assertj.core.error.ShouldBeNumeric.NumericType.SHORT;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Comparator;

import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public class AbstractStringAssert<SELF extends AbstractStringAssert<SELF>> extends AbstractCharSequenceAssert<SELF, String> {

  @VisibleForTesting
  Failures failures = Failures.instance();

  protected AbstractStringAssert(String actual, Class<?> selfType) {
    super(actual, selfType);
  }

  @VisibleForTesting
  Comparables comparables = new Comparables();

  /**
   * Verifies that the actual value is less than the given {@link String} according to {@link String#compareTo(String)}.
   * <p>
   * Note that it is possible to change the comparison strategy with {@link AbstractAssert#usingComparator(Comparator) usingComparator}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isLessThan(&quot;bcd&quot;)
   *                  .isLessThan(&quot;b&quot;)
   *                  .isLessThan(&quot;abca&quot;)
   *                  .usingComparator(String.CASE_INSENSITIVE_ORDER)
   *                  .isLessThan(&quot;BCD&quot;);
   *
   * // assertions fail
   * assertThat(&quot;abc&quot;).isLessThan(&quot;ab&quot;);
   * assertThat(&quot;abc&quot;).isLessThan(&quot;abc&quot;);
   * assertThat(&quot;abc&quot;).isLessThan(&quot;ABC&quot;);</code></pre>
   *
   * @param other the {@link String} to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than or equal to the given one.
   *
   * @since 3.11.0
   */
  public SELF isLessThan(String other) {
    comparables.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given {@link String} according to {@link String#compareTo(String)}.
   * <p>
   * Note that it is possible to change the comparison strategy with {@link AbstractAssert#usingComparator(Comparator) usingComparator}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isLessThanOrEqualTo(&quot;bcd&quot;)
   *                  .isLessThanOrEqualTo(&quot;abc&quot;)
   *                  .isLessThanOrEqualTo(&quot;b&quot;)
   *                  .isLessThanOrEqualTo(&quot;abca&quot;)
   *                  .usingComparator(String.CASE_INSENSITIVE_ORDER)
   *                  .isLessThanOrEqualTo(&quot;ABC&quot;);
   *
   * // assertions fail
   * assertThat(&quot;abc&quot;).isLessThanOrEqualTo(&quot;ab&quot;);
   * assertThat(&quot;abc&quot;).isLessThanOrEqualTo(&quot;ABC&quot;);</code></pre>
   *
   * @param other the {@link String} to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   *
   * @since 3.11.0
   */
  public SELF isLessThanOrEqualTo(String other) {
    comparables.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given {@link String} according to {@link String#compareTo(String)}.
   * <p>
   * Note that it is possible to change the comparison strategy with {@link AbstractAssert#usingComparator(Comparator) usingComparator}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;xyz&quot;).isGreaterThan(&quot;abc&quot;)
   *                  .isGreaterThan(&quot;xy&quot;)
   *                  .isGreaterThan(&quot;ABC&quot;);
   * assertThat(&quot;XYZ&quot;).usingComparator(String.CASE_INSENSITIVE_ORDER)
   *                  .isGreaterThan(&quot;abc&quot;);
   *
   * // assertions fail
   * assertThat(&quot;xyz&quot;).isGreaterThan(&quot;xyzz&quot;);
   * assertThat(&quot;xyz&quot;).isGreaterThan(&quot;xyz&quot;);
   * assertThat(&quot;xyz&quot;).isGreaterThan(&quot;z&quot;);</code></pre>
   *
   * @param other the {@link String} to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than or equal to the given one.
   *
   * @since 3.11.0
   */
  public SELF isGreaterThan(String other) {
    comparables.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given {@link String} according to {@link String#compareTo(String)}.
   * <p>
   * Note that it is possible to change the comparison strategy with {@link AbstractAssert#usingComparator(Comparator) usingComparator}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;xyz&quot;).isGreaterThanOrEqualTo(&quot;abc&quot;)
   *                  .isGreaterThanOrEqualTo(&quot;xyz&quot;)
   *                  .isGreaterThanOrEqualTo(&quot;xy&quot;)
   *                  .isGreaterThanOrEqualTo(&quot;ABC&quot;);
   * assertThat(&quot;XYZ&quot;).usingComparator(String.CASE_INSENSITIVE_ORDER)
   *                  .isGreaterThanOrEqualTo(&quot;abc&quot;);
   *
   * // assertions fail
   * assertThat(&quot;xyz&quot;).isGreaterThanOrEqualTo(&quot;xyzz&quot;);
   * assertThat(&quot;xyz&quot;).isGreaterThanOrEqualTo(&quot;z&quot;);</code></pre>
   *
   * @param other the {@link String} to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   *
   * @since 3.11.0
   */
  public SELF isGreaterThanOrEqualTo(String other) {
    comparables.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is in [start, end] range (start included, end included) according to {@link String#compareTo(String)}.
   * <p>
   * Note that it is possible to change the comparison strategy with {@link AbstractAssert#usingComparator(Comparator) usingComparator}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;ab&quot;).isBetween(&quot;aa&quot;, &quot;ac&quot;)
   *                 .isBetween(&quot;ab&quot;, &quot;ac&quot;)
   *                 .isBetween(&quot;aa&quot;, &quot;ab&quot;)
   *                 .isBetween(&quot;ab&quot;, &quot;ab&quot;)
   *                 .isBetween(&quot;a&quot;, &quot;c&quot;)
   *                 .usingComparator(String.CASE_INSENSITIVE_ORDER)
   *                 .isBetween("AA", "AC");
   *
   * // assertions fail
   * assertThat(&quot;ab&quot;).isBetween(&quot;ac&quot;, &quot;bc&quot;);
   * assertThat(&quot;ab&quot;).isBetween(&quot;abc&quot;, &quot;ac&quot;);</code></pre>
   *
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in the [start, end] range.
   *
   * @since 3.11.0
   */
  public SELF isBetween(String startInclusive, String endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return myself;
  }

  /**
   * Verifies that the actual value is strictly in ]start, end[ range (start excluded, end excluded) according to {@link String#compareTo(String)}.
   * <p>
   * Note that it is possible to change the comparison strategy with {@link AbstractAssert#usingComparator(Comparator) usingComparator}.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;ab&quot;).isStrictlyBetween(&quot;aa&quot;, &quot;ac&quot;)
   *                 .isStrictlyBetween(&quot;a&quot;, &quot;c&quot;)
   *                 .usingComparator(String.CASE_INSENSITIVE_ORDER)
   *                 .isStrictlyBetween("AA", "AC");
   *
   * // assertions fail
   * assertThat(&quot;ab&quot;).isStrictlyBetween(&quot;ac&quot;, &quot;bc&quot;);
   * assertThat(&quot;ab&quot;).isStrictlyBetween(&quot;ab&quot;, &quot;ac&quot;);
   * assertThat(&quot;ab&quot;).isStrictlyBetween(&quot;aa&quot;, &quot;ab&quot;);
   * assertThat(&quot;ab&quot;).isStrictlyBetween(&quot;abc&quot;, &quot;ac&quot;);</code></pre>
   *
   * @param startExclusive the start value (exclusive), expected not to be null.
   * @param endExclusive the end value (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ range.
   *
   * @since 3.11.0
   */
  public SELF isStrictlyBetween(String startExclusive, String endExclusive) {
    comparables.assertIsBetween(info, actual, startExclusive, endExclusive, false, false);
    return myself;
  }

  /**
   * Verifies that the actual value is a valid Base64 encoded string.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(&quot;QXNzZXJ0Sg==&quot;).isBase64();
   *
   * // assertion succeeds even without padding as it is optional by specification
   * assertThat(&quot;QXNzZXJ0Sg&quot;).isBase64();
   *
   * // assertion fails as it has invalid Base64 characters
   * assertThat(&quot;inv@lid&quot;).isBase64();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not a valid Base64 encoded string.
   *
   * @since 3.16.0
   */
  public SELF isBase64() {
    strings.assertIsBase64(info, actual);
    return myself;
  }

  /**
   * Decodes the actual value as a Base64 encoded string, the decoded bytes becoming the new array under test.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(&quot;QXNzZXJ0Sg==&quot;).asBase64Decoded().containsExactly("AssertJ".getBytes());
   *
   * // assertion succeeds even without padding as it is optional by specification
   * assertThat(&quot;QXNzZXJ0Sg&quot;).asBase64Decoded().containsExactly("AssertJ".getBytes());
   *
   * // assertion fails as it has invalid Base64 characters
   * assertThat(&quot;inv@lid&quot;).asBase64Decoded();</code></pre>
   *
   * @return a new {@link ByteArrayAssert} instance whose array under test is the result of the decoding.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not a valid Base64 encoded string.
   *
   * @since 3.22.0
   */
  @CheckReturnValue
  public AbstractByteArrayAssert<?> asBase64Decoded() {
    isBase64();
    return new ByteArrayAssert(Base64.getDecoder().decode(actual)).withAssertionState(myself);
  }

  /**
   * <p>
   * Decodes the actual value as a Base64 encoded string, the decoded bytes becoming the new array under test.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(&quot;QXNzZXJ0Sg==&quot;).decodedAsBase64().containsExactly("AssertJ".getBytes());
   *
   * // assertion succeeds even without padding as it is optional by specification
   * assertThat(&quot;QXNzZXJ0Sg&quot;).decodedAsBase64().containsExactly("AssertJ".getBytes());
   *
   * // assertion fails as it has invalid Base64 characters
   * assertThat(&quot;inv@lid&quot;).decodedAsBase64();</code></pre>
   *
   * @return a new {@link ByteArrayAssert} instance whose array under test is the result of the decoding.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not a valid Base64 encoded string.
   *
   * @since 3.16.0
   * @deprecated use {@link #asBase64Decoded()} instead.
   */
  @Deprecated
  @CheckReturnValue
  public AbstractByteArrayAssert<?> decodedAsBase64() {
    return asBase64Decoded();
  }

  /**
   * Use the given custom comparator instead of relying on {@link String} natural comparator for the incoming assertions.
   * <p>
   * The custom comparator is bound to an assertion instance, meaning that if a new assertion instance is created
   * it is forgotten and the default ({@link String} natural comparator) is used.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).usingComparator(String.CASE_INSENSITIVE_ORDER)
   *                  .isEqualTo(&quot;Abc&quot;)
   *                  .isEqualTo(&quot;ABC&quot;);
   *
   * // assertion fails as it relies on String natural comparator
   * assertThat(&quot;abc&quot;).isEqualTo(&quot;ABC&quot;);</code></pre>
   *
   * @param customComparator the comparator to use for the incoming assertions.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super String> customComparator) {
    return usingComparator(customComparator, null);
  }

  /**
   * Use the given custom comparator instead of relying on {@link String} natural comparator for the incoming assertions.
   * <p>
   * The custom comparator is bound to an assertion instance, meaning that if a new assertion instance is created
   * it is forgotten and the default ({@link String} natural comparator) is used.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).usingComparator(String.CASE_INSENSITIVE_ORDER, &quot;String case insensitive comparator&quot;)
   *                  .isEqualTo(&quot;Abc&quot;)
   *                  .isEqualTo(&quot;ABC&quot;);
   *
   * // assertion fails as it relies on String natural comparator
   * assertThat(&quot;abc&quot;).isEqualTo(&quot;ABC&quot;);</code></pre>
   *
   * @param customComparator the comparator to use for the incoming assertions.
   * @param customComparatorDescription comparator description to be used in assertion error messages
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super String> customComparator, String customComparatorDescription) {
    this.comparables = new Comparables(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    this.comparables = new Comparables();
    return super.usingDefaultComparator();
  }

  /**
   * Verifies that the actual value is equal to expected build using {@link String#format(String stringTemplate, Object... args)}.
   * <p>
   * Note that for this assertion to be called, <b>you must use a format template with parameters</b> otherwise {@link #isEqualTo(Object)} is called which
   * does not perform any formatting. For example, if you only use {@code %n} in the template they won't be replaced.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(&quot;R2D2&quot;).isEqualTo(&quot;%d%s%d%s&quot;, &quot;R&quot;, 2, &quot;D&quot;, 2);
   *
   * // assertion fails
   * assertThat(&quot;C6PO&quot;).isEqualTo(&quot;%d%s%d%s&quot;, &quot;R&quot;, 2, &quot;D&quot;, 2);
   *
   * // assertion fails with {@link java.lang.NullPointerException}
   * assertThat(&quot;1,A,2&quot;).isEqualTo(null, 1, &quot;A&quot;, 2);
   *
   * // assertion fails with {@link java.util.IllegalFormatException}
   * assertThat(&quot;1&quot;).isEqualTo(&quot;%s%s&quot;, 1); </code></pre>
   *
   * @param expectedStringTemplate the format template used to build the expected String.
   * @param args the arguments referenced by the format specifiers in the format string.
   * @return this assertion object.
   * @throws NullPointerException if stringTemplate parameter is {@code null}.
   * @throws AssertionError if the actual value is {@code null} as the template you provide must not be {@code null}.
   * @throws java.util.IllegalFormatException as in {@link String#format(String, Object...)}, see
   *         <a href="http://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#detail">Details</a> section of the
   *         formatter class specification.
   *
   * @since 3.12.0
   */
  public SELF isEqualTo(String expectedStringTemplate, Object... args) {
    requireNonNull(expectedStringTemplate, "The expectedStringTemplate must not be null");
    return super.isEqualTo(format(expectedStringTemplate, args));
  }

  /**
   * Verifies that the actual value is equal to expected.
   * <p>
   * This method needs to be overridden because otherwise {@link #isEqualTo(String, Object...)} is called from tests in Kotlin without args which breaks whenever the is {@code %} in the string.
   *
   * @param expected the given {@link String} to compare the actual to.
   * @return this assertion object.
   * @see Assert#isEqualTo(java.lang.Object)
   * @since 3.13.0
   */
  public SELF isEqualTo(String expected) {
    return super.isEqualTo(expected);
  }

  /**
   * Parses the actual value as boolean, the parsed boolean becoming the new value under test.
   * <p>
   * Note that only when the string is equal to the string "true", ignoring case, and can have leading and trailing space, the parsed value will be {@code true}.
   * Otherwise, the value will be {@code false}.
   * <p>
   * Examples:
   * <pre><code class='java'> assertThat(&quot;truE&quot;).asBoolean().isTrue();
   * assertThat(&quot;false&quot;).asBoolean().isFalse();
   * assertThat(&quot;foo bar&quot;).asBoolean().isFalse();
   * assertThat((String) null).asBoolean().isFalse(); </code></pre>
   *
   * @return a new {@link BooleanAssert} instance whose value under test is the result of the parse.
   *
   * @since 3.25.0
   */
  public AbstractBooleanAssert<?> asBoolean() {
    return InstanceOfAssertFactories.BOOLEAN.createAssert(Boolean.parseBoolean(actual)).withAssertionState(myself);
  }

  /**
   * Parses the actual value as byte, using radix 10, the parsed byte becoming the new value under test.
   * <p>
   * Examples:
   * <pre><code class='java'>
   * // assertion succeeds:
   * assertThat(&quot;127&quot;).asByte().isEqualTo((byte) 127);
   *
   * // assertions fail as the actual value is null or not a valid byte
   * assertThat((String) null).asByte();
   * assertThat(&quot;1L&quot;).asByte(); </code></pre>
   *
   * @return a new {@link ByteAssert} instance whose value under test is the result of the parse.
   * @throws AssertionError if the actual value is null or not a valid byte.
   *
   * @since 3.25.0
   */
  public AbstractByteAssert<?> asByte() {
    try {
      return InstanceOfAssertFactories.BYTE.createAssert(Byte.parseByte(actual)).withAssertionState(myself);
    } catch (NumberFormatException e) {
      throw failures.failure(info, shouldBeNumeric(actual, BYTE));
    }
  }

  /**
   * Encodes the actual value as byte array using the platform's default charset, the encoded byte array becoming the new value under test.
   * <p>
   * Examples:
   * <pre><code class='java'> assertThat("abc").bytes()
   *                  .isEqualTo(new byte[] {'a', 'b', 'c'});
   *
   * assertThat("").bytes()
   *               .isEmpty(); </code></pre>
   *
   * @return a new {@link AbstractByteArrayAssert} instance whose value under test is the result of parsing the string.
   * @throws AssertionError if the actual string is {@code null}.
   *
   * @since 3.26.0
   */
  public AbstractByteArrayAssert<?> bytes() {
    isNotNull();
    return InstanceOfAssertFactories.BYTE_ARRAY.createAssert(actual.getBytes()).withAssertionState(myself);
  }

  /**
   * Encodes the actual value as byte array using a specific {@link Charset}, the encoded byte array becoming the new value under test.
   * <p>
   * Examples:
   * <pre><code class='java'> assertThat("abc").bytes(StandardCharsets.US_ASCII)
   *                  .isEqualTo("abc".getBytes(StandardCharsets.US_ASCII));
   * 
   * assertThat("").bytes(StandardCharsets.US_ASCII)
   *               .isEmpty(); </code></pre>
   *
   * @param charset the {@link Charset} to be used to encode the string.
   * @return a new {@link AbstractByteArrayAssert} instance whose value under test is the result of parsing the string.
   * @throws NullPointerException if charset parameter is {@code null}.
   * @throws AssertionError if the actual string is {@code null}.
   *
   * @since 3.26.0
   */
  public AbstractByteArrayAssert<?> bytes(Charset charset) {
    isNotNull();
    byte[] bytes = actual.getBytes(requireNonNull(charset, "The charset must not be null"));
    return InstanceOfAssertFactories.BYTE_ARRAY.createAssert(bytes).withAssertionState(myself);
  }

  /**
   * Encodes the actual value as byte array using a specific {@link Charset}, the encoded byte array becoming the new value under test.
   * <p>
   * Examples:
   * <pre><code class='java'> assertThat("abc").bytes(StandardCharsets.US_ASCII)
   *                  .isEqualTo("abc".getBytes(StandardCharsets.US_ASCII));
   *
   * assertThat("").bytes(StandardCharsets.US_ASCII)
   *               .isEmpty(); </code></pre>
   *
   * @param charsetName the Charset to be used to encode the string.
   * @return a new {@link AbstractByteArrayAssert} instance whose value under test is the result of parsing the string.
   * @throws NullPointerException if named charset parameter is {@code null}.
   * @throws AssertionError if the actual string is {@code null} or if the named charset parameter is not supported.
   *
   * @since 3.26.0
   */
  public AbstractByteArrayAssert<?> bytes(String charsetName) {
    isNotNull();
    try {
      byte[] bytes = actual.getBytes(requireNonNull(charsetName, "The charsetName must not be null"));
      return InstanceOfAssertFactories.BYTE_ARRAY.createAssert(bytes).withAssertionState(myself);
    } catch (UnsupportedEncodingException e) {
      throw failures.failure(charsetName + " is not a supported Charset");
    }
  }

  /**
   * Parses the actual value as short, using radix 10, the parsed short becoming the new value under test.
   * <p>
   * Examples:
   * <pre><code class='java'>
   * // assertion succeeds:
   * assertThat(&quot;32767&quot;).asShort().isEqualTo((short) 32767);
   *
   * // assertions fail as the actual value is null or not a valid short:
   * assertThat((String) null).asShort();
   * assertThat(&quot;-32769&quot;).asShort(); </code></pre>
   *
   * @return a new {@link ShortAssert} instance whose value under test is the result of the parse.
   * @throws AssertionError if the actual value is null or not a valid short.
   *
   * @since 3.25.0
   */
  public AbstractShortAssert<?> asShort() {
    try {
      return InstanceOfAssertFactories.SHORT.createAssert(Short.parseShort(actual)).withAssertionState(myself);
    } catch (NumberFormatException e) {
      throw failures.failure(info, shouldBeNumeric(actual, SHORT));
    }
  }

  /**
   * Parses the actual value as integer, using radix 10, the parsed integer becoming the new value under test.
   * <p>
   * Examples:
   * <pre><code class='java'>
   * // assertion succeeds:
   * assertThat(&quot;2147483647&quot;).asInt().isEqualTo(2147483647);
   *
   * // assertions fail as the actual value is null or not a valid int:
   * assertThat((String) null).asInt();
   * assertThat(&quot;1e100&quot;).asInt();</code></pre>
   *
   * @return a new {@link IntegerAssert} instance whose value under test is the result of the parse.
   * @throws AssertionError if the actual value is null or not a valid int.
   *
   * @since 3.25.0
   */
  public AbstractIntegerAssert<?> asInt() {
    try {
      return InstanceOfAssertFactories.INTEGER.createAssert(Integer.parseInt(actual)).withAssertionState(myself);
    } catch (NumberFormatException e) {
      throw failures.failure(info, shouldBeNumeric(actual, INTEGER));
    }
  }

  /**
   * Parses the actual value as long, using radix 10, the parsed long becoming the new value under test.
   * <p>
   * Examples:
   * <pre><code class='java'>
   * // assertion succeeds:
   * assertThat(&quot;1&quot;).asLong().isEqualTo(1L);
   *
   * // assertions fail as the actual value is null or not a long:
   * assertThat((String) null).asLong();
   * assertThat(&quot;1e100&quot;).asLong();</code></pre>
   *
   * @return a new {@link LongAssert} instance whose value under test is the result of the parse.
   * @throws AssertionError if the actual value is null or not a valid long.
   *
   * @since 3.25.0
   */
  public AbstractLongAssert<?> asLong() {
    try {
      return InstanceOfAssertFactories.LONG.createAssert(Long.parseLong(actual)).withAssertionState(myself);
    } catch (NumberFormatException e) {
      throw failures.failure(info, shouldBeNumeric(actual, LONG));
    }
  }

  /**
   * Parses the actual value as float, the parsed float becoming the new value under test.
   * <p>
   * Examples:
   * <pre><code class='java'>
   * // assertion succeeds:
   * assertThat(&quot;1.2f&quot;).asFloat().isCloseTo(1.2f, withinPercentage(0.01));
   *
   * // assertions fail as the actual value is null or not a float:
   * assertThat((String) null).asFloat();
   * assertThat(&quot;foo&quot;).asFloat();</code></pre>
   *
   * @return a new {@link FloatAssert} instance whose value under test is the result of the parse.
   * @throws AssertionError if the actual value is null or not a parseable float.
   *
   * @since 3.25.0
   */
  public AbstractFloatAssert<?> asFloat() {
    try {
      return InstanceOfAssertFactories.FLOAT.createAssert(Float.parseFloat(actual)).withAssertionState(myself);
    } catch (NumberFormatException | NullPointerException e) {
      throw failures.failure(info, shouldBeNumeric(actual, FLOAT));
    }
  }

  /**
   * Parses the actual value as double, the parsed double becoming the new value under test.
   * <p>
   * Examples:
   * <pre><code class='java'>
   * // assertion succeeds:
   * assertThat(&quot;1.2e308&quot;).asDouble().isCloseTo(1.2e308, withinPercentage(0.001));
   *
   * // assertions fail as the actual value is null or not a double:
   * assertThat((String) null).asDouble();
   * assertThat(&quot;foo&quot;).asDouble(); </code></pre>
   *
   * @return a new {@link DoubleAssert} instance whose value under test is the result of the parse.
   * @throws AssertionError if the actual value is null or not a parseable double.
   *
   * @since 3.25.0
   */
  public AbstractDoubleAssert<?> asDouble() {
    try {
      return InstanceOfAssertFactories.DOUBLE.createAssert(Double.parseDouble(actual)).withAssertionState(myself);
    } catch (NumberFormatException | NullPointerException e) {
      throw failures.failure(info, shouldBeNumeric(actual, DOUBLE));
    }
  }
}
