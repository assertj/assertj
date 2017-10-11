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
package org.assertj.core.util;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility methods related to dates.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class DateUtil {

  /**
   * ISO 8601 date format (yyyy-MM-dd), example : <code>2003-04-23</code>
   */
  private static final DateFormat ISO_DATE_FORMAT = newIsoDateFormat();
  /**
   * ISO 8601 local date-time format (yyyy-MM-dd'T'HH:mm:ss), example : <code>2003-04-26T13:01:02</code>
   */
  private static final DateFormat ISO_DATE_TIME_FORMAT = newIsoDateTimeFormat();
  /**
   * ISO 8601 local date-time format with millisecond (yyyy-MM-dd'T'HH:mm:ss.SSS), example :
   * <code>2003-04-26T03:01:02.999</code>
   */
  private static final DateFormat ISO_DATE_TIME_FORMAT_WITH_MS = newIsoDateTimeWithMsFormat();

  /**
   * ISO 8601 date format (yyyy-MM-dd), example : <code>2003-04-23</code>
   * @return a {@code yyyy-MM-dd} {@link DateFormat}
   */
  public static DateFormat newIsoDateFormat() {
    return strictDateFormatForPattern("yyyy-MM-dd");
  }

  /**
   * ISO 8601 date-time format (yyyy-MM-dd'T'HH:mm:ss), example : <code>2003-04-26T13:01:02</code>
   * @return a {@code yyyy-MM-dd'T'HH:mm:ss} {@link DateFormat}
   */
  public static DateFormat newIsoDateTimeFormat() {
    return strictDateFormatForPattern("yyyy-MM-dd'T'HH:mm:ss");
  }

  /**
   * ISO 8601 date-time format with millisecond (yyyy-MM-dd'T'HH:mm:ss.SSS), example :
   * <code>2003-04-26T03:01:02.999</code>
   * @return a {@code yyyy-MM-dd'T'HH:mm:ss.SSS} {@link DateFormat}
   */
  public static DateFormat newIsoDateTimeWithMsFormat() {
    return strictDateFormatForPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
  }

  /**
   * {@link Timestamp} date-time format with millisecond (yyyy-MM-dd HH:mm:ss.SSS), example :
   * <code>2003-04-26 03:01:02.999</code>
   * @return a {@code yyyy-MM-dd HH:mm:ss.SSS} {@link DateFormat}
   */
  public static DateFormat newTimestampDateFormat() {
    return strictDateFormatForPattern("yyyy-MM-dd HH:mm:ss.SSS");
  }

  private static DateFormat strictDateFormatForPattern(String pattern) {
    DateFormat dateFormat = new SimpleDateFormat(pattern);
    dateFormat.setLenient(false);
    return dateFormat;
  }

  /**
   * Formats the given date using the ISO 8601 date-time format (yyyy-MM-dd'T'HH:mm:ss).<br>
   * Method is synchronized
   * because SimpleDateFormat is not thread safe (sigh).
   * <p>
   * Returns null if given the date is null.
   *
   * @param date the date to format.
   * @return the formatted date or null if given the date was null.
   */
  public static synchronized String formatAsDatetime(Date date) {
    return date == null ? null : ISO_DATE_TIME_FORMAT.format(date);
  }

  /**
   * Formats the given date using the ISO 8601 date-time format with millisecond (yyyy-MM-dd'T'HH:mm:ss:SSS).<br>
   * Method
   * is synchronized because SimpleDateFormat is not thread safe (sigh).
   * <p>
   * Returns null if given the date is null.
   *
   * @param date the date to format.
   * @return the formatted date or null if given the date was null.
   */
  public static synchronized String formatAsDatetimeWithMs(Date date) {
    return date == null ? null : ISO_DATE_TIME_FORMAT_WITH_MS.format(date);
  }

  /**
   * Formats the date of the given calendar using the ISO 8601 date-time format (yyyy-MM-dd'T'HH:mm:ss).<br> Method is
   * thread safe.
   * <p>
   * Returns null if the given calendar is null.
   *
   * @param calendar the calendar to format.
   * @return the formatted calendar or null if the given calendar was null.
   */
  public static String formatAsDatetime(Calendar calendar) {
    return calendar == null ? null : formatAsDatetime(calendar.getTime());
  }

  /**
   * Utility method to parse a Date following {@link #ISO_DATE_FORMAT}, returns null if the given String is null.
   *
   * @param dateAsString the string to parse as a Date following {@link #ISO_DATE_FORMAT}
   * @return the corresponding Date or null if the given String is null.
   * @throws RuntimeException encapsulating ParseException if the string can't be parsed as a Date
   */
  public static synchronized Date parse(String dateAsString) {
    try {
      return dateAsString == null ? null : ISO_DATE_FORMAT.parse(dateAsString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Utility method to parse a Date following {@link #ISO_DATE_TIME_FORMAT}, returns null if the given String is null.
   * <p> 
   * Example:
   * <pre><code class='java'> Date date = parseDatetime("2003-04-26T03:01:02");</code></pre>
   *
   * @param dateAsString the string to parse as a Date following {@link #ISO_DATE_TIME_FORMAT}
   * @return the corresponding Date with time details or null if the given String is null.
   * @throws RuntimeException encapsulating ParseException if the string can't be parsed as a Date
   */
  public static synchronized Date parseDatetime(String dateAsString) {
    try {
      return dateAsString == null ? null : ISO_DATE_TIME_FORMAT.parse(dateAsString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Utility method to parse a Date following {@link #ISO_DATE_TIME_FORMAT_WITH_MS}, returns null if the given String is
   * null.
   * <p>
   * Example:
   * <pre><code class='java'> Date date = parseDatetimeWithMs("2003-04-26T03:01:02.999");</code></pre>
   *
   * @param dateAsString the string to parse as a Date following {@link #ISO_DATE_TIME_FORMAT_WITH_MS}
   * @return the corresponding Date with time details or null if the given String is null.
   * @throws RuntimeException encapsulating ParseException if the string can't be parsed as a Date
   */
  public static synchronized Date parseDatetimeWithMs(String dateAsString) {
    try {
      return dateAsString == null ? null : ISO_DATE_TIME_FORMAT_WITH_MS.parse(dateAsString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Converts the given Date to Calendar, returns null if the given Date is null.
   *
   * @param date the date to convert to a Calendar.
   * @return the Calendar corresponding to the given Date or null if the given Date is null.
   */
  public static Calendar toCalendar(Date date) {
    if (date == null) {
      return null;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }

  /**
   * Extracts the year of the given Date.
   *
   * @param date the date to extract the year from - must not be null.
   * @return the year of the given Date
   * @throws NullPointerException if given Date is null
   */
  public static int yearOf(Date date) {
    return toCalendar(date).get(Calendar.YEAR);
  }

  /**
   * Dates Extracts the month of the given Date <b>starting at 1</b> (January=1, February=2, ...).
   *
   * @param date the date to extract the month from - must not be null.
   * @return the month of the given Date <b>starting at 1</b> (January=1, February=2, ...)
   * @throws NullPointerException if given Date is null
   */
  public static int monthOf(Date date) {
    return toCalendar(date).get(Calendar.MONTH) + 1; // based 1 month (January=1)
  }

  /**
   * Dates Extracts the day of month of the given Date.
   *
   * @param date the date to extract the day of month from - must not be null.
   * @return the day of month of the given Date
   * @throws NullPointerException if given Date is null
   */
  public static int dayOfMonthOf(Date date) {
    return toCalendar(date).get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Extracts the day of week of the given Date, returned value follows {@link Calendar#DAY_OF_WEEK} .
   *
   * @param date the date to extract the day of week from - must not be null.
   * @return the day of week of the given Date
   * @throws NullPointerException if given Date is null
   */
  public static int dayOfWeekOf(Date date) {
    return toCalendar(date).get(Calendar.DAY_OF_WEEK);
  }

  /**
   * Extracts the hour of day if the given Date (24-hour clock).
   *
   * @param date the date to extract the hour of day from - must not be null.
   * @return the hour of day of the given Date (24-hour clock)
   * @throws NullPointerException if given Date is null
   */
  public static int hourOfDayOf(Date date) {
    return toCalendar(date).get(Calendar.HOUR_OF_DAY);
  }

  /**
   * Dates Extracts the minute of the given Date.
   *
   * @param date the date to extract the minute from - must not be null.
   * @return the minute of the given Date
   * @throws NullPointerException if given Date is null
   */
  public static int minuteOf(Date date) {
    return toCalendar(date).get(Calendar.MINUTE);
  }

  /**
   * Extracts the second of the given Date.
   *
   * @param date the date to extract the second from - must not be null.
   * @return the second of the given Date
   * @throws NullPointerException if given Date is null
   */
  public static int secondOf(Date date) {
    return toCalendar(date).get(Calendar.SECOND);
  }

  /**
   * Extracts the millisecond of the given Date.
   *
   * @param date the date to extract the millisecond from - must not be null.
   * @return the millisecond of the given Date
   * @throws NullPointerException if given Date is null
   */
  public static int millisecondOf(Date date) {
    return toCalendar(date).get(Calendar.MILLISECOND);
  }

  /**
   * Compute the time difference between the two given dates in milliseconds, it always gives a positive result.
   *
   * @param date1 the first date.
   * @param date2 the second date.
   * @return the difference between the two given dates in milliseconds
   * @throws IllegalArgumentException if one a the given Date is null.
   */
  public static long timeDifference(Date date1, Date date2) {
    checkArgument(date1 != null && date2 != null, "Expecting date parameter not to be null");
    return Math.abs(date1.getTime() - date2.getTime());
  }

  /**
   * Returns a copy of the given date without the time part (which is set to 00:00:00), for example :<br>
   * <code>truncateTime(2008-12-29T23:45:12)</code> will give <code>2008-12-29T00:00:00</code>.
   * <p>
   * Returns null if the given Date is null.
   *
   * @param date we want to get the day part (the parameter is read only).
   * @return the truncated date.
   */
  public static Date truncateTime(Date date) {
    if (date == null) return null;
    Calendar cal = toCalendar(date);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  public static Date now() {
    return new Date();
  }

  public static Date yesterday() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, -1);
    return cal.getTime();
  }

  public static Date tomorrow() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 1);
    return cal.getTime();
  }

  /**
   * Utility method to display a human readable time difference.
   *
   * @param date1 the first date
   * @param date2 the second date
   * @return a human readable time difference.
   */
  public static String formatTimeDifference(final Date date1, final Date date2) {

    // difference in ms, s, m, h, d
    final long millisecondsDiff = timeDifference(date1, date2);
    final long secondsDiff = millisecondsDiff / SECONDS.toMillis(1);
    final long minutesDiff = millisecondsDiff / MINUTES.toMillis(1);
    final long hoursDiff = millisecondsDiff / HOURS.toMillis(1);
    final long daysDiff = millisecondsDiff / DAYS.toMillis(1);

    // date field difference
    final long hourFieldDiff = hoursDiff - DAYS.toHours(daysDiff);
    final long minuteFieldDiff = minutesDiff - HOURS.toMinutes(hoursDiff);
    final long secondFieldDiff = secondsDiff - MINUTES.toSeconds(minutesDiff);
    final long millisecondsFieldDiff = millisecondsDiff % SECONDS.toMillis(1);

    StringBuilder result = new StringBuilder();

    if (daysDiff > 0) result.append(format("%dd", daysDiff));

    if (hourFieldDiff > 0) {
      if (daysDiff > 0 && minuteFieldDiff == 0 && secondFieldDiff == 0 && millisecondsFieldDiff == 0) {
        // hour diff field is the last field that differs but not the only one
        result.append(" and ");
      } else if (daysDiff > 0) {
        result.append(" ");
      }
      result.append(format("%dh", hourFieldDiff));
    }

    if (minuteFieldDiff > 0) {
      final boolean notFirstDiff = daysDiff > 0 || hourFieldDiff > 0;
      if (notFirstDiff && secondFieldDiff == 0 && millisecondsFieldDiff == 0) {
        // min diff field is the last field that differs but not the only one
        result.append(" and ");
      } else if (notFirstDiff) {
        result.append(" ");
      }
      result.append(format("%dm", minuteFieldDiff));
    }

    if (secondFieldDiff > 0) {
      final boolean notFirstDiff = daysDiff > 0 || hourFieldDiff > 0 || minuteFieldDiff > 0;
      if (notFirstDiff && millisecondsFieldDiff == 0) {
        // seconds diff field is the last field that differs but not the only one
        result.append(" and ");
      } else if (notFirstDiff) {
        result.append(" ");
      }
      result.append(format("%ds", secondFieldDiff));
    }

    if (millisecondsFieldDiff > 0) {
      if (result.length() > 0) result.append(" and ");
      result.append(format("%dms", millisecondsFieldDiff));
    }

    return result.toString();

  }
}
