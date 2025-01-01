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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeAfter.shouldBeAfter;
import static org.assertj.core.error.ShouldBeAfterOrEqualTo.shouldBeAfterOrEqualTo;
import static org.assertj.core.error.ShouldBeBefore.shouldBeBefore;
import static org.assertj.core.error.ShouldBeBeforeOrEqualTo.shouldBeBeforeOrEqualTo;
import static org.assertj.core.error.ShouldBeCurrentYearMonth.shouldBeCurrentYearMonth;
import static org.assertj.core.error.ShouldBeInTheFuture.shouldBeInTheFuture;
import static org.assertj.core.error.ShouldBeInThePast.shouldBeInThePast;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveMonth;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import org.assertj.core.internal.Objects;

/**
 * Base class for all implementations of assertions for {@link YearMonth} type
 * from new Date &amp; Time API introduced in Java 8.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @since 3.26.0
 */
@SuppressWarnings("GrazieInspection")
public abstract class AbstractYearMonthAssert<SELF extends AbstractYearMonthAssert<SELF>>
    extends AbstractTemporalAssert<SELF, YearMonth> {

  /**
   * Creates a new <code>{@link AbstractYearMonthAssert}</code>.
   *
   * @param selfType the "self-type".
   * @param actual the actual value to verify.
   */
  protected AbstractYearMonthAssert(YearMonth actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code YearMonth} is strictly before the given one.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(YearMonth.of(2000, 1)).isBefore(YearMonth.of(2000, 2));</code></pre>
   *
   * @param other the other {@code YearMonth} to compare to, not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if other {@code YearMonth} is {@code null}.
   * @throws AssertionError if the actual {@code YearMonth} is not strictly before the given one.
   */
  public SELF isBefore(YearMonth other) {
    Objects.instance().assertNotNull(info, actual);
    assertYearMonthParameterIsNotNull(other);
    if (!actual.isBefore(other)) throwAssertionError(shouldBeBefore(actual, other));
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is strictly before the given one.
   * <p>
   * Same assertion as {@link #isBefore(YearMonth)} but the {@code YearMonth} is built from the given string,
   * which must follow the {@link DateTimeFormatter} pattern <code>uuuu-MM</code>
   * to allow calling {@link YearMonth#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> // use string representation of YearMonth
   * assertThat(YearMonth.of(2000, 1)).isBefore("2000-02");</code></pre>
   *
   * @param otherYearMonthAsString string representation of the other {@code YearMonth} to compare to, not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if the given string is {@code null}.
   * @throws DateTimeParseException if the given string cannot be parsed to a {@code YearMonth}.
   * @throws AssertionError if the actual {@code YearMonth} is not strictly before the {@code YearMonth} built
   *           from given string.
   */
  public SELF isBefore(String otherYearMonthAsString) {
    assertYearMonthAsStringParameterIsNotNull(otherYearMonthAsString);
    return isBefore(parse(otherYearMonthAsString));
  }

  /**
   * Verifies that the actual {@code YearMonth} is before or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(YearMonth.of(2000, 1)).isBeforeOrEqualTo(YearMonth.of(2000, 1))
   *                                  .isBeforeOrEqualTo(YearMonth.of(2000, 2));</code></pre>
   *
   * @param other the other {@code YearMonth} to compare to, not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if other {@code YearMonth} is {@code null}.
   * @throws AssertionError if the actual {@code YearMonth} is not before or equal to the given one.
   */
  public SELF isBeforeOrEqualTo(YearMonth other) {
    Objects.instance().assertNotNull(info, actual);
    assertYearMonthParameterIsNotNull(other);
    if (actual.isAfter(other)) {
      throwAssertionError(shouldBeBeforeOrEqualTo(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is before or equal to the given one.
   * <p>
   * Same assertion as {@link #isBeforeOrEqualTo(YearMonth)} but the {@code YearMonth} is built from given
   * string, which must follow the {@link DateTimeFormatter} pattern <code>uuuu-MM</code>
   * to allow calling {@link YearMonth#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> // use string representation of YearMonth
   * assertThat(YearMonth.of(2000, 1)).isBeforeOrEqualTo("2000-01")
   *                                  .isBeforeOrEqualTo("2000-02");</code></pre>
   *
   * @param otherYearMonthAsString string representation of the other {@code YearMonth} to compare to, not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if the given string is {@code null}.
   * @throws DateTimeParseException if the given string cannot be parsed to a {@code YearMonth}.
   * @throws AssertionError if the actual {@code YearMonth} is not before or equal to the {@code YearMonth} built from
   *           given string.
   */
  public SELF isBeforeOrEqualTo(String otherYearMonthAsString) {
    assertYearMonthAsStringParameterIsNotNull(otherYearMonthAsString);
    return isBeforeOrEqualTo(parse(otherYearMonthAsString));
  }

  /**
   * Verifies that the actual {@code YearMonth} is after or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(YearMonth.of(2000, 1)).isAfterOrEqualTo(YearMonth.of(2000, 1))
   *                                  .isAfterOrEqualTo(parse("1999-12"));</code></pre>
   *
   * @param other the other {@code YearMonth} to compare to, not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if other {@code YearMonth} is {@code null}.
   * @throws AssertionError if the actual {@code YearMonth} is not after or equal to the given one.
   */
  public SELF isAfterOrEqualTo(YearMonth other) {
    Objects.instance().assertNotNull(info, actual);
    assertYearMonthParameterIsNotNull(other);
    if (actual.isBefore(other)) throwAssertionError(shouldBeAfterOrEqualTo(actual, other));
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is after or equal to the given one.
   * <p>
   * Same assertion as {@link #isAfterOrEqualTo(YearMonth)} but the {@code YearMonth} is built from given
   * string, which must follow the {@link DateTimeFormatter} pattern <code>uuuu-MM</code>
   * to allow calling {@link YearMonth#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> // use string representation of YearMonth
   * assertThat(YearMonth.of(2000, 1)).isAfterOrEqualTo("2000-01")
   *                                  .isAfterOrEqualTo("1999-12");</code></pre>
   *
   * @param otherYearMonthAsString string representation of the other {@code YearMonth} to compare to, not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if the given string is {@code null}.
   * @throws DateTimeParseException if the given string cannot be parsed to a {@code YearMonth}.
   * @throws AssertionError if the actual {@code YearMonth} is not after or equal to the {@code YearMonth} built from
   *           given string.
   */
  public SELF isAfterOrEqualTo(String otherYearMonthAsString) {
    assertYearMonthAsStringParameterIsNotNull(otherYearMonthAsString);
    return isAfterOrEqualTo(parse(otherYearMonthAsString));
  }

  /**
   * Verifies that the actual {@code YearMonth} is strictly after the given one.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(YearMonth.of(2000, 1)).isAfter(parse("1999-12"));</code></pre>
   *
   * @param other the other {@code YearMonth} to compare to, not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if other {@code YearMonth} is {@code null}.
   * @throws AssertionError if the actual {@code YearMonth} is not strictly after the given one.
   */
  public SELF isAfter(YearMonth other) {
    Objects.instance().assertNotNull(info, actual);
    assertYearMonthParameterIsNotNull(other);
    if (!actual.isAfter(other)) throwAssertionError(shouldBeAfter(actual, other));
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is strictly after the given one.
   * <p>
   * Same assertion as {@link #isAfter(YearMonth)} but the {@code YearMonth} is built from given
   * string, which must follow the {@link DateTimeFormatter} pattern <code>uuuu-MM</code>
   * to allow calling {@link YearMonth#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> // use string representation of YearMonth
   * assertThat(YearMonth.of(2000, 1)).isAfter("1999-12");</code></pre>
   *
   * @param otherYearMonthAsString string representation of the other {@code YearMonth} to compare to, not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if the given string is {@code null}.
   * @throws DateTimeParseException if the given string cannot be parsed to a {@code YearMonth}.
   * @throws AssertionError if the actual {@code YearMonth} is not strictly after the {@code YearMonth} built
   *           from given string.
   */
  public SELF isAfter(String otherYearMonthAsString) {
    assertYearMonthAsStringParameterIsNotNull(otherYearMonthAsString);
    return isAfter(parse(otherYearMonthAsString));
  }

  /**
   * Verifies that the actual {@code YearMonth} is equal to the given one.
   * <p>
   * Same assertion as {@link #isEqualTo(Object)} but the {@code YearMonth} is built from given
   * string, which must follow the {@link DateTimeFormatter} pattern <code>uuuu-MM</code>
   * to allow calling {@link YearMonth#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> // use string representation of YearMonth
   * assertThat(YearMonth.of(2000, 1)).isEqualTo("2000-01");</code></pre>
   *
   * @param otherYearMonthAsString string representation of the other {@code YearMonth} to compare to, not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if the given string is {@code null}.
   * @throws DateTimeParseException if the given string cannot be parsed to a {@code YearMonth}.
   * @throws AssertionError if the actual {@code YearMonth} is not equal to the {@code YearMonth} built from
   *           given string.
   */
  public SELF isEqualTo(String otherYearMonthAsString) {
    assertYearMonthAsStringParameterIsNotNull(otherYearMonthAsString);
    return isEqualTo(parse(otherYearMonthAsString));
  }

  /**
   * Verifies that the actual {@code YearMonth} is not equal to the given one.
   * <p>
   * Same assertion as {@link #isNotEqualTo(Object)} but the {@code YearMonth} is built from given
   * string, which must follow the {@link DateTimeFormatter} pattern <code>uuuu-MM</code>
   * to allow calling {@link YearMonth#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> // use string representation of YearMonth
   * assertThat(YearMonth.of(2000, 1)).isNotEqualTo("2000-02");</code></pre>
   *
   * @param otherYearMonthAsString string representation of the other {@code YearMonth} to compare to, not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if the given string is {@code null}.
   * @throws DateTimeParseException if the given string cannot be parsed to a {@code YearMonth}.
   * @throws AssertionError if the actual {@code YearMonth} is equal to the {@code YearMonth} built from given string.
   */
  public SELF isNotEqualTo(String otherYearMonthAsString) {
    assertYearMonthAsStringParameterIsNotNull(otherYearMonthAsString);
    return isNotEqualTo(parse(otherYearMonthAsString));
  }

  /**
   * Verifies that the actual {@code YearMonth} is present in the given array of values.
   * <p>
   * Same assertion as {@link #isIn(Object...)} but the {@code YearMonth}s are built from given
   * strings, which must follow the {@link DateTimeFormatter} pattern <code>uuuu-MM</code>
   * to allow calling {@link YearMonth#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> // use string representation of YearMonth
   * assertThat(YearMonth.of(2000, 1)).isIn("1999-12", "2000-01");</code></pre>
   *
   * @param otherYearMonthsAsString string array representing {@code YearMonth}s.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if any of the given strings is {@code null}.
   * @throws DateTimeParseException if any of the given strings cannot be parsed to a {@code YearMonth}.
   * @throws AssertionError if the actual {@code YearMonth} is not in the {@code YearMonth}s built from given strings.
   */
  public SELF isIn(String... otherYearMonthsAsString) {
    checkIsNotNullAndNotEmpty(otherYearMonthsAsString);
    return isIn(convertToYearMonthArray(otherYearMonthsAsString));
  }

  /**
   * Verifies that the actual {@code YearMonth} is not present in the given array of values.
   * <p>
   * Same assertion as {@link #isNotIn(Object...)} but the {@code YearMonth}s are built from given
   * strings, which must follow the {@link DateTimeFormatter} pattern <code>uuuu-MM</code>
   * to allow calling {@link YearMonth#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> // use string representation of YearMonth
   * assertThat(YearMonth.of(2000, 1)).isNotIn("1999-12", "2000-02");</code></pre>
   *
   * @param otherYearMonthsAsString Array of string representing a {@code YearMonth}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws IllegalArgumentException if any of the given strings is {@code null}.
   * @throws DateTimeParseException if any of the given strings cannot be parsed to a {@code YearMonth}.
   * @throws AssertionError if the actual {@code YearMonth} is in the {@code YearMonth}s built from given strings.
   */
  public SELF isNotIn(String... otherYearMonthsAsString) {
    checkIsNotNullAndNotEmpty(otherYearMonthsAsString);
    return isNotIn(convertToYearMonthArray(otherYearMonthsAsString));
  }

  /**
   * Verifies that the actual {@code YearMonth} is strictly in the past.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(YearMonth.now().minusMonths(1)).isInThePast();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws AssertionError if the actual {@code YearMonth} is not in the past.
   */
  public SELF isInThePast() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isBefore(YearMonth.now())) throwAssertionError(shouldBeInThePast(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is the current {@code YearMonth}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(YearMonth.now()).isCurrentYearMonth();
   *
   * // assertion fails:
   * assertThat(theFellowshipOfTheRing.getReleaseDate()).isCurrentYearMonth();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws AssertionError if the actual {@code YearMonth} is not the current {@code YearMonth}.
   */
  public SELF isCurrentYearMonth() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.equals(YearMonth.now())) throwAssertionError(shouldBeCurrentYearMonth(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is strictly in the future.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(YearMonth.now().plusMonths(1)).isInTheFuture();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws AssertionError if the actual {@code YearMonth} is not in the future.
   */
  public SELF isInTheFuture() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isAfter(YearMonth.now())) throwAssertionError(shouldBeInTheFuture(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is in the [start, end] period (start and end included).
   * <p>
   * Example:
   * <pre><code class='java'> YearMonth yearMonth = YearMonth.now();
   *
   * // assertions succeed:
   * assertThat(yearMonth).isBetween(yearMonth.minusMonths(1), yearMonth.plusMonths(1))
   *                      .isBetween(yearMonth, yearMonth.plusMonths(1))
   *                      .isBetween(yearMonth.minusMonths(1), yearMonth)
   *                      .isBetween(yearMonth, yearMonth);
   *
   * // assertions fail:
   * assertThat(yearMonth).isBetween(yearMonth.minusMonths(10), yearMonth.minusMonths(1));
   * assertThat(yearMonth).isBetween(yearMonth.plusMonths(1), yearMonth.plusMonths(10));</code></pre>
   *
   * @param startInclusive the start value (inclusive), not null.
   * @param endInclusive the end value (inclusive), not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in [start, end] period.
   */
  public SELF isBetween(YearMonth startInclusive, YearMonth endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is in the [start, end] period (start and end included).
   * <p>
   * Same assertion as {@link #isBetween(YearMonth, YearMonth)} but the {@code YearMonth}s are built from given
   * strings, which must follow the {@link DateTimeFormatter} pattern <code>uuuu-MM</code>
   * to allow calling {@link YearMonth#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> YearMonth january2000 = YearMonth.parse("2000-01");
   *
   * // assertions succeed:
   * assertThat(january2000).isBetween("1999-01", "2001-01")
   *                        .isBetween("2000-01", "2001-01")
   *                        .isBetween("1999-01", "2000-01")
   *                        .isBetween("2000-01", "2000-01");
   *
   * // assertion fails:
   * assertThat(january2000).isBetween("1999-01", "1999-12");</code></pre>
   *
   * @param startInclusive the start value (inclusive), not null.
   * @param endInclusive the end value (inclusive), not null.
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws DateTimeParseException if any of the given string can't be converted to a {@code YearMonth}.
   * @throws AssertionError if the actual value is not in [start, end] period.
   */
  public SELF isBetween(String startInclusive, String endInclusive) {
    return isBetween(parse(startInclusive), parse(endInclusive));
  }

  /**
   * Verifies that the actual {@code YearMonth} is in the ]start, end[ period (start and end excluded).
   * <p>
   * Example:
   * <pre><code class='java'> YearMonth yearMonth = YearMonth.now();
   *
   * // assertion succeeds:
   * assertThat(yearMonth).isStrictlyBetween(yearMonth.minusMonths(1), yearMonth.plusMonths(1));
   *
   * // assertions fail:
   * assertThat(yearMonth).isStrictlyBetween(yearMonth.minusMonths(10), yearMonth.minusMonths(1));
   * assertThat(yearMonth).isStrictlyBetween(yearMonth.plusMonths(1), yearMonth.plusMonths(10));
   * assertThat(yearMonth).isStrictlyBetween(yearMonth, yearMonth.plusMonths(1));
   * assertThat(yearMonth).isStrictlyBetween(yearMonth.minusMonths(1), yearMonth);</code></pre>
   *
   * @param startExclusive the start value (exclusive), not null.
   * @param endExclusive the end value (exclusive), not null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ period.
   */
  public SELF isStrictlyBetween(YearMonth startExclusive, YearMonth endExclusive) {
    comparables.assertIsBetween(info, actual, startExclusive, endExclusive, false, false);
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is in the ]start, end[ period (start and end excluded).
   * <p>
   * Same assertion as {@link #isStrictlyBetween(YearMonth, YearMonth)} but the {@code YearMonth}s are built from given
   * strings, which must follow the {@link DateTimeFormatter} pattern <code>uuuu-MM</code>
   * to allow calling {@link YearMonth#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> YearMonth january2000 = YearMonth.parse("2000-01");
   *
   * // assertion succeeds:
   * assertThat(january2000).isStrictlyBetween("1999-01", "2001-01");
   *
   * // assertions fail:
   * assertThat(january2000).isStrictlyBetween("1999-01", "1999-12");
   * assertThat(january2000).isStrictlyBetween("2000-01", "2001-01");
   * assertThat(january2000).isStrictlyBetween("1999-01", "2000-01");</code></pre>
   *
   * @param startExclusive the start value (exclusive), not null.
   * @param endExclusive the end value (exclusive), not null.
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws DateTimeParseException if any of the given string can't be converted to a {@code YearMonth}.
   * @throws AssertionError if the actual value is not in ]start, end[ period.
   */
  public SELF isStrictlyBetween(String startExclusive, String endExclusive) {
    return isStrictlyBetween(parse(startExclusive), parse(endExclusive));
  }

  /**
   * Verifies that the actual {@code YearMonth} is in the given year.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(YearMonth.of(2000, 12)).hasYear(2000);
   *
   * // assertion fails:
   * assertThat(YearMonth.of(2000, 12)).hasYear(2001);</code></pre>
   *
   * @param year the given year.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws AssertionError if the actual {@code YearMonth} is not in the given year.
   */
  public SELF hasYear(int year) {
    Objects.instance().assertNotNull(info, actual);
    if (actual.getYear() != year) throwAssertionError(shouldHaveDateField(actual, "year", year));
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is in the given month.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(YearMonth.of(2000, 12)).hasMonth(Month.DECEMBER);
   *
   * // assertion fails:
   * assertThat(YearMonth.of(2000, 12)).hasMonth(Month.JANUARY);</code></pre>
   *
   * @param month the given {@link Month}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws AssertionError if the actual {@code YearMonth} is not in the given month.
   */
  public SELF hasMonth(Month month) {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.getMonth().equals(month)) throwAssertionError(shouldHaveMonth(actual, month));
    return myself;
  }

  /**
   * Verifies that the actual {@code YearMonth} is in the given month.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(YearMonth.of(2000, 12)).hasMonthValue(12);
   *
   * // assertion fails:
   * assertThat(YearMonth.of(2000, 12)).hasMonthValue(11);</code></pre>
   *
   * @param month the given month's value between 1 and 12 inclusive.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code YearMonth} is {@code null}.
   * @throws AssertionError if the actual {@code YearMonth} is not in the given month.
   */
  public SELF hasMonthValue(int month) {
    Objects.instance().assertNotNull(info, actual);
    if (actual.getMonthValue() != month) throwAssertionError(shouldHaveDateField(actual, "month", month));
    return myself;
  }

  /**
   * Obtains an instance of {@link YearMonth} from a text string such as 2007-12.
   *
   * @see YearMonth#parse(CharSequence)
   */
  @Override
  protected YearMonth parse(String yearMonthAsString) {
    return YearMonth.parse(yearMonthAsString);
  }

  /**
   * Check that the {@code YearMonth} to compare actual {@code YearMonth} to is not null, in that case throws a
   * {@link IllegalArgumentException} with an explicit message.
   *
   * @param other the {@code YearMonth} to check.
   * @throws IllegalArgumentException with an explicit message if the given {@code YearMonth} is null.
   */
  private static void assertYearMonthParameterIsNotNull(YearMonth other) {
    checkArgument(other != null, "The YearMonth to compare actual with should not be null");
  }

  /**
   * Check that the {@code YearMonth} string representation to compare actual {@code YearMonth} to is not null,
   * otherwise throws a {@link IllegalArgumentException} with an explicit message.
   *
   * @param otherYearMonthAsString string representing the {@code YearMonth} to compare actual with.
   * @throws IllegalArgumentException with an explicit message if the given {@code String} is null.
   */
  private static void assertYearMonthAsStringParameterIsNotNull(String otherYearMonthAsString) {
    checkArgument(otherYearMonthAsString != null,
                  "The String representing the YearMonth to compare actual with should not be null");
  }

  private static void checkIsNotNullAndNotEmpty(Object[] values) {
    checkArgument(values != null, "The given YearMonth array should not be null");
    checkArgument(values.length > 0, "The given YearMonth array should not be empty");
  }

  private static Object[] convertToYearMonthArray(String... yearMonthsAsString) {
    return Arrays.stream(yearMonthsAsString).map(YearMonth::parse).toArray();
  }

}
