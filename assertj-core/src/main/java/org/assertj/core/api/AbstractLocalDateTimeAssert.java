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

import static java.time.Clock.systemUTC;
import static java.time.LocalDateTime.now;
import static org.assertj.core.error.ShouldBeEqualIgnoringHours.shouldBeEqualIgnoringHours;
import static org.assertj.core.error.ShouldBeEqualIgnoringMinutes.shouldBeEqualIgnoringMinutes;
import static org.assertj.core.error.ShouldBeEqualIgnoringNanos.shouldBeEqualIgnoringNanos;
import static org.assertj.core.error.ShouldBeEqualIgnoringSeconds.shouldBeEqualIgnoringSeconds;
import static org.assertj.core.error.ShouldBeInTheFuture.shouldBeInTheFuture;
import static org.assertj.core.error.ShouldBeInThePast.shouldBeInThePast;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveMonth;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Comparator;

import org.assertj.core.data.TemporalOffset;
import org.assertj.core.data.TemporalUnitOffset;
import org.assertj.core.internal.ChronoLocalDateTimeComparator;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.CheckReturnValue;

/**
 * Assertions for {@link LocalDateTime} type from new Date &amp; Time API introduced in Java 8.
 *
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 * @author Nikolaos Georgiou
 *
 * @param <SELF> the "self" type of this assertion class.
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
    this.comparables = buildDefaultComparables();
  }

  /**
   * Verifies that the actual {@code LocalDateTime} is <b>strictly</b> before the given one according to the {@link ChronoLocalDateTime#timeLineOrder()} comparator
   * which is consistent with {@link LocalDateTime#isBefore(ChronoLocalDateTime)}.
   * <p>
   * {@link ChronoLocalDateTime#timeLineOrder()} compares {@code LocalDateTime} in time-line order <b>ignoring the chronology</b>, this is equivalent to comparing the epoch-day and nano-of-day.
   * <p>
   * This behaviour can be overridden by {@link AbstractLocalDateTimeAssert#usingComparator(Comparator)}.
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
    assertLocalDateTimeParameterIsNotNull(other);
    comparables.assertIsBefore(info, actual, other);
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
   * Verifies that the actual {@code LocalDateTime} is before or equals to the given one according to the {@link ChronoLocalDateTime#timeLineOrder()} comparator
   * which is consistent with {@link LocalDateTime#isBefore(ChronoLocalDateTime)}.
   * <p>
   * {@link ChronoLocalDateTime#timeLineOrder()} compares {@code LocalDateTime} in time-line order <b>ignoring the chronology</b>, this is equivalent to comparing the epoch-day and nano-of-day.
   * <p>
   * This behaviour can be overridden by {@link AbstractLocalDateTimeAssert#usingComparator(Comparator)}.
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
    assertLocalDateTimeParameterIsNotNull(other);
    comparables.assertIsBeforeOrEqualTo(info, actual, other);
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
   * Verifies that the actual {@code LocalDateTime} is after or equals to the given one according to the {@link ChronoLocalDateTime#timeLineOrder()} comparator
   * which is consistent with {@link LocalDateTime#isAfter(ChronoLocalDateTime)}.
   * <p>
   * {@link ChronoLocalDateTime#timeLineOrder()} compares {@code LocalDateTime} in time-line order <b>ignoring the chronology</b>, this is equivalent to comparing the epoch-day and nano-of-day.
   * <p>
   * This behaviour can be overridden by {@link AbstractLocalDateTimeAssert#usingComparator(Comparator)}.
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
    assertLocalDateTimeParameterIsNotNull(other);
    comparables.assertIsAfterOrEqualTo(info, actual, other);
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
   * Verifies that the actual {@code LocalDateTime} is <b>strictly</b> after the given one according to the {@link ChronoLocalDateTime#timeLineOrder()} comparator
   * which is consistent with {@link LocalDateTime#isAfter(ChronoLocalDateTime)}.
   * <p>
   * {@link ChronoLocalDateTime#timeLineOrder()} compares {@code LocalDateTime} in time-line order <b>ignoring the chronology</b>, this is equivalent to comparing the epoch-day and nano-of-day.
   * <p>
   * This behaviour can be overridden by {@link AbstractLocalDateTimeAssert#usingComparator(Comparator)}.
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
    assertLocalDateTimeParameterIsNotNull(other);
    comparables.assertIsAfter(info, actual, other);
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
   * Verifies that the actual {@code LocalDateTime} is equal to the given one according to the {@link ChronoLocalDateTime#timeLineOrder()} comparator
   * which is consistent with {@link LocalDateTime#isEqual(ChronoLocalDateTime)}.
   * <p>
   * {@link ChronoLocalDateTime#timeLineOrder()} compares {@code LocalDateTime} in time-line order <b>ignoring the chronology</b>, this is equivalent to comparing the epoch-day and nano-of-day.
   * <p>
   * This behaviour can be overridden by {@link AbstractLocalDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01T00:00:00")).isEqualTo(parse("2000-01-01T00:00:00"));</code></pre>
   *
   * @param other the given {@link LocalDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} differs from the given {@code LocalDateTime}
   *            according to the comparator in use.
   */
  @Override
  public SELF isEqualTo(Object other) {
    if (actual == null || other == null) {
      super.isEqualTo(other);
    } else {
      comparables.assertEqual(info, actual, other);
    }
    return myself;
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
   * Verifies that the actual {@code LocalDateTime} is not equal to the given one according to the {@link ChronoLocalDateTime#timeLineOrder()} comparator
   * which is consistent with {@link LocalDateTime#isEqual(ChronoLocalDateTime)}.
   * <p>
   * {@link ChronoLocalDateTime#timeLineOrder()} compares {@code LocalDateTime} in time-line order <b>ignoring the chronology</b>, this is equivalent to comparing the epoch-day and nano-of-day.
   * <p>
   * This behaviour can be overridden by {@link AbstractLocalDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat(parse("2000-01-01T00:00:00")).isEqualTo(parse("2000-01-01T00:00:00"));</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} equals to the given {@code LocalDateTime}
   *            according to the comparator in use.
   */
  @Override
  public SELF isNotEqualTo(Object other) {
    if (actual == null || other == null) {
      super.isNotEqualTo(other);
    } else {
      comparables.assertNotEqual(info, actual, other);
    }
    return myself;
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

  /**
   * Verifies that the actual {@link LocalDateTime} is close to the current date and time on the UTC timezone,
   * according to the given {@link TemporalUnitOffset}.
   * You can build the offset parameter using {@link Assertions#within(long, TemporalUnit)} or {@link Assertions#byLessThan(long, TemporalUnit)}.
   * <p>
   * If the difference is equal to the offset, the assertion succeeds.
   * <p>
   * Example:
   * <pre><code class='java'> LocalDateTime actual = LocalDateTime.now(Clock.systemUTC());
   *
   * // assertion will pass as if executed less than one second after actual was built
   * assertThat(actual).isCloseToUtcNow(within(1, ChronoUnit.SECONDS));
   *
   * // assertion will fail
   * assertThat(actual.plusSeconds(2)).isCloseToUtcNow(within(1, ChronoUnit.SECONDS));</code></pre>
   *
   * @param offset The offset used for comparison
   * @return this assertion object
   * @throws NullPointerException if {@code offset} parameter is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not close to the current time by less than the given offset.
   */
  public SELF isCloseToUtcNow(TemporalUnitOffset offset) {
    return isCloseTo(now(systemUTC()), offset);
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
    ChronoLocalDateTimeComparator defaultComparator = ChronoLocalDateTimeComparator.getInstance();
    return new Comparables(new ComparatorBasedComparisonStrategy(defaultComparator, defaultComparator.description()));
  }

  private static Object[] convertToLocalDateTimeArray(String... dateTimesAsString) {
    return Arrays.stream(dateTimesAsString)
                 .map(LocalDateTime::parse)
                 .toArray();
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
   * @throws AssertionError if the actual {@code LocalDateTime} is are not equal with second and nanosecond fields ignored.
   * @deprecated Use {@link #isCloseTo(LocalDateTime, TemporalOffset)} instead, although not exactly the same semantics,
   * this is the right way to compare with a given precision.
   */
  @Deprecated
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
   * @throws AssertionError if the actual {@code LocalDateTime} is are not equal ignoring minute, second and nanosecond fields.
   * @deprecated Use {@link #isCloseTo(LocalDateTime, TemporalOffset)} instead, although not exactly the same semantics,
   * this is the right way to compare with a given precision.
   */
  @Deprecated
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
   * @throws AssertionError if the actual {@code LocalDateTime} is are not equal with second and nanosecond fields ignored.
   * @deprecated Use {@link #isCloseTo(LocalDateTime, TemporalOffset)} instead, although not exactly the same semantics,
   * this is the right way to compare with a given precision.
   */
  @Deprecated
  public SELF isEqualToIgnoringHours(LocalDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertLocalDateTimeParameterIsNotNull(other);
    if (!haveSameYearMonthAndDayOfMonth(actual, other)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringHours(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code LocalDateTime} is strictly in the past.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(LocalDateTime.now().minusMinutes(1)).isInThePast();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not in the past.
   *
   * @since 3.25.0
   */
  public SELF isInThePast() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isBefore(LocalDateTime.now())) throw Failures.instance().failure(info, shouldBeInThePast(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code LocalDateTime} is strictly in the future.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(LocalDateTime.now().plusMinutes(1)).isInTheFuture();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not in the future.
   *
   * @since 3.25.0
   */
  public SELF isInTheFuture() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isAfter(LocalDateTime.now())) throw Failures.instance().failure(info, shouldBeInTheFuture(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link LocalDateTime} is in the [start, end] period (start and end included) according to the {@link ChronoLocalDateTime#timeLineOrder()} comparator.
   * <p>
   * {@link ChronoLocalDateTime#timeLineOrder()} compares {@code LocalDateTime} in time-line order <b>ignoring the chronology</b>, this is equivalent to comparing the epoch-day and nano-of-day.
   * <p>
   * This behaviour can be overridden by {@link AbstractLocalDateTimeAssert#usingComparator(Comparator)}.
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
   * Verifies that the actual {@link LocalDateTime} is in the ]start, end[ period (start and end excluded) according to the {@link ChronoLocalDateTime#timeLineOrder()} comparator.
   * <p>
   * {@link ChronoLocalDateTime#timeLineOrder()} compares {@code LocalDateTime} in time-line order <b>ignoring the chronology</b>, this is equivalent to comparing the epoch-day and nano-of-day.
   * <p>
   * This behaviour can be overridden by {@link AbstractLocalDateTimeAssert#usingComparator(Comparator)}.
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
  public SELF isStrictlyBetween(LocalDateTime startExclusive, LocalDateTime endExclusive) {
    comparables.assertIsBetween(info, actual, startExclusive, endExclusive, false, false);
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
   * @param startExclusive the start value (exclusive), expected not to be null.
   * @param endExclusive the end value (exclusive), expected not to be null.
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
  public SELF isStrictlyBetween(String startExclusive, String endExclusive) {
    return isStrictlyBetween(parse(startExclusive), parse(endExclusive));
  }

  /**
   * Verifies that actual {@code LocalDateTime} is in the given year.
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds:
   * assertThat(LocalDateTime.of(2002, 1, 1, 0, 0, 0)).hasYear(2002);
   *
   * // Assertion fails:
   * assertThat(LocalDate.of(2002, 1, 1, 0, 0, 0)).hasYear(2001);</code></pre>
   *
   * @param year the given year.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not in the given year.
   *
   * @since 3.23.0
   */
  public SELF hasYear(int year) {
    Objects.instance().assertNotNull(info, actual);
    if (actual.getYear() != year) {
      throw Failures.instance().failure(info, shouldHaveDateField(actual, "year", year));
    }
    return myself;
  }

  /**
   * Verifies that actual {@link LocalDateTime} is in the given {@link Month}.
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds:
   * assertThat(LocalDateTime.of(2022, Month.APRIL, 16, 20, 18, 59)).hasMonth(Month.APRIL);
   *
   * // Assertion fails:
   * assertThat(LocalDateTime.of(2022, Month.APRIL, 16, 20, 18, 59)).hasMonth(Month.MAY); </code></pre>
   *
   * @param month the given {@link Month}.
   * @return this assertion object.
   * @throws IllegalArgumentException if the given Month is null.
   * @throws AssertionError           if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError           if the actual {@code LocalDateTime} is not in the given {@code Month}.
   *
   * @since 3.23.0
   */
  public SELF hasMonth(Month month) {
    checkArgument(month != null, "The given Month should not be null");
    Objects.instance().assertNotNull(info, actual);
    if (!actual.getMonth().equals(month)) {
      throw Failures.instance().failure(info, shouldHaveMonth(actual, month));
    }
    return myself;
  }

  /**
   * Verifies that actual {@link LocalDateTime} has same month value.
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds:
   * assertThat(LocalDateTime.of(2000, 12, 31, 23, 59, 59)).hasMonthValue(12);
   *
   * // Assertion fails:
   * assertThat(LocalDateTime.of(2000, 12, 31, 23, 59, 59)).hasMonthValue(3); </code></pre>
   *
   * @param monthVal the given month value between 1 and 12 inclusive.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not equal with month field.
   *
   * @since 3.23.0
   */
  public SELF hasMonthValue(int monthVal) {
    Objects.instance().assertNotNull(info, actual);
    if (actual.getMonthValue() != monthVal) {
      throw Failures.instance().failure(info, shouldHaveDateField(actual, "month", monthVal));
    }
    return myself;
  }

  /**
   * Verifies that actual {@code LocalDateTime} is in the given day of month.
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds:
   * assertThat(LocalDateTime.of(2002, 1, 1, 0, 0, 0)).hasDayOfMonth(1);
   *
   * // Assertion fails:
   * assertThat(LocalDate.of(2002, 1, 1, 0, 0, 0)).hasDayOfMonth(2);</code></pre>
   *
   * @param dayOfMonth the given numeric day.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not in the given day of month.
   *
   * @since 3.23.0
   */
  public SELF hasDayOfMonth(int dayOfMonth) {
    Objects.instance().assertNotNull(info, actual);
    if (actual.getDayOfMonth() != dayOfMonth) {
      throw Failures.instance().failure(info, shouldHaveDateField(actual, "day of month", dayOfMonth));
    }
    return myself;
  }

  /**
   * Verifies that actual {@code LocalDateTime} is in the given hour.
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds:
   * assertThat(LocalDateTime.of(2021, 12, 31, 23, 59, 59)).hasHour(23);
   *
   * // Assertion fails:
   * assertThat(LocalDateTime.of(2021, 12, 31, 23, 59, 59)).hasHour(22);</code></pre>
   *
   * @param hour the given hour.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not in the given hour.
   *
   * @since 3.23.0
   */
  public SELF hasHour(int hour) {
    Objects.instance().assertNotNull(info, actual);
    if (actual.getHour() != hour) {
      throw Failures.instance().failure(info, shouldHaveDateField(actual, "hour", hour));
    }
    return myself;
  }

  /**
   * Verifies that actual {@code LocalDateTime} is in the given minute.
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds:
   * assertThat(LocalDateTime.of(2021, 12, 31, 23, 59, 00)).hasMinute(59);
   *
   * // Assertion fails:
   * assertThat(LocalDateTime.of(2021, 12, 31, 23, 59, 00)).hasMinute(58);</code></pre>
   *
   * @param minute the given minute.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not in the given minute.
   *
   * @since 3.23.0
   */
  public SELF hasMinute(int minute) {
    Objects.instance().assertNotNull(info, actual);
    if (actual.getMinute() != minute) {
      throw Failures.instance().failure(info, shouldHaveDateField(actual, "minute", minute));
    }
    return myself;
  }

  /**
   * Verifies that actual {@code LocalDateTime} is in the given second.
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds:
   * assertThat(LocalDateTime.of(2021, 12, 31, 23, 59, 00)).hasSecond(00);
   *
   * // Assertion fails:
   * assertThat(LocalDateTime.of(2021, 12, 31, 23, 59, 00)).hasSecond(17);</code></pre>
   *
   * @param second the given second.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not in the given second.
   *
   * @since 3.23.0
   */
  public SELF hasSecond(int second) {
    Objects.instance().assertNotNull(info, actual);
    if (actual.getSecond() != second) {
      throw Failures.instance().failure(info, shouldHaveDateField(actual, "second", second));
    }
    return myself;
  }

  /**
   * Verifies that actual {@code LocalDateTime} is in the given nanosecond.
   * <p>
   * Example:
   * <pre><code class='java'> // Assertion succeeds:
   * assertThat(LocalDateTime.of(2021, 12, 31, 23, 59, 15)).hasNano(15);
   *
   * // Assertion fails:
   * assertThat(LocalDateTime.of(2021, 12, 31, 23, 59, 00)).hasNano(15);</code></pre>
   *
   * @param nano the given second.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not in the given nanosecond.
   *
   * @since 3.23.0
   */
  public SELF hasNano(int nano) {
    Objects.instance().assertNotNull(info, actual);
    if (actual.getNano() != nano) {
      throw Failures.instance().failure(info, shouldHaveDateField(actual, "nanosecond", nano));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link LocalDateTime} is close to the other according to the given {@link TemporalOffset}.
   * <p>
   * You can build the offset parameter using {@link Assertions#within(long, TemporalUnit)} or {@link Assertions#byLessThan(long, TemporalUnit)}.
   * <p>
   * Example:
   * <pre><code class='java'> LocalDateTime localDateTime = LocalDateTime.now();
   *
   * // assertion succeeds:
   * assertThat(localDateTime).isCloseTo(localDateTime.plusHours(1), within(32, ChronoUnit.MINUTES));
   *
   * // assertion fails:
   * assertThat(localDateTime).isCloseTo(localDateTime.plusHours(1), within(10, ChronoUnit.SECONDS));</code></pre>
   *
   * @param other the localDateTime to compare actual to
   * @param offset the offset used for comparison
   * @return this assertion object
   * @throws NullPointerException if {@code LocalDateTime} or {@code TemporalOffset} parameter is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code LocalDateTime} is not close to the given one for a provided offset.
   */
  @Override
  public SELF isCloseTo(LocalDateTime other, TemporalOffset<? super LocalDateTime> offset) {
    return super.isCloseTo(other, offset);
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
