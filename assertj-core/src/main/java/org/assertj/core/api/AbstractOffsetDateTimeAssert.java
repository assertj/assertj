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
import static java.time.OffsetDateTime.now;
import static org.assertj.core.error.ShouldBeAtSameInstant.shouldBeAtSameInstant;
import static org.assertj.core.error.ShouldBeEqualIgnoringHours.shouldBeEqualIgnoringHours;
import static org.assertj.core.error.ShouldBeEqualIgnoringMinutes.shouldBeEqualIgnoringMinutes;
import static org.assertj.core.error.ShouldBeEqualIgnoringNanos.shouldBeEqualIgnoringNanos;
import static org.assertj.core.error.ShouldBeEqualIgnoringSeconds.shouldBeEqualIgnoringSeconds;
import static org.assertj.core.error.ShouldBeEqualIgnoringTimezone.shouldBeEqualIgnoringTimezone;
import static org.assertj.core.error.ShouldBeInTheFuture.shouldBeInTheFuture;
import static org.assertj.core.error.ShouldBeInThePast.shouldBeInThePast;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalUnit;
import java.util.Comparator;

import org.assertj.core.data.TemporalOffset;
import org.assertj.core.data.TemporalUnitOffset;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.OffsetDateTimeByInstantComparator;
import org.assertj.core.util.CheckReturnValue;

/**
 * Assertions for {@link java.time.OffsetDateTime} type from new Date &amp; Time API introduced in Java 8.
 *
 * @author Paweł Stawicki
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 * @author Nikolaos Georgiou
 *
 * @param <SELF> the "self" type of this assertion class.
 */
