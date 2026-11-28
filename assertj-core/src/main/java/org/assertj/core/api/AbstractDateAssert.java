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

import static java.lang.String.format;
import static java.time.ZoneId.systemDefault;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.error.ShouldBeAfter.shouldBeAfter;
import static org.assertj.core.error.ShouldBeAfterOrEqualTo.shouldBeAfterOrEqualTo;
import static org.assertj.core.error.ShouldBeAfterYear.shouldBeAfterYear;
import static org.assertj.core.error.ShouldBeBefore.shouldBeBefore;
import static org.assertj.core.error.ShouldBeBeforeOrEqualTo.shouldBeBeforeOrEqualTo;
import static org.assertj.core.error.ShouldBeBeforeYear.shouldBeBeforeYear;
import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.error.ShouldBeCloseTo.shouldBeCloseTo;
import static org.assertj.core.error.ShouldBeInSameDay.shouldBeInSameDay;
import static org.assertj.core.error.ShouldBeInSameHourWindow.shouldBeInSameHourWindow;
import static org.assertj.core.error.ShouldBeInSameMinuteWindow.shouldBeInSameMinuteWindow;
import static org.assertj.core.error.ShouldBeInSameMonth.shouldBeInSameMonth;
import static org.assertj.core.error.ShouldBeInSameSecondWindow.shouldBeInSameSecondWindow;
import static org.assertj.core.error.ShouldBeInSameYear.shouldBeInSameYear;
import static org.assertj.core.error.ShouldBeInTheFuture.shouldBeInTheFuture;
import static org.assertj.core.error.ShouldBeInThePast.shouldBeInThePast;
import static org.assertj.core.error.ShouldBeNotInTheFuture.shouldBeNotInTheFuture;
import static org.assertj.core.error.ShouldBeNotInThePast.shouldBeNotInThePast;
import static org.assertj.core.error.ShouldBeToday.shouldBeToday;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.error.ShouldHaveSameTime.shouldHaveSameTime;
import static org.assertj.core.error.ShouldHaveTime.shouldHaveTime;
import static org.assertj.core.error.ShouldNotBeBetween.shouldNotBeBetween;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.DateUtil.dayOfMonthOf;
import static org.assertj.core.util.DateUtil.dayOfWeekOf;
import static org.assertj.core.util.DateUtil.hourOfDayOf;
import static org.assertj.core.util.DateUtil.millisecondOf;
import static org.assertj.core.util.DateUtil.minuteOf;
import static org.assertj.core.util.DateUtil.monthOf;
import static org.assertj.core.util.DateUtil.now;
import static org.assertj.core.util.DateUtil.secondOf;
import static org.assertj.core.util.DateUtil.timeDifference;
import static org.assertj.core.util.DateUtil.truncateTime;
import static org.assertj.core.util.DateUtil.yearOf;
import static org.assertj.core.util.Lists.list;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.error.AssertionErrorCreator;

/**
 * Base class for all implementations of assertions for {@link Date}s.
 * <p>
 * Note that assertions with date parameter come with two flavors, one is obviously a {@link Date} and the other is a
 * {@link String} representing a Date.<br>
 * For the latter, the default format follows ISO 8901: "yyyy-MM-dd", user can override it with a custom format by
 * calling {@link #withDateFormat(DateFormat)}.<br>
 * The user custom format will then be used for all next Date assertions (i.e. not limited to the current assertion) in
 * the test suite.<br>
 * To turn back to default format, simply call {@link #withDefaultDateFormatsOnly()}.
 *
 * @param <SELF> the "self" type of this assertion class. Please read "<a href="https://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>" for
 *          more details.
 * @author Tomasz Nurkiewicz (thanks for giving assertions idea)
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author William Delanoue
 * @author Michal Kordas
 * @author Eddú Meléndez
 */
public abstract class AbstractDateAssert<SELF extends AbstractDateAssert<SELF>> extends AbstractAssertWithComparator<SELF, Date> {

  private static final String DATE_FORMAT_PATTERN_SHOULD_NOT_BE_NULL = "Given date format pattern should not be null";
  private static final String DATE_FORMAT_SHOULD_NOT_BE_NULL = "Given date format should not be null";

  private static final List<Function<String, Date>> DEFAULT_DATE_PARSERS = list(s -> Date.from(OffsetDateTime.parse(s)
                                                                                                             .toInstant()),
                                                                                s -> Date.from(LocalDateTime.parse(s)
                                                                                                            .atZone(systemDefault())
                                                                                                            .toInstant()),
                                                                                s -> Date.from(LocalDate.parse(s)
                                                                                                        .atStartOfDay(systemDefault())
                                                                                                        .toInstant()),
                                                                                s -> new Date(Timestamp.valueOf(s).getTime()));

  static final List<String> DEFAULT_DATE_PARSERS_REPRESENTATION = list("yyyy-MM-dd'T'HH:mm:ss.SSSX",
                                                                       "yyyy-MM-dd'T'HH:mm:ss.SSS",
                                                                       "yyyy-MM-dd HH:mm:ss.SSS",
                                                                       "yyyy-MM-dd'T'HH:mm:ssX",
                                                                       "yyyy-MM-dd'T'HH:mm:ss",
                                                                       "yyyy-MM-dd HH:mm:ss",
                                                                       "yyyy-MM-dd");

  /**
   * Used in String based Date assertions - like {@link #isAfter(String)} - to convert input date represented as string
   * to Date.<br>
   * It keeps the insertion order, so the first format added will be the first format used.
   */
  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  static ThreadLocal<LinkedHashSet<DateFormat>> userDateFormats = ThreadLocal.withInitial(LinkedHashSet::new);

