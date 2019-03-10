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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.String.format;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.util.Comparator;

import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public class AbstractStringAssert<SELF extends AbstractStringAssert<SELF>> extends AbstractCharSequenceAssert<SELF, String> {

  public AbstractStringAssert(String actual, Class<?> selfType) {
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
   *                  .usingComparator(CASE_INSENSITIVE)
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
   *                  .usingComparator(CASE_INSENSITIVE)
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
   * assertThat(&quot;XYZ&quot;).usingComparator(CASE_INSENSITIVE)
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
   * assertThat(&quot;XYZ&quot;).usingComparator(CASE_INSENSITIVE)
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
   *                 .usingComparator(CASE_INSENSITIVE)
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
   *                 .usingComparator(CASE_INSENSITIVE)
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
   * Use the given custom comparator instead of relying on {@link String} natural comparator for the incoming assertions.
   * <p>
   * The custom comparator is bound to an assertion instance, meaning that if a new assertion instance is created
   * it is forgotten and the default ({@link String} natural comparator) is used.
   * <p>
   * Examples :
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).usingComparator(CASE_INSENSITIVE)
   *                  .isEqualTo(&quot;Abc&quot;)
   *                  .isEqualTo(&quot;ABC&quot;);
   *
   * // assertion fails as it relies on String natural comparator
   * assertThat(&quot;abc&quot;).isEqualTo(&quot;ABC&quot;);</code></pre>
   *
   * @param customComparator the comparator to use for the incoming assertions.
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
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
   * assertThat(&quot;abc&quot;).usingComparator(CASE_INSENSITIVE, &quot;String case insensitive comparator&quot;)
   *                  .isEqualTo(&quot;Abc&quot;)
   *                  .isEqualTo(&quot;ABC&quot;);
   *
   * // assertion fails as it relies on String natural comparator
   * assertThat(&quot;abc&quot;).isEqualTo(&quot;ABC&quot;);</code></pre>
   *
   * @param customComparator the comparator to use for the incoming assertions.
   * @param customComparatorDescription comparator description to be used in assertion error messages
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
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
   * Note that for this assertion to be called, <b>you must use a format template with parameters</b> otherwise {@link #isEqualTo(Object)} is callled which
   * does not perform any formatting. For example, it you only use {@code %n} in the template they won't be replaced.
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
   * @throws NullPointerException if stringTemplate parameter is {@code null}.
   * @throws AssertionError if the actual value is {@code null} as the template you provide must not be {@code null}.
   * @throws java.util.IllegalFormatException as in {@link String#format(String, Object...)}, see
   *         <a href="http://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html#detail">Details</a> section of the
   *         formatter class specification.
   * @return this assertion object.
   *
   * @since 3.12.0
   */
  public SELF isEqualTo(String expectedStringTemplate, Object... args) {
    checkNotNull(expectedStringTemplate, "The expectedStringTemplate must not be null");
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
}
