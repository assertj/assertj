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

import static org.assertj.core.error.ShouldBeAfter.shouldBeAfter;
import static org.assertj.core.error.ShouldBeAfterOrEqualsTo.shouldBeAfterOrEqualsTo;
import static org.assertj.core.error.ShouldBeBefore.shouldBeBefore;
import static org.assertj.core.error.ShouldBeBeforeOrEqualsTo.shouldBeBeforeOrEqualsTo;
import static org.assertj.core.error.ShouldBeToday.shouldBeToday;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;

/**
 * Assertions for {@link LocalDate} type from new Date &amp; Time API introduced in Java 8.
 *
 * @param <SELF> the "self" type of this assertion class.
 */
public abstract class AbstractLocalDateAssert<SELF extends AbstractLocalDateAssert<SELF>>
    extends AbstractTemporalAssert<SELF, LocalDate> {

  public static final String NULL_LOCAL_DATE_TIME_PARAMETER_MESSAGE = "The LocalDate to compare actual with should not be null";

  /**
   * Creates a new <code>{@link org.assertj.core.api.AbstractLocalDateAssert}</code>.
   * 
   * @param selfType the "self type"
   * @param actual the actual value to verify
   */
  protected AbstractLocalDateAssert(LocalDate actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code LocalDate} is <b>strictly</b> before the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01")).isBefore(parse("2000-01-02"));</code></pre>
   * 
   * @param other the given {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDate} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDate} is not strictly before the given one.
   */
  public SELF isBefore(LocalDate other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateParameterIsNotNull(other);
    if (!actual.isBefore(other)) throw Failures.instance().failure(info, shouldBeBefore(actual, other));
    return myself;
  }

  /**
   * Same assertion as {@link #isBefore(LocalDate)} but the {@link LocalDate} is built from given String, which
   * must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE"
   * >ISO LocalDate format</a> to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2000-01-01")).isBefore("2000-01-02");</code></pre>
   * 
   * @param localDateAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not strictly before the {@link LocalDate} built
   *           from given String.
   */
  public SELF isBefore(String localDateAsString) {
    assertLocalDateAsStringParameterIsNotNull(localDateAsString);
    return isBefore(parse(localDateAsString));
  }

  /**
   * Verifies that the actual {@code LocalDate} is before or equals to the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01")).isBeforeOrEqualTo(parse("2000-01-01"))
   *                                .isBeforeOrEqualTo(parse("2000-01-02"));</code></pre>
   * 
   * @param other the given {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDate} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDate} is not before or equals to the given one.
   */
  public SELF isBeforeOrEqualTo(LocalDate other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateParameterIsNotNull(other);
    if (actual.isAfter(other)) {
      throw Failures.instance().failure(info, shouldBeBeforeOrEqualsTo(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isBeforeOrEqualTo(LocalDate)} but the {@link LocalDate} is built from given
   * String, which must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE"
   * >ISO LocalDate format</a> to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid conversion
   * assertThat(parse("2000-01-01")).isBeforeOrEqualTo("2000-01-01")
   *                                .isBeforeOrEqualTo("2000-01-02");</code></pre>
   * 
   * @param localDateAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not before or equals to the {@link LocalDate} built from
   *           given String.
   */
  public SELF isBeforeOrEqualTo(String localDateAsString) {
    assertLocalDateAsStringParameterIsNotNull(localDateAsString);
    return isBeforeOrEqualTo(parse(localDateAsString));
  }

  /**
   * Verifies that the actual {@code LocalDate} is after or equals to the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01")).isAfterOrEqualTo(parse("2000-01-01"))
   *                                .isAfterOrEqualTo(parse("1999-12-31"));</code></pre>
   * 
   * @param other the given {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDate} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDate} is not after or equals to the given one.
   */
  public SELF isAfterOrEqualTo(LocalDate other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateParameterIsNotNull(other);
    if (actual.isBefore(other)) {
      throw Failures.instance().failure(info, shouldBeAfterOrEqualsTo(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isAfterOrEqualTo(LocalDate)} but the {@link LocalDate} is built from given
   * String, which must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE"
   * >ISO LocalDate format</a> to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid conversion
   * assertThat(parse("2000-01-01")).isAfterOrEqualTo("2000-01-01")
   *                                .isAfterOrEqualTo("1999-12-31");</code></pre>
   * 
   * @param localDateAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not after or equals to the {@link LocalDate} built from
   *           given String.
   */
  public SELF isAfterOrEqualTo(String localDateAsString) {
    assertLocalDateAsStringParameterIsNotNull(localDateAsString);
    return isAfterOrEqualTo(parse(localDateAsString));
  }

  /**
   * Verifies that the actual {@code LocalDate} is <b>strictly</b> after the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01")).isAfter(parse("1999-12-31"));</code></pre>
   * 
   * @param other the given {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDate} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDate} is not strictly after the given one.
   */
  public SELF isAfter(LocalDate other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateParameterIsNotNull(other);
    if (!actual.isAfter(other)) {
      throw Failures.instance().failure(info, shouldBeAfter(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isAfter(LocalDate)} but the {@link LocalDate} is built from given a String that
   * must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE"
   * >ISO LocalDate format</a> to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid conversion
   * assertThat(parse("2000-01-01")).isAfter("1999-12-31");</code></pre>
   * 
   * @param localDateAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not strictly after the {@link LocalDate} built
   *           from given String.
   */
  public SELF isAfter(String localDateAsString) {
    assertLocalDateAsStringParameterIsNotNull(localDateAsString);
    return isAfter(parse(localDateAsString));
  }

  /**
   * Same assertion as {@link #isEqualTo(Object)} (where Object is expected to be {@link LocalDate}) but here you
   * pass {@link LocalDate} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE"
   * >ISO LocalDate format</a> to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2000-01-01")).isEqualTo("2000-01-01");</code></pre>
   * 
   * @param localDateAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not equal to the {@link LocalDate} built from
   *           given String.
   */
  public SELF isEqualTo(String localDateAsString) {
    assertLocalDateAsStringParameterIsNotNull(localDateAsString);
    return isEqualTo(parse(localDateAsString));
  }

  /**
   * Same assertion as {@link #isNotEqualTo(Object)} (where Object is expected to be {@link LocalDate}) but here you
   * pass {@link LocalDate} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE"
   * >ISO LocalDate format</a> to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2000-01-01")).isNotEqualTo("2000-01-15");</code></pre>
   * 
   * @param localDateAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is equal to the {@link LocalDate} built from given
   *           String.
   */
  public SELF isNotEqualTo(String localDateAsString) {
    assertLocalDateAsStringParameterIsNotNull(localDateAsString);
    return isNotEqualTo(parse(localDateAsString));
  }

  /**
   * Same assertion as {@link #isIn(Object...)} (where Objects are expected to be {@link LocalDate}) but here you
   * pass {@link LocalDate} String representations that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE"
   * >ISO LocalDate format</a> to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String based representation of LocalDate
   * assertThat(parse("2000-01-01")).isIn("1999-12-31", "2000-01-01");</code></pre>
   * 
   * @param localDatesAsString String array representing {@link LocalDate}s.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not in the {@link LocalDate}s built from given
   *           Strings.
   */
  public SELF isIn(String... localDatesAsString) {
    checkIsNotNullAndNotEmpty(localDatesAsString);
    return isIn(convertToLocalDateArray(localDatesAsString));
  }

  /**
   * Same assertion as {@link #isNotIn(Object...)} (where Objects are expected to be {@link LocalDate}) but here you
   * pass {@link LocalDate} String representations that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE"
   * >ISO LocalDate format</a> to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String based representation of LocalDate
   * assertThat(parse("2000-01-01")).isNotIn("1999-12-31", "2000-01-02");</code></pre>
   * 
   * @param localDatesAsString Array of String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is in the {@link LocalDate}s built from given
   *           Strings.
   */
  public SELF isNotIn(String... localDatesAsString) {
    checkIsNotNullAndNotEmpty(localDatesAsString);
    return isNotIn(convertToLocalDateArray(localDatesAsString));
  }

  /**
   * Verifies that the actual {@code LocalDate} is today, that is matching current year, month and day.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(LocalDate.now()).isToday();
   *
   * // assertion will fail
   * assertThat(theFellowshipOfTheRing.getReleaseDate()).isToday();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDate} is not today.
   */
  public SELF isToday() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isEqual(LocalDate.now())) throw Failures.instance().failure(info, shouldBeToday(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link LocalDate} is in the [start, end] period (start and end included).
   * <p>
   * Example:
   * <pre><code class='java'> LocalDate localDate = LocalDate.now();
   * 
   * // assertions succeed:
   * assertThat(localDate).isBetween(localDate.minusDays(1), localDate.plusDays(1))
   *                      .isBetween(localDate, localDate.plusDays(1))
   *                      .isBetween(localDate.minusDays(1), localDate)
   *                      .isBetween(localDate, localDate);
   * 
   * // assertions fail:
   * assertThat(localDate).isBetween(localDate.minusDays(10), localDate.minusDays(1));
   * assertThat(localDate).isBetween(localDate.plusDays(1), localDate.plusDays(10));</code></pre>
   * 
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in [start, end] period.
   * 
   * @since 3.7.1
   */
  public SELF isBetween(LocalDate startInclusive, LocalDate endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return myself;
  }

  /**
   * Same assertion as {@link #isBetween(LocalDate, LocalDate)} but here you pass {@link LocalDate} String representations 
   * which must follow <a href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE">ISO LocalDate format</a> 
   * to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> LocalDate firstOfJanuary2000 = LocalDate.parse("2000-01-01");
   * 
   * // assertions succeed:
   * assertThat(firstOfJanuary2000).isBetween("1999-01-01", "2001-01-01")
   *                               .isBetween("2000-01-01", "2001-01-01")
   *                               .isBetween("1999-01-01", "2000-01-01")
   *                               .isBetween("2000-01-01", "2000-01-01");
   * 
   * // assertion fails:
   * assertThat(firstOfJanuary2000).isBetween("1999-01-01", "1999-12-31");</code></pre>
   * 
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * 
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws DateTimeParseException if any of the given String can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual value is not in [start, end] period.
   * 
   * @since 3.7.1
   */
  public SELF isBetween(String startInclusive, String endInclusive) {
    return isBetween(parse(startInclusive), parse(endInclusive));
  }

  /**
   * Verifies that the actual {@link LocalDate} is in the ]start, end[ period (start and end excluded).
   * <p>
   * Example:
   * <pre><code class='java'> LocalDate localDate = LocalDate.now();
   * 
   * // assertion succeeds:
   * assertThat(localDate).isStrictlyBetween(localDate.minusDays(1), localDate.plusDays(1));
   * 
   * // assertions fail:
   * assertThat(localDate).isStrictlyBetween(localDate.minusDays(10), localDate.minusDays(1));
   * assertThat(localDate).isStrictlyBetween(localDate.plusDays(1), localDate.plusDays(10));
   * assertThat(localDate).isStrictlyBetween(localDate, localDate.plusDays(1));
   * assertThat(localDate).isStrictlyBetween(localDate.minusDays(1), localDate);</code></pre>
   * 
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ period.
   * 
   * @since 3.7.1
   */
  public SELF isStrictlyBetween(LocalDate startInclusive, LocalDate endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, false, false);
    return myself;
  }

  /**
   * Same assertion as {@link #isStrictlyBetween(LocalDate, LocalDate)} but here you pass {@link LocalDate} String representations 
   * which must follow <a href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE">ISO LocalDate format</a> 
   * to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> LocalDate firstOfJanuary2000 = LocalDate.parse("2000-01-01");
   * 
   * // assertion succeeds:
   * assertThat(firstOfJanuary2000).isStrictlyBetween("1999-01-01", "2001-01-01");
   * 
   * // assertions fail:
   * assertThat(firstOfJanuary2000).isStrictlyBetween("1999-01-01", "1999-12-31");
   * assertThat(firstOfJanuary2000).isStrictlyBetween("2000-01-01", "2001-01-01");
   * assertThat(firstOfJanuary2000).isStrictlyBetween("1999-01-01", "2000-01-01");</code></pre>
   * 
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * 
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws DateTimeParseException if any of the given String can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual value is not in ]start, end[ period.
   * 
   * @since 3.7.1
   */
  public SELF isStrictlyBetween(String startInclusive, String endInclusive) {
    return isStrictlyBetween(parse(startInclusive), parse(endInclusive));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected LocalDate parse(String localDateAsString) {
    return LocalDate.parse(localDateAsString);
  }

  private static Object[] convertToLocalDateArray(String... localDatesAsString) {
    return Arrays.stream(localDatesAsString).map(LocalDate::parse).toArray();
  }

  private void checkIsNotNullAndNotEmpty(Object[] values) {
    checkArgument(values != null, "The given LocalDate array should not be null");
    checkArgument(values.length > 0, "The given LocalDate array should not be empty");
  }

  /**
   * Check that the {@link LocalDate} string representation to compare actual {@link LocalDate} to is not null,
   * otherwise throws a {@link IllegalArgumentException} with an explicit message
   * 
   * @param localDateAsString String representing the {@link LocalDate} to compare actual with
   * @throws IllegalArgumentException with an explicit message if the given {@link String} is null
   */
  private static void assertLocalDateAsStringParameterIsNotNull(String localDateAsString) {
    checkArgument(localDateAsString != null,
                  "The String representing the LocalDate to compare actual with should not be null");
  }

  /**
   * Check that the {@link LocalDate} to compare actual {@link LocalDate} to is not null, in that case throws a
   * {@link IllegalArgumentException} with an explicit message
   * 
   * @param other the {@link LocalDate} to check
   * @throws IllegalArgumentException with an explicit message if the given {@link LocalDate} is null
   */
  private static void assertLocalDateParameterIsNotNull(LocalDate other) {
    checkArgument(other != null, NULL_LOCAL_DATE_TIME_PARAMETER_MESSAGE);
  }

}