  /**
   * Creates a new {@link Date} assertion.
   *
   * @param actual the actual date to verify
   * @param selfType the type of the concrete assertion
   */
  protected AbstractDateAssert(Date actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Same assertion as {@link AbstractAssert#isEqualTo(Object) isEqualTo(Date date)} but given date is represented as
   * a {@code String} either with one of the supported default date formats or a user custom date format set with method
   * {@link #withDateFormat(DateFormat)}.
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isEqualTo("2002-12-18");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isEqualTo("2002-12-19");</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are not equal.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isEqualTo(String dateAsString) {
    return isEqualTo(parse(dateAsString));
  }

  /**
   * Calls {@link AbstractAssert#isEqualTo(Object) isEqualTo(Date date)} after converting the given {@code Instant} to a
   * {@code Date}.
   * <p>
   * Example:
   * <pre><code class='java'> // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isEqualTo(Instant.parse("2002-12-18T00:00:00.00Z"));</code></pre>
   *
   * @param instant the given {@code Instant} to compare to actual.
   * @return this assertion object.
   * @throws NullPointerException if given {@code Instant} is {@code null}.
   * @throws AssertionError if actual {@code Date} and given {@link Instant} are not equal (after converting instant to a Date).
   */
  public SELF isEqualTo(Instant instant) {
    return isEqualTo(dateFrom(instant));
  }

  /**
   * Same assertion as {@link AbstractAssert#isNotEqualTo(Object) isNotEqualTo(Date date)} but given date is
   * represented as String either with one of the supported default date formats or a user custom date format (set with
   * method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isNotEqualTo("2002-12-19");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isNotEqualTo("2002-12-18")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are equal.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isNotEqualTo(String dateAsString) {
    return isNotEqualTo(parse(dateAsString));
  }

  /**
   * Same assertion as {@link AbstractAssert#isNotEqualTo(Object) isNotEqualTo(Date date)} but given date is
   * represented as an {@code java.time.Instant}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isNotEqualTo(Instant.now());</code></pre>
   *
   * @param instant the given {@code Instant}.
   * @return this assertion object.
   * @throws AssertionError if actual {@code Date} and given {@code Instant} are equal.
   * @since 3.19.0
   */
  public SELF isNotEqualTo(Instant instant) {
    return isNotEqualTo(dateFrom(instant));
  }

  /**
   * Same assertion as {@link Assert#isIn(Object...)}but given dates are represented as String either with one of the
   * supported default date formats or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isIn("2002-12-17", "2002-12-18", "2002-12-19");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isIn("2002-12-17", "2002-12-19", "2002-12-20")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param datesAsString the given Dates represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual is not in given Dates represented as String.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isIn(String... datesAsString) {
    Date[] dates = toDateArray(datesAsString, this::parse);
    return isIn((Object[]) dates);
  }

  /**
   * Same assertion as {@link Assert#isIn(Object...) }but given dates are represented as an {@code java.time.Instant}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will fail
   * // theTwoTowers release date : 2002-12-18
   * Instant now = Instant.now()
   * assertThat(theTwoTowers.getReleaseDate()).isIn(now, now.plusSeconds(5), now.minusSeconds(5));</code></pre>
   *
   * @param instants the given dates represented as {@code Instant}.
   * @return this assertion object.
   * @throws AssertionError if actual is not in given dates represented as {@code Instant}.
   */
  public SELF isIn(Instant... instants) {
    Date[] dates = toDateArray(instants, Date::from);
    return isIn((Object[]) dates);
  }

  /**
   * Same assertion as {@link Assert#isIn(Iterable)} but given dates are represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format take precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isInWithStringDateCollection(asList("2002-12-17", "2002-12-18", "2002-12-19"));
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isInWithStringDateCollection(asList("2002-12-17", "2002-12-19", "2002-12-20"))</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   * <p>
   * Method signature could not be <code>isIn(Collection&lt;String&gt;)</code> because it would be same signature as
   * <code>isIn(Collection&lt;Date&gt;)</code> since java collection type are erased at runtime.
   *
   * @param datesAsString the given Dates represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual is not in given Dates represented as String.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isInWithStringDateCollection(Collection<String> datesAsString) {
    return isIn(datesAsString.stream().map(this::parse).collect(toList()));
  }

  /**
   * Same assertion as {@link Assert#isNotIn(Object...)} but given dates are represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format take precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isNotIn("2002-12-17", "2002-12-19");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isNotIn("2002-12-17", "2002-12-18")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param datesAsString the given Dates represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual is in given Dates represented as String.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isNotIn(String... datesAsString) {
    Date[] dates = toDateArray(datesAsString, this::parse);
    return isNotIn((Object[]) dates);
  }

  /**
   * Same assertion as {@link Assert#isNotIn(Object...)} but given dates are represented as {@code java.time.Instant}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * Instant now = Instant.now()
   * assertThat(theTwoTowers.getReleaseDate()).isIn(now, now.plusSeconds(5), now.minusSeconds(5));</code></pre>
   *
   * @param instants the given dates represented as {@code Instant}.
   * @return this assertion object.
   * @throws AssertionError if actual is not in given dates represented as {@code Instant}.
   * @since 3.19.0
   */
  public SELF isNotIn(Instant... instants) {
    Date[] dates = toDateArray(instants, Date::from);
    return isNotIn((Object[]) dates);
  }

  /**
   * Same assertion as {@link Assert#isNotIn(Iterable)} but given date is represented as String either with one of the
   * supported default date formats or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isNotInWithStringDateCollection(Arrays.asList("2002-12-17", "2002-12-19"));
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isNotInWithStringDateCollection(Arrays.asList("2002-12-17", "2002-12-18"))</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   * Method signature could not be <code>isNotIn(Collection&lt;String&gt;)</code> because it would be same signature as
   * <code>isNotIn(Collection&lt;Date&gt;)</code> since java collection type are erased at runtime.
   *
   * @param datesAsString the given Dates represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual is in given Dates represented as String.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isNotInWithStringDateCollection(Collection<String> datesAsString) {
    return isNotIn(datesAsString.stream().map(this::parse).collect(toList()));
  }

  /**
   * Verifies that the actual {@code Date} is <b>strictly</b> before the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBefore(theReturnOfTheKing.getReleaseDate());
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isBefore(theFellowshipOfTheRing.getReleaseDate());</code></pre>
   *
   * @param other the given Date.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly before the given one.
   */
  public SELF isBefore(Date other) {
    return executeAssertion(() -> assertIsBefore(other));
  }

  /**
   * Verifies that the actual {@code Date} is <b>strictly</b> before the given {@link Instant}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBefore(Instant.parse("2002-12-19T00:00:00.00Z"));
   *
   * // assertions fail
   * assertThat(theTwoTowers.getReleaseDate()).isBefore(Instant.parse("2002-12-17T00:00:00.00Z"));
   * assertThat(theTwoTowers.getReleaseDate()).isBefore(Instant.parse("2002-12-18T00:00:00.00Z"));</code></pre>
   *
   * @param other the given {@code Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Instant} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly before the given {@code Instant}.
   * @since 3.19.0
   */
  public SELF isBefore(Instant other) {
    return executeAssertion(() -> assertIsBefore(dateFrom(other)));
  }

  /**
   * Same assertion as {@link #isBefore(Date)} but given date is represented as String either with one of the
   * supported default date formats or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBefore("2002-12-19");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isBefore("2002-12-17");
   * assertThat(theTwoTowers.getReleaseDate()).isBefore("2002-12-18")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly before the given Date represented as
   *           String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isBefore(String dateAsString) {
    return isBefore(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is before or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> SimpleDateFormat dateFormat = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
   *
   * // assertions will pass
   * assertThat(dateFormat.parse(&quot;1990-12-01&quot;)).isBeforeOrEqualTo(dateFormat.parse(&quot;2000-12-01&quot;));
   * assertThat(dateFormat.parse(&quot;2000-12-01&quot;)).isBeforeOrEqualTo(dateFormat.parse(&quot;2000-12-01&quot;));
   *
   * // assertion will fail
   * assertThat(dateFormat.parse(&quot;2000-12-01&quot;)).isBeforeOrEqualTo(dateFormat.parse(&quot;1990-12-01&quot;));</code></pre>
   *
   * @param other the given Date.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not before or equals to the given one.
   */
  public SELF isBeforeOrEqualTo(Date other) {
    return executeAssertion(() -> assertIsBeforeOrEqualTo(other));
  }

  /**
   * Verifies that the actual {@code Date} is before or equal to the given {@link Instant}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeOrEqualTo(Instant.parse("2002-12-19T00:00:00.00Z"));
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeOrEqualTo(Instant.parse("2002-12-18T00:00:00.00Z"));
   *
   * // assertion fails
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeOrEqualTo(Instant.parse("2002-12-17T00:00:00.00Z"));</code></pre>
   *
   * @param other the given {@code Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Instant} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not before or equal to the given {@code Instant}.
   * @since 3.19.0
   */
  public SELF isBeforeOrEqualTo(Instant other) {
    return executeAssertion(() -> assertIsBeforeOrEqualTo(dateFrom(other)));
  }

  /**
   * Same assertion as {@link #isBeforeOrEqualTo(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format take precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeOrEqualTo("2002-12-19");
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeOrEqualTo("2002-12-18");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeOrEqualTo("2002-12-17")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not before or equals to the given Date represented as
   *           String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isBeforeOrEqualTo(String dateAsString) {
    return isBeforeOrEqualTo(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is <b>strictly</b> after the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isAfter(theFellowshipOfTheRing.getReleaseDate());
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isAfter(theReturnOfTheKing.getReleaseDate());</code></pre>
   *
   * @param other the given Date.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly after the given one.
   */
  public SELF isAfter(Date other) {
    return executeAssertion(() -> assertIsAfter(other));
  }

  /**
   * Verifies that the actual {@code Date} is <b>strictly</b> after the given {@link Instant}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isAfter(Instant.parse("2002-12-17T00:00:00.00Z"));
   *
   * // assertions fail
   * assertThat(theTwoTowers.getReleaseDate()).isAfter(Instant.parse("2002-12-18T00:00:00.00Z"));
   * assertThat(theTwoTowers.getReleaseDate()).isAfter(Instant.parse("2002-12-19T00:00:00.00Z"));</code></pre>
   *
   * @param other the given {@code Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Instant} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly after the given {@code Instant}.
   * @since 3.19.0
   */
  public SELF isAfter(Instant other) {
    return executeAssertion(() -> assertIsAfter(dateFrom(other)));
  }

  /**
   * Same assertion as {@link #isAfter(Date)} but given date is represented as String either with one of the
   * supported default date formats or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isAfter("2002-12-17");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isAfter("2002-12-18");
   * assertThat(theTwoTowers.getReleaseDate()).isAfter("2002-12-19")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly after the given Date represented as
   *           String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isAfter(String dateAsString) {
    return isAfter(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is after or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> SimpleDateFormat dateFormat = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
   *
   * // assertions will pass
   * assertThat(dateFormat.parse(&quot;2000-12-01&quot;)).isAfterOrEqualTo(dateFormat.parse(&quot;1990-12-01&quot;));
   * assertThat(dateFormat.parse(&quot;2000-12-01&quot;)).isAfterOrEqualTo(dateFormat.parse(&quot;2000-12-01&quot;));
   *
   * // assertion will fail
   * assertThat(dateFormat.parse(&quot;1990-12-01&quot;)).isAfterOrEqualTo(dateFormat.parse(&quot;2000-12-01&quot;));</code></pre>
   *
   * @param other the given Date.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not after or equals to the given one.
   */
  public SELF isAfterOrEqualTo(Date other) {
    return executeAssertion(() -> assertIsAfterOrEqualTo(other));
  }

  /**
   * Verifies that the actual {@code Date} is after or equal to the given {@link Instant}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).assertIsAfterOrEqualTo(Instant.parse("2002-12-17T00:00:00.00Z"))
   *                                          .assertIsAfterOrEqualTo(Instant.parse("2002-12-18T00:00:00.00Z"));
   * // assertion fails
   * assertThat(theTwoTowers.getReleaseDate()).assertIsAfterOrEqualTo(Instant.parse("2002-12-19T00:00:00.00Z"));</code></pre>
   *
   * @param other the given {@code Instant}.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Instant} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not after or equal to the given {@code Instant}.
   * @since 3.19.0
   */
  public SELF isAfterOrEqualTo(Instant other) {
    return executeAssertion(() -> assertIsAfterOrEqualTo(dateFrom(other)));
  }

  /**
   * Same assertion as {@link #isAfterOrEqualTo(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format take precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isAfterOrEqualTo("2002-12-17");
   * assertThat(theTwoTowers.getReleaseDate()).isAfterOrEqualTo("2002-12-18");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isAfterOrEqualTo("2002-12-19")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not after or equals to the given Date represented as
   *           String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isAfterOrEqualTo(String dateAsString) {
    return isAfterOrEqualTo(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is in [start, end[ period (start included, end excluded).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBetween(theFellowshipOfTheRing.getReleaseDate(), theReturnOfTheKing.getReleaseDate());
   *
   * // assertion will fail
   * assertThat(theFellowshipOfTheRing.getReleaseDate()).isBetween(theTwoTowers.getReleaseDate(), theReturnOfTheKing.getReleaseDate());</code></pre>
   *
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if start {@code Date} is {@code null}.
   * @throws NullPointerException if end {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in [start, end[ period.
   */
  public SELF isBetween(Date start, Date end) {
    return isBetween(start, end, true, false);
  }

  /**
   * Same assertion as {@link #isBetween(Date, Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format take precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBetween("2002-12-17", "2002-12-19");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isBetween("2002-12-15", "2002-12-17")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if start Date as String is {@code null}.
   * @throws NullPointerException if end Date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in [start, end[ period.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isBetween(String start, String end) {
    return isBetween(parse(start), parse(end));
  }

  /**
   * Same assertion as {@link #isBetween(Date, Date)} but given period is represented with {@link java.time.Instant}.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(new Date()).isBetween(Instant.now().minusSeconds(5), Instant.now().plusSeconds(5));</code></pre>
   *
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if start Instant as String is {@code null}.
   * @throws NullPointerException if end Instant as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in [start, end[ period.
   * @since 3.19.0
   */
  public SELF isBetween(Instant start, Instant end) {
    return isBetween(dateFrom(start), dateFrom(end));
  }

  /**
   * Verifies that the actual {@code Date} is in the given period defined by start and end dates.<br>
   * To include start
   * in the period set inclusiveStart parameter to <code>true</code>.<br>
   * To include end in the period set inclusiveEnd
   * parameter to <code>true</code>.<br>
   * <p>
   * Example:
   * <pre><code class='java'> SimpleDateFormat format = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
   * // assertions will pass
   * assertThat(format.parse(&quot;2000-01-01&quot;)).isBetween(format.parse(&quot;2000-01-01&quot;), format.parse(&quot;2100-12-01&quot;), true, true);
   * assertThat(format.parse(&quot;2000-01-01&quot;)).isBetween(format.parse(&quot;1900-01-01&quot;), format.parse(&quot;2000-01-01&quot;), true, true);
   * assertThat(format.parse(&quot;2000-01-01&quot;)).isBetween(format.parse(&quot;1900-01-01&quot;), format.parse(&quot;2100-01-01&quot;), false, false);
   *
   * // assertions will fail
   * assertThat(format.parse(&quot;2000-01-01&quot;)).isBetween(format.parse(&quot;2000-01-01&quot;), format.parse(&quot;2100-12-01&quot;), false, true);
   * assertThat(format.parse(&quot;2000-01-01&quot;)).isBetween(format.parse(&quot;1900-01-01&quot;), format.parse(&quot;2000-01-01&quot;), true, false);</code></pre>
   *
   * @param start the period start, expected not to be null.
   * @param end the period end, expected not to be null.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start {@code Date} is {@code null}.
   * @throws NullPointerException if end {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in (start, end) period.
   */
  public SELF isBetween(Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    return executeAssertion(() -> assertIsBetween(start, end, inclusiveStart, inclusiveEnd));
  }

  /**
   * Same assertion as {@link #isBetween(Date, Date, boolean, boolean)} but given date is represented as String either
   * with one of the supported default date formats or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBetween("2002-12-17", "2002-12-18", false, true);
   * assertThat(theTwoTowers.getReleaseDate()).isBetween("2002-12-18", "2002-12-19", true, false);
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isBetween("2002-12-17", "2002-12-18", false, false)</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param start the period start, expected not to be null.
   * @param end the period end, expected not to be null.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start Date as String is {@code null}.
   * @throws NullPointerException if end Date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in (start, end) period.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isBetween(String start, String end, boolean inclusiveStart, boolean inclusiveEnd) {
    return executeAssertion(() -> assertIsBetween(parse(start), parse(end), inclusiveStart, inclusiveEnd));
  }

  /**
   * Same assertion as {@link #isBetween(Date, Date, boolean, boolean)} but given period is represented with {@link java.time.Instant}.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(new Date()).isBetween(Instant.now().minusSeconds(5), Instant.now().plusSeconds(5), true, true);</code></pre>
   *
   * @param start the period start, expected not to be null.
   * @param end the period end, expected not to be null.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start Date as Instant is {@code null}.
   * @throws NullPointerException if end Date as Instant is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in (start, end) period.
   * @since 3.19.0
   */
  public SELF isBetween(Instant start, Instant end, boolean inclusiveStart, boolean inclusiveEnd) {
    return executeAssertion(() -> assertIsBetween(dateFrom(start), dateFrom(end), inclusiveStart,
                                                  inclusiveEnd));
  }

  /**
   * Verifies that the actual {@code Date} is not in the given period defined by start and end dates.<br>
   * To include start in the period set inclusiveStart parameter to <code>true</code>.<br>
   * To include end in the period set inclusiveEnd parameter to <code>true</code>.<br>
   * <p>
   * Example:
   * <pre><code class='java'> SimpleDateFormat format = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
   * // assertions will pass
   * assertThat(format.parse("2000-01-01")).isNotBetween(format.parse("2000-01-01"), format.parse("2100-12-01"), false, true);
   * assertThat(format.parse("2000-01-01")).isNotBetween(format.parse("1900-01-01"), format.parse("2000-01-01"), true, false);
   *
   * // assertions will fail
   * assertThat(format.parse("2000-01-01")).isNotBetween(format.parse("2000-01-01"), format.parse("2100-12-01"), true, true);
   * assertThat(format.parse("2000-01-01")).isNotBetween(format.parse("1900-01-01"), format.parse("2000-01-01"), true, true);
   * assertThat(format.parse("2000-01-01")).isNotBetween(format.parse("1900-01-01"), format.parse("2100-01-01"), false, false);</code></pre>
   *
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start {@code Date} is {@code null}.
   * @throws NullPointerException if end {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in (start, end) period.
   */
  public SELF isNotBetween(Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    return executeAssertion(() -> assertIsNotBetween(start, end, inclusiveStart, inclusiveEnd));
  }

  /**
   * Verifies that the actual {@code Date} is not in the given period defined by start and end dates.<br>
   * To include start in the period set inclusiveStart parameter to <code>true</code>.<br>
   * To include end in the period set inclusiveEnd parameter to <code>true</code>.<br>
   * <p>
   * Example:
   * <pre><code class='java'> SimpleDateFormat format = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
   * // assertions will pass
   * assertThat(format.parse("2000-01-01")).isNotBetween(format.parse("2000-01-01T00:00:00Z"), format.parse("2100-12-01T00:00:00Z"), false, true);
   * assertThat(format.parse("2000-01-01")).isNotBetween(format.parse("1900-01-01T00:00:00Z"), format.parse("2000-01-01T00:00:00Z"), true, false);
   *
   * // assertions will fail
   * assertThat(format.parse("2000-01-01")).isNotBetween(format.parse("2000-01-01T00:00:00Z"), format.parse("2100-12-01T00:00:00Z"), true, true);
   * assertThat(format.parse("2000-01-01")).isNotBetween(format.parse("1900-01-01T00:00:00Z"), format.parse("2000-01-01T00:00:00Z"), true, true);
   * assertThat(format.parse("2000-01-01")).isNotBetween(format.parse("1900-01-01T00:00:00Z"), format.parse("2100-01-01T00:00:00Z"), false, false);</code></pre>
   *
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start {@code Instant} is {@code null}.
   * @throws NullPointerException if end {@code Instant} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in (start, end) period.
   * @since 3.19.0
   */
  public SELF isNotBetween(Instant start, Instant end, boolean inclusiveStart, boolean inclusiveEnd) {
    return executeAssertion(() -> assertIsNotBetween(dateFrom(start), dateFrom(end), inclusiveStart,
                                                     inclusiveEnd));
  }

  /**
   * Same assertion as {@link #isNotBetween(Date, Date, boolean, boolean)} but given date is represented as String
   * either with one of the supported default date formats or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isNotBetween("2002-12-17", "2002-12-18", false, false);
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isNotBetween("2002-12-17", "2002-12-18", false, true);
   * assertThat(theTwoTowers.getReleaseDate()).isNotBetween("2002-12-18", "2002-12-19", true, false)</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start Date as String is {@code null}.
   * @throws NullPointerException if end Date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in (start, end) period.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isNotBetween(String start, String end, boolean inclusiveStart, boolean inclusiveEnd) {
    return isNotBetween(parse(start), parse(end), inclusiveStart, inclusiveEnd);
  }

  /**
   * Verifies that the actual {@code Date} is not in [start, end[ period
   * <p>
   * Example:
   * <pre><code class='java'> SimpleDateFormat format = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
   * // assertions will pass
   * assertThat(format.parse(&quot;1900-01-01&quot;)).isNotBetween(format.parse(&quot;2000-01-01&quot;), format.parse(&quot;2100-12-01&quot;));
   * assertThat(format.parse(&quot;2200-01-01&quot;)).isNotBetween(format.parse(&quot;2000-01-01&quot;), format.parse(&quot;2100-12-01&quot;));
   * assertThat(format.parse(&quot;2000-01-01&quot;)).isNotBetween(format.parse(&quot;1900-01-01&quot;), format.parse(&quot;2000-01-01&quot;));
   *
   * // assertions will fail
   * assertThat(format.parse(&quot;2001-12-24&quot;)).isNotBetween(format.parse(&quot;2000-01-01&quot;), format.parse(&quot;2100-01-01&quot;));
   * assertThat(format.parse(&quot;1900-01-01&quot;)).isNotBetween(format.parse(&quot;1900-01-01&quot;), format.parse(&quot;2000-01-01&quot;));</code></pre>
   *
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if start {@code Date} is {@code null}.
   * @throws NullPointerException if end {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is in [start, end[ period.
   */
  public SELF isNotBetween(Date start, Date end) {
    return isNotBetween(start, end, true, false);
  }

  /**
   * Verifies that the actual {@code Date} is not in [start, end[ period
   * <p>
   * Example:
   * <pre><code class='java'> SimpleDateFormat format = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
   * // assertions will pass
   * assertThat(format.parse(&quot;1900-01-01&quot;)).isNotBetween(Instant.parse(&quot;2000-01-01T00:00:00Z&quot;), Instant.parse(&quot;2100-12-01T00:00:00Z&quot;));
   * assertThat(format.parse(&quot;2200-01-01&quot;)).isNotBetween(Instant.parse(&quot;2000-01-01T00:00:00Z&quot;), Instant.parse(&quot;2100-12-01T00:00:00Z&quot;));
   * assertThat(format.parse(&quot;2000-01-01&quot;)).isNotBetween(Instant.parse(&quot;1900-01-01T00:00:00Z&quot;), Instant.parse(&quot;2000-01-01T00:00:00Z&quot;));
   *
   * // assertions will fail
   * assertThat(format.parse(&quot;2001-12-24&quot;)).isNotBetween(Instant.parse(&quot;2000-01-01T00:00:00Z&quot;), Instant.parse(&quot;2100-01-01T00:00:00Z&quot;));
   * assertThat(format.parse(&quot;1900-01-01&quot;)).isNotBetween(Instant.parse(&quot;1900-01-01T00:00:00Z&quot;), Instant.parse(&quot;2000-01-01T00:00:00Z&quot;));</code></pre>
   *
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if start {@code Instant} is {@code null}.
   * @throws NullPointerException if end {@code Instant} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is in [start, end[ period.
   * @since 3.19.0
   */
  public SELF isNotBetween(Instant start, Instant end) {
    return isNotBetween(dateFrom(start), dateFrom(end), true, false);
  }

  /**
   * Same assertion as {@link #isNotBetween(Date, Date)} but given date is represented as String either with one of the
   * supported default date formats or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(theFellowshipOfTheRing.getReleaseDate()).isNotBetween("2002-12-01", "2002-12-10");
   *
   * // assertion will fail
   * assertThat(theFellowshipOfTheRing.getReleaseDate()).isNotBetween("2002-12-01", "2002-12-19")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if start Date as String is {@code null}.
   * @throws NullPointerException if end Date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is in [start, end[ period.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isNotBetween(String start, String end) {
    return isNotBetween(parse(start), parse(end), true, false);
  }

  /**
   * Verifies that the actual {@code Date} is strictly in the past.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(theTwoTowers.getReleaseDate()).isInThePast();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in the past.
   */
  public SELF isInThePast() {
    return executeAssertion(this::assertIsInThePast);
  }

  /**
   * Verifies that the actual {@code Date} is today, that is matching current year, month and day (no check on hour,
   * minute, second, milliseconds).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new Date()).isToday();
   *
   * // assertion will fail
   * assertThat(theFellowshipOfTheRing.getReleaseDate()).isToday();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not today.
   */
  public SELF isToday() {
    return executeAssertion(this::assertIsToday);
  }

  /**
   * Verifies that the actual {@code Date} is strictly in the future.
   * <p>
   * Example:
   * <pre><code class='java'> Date now = new Date();
   * // assertion succeeds:
   * assertThat(new Date(now.getTime() + 1000)).isInTheFuture();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in the future.
   */
  public SELF isInTheFuture() {
    return executeAssertion(this::assertIsInTheFuture);
  }

  /**
   * Verifies that the actual {@code Date} is not in the past, i.e. it is either now or in the future.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(new Date(System.currentTimeMillis() + 1000)).isNotInThePast();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is in the past.
   *
   * @since 4.0.0
   */
  public SELF isNotInThePast() {
    return executeAssertion(this::assertIsNotInThePast);
  }

  /**
   * Verifies that the actual {@code Date} is not in the future, i.e. it is either now or in the past.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds:
   * assertThat(theTwoTowers.getReleaseDate()).isNotInTheFuture();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is in the future.
   *
   * @since 4.0.0
   */
  public SELF isNotInTheFuture() {
    return executeAssertion(this::assertIsNotInTheFuture);
  }

  /**
   * Verifies that the actual {@code Date} is <b>strictly</b> before the given year.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeYear(2004);
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeYear(2002);
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeYear(2000);</code></pre>
   *
   * @param year the year to compare actual year to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} year is after or equals to the given year.
   */
  public SELF isBeforeYear(int year) {
    return executeAssertion(() -> assertIsBeforeYear(year));
  }

  /**
   * Verifies that the actual {@code Date} is <b>strictly</b> after the given year.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isAfterYear(2001);
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isAfterYear(2002);
   * assertThat(theTwoTowers.getReleaseDate()).isAfterYear(2004);</code></pre>
   *
   * @param year the year to compare actual year to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} year is before or equals to the given year.
   */
  public SELF isAfterYear(int year) {
    return executeAssertion(() -> assertIsAfterYear(year));
  }

  /**
   * Verifies that the actual {@code Date} year is equal to the given year.
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).hasYear(2002);
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).hasYear(2004);</code></pre>
   *
   * @param year the year to compare actual year to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} year is not equal to the given year.
   */
  public SELF hasYear(int year) {
    return executeAssertion(() -> assertHasYear(year));
  }

  /**
   * Verifies that the actual {@code Date} month is equal to the given month, <b>month value starting at 1</b>
   * (January=1, February=2, ...).
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).hasMonth(12);
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).hasMonth(10);</code></pre>
   *
   * @param month the month to compare actual month to, <b>month value starting at 1</b> (January=1, February=2, ...).
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} month is not equal to the given month.
   */
  public SELF hasMonth(int month) {
    return executeAssertion(() -> assertHasMonth(month));
  }

  /**
   * Verifies that the actual {@code Date} day of month is equal to the given day of month.
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).hasDayOfMonth(18);
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).hasDayOfMonth(20);</code></pre>
   *
   * @param dayOfMonth the day of month to compare actual day of month to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} month is not equal to the given day of month.
   */
  public SELF hasDayOfMonth(int dayOfMonth) {
    return executeAssertion(() -> assertHasDayOfMonth(dayOfMonth));
  }

  /**
   * Verifies that the actual {@code Date} day of week is equal to the given day of week (see
   * {@link Calendar#DAY_OF_WEEK} for valid values).
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new Date(parseDatetime("2003-04-26T13:20:35").getTime()).hasDayOfWeek(Calendar.SATURDAY);
   *
   * // assertion will fail
   * assertThat(new Date(parseDatetime("2003-04-26T13:20:35").getTime()).hasDayOfWeek(Calendar.MONDAY);</code></pre>
   *
   * @param dayOfWeek the day of week to compare actual day of week to, see {@link Calendar#DAY_OF_WEEK} for valid
   *          values
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} week is not equal to the given day of week.
   */
  public SELF hasDayOfWeek(int dayOfWeek) {
    return executeAssertion(() -> assertHasDayOfWeek(dayOfWeek));
  }

  /**
   * Verifies that the actual {@code Date} hour of day is equal to the given hour of day (24-hour clock).
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new Date(parseDatetime("2003-04-26T13:20:35").getTime()).hasHourOfDay(13);
   *
   * // assertion will fail
   * assertThat(new Date(parseDatetime("2003-04-26T13:20:35").getTime()).hasHourOfDay(22);</code></pre>
   *
   * @param hourOfDay the hour of day to compare actual hour of day to (24-hour clock)
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} hour is not equal to the given hour.
   */
  public SELF hasHourOfDay(int hourOfDay) {
    return executeAssertion(() -> assertHasHourOfDay(hourOfDay));
  }

  /**
   * Verifies that the actual {@code Date} minute is equal to the given minute.
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new Date(parseDatetime("2003-04-26T13:20:35").getTime()).hasMinute(20);
   *
   * // assertion will fail
   * assertThat(new Date(parseDatetime("2003-04-26T13:20:35").getTime()).hasMinute(17);</code></pre>
   *
   * @param minute the minute to compare actual minute to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} minute is not equal to the given minute.
   */
  public SELF hasMinute(int minute) {
    return executeAssertion(() -> assertHasMinute(minute));
  }

  /**
   * Verifies that the actual {@code Date} second is equal to the given second.
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new Date(parseDatetime("2003-04-26T13:20:35").getTime()).hasSecond(35);
   *
   * // assertion will fail
   * assertThat(new Date(parseDatetime("2003-04-26T13:20:35").getTime()).hasSecond(11);</code></pre>
   *
   * @param second the second to compare actual second to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} second is not equal to the given second.
   */
  public SELF hasSecond(int second) {
    return executeAssertion(() -> assertHasSecond(second));
  }

  /**
   * Verifies that the actual {@code Date} millisecond is equal to the given millisecond.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion will pass
   * assertThat(parseDatetimeWithMs("2003-04-26T13:20:35.017")).hasMillisecond(17);
   *
   * // assertion will fail
   * assertThat(parseDatetimeWithMs("2003-04-26T13:20:35.017")).hasMillisecond(25);</code></pre>
   *
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param millisecond the millisecond to compare actual millisecond to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} millisecond is not equal to the given millisecond.
   */
  public SELF hasMillisecond(int millisecond) {
    return executeAssertion(() -> assertHasMillisecond(millisecond));
  }

  /**
   * Verifies that actual and given {@code Date} are in the same year.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parse("2003-04-26");
   * Date date2 = parse("2003-05-27");
   *
   * assertThat(date1).isInSameYearAs(date2);</code></pre>
   *
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same year.
   */
  public SELF isInSameYearAs(Date other) {
    return executeAssertion(() -> assertIsInSameYearAs(other));
  }

  /**
   * Verifies that actual {@code Date} and given {@code Instant} are in the same year.
   * <p>
   * Example:
   * <pre><code class='java'> Date date = parse("2003-04-26");
   * Instant instant = Instant.parse("2003-04-26T12:30:00Z");
   *
   * assertThat(date).isInSameYearAs(instant);</code></pre>
   *
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param other the given {@code Instant} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Instant} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual {@code Date} and given {@code Instant} are not in the same year.
   * @since 3.19.0
   */
  public SELF isInSameYearAs(Instant other) {
    return executeAssertion(() -> assertIsInSameYearAs(dateFrom(other)));
  }

  /**
   * Same assertion as {@link #isInSameYearAs(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format take precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parse("2003-04-26");
   * assertThat(date1).isInSameYearAs("2003-05-27")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given Date represented as String are not in the same year.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isInSameYearAs(String dateAsString) {
    return isInSameYearAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} have same month and year fields.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parse("2003-04-26");
   * Date date2 = parse("2003-04-27");
   *
   * assertThat(date1).isInSameMonthAs(date2);</code></pre>
   *
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same month and year.
   */
  public SELF isInSameMonthAs(Date other) {
    return executeAssertion(() -> assertIsInSameMonthAs(other));
  }

  /**
   * Verifies that actual {@code Date} and given {@code Instant} have same month and year fields.
   * <p>
   * Example:
   * <pre><code class='java'> Date date = parse("2003-04-26");
   * Instant instant = Instant.parse("2003-04-27T12:30:00Z");
   *
   * assertThat(date).isInSameMonthAs(instant);</code></pre>
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param other the given {@code Instant} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Instant} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual {@code Date} and given {@code Instant} are not in the same month and year.
   * @since 3.19.0
   */
  public SELF isInSameMonthAs(Instant other) {
    return executeAssertion(() -> assertIsInSameMonthAs(dateFrom(other)));
  }

  /**
   * Same assertion as {@link #isInSameMonthAs(Date)}but given date is represented as String either with one of the
   * supported default date formats or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parse("2003-04-26");
   * assertThat(date1).isInSameMonthAs("2003-04-27")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same month.
   */
  public SELF isInSameMonthAs(String dateAsString) {
    return isInSameMonthAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} have the same day of month, month and year fields values.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T23:17:00");
   * Date date2 = parseDatetime("2003-04-26T12:30:00");
   *
   * assertThat(date1).isInSameDayAs(date2);</code></pre>
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same day, month and year.
   */
  public SELF isInSameDayAs(Date other) {
    return executeAssertion(() -> assertIsInSameDayAs(other));
  }

  /**
   * Verifies that actual {@code Date} and given {@code Instant} have the same day of month, month and year fields values.
   * <p>
   * Example:
   * <pre><code class='java'> Date date = parseDatetime("2003-04-26T23:17:00");
   * Instant instant = Instant.parse("2003-04-26T12:30:00Z");
   *
   * assertThat(date).isInSameDayAs(instant);</code></pre>
   *
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Instant} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual {@code Date} and given {@code Instant} are not in the same day, month and year.
   * @since 3.19.0
   */
  public SELF isInSameDayAs(Instant other) {
    return executeAssertion(() -> assertIsInSameDayAs(dateFrom(other)));
  }

  /**
   * Same assertion as {@link #isInSameDayAs(Date)} but given date is represented as String either with one of the
   * supported default date formats or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T23:17:00");
   * assertThat(date1).isInSameDayAs("2003-04-26")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same day of month.
   */
  public SELF isInSameDayAs(String dateAsString) {
    return isInSameDayAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same hour (i.e. their time difference &lt; 1 hour).
   * <p>
   * This assertion succeeds as time difference is exactly &lt; 1h:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T13:00:00");
   * Date date2 = parseDatetime("2003-04-26T13:30:00");
   * assertThat(date1).isInSameHourWindowAs(date2);</code></pre>
   *
   * Two dates can have different hour fields and yet be in the same chronological hour, example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T13:00:00");
   * Date date2 = parseDatetime("2003-04-26T12:59:59");
   * // succeeds as time difference == 1s
   * assertThat(date1).isInSameHourWindowAs(date2);</code></pre>
   *
   * These assertions fail as time difference is equal to or greater than one hour:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T13:00:00");
   * Date date2 = parseDatetime("2003-04-26T14:00:01");
   * Date date3 = parseDatetime("2003-04-26T14:00:00");
   * assertThat(date1).isInSameHourWindowAs(date2);
   * assertThat(date1).isInSameHourWindowAs(date3);</code></pre>
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same hour.
   */
  public SELF isInSameHourWindowAs(Date other) {
    return executeAssertion(() -> assertIsInSameHourWindowAs(other));
  }

  /**
   * Verifies that actual {@code Date} and given {@code Instant} are chronologically in the same hour (i.e. their time
   * difference &lt; 1 hour).
   * <p>
   * This assertion succeeds as time difference is exactly = 1h:
   * <pre><code class='java'> Date date = parseDatetime("2003-04-26T13:00:00Z");
   * Instant instant = Instant.parse("2003-04-26T14:00:00Z");
   * assertThat(date).isInSameHourWindowAs(instant);</code></pre>
   *
   * Two date/instant can have different hour fields and yet be in the same chronological hour, example:
   * <pre><code class='java'> Date date = parseDatetime("2003-04-26T13:00:00Z");
   * Instant instant = Instant.parse("2003-04-26T12:59:59Z");
   * // succeeds as time difference == 1s
   * assertThat(date).isInSameHourWindowAs(instant);</code></pre>
   *
   * These assertions fail as time difference is equal to or greater than one hour:
   * <pre><code class='java'> Date date = parseDatetime("2003-04-26T13:00:00Z");
   * Instant instant = Instant.parse("2003-04-26T14:00:01Z");
   * Instant instant2 = Instant.parse("2003-04-26T14:00:00Z");
   * assertThat(date).isInSameHourWindowAs(instant);
   * assertThat(date).isInSameHourWindowAs(instant2);</code></pre>
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param other the given {@code Instant} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Instant} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual {@code Date} and given {@code Instant} are not in the same hour.
   * @since 3.19.0
   */
  public SELF isInSameHourWindowAs(Instant other) {
    return executeAssertion(() -> assertIsInSameHourWindowAs(dateFrom(other)));
  }

  /**
   * Same assertion as {@link #isInSameHourWindowAs(java.util.Date)} but given date is represented as String either
   * with one of the supported default date formats or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same day of month.
   */
  public SELF isInSameHourWindowAs(String dateAsString) {
    return isInSameHourWindowAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same minute (i.e. their time difference &lt; 1
   * minute).
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-01-01T12:01:00");
   * Date date2 = parseDatetime("2003-01-01T12:01:30");
   *
   * // succeeds because date time difference &lt; 1 min
   * assertThat(date1).isInSameMinuteWindowAs(date2);</code></pre>
   *
   * Two dates can have different minute fields and yet be in the same chronological minute, example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-01-01T12:01:00");
   * Date date3 = parseDatetime("2003-01-01T12:00:59");
   *
   * // succeeds as time difference == 1s even though minute fields differ
   * assertThat(date1).isInSameMinuteWindowAs(date3);</code></pre>
   *
   * This assertion fails as time difference is &ge; 1 min:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-01-01T12:01:00");
   * Date date2 = parseDatetime("2003-01-01T12:02:00");
   * assertThat(date1).isInSameMinuteWindowAs(date2);</code></pre>
   *
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same minute.
   */
  public SELF isInSameMinuteWindowAs(Date other) {
    return executeAssertion(() -> assertIsInSameMinuteWindowAs(other));
  }

  /**
   * Verifies that actual {@code Date} and given {@code Instant} are chronologically in the same minute (i.e. their time difference &lt; 1
   * minute).
   * <p>
   * Example:
   * <pre><code class='java'> Date date = parseDatetime("2003-01-01T12:01:00Z");
   * Instant instant = Instant.parse("2003-01-01T12:01:30Z");
   *
   * // succeeds because date time difference &lt; 1 min
   * assertThat(date).isInSameMinuteWindowAs(instant);</code></pre>
   *
   * Two date/instant can have different minute fields and yet be in the same chronological minute, example:
   * <pre><code class='java'> Date date = parseDatetime("2003-01-01T12:01:00Z");
   * Instant instant = Instant.parse("2003-01-01T12:00:59Z");
   *
   * // succeeds as time difference == 1s even though minute fields differ
   * assertThat(date).isInSameMinuteWindowAs(instant);</code></pre>
   *
   * This assertion fails as time difference is &ge; 1 min:
   * <pre><code class='java'> Date date = parseDatetime("2003-01-01T12:01:00Z");
   * Instant instant = Instant.parse("2003-01-01T12:02:00Z");
   * assertThat(date).isInSameMinuteWindowAs(instant);</code></pre>
   *
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param other the given {@code Instant} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Instant} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual {@code Date} and given {@code Instant} are not in the same minute.
   * @since 3.19.0
   */
  public SELF isInSameMinuteWindowAs(Instant other) {
    return executeAssertion(() -> assertIsInSameMinuteWindowAs(dateFrom(other)));
  }

  /**
   * Same assertion as {@link #isInSameMinuteWindowAs(Date)} but given date is represented as String either with one of
   * the supported default date formats or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same minute.
   */
  public SELF isInSameMinuteWindowAs(String dateAsString) {
    return isInSameMinuteWindowAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically strictly in the same second (i.e. their time
   * difference &lt; 1 second).
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetimeWithMs("2003-04-26T13:01:02.123");
   * Date date2 = parseDatetimeWithMs("2003-04-26T13:01:02.456");
   *
   * // succeeds as time difference is &lt; 1s
   * assertThat(date1).isInSameSecondWindowAs(date2);</code></pre>
   *
   * Two dates can have different second fields and yet be in the same chronological second, example:
   * <pre><code class='java'> Date date1 = parseDatetimeWithMs("2003-04-26T13:01:02.999");
   * Date date2 = parseDatetimeWithMs("2003-04-26T13:01:03.000");
   *
   * // succeeds as time difference is 1ms &lt; 1s
   * assertThat(date1).isInSameSecondWindowAs(date2);</code></pre>
   *
   * Those assertions fail as time difference is greater or equal to one second:
   * <pre><code class='java'> Date date1 = parseDatetimeWithMs("2003-04-26T13:01:01.000");
   * Date date2 = parseDatetimeWithMs("2003-04-26T13:01:02.000");
   *
   * // fails as time difference = 1s
   * assertThat(date1).isInSameSecondWindowAs(date2);
   *
   * Date date3 = parseDatetimeWithMs("2003-04-26T13:01:02.001");
   * // fails as time difference &gt; 1s
   * assertThat(date1).isInSameSecondWindowAs(date3);</code></pre>
   *
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same second.
   */
  public SELF isInSameSecondWindowAs(Date other) {
    return executeAssertion(() -> assertIsInSameSecondWindowAs(other));
  }

  /**
   * Verifies that actual {@code Date} and given {@code Instant} are chronologically strictly in the same second (i.e. their time
   * difference &lt; 1 second).
   * <p>
   * Example:
   * <pre><code class='java'> Date date = parseDatetimeWithMs("2003-04-26T13:01:02.123Z");
   * Instant instant = Instant.parse("2003-04-26T13:01:02.456Z");
   *
   * // succeeds as time difference is &lt; 1s
   * assertThat(date).isInSameSecondWindowAs(instant);</code></pre>
   *
   * Two dates can have different second fields and yet be in the same chronological second, example:
   * <pre><code class='java'> Date date = parseDatetimeWithMs("2003-04-26T13:01:02.999Z");
   * Instant instant = Instant.parse("2003-04-26T13:01:03.000Z");
   *
   * // succeeds as time difference is 1ms &lt; 1s
   * assertThat(date).isInSameSecondWindowAs(instant);</code></pre>
   *
   * Those assertions fail as time difference is greater or equal to one second:
   * <pre><code class='java'> Date date = parseDatetimeWithMs("2003-04-26T13:01:01.000Z");
   * Instant instant = Instant.parse("2003-04-26T13:01:02.000Z");
   *
   * // fails as time difference = 1s
   * assertThat(date).isInSameSecondWindowAs(instant);
   *
   * Instant instant2 = Instant.parse("2003-04-26T13:01:02.001Z");
   * // fails as time difference &gt; 1s
   * assertThat(date).isInSameSecondWindowAs(instant2);</code></pre>
   *
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   *
   * @param other the given {@code Instant} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Instant} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual {@code Date} and given {@code Instant} are not in the same second.
   * @since 3.19.0
   */
  public SELF isInSameSecondWindowAs(Instant other) {
    return executeAssertion(() -> assertIsInSameSecondWindowAs(dateFrom(other)));
  }

  /**
   * Same assertion as {@link #isInSameSecondWindowAs(Date)} but given date is represented as String either with one of
   * the supported default date formats or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same second.
   */
  public SELF isInSameSecondWindowAs(String dateAsString) {
    return isInSameSecondWindowAs(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is close to the other date by less than delta (expressed in milliseconds),
   * if
   * difference is equal to delta it's ok.
   * <p>
   * One can use handy {@link TimeUnit} to convert a duration in milliseconds, for example you can express a delta of 5
   * seconds with <code>TimeUnit.SECONDS.toMillis(5)</code>.
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = new Date();
   * Date date2 = new Date(date1.getTime() + 100);
   *
   * // assertion succeeds
   * assertThat(date1).isCloseTo(date2, 101)
   *                  .isCloseTo(date2, 100);
   *
   * // assertion fails
   * assertThat(date1).isCloseTo(date2, 80);</code></pre>
   *
   * @param other the date to compare actual to
   * @param deltaInMilliseconds the delta used for date comparison, expressed in milliseconds
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not close to the given date by less than delta.
   */
  public SELF isCloseTo(Date other, long deltaInMilliseconds) {
    return executeAssertion(() -> assertIsCloseTo(other, deltaInMilliseconds));
  }

  /**
   * Verifies that the actual {@code Date} is close to the given {@code Instant} by less than delta (expressed in milliseconds),
   * if the difference is equal to delta the assertion succeeds.
   * <p>
   * One can use handy {@link TimeUnit} to convert a duration in milliseconds, for example you can express a delta of 5
   * seconds with <code>TimeUnit.SECONDS.toMillis(5)</code>.
   * <p>
   * Note that using a {@link #usingComparator(Comparator) custom comparator}  has no effect on this assertion.
   * <p>
   * Example:
   * <pre><code class='java'> Date date = new Date();
   *
   * // assertions succeed
   * assertThat(date).isCloseTo(date.toInstant().plusMillis(80), 80)
   *                 .isCloseTo(date.toInstant().plusMillis(80), 100);
   *
   * // assertions fails
   * assertThat(date).isCloseTo(date.toInstant().minusMillis(101), 100);</code></pre>
   *
   * @param other the Instant to compare actual to
   * @param deltaInMilliseconds the delta used for date comparison, expressed in milliseconds
   * @return this assertion object.
   * @throws NullPointerException if {@code Instant} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not close to the given Instant by less than delta.
   * @since 3.19.0
   */
  public SELF isCloseTo(Instant other, long deltaInMilliseconds) {
    return executeAssertion(() -> assertIsCloseTo(dateFrom(other), deltaInMilliseconds));
  }

  /**
   * Same assertion as {@link #isCloseTo(Date, long)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format take precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Examples of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @param deltaInMilliseconds the delta used for date comparison, expressed in milliseconds
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not close to the given date by less than delta.
   */
  public SELF isCloseTo(String dateAsString, long deltaInMilliseconds) {
    return isCloseTo(parse(dateAsString), deltaInMilliseconds);
  }

  /**
   * Verifies that the actual {@code Date} has the same time as the given timestamp.
   * <p>
   * Both time or timestamp express a number of milliseconds since January 1, 1970, 00:00:00 GMT.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(new Date(42)).hasTime(42);</code></pre>
   *
   * @param timestamp the timestamp to compare actual time to.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} time is not equal to the given timestamp.
   * @see Date#getTime()
   */
  public SELF hasTime(long timestamp) {
    return executeAssertion(() -> assertHasTime(timestamp));
  }

  /**
   * Verifies that the actual {@code Date} has the same time as the given date, useful to compare {@link Date} and
   * {@link java.sql.Timestamp}.
   * <p>
   * Example:
   * <pre><code class='java'> Date date = new Date();
   * Timestamp timestamp = new Timestamp(date.getTime());
   *
   * // Fail as date is not an instance of Timestamp
   * assertThat(date).isEqualTo(timestamp);
   *
   * // Succeed as we compare date and timestamp time.
   * assertThat(date).hasSameTimeAs(timestamp);</code></pre>
   *
   * @param date the date to compare actual time to.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} time is not equal to the given date time.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @see Date#getTime()
   */
  public SELF hasSameTimeAs(Date date) {
    return executeAssertion(() -> assertHasSameTime(date));
  }

  /**
   * Verifies that the actual {@code Date} represents the same time as the given date in {@code String} format.
   * <p>
   * It is the same assertion as {@link #hasSameTimeAs(Date)} but given date is represented as String either with one of
   * the supported default date formats or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * User custom date format take precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date = parseDatetime("2003-04-26T12:00:00");
   *
   * // assertion will pass
   * assertThat(date).hasSameTimeAs("2003-04-26T12:00:00");
   *
   * // assertion will fail
   * assertThat(date).hasSameTimeAs("2003-04-26T12:00:01");
   * assertThat(date).hasSameTimeAs("2003-04-27T12:00:00")</code></pre>
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   *
   * @param dateAsString the given {@code Date} represented as {@code String} in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} time is not equal to the time from date represented as
   *           String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF hasSameTimeAs(String dateAsString) {
    return executeAssertion(() -> assertHasSameTime(parse(dateAsString)));
  }

  /**
   * Instead of using default date formats for the date String based Date assertions like {@link #isEqualTo(String)},
   * AssertJ is going to use any date formats registered with one of these methods :
   * <ul>
   * <li>{@link #withDateFormat(String)}</li>
   * <li>this method</li>
   * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(String)}</li>
   * </ul>
   * <p>
   * Beware that :
   * <ul>
   * <li>this will be the case for <b>all future Date assertions in the test suite</b></li>
   * <li>once a custom date format is registered, the default date formats are not used anymore</li>
   * </ul>
   * <p>
   * To revert to default format, call {@link #useDefaultDateFormatsOnly()} or {@link #withDefaultDateFormatsOnly()}.
   *
   * @param userCustomDateFormat the new Date format used for String based Date assertions.
   * @return this assertion object.
   */
  @CheckReturnValue
  public SELF withDateFormat(DateFormat userCustomDateFormat) {
    registerCustomDateFormat(userCustomDateFormat);
    return myself;
  }

  /**
   * Instead of using default date formats for the date String based Date assertions like {@link #isEqualTo(String)},
   * AssertJ is going to use any date formats registered with one of these methods :
   * <ul>
   * <li>this method</li>
   * <li>{@link #withDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(String)}</li>
   * </ul>
   * <p>
   * Beware that :
   * <ul>
   * <li>this will be the case for <b>all future Date assertions in the test suite</b></li>
   * <li>once a custom date format is registered, the default date formats are not used anymore</li>
   * </ul>
   * <p>
   * To revert to default format, call {@link #useDefaultDateFormatsOnly()} or {@link #withDefaultDateFormatsOnly()}.
   *
   * @param userCustomDateFormatPattern the new Date format string pattern used for String based Date assertions.
   * @return this assertion object.
   */
  @CheckReturnValue
  public SELF withDateFormat(String userCustomDateFormatPattern) {
    requireNonNull(userCustomDateFormatPattern, DATE_FORMAT_PATTERN_SHOULD_NOT_BE_NULL);
    return withDateFormat(new SimpleDateFormat(userCustomDateFormatPattern));
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link #isEqualTo(String)}.
   * <p>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p>
   * AssertJ is going to use any date formats registered with one of these methods :
   * <ul>
   * <li>{@link #withDateFormat(String)}</li>
   * <li>{@link #withDateFormat(java.text.DateFormat)}</li>
   * <li>this method</li>
   * <li>{@link #registerCustomDateFormat(String)}</li>
   * </ul>
   * <p>
   * Beware that AssertJ will use the newly registered format for <b>all remaining Date assertions in the test suite</b>
   * <p>
   * To revert to default formats only, call {@link #useDefaultDateFormatsOnly()} or
   * {@link #withDefaultDateFormatsOnly()}.
   * <p>
   * Code examples:
   * <pre><code class='java'> Date date = ... // set to 2003 April the 26th
   * assertThat(date).isEqualTo("2003-04-26");
   *
   * try {
   *   // date with a custom format : assertionError since the default formats don't match.
   *   assertThat(date).isEqualTo("2003/04/26");
   * } catch (AssertionError e) {
   *   assertThat(e).hasMessage("Failed to parse 2003/04/26 with any of these date formats: " +
   *                            "[yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
   * }
   *
   * // registering a custom date format to make the assertion pass
   * registerCustomDateFormat(new SimpleDateFormat("yyyy/MM/dd")); // registerCustomDateFormat("yyyy/MM/dd") would work to.
   * assertThat(date).isEqualTo("2003/04/26");
   *
   * // the default formats are still available and should work
   * assertThat(date).isEqualTo("2003-04-26");</code></pre>
   *
   * @param userCustomDateFormat the new Date format used for String based Date assertions.
   */
  public static void registerCustomDateFormat(DateFormat userCustomDateFormat) {
    ConfigurationProvider.loadRegisteredConfiguration();
    requireNonNull(userCustomDateFormat, DATE_FORMAT_SHOULD_NOT_BE_NULL);
    userDateFormats.get().add(userCustomDateFormat);
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link #isEqualTo(String)}.
   * <p>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p>
   * AssertJ is going to use any date formats registered with one of these methods:
   * <ul>
   * <li>{@link #withDateFormat(String)}</li>
   * <li>{@link #withDateFormat(java.text.DateFormat)}</li>
   * <li>{@link #registerCustomDateFormat(java.text.DateFormat)}</li>
   * <li>this method</li>
   * </ul>
   * <p>
   * Beware that AssertJ will use the newly registered format for <b>all remaining Date assertions in the test suite</b>
   * <p>
   * To revert to default formats only, call {@link #useDefaultDateFormatsOnly()} or
   * {@link #withDefaultDateFormatsOnly()}.
   * <p>
   * Code examples:
   * <pre><code class='java'> Date date = ... // set to 2003 April the 26th
   * assertThat(date).isEqualTo("2003-04-26");
   *
   * try {
   *   // date with a custom format : assertionError since the default formats don't match.
   *   assertThat(date).isEqualTo("2003/04/26");
   * } catch (AssertionError e) {
   *   assertThat(e).hasMessage("Failed to parse 2003/04/26 with any of these date formats: " +
   *                            "[yyyy-MM-dd'T'HH:mm:ss.SSS, yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd]");
   * }
   *
   * // registering a custom date format to make the assertion pass
   * registerCustomDateFormat("yyyy/MM/dd");
   * assertThat(date).isEqualTo("2003/04/26");
   *
   * // the default formats are still available and should work
   * assertThat(date).isEqualTo("2003-04-26");</code></pre>
   *
   * @param userCustomDateFormatPattern the new Date format pattern used for String based Date assertions.
   */
  public static void registerCustomDateFormat(String userCustomDateFormatPattern) {
    requireNonNull(userCustomDateFormatPattern, DATE_FORMAT_PATTERN_SHOULD_NOT_BE_NULL);
    registerCustomDateFormat(new SimpleDateFormat(userCustomDateFormatPattern));
  }

  /**
   * Remove all registered custom date formats =&gt; use only the defaults date formats to parse string as date.
   * <p>
   * User custom date format take precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Examples of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * <p>
   * If you are getting an {@code IllegalArgumentException} with <i>"Unknown pattern character 'X'"</i> message (some Android versions don't support it),
   * you can explicitly specify the date format to use so that the default ones are bypassed.
   */
  public static void useDefaultDateFormatsOnly() {
    userDateFormats.get().clear();
  }

  /**
   * Remove all registered custom date formats =&gt; use only the default date formats to parse string as date.
   * <p>
   * User custom date format takes precedence over the default ones.
   * <p>
   * Unless specified otherwise, beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format are:
   * <ul>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME ISO_LOCAL_DATE_TIME}</li>
   * <li>{@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE ISO_LOCAL_DATE}</li>
   * <li>{@link Timestamp} format as supported by {@link Timestamp#valueOf(String)}</li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.000000001Z</code> (nanos precision)</li>
   * <li><code>2003-04-26T03:01:02.758+02:00</code></li>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T03:01:02+05:00</code></li>
   * <li><code>2003-04-26T03:01:02</code></li>
   * <li><code>2003-04-26 03:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @return this assertion
   */
  @CheckReturnValue
  public SELF withDefaultDateFormatsOnly() {
    useDefaultDateFormatsOnly();
    return myself;
  }

  /**
   * Thread safe utility method to parse a Date with {@link #userDateFormats} first, then {@link #DEFAULT_DATE_PARSERS}.
   * <p>
   * Returns <code>null</code> if dateAsString parameter is <code>null</code>.
   *
   * @param dateAsString the string to parse as a Date with {@link #userDateFormats}
   * @return the corresponding Date, null if dateAsString parameter is null.
   * @throws AssertionError if the string can't be parsed as a Date
   */
  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Date parse(String dateAsString) {
    if (dateAsString == null) return null;
    // parse with date format specified by user if any, otherwise use default formats
    // no synchronization needed as userCustomDateFormat is thread local
    Date date = parseDateWith(dateAsString, userDateFormats.get());
    if (date != null) return date;
    // no matching user date format, let's try default formats
    date = parseDateWithDefaultDateParsers(dateAsString);
    if (date != null) return date;
    // no matching date format, throw an error
    throw AssertionErrorCreator.instance().assertionError(format("Failed to parse %s with any of these date formats:%n   %s",
                                                                 dateAsString, usedFormats()));
  }

  private String usedFormats() {
    List<String> allDateFormatsInOrderOfUsage = userDateFormats.get().stream()
                                                               .map(STANDARD_REPRESENTATION::toStringOf)
                                                               .collect(toList());
    allDateFormatsInOrderOfUsage.addAll(DEFAULT_DATE_PARSERS_REPRESENTATION);
    // increment all elements except the first
    List<String> formattedDateFormats = allDateFormatsInOrderOfUsage.stream().skip(1).map(s -> "    " + s).collect(toList());
    formattedDateFormats.add(0, allDateFormatsInOrderOfUsage.get(0));
    return formattedDateFormats.stream().collect(joining(format(",%n"), "[", "]"));
  }

  private Date parseDateWithDefaultDateParsers(final String dateAsString) {
    // try from more specific to less specific ISO formats, then try timestamp
    return parseDateWithDateParsers(dateAsString, DEFAULT_DATE_PARSERS);
  }

  private Date parseDateWithDateParsers(final String dateAsString, final Collection<Function<String, Date>> dateParsers) {
    for (Function<String, Date> dateParser : dateParsers) {
      try {
        return dateParser.apply(dateAsString);
      } catch (@SuppressWarnings("unused") DateTimeParseException e) {
        // ignore and try next date format
      } catch (@SuppressWarnings("unused") IllegalArgumentException e) {
        // ignore (it's from Timestamp.valueOf)
      }
    }
    return null;
  }

  private Date parseDateWith(final String dateAsString, final Collection<DateFormat> dateFormats) {
    for (DateFormat defaultDateFormat : dateFormats) {
      try {
        return defaultDateFormat.parse(dateAsString);
      } catch (@SuppressWarnings("unused") ParseException e) {
        // ignore and try next date format
      }
    }
    return null;
  }

  private static <T> Date[] toDateArray(T[] values, Function<T, Date> converter) {
    Date[] dates = new Date[values.length];
    for (int i = 0; i < values.length; i++) {
      dates[i] = converter.apply(values[i]);
    }
    return dates;
  }

  private Date dateFrom(Instant instant) {
    return actual instanceof Timestamp ? Timestamp.from(instant) : Date.from(instant);
  }

  private void assertIsBefore(Date other) {
    isNotNull();
    dateParameterIsNotNull(other);
    if (!isBefore(actual, other)) throw assertionError(shouldBeBefore(actual, other, getComparisonStrategy()));
  }

  private void assertIsBeforeOrEqualTo(Date other) {
    isNotNull();
    dateParameterIsNotNull(other);
    if (!isBeforeOrEqualTo(actual, other)) throw assertionError(shouldBeBeforeOrEqualTo(actual, other, getComparisonStrategy()));
  }

  private void assertIsAfter(Date other) {
    isNotNull();
    dateParameterIsNotNull(other);
    if (!isAfter(actual, other)) throw assertionError(shouldBeAfter(actual, other, getComparisonStrategy()));
  }

  private void assertIsAfterOrEqualTo(Date other) {
    isNotNull();
    dateParameterIsNotNull(other);
    if (!isAfterOrEqualTo(actual, other)) throw assertionError(shouldBeAfterOrEqualTo(actual, other, getComparisonStrategy()));
  }

  private void assertIsBetween(Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    if (!actualIsBetweenGivenPeriod(start, end, inclusiveStart, inclusiveEnd))
      throw assertionError(shouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, getComparisonStrategy()));
  }

  private void assertIsNotBetween(Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    if (actualIsBetweenGivenPeriod(start, end, inclusiveStart, inclusiveEnd))
      throw assertionError(shouldNotBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, getComparisonStrategy()));
  }

  private boolean actualIsBetweenGivenPeriod(Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    isNotNull();
    requireNonNull(start, "The start date of period to compare actual with should not be null");
    requireNonNull(end, "The end date of period to compare actual with should not be null");
    boolean checkLowerBoundaryPeriod = inclusiveStart ? isAfterOrEqualTo(actual, start) : isAfter(actual, start);
    boolean checkUpperBoundaryPeriod = inclusiveEnd ? isBeforeOrEqualTo(actual, end) : isBefore(actual, end);
    return checkLowerBoundaryPeriod && checkUpperBoundaryPeriod;
  }

  private void assertIsInThePast() {
    isNotNull();
    if (!isBefore(actual, now())) throw assertionError(shouldBeInThePast(actual, getComparisonStrategy()));
  }

  private void assertIsToday() {
    isNotNull();
    Date todayWithoutTime = truncateTime(now());
    Date actualWithoutTime = truncateTime(actual);
    if (!getComparisonStrategy().areEqual(actualWithoutTime, todayWithoutTime))
      throw assertionError(shouldBeToday(actual, getComparisonStrategy()));
  }

  private void assertIsInTheFuture() {
    isNotNull();
    if (!isAfter(actual, now())) throw assertionError(shouldBeInTheFuture(actual, getComparisonStrategy()));
  }

  private void assertIsNotInThePast() {
    isNotNull();
    if (isBefore(actual, now())) throw assertionError(shouldBeNotInThePast(actual, getComparisonStrategy()));
  }

  private void assertIsNotInTheFuture() {
    isNotNull();
    if (isAfter(actual, now())) throw assertionError(shouldBeNotInTheFuture(actual, getComparisonStrategy()));
  }

  private void assertIsBeforeYear(int year) {
    isNotNull();
    if (yearOf(actual) >= year) throw assertionError(shouldBeBeforeYear(actual, year));
  }

  private void assertIsAfterYear(int year) {
    isNotNull();
    if (yearOf(actual) <= year) throw assertionError(shouldBeAfterYear(actual, year));
  }

  private void assertHasYear(int year) {
    isNotNull();
    if (yearOf(actual) != year) throw assertionError(shouldHaveDateField(actual, "year", year));
  }

  private void assertHasMonth(int month) {
    isNotNull();
    if (monthOf(actual) != month) throw assertionError(shouldHaveDateField(actual, "month", month));
  }

  private void assertHasDayOfMonth(int dayOfMonth) {
    isNotNull();
    if (dayOfMonthOf(actual) != dayOfMonth) throw assertionError(shouldHaveDateField(actual, "day of month", dayOfMonth));
  }

  private void assertHasDayOfWeek(int dayOfWeek) {
    isNotNull();
    if (dayOfWeekOf(actual) != dayOfWeek) throw assertionError(shouldHaveDateField(actual, "day of week", dayOfWeek));
  }

  private void assertHasHourOfDay(int hourOfDay) {
    isNotNull();
    if (hourOfDayOf(actual) != hourOfDay) throw assertionError(shouldHaveDateField(actual, "hour", hourOfDay));
  }

  private void assertHasMinute(int minute) {
    isNotNull();
    if (minuteOf(actual) != minute) throw assertionError(shouldHaveDateField(actual, "minute", minute));
  }

  private void assertHasSecond(int second) {
    isNotNull();
    if (secondOf(actual) != second) throw assertionError(shouldHaveDateField(actual, "second", second));
  }

  private void assertHasMillisecond(int millisecond) {
    isNotNull();
    if (millisecondOf(actual) != millisecond) throw assertionError(shouldHaveDateField(actual, "millisecond", millisecond));
  }

  private void assertIsInSameYearAs(Date other) {
    isNotNull();
    dateParameterIsNotNull(other);
    if (!areInSameYear(actual, other)) throw assertionError(shouldBeInSameYear(actual, other));
  }

  private void assertIsInSameMonthAs(Date other) {
    isNotNull();
    dateParameterIsNotNull(other);
    if (!areInSameMonth(actual, other)) throw assertionError(shouldBeInSameMonth(actual, other));
  }

  private void assertIsInSameDayAs(Date other) {
    isNotNull();
    dateParameterIsNotNull(other);
    if (!areInSameDayOfMonth(actual, other)) throw assertionError(shouldBeInSameDay(actual, other));
  }

  private void assertIsInSameHourWindowAs(Date other) {
    isNotNull();
    dateParameterIsNotNull(other);
    if (timeDifference(actual, other) >= HOURS.toMillis(1)) throw assertionError(shouldBeInSameHourWindow(actual, other));
  }

  private void assertIsInSameMinuteWindowAs(Date other) {
    isNotNull();
    dateParameterIsNotNull(other);
    if (timeDifference(actual, other) >= MINUTES.toMillis(1)) throw assertionError(shouldBeInSameMinuteWindow(actual, other));
  }

  private void assertIsInSameSecondWindowAs(Date other) {
    isNotNull();
    dateParameterIsNotNull(other);
    if (timeDifference(actual, other) >= SECONDS.toMillis(1)) throw assertionError(shouldBeInSameSecondWindow(actual, other));
  }

  private void assertIsCloseTo(Date other, long deltaInMilliseconds) {
    isNotNull();
    dateParameterIsNotNull(other);
    long difference = Math.abs(actual.getTime() - other.getTime());
    if (difference > deltaInMilliseconds)
      throw assertionError(shouldBeCloseTo(actual, other, deltaInMilliseconds, difference));
  }

  private void assertHasTime(long timestamp) {
    isNotNull();
    if (actual.getTime() != timestamp) throw assertionError(shouldHaveTime(actual, timestamp));
  }

  private void assertHasSameTime(Date expected) {
    isNotNull();
    dateParameterIsNotNull(expected);
    if (actual.getTime() != expected.getTime()) throw assertionError(shouldHaveSameTime(actual, expected));
  }

  private static boolean areInSameYear(Date actual, Date other) {
    return yearOf(actual) == yearOf(other);
  }

  private static boolean areInSameMonth(Date actual, Date other) {
    return areInSameYear(actual, other) && monthOf(actual) == monthOf(other);
  }

  private static boolean areInSameDayOfMonth(Date actual, Date other) {
    return areInSameMonth(actual, other) && dayOfMonthOf(actual) == dayOfMonthOf(other);
  }

  private static void dateParameterIsNotNull(Date date) {
    requireNonNull(date, "The date to compare actual with should not be null");
  }

  private boolean isBefore(Date actual, Date other) {
    return getComparisonStrategy().isLessThan(actual, other);
  }

  private boolean isBeforeOrEqualTo(Date actual, Date other) {
    return getComparisonStrategy().isLessThanOrEqualTo(actual, other);
  }

  private boolean isAfter(Date actual, Date other) {
    return getComparisonStrategy().isGreaterThan(actual, other);
  }

  private boolean isAfterOrEqualTo(Date actual, Date other) {
    return getComparisonStrategy().isGreaterThanOrEqualTo(actual, other);
  }

}