public abstract class AbstractOffsetDateTimeAssert<SELF extends AbstractOffsetDateTimeAssert<SELF>> extends
    AbstractTemporalAssert<SELF, OffsetDateTime> {

  public static final String NULL_OFFSET_DATE_TIME_PARAMETER_MESSAGE = "The OffsetDateTime to compare actual with should not be null";

  /**
   * Creates a new <code>{@link org.assertj.core.api.AbstractOffsetDateTimeAssert}</code>.
   *
   * @param selfType the "self type"
   * @param actual the actual value to verify
   */
  protected AbstractOffsetDateTimeAssert(OffsetDateTime actual, Class<?> selfType) {
    super(actual, selfType);
    comparables = buildDefaultComparables();
  }

  /**
   * Verifies that the actual {@code OffsetDateTime} is <b>strictly</b> before the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion succeeds
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore(parse("2020-01-01T01:00:00Z"));
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore(parse("1999-01-01T01:00:00Z"));
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore(parse("2000-01-01T01:00:00Z"));
   * // fails because both OffsetDateTime refer to the same instant (on different offsets)
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore(parse("2000-01-01T00:00:00-01:00"));
   *
   * // succeeds because both OffsetDateTime refer to the same instant and OffsetDateTime natural comparator is used.
   * assertThat(parse("2000-01-02T00:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isBefore(parse("2000-01-02T01:00:00+01:00")); </code></pre>
   *
   * @param other the given {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not strictly before the given one according to
   *                    the comparator in use.
   * @throws IllegalArgumentException if other {@code OffsetDateTime} is {@code null}.
   */
  public SELF isBefore(OffsetDateTime other) {
    assertOffsetDateTimeParameterIsNotNull(other);
    comparables.assertIsBefore(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isBefore(java.time.OffsetDateTime)} but the {@link java.time.OffsetDateTime} is built
   * from given String, which
   * must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME"
   * >ISO OffsetDateTime format</a> to allow calling {@link java.time.OffsetDateTime#parse(CharSequence)} method.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion succeeds
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore("2020-01-01T01:00:00Z");
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore("1999-01-01T01:00:00Z");
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore("2000-01-01T01:00:00Z");
   * // fails because both OffsetDateTime refer to the same instant (on different offsets)
   * assertThat(parse("2000-01-01T01:00:00Z")).isBefore("2000-01-01T00:00:00-01:00");
   *
   * // succeeds because both OffsetDateTime refer to the same instant and OffsetDateTime natural comparator is used.
   * assertThat(parse("2000-01-02T00:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isBefore("2000-01-02T01:00:00+01:00"); </code></pre>
   *
   * @param offsetDateTimeAsString String representing a {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not strictly before the
   *           {@link java.time.OffsetDateTime} built
   *           from given String.
   * @throws IllegalArgumentException if given String is null or can't be converted to a
   *           {@link java.time.OffsetDateTime}.
   */
  public SELF isBefore(String offsetDateTimeAsString) {
    assertOffsetDateTimeAsStringParameterIsNotNull(offsetDateTimeAsString);
    return isBefore(parse(offsetDateTimeAsString));
  }

  /**
   * Verifies that the actual {@code OffsetDateTime} is before or equals to the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
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
   * // even though the same instant, fails because of OffsetDateTime natural comparator is used and OffsetDateTime are on different offsets
   * assertThat(parse("2000-01-01T01:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isBeforeOrEqualTo(parse("2000-01-01T00:00:00-01:00")); </code></pre>
   *
   * @param other the given {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not before or equals to the given one according to
   * the comparator in use.
   * @throws IllegalArgumentException if other {@code OffsetDateTime} is {@code null}.
   */
  public SELF isBeforeOrEqualTo(OffsetDateTime other) {
    assertOffsetDateTimeParameterIsNotNull(other);
    comparables.assertIsBeforeOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isBeforeOrEqualTo(java.time.OffsetDateTime)} but the {@link java.time.OffsetDateTime} is
   * built from given
   * String, which must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME"
   * >ISO OffsetDateTime format</a> to allow calling {@link java.time.OffsetDateTime#parse(CharSequence)} method.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
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
   * // even though the same instant, fails because of OffsetDateTime natural comparator is used and OffsetDateTime are on different offsets
   * assertThat(parse("2000-01-01T01:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isBeforeOrEqualTo("2000-01-01T00:00:00-01:00"); </code></pre>
   *
   * @param offsetDateTimeAsString String representing a {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not before or equals to the
   *           {@link java.time.OffsetDateTime} built from given String.
   * @throws IllegalArgumentException if given String is null or can't be converted to a
   *           {@link java.time.OffsetDateTime}.
   */
  public SELF isBeforeOrEqualTo(String offsetDateTimeAsString) {
    assertOffsetDateTimeAsStringParameterIsNotNull(offsetDateTimeAsString);
    return isBeforeOrEqualTo(parse(offsetDateTimeAsString));
  }

  /**
   * Verifies that the actual {@code OffsetDateTime} is after or equals to the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
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
   * // fails even though they refer to the same instant due to OffsetDateTime natural comparator
   * assertThat(parse("2000-01-01T00:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isAfterOrEqualTo(parse("2000-01-01T01:00:00+01:00"));</code></pre>
   *
   * @param other the given {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not after or equals to the given one according to
   *                        the comparator in use.
   * @throws IllegalArgumentException if other {@code OffsetDateTime} is {@code null}.
   */
  public SELF isAfterOrEqualTo(OffsetDateTime other) {
    assertOffsetDateTimeParameterIsNotNull(other);
    comparables.assertIsAfterOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isAfterOrEqualTo(java.time.OffsetDateTime)} but the {@link java.time.OffsetDateTime} is
   * built from given
   * String, which must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME"
   * >ISO OffsetDateTime format</a> to allow calling {@link java.time.OffsetDateTime#parse(CharSequence)} method.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions succeed
   * assertThat(parse("2000-01-01T00:00:00Z")).isAfterOrEqualTo("2000-01-01T00:00:00Z")
   *                                          .isAfterOrEqualTo("1999-12-31T23:59:59Z")
   *                                          // same instant in different offset
   *                                          .isAfterOrEqualTo("2000-01-01T00:00:00-01:00");
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T00:00:00Z")).isAfterOrEqualTo("2001-01-01T00:00:00Z");
   * // fails even though they refer to the same instant due to OffsetDateTime natural comparator
   * assertThat(parse("2000-01-01T00:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isAfterOrEqualTo("2000-01-01T01:00:00+01:00");</code></pre>
   *
   * @param offsetDateTimeAsString String representing a {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not after or equals to the
   *           {@link java.time.OffsetDateTime} built from given String.
   * @throws IllegalArgumentException if given String is null or can't be converted to a
   *           {@link java.time.OffsetDateTime}.
   */
  public SELF isAfterOrEqualTo(String offsetDateTimeAsString) {
    assertOffsetDateTimeAsStringParameterIsNotNull(offsetDateTimeAsString);
    return isAfterOrEqualTo(parse(offsetDateTimeAsString));
  }

  /**
   * Verifies that the actual {@code OffsetDateTime} is <b>strictly</b> after the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions succeed:
   *  assertThat(parse("2000-01-01T00:00:00Z")).isAfter(parse("1999-12-31T23:59:59Z"));
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter(parse("2001-01-01T01:00:00Z"));
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter(parse("2000-01-01T01:00:00Z"));
   * // fails because both OffsetDateTime refer to the same instant (on different offsets)
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter(parse("2000-01-01T00:00:00-01:00"));
   *
   * // even though they refer to the same instant assertion succeeds because of different offset
   * assertThat(parse("2000-01-01T01:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isAfter(parse("2000-01-01T00:00:00-01:00"));</code></pre>
   *
   * @param other the given {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not strictly after the given one according to
   *                        the comparator in use.
   * @throws IllegalArgumentException if other {@code OffsetDateTime} is {@code null}.
   */
  public SELF isAfter(OffsetDateTime other) {
    assertOffsetDateTimeParameterIsNotNull(other);
    comparables.assertIsAfter(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isAfter(java.time.OffsetDateTime)} but the {@link java.time.OffsetDateTime} is built from
   * given a String that
   * must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME"
   * >ISO OffsetDateTime format</a> to allow calling {@link java.time.OffsetDateTime#parse(CharSequence)} method.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion succeeds
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter("1999-01-01T00:00:00Z");
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter("2001-01-01T01:00:00Z");
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter("2000-01-01T01:00:00Z");
   * // fails because both OffsetDateTime refer to the same instant (on different offsets)
   * assertThat(parse("2000-01-01T01:00:00Z")).isAfter("2000-01-01T00:00:00-01:00");
   *
   * // even though they refer to the same instant assertion succeeds because of different offset
   * assertThat(parse("2000-01-01T01:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isAfter("2000-01-01T00:00:00-01:00");</code></pre>
   *
   * @param offsetDateTimeAsString String representing a {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not strictly after the
   *           {@link java.time.OffsetDateTime} built from given String.
   * @throws IllegalArgumentException if given String is null or can't be converted to a
   *           {@link java.time.OffsetDateTime}.
   */
  public SELF isAfter(String offsetDateTimeAsString) {
    assertOffsetDateTimeAsStringParameterIsNotNull(offsetDateTimeAsString);
    return isAfter(parse(offsetDateTimeAsString));
  }

  /**
   * Verifies that the actual {@code OffsetDateTime} is equal to the given one according to the comparator in use.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> OffsetDateTime firstOfJanuary2000InUTC = OffsetDateTime.parse("2000-01-01T00:00:00Z");
   *
   * // both assertions succeed, the second one because the comparison based on the instant they are referring to
   * // 2000-01-01T01:00:00+01:00 = 2000-01-01T00:00:00 in UTC
   * assertThat(firstOfJanuary2000InUTC).isEqualTo(parse("2000-01-01T00:00:00Z"))
   *                                    .isEqualTo(parse("2000-01-01T01:00:00+01:00"));
   *
   * // assertions fail
   * assertThat(firstOfJanuary2000InUTC).isEqualTo(parse("1999-01-01T01:00:00Z"));
   * // fails as the comparator compares the offsets
   * assertThat(firstOfJanuary2000InUTC).usingComparator(OffsetDateTime::compareTo)
   *                                    .isEqualTo(parse("2000-01-01T01:00:00+01:00"));</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not equal to the given one according to
   *                        the comparator in use.
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
   * Verifies that the actual {@link OffsetDateTime} is close to the current date and time on the UTC timezone,
   * according to the given {@link TemporalUnitOffset}.
   * You can build the offset parameter using {@link Assertions#within(long, TemporalUnit)} or {@link Assertions#byLessThan(long, TemporalUnit)}.
   * <p>
   * If the difference is equal to the offset, the assertion succeeds.
   * <p>
   * Example:
   * <pre><code class='java'> OffsetDateTime actual = OffsetDateTime.now(Clock.systemUTC());
   *
   * // assertion will pass as if executed less than one second after actual was built
   * assertThat(actual).isCloseToUtcNow(within(1, ChronoUnit.SECONDS));
   *
   * // assertion will fail
   * assertThat(actual.plusSeconds(2)).isCloseToUtcNow(within(1, ChronoUnit.SECONDS));</code></pre>
   *
   * @param offset The offset used for comparison
   * @return this assertion object
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not close to the current time by less than the given offset.
   * @throws NullPointerException if {@code offset} parameter is {@code null}.
   */
  public SELF isCloseToUtcNow(TemporalUnitOffset offset) {
    return isCloseTo(now(systemUTC()), offset);
  }

  /**
   * Verifies that the actual {@link OffsetDateTime} is close to the other according to the given {@link TemporalOffset}.
   * <p>
   * You can build the offset parameter using {@link Assertions#within(long, TemporalUnit)} or {@link Assertions#byLessThan(long, TemporalUnit)}.
   * <p>
   * Example:
   * <pre><code class='java'> OffsetDateTime offsetDateTime = OffsetDateTime.parse("2000-01-01T00:00:00Z");
   *
   * // assertions succeeds:
   * assertThat(offsetDateTime).isCloseTo(offsetDateTime.plusHours(1), within(90, ChronoUnit.MINUTES));
   *
   * // assertions fails:
   * assertThat(offsetDateTime).isCloseTo(offsetDateTime.plusHours(1), within(30, ChronoUnit.MINUTES));</code></pre>
   * @param other the OffsetDateTime to compare actual to
   * @param offset the offset used for comparison
   * @return this assertion object
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not close to the given one for a provided offset.
   * @throws NullPointerException if {@code OffsetDateTime} or {@code TemporalOffset} parameter is {@code null}.
   */
  @Override
  public SELF isCloseTo(OffsetDateTime other, TemporalOffset<? super OffsetDateTime> offset) {
    // overridden for javadoc
    return super.isCloseTo(other, offset);
  }

  /**
   * Same assertion as {@link #isEqualTo(Object)} (where Object is expected to be {@link java.time.OffsetDateTime}) but
   * here you
   * pass {@link java.time.OffsetDateTime} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME"
   * >ISO OffsetDateTime format</a> to allow calling {@link java.time.OffsetDateTime#parse(CharSequence)} method.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> OffsetDateTime firstOfJanuary2000InUTC = OffsetDateTime.parse("2000-01-01T00:00:00Z");
   *
   * // both assertions succeed, the second one because the comparison based on the instant they are referring to
   * // 2000-01-01T01:00:00+01:00 = 2000-01-01T00:00:00 in UTC
   * assertThat(firstOfJanuary2000InUTC).isEqualTo("2000-01-01T00:00:00Z")
   *                                    .isEqualTo("2000-01-01T01:00:00+01:00");
   *
   * // assertions fail
   * assertThat(firstOfJanuary2000InUTC).isEqualTo("1999-01-01T01:00:00Z");
   * // fails as the comparator compares the offsets
   * assertThat(firstOfJanuary2000InUTC).usingComparator(OffsetDateTime::compareTo)
   *                                    .isEqualTo("2000-01-01T01:00:00+01:00");</code></pre>
   *
   * @param dateTimeAsString String representing a {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not equal to the {@link java.time.OffsetDateTime}
   *           built from given String.
   * @throws IllegalArgumentException if given String is null or can't be converted to a
   *           {@link java.time.OffsetDateTime}.
   */
  public SELF isEqualTo(String dateTimeAsString) {
    assertOffsetDateTimeAsStringParameterIsNotNull(dateTimeAsString);
    return isEqualTo(parse(dateTimeAsString));
  }

  /**
   * Verifies that the actual {@code OffsetDateTime} is not equal to the given value according to the comparator in use.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions succeed
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo(parse("2020-01-01T00:00:00Z"));
   * // even though they refer to the same instant, succeeds as the OffsetDateTime comparator checks the offsets
   * assertThat(parse("2000-01-01T00:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isNotEqualTo(parse("2000-01-01T02:00:00+02:00"));
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo(parse("2000-01-01T00:00:00Z"));
   * // fails because the default comparator only checks the instant and they refer to the same
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo(parse("2000-01-01T02:00:00+02:00"));</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is equal to the given one according to
   *                        the comparator in use.
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
   * Same assertion as {@link #isNotEqualTo(Object)} (where Object is expected to be {@link java.time.OffsetDateTime})
   * but here you
   * pass {@link java.time.OffsetDateTime} String representation that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME"
   * >ISO OffsetDateTime format</a> to allow calling {@link java.time.OffsetDateTime#parse(CharSequence)} method.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertions succeed
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo("2020-01-01T00:00:00Z");
   * // even though they refer to the same instant, succeeds as the OffsetDateTime comparator checks the offsets
   * assertThat(parse("2000-01-01T00:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isNotEqualTo("2000-01-01T02:00:00+02:00");
   *
   * // assertions fail
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo("2000-01-01T00:00:00Z");
   * // fails because the default comparator only checks the instant and they refer to the same
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotEqualTo("2000-01-01T02:00:00+02:00");</code></pre>
   *
   * @param dateTimeAsString String representing a {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is equal to the {@link java.time.OffsetDateTime} built
   *           from given String.
   * @throws IllegalArgumentException if given String is null or can't be converted to a
   *           {@link java.time.OffsetDateTime}.
   */
  public SELF isNotEqualTo(String dateTimeAsString) {
    assertOffsetDateTimeAsStringParameterIsNotNull(dateTimeAsString);
    return isNotEqualTo(parse(dateTimeAsString));
  }

  /**
   * Same assertion as {@link #isIn(Object...)} (where Objects are expected to be {@link java.time.OffsetDateTime}) but
   * here you
   * pass {@link java.time.OffsetDateTime} String representations that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME"
   * >ISO OffsetDateTime format</a> to allow calling {@link java.time.OffsetDateTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String based representation of OffsetDateTime
   * assertThat(parse("2000-01-01T00:00:00Z")).isIn("1999-12-31T00:00:00Z",
   *                                                "2000-01-01T00:00:00Z");</code></pre>
   *
   * @param dateTimesAsString String array representing {@link java.time.OffsetDateTime}s.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not in the {@link java.time.OffsetDateTime}s built
   *           from given Strings.
   * @throws IllegalArgumentException if given String is null or can't be converted to a
   *           {@link java.time.OffsetDateTime}.
   */
  public SELF isIn(String... dateTimesAsString) {
    checkIsNotNullAndNotEmpty(dateTimesAsString);
    return isIn(convertToOffsetDateTimeArray(dateTimesAsString));
  }

  /**
   * Same assertion as {@link #isNotIn(Object...)} (where Objects are expected to be {@link java.time.OffsetDateTime})
   * but here you
   * pass {@link java.time.OffsetDateTime} String representations that must follow <a href=
   * "http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME"
   * >ISO OffsetDateTime format</a> to allow calling {@link java.time.OffsetDateTime#parse(CharSequence)} method.
   * <p>
   * Example :
   * <pre><code class='java'> // use String based representation of OffsetDateTime
   * assertThat(parse("2000-01-01T00:00:00Z")).isNotIn("1999-12-31T00:00:00Z",
   *                                                   "2000-01-02T00:00:00Z");</code></pre>
   *
   * @param dateTimesAsString Array of String representing a {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is in the {@link java.time.OffsetDateTime}s built from
   *           given Strings.
   * @throws IllegalArgumentException if given String is null or can't be converted to a
   *           {@link java.time.OffsetDateTime}.
   */
  public SELF isNotIn(String... dateTimesAsString) {
    checkIsNotNullAndNotEmpty(dateTimesAsString);
    return isNotIn(convertToOffsetDateTimeArray(dateTimesAsString));
  }

  /**
   * Verifies that actual and given {@code OffsetDateTime} have same year, month, day, hour, minute and second fields,
   * (nanosecond fields are ignored in comparison).
   * <p>
   * Assertion can fail with OffsetDateTimes in same chronological nanosecond time window, e.g :
   * <p>
   * 2000-01-01T00:00:<b>01.000000000</b>+01:00 and 2000-01-01T00:00:<b>00.999999999</b>+01:00.
   * <p>
   * Assertion fails as second fields differ even if time difference is only 1ns.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * OffsetDateTime OffsetDateTime1 = OffsetDateTime.of(2000, 1, 1, 0, 0, 1, 0, ZoneOffset.UTC);
   * OffsetDateTime OffsetDateTime2 = OffsetDateTime.of(2000, 1, 1, 0, 0, 1, 456, ZoneOffset.UTC);
   * assertThat(OffsetDateTime1).isEqualToIgnoringNanos(OffsetDateTime2);</code></pre>
   *
   * <pre><code class='java'> // failing assertions (even if time difference is only 1ns)
   * OffsetDateTime OffsetDateTimeA = OffsetDateTime.of(2000, 1, 1, 0, 0, 1, 0, ZoneOffset.UTC);
   * OffsetDateTime OffsetDateTimeB = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 999999999, ZoneOffset.UTC);
   * assertThat(OffsetDateTimeA).isEqualToIgnoringNanos(OffsetDateTimeB);</code></pre>
   *
   * @param other the given {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is are not equal with nanoseconds ignored.
   * @throws IllegalArgumentException if other {@code OffsetDateTime} is {@code null}.
   * @deprecated Use {@link #isCloseTo(OffsetDateTime, TemporalOffset)} instead, although not exactly the same semantics,
   * this is the right way to compare with a given precision.
   */
  @Deprecated
  public SELF isEqualToIgnoringNanos(OffsetDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertOffsetDateTimeParameterIsNotNull(other);
    if (!areEqualIgnoringNanos(actual, other)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringNanos(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that actual and given {@code OffsetDateTime} have same year, month, day, hour, minute, second and
   * nanosecond fields (timezone fields are ignored in comparison).
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * OffsetDateTime OffsetDateTime1 = OffsetDateTime.of(2000, 1, 1, 0, 0, 1, 0, ZoneOffset.UTC);
   * OffsetDateTime OffsetDateTime2 = OffsetDateTime.of(2000, 1, 1, 0, 0, 1, 0, ZoneOffset.MAX);
   * assertThat(OffsetDateTime1).isEqualToIgnoringTimezone(OffsetDateTime2);
   *
   * // failing assertions
   * OffsetDateTime OffsetDateTimeA = OffsetDateTime.of(2000, 1, 1, 0, 0, 1, 0, ZoneOffset.UTC);
   * OffsetDateTime OffsetDateTimeB = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 999999999, ZoneOffset.UTC);
   * assertThat(OffsetDateTimeA).isEqualToIgnoringTimezone(OffsetDateTimeB);</code></pre>
   *
   * @param other the given {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is are not equal with timezone ignored.
   * @throws IllegalArgumentException if other {@code OffsetDateTime} is {@code null}.
   */
  public SELF isEqualToIgnoringTimezone(OffsetDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertOffsetDateTimeParameterIsNotNull(other);
    if (!areEqualIgnoringTimezone(actual, other)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringTimezone(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that actual and given {@link java.time.OffsetDateTime} have same year, month, day, hour and minute fields
   * (second and
   * nanosecond fields are ignored in comparison).
   * <p>
   * Assertion can fail with OffsetDateTimes in same chronological second time window, e.g :
   * <p>
   * 2000-01-01T00:<b>01:00</b>.000+01:00 and 2000-01-01T00:<b>00:59</b>.000+01:00.
   * <p>
   * Assertion fails as minute fields differ even if time difference is only 1s.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * OffsetDateTime OffsetDateTime1 = OffsetDateTime.of(2000, 1, 1, 23, 50, 0, 0, ZoneOffset.UTC);
   * OffsetDateTime OffsetDateTime2 = OffsetDateTime.of(2000, 1, 1, 23, 50, 10, 456, ZoneOffset.UTC);
   * assertThat(OffsetDateTime1).isEqualToIgnoringSeconds(OffsetDateTime2);
   *
   * // failing assertions (even if time difference is only 1ms)
   * OffsetDateTime OffsetDateTimeA = OffsetDateTime.of(2000, 1, 1, 23, 50, 00, 000, ZoneOffset.UTC);
   * OffsetDateTime OffsetDateTimeB = OffsetDateTime.of(2000, 1, 1, 23, 49, 59, 999, ZoneOffset.UTC);
   * assertThat(OffsetDateTimeA).isEqualToIgnoringSeconds(OffsetDateTimeB);</code></pre>
   *
   * @param other the given {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is are not equal with second and nanosecond fields ignored.
   * @throws IllegalArgumentException if other {@code OffsetDateTime} is {@code null}.
   * @deprecated Use {@link #isCloseTo(OffsetDateTime, TemporalOffset)} instead, although not exactly the same semantics,
   * this is the right way to compare with a given precision.
   */
  @Deprecated
  public SELF isEqualToIgnoringSeconds(OffsetDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertOffsetDateTimeParameterIsNotNull(other);
    if (!areEqualIgnoringSeconds(actual, other)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringSeconds(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that actual and given {@code OffsetDateTime} have same year, month, day and hour fields (minute, second
   * and
   * nanosecond fields are ignored in comparison).
   * <p>
   * Assertion can fail with OffsetDateTimes in same chronological second time window, e.g :
   * <p>
   * 2000-01-01T<b>01:00</b>:00.000+01:00 and 2000-01-01T<b>00:59:59</b>.000+01:00.
   * <p>
   * Time difference is only 1s but hour fields differ.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * OffsetDateTime offsetDateTime1 = OffsetDateTime.of(2000, 1, 1, 23, 50, 0, 0, ZoneOffset.UTC);
   * OffsetDateTime offsetDateTime2 = OffsetDateTime.of(2000, 1, 1, 23, 00, 2, 7, ZoneOffset.UTC);
   * assertThat(offsetDateTime1).isEqualToIgnoringMinutes(offsetDateTime2);
   *
   * // failing assertions (even if time difference is only 1ms)
   * OffsetDateTime OffsetDateTimeA = OffsetDateTime.of(2000, 1, 1, 01, 00, 00, 000, ZoneOffset.UTC);
   * OffsetDateTime OffsetDateTimeB = OffsetDateTime.of(2000, 1, 1, 00, 59, 59, 999, ZoneOffset.UTC);
   * assertThat(OffsetDateTimeA).isEqualToIgnoringMinutes(OffsetDateTimeB);</code></pre>
   *
   * @param other the given {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is are not equal ignoring minute, second and nanosecond fields.
   * @throws IllegalArgumentException if other {@code OffsetDateTime} is {@code null}.
   * @deprecated Use {@link #isCloseTo(OffsetDateTime, TemporalOffset)} instead, although not exactly the same semantics,
   * this is the right way to compare with a given precision.
   */
  @Deprecated
  public SELF isEqualToIgnoringMinutes(OffsetDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertOffsetDateTimeParameterIsNotNull(other);
    if (!areEqualIgnoringMinutes(actual, other)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringMinutes(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that actual and given {@code OffsetDateTime} have same year, month and day fields (hour, minute, second
   * and nanosecond fields are ignored in comparison).
   * <p>
   * Assertion can fail with OffsetDateTimes in same chronological minute time window, e.g :
   * <p>
   * 2000-01-<b>01T23:59</b>:00.000+01:00 and 2000-01-02T<b>00:00</b>:00.000+01:00.
   * <p>
   * Time difference is only 1min but day fields differ.
   * <p>
   * Code example :
   * <pre><code class='java'> // successful assertions
   * OffsetDateTime OffsetDateTime1 = OffsetDateTime.of(2000, 1, 1, 23, 59, 59, 999, ZoneOffset.UTC);
   * OffsetDateTime OffsetDateTime2 = OffsetDateTime.of(2000, 1, 1, 00, 00, 00, 000, ZoneOffset.UTC);
   * assertThat(OffsetDateTime1).isEqualToIgnoringHours(OffsetDateTime2);
   *
   * // failing assertions (even if time difference is only 1ms)
   * OffsetDateTime OffsetDateTimeA = OffsetDateTime.of(2000, 1, 2, 00, 00, 00, 000, ZoneOffset.UTC);
   * OffsetDateTime OffsetDateTimeB = OffsetDateTime.of(2000, 1, 1, 23, 59, 59, 999, ZoneOffset.UTC);
   * assertThat(OffsetDateTimeA).isEqualToIgnoringHours(OffsetDateTimeB);</code></pre>
   *
   * @param other the given {@link java.time.OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is are not equal with hour, minute, second and nanosecond ignored.
   * @throws IllegalArgumentException if other {@code OffsetDateTime} is {@code null}.
   * @deprecated Use {@link #isCloseTo(OffsetDateTime, TemporalOffset)} instead, although not exactly the same semantics,
   * this is the right way to compare with a given precision.
   */
  @Deprecated
  public SELF isEqualToIgnoringHours(OffsetDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertOffsetDateTimeParameterIsNotNull(other);
    if (!haveSameYearMonthAndDayOfMonth(actual, other)) {
      throw Failures.instance().failure(info, shouldBeEqualIgnoringHours(actual, other));
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code OffsetDateTime} is strictly in the past.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(OffsetDateTime.now().minusMinutes(1)).isInThePast();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not in the past.
   *
   * @since 3.25.0
   */
  public SELF isInThePast() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isBefore(OffsetDateTime.now(actual.getOffset())))
      throw Failures.instance().failure(info, shouldBeInThePast(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code OffsetDateTime} is strictly in the future.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(OffsetDateTime.now().plusMinutes(1)).isInTheFuture();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not in the future.
   *
   * @since 3.25.0
   */
  public SELF isInTheFuture() {
    Objects.instance().assertNotNull(info, actual);
    if (!actual.isAfter(OffsetDateTime.now(actual.getOffset())))
      throw Failures.instance().failure(info, shouldBeInTheFuture(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link OffsetDateTime} is in the [start, end] period (start and end included) according to the comparator in use.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example:
   * <pre><code class='java'> OffsetDateTime offsetDateTime = OffsetDateTime.now();
   *
   * // assertions succeed:
   * assertThat(offsetDateTime).isBetween(offsetDateTime.minusSeconds(1), offsetDateTime.plusSeconds(1))
   *                           .isBetween(offsetDateTime, offsetDateTime.plusSeconds(1))
   *                           .isBetween(offsetDateTime.minusSeconds(1), offsetDateTime)
   *                           .isBetween(offsetDateTime, offsetDateTime);
   * // succeeds with default comparator which compares the point in time
   * assertThat(parse("2010-01-01T00:00:00Z")).isBetween(parse("2010-01-01T01:00:00+01:00"),
   *                                                     parse("2010-01-01T01:00:00+01:00"));
   *
   * // assertions fail:
   * assertThat(offsetDateTime).isBetween(offsetDateTime.minusSeconds(10), offsetDateTime.minusSeconds(1));
   * assertThat(offsetDateTime).isBetween(offsetDateTime.plusSeconds(1), offsetDateTime.plusSeconds(10));
   *
   * // succeeds with default comparator
   * assertThat(parse("2010-01-01T00:00:00Z")).isBetween(parse("2010-01-01T01:00:00+01:00"),
   *                                                     parse("2010-01-01T01:00:00+01:00"));
   * // fails with a comparator which checks the offset, too
   * assertThat(parse("2010-01-01T00:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isBetween(parse("2010-01-01T01:00:00+01:00"),
   *                                                     parse("2010-01-01T01:00:00+01:00"));</code></pre>
   *
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not in [start, end] period according to the comparator in use.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   *
   * @since 3.7.1
   */
  public SELF isBetween(OffsetDateTime startInclusive, OffsetDateTime endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return myself;
  }

  /**
   * Same assertion as {@link #isBetween(OffsetDateTime, OffsetDateTime)} but here you pass {@link OffsetDateTime} String representations
   * which must follow <a href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME">ISO OffsetDateTime format</a>
   * to allow calling {@link OffsetDateTime#parse(CharSequence)} method.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example:
   * <pre><code class='java'> OffsetDateTime firstOfJanuary2000 = OffsetDateTime.parse("2000-01-01T00:00:00Z");
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
   * @throws AssertionError if the actual value is not in [start, end] period.
   * @throws DateTimeParseException if any of the given String can't be converted to a {@link OffsetDateTime}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   *
   * @since 3.7.1
   */
  public SELF isBetween(String startInclusive, String endInclusive) {
    return isBetween(parse(startInclusive), parse(endInclusive));
  }

  /**
   * Verifies that the actual {@link OffsetDateTime} is in the ]start, end[ period (start and end excluded) according to
   * the comparator in use.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example:
   * <pre><code class='java'> OffsetDateTime offsetDateTime = OffsetDateTime.now();
   *
   * // assertions succeed:
   * assertThat(offsetDateTime).isStrictlyBetween(offsetDateTime.minusSeconds(1), offsetDateTime.plusSeconds(1));
   * // succeeds with a different comparator even though the end value refers to the same instant as the actual
   * assertThat(parse("2010-01-01T12:00:00Z")).usingComparator(OffsetDateTime::compareTo)
   *                                          .isStrictlyBetween(parse("2010-01-01T12:59:59+01:00"),
   *                                                             parse("2010-01-01T13:00:00+01:00"));
   *
   * // assertions fail:
   * assertThat(offsetDateTime).isStrictlyBetween(offsetDateTime.minusSeconds(10), offsetDateTime.minusSeconds(1));
   * assertThat(offsetDateTime).isStrictlyBetween(offsetDateTime.plusSeconds(1), offsetDateTime.plusSeconds(10));
   * assertThat(offsetDateTime).isStrictlyBetween(offsetDateTime, offsetDateTime.plusSeconds(1));
   * assertThat(offsetDateTime).isStrictlyBetween(offsetDateTime.minusSeconds(1), offsetDateTime);
   *
   * // fails with default comparator since the end value refers to the same instant as the actual
   * assertThat(parse("2010-01-01T12:00:00Z")).isStrictlyBetween(parse("2010-01-01T12:59:59+01:00"),
   *                                                             parse("2010-01-01T13:00:00+01:00"));</code></pre>
   *
   * @param startExclusive the start value (exclusive), expected not to be null.
   * @param endExclusive the end value (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ period according to the comparator in use.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   *
   * @since 3.7.1
   */
  public SELF isStrictlyBetween(OffsetDateTime startExclusive, OffsetDateTime endExclusive) {
    comparables.assertIsBetween(info, actual, startExclusive, endExclusive, false, false);
    return myself;
  }

  /**
   * Same assertion as {@link #isStrictlyBetween(OffsetDateTime, OffsetDateTime)} but here you pass {@link OffsetDateTime} String representations
   * which must follow <a href="http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME">ISO OffsetDateTime format</a>
   * to allow calling {@link OffsetDateTime#parse(CharSequence)} method.
   * <p>
   * <b>Breaking change</b> since 3.15.0 The default comparator uses {@link OffsetDateTime#timeLineOrder()}
   * which only compares the underlying instant and ignores different timezones / offsets / chronologies.<br>
   * This behaviour can be overridden by {@link AbstractOffsetDateTimeAssert#usingComparator(Comparator)}.
   * <p>
   * Example:
   * <pre><code class='java'> OffsetDateTime firstOfJanuary2000 = OffsetDateTime.parse("2000-01-01T00:00:00Z");
   *
   * // assertion succeeds:
   * assertThat(firstOfJanuary2000).isStrictlyBetween("1999-12-31T23:59:59Z", "2000-01-01T00:00:01Z")
   *                               // succeeds with a different comparator even though the end value refers to the same instant as the actual
   *                               .usingComparator(OffsetDateTime::compareTo)
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
   * @throws AssertionError if the actual value is not in ]start, end[ period.
   * @throws DateTimeParseException if any of the given String can't be converted to a {@link OffsetDateTime}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
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
    OffsetDateTimeByInstantComparator defaultComparator = OffsetDateTimeByInstantComparator.getInstance();
    return new Comparables(new ComparatorBasedComparisonStrategy(defaultComparator, defaultComparator.description()));
  }

  /**
   * Verifies that actual and given {@code OffsetDateTime} are at the same {@link java.time.Instant}.
   * <p>
   * Example:
   * <pre><code class='java'> OffsetDateTime offsetDateTime1 = OffsetDateTime.of(2000, 12, 12, 3, 0, 0, 0, ZoneOffset.ofHours(3));
   * OffsetDateTime offsetDateTime2 = OffsetDateTime.of(2000, 12, 12, 0, 0, 0, 0, ZoneOffset.ofHours(0));
   * // assertion succeeds
   * assertThat(offsetDateTime1).isAtSameInstantAs(offsetDateTime2);
   *
   * offsetDateTime2 = OffsetDateTime.of(2000, 12, 12, 2, 0, 0, 0, ZoneOffset.ofHours(0));
   * // assertion fails
   * assertThat(offsetDateTime1).isAtSameInstantAs(offsetDateTime2);</code></pre>
   *
   * @param other the given {@link OffsetDateTime}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code OffsetDateTime} is {@code null}.
   * @throws AssertionError if the actual {@code OffsetDateTime} is not at the same {@code Instant} as the other.
   * @throws IllegalArgumentException if other {@code OffsetDateTime} is {@code null}.
   */
  public SELF isAtSameInstantAs(OffsetDateTime other) {
    Objects.instance().assertNotNull(info, actual);
    assertOffsetDateTimeParameterIsNotNull(other);
    if (!actual.toInstant().equals(other.toInstant()))
      throw Failures.instance().failure(info, shouldBeAtSameInstant(actual, other));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected OffsetDateTime parse(String offsetDateTimeAsString) {
    return OffsetDateTime.parse(offsetDateTimeAsString);
  }

  /**
   * Returns true if both OffsetDateTime are in the same year, month and day of month, hour, minute and second, false
   * otherwise.
   *
   * @param actual the actual OffsetDateTime. expected not be null
   * @param other the other OffsetDateTime. expected not be null
   * @return true if both OffsetDateTime are in the same year, month and day of month, hour, minute and second, false
   *         otherwise.
   */
  private static boolean areEqualIgnoringNanos(OffsetDateTime actual, OffsetDateTime other) {
    return areEqualIgnoringSeconds(actual, other) && actual.getSecond() == other.getSecond();
  }

  /**
   * Returns true if both OffsetDateTime are in the same year, month, day of month, hour and minute, false otherwise.
   *
   * @param actual the actual OffsetDateTime. expected not be null
   * @param other the other OffsetDateTime. expected not be null
   * @return true if both OffsetDateTime are in the same year, month, day of month, hour and minute, false otherwise.
   */
  private static boolean areEqualIgnoringSeconds(OffsetDateTime actual, OffsetDateTime other) {
    return areEqualIgnoringMinutes(actual, other) && actual.getMinute() == other.getMinute();
  }

  /**
   * Returns true if both OffsetDateTime are in the same year, month, day of month and hour, false otherwise.
   *
   * @param actual the actual OffsetDateTime. expected not be null
   * @param other the other OffsetDateTime. expected not be null
   * @return true if both OffsetDateTime are in the same year, month, day of month and hour, false otherwise.
   */
  private static boolean areEqualIgnoringMinutes(OffsetDateTime actual, OffsetDateTime other) {
    return haveSameYearMonthAndDayOfMonth(actual, other) && actual.getHour() == other.getHour();
  }

  /**
   * Returns true if both OffsetDateTime are in the same year, month and day of month, false otherwise.
   *
   * @param actual the actual OffsetDateTime. expected not be null
   * @param other the other OffsetDateTime. expected not be null
   * @return true if both OffsetDateTime are in the same year, month and day of month, false otherwise
   */
  private static boolean haveSameYearMonthAndDayOfMonth(OffsetDateTime actual, OffsetDateTime other) {
    return haveSameYearAndMonth(actual, other) && actual.getDayOfMonth() == other.getDayOfMonth();
  }

  /**
   * Returns true if both OffsetDateTime are in the same year and month, false otherwise.
   *
   * @param actual the actual OffsetDateTime. expected not be null
   * @param other the other OffsetDateTime. expected not be null
   * @return true if both OffsetDateTime are in the same year and month, false otherwise
   */
  private static boolean haveSameYearAndMonth(OffsetDateTime actual, OffsetDateTime other) {
    return haveSameYear(actual, other) && actual.getMonth() == other.getMonth();
  }

  /**
   * Returns true if both OffsetDateTime are in the same year, false otherwise.
   *
   * @param actual the actual OffsetDateTime. expected not be null
   * @param other the other OffsetDateTime. expected not be null
   * @return true if both OffsetDateTime are in the same year, false otherwise
   */
  private static boolean haveSameYear(OffsetDateTime actual, OffsetDateTime other) {
    return actual.getYear() == other.getYear();
  }

  /**
   * Returns true if both OffsetDateTime are in the same hour, minute, second and nanosecond false otherwise.
   *
   * @param actual the actual OffsetDateTime. expected not be null
   * @param other the other OffsetDateTime. expected not be null
   * @return true if both OffsetDateTime are in the same hour, minute, second and nanosecond false otherwise.
   */
  private static boolean areEqualIgnoringTimezone(OffsetDateTime actual, OffsetDateTime other) {
    return areEqualIgnoringNanos(actual, other) && haveSameNano(actual, other);
  }

  /**
   * Returns true if both OffsetDateTime are in the same nanosecond, false otherwise.
   *
   * @param actual the actual OffsetDateTime. expected not be null
   * @param other the other OffsetDateTime. expected not be null
   * @return true if both OffsetDateTime are in the same year, false otherwise
   */
  private static boolean haveSameNano(OffsetDateTime actual, OffsetDateTime other) {
    return actual.getNano() == other.getNano();
  }

  private static Object[] convertToOffsetDateTimeArray(String... dateTimesAsString) {
    OffsetDateTime[] dates = new OffsetDateTime[dateTimesAsString.length];
    for (int i = 0; i < dateTimesAsString.length; i++) {
      dates[i] = OffsetDateTime.parse(dateTimesAsString[i]);
    }
    return dates;
  }

  private void checkIsNotNullAndNotEmpty(Object[] values) {
    checkArgument(values != null, "The given OffsetDateTime array should not be null");
    checkArgument(values.length > 0, "The given OffsetDateTime array should not be empty");
  }

  /**
   * Check that the {@link java.time.OffsetDateTime} string representation to compare actual
   * {@link java.time.OffsetDateTime} to is not null,
   * otherwise throws a {@link IllegalArgumentException} with an explicit message
   *
   * @param offsetDateTimeAsString String representing the {@link java.time.OffsetDateTime} to compare actual with
   * @throws IllegalArgumentException with an explicit message if the given {@link String} is null
   */
  private static void assertOffsetDateTimeAsStringParameterIsNotNull(String offsetDateTimeAsString) {
    checkArgument(offsetDateTimeAsString != null,
                  "The String representing the OffsetDateTime to compare actual with should not be null");
  }

  /**
   * Check that the {@link java.time.OffsetDateTime} to compare actual {@link java.time.OffsetDateTime} to is not null,
   * in that case throws a {@link IllegalArgumentException} with an explicit message
   *
   * @param other the {@link java.time.OffsetDateTime} to check
   * @throws IllegalArgumentException with an explicit message if the given {@link java.time.OffsetDateTime} is null
   */
  private static void assertOffsetDateTimeParameterIsNotNull(OffsetDateTime other) {
    checkArgument(other != null, "The OffsetDateTime to compare actual with should not be null");
  }

}
