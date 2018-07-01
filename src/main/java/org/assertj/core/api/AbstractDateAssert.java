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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.DateUtil.*;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Dates;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Date}s.
 * <p>
 * Note that assertions with date parameter comes with two flavor, one is obviously a {@link Date} and the other is a
 * {@link String} representing a Date.<br>
 * For the latter, the default format follows ISO 8901 : "yyyy-MM-dd", user can override it with a custom format by
 * calling {@link #withDateFormat(DateFormat)}.<br>
 * The user custom format will then be used for all next Date assertions (i.e not limited to the current assertion) in
 * the test suite.<br>
 * To turn back to default format, simply call {@link #withDefaultDateFormatsOnly()}.
 *
 * @param <SELF> the "self" type of this assertion class. Please read "<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>" for
 *          more details.
 * @author Tomasz Nurkiewicz (thanks for giving assertions idea)
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author William Delanoue
 * @author Michal Kordas
 */
public abstract class AbstractDateAssert<SELF extends AbstractDateAssert<SELF>> extends AbstractAssert<SELF, Date> {

  /**
   * the default DateFormat used to parse any String date representation.
   */
  private static final List<DateFormat> DEFAULT_DATE_FORMATS = newArrayList(newIsoDateTimeWithMsFormat(),
                                                                            newTimestampDateFormat(),
                                                                            newIsoDateTimeWithUtcTimeZoneFormat(),
                                                                            newIsoDateTimeFormat(),
                                                                            newIsoDateFormat());

  private static final String DATE_FORMAT_PATTERN_SHOULD_NOT_BE_NULL = "Given date format pattern should not be null";
  private static final String DATE_FORMAT_SHOULD_NOT_BE_NULL = "Given date format should not be null";

  /**
   * Used in String based Date assertions - like {@link #isAfter(String)} - to convert input date represented as string
   * to Date.<br>
   * It keeps the insertion order so first format added will be first format used.
   */
  @VisibleForTesting
  static ThreadLocal<LinkedHashSet<DateFormat>> userDateFormats = new ThreadLocal<LinkedHashSet<DateFormat>>() {
    @Override
    protected LinkedHashSet<DateFormat> initialValue() {
      return new LinkedHashSet<>();
    }
  };
  @VisibleForTesting
  Dates dates = Dates.instance();

  public AbstractDateAssert(Date actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Same assertion as {@link AbstractAssert#isEqualTo(Object) isEqualTo(Date date)} but given date is represented as
   * String either with one of the supported defaults date format or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isEqualTo("2002-12-18");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isEqualTo("2002-12-19");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * Same assertion as {@link AbstractDateAssert#isEqualToIgnoringHours(Date)} but given Date is represented as String
   * either with one of the default supported date format or user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // OK : all dates fields are the same up to minutes excluded
   * assertThat("2003-04-26T13:01:35").isEqualToIgnoringHours("2003-04-26T14:02:35");
   *
   * // KO : fail as day fields differ
   * assertThat("2003-04-26T14:01:35").isEqualToIgnoringHours("2003-04-27T13:02:35");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are not equal ignoring hours, minutes,
   *           seconds and milliseconds.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isEqualToIgnoringHours(String dateAsString) {
    return isEqualToIgnoringHours(parse(dateAsString));
  }

  /**
   * Same assertion as {@link AbstractAssert#isEqualTo(Object)} but given Date is represented as String either with
   * one of the default supported date format or user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T13:01:35");
   * Date date2 = parseDatetime("2003-04-26T14:01:00");
   * Date date3 = parseDatetime("2003-04-27T13:01:35");
   *
   * // OK : all dates fields are the same up to hours excluded
   * assertThat(date1).isEqualToIgnoringHours(date2);
   *
   * // KO : fail as day fields differ
   * assertThat(date1).isEqualToIgnoringHours(date3);</code></pre>
   *
   * @param date the given Date.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are not equal ignoring hours, minutes,
   *           seconds and milliseconds.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isEqualToIgnoringHours(Date date) {
    dates.assertIsEqualWithPrecision(info, actual, date, HOURS);
    return myself;
  }

  /**
   * Same assertion as {@link AbstractDateAssert#isEqualToIgnoringMinutes(Date)} but given Date is represented as
   * String either with one of the default supported date format or user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> withDateFormat("yyyy-MM-dd'T'HH:mm:ss");
   * // OK : all dates fields are the same up to minutes excluded
   * assertThat("2003-04-26T13:01:35").isEqualToIgnoringMinutes("2003-04-26T13:02:35");
   *
   * // KO : fail as hour fields differ
   * assertThat("2003-04-26T14:01:35").isEqualToIgnoringMinutes("2003-04-26T13:02:35");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are not equal ignoring minutes, seconds and
   *           milliseconds.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isEqualToIgnoringMinutes(String dateAsString) {
    return isEqualToIgnoringMinutes(parse(dateAsString));
  }

  /**
   * Same assertion as {@link AbstractAssert#isEqualTo(Object)} but given Date should not take care of minutes,
   * seconds and milliseconds precision.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T13:01:35");
   * Date date2 = parseDatetime("2003-04-26T13:02:00");
   * Date date3 = parseDatetime("2003-04-26T14:02:00");
   *
   * // OK : all dates fields are the same up to minutes excluded
   * assertThat(date1).isEqualToIgnoringMinutes(date2);
   *
   * // KO : fail as hour fields differ
   * assertThat(date1).isEqualToIgnoringMinutes(date3);</code></pre>
   *
   * @param date the given Date.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are not equal ignoring minutes, seconds and
   *           milliseconds.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isEqualToIgnoringMinutes(Date date) {
    dates.assertIsEqualWithPrecision(info, actual, date, MINUTES);
    return myself;
  }

  /**
   * Same assertion as {@link AbstractDateAssert#isEqualToIgnoringSeconds(Date)} but given Date is represented as
   * String
   * either with one of the default supported date format or user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T13:01:35");
   *
   * // OK : all dates fields are the same up to seconds excluded
   * assertThat(date1).isEqualToIgnoringSeconds("2003-04-26T13:01:57");
   *
   * // KO : fail as minute fields differ
   * assertThat(date1).isEqualToIgnoringMinutes("2003-04-26T13:02:00");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are not equal ignoring seconds and
   *           milliseconds.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isEqualToIgnoringSeconds(String dateAsString) {
    return isEqualToIgnoringSeconds(parse(dateAsString));
  }

  /**
   * Same assertion as {@link AbstractAssert#isEqualTo(Object)} but given Date should not take care of seconds and
   * milliseconds precision.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T13:01:35");
   * Date date2 = parseDatetime("2003-04-26T13:01:36");
   * Date date3 = parseDatetime("2003-04-26T14:02:00");
   *
   * // OK : all dates fields are the same up to seconds excluded
   * assertThat(date1).isEqualToIgnoringSeconds(date2);
   *
   * // KO : fail as minute fields differ
   * assertThat(date1).isEqualToIgnoringSeconds(date3);</code></pre>
   *
   * @param date the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are not equal ignoring seconds and
   *           milliseconds.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isEqualToIgnoringSeconds(Date date) {
    dates.assertIsEqualWithPrecision(info, actual, date, SECONDS);
    return myself;
  }

  /**
   * Same assertion as {@link AbstractDateAssert#isEqualToIgnoringMillis(Date)} but given Date is represented as String
   * either with one of the default supported date format or user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetimeWithMs("2003-04-26T13:01:35.998");
   *
   * // OK : all dates fields are the same up to milliseconds excluded
   * assertThat().isEqualToIgnoringMillis("2003-04-26T13:01:35.997");
   *
   * // KO : fail as seconds fields differ
   * assertThat("2003-04-26T13:01:35.998").isEqualToIgnoringMinutes("2003-04-26T13:01:36.998");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are not equal ignoring milliseconds.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isEqualToIgnoringMillis(String dateAsString) {
    return isEqualToIgnoringMillis(parse(dateAsString));
  }

  /**
   * Same assertion as {@link AbstractAssert#isEqualTo(Object)} but given Date should not take care of milliseconds
   * precision.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetimeAndMs("2003-04-26T13:01:35.001");
   * Date date2 = parseDatetimeAndMs("2003-04-26T13:01:35.002");
   * Date date3 = parseDatetimeAndMs("2003-04-26T14:01:36.001");
   *
   * // OK : all dates fields are the same up to milliseconds excluded
   * assertThat(date1).isEqualToIgnoringMillis(date2);
   *
   * // KO : fail as second fields differ
   * assertThat(date1).isEqualToIgnoringMillis(date3);</code></pre>
   *
   * @param date the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are not equal ignoring milliseconds.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isEqualToIgnoringMillis(Date date) {
    dates.assertIsEqualWithPrecision(info, actual, date, MILLISECONDS);
    return myself;
  }

  /**
   * Same assertion as {@link AbstractAssert#isNotEqualTo(Object) isNotEqualTo(Date date)} but given date is
   * represented as String either with one of the supported defaults date format or a user custom date format (set with
   * method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass 
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isNotEqualTo("2002-12-19");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isNotEqualTo("2002-12-18");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * Same assertion as {@link Assert#isIn(Object...)}but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isIn("2002-12-17", "2002-12-18", "2002-12-19");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isIn("2002-12-17", "2002-12-19", "2002-12-20");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param datesAsString the given Dates represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual is not in given Dates represented as String.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isIn(String... datesAsString) {
    Date[] dates = new Date[datesAsString.length];
    for (int i = 0; i < datesAsString.length; i++) {
      dates[i] = parse(datesAsString[i]);
    }
    return isIn((Object[]) dates);
  }

  /**
   * Same assertion as {@link Assert#isIn(Iterable)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isInWithStringDateCollection(asList("2002-12-17", "2002-12-18", "2002-12-19"));
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isInWithStringDateCollection(asList("2002-12-17", "2002-12-19", "2002-12-20"));</code></pre>
   *
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * Same assertion as {@link Assert#isNotIn(Object...)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isNotIn("2002-12-17", "2002-12-19");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isNotIn("2002-12-17", "2002-12-18");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param datesAsString the given Dates represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual is in given Dates represented as String.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isNotIn(String... datesAsString) {
    Date[] dates = new Date[datesAsString.length];
    for (int i = 0; i < datesAsString.length; i++) {
      dates[i] = parse(datesAsString[i]);
    }
    return isNotIn((Object[]) dates);
  }

  /**
   * Same assertion as {@link Assert#isNotIn(Iterable)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isNotInWithStringDateCollection(Arrays.asList("2002-12-17", "2002-12-19"));
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isNotInWithStringDateCollection(Arrays.asList("2002-12-17", "2002-12-18"));</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
    dates.assertIsBefore(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isBefore(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBefore("2002-12-19");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isBefore("2002-12-17");
   * assertThat(theTwoTowers.getReleaseDate()).isBefore("2002-12-18");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * Verifies that the actual {@code Date} is before or equals to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> SimpleDateFormat dateFormat = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
   * 
   * // assertions will pass
   * assertThat(dateFormat.parse(&quot;1990-12-01&quot;)).isBeforeOrEqualsTo(dateFormat.parse(&quot;2000-12-01&quot;));
   * assertThat(dateFormat.parse(&quot;2000-12-01&quot;)).isBeforeOrEqualsTo(dateFormat.parse(&quot;2000-12-01&quot;));
   * 
   * // assertion will fail
   * assertThat(dateFormat.parse(&quot;2000-12-01&quot;)).isBeforeOrEqualsTo(dateFormat.parse(&quot;1990-12-01&quot;));</code></pre>
   *
   * @param other the given Date.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not before or equals to the given one.
   */
  public SELF isBeforeOrEqualsTo(Date other) {
    dates.assertIsBeforeOrEqualsTo(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isBeforeOrEqualsTo(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeOrEqualsTo("2002-12-19");
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeOrEqualsTo("2002-12-18");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isBeforeOrEqualsTo("2002-12-17");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not before or equals to the given Date represented as
   *           String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isBeforeOrEqualsTo(String dateAsString) {
    return isBeforeOrEqualsTo(parse(dateAsString));
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
    dates.assertIsAfter(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isAfter(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isAfter("2002-12-17");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isAfter("2002-12-18");
   * assertThat(theTwoTowers.getReleaseDate()).isAfter("2002-12-19");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * Verifies that the actual {@code Date} is after or equals to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> SimpleDateFormat dateFormat = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
   * 
   * // assertions will pass
   * assertThat(dateFormat.parse(&quot;2000-12-01&quot;)).isAfterOrEqualsTo(dateFormat.parse(&quot;1990-12-01&quot;));
   * assertThat(dateFormat.parse(&quot;2000-12-01&quot;)).isAfterOrEqualsTo(dateFormat.parse(&quot;2000-12-01&quot;));
   * 
   * // assertion will fail
   * assertThat(dateFormat.parse(&quot;1990-12-01&quot;)).isAfterOrEqualsTo(dateFormat.parse(&quot;2000-12-01&quot;));</code></pre>
   *
   * @param other the given Date.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not after or equals to the given one.
   */
  public SELF isAfterOrEqualsTo(Date other) {
    dates.assertIsAfterOrEqualsTo(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isAfterOrEqualsTo(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isAfterOrEqualsTo("2002-12-17");
   * assertThat(theTwoTowers.getReleaseDate()).isAfterOrEqualsTo("2002-12-18");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isAfterOrEqualsTo("2002-12-19");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not after or equals to the given Date represented as
   *           String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public SELF isAfterOrEqualsTo(String dateAsString) {
    return isAfterOrEqualsTo(parse(dateAsString));
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
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBetween("2002-12-17", "2002-12-19");
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isBetween("2002-12-15", "2002-12-17");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
    dates.assertIsBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    return myself;
  }

  /**
   * Same assertion as {@link #isBetween(Date, Date, boolean, boolean)}but given date is represented as String either
   * with one of the supported defaults date format or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isBetween("2002-12-17", "2002-12-18", false, true);
   * assertThat(theTwoTowers.getReleaseDate()).isBetween("2002-12-18", "2002-12-19", true, false);
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isBetween("2002-12-17", "2002-12-18", false, false);</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
    dates.assertIsBetween(info, actual, parse(start), parse(end), inclusiveStart, inclusiveEnd);
    return myself;
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
    dates.assertIsNotBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    return myself;
  }

  /**
   * Same assertion as {@link #isNotBetween(Date, Date, boolean, boolean)} but given date is represented as String
   * either with one of the supported defaults date format or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * // theTwoTowers release date : 2002-12-18
   * assertThat(theTwoTowers.getReleaseDate()).isNotBetween("2002-12-17", "2002-12-18", false, false);
   *
   * // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isNotBetween("2002-12-17", "2002-12-18", false, true);
   * assertThat(theTwoTowers.getReleaseDate()).isNotBetween("2002-12-18", "2002-12-19", true, false);</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public SELF isNotBetween(Date start, Date end) {
    return isNotBetween(start, end, true, false);
  }

  /**
   * Same assertion as {@link #isNotBetween(Date, Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(theFellowshipOfTheRing.getReleaseDate()).isNotBetween("2002-12-01", "2002-12-10");
   *
   * // assertion will fail
   * assertThat(theFellowshipOfTheRing.getReleaseDate()).isNotBetween("2002-12-01", "2002-12-19");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
    dates.assertIsInThePast(info, actual);
    return myself;
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
    dates.assertIsToday(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Date} is strictly in the future.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will fail
   * assertThat(theTwoTowers.getReleaseDate()).isInTheFuture();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in the future.
   */
  public SELF isInTheFuture() {
    dates.assertIsInTheFuture(info, actual);
    return myself;
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
    dates.assertIsBeforeYear(info, actual, year);
    return myself;
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
    dates.assertIsAfterYear(info, actual, year);
    return myself;
  }

  /**
   * Verifies that the actual {@code Date} year is equal to the given year.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
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
    dates.assertHasYear(info, actual, year);
    return myself;
  }

  /**
   * @deprecated use {@link #hasYear(int)} instead.
   * @param year the year to compare actual year to
   * @return this assertion object.
   */
  @Deprecated
  public SELF isWithinYear(int year) {
    dates.assertHasYear(info, actual, year);
    return myself;
  }

  /**
   * Verifies that the actual {@code Date} month is equal to the given month, <b>month value starting at 1</b>
   * (January=1, February=2, ...).
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
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
    dates.assertHasMonth(info, actual, month);
    return myself;
  }

  /**
   * @deprecated use {@link #hasMonth(int)} instead.
   * @param month the month to compare actual month to, <b>month value starting at 1</b> (January=1, February=2, ...).
   * @return this assertion object.
   */
  @Deprecated
  public SELF isWithinMonth(int month) {
    dates.assertHasMonth(info, actual, month);
    return myself;
  }

  /**
   * Verifies that the actual {@code Date} day of month is equal to the given day of month.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
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
    dates.assertHasDayOfMonth(info, actual, dayOfMonth);
    return myself;
  }

  /**
   * @deprecated use {@link #hasDayOfMonth(int)} instead.
   * @param dayOfMonth the day of month to compare actual day of month to
   * @return this assertion object.
   */
  @Deprecated
  public SELF isWithinDayOfMonth(int dayOfMonth) {
    dates.assertHasDayOfMonth(info, actual, dayOfMonth);
    return myself;
  }

  /**
   * Verifies that the actual {@code Date} day of week is equal to the given day of week (see
   * {@link Calendar#DAY_OF_WEEK} for valid values).
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
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
    dates.assertHasDayOfWeek(info, actual, dayOfWeek);
    return myself;
  }

  /**
   * @deprecated use {@link #hasDayOfWeek(int)} instead.
   * @param dayOfWeek the day of week to compare actual day of week to, see {@link Calendar#DAY_OF_WEEK} for valid
   *          values
   * @return this assertion object.
   */
  @Deprecated
  public SELF isWithinDayOfWeek(int dayOfWeek) {
    dates.assertHasDayOfWeek(info, actual, dayOfWeek);
    return myself;
  }

  /**
   * Verifies that the actual {@code Date} hour of day is equal to the given hour of day (24-hour clock).
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
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
    dates.assertHasHourOfDay(info, actual, hourOfDay);
    return myself;
  }

  /**
   * @deprecated use {@link #hasHourOfDay(int)} instead.
   * @param hourOfDay the hour of day to compare actual hour of day to (24-hour clock)
   * @return this assertion object.
   */
  @Deprecated
  public SELF isWithinHourOfDay(int hourOfDay) {
    dates.assertHasHourOfDay(info, actual, hourOfDay);
    return myself;
  }

  /**
   * Verifies that the actual {@code Date} minute is equal to the given minute.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
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
    dates.assertHasMinute(info, actual, minute);
    return myself;
  }

  /**
   * @deprecated use {@link #hasMinute(int)} instead.
   * @param minute the minute to compare actual minute to
   * @return this assertion object.
   */
  @Deprecated
  public SELF isWithinMinute(int minute) {
    dates.assertHasMinute(info, actual, minute);
    return myself;
  }

  /**
   * Verifies that the actual {@code Date} second is equal to the given second.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
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
    dates.assertHasSecond(info, actual, second);
    return myself;
  }

  /**
   * @deprecated use {@link #hasSecond(int)} instead.
   * @param second the second to compare actual second to
   * @return this assertion object.
   */
  @Deprecated
  public SELF isWithinSecond(int second) {
    dates.assertHasSecond(info, actual, second);
    return myself;
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
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   *
   * @param millisecond the millisecond to compare actual millisecond to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} millisecond is not equal to the given millisecond.
   */
  public SELF hasMillisecond(int millisecond) {
    dates.assertHasMillisecond(info, actual, millisecond);
    return myself;
  }

  /**
   * @deprecated use {@link #hasMillisecond(int)} instead.
   * @param millisecond the millisecond to compare actual millisecond to
   * @return this assertion object.
   */
  @Deprecated
  public SELF isWithinMillisecond(int millisecond) {
    dates.assertHasMillisecond(info, actual, millisecond);
    return myself;
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
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same year.
   */
  public SELF isInSameYearAs(Date other) {
    dates.assertIsInSameYearAs(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isInSameYearAs(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parse("2003-04-26");
   * assertThat(date1).isInSameYearAs("2003-05-27");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same month.
   */
  public SELF isInSameMonthAs(Date other) {
    dates.assertIsInSameMonthAs(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isInSameMonthAs(Date)}but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parse("2003-04-26");
   * assertThat(date1).isInSameMonthAs("2003-04-27");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * 
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same day of month.
   */
  public SELF isInSameDayAs(Date other) {
    dates.assertIsInSameDayAs(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isInSameDayAs(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T23:17:00");
   * assertThat(date1).isInSameDayAs("2003-04-26");</code></pre>
   * 
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
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
   * Verifies that actual and given {@code Date} are chronologically in the same hour (i.e. their time difference &lt;= 1
   * hour).
   * <p>
   * This assertion succeeds as time difference is exactly = 1h:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T13:00:00");
   * Date date2 = parseDatetime("2003-04-26T14:00:00");
   * assertThat(date1).isInSameHourWindowAs(date2);</code></pre>
   * 
   * Two dates can have different hour fields and yet be in the same chronological hour, example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T13:00:00");
   * Date date2 = parseDatetime("2003-04-26T12:59:59");
   * // succeeds as time difference == 1s
   * assertThat(date1).isInSameHourWindowAs(date2);</code></pre>
   * 
   * This assertion fails as time difference is more than one hour:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-04-26T13:00:00");
   * Date date2 = parseDatetime("2003-04-26T14:00:01");
   * assertThat(date1).isInSameHourWindowAs(date2);</code></pre>
   * 
   * To compare date's hour fields only (without day, month and year), you can write :
   * <pre><code class='java'> assertThat(myDate).isWithinHour(hourOfDayOf(otherDate));</code></pre>
   * 
   * see {@link org.assertj.core.util.DateUtil#hourOfDayOf(java.util.Date) hourOfDayOf} to get the hour of a given Date.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}).
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same hour.
   */
  public SELF isInSameHourWindowAs(Date other) {
    dates.assertIsInSameHourWindowAs(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isInSameHourWindowAs(java.util.Date)} but given date is represented as String either
   * with one of the supported defaults date format or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * Verifies that actual and given {@code Date} have same hour, day, month and year fields values.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-01-01T12:00:00");
   * Date date2 = parseDatetime("2003-01-01T12:30:00");
   *
   * // succeeds
   * assertThat(date1).isInSameHourAs(date2);</code></pre>
   * 
   * <b>This assertion does not make a true chronological comparison</b> since two dates can have different hour fields
   * and yet be in the same chronological hour, e.g:
   * 
   * <pre><code class='java'> // dates in the same hour time window but with different hour fields
   * Date date1 = parseDatetime("2003-01-01T12:00:00");
   * Date date2 = parseDatetime("2003-01-01T11:59:00");</code></pre>
   * 
   * If you want to assert that two dates are chronologically in the same hour time window use
   * {@link #isInSameHourWindowAs(java.util.Date) isInSameHourWindowAs} assertion (note that if
   * <code>isInSameHourAs</code> succeeds then <code>isInSameHourWindowAs</code> will succeed too).
   * <p>
   * If you want to compare hour only (without day, month and year), you could write :
   * <code>assertThat(myDate).isWithinHour(hourOfDayOf(otherDate))</code><br>
   * see {@link org.assertj.core.util.DateUtil#hourOfDayOf(Date)} to get the hour of a given Date.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same hour.
   */
  public SELF isInSameHourAs(Date other) {
    dates.assertIsInSameHourAs(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isInSameHourAs(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same hour.
   */
  public SELF isInSameHourAs(String dateAsString) {
    return isInSameHourAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same minute (i.e. their time difference &lt;= 1
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
   * // succeeds as time difference == 1s even though minutes fields differ
   * assertThat(date1).isInSameMinuteWindowAs(date3);</code></pre>
   * 
   * This assertion fails as time difference is &gt;= one minute:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-01-01T12:01:00");
   * Date date2 = parseDatetime("2003-01-01T12:02:00");
   *
   * // fails, time difference should hae been &lt; 1 min
   * assertThat(date1).isInSameMinuteWindowAs(date2); // ERROR</code></pre>
   * 
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}).
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same minute.
   */
  public SELF isInSameMinuteWindowAs(Date other) {
    dates.assertIsInSameMinuteWindowAs(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isInSameMinuteWindowAs(Date)} but given date is represented as String either with one of
   * the supported defaults date format or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * Verifies that actual and given {@code Date} have same minute, same hour, day, month and year fields values.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = parseDatetime("2003-01-01T12:01:00");
   * Date date2 = parseDatetime("2003-01-01T12:01:30");
   *
   * // succeeds because the all the fields up to minutes are the same
   * assertThat(date1).isInSameMinuteAs(date2);</code></pre>
   * 
   * <b>It does not make a true chronological comparison</b> since two dates can have different minute fields and yet be
   * in the same chronological minute, e.g:
   * <pre><code class='java'> // dates in the same minute time window but with different minute fields
   * Date date1 = parseDatetime("2003-01-01T12:01:00");
   * Date date3 = parseDatetime("2003-01-01T12:00:59");
   *
   * // fails because minutes fields differ even though time difference is only 1s !
   * assertThat(date1).isInSameMinuteAs(date3); // ERROR</code></pre>
   * 
   * If you want to assert that two dates are in the same minute time window use
   * {@link #isInSameMinuteWindowAs(java.util.Date) isInSameMinuteWindowAs} assertion (note that if
   * <code>isInSameMinuteAs</code> succeeds then <code>isInSameMinuteWindowAs</code> will succeed too).
   * <p>
   * If you want to compare minute field only (without hour, day, month and year), you could write :
   * <code>assertThat(myDate).hasMinute(minuteOf(otherDate))</code><br>
   * using {@link org.assertj.core.util.DateUtil#minuteOf(Date)} to get the minute of a given Date.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}).
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same minute.
   */
  public SELF isInSameMinuteAs(Date other) {
    dates.assertIsInSameMinuteAs(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isInSameMinuteAs(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same minute.
   */
  public SELF isInSameMinuteAs(String dateAsString) {
    return isInSameMinuteAs(parse(dateAsString));
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
   * assertThat(date1).isInSameSecondWindowAs(date2); // ERROR
   *
   * Date date3 = parseDatetimeWithMs("2003-04-26T13:01:02.001");
   * // fails as time difference &gt; 1s
   * assertThat(date1).isInSameSecondWindowAs(date3); // ERROR</code></pre>
   * 
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same second.
   */
  public SELF isInSameSecondWindowAs(Date other) {
    dates.assertIsInSameSecondWindowAs(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isInSameSecondWindowAs(Date)} but given date is represented as String either with one of
   * the supported defaults date format or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
   * Verifies that actual and given {@code Date} have same second, minute, hour, day, month and year fields values.
   * 
   * <pre><code class='java'> Date date1 = parseDatetimeWithMs("2003-01-01T12:00:01.000");
   * Date date2 = parseDatetimeWithMs("2003-01-01T12:00:01.250");
   *
   * // succeeds because the all the time fields up to seconds are the same
   * assertThat(date1).isInSameSecondAs(date2);</code></pre>
   * 
   * <b>It does not make a true chronological comparison</b> since two dates can have different second fields and yet
   * be
   * in the same chronological second, e.g:
   * <pre><code class='java'> Date date1 = parseDatetimeWithMs("2003-01-01T12:00:01.000");
   * Date date3 = parseDatetimeWithMs("2003-01-01T12:00:00.999");
   *
   * // fails because seconds fields differ even though time difference is only 1ms !
   * assertThat(date1).isInSameSecondAs(date3); // ERROR</code></pre>
   * 
   * If you want to assert that two dates are in the same second time window use
   * {@link #isInSameSecondWindowAs(java.util.Date) isInSameSecondWindowAs} assertion.
   * <p>
   * If you want to compare second fields only (without minute, hour, day, month and year), you could write :
   * <code>assertThat(myDate).hasSecond(secondOf(otherDate))</code><br>
   * using {@link org.assertj.core.util.DateUtil#secondOf(Date)} to get the second of a given Date.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}).
   *
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same second.
   */
  public SELF isInSameSecondAs(Date other) {
    dates.assertIsInSameSecondAs(info, actual, other);
    return myself;
  }

  /**
   * Same assertion as {@link #isInSameSecondAs(Date)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   * @param dateAsString the given Date represented as String.
   * @return this assertion object.
   */
  public SELF isInSameSecondAs(String dateAsString) {
    return isInSameSecondAs(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is close to the other date by less than delta (expressed in milliseconds),
   * if
   * difference is equals to delta it's ok.
   * <p>
   * One can use handy {@link TimeUnit} to convert a duration in milliseconds, for example you can express a delta of 5
   * seconds with <code>TimeUnit.SECONDS.toMillis(5)</code>.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * <p>
   * Example:
   * <pre><code class='java'> Date date1 = new Date();
   * Date date2 = new Date(date1.getTime() + 100);
   *
   * // assertion will pass
   * assertThat(date1).isCloseTo(date2, 80);
   * assertThat(date1).isCloseTo(date2, 100);
   *
   * // assertion will fail
   * assertThat(date1).isCloseTo(date2, 101);</code></pre>
   *
   * @param other the date to compare actual to
   * @param deltaInMilliseconds the delta used for date comparison, expressed in milliseconds
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} week is not close to the given date by less than delta.
   */
  public SELF isCloseTo(Date other, long deltaInMilliseconds) {
    dates.assertIsCloseTo(info, actual, other, deltaInMilliseconds);
    return myself;
  }

  /**
   * Same assertion as {@link #isCloseTo(Date, long)} but given date is represented as String either with one of the
   * supported defaults date format or a user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   *
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @param deltaInMilliseconds the delta used for date comparison, expressed in milliseconds
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} week is not close to the given date by less than delta.
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
    dates.assertHasTime(info, actual, timestamp);
    return myself;
  }

  /**
   * Verifies that the actual {@code Date} has the same time as the given date, useful to compare {@link Date} and
   * {@link Timestamp}.
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
    dates.hasSameTimeAs(info, actual, date);
    return myself;
  }

  /**
   * Verifies that the actual {@code Date} represents the same time as the given date in {@code String} format.
   * <p>
   * It is the same assertion as {@link #hasSameTimeAs(Date)} but given date is represented as String either with one of
   * the supported default date formats or a user custom date format (set with method
   * {@link #withDateFormat(DateFormat)}).
   * <p>
   * Beware that the default formats are expressed in the current local time zone.
   * <p>
   * Example:
   * <pre><code class='java'> Date date = parseDatetime("2003-04-26T12:00:00");
   *
   * // assertion will pass
   * assertThat(date).hasSameTimeAs("2003-04-26T12:00:00");
   *
   * // assertion will fail
   * assertThat(date).hasSameTimeAs("2003-04-26T12:00:01");
   * assertThat(date).hasSameTimeAs("2003-04-27T12:00:00");</code></pre>
   * 
   * Default date formats (expressed in the local time zone) are:
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
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
    dates.hasSameTimeAs(info, actual, parse(dateAsString));
    return myself;
  }

  /**
   * Instead of using default date formats for the date String based Date assertions like {@link #isEqualTo(String)},
   * AssertJ is gonna use any date formats registered with one of these methods :
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
   * AssertJ is gonna use any date formats registered with one of these methods :
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
    checkNotNull(userCustomDateFormatPattern, DATE_FORMAT_PATTERN_SHOULD_NOT_BE_NULL);
    return withDateFormat(new SimpleDateFormat(userCustomDateFormatPattern));
  }

  /**
   * Instead of using default strict date/time parsing, it is possible to use lenient parsing mode for default date
   * formats parser to interpret inputs that do not precisely match supported date formats (lenient parsing).
   * <p>
   * With strict parsing, inputs must match exactly date/time format.
   * <p>
   * Example:
   * <pre><code class='java'> final Date date = Dates.parse("2001-02-03");
   * final Date dateTime = parseDatetime("2001-02-03T04:05:06");
   * final Date dateTimeWithMs = parseDatetimeWithMs("2001-02-03T04:05:06.700");
   *
   * AbstractDateAssert.setLenientDateParsing(true);
   *
   * // assertions will pass
   * assertThat(date).isEqualTo("2001-01-34");
   * assertThat(date).isEqualTo("2001-02-02T24:00:00");
   * assertThat(date).isEqualTo("2001-02-04T-24:00:00.000");
   * assertThat(dateTime).isEqualTo("2001-02-03T04:05:05.1000");
   * assertThat(dateTime).isEqualTo("2001-02-03T04:04:66");
   * assertThat(dateTimeWithMs).isEqualTo("2001-02-03T04:05:07.-300");
   *
   * // assertions will fail
   * assertThat(date).hasSameTimeAs("2001-02-04"); // different date
   * assertThat(dateTime).hasSameTimeAs("2001-02-03 04:05:06"); // leniency does not help here</code></pre>
   *
   * To revert to default strict date parsing, call {@code setLenientDateParsing(false)}.
   *
   * @param value whether lenient parsing mode should be enabled or not
   */
  public static void setLenientDateParsing(boolean value) {
    for (DateFormat defaultDateFormat : DEFAULT_DATE_FORMATS) {
      defaultDateFormat.setLenient(value);
    }
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link #isEqualTo(String)}.
   * <p>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p>
   * AssertJ is gonna use any date formats registered with one of these methods :
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
   *   // date with a custom format : failure since the default formats don't match.
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
    checkNotNull(userCustomDateFormat, DATE_FORMAT_SHOULD_NOT_BE_NULL);
    userDateFormats.get().add(userCustomDateFormat);
  }

  /**
   * Add the given date format to the ones used to parse date String in String based Date assertions like
   * {@link #isEqualTo(String)}.
   * <p>
   * User date formats are used before default ones in the order they have been registered (first registered, first
   * used).
   * <p>
   * AssertJ is gonna use any date formats registered with one of these methods :
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
   *   // date with a custom format : failure since the default formats don't match.
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
    checkNotNull(userCustomDateFormatPattern, DATE_FORMAT_PATTERN_SHOULD_NOT_BE_NULL);
    registerCustomDateFormat(new SimpleDateFormat(userCustomDateFormatPattern));
  }

  /**
   * Remove all registered custom date formats =&gt; use only the defaults date formats to parse string as date.
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
   * <li><code>2003-04-26</code></li>
   * </ul>
   */
  public static void useDefaultDateFormatsOnly() {
    userDateFormats.get().clear();
  }

  /**
   * Remove all registered custom date formats =&gt; use only the defaults date formats to parse string as date.
   * <p>
   * Beware that the default formats are expressed in the current local timezone.
   * <p>
   * Defaults date format (expressed in the local time zone) are :
   * <ul>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd HH:mm:ss.SSS</code></li>
   * <li><code>yyyy-MM-dd'T'HH:mm:ss</code></li>
   * <li><code>yyyy-MM-dd</code></li>
   * </ul>
   * <p>
   * Example of valid string date representations:
   * <ul>
   * <li><code>2003-04-26T03:01:02.999</code></li>
   * <li><code>2003-04-26 03:01:02.999</code></li>
   * <li><code>2003-04-26T13:01:02</code></li>
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
   * Thread safe utility method to parse a Date with {@link #userDateFormats} first, then {@link #DEFAULT_DATE_FORMATS}.
   * <p>
   * Returns <code>null</code> if dateAsString parameter is <code>null</code>.
   *
   * @param dateAsString the string to parse as a Date with {@link #userDateFormats}
   * @return the corresponding Date, null if dateAsString parameter is null.
   * @throws AssertionError if the string can't be parsed as a Date
   */
  @VisibleForTesting
  Date parse(String dateAsString) {
    if (dateAsString == null) return null;
    // parse with date format specified by user if any, otherwise use default formats
    // no synchronization needed as userCustomDateFormat is thread local
    Date date = parseDateWith(dateAsString, userDateFormats.get());
    if (date != null) return date;
    // no matching user date format, let's try default format
    date = parseDateWithDefaultDateFormats(dateAsString);
    if (date != null) return date;
    // no matching date format, throw an error
    throw new AssertionError(String.format("Failed to parse %s with any of these date formats:%n   %s", dateAsString,
                                           info.representation().toStringOf(dateFormatsInOrderOfUsage())));
  }

  private Date parseDateWithDefaultDateFormats(final String dateAsString) {
    synchronized (DEFAULT_DATE_FORMATS) {
      return parseDateWith(dateAsString, DEFAULT_DATE_FORMATS);
    }
  }

  private List<DateFormat> dateFormatsInOrderOfUsage() {
    List<DateFormat> allDateFormatsInOrderOfUsage = newArrayList(userDateFormats.get());
    allDateFormatsInOrderOfUsage.addAll(DEFAULT_DATE_FORMATS);
    return allDateFormatsInOrderOfUsage;
  }

  private Date parseDateWith(final String dateAsString, final Collection<DateFormat> dateFormats) {
    for (DateFormat defaultDateFormat : dateFormats) {
      try {
        return defaultDateFormat.parse(dateAsString);
      } catch (ParseException e) {
        // ignore and try next date format
      }
    }
    return null;
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Date> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Date> customComparator, String customComparatorDescription) {
    this.dates = new Dates(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    this.dates = Dates.instance();
    return super.usingDefaultComparator();
  }

}
