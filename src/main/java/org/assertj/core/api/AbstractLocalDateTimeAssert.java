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
import static org.assertj.core.error.ShouldBeEqualIgnoringHours.shouldBeEqualIgnoringHours;
import static org.assertj.core.error.ShouldBeEqualIgnoringMinutes.shouldBeEqualIgnoringMinutes;
import static org.assertj.core.error.ShouldBeEqualIgnoringNanos.shouldBeEqualIgnoringNanos;
import static org.assertj.core.error.ShouldBeEqualIgnoringSeconds.shouldBeEqualIgnoringSeconds;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;

/**
 * Assertions for {@link LocalDateTime} type from new Date &amp; Time API introduced in Java 8.
 *
 * @param <SELF> the "self" type of this assertion class.
 *
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public abstract class AbstractLocalDateTimeAssert<SELF extends AbstractLocalDateTimeAssert<SELF>> extends
    AbstractTemporalAssert<SELF, LocalDateTime> {

  public static final String NULL_LOCAL_DATE_TIME_PARAMETER_MESSAGE = "The LocalDateTime to compare actual with should not be null";

  /**
   * Creates a new <code>{@link org.assertj.core.api.AbstractLocalDateTimeAssert}</code>.
   *
   * @param selfType the "self type"
   * @param actual the actual value to verify
   */
  protected AbstractLocalDateTimeAssert(LocalDateTime actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual {@code LocalDateTime} is <b>strictly</b> before the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01T23:59:59")).isBefore(parse("2000-01-02T00:00:00"));</code></pre>
   *
   * @param other the given {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not strictly before the given one.
   */
  public SELF isBefore(LocalDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateTimeParameterIsNotNull(other);
    if (!actual.isBefore(other)) {
      throw Failures.instance().failure(info, shouldBeBefore(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isBefore(LocalDateTime)} but the {@link LocalDateTime} is built from given String, which
   * must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME"
   * >ISO LocalDateTime format</a> to allow calling {@link LocalDateTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2000-01-01T23:59:59")).isBefore("2000-01-02T00:00:00");</code></pre>
   *
   * @param localDateTimeAsString String representing a {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDateTime}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not strictly before the {@link LocalDateTime} built
   *           from given String.
   */
  public SELF isBefore(String localDateTimeAsString) {
    assertLocalDateTimeAsStringParameterIsNotNull(localDateTimeAsString);
    return isBefore(parse(localDateTimeAsString));
  }

  /**
   * Verifies that the actual {@code LocalDateTime} is before or equals to the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01T23:59:59")).isBeforeOrEqualTo(parse("2000-01-01T23:59:59"))
   *                                         .isBeforeOrEqualTo(parse("2000-01-02T00:00:00"));</code></pre>
   *
   * @param other the given {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not before or equals to the given one.
   */
  public SELF isBeforeOrEqualTo(LocalDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateTimeParameterIsNotNull(other);
    if (actual.isAfter(other)) {
      throw Failures.instance().failure(info, shouldBeBeforeOrEqualsTo(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isBeforeOrEqualTo(LocalDateTime)} but the {@link LocalDateTime} is built from given
   * String, which must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME"
   * >ISO LocalDateTime format</a> to allow calling {@link LocalDateTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid conversion
   * assertThat(parse("2000-01-01T23:59:59")).isBeforeOrEqualTo("2000-01-01T23:59:59")
   *                                         .isBeforeOrEqualTo("2000-01-02T00:00:00");</code></pre>
   *
   * @param localDateTimeAsString String representing a {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDateTime}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not before or equals to the {@link LocalDateTime}
   *           built from given String.
   */
  public SELF isBeforeOrEqualTo(String localDateTimeAsString) {
    assertLocalDateTimeAsStringParameterIsNotNull(localDateTimeAsString);
    return isBeforeOrEqualTo(parse(localDateTimeAsString));
  }

  /**
   * Verifies that the actual {@code LocalDateTime} is after or equals to the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01T00:00:00")).isAfterOrEqualTo(parse("2000-01-01T00:00:00"))
   *                                         .isAfterOrEqualTo(parse("1999-12-31T23:59:59"));</code></pre>
   *
   * @param other the given {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not after or equals to the given one.
   */
  public SELF isAfterOrEqualTo(LocalDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateTimeParameterIsNotNull(other);
    if (actual.isBefore(other)) {
      throw Failures.instance().failure(info, shouldBeAfterOrEqualsTo(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isAfterOrEqualTo(LocalDateTime)} but the {@link LocalDateTime} is built from given
   * String, which must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME"
   * >ISO LocalDateTime format</a> to allow calling {@link LocalDateTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid conversion
   * assertThat(parse("2000-01-01T00:00:00")).isAfterOrEqualTo("2000-01-01T00:00:00")
   *                                         .isAfterOrEqualTo("1999-12-31T23:59:59");</code></pre>
   *
   * @param localDateTimeAsString String representing a {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDateTime}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not after or equals to the {@link LocalDateTime}
   *           built from given String.
   */
  public SELF isAfterOrEqualTo(String localDateTimeAsString) {
    assertLocalDateTimeAsStringParameterIsNotNull(localDateTimeAsString);
    return isAfterOrEqualTo(parse(localDateTimeAsString));
  }

  /**
   * Verifies that the actual {@code LocalDateTime} is <b>strictly</b> after the given one.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01T00:00:00")).isAfter(parse("1999-12-31T23:59:59"));</code></pre>
   *
   * @param other the given {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not strictly after the given one.
   */
  public SELF isAfter(LocalDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateTimeParameterIsNotNull(other);
    if (!actual.isAfter(other)) {
      throw Failures.instance().failure(info, shouldBeAfter(actual, other));
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isAfter(LocalDateTime)} but the {@link LocalDateTime} is built from given a String that
   * must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME"
   * >ISO LocalDateTime format</a> to allow calling {@link LocalDateTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid conversion
   * assertThat(parse("2000-01-01T00:00:00")).isAfter("1999-12-31T23:59:59");</code></pre>
   *
   * @param localDateTimeAsString String representing a {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDateTime}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not strictly after the {@link LocalDateTime} built
   *           from given String.
   */
  public SELF isAfter(String localDateTimeAsString) {
    assertLocalDateTimeAsStringParameterIsNotNull(localDateTimeAsString);
    return isAfter(parse(localDateTimeAsString));
  }

  /**
   * Same assertion as {@link #isEqualTo(Object)} (where Object is expected to be {@link LocalDateTime}) but here you
   * pass {@link LocalDateTime} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME"
   * >ISO LocalDateTime format</a> to allow calling {@link LocalDateTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2000-01-01T00:00:00")).isEqualTo("2000-01-01T00:00:00");</code></pre>
   *
   * @param dateTimeAsString String representing a {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDateTime}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not equal to the {@link LocalDateTime} built from
   *           given String.
   */
  public SELF isEqualTo(String dateTimeAsString) {
    assertLocalDateTimeAsStringParameterIsNotNull(dateTimeAsString);
    return isEqualTo(parse(dateTimeAsString));
  }

  /**
   * Same assertion as {@link #isNotEqualTo(Object)} (where Object is expected to be {@link LocalDateTime}) but here you
   * pass {@link LocalDateTime} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME"
   * >ISO LocalDateTime format</a> to allow calling {@link LocalDateTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String in comparison to avoid writing the code to perform the conversion
   * assertThat(parse("2000-01-01T00:00:00")).isNotEqualTo("2000-01-15T00:00:00");</code></pre>
   *
   * @param dateTimeAsString String representing a {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDateTime}.
   * @throws AssertionError if the actual {@code LocalDateTime} is equal to the {@link LocalDateTime} built from given
   *           String.
   */
  public SELF isNotEqualTo(String dateTimeAsString) {
    assertLocalDateTimeAsStringParameterIsNotNull(dateTimeAsString);
    return isNotEqualTo(parse(dateTimeAsString));
  }

  /**
   * Same assertion as {@link #isIn(Object...)} (where Objects are expected to be {@link LocalDateTime}) but here you
   * pass {@link LocalDateTime} String representations that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME"
   * >ISO LocalDateTime format</a> to allow calling {@link LocalDateTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String based representation of LocalDateTime
   * assertThat(parse("2000-01-01T00:00:00")).isIn("1999-12-31T00:00:00", "2000-01-01T00:00:00");</code></pre>
   *
   * @param dateTimesAsString String array representing {@link LocalDateTime}s.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDateTime}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not in the {@link LocalDateTime}s built from given
   *           Strings.
   */
  public SELF isIn(String... dateTimesAsString) {
    checkIsNotNullAndNotEmpty(dateTimesAsString);
    return isIn(convertToLocalDateTimeArray(dateTimesAsString));
  }

  /**
   * Same assertion as {@link #isNotIn(Object...)} (where Objects are expected to be {@link LocalDateTime}) but here you
   * pass {@link LocalDateTime} String representations that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME"
   * >ISO LocalDateTime format</a> to allow calling {@link LocalDateTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String based representation of LocalDateTime
   * assertThat(parse("2000-01-01T00:00:00")).isNotIn("1999-12-31T00:00:00", "2000-01-02T00:00:00");</code></pre>
   *
   * @param dateTimesAsString Array of String representing a {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link LocalDateTime}.
   * @throws AssertionError if the actual {@code LocalDateTime} is in the {@link LocalDateTime}s built from given
   *           Strings.
   */
  public SELF isNotIn(String... dateTimesAsString) {
    checkIsNotNullAndNotEmpty(dateTimesAsString);
    return isNotIn(convertToLocalDateTimeArray(dateTimesAsString));
  }

  private static Object[] convertToLocalDateTimeArray(String... dateTimesAsString) {
    return Arrays.stream(dateTimesAsString).map(LocalDateTime::parse).toArray();
  }

  private void checkIsNotNullAndNotEmpty(Object[] values) {
    checkArgument(values != null, "The given LocalDateTime array should not be null");
    checkArgument(values.length > 0, "The given LocalDateTime array should not be empty");
  }

  /**
   * Check that the {@link LocalDateTime} string representation to compare actual {@link LocalDateTime} to is not null,
   * otherwise throws a {@link IllegalArgumentException} with an explicit message
   *
   * @param localDateTimeAsString String representing the {@link LocalDateTime} to compare actual with
   * @throws IllegalArgumentException with an explicit message if the given {@link String} is null
   */
  private static void assertLocalDateTimeAsStringParameterIsNotNull(String localDateTimeAsString) {
    checkArgument(localDateTimeAsString != null,
                  "The String representing the LocalDateTime to compare actual with should not be null");
  }

  /**
   * Check that the {@link LocalDateTime} to compare actual {@link LocalDateTime} to is not null, in that case throws a
   * {@link IllegalArgumentException} with an explicit message
   *
   * @param other the {@link LocalDateTime} to check
   * @throws IllegalArgumentException with an explicit message if the given {@link LocalDateTime} is null
   */
  private static void assertLocalDateTimeParameterIsNotNull(LocalDateTime other) {
    checkArgument(other != null, "The LocalDateTime to compare actual with should not be null");
  }

  /**
   * Verifies that actual and given {@code LocalDateTime} have same year, month, day, hour, minute and second fields,
   * (nanosecond fields are ignored in comparison).
   * <p>
   * Assertion can fail with localDateTimes in same chronological nanosecond time window, e.g :
   * <p>
   * 2000-01-01T00:00:<b>01.000000000</b> and 2000-01-01T00:00:<b>00.999999999</b>.
   * <p>
   * Assertion fails as second fields differ even if time difference is only 1ns.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * LocalDateTime localDateTime1 = LocalDateTime.of(2000, 1, 1, 0, 0, 1, 0);
   * LocalDateTime localDateTime2 = LocalDateTime.of(2000, 1, 1, 0, 0, 1, 456);
   * assertThat(localDateTime1).isEqualToIgnoringNanos(localDateTime2);
   *
   * // failing assertions (even if time difference is only 1ms)
   * LocalDateTime localDateTimeA = LocalDateTime.of(2000, 1, 1, 0, 0, 1, 0);
   * LocalDateTime localDateTimeB = LocalDateTime.of(2000, 1, 1, 0, 0, 0, 999999999);
   * assertThat(localDateTimeA).isEqualToIgnoringNanos(localDateTimeB);</code></pre>
   *
   * @param other the given {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is are not equal with nanoseconds ignored.
   */
  public SELF isEqualToIgnoringNanos(LocalDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateTimeParameterIsNotNull(other);
    if (!areEqualIgnoringNanos(actual, other)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringNanos(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that actual and given {@link LocalDateTime} have same year, month, day, hour and minute fields (second and
   * nanosecond fields are ignored in comparison).
   * <p>
   * Assertion can fail with LocalDateTimes in same chronological second time window, e.g :
   * <p>
   * 2000-01-01T00:<b>01:00</b>.000 and 2000-01-01T00:<b>00:59</b>.000.
   * <p>
   * Assertion fails as minute fields differ even if time difference is only 1s.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * LocalDateTime localDateTime1 = LocalDateTime.of(2000, 1, 1, 23, 50, 0, 0);
   * LocalDateTime localDateTime2 = LocalDateTime.of(2000, 1, 1, 23, 50, 10, 456);
   * assertThat(localDateTime1).isEqualToIgnoringSeconds(localDateTime2);
   *
   * // failing assertions (even if time difference is only 1ms)
   * LocalDateTime localDateTimeA = LocalDateTime.of(2000, 1, 1, 23, 50, 00, 000);
   * LocalDateTime localDateTimeB = LocalDateTime.of(2000, 1, 1, 23, 49, 59, 999);
   * assertThat(localDateTimeA).isEqualToIgnoringSeconds(localDateTimeB);</code></pre>
   *
   * @param other the given {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is are not equal with second and nanosecond fields
   *           ignored.
   */
  public SELF isEqualToIgnoringSeconds(LocalDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateTimeParameterIsNotNull(other);
    if (!areEqualIgnoringSeconds(actual, other)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringSeconds(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that actual and given {@code LocalDateTime} have same year, month, day and hour fields (minute, second and
   * nanosecond fields are ignored in comparison).
   * <p>
   * Assertion can fail with localDateTimes in same chronological second time window, e.g :
   * <p>
   * 2000-01-01T<b>01:00</b>:00.000 and 2000-01-01T<b>00:59:59</b>.000.
   * <p>
   * Time difference is only 1s but hour fields differ.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * LocalDateTime localDateTime1 = LocalDateTime.of(2000, 1, 1, 23, 50, 0, 0);
   * LocalDateTime localDateTime2 = LocalDateTime.of(2000, 1, 1, 23, 00, 2, 7);
   * assertThat(localDateTime1).isEqualToIgnoringMinutes(localDateTime2);
   *
   * // failing assertions (even if time difference is only 1ms)
   * LocalDateTime localDateTimeA = LocalDateTime.of(2000, 1, 1, 01, 00, 00, 000);
   * LocalDateTime localDateTimeB = LocalDateTime.of(2000, 1, 1, 00, 59, 59, 999);
   * assertThat(localDateTimeA).isEqualToIgnoringMinutes(localDateTimeB);</code></pre>
   *
   * @param other the given {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is are not equal ignoring minute, second and nanosecond
   *           fields.
   */
  public SELF isEqualToIgnoringMinutes(LocalDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateTimeParameterIsNotNull(other);
    if (!areEqualIgnoringMinutes(actual, other)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringMinutes(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that actual and given {@code LocalDateTime} have same year, month and day fields (hour, minute, second and
   * nanosecond fields are ignored in comparison).
   * <p>
   * Assertion can fail with localDateTimes in same chronological minute time window, e.g :
   * <p>
   * 2000-01-<b>01T23:59</b>:00.000 and 2000-01-02T<b>00:00</b>:00.000.
   * <p>
   * Time difference is only 1min but day fields differ.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * LocalDateTime localDateTime1 = LocalDateTime.of(2000, 1, 1, 23, 59, 59, 999);
   * LocalDateTime localDateTime2 = LocalDateTime.of(2000, 1, 1, 00, 00, 00, 000);
   * assertThat(localDateTime1).isEqualToIgnoringHours(localDateTime2);
   *
   * // failing assertions (even if time difference is only 1ms)
   * LocalDateTime localDateTimeA = LocalDateTime.of(2000, 1, 2, 00, 00, 00, 000);
   * LocalDateTime localDateTimeB = LocalDateTime.of(2000, 1, 1, 23, 59, 59, 999);
   * assertThat(localDateTimeA).isEqualToIgnoringHours(localDateTimeB);</code></pre>
   *
   * @param other the given {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is are not equal with second and nanosecond fields
   *           ignored.
   */
  public SELF isEqualToIgnoringHours(LocalDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateTimeParameterIsNotNull(other);
    if (!haveSameYearMonthAndDayOfMonth(actual, other)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringHours(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link LocalDateTime} is in the [start, end] period (start and end included).
   * <p>
   * Example:
   * <pre><code class='java'> LocalDateTime localDateTime = LocalDateTime.now();
   * 
   * // assertions succeed:
   * assertThat(localDateTime).isBetween(localDateTime.minusSeconds(1), localDateTime.plusSeconds(1))
   *                          .isBetween(localDateTime, localDateTime.plusSeconds(1))
   *                          .isBetween(localDateTime.minusSeconds(1), localDateTime)
   *                          .isBetween(localDateTime, localDateTime);
   * 
   * // assertions fail:
   * assertThat(localDateTime).isBetween(localDateTime.minusSeconds(10), localDateTime.minusSeconds(1));
   * assertThat(localDateTime).isBetween(localDateTime.plusSeconds(1), localDateTime.plusSeconds(10));</code></pre>
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
  public SELF isBetween(LocalDateTime startInclusive, LocalDateTime endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return myself;
  }

  /**
   * Same assertion as {@link #isBetween(LocalDateTime, LocalDateTime)} but here you pass {@link LocalDateTime} String representations 
   * which must follow <a href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME">ISO LocalDateTime format</a> 
   * to allow calling {@link LocalDateTime#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> LocalDateTime firstOfJanuary2000 = LocalDateTime.parse("2000-01-01T00:00:00");
   * 
   * // assertions succeed:
   * assertThat(firstOfJanuary2000).isBetween("1999-12-31T23:59:59", "2000-01-01T00:00:01")         
   *                               .isBetween("2000-01-01T00:00:00", "2000-01-01T00:00:01")         
   *                               .isBetween("1999-12-31T23:59:59", "2000-01-01T00:00:00")         
   *                               .isBetween("2000-01-01T00:00:00", "2000-01-01T00:00:00");
   *                               
   * // assertion fails:
   * assertThat(firstOfJanuary2000).isBetween("1999-01-01T00:00:01", "1999-12-31T23:59:59");</code></pre>
   * 
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * 
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws DateTimeParseException if any of the given String can't be converted to a {@link LocalDateTime}.
   * @throws AssertionError if the actual value is not in [start, end] period.
   * 
   * @since 3.7.1
   */
  public SELF isBetween(String startInclusive, String endInclusive) {
    return isBetween(parse(startInclusive), parse(endInclusive));
  }

  /**
   * Verifies that the actual {@link LocalDateTime} is in the ]start, end[ period (start and end excluded).
   * <p>
   * Example:
   * <pre><code class='java'> LocalDateTime localDateTime = LocalDateTime.now();
   * 
   * // assertion succeeds:
   * assertThat(localDateTime).isStrictlyBetween(localDateTime.minusSeconds(1), localDateTime.plusSeconds(1));
   * 
   * // assertions fail:
   * assertThat(localDateTime).isStrictlyBetween(localDateTime.minusSeconds(10), localDateTime.minusSeconds(1));
   * assertThat(localDateTime).isStrictlyBetween(localDateTime.plusSeconds(1), localDateTime.plusSeconds(10));
   * assertThat(localDateTime).isStrictlyBetween(localDateTime, localDateTime.plusSeconds(1));
   * assertThat(localDateTime).isStrictlyBetween(localDateTime.minusSeconds(1), localDateTime);</code></pre>
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
  public SELF isStrictlyBetween(LocalDateTime startInclusive, LocalDateTime endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, false, false);
    return myself;
  }

  /**
   * Same assertion as {@link #isStrictlyBetween(LocalDateTime, LocalDateTime)} but here you pass {@link LocalDateTime} String representations 
   * which must follow <a href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_LOCAL_DATE_TIME">ISO LocalDateTime format</a> 
   * to allow calling {@link LocalDateTime#parse(CharSequence)} method.
   * <p>
   * Example:
   * <pre><code class='java'> LocalDateTime firstOfJanuary2000 = LocalDateTime.parse("2000-01-01T00:00:00");
   * 
   * // assertion succeeds:
   * assertThat(firstOfJanuary2000).isStrictlyBetween("1999-12-31T23:59:59", "2000-01-01T00:00:01");
   * 
   * // assertions fail:
   * assertThat(firstOfJanuary2000).isStrictlyBetween("1999-01-01T00:00:01", "1999-12-31T23:59:59");
   * assertThat(firstOfJanuary2000).isStrictlyBetween("2000-01-01T00:00:00", "2000-01-01T00:00:01");
   * assertThat(firstOfJanuary2000).isStrictlyBetween("1999-12-31T23:59:59", "2000-01-01T00:00:00");</code></pre>
   * 
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * 
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws DateTimeParseException if any of the given String can't be converted to a {@link LocalDateTime}.
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
  protected LocalDateTime parse(String localDateTimeAsString) {
    return LocalDateTime.parse(localDateTimeAsString);
  }

  /**
   * Returns true if both datetime are in the same year, month and day of month, hour, minute and second, false
   * otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year, month and day of month, hour, minute and second, false
   *         otherwise.
   */
  private static boolean areEqualIgnoringNanos(LocalDateTime actual, LocalDateTime other) {
    return areEqualIgnoringSeconds(actual, other) && actual.getSecond() == other.getSecond();
  }

  /**
   * Returns true if both datetime are in the same year, month, day of month, hour and minute, false otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year, month, day of month, hour and minute, false otherwise.
   */
  private static boolean areEqualIgnoringSeconds(LocalDateTime actual, LocalDateTime other) {
    return areEqualIgnoringMinutes(actual, other) && actual.getMinute() == other.getMinute();
  }

  /**
   * Returns true if both datetime are in the same year, month, day of month and hour, false otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year, month, day of month and hour, false otherwise.
   */
  private static boolean areEqualIgnoringMinutes(LocalDateTime actual, LocalDateTime other) {
    return haveSameYearMonthAndDayOfMonth(actual, other) && actual.getHour() == other.getHour();
  }

  /**
   * Returns true if both datetime are in the same year, month and day of month, false otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year, month and day of month, false otherwise
   */
  private static boolean haveSameYearMonthAndDayOfMonth(LocalDateTime actual, LocalDateTime other) {
    return haveSameYearAndMonth(actual, other) && actual.getDayOfMonth() == other.getDayOfMonth();
  }

  /**
   * Returns true if both datetime are in the same year and month, false otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year and month, false otherwise
   */
  private static boolean haveSameYearAndMonth(LocalDateTime actual, LocalDateTime other) {
    return haveSameYear(actual, other) && actual.getMonth() == other.getMonth();
  }

  /**
   * Returns true if both datetime are in the same year, false otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year, false otherwise
   */
  private static boolean haveSameYear(LocalDateTime actual, LocalDateTime other) {
    return actual.getYear() == other.getYear();
  }
}
