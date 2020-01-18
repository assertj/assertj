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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeEqualIgnoringHours.shouldBeEqualIgnoringHours;
import static org.assertj.core.error.ShouldBeEqualIgnoringMinutes.shouldBeEqualIgnoringMinutes;
import static org.assertj.core.error.ShouldBeEqualIgnoringNanos.shouldBeEqualIgnoringNanos;
import static org.assertj.core.error.ShouldBeEqualIgnoringSeconds.shouldBeEqualIgnoringSeconds;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;

import org.assertj.core.internal.ChronoZonedDateTimeByInstantComparator;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.CheckReturnValue;

public abstract class AbstractZonedDateTimeAssert<SELF extends AbstractZonedDateTimeAssert<SELF>> extends
    AbstractTemporalAssert<SELF, ZonedDateTime> {

  public static final String NULL_DATE_TIME_PARAMETER_MESSAGE = "The ZonedDateTime to compare actual with should not be null";

  /**
   * Check that the {@link ZonedDateTime} to compare actual {@link ZonedDateTime} to is not null, otherwise throws a
   * {@link IllegalArgumentException} with an explicit message
   *
   * @param dateTime the {@link ZonedDateTime} to check
   * @throws IllegalArgumentException with an explicit message if the given {@link ZonedDateTime} is null
   */
  private static void assertDateTimeParameterIsNotNull(ZonedDateTime dateTime) {
    checkArgument(dateTime != null, NULL_DATE_TIME_PARAMETER_MESSAGE);
  }

  /**
   * Verifies that the actual {@code ZonedDateTime} is <b>strictly</b> before the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion succeeds
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore(parse("2020-01-01T01:00:00Z"));
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore(parse("1999-01-01T01:00:00Z"));
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore(parse("2000-01-01T01:00:00Z"));
   * // fails because both ZonedDateTime refer to the same instant (on different offsets)
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore(parse("2000-01-01T00:00:00-01:00"));
   *
   * // succeeds because both ZonedDateTime refer to the same instant and ZonedDateTime natural comparator is used.
   * assertThat(parse("2000-01-02T00:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isBefore(parse("2000-01-02T01:00:00+01:00")); </code></pre>
   *
   * @param other the given {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code ZonedDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not strictly before the given one.
   */
  public SELF isBefore(ZonedDateTime other) {
    assertDateTimeParameterIsNotNull(other);
    comparables.assertIsBefore(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isBefore(ZonedDateTime)} but the {@link ZonedDateTime} is built from given String, which
   * must follow <a
   * href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_DATE_TIME"
   * >ISO date-time format</a> to allow calling {@link ZonedDateTime#parse(CharSequence, DateTimeFormatter)} method.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion succeeds
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore("2020-01-01T01:00:00Z");
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore("1999-01-01T01:00:00Z");
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore("2000-01-01T01:00:00Z");
   * // fails because both ZonedDateTime refer to the same instant (on different offsets)
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore("2000-01-01T00:00:00-01:00");
   *
   * // succeeds because both ZonedDateTime refer to the same instant and ZonedDateTime natural comparator is used.
   * assertThat(parse("2000-01-02T00:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isBefore("2000-01-02T01:00:00+01:00"); </code></pre>
   *
   * @param dateTimeAsString String representing a {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link ZonedDateTime}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not strictly before the {@link ZonedDateTime} built
   *           from given String.
   */
  public SELF isBefore(String dateTimeAsString) {
    assertDateTimeAsStringParameterIsNotNull(dateTimeAsString);
    return isBefore(parse(dateTimeAsString));
  }

  /**
   * Verifies that the actual {@code ZonedDateTime} is before or equals to the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions succeed
   * assertThat(parse("2000-01-01T01:00:00Z")).isBeforeOrEqualTo(parse("2020-01-01T01:00:00Z"))
   *                                          .isBeforeOrEqualTo(parse("2000-01-01T01:00:00Z"))
   *                                          // same instant (on different offsets)
   *                                          .isBeforeOrEqualTo(parse("2000-01-01T00:00:00-01:00"));
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T01:00:00Z")).isBeforeOrEqualTo(parse("1999-01-01T01:00:00Z"));
   * // even though the same instant, fails because of ZonedDateTime natural comparator is used and ZonedDateTime are on different offsets
   * assertThat(parse("2000-01-01T01:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isBeforeOrEqualTo(parse("2000-01-01T00:00:00-01:00")); </code></pre>
   *
   * @param other the given {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code ZonedDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code ZoneDateTime} is not before or equals to the given one.
   */
  public SELF isBeforeOrEqualTo(ZonedDateTime other) {
    assertDateTimeParameterIsNotNull(other);
    comparables.assertIsBeforeOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isBeforeOrEqualTo(ZonedDateTime)} but the {@link ZonedDateTime} is built from given
   * String which must follow <a
   * href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_DATE_TIME"
   * >ISO date-time format</a> to allow calling {@link ZonedDateTime#parse(CharSequence, DateTimeFormatter)} method.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions succeed
   * assertThat(parse("2000-01-01T01:00:00Z")).isBeforeOrEqualTo("2020-01-01T01:00:00Z")
   *                                          .isBeforeOrEqualTo("2000-01-01T01:00:00Z")
   *                                          // same instant (on different offsets)
   *                                          .isBeforeOrEqualTo("2000-01-01T00:00:00-01:00");
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T01:00:00Z")).isBeforeOrEqualTo("1999-01-01T01:00:00Z");
   * // even though the same instant, fails because of ZonedDateTime natural comparator is used and ZonedDateTime are on different offsets
   * assertThat(parse("2000-01-01T01:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isBeforeOrEqualTo("2000-01-01T00:00:00-01:00"); </code></pre>
   *
   * @param dateTimeAsString String representing a {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link ZonedDateTime}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not before or equals to the {@link ZonedDateTime}
   *           built from given String.
   */
  public SELF isBeforeOrEqualTo(String dateTimeAsString) {
    assertDateTimeAsStringParameterIsNotNull(dateTimeAsString);
    return isBeforeOrEqualTo(parse(dateTimeAsString));
  }

  /**
   * Verifies that the actual {@code ZonedDateTime} is after or equals to the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions succeed
   * assertThat(parse("2000-01-01T00:00:00Z")).isAfterOrEqualTo(parse("2000-01-01T00:00:00Z"))
   *                                          .isAfterOrEqualTo(parse("1999-12-31T23:59:59Z"))
   *                                          // same instant in different offset
   *                                          .isAfterOrEqualTo(parse("2000-01-01T00:00:00-01:00"));
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T00:00:00Z")).isAfterOrEqualTo(parse("2001-01-01T00:00:00Z"));
   * // fails even though they refer to the same instant due to ZonedDateTime natural comparator
   * assertThat(parse("2000-01-01T00:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isAfterOrEqualTo(parse("2000-01-01T01:00:00+01:00"));</code></pre>
   *
   * @param other the given {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code ZonedDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not after or equals to the given one.
   */
  public SELF isAfterOrEqualTo(ZonedDateTime other) {
    assertDateTimeParameterIsNotNull(other);
    comparables.assertIsAfterOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isAfterOrEqualTo(ZonedDateTime)} but the {@link ZonedDateTime} is built from given
   * String which must follow <a
   * href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_DATE_TIME"
   * >ISO date-time format</a> to allow calling {@link ZonedDateTime#parse(CharSequence, DateTimeFormatter)} method.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions succeed
   * assertThat(parse("2000-01-01T00:00:00Z")).isAfterOrEqualTo("2000-01-01T00:00:00Z")
   *                                          .isAfterOrEqualTo("1999-12-31T23:59:59Z")
   *                                          // same instant in different offset
   *                                          .isAfter("2000-01-01T00:00:00-01:00");
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T00:00:00Z")).isAfterOrEqualTo("2001-01-01T00:00:00Z");
   * // fails even though they refer to the same instant due to ZonedDateTime natural comparator
   * assertThat(parse("2000-01-01T00:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isAfterOrEqualTo("2000-01-01T01:00:00+01:00");</code></pre>
   *
   * @param dateTimeAsString String representing a {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link ZonedDateTime}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not after or equals to the {@link ZonedDateTime}
   *           built from given String.
   */
  public SELF isAfterOrEqualTo(String dateTimeAsString) {
    assertDateTimeAsStringParameterIsNotNull(dateTimeAsString);
    return isAfterOrEqualTo(parse(dateTimeAsString));
  }

  /**
   * Verifies that the actual {@code ZonedDateTime} is <b>strictly</b> after the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion succeeds
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter(parse("1999-01-01T00:00:00Z"));
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter(parse("2001-01-01T01:00:00Z"));
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter(parse("2000-01-01T01:00:00Z"));
   * // fails because both ZonedDateTime refer to the same instant (on different offsets)
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter(parse("2000-01-01T00:00:00-01:00"));
   *
   * // even though they refer to the same instant assertion succeeds because of different offset
   * assertThat(parse("2000-01-01T01:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isAfter(parse("2000-01-01T00:00:00-01:00"));</code></pre>
   *
   * @param other the given {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code ZonedDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not strictly after the given one.
   */
  public SELF isAfter(ZonedDateTime other) {
    assertDateTimeParameterIsNotNull(other);
    comparables.assertIsAfter(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isAfter(ZonedDateTime)} but the {@link ZonedDateTime} is built from given String, which
   * must follow <a
   * href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_DATE_TIME"
   * >ISO date-time format</a> to allow calling {@link ZonedDateTime#parse(CharSequence, DateTimeFormatter)} method.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion succeeds
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter("1999-01-01T00:00:00Z");
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter("2001-01-01T01:00:00Z");
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter("2000-01-01T01:00:00Z");
   * // fails because both ZonedDateTime refer to the same instant (on different offsets)
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter("2000-01-01T00:00:00-01:00");
   *
   * // even though they refer to the same instant assertion succeeds because of different offset
   * assertThat(parse("2000-01-01T01:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isAfter("2000-01-01T00:00:00-01:00");</code></pre>
   *
   * @param dateTimeAsString String representing a {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link ZonedDateTime}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not strictly after the {@link ZonedDateTime} built from the given String.
   */
  public SELF isAfter(String dateTimeAsString) {
    assertDateTimeAsStringParameterIsNotNull(dateTimeAsString);
    return isAfter(parse(dateTimeAsString));
  }

  /**
   * Verifies that actual and given {@code ZonedDateTime} have same year, month, day, hour, minute and second fields,
   * (nanosecond fields are ignored in comparison).
   * <p>
   * Note that given {@link ZonedDateTime} is converted in the actual's {@link java.time.ZoneId} before comparison.
   * <p>
   * Assertion can fail with dateTimes in same chronological nanosecond time window, e.g :
   * <p>
   * 2000-01-01T00:00:<b>01.000000000</b> and 2000-01-01T00:00:<b>00.999999999</b>.
   * <p>
   * Assertion fails as second fields differ even if time difference is only 1ns.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * ZonedDateTime dateTime1 = ZonedDateTime.of(2000, 1, 1, 0, 0, 1, 0);
   * ZonedDateTime dateTime2 = ZonedDateTime.of(2000, 1, 1, 0, 0, 1, 456);
   * assertThat(dateTime1).isEqualToIgnoringNanos(dateTime2);
   *
   * // failing assertions (even if time difference is only 1ms)
   * ZonedDateTime dateTimeA = ZonedDateTime.of(2000, 1, 1, 0, 0, 1, 0);
   * ZonedDateTime dateTimeB = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 999999999);
   * assertThat(dateTimeA).isEqualToIgnoringNanos(dateTimeB);</code></pre>
   *
   * @param other the given {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code ZonedDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is are not equal with nanoseconds ignored.
   */
  public SELF isEqualToIgnoringNanos(ZonedDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertDateTimeParameterIsNotNull(other);
    ZonedDateTime otherInActualTimeZone = sameInstantInActualTimeZone(other);
    if (!areEqualIgnoringNanos(actual, otherInActualTimeZone)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringNanos(actual, otherInActualTimeZone));
    }
    return myself;
  }

  /**
   * Verifies that actual and given {@link ZonedDateTime} have same year, month, day, hour and minute fields (second and
   * nanosecond fields are ignored in comparison).
   * <p>
   * Note that given {@link ZonedDateTime} is converted in the actual's {@link java.time.ZoneId} before comparison.
   * <p>
   * Assertion can fail with ZonedDateTimes in same chronological second time window, e.g :
   * <p>
   * 2000-01-01T00:<b>01:00</b>.000 and 2000-01-01T00:<b>00:59</b>.000.
   * <p>
   * Assertion fails as minute fields differ even if time difference is only 1ns.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * ZonedDateTime dateTime1 = ZonedDateTime.of(2000, 1, 1, 23, 50, 0, 0);
   * ZonedDateTime dateTime2 = ZonedDateTime.of(2000, 1, 1, 23, 50, 10, 456);
   * assertThat(dateTime1).isEqualToIgnoringSeconds(dateTime2);
   *
   * // failing assertions (even if time difference is only 1ns)
   * ZonedDateTime dateTimeA = ZonedDateTime.of(2000, 1, 1, 23, 50, 00, 0);
   * ZonedDateTime dateTimeB = ZonedDateTime.of(2000, 1, 1, 23, 49, 59, 999999999);
   * assertThat(dateTimeA).isEqualToIgnoringSeconds(dateTimeB);</code></pre>
   *
   * @param other the given {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code ZonedDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is are not equal with second and nanosecond fields
   *           ignored.
   */
  public SELF isEqualToIgnoringSeconds(ZonedDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertDateTimeParameterIsNotNull(other);
    ZonedDateTime otherInActualTimeZone = sameInstantInActualTimeZone(other);
    if (!areEqualIgnoringSeconds(actual, otherInActualTimeZone)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringSeconds(actual, otherInActualTimeZone));
    }
    return myself;
  }

  /**
   * Verifies that actual and given {@code ZonedDateTime} have same year, month, day and hour fields (minute, second and
   * nanosecond fields are ignored in comparison).
   * <p>
   * Note that given {@link ZonedDateTime} is converted in the actual's {@link java.time.ZoneId} before comparison.
   * <p>
   * Assertion can fail with dateTimes in same chronological second time window, e.g :
   * <p>
   * 2000-01-01T<b>01:00</b>:00.000 and 2000-01-01T<b>00:59:59</b>.000.
   * <p>
   * Time difference is only 1s but hour fields differ.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * ZonedDateTime dateTime1 = ZonedDateTime.of(2000, 1, 1, 23, 50, 0, 0);
   * ZonedDateTime dateTime2 = ZonedDateTime.of(2000, 1, 1, 23, 00, 2, 7);
   * assertThat(dateTime1).isEqualToIgnoringMinutes(dateTime2);
   *
   * // failing assertions (even if time difference is only 1ms)
   * ZonedDateTime dateTimeA = ZonedDateTime.of(2000, 1, 1, 01, 00, 00, 000);
   * ZonedDateTime dateTimeB = ZonedDateTime.of(2000, 1, 1, 00, 59, 59, 999);
   * assertThat(dateTimeA).isEqualToIgnoringMinutes(dateTimeB);</code></pre>
   *
   * @param other the given {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code ZonedDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is are not equal ignoring minute, second and nanosecond
   *           fields.
   */
  public SELF isEqualToIgnoringMinutes(ZonedDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertDateTimeParameterIsNotNull(other);
    ZonedDateTime otherInActualTimeZone = sameInstantInActualTimeZone(other);
    if (!areEqualIgnoringMinutes(actual, otherInActualTimeZone)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringMinutes(actual, otherInActualTimeZone));
    }
    return myself;
  }

  /**
   * Verifies that actual and given {@code ZonedDateTime} have same year, month and day fields (hour, minute, second and
   * nanosecond fields are ignored in comparison).
   * <p>
   * Note that given {@link ZonedDateTime} is converted in the actual's {@link java.time.ZoneId} before comparison.
   * <p>
   * Assertion can fail with dateTimes in same chronological minute time window, e.g :
   * <p>
   * 2000-01-<b>01T23:59</b>:00.000 and 2000-01-02T<b>00:00</b>:00.000.
   * <p>
   * Time difference is only 1min but day fields differ.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * ZonedDateTime dateTime1 = ZonedDateTime.of(2000, 1, 1, 23, 59, 59, 999, ZoneId.systemDefault());
   * ZonedDateTime dateTime2 = ZonedDateTime.of(2000, 1, 1, 00, 00, 00, 000, ZoneId.systemDefault());
   * assertThat(dateTime1).isEqualToIgnoringHours(dateTime2);
   *
   * // failing assertions (even if time difference is only 1ms)
   * ZonedDateTime dateTimeA = ZonedDateTime.of(2000, 1, 2, 00, 00, 00, 000, ZoneId.systemDefault());
   * ZonedDateTime dateTimeB = ZonedDateTime.of(2000, 1, 1, 23, 59, 59, 999, ZoneId.systemDefault());
   * assertThat(dateTimeA).isEqualToIgnoringHours(dateTimeB);</code></pre>
   *
   * @param other the given {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if other {@code ZonedDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is are not equal with second and nanosecond fields
   *           ignored.
   */
  public SELF isEqualToIgnoringHours(ZonedDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertDateTimeParameterIsNotNull(other);
    ZonedDateTime otherInActualTimeZone = sameInstantInActualTimeZone(other);
    if (!haveSameYearMonthAndDayOfMonth(actual, otherInActualTimeZone)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringHours(actual, otherInActualTimeZone));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link ZonedDateTime} is equal to the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> ZonedDateTime firstOfJanuary2000InUTC = ZonedDateTime.parse("2000-01-01T00:00:00Z");
   *
   * // both assertions succeed, the second one because the comparison based on the instant they are referring to
   * // 2000-01-01T01:00:00+01:00 = 2000-01-01T00:00:00 in UTC
   * assertThat(firstOfJanuary2000InUTC).isEqualTo(parse("2000-01-01T00:00:00Z"))
   *                                    .isEqualTo(parse("2000-01-01T01:00:00+01:00"));
   *
   * // assertions fail
   * assertThat(firstOfJanuary2000InUTC).isEqualTo(parse("1999-01-01T01:00:00Z"));
   * // fails as the comparator compares the offsets
   * assertThat(firstOfJanuary2000InUTC).usingComparator(ZonedDateTime::compareTo)
   *                                    .isEqualTo(parse("2000-01-01T01:00:00+01:00"));</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not equal to the {@link ZonedDateTime} according
   *                      to the comparator in use.
   */
  @Override
  public SELF isEqualTo(Object expected) {
    if (actual == null || expected == null) {
      super.isEqualTo(expected);
    } else {
      comparables.assertEqual(info, actual, expected);
    }
    return myself;
  }

  /**
   * Same assertion as {@link #isEqualTo(Object)} but the {@link ZonedDateTime} is built from given String which must follow
   * <a href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_DATE_TIME"
   * >ISO date-time format</a> to allow calling {@link ZonedDateTime#parse(CharSequence, DateTimeFormatter)} method.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> ZonedDateTime firstOfJanuary2000InUTC = ZonedDateTime.parse("2000-01-01T00:00:00Z");
   *
   * // both assertions succeed, the second one because the comparison based on the instant they are referring to
   * // 2000-01-01T01:00:00+01:00 = 2000-01-01T00:00:00 in UTC
   * assertThat(firstOfJanuary2000InUTC).isEqualTo("2000-01-01T00:00:00Z")
   *                                    .isEqualTo("2000-01-01T01:00:00+01:00");
   *
   * // assertions fail
   * assertThat(firstOfJanuary2000InUTC).isEqualTo("1999-01-01T01:00:00Z");
   * // fails as the comparator compares the offsets
   * assertThat(firstOfJanuary2000InUTC).usingComparator(ZonedDateTime::compareTo)
   *                                    .isEqualTo("2000-01-01T01:00:00+01:00");</code></pre>
   *
   * @param dateTimeAsString String representing a {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link ZonedDateTime}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not equal to the {@link ZonedDateTime} built from the given String.
   */
  public SELF isEqualTo(String dateTimeAsString) {
    assertDateTimeAsStringParameterIsNotNull(dateTimeAsString);
    return isEqualTo(parse(dateTimeAsString));
  }

  /**
   * Verifies that the actual value is not equal to the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions succeed
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo(parse("2020-01-01T00:00:00Z"));
   * // even though they refer to the same instant, succeeds as the ZonedDateTime comparator checks the offsets
   * assertThat(parse("2000-01-01T00:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isNotEqualTo(parse("2000-01-01T02:00:00+02:00"));
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo(parse("2000-01-01T00:00:00Z"));
   * // fails because the default comparator only checks the instant and they refer to the same
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo(parse("2000-01-01T02:00:00+02:00"));</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is equal to the {@link ZonedDateTime} according
   *                to the comparator in use.
   */
  @Override
  public SELF isNotEqualTo(Object expected) {
    if (actual == null || expected == null) {
      super.isNotEqualTo(expected);
    } else {
      comparables.assertNotEqual(info, actual, expected);
    }
    return myself;
  }

  /**
   * Same assertion as {@code #isNotEqualTo(Object)} but the {@link ZonedDateTime} is built from given String which must follow
   * <a href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_DATE_TIME">ISO date-time format</a>
   * to allow calling {@link ZonedDateTime#parse(CharSequence, DateTimeFormatter)} method.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions succeed
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo("2020-01-01T00:00:00Z");
   * // even though they refer to the same instant, succeeds as the ZonedDateTime comparator checks the offsets
   * assertThat(parse("2000-01-01T00:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isNotEqualTo("2000-01-01T02:00:00+02:00");
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo("2000-01-01T00:00:00Z");
   * // fails because the default comparator only checks the instant and they refer to the same
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo("2000-01-01T02:00:00+02:00");</code></pre>
   *
   * @param dateTimeAsString String representing a {@link ZonedDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link ZonedDateTime}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is equal to the {@link ZonedDateTime} built from given {@link String}.
   */
  public SELF isNotEqualTo(String dateTimeAsString) {
    assertDateTimeAsStringParameterIsNotNull(dateTimeAsString);
    return isNotEqualTo(parse(dateTimeAsString));
  }

  /**
   * Verifies that the actual {@link ZonedDateTime} is equal to one of the given {@link ZonedDateTime} <b>in the actual
   * ZonedDateTime's {@link java.time.ZoneId}</b>.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01T00:00:00Z")).isIn(parse("1999-12-31T23:59:59Z"),
   *                                                parse("2000-01-01T00:00:00Z"));</code></pre>
   *
   * @param expected the given {@link ZonedDateTime}s to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not in the given {@link ZonedDateTime}s.
   */
  public SELF isIn(ZonedDateTime... expected) {
    return isIn((Object[]) changeToActualTimeZone(expected));
  }

  /**
   * Same assertion as {@link #isIn(ZonedDateTime...)} but the {@link ZonedDateTime} are built from given String, which
   * must follow <a
   * href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_DATE_TIME"
   * >ISO date-time format</a> to allow calling {@link ZonedDateTime#parse(CharSequence, DateTimeFormatter)} method.
   * <p>
   * Note that the {@link ZonedDateTime}s created from the given Strings are built in the {@link java.time.ZoneId} of
   * the {@link ZonedDateTime} to check..
   * <p>
   * Example :
   * <pre><code class='java'> // use String based representation of LocalDateTime
   * assertThat(parse("2000-01-01T00:00:00Z")).isIn("1999-12-31T23:59:59Z",
   *                                                "2000-01-01T00:00:00Z");</code></pre>
   *
   * @param dateTimesAsString String array representing {@link ZonedDateTime}s.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link ZonedDateTime}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not in the {@link ZonedDateTime}s built from given
   *           Strings.
   */
  public SELF isIn(String... dateTimesAsString) {
    checkIsNotNullAndNotEmpty(dateTimesAsString);
    return isIn(convertToDateTimeArray(dateTimesAsString));
  }

  /**
   * Verifies that the actual {@link ZonedDateTime} is equal to one of the given {@link ZonedDateTime} <b>in the actual
   * ZonedDateTime's {@link java.time.ZoneId}</b>.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01T00:00:00Z")).isNotIn(parse("1999-12-31T23:59:59Z"),
   *                                                   parse("2000-01-02T00:00:00Z"));</code></pre>
   *
   * @param expected the given {@link ZonedDateTime}s to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not in the given {@link ZonedDateTime}s.
   */
  public SELF isNotIn(ZonedDateTime... expected) {
    return isNotIn((Object[]) changeToActualTimeZone(expected));
  }

  /**
   * Same assertion as {@link #isNotIn(ZonedDateTime...)} but the {@link ZonedDateTime} is built from given String,
   * which must follow <a
   * href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_DATE_TIME"
   * >ISO date-time format</a> to allow calling {@link ZonedDateTime#parse(CharSequence, DateTimeFormatter)} method.
   * <p>
   * Note that the {@link ZonedDateTime}s created from the given Strings are built in the {@link java.time.ZoneId} of
   * the {@link ZonedDateTime} to check..
   * <p>
   * Example :
   * <pre><code class='java'> // use String based representation of ZonedDateTime
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotIn("1999-12-31T23:59:59Z",
   *                                                   "2000-01-02T00:00:00Z");</code></pre>
   *
   * @param dateTimesAsString String array representing {@link ZonedDateTime}s.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code ZonedDateTime} is {@code null}.
   * @throws IllegalArgumentException if given String is null or can't be converted to a {@link ZonedDateTime}.
   * @throws AssertionError if the actual {@code ZonedDateTime} is not equal to the {@link ZonedDateTime} built from
   *           given String.
   */
  public SELF isNotIn(String... dateTimesAsString) {
    checkIsNotNullAndNotEmpty(dateTimesAsString);
    return isNotIn(convertToDateTimeArray(dateTimesAsString));
  }

  /**
   * Verifies that the actual {@link ZonedDateTime} is in the [start, end] period (start and end included) according to
   * the comparator in use.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example:
   * <pre><code class='java'> ZonedDateTime zonedDateTime = ZonedDateTime.now();
   *
   * // assertions succeed:
   * assertThat(zonedDateTime).isBetween(zonedDateTime.minusSeconds(1), zonedDateTime.plusSeconds(1))
   *                           .isBetween(zonedDateTime, zonedDateTime.plusSeconds(1))
   *                           .isBetween(zonedDateTime.minusSeconds(1), zonedDateTime)
   *                           .isBetween(zonedDateTime, zonedDateTime);
   * // succeeds with default comparator which compares the point in time
   * assertThat(parse("2010-01-01T00:00:00Z")).isBetween(parse("2010-01-01T01:00:00+01:00"),
   *                                                     parse("2010-01-01T01:00:00+01:00"));
   *
   * // assertions fail:
   * assertThat(zonedDateTime).isBetween(zonedDateTime.minusSeconds(10), zonedDateTime.minusSeconds(1));
   * assertThat(zonedDateTime).isBetween(zonedDateTime.plusSeconds(1), zonedDateTime.plusSeconds(10));
   * // fails because the comparator checks the offsets are the same
   * assertThat(parse("2010-01-01T00:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isBetween(parse("2010-01-01T01:00:00+01:00"),
   *                                                     parse("2010-01-01T01:00:00+01:00"));</code></pre>
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
  public SELF isBetween(ZonedDateTime startInclusive, ZonedDateTime endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return myself;
  }

  /**
   * Same assertion as {@link #isBetween(ZonedDateTime, ZonedDateTime)} but here you pass {@link ZonedDateTime} String representations
   * which must follow <a href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_ZONED_DATE_TIME">ISO ZonedDateTime format</a>
   * to allow calling {@link ZonedDateTime#parse(CharSequence)} method.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example:
   * <pre><code class='java'> ZonedDateTime firstOfJanuary2000 = ZonedDateTime.parse("2000-01-01T00:00:00Z");
   *
   * // assertions succeed:
   * assertThat(firstOfJanuary2000).isBetween("1999-12-31T23:59:59Z", "2000-01-01T00:00:01Z")
   *                               .isBetween("2000-01-01T00:00:00Z", "2000-01-01T00:00:01Z")
   *                               .isBetween("1999-12-31T23:59:59Z", "2000-01-01T00:00:00Z")
   *                               .isBetween("2000-01-01T00:00:00Z", "2000-01-01T00:00:00Z")
   *                               // same instant as firstOfJanuary2000 but on a different offset
   *                               .isBetween("2000-01-01T01:00:00+01:00", "2000-01-01T01:00:00+01:00");
   *
   * // assertion fails:
   * assertThat(firstOfJanuary2000).isBetween("1999-01-01T00:00:01Z", "1999-12-31T23:59:59Z");</code></pre>
   *
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   *
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws DateTimeParseException if any of the given String can't be converted to a {@link ZonedDateTime}.
   * @throws AssertionError if the actual value is not in [start, end] period.
   *
   * @since 3.7.1
   */
  public SELF isBetween(String startInclusive, String endInclusive) {
    return isBetween(parse(startInclusive), parse(endInclusive));
  }

  /**
   * Verifies that the actual {@link ZonedDateTime} is in the ]start, end[ period (start and end excluded) according to
   * the comparator in use.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example:
   * <pre><code class='java'> ZonedDateTime zonedDateTime = ZonedDateTime.now();
   *
   * // assertions succeed:
   * assertThat(zonedDateTime).isStrictlyBetween(zonedDateTime.minusSeconds(1), zonedDateTime.plusSeconds(1));
   * // succeeds with a different comparator even though the end value refers to the same instant as the actual
   * assertThat(parse("2010-01-01T12:00:00Z")).usingComparator(ZonedDateTime::compareTo)
   *                                          .isStrictlyBetween(parse("2010-01-01T12:59:59+01:00"),
   *                                                             parse("2010-01-01T13:00:00+01:00"));
   *
   * // assertions fail:
   * assertThat(zonedDateTime).isStrictlyBetween(zonedDateTime.minusSeconds(10), zonedDateTime.minusSeconds(1));
   * assertThat(zonedDateTime).isStrictlyBetween(zonedDateTime.plusSeconds(1), zonedDateTime.plusSeconds(10));
   * assertThat(zonedDateTime).isStrictlyBetween(zonedDateTime, zonedDateTime.plusSeconds(1));
   * assertThat(zonedDateTime).isStrictlyBetween(zonedDateTime.minusSeconds(1), zonedDateTime);
   * // fails with default comparator since the end value refers to the same instant as the actual
   * assertThat(parse("2010-01-01T12:00:00Z")).isStrictlyBetween(parse("2010-01-01T12:59:59+01:00"),
   *                                                             parse("2010-01-01T13:00:00+01:00"));</code></pre>
   *
   * @param startExclusive the start value (exclusive), expected not to be null.
   * @param endExclusive the end value (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ period.
   *
   * @since 3.7.1
   */
  public SELF isStrictlyBetween(ZonedDateTime startExclusive, ZonedDateTime endExclusive) {
    comparables.assertIsBetween(info, actual, startExclusive, endExclusive, false, false);
    return myself;
  }

  /**
   * Same assertion as {@link #isStrictlyBetween(ZonedDateTime, ZonedDateTime)} but here you pass {@link ZonedDateTime} String representations
   * which must follow <a href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_ZONED_DATE_TIME">ISO ZonedDateTime format</a>
   * to allow calling {@link ZonedDateTime#parse(CharSequence)} method.
   * <p>
   * <b>Breaking change</b>: since 3.15.0 the default comparator uses {@link ChronoZonedDateTime#timeLineOrder()} which only
   * compares the underlying instant and not the chronology. The underlying comparison is equivalent to comparing the epoch-second and nano-of-second.<br>
   * This behaviour can be overridden by {@link AbstractZonedDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example:
   * <pre><code class='java'> ZonedDateTime firstOfJanuary2000 = ZonedDateTime.parse("2000-01-01T00:00:00Z");
   *
   * // assertions succeed:
   * assertThat(firstOfJanuary2000).isStrictlyBetween("1999-12-31T23:59:59Z", "2000-01-01T00:00:01Z")
   *                               // succeeds with a different comparator even though the end value refers to the same instant as the actual
   *                               .usingComparator(ZonedDateTime::compareTo)
   *                               .isStrictlyBetween("1999-12-31T23:59:59Z", "2000-01-01T01:00:00+01:00");
   *
   * // assertions fail:
   * assertThat(firstOfJanuary2000).isStrictlyBetween("1999-01-01T00:00:01Z", "1999-12-31T23:59:59Z");
   * assertThat(firstOfJanuary2000).isStrictlyBetween("2000-01-01T00:00:00Z", "2000-01-01T00:00:01Z");
   * assertThat(firstOfJanuary2000).isStrictlyBetween("1999-12-31T23:59:59Z", "2000-01-01T00:00:00Z");
   * // fails with default comparator since the end value refers to the same instant as the actual
   * assertThat(parse("2010-01-01T12:00:00Z")).isStrictlyBetween("2010-01-01T12:59:59+01:00", "2010-01-01T13:00:00+01:00");</code></pre>
   *
   * @param startExclusive the start value (exclusive), expected not to be null.
   * @param endExclusive the end value (exclusive), expected not to be null.
   * @return this assertion object.
   *
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws DateTimeParseException if any of the given String can't be converted to a {@link ZonedDateTime}.
   * @throws AssertionError if the actual value is not in ]start, end[ period.
   *
   * @since 3.7.1
   */
  public SELF isStrictlyBetween(String startExclusive, String endExclusive) {
    return isStrictlyBetween(parse(startExclusive), parse(endExclusive));
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    SELF self = super.usingDefaultComparator();
    self.comparables = buildDefaultComparables();
    return self;
  }

  private Comparables buildDefaultComparables() {
    ChronoZonedDateTimeByInstantComparator defaultComparator = ChronoZonedDateTimeByInstantComparator.getInstance();
    return new Comparables(new ComparatorBasedComparisonStrategy(defaultComparator, defaultComparator.description()));
  }

  private ZonedDateTime[] convertToDateTimeArray(String... dateTimesAsString) {
    ZonedDateTime[] dates = new ZonedDateTime[dateTimesAsString.length];
    for (int i = 0; i < dateTimesAsString.length; i++) {
      dates[i] = parse(dateTimesAsString[i]);
    }
    return dates;
  }

  private ZonedDateTime[] changeToActualTimeZone(ZonedDateTime... dateTimes) {
    ZonedDateTime[] dates = new ZonedDateTime[dateTimes.length];
    for (int i = 0; i < dateTimes.length; i++) {
      dates[i] = sameInstantInActualTimeZone(dateTimes[i]);
    }
    return dates;
  }

  private void checkIsNotNullAndNotEmpty(Object[] values) {
    checkArgument(values != null, "The given ZonedDateTime array should not be null");
    checkArgument(values.length > 0, "The given ZonedDateTime array should not be empty");
  }

  /**
   * Obtains an instance of {@link ZonedDateTime} from a string representation in ISO date format.
   *
   * @param dateTimeAsString the string to parse
   * @return the parsed {@link ZonedDateTime}
   */
  @Override
  protected ZonedDateTime parse(String dateTimeAsString) {
    return ZonedDateTime.parse(dateTimeAsString, DateTimeFormatter.ISO_DATE_TIME);
  }

  private ZonedDateTime sameInstantInActualTimeZone(ZonedDateTime zonedDateTime) {
    if (zonedDateTime == null) return null; // nothing to convert in actual's TZ
    if (actual == null) return zonedDateTime; // no actual => let's keep zonedDateTime as it is.
    return zonedDateTime.withZoneSameInstant(actual.getZone());
  }

  /**
   * Check that the {@link ZonedDateTime} string representation to compare actual {@link ZonedDateTime} to is not null,
   * otherwise throws a {@link IllegalArgumentException} with an explicit message
   *
   * @param dateTimeAsString String representing the ZonedDateTime to compare actual with
   * @throws IllegalArgumentException with an explicit message if the given {@link String} is null
   */
  private static void assertDateTimeAsStringParameterIsNotNull(String dateTimeAsString) {
    checkArgument(dateTimeAsString != null,
                  "The String representing the ZonedDateTime to compare actual with should not be null");
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
  private static boolean areEqualIgnoringNanos(ZonedDateTime actual, ZonedDateTime other) {
    return areEqualIgnoringSeconds(actual, other) && actual.getSecond() == other.getSecond();
  }

  /**
   * Returns true if both datetime are in the same year, month, day of month, hour and minute, false otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year, month, day of month, hour and minute, false otherwise.
   */
  private static boolean areEqualIgnoringSeconds(ZonedDateTime actual, ZonedDateTime other) {
    return areEqualIgnoringMinutes(actual, other) && actual.getMinute() == other.getMinute();
  }

  /**
   * Returns true if both datetime are in the same year, month, day of month and hour, false otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year, month, day of month and hour, false otherwise.
   */
  private static boolean areEqualIgnoringMinutes(ZonedDateTime actual, ZonedDateTime other) {
    return haveSameYearMonthAndDayOfMonth(actual, other) && actual.getHour() == other.getHour();
  }

  /**
   * Returns true if both datetime are in the same year, month and day of month, false otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year, month and day of month, false otherwise
   */
  private static boolean haveSameYearMonthAndDayOfMonth(ZonedDateTime actual, ZonedDateTime other) {
    return haveSameYearAndMonth(actual, other) && actual.getDayOfMonth() == other.getDayOfMonth();
  }

  /**
   * Returns true if both datetime are in the same year and month, false otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year and month, false otherwise
   */
  private static boolean haveSameYearAndMonth(ZonedDateTime actual, ZonedDateTime other) {
    return haveSameYear(actual, other) && actual.getMonth() == other.getMonth();
  }

  /**
   * Returns true if both datetime are in the same year, false otherwise.
   *
   * @param actual the actual datetime. expected not be null
   * @param other the other datetime. expected not be null
   * @return true if both datetime are in the same year, false otherwise
   */
  private static boolean haveSameYear(ZonedDateTime actual, ZonedDateTime other) {
    return actual.getYear() == other.getYear();
  }

  protected AbstractZonedDateTimeAssert(ZonedDateTime actual, Class<?> selfType) {
    super(actual, selfType);
    comparables = buildDefaultComparables();
  }

}
