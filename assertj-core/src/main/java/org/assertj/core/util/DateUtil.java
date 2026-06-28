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
package org.assertj.core.util;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.util.Preconditions.checkArgument;

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

    if (daysDiff > 0) result.append("%dd".formatted(daysDiff));

    if (hourFieldDiff > 0) {
      if (daysDiff > 0 && minuteFieldDiff == 0 && secondFieldDiff == 0 && millisecondsFieldDiff == 0) {
        // hour diff field is the last field that differs but not the only one
        result.append(" and ");
      } else if (daysDiff > 0) {
        result.append(" ");
      }
      result.append("%dh".formatted(hourFieldDiff));
    }

    if (minuteFieldDiff > 0) {
      final boolean notFirstDiff = daysDiff > 0 || hourFieldDiff > 0;
      if (notFirstDiff && secondFieldDiff == 0 && millisecondsFieldDiff == 0) {
        // min diff field is the last field that differs but not the only one
        result.append(" and ");
      } else if (notFirstDiff) {
        result.append(" ");
      }
      result.append("%dm".formatted(minuteFieldDiff));
    }

    if (secondFieldDiff > 0) {
      final boolean notFirstDiff = daysDiff > 0 || hourFieldDiff > 0 || minuteFieldDiff > 0;
      if (notFirstDiff && millisecondsFieldDiff == 0) {
        // seconds diff field is the last field that differs but not the only one
        result.append(" and ");
      } else if (notFirstDiff) {
        result.append(" ");
      }
      result.append("%ds".formatted(secondFieldDiff));
    }

    if (millisecondsFieldDiff > 0) {
      if (!result.isEmpty()) result.append(" and ");
      result.append("%dms".formatted(millisecondsFieldDiff));
    }

    return result.toString();

  }
}
