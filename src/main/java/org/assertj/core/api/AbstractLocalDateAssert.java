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

import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;

/**
 * Assertions for {@link LocalDate} type from new Date &amp; Time API introduced in Java 8.
 */
public abstract class AbstractLocalDateAssert<S extends AbstractLocalDateAssert<S>>
    extends AbstractTemporalAssert<S, LocalDate> {

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
  public S isBefore(LocalDate other) {
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
   * <pre><code class='java'> // use directly String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2000-01-01")).isBefore("2000-01-02");</code></pre>
   * 
   * @param localDateTimeAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not strictly before the {@link LocalDate} built
   *           from given String.
   */
  public S isBefore(String localDateTimeAsString) {
    assertLocalDateAsStringParameterIsNotNull(localDateTimeAsString);
    return isBefore(parse(localDateTimeAsString));
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
  public S isBeforeOrEqualTo(LocalDate other) {
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
   * @param localDateTimeAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not before or equals to the {@link LocalDate} built from
   *           given String.
   */
  public S isBeforeOrEqualTo(String localDateTimeAsString) {
    assertLocalDateAsStringParameterIsNotNull(localDateTimeAsString);
    return isBeforeOrEqualTo(parse(localDateTimeAsString));
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
  public S isAfterOrEqualTo(LocalDate other) {
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
   * @param localDateTimeAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not after or equals to the {@link LocalDate} built from
   *           given String.
   */
  public S isAfterOrEqualTo(String localDateTimeAsString) {
    assertLocalDateAsStringParameterIsNotNull(localDateTimeAsString);
    return isAfterOrEqualTo(parse(localDateTimeAsString));
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
  public S isAfter(LocalDate other) {
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
   * @param localDateTimeAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not strictly after the {@link LocalDate} built
   *           from given String.
   */
  public S isAfter(String localDateTimeAsString) {
    assertLocalDateAsStringParameterIsNotNull(localDateTimeAsString);
    return isAfter(parse(localDateTimeAsString));
  }

  /**
   * Same assertion as {@link #isEqualTo(Object)} (where Object is expected to be {@link LocalDate}) but here you
   * pass {@link LocalDate} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE"
   * >ISO LocalDate format</a> to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use directly String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2000-01-01")).isEqualTo("2000-01-01");</code></pre>
   * 
   * @param dateTimeAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not equal to the {@link LocalDate} built from
   *           given String.
   */
  public S isEqualTo(String dateTimeAsString) {
    assertLocalDateAsStringParameterIsNotNull(dateTimeAsString);
    return isEqualTo(parse(dateTimeAsString));
  }

  /**
   * Same assertion as {@link #isNotEqualTo(Object)} (where Object is expected to be {@link LocalDate}) but here you
   * pass {@link LocalDate} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE"
   * >ISO LocalDate format</a> to allow calling {@link LocalDate#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use directly String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2000-01-01")).isNotEqualTo("2000-01-15");</code></pre>
   * 
   * @param dateTimeAsString String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is equal to the {@link LocalDate} built from given
   *           String.
   */
  public S isNotEqualTo(String dateTimeAsString) {
    assertLocalDateAsStringParameterIsNotNull(dateTimeAsString);
    return isNotEqualTo(parse(dateTimeAsString));
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
   * @param dateTimesAsString String array representing {@link LocalDate}s.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is not in the {@link LocalDate}s built from given
   *           Strings.
   */
  public S isIn(String... dateTimesAsString) {
    checkIsNotNullAndNotEmpty(dateTimesAsString);
    return isIn(convertToLocalDateArray(dateTimesAsString));
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
   * @param dateTimesAsString Array of String representing a {@link LocalDate}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDate} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDate}.
   * @throws AssertionError if the actual {@code LocalDate} is in the {@link LocalDate}s built from given
   *           Strings.
   */
  public S isNotIn(String... dateTimesAsString) {
    checkIsNotNullAndNotEmpty(dateTimesAsString);
    return isNotIn(convertToLocalDateArray(dateTimesAsString));
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
  public S isToday() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isEqual(LocalDate.now())) throw Failures.instance().failure(info, shouldBeToday(actual));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected LocalDate parse(String localDateTimeAsString) {
    return LocalDate.parse(localDateTimeAsString);
  }

  private static Object[] convertToLocalDateArray(String... dateTimesAsString) {
    LocalDate[] dates = new LocalDate[dateTimesAsString.length];
    for (int i = 0; i < dateTimesAsString.length; i++) {
      dates[i] = LocalDate.parse(dateTimesAsString[i]);
    }
    return dates;
  }

  private void checkIsNotNullAndNotEmpty(Object[] values) {
    checkArgument(values != null, "The given LocalDate array should not be null");
    checkArgument(values.length > 0, "The given LocalDate array should not be empty");
  }

  /**
   * Check that the {@link LocalDate} string representation to compare actual {@link LocalDate} to is not null,
   * otherwise throws a {@link IllegalArgumentException} with an explicit message
   * 
   * @param localDateTimeAsString String representing the {@link LocalDate} to compare actual with
   * @throws IllegalArgumentException with an explicit message if the given {@link String} is null
   */
  private static void assertLocalDateAsStringParameterIsNotNull(String localDateTimeAsString) {
    checkArgument(localDateTimeAsString != null,
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
