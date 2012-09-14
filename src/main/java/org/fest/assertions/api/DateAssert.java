package org.fest.assertions.api;

import static org.fest.util.Dates.ISO_DATE_FORMAT;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.fest.assertions.core.Assert;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * 
 * Assertions for {@code Date}s.
 * <p>
 * To create a new instance of this class invoke <code>{@link Assertions#assertThat(Date)}</code>.
 * </p>
 * Note that assertions with date parameter comes with two flavor, one is obviously a {@link Date} and the other is a String
 * representing a Date.<br>
 * For the latter, the default format follows ISO 8901 : "yyyy-MM-dd", user can override it with a custom format by calling
 * {@link #withDateFormat(DateFormat)}.<br>
 * The user custom format will then be used for all next Date assertions (i.e not limited to the current assertion) in the test
 * suite.<br>
 * To turn back to default format, simply call {@link #withIsoDateFormat()}.
 * 
 * @author Tomasz Nurkiewicz (thanks for giving assertions idea)
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class DateAssert extends AbstractAssert<DateAssert, Date> {

  @VisibleForTesting
  Dates dates = Dates.instance();

  /**
   * Used in String based Date assertions - like {@link #isAfter(String)} - to convert input date represented as string to Date.<br>
   * The format used can be overriden by invoking {@link #withDateFormat(DateFormat)}
   */
  @VisibleForTesting
  static DateFormat dateFormat = ISO_DATE_FORMAT;

  /**
   * Creates a new </code>{@link DateAssert}</code>.
   * @param actual the target to verify.
   */
  protected DateAssert(Date actual) {
    super(actual, DateAssert.class);
  }

  /**
   * Same assertion as {@link AbstractAssert#isEqualTo(Object) isEqualTo(Date date)} but given Date is represented as String
   * either with ISO date format (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are not equal.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public DateAssert isEqualTo(String dateAsString) {
    return isEqualTo(parse(dateAsString));
  }

  /**
   * Same assertion as {@link AbstractAssert#isNotEqualTo(Object) isNotEqualTo(Date date)} but given Date is represented as String
   * either with ISO date format (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual and given Date represented as String are equal.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public DateAssert isNotEqualTo(String dateAsString) {
    return isNotEqualTo(parse(dateAsString));
  }

  /**
   * Same assertion as {@link Assert#isIn(Object...)} but given Dates are represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param datesAsString the given Dates represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual is not in given Dates represented as String.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public DateAssert isIn(String... datesAsString) {
    Date[] dates = new Date[datesAsString.length];
    for (int i = 0; i < datesAsString.length; i++) {
      dates[i] = parse(datesAsString[i]);
    }
    return isIn(dates);
  }

  /**
   * Same assertion as {@link Assert#isIn(Iterable)} but given Dates are represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).<br>
   * Method signature could not be <code>isIn(Collection&lt;String&gt;)</code> because it would be same signature as
   * <code>isIn(Collection&lt;Date&gt;)</code> since java collection type are erased at runtime.
   * @param datesAsString the given Dates represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual is not in given Dates represented as String.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public DateAssert isInWithStringDateCollection(Collection<String> datesAsString) {
    Collection<Date> dates = new ArrayList<Date>(datesAsString.size());
    for (String dateAsString : datesAsString) {
      dates.add(parse(dateAsString));
    }
    return isIn(dates);
  }

  /**
   * Same assertion as {@link Assert#isNotIn(Object...)} but given Dates are represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param datesAsString the given Dates represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual is in given Dates represented as String.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public DateAssert isNotIn(String... datesAsString) {
    Date[] dates = new Date[datesAsString.length];
    for (int i = 0; i < datesAsString.length; i++) {
      dates[i] = parse(datesAsString[i]);
    }
    return isNotIn(dates);
  }

  /**
   * Same assertion as {@link Assert#isNotIn(Iterable)} but given Dates are represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).<br>
   * Method signature could not be <code>isNotIn(Collection&lt;String&gt;)</code> because it would be same signature as
   * <code>isNotIn(Collection&lt;Date&gt;)</code> since java collection type are erased at runtime.
   * @param datesAsString the given Dates represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if actual is in given Dates represented as String.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public DateAssert isNotInWithStringDateCollection(Collection<String> datesAsString) {
    Collection<Date> dates = new ArrayList<Date>(datesAsString.size());
    for (String dateAsString : datesAsString) {
      dates.add(parse(dateAsString));
    }
    return isNotIn(dates);
  }

  /**
   * Verifies that the actual {@code Date} is <b>strictly</b> before the given one.
   * @param other the given Date.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly before the given one.
   */
  public DateAssert isBefore(Date other) {
    dates.assertIsBefore(info, actual, other);
    return this;
  }

  /**
   * Same assertion as {@link #isBefore(Date)} but given Date is represented as String either with ISO date format (yyyy-MM-dd) or
   * user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly before the given Date represented as String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public DateAssert isBefore(String dateAsString) {
    return isBefore(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is before or equals to the given one.
   * @param other the given Date.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not before or equals to the given one.
   */
  public DateAssert isBeforeOrEqualsTo(Date other) {
    dates.assertIsBeforeOrEqualsTo(info, actual, other);
    return this;
  }

  /**
   * Same assertion as {@link #isBeforeOrEqualsTo(Date)} but given Date is represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not before or equals to the given Date represented as String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public DateAssert isBeforeOrEqualsTo(String dateAsString) {
    return isBeforeOrEqualsTo(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is <b>strictly</b> after the given one.
   * @param other the given Date.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly after the given one.
   */
  public DateAssert isAfter(Date other) {
    dates.assertIsAfter(info, actual, other);
    return this;
  }

  /**
   * Same assertion as {@link #isAfter(Date)} but given Date is represented as String either with ISO date format (yyyy-MM-dd) or
   * user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly after the given Date represented as String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public DateAssert isAfter(String dateAsString) {
    return isAfter(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is after or equals to the given one.
   * @param other the given Date.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not after or equals to the given one.
   */
  public DateAssert isAfterOrEqualsTo(Date other) {
    dates.assertIsAfterOrEqualsTo(info, actual, other);
    return this;
  }

  /**
   * Same assertion as {@link #isAfterOrEqualsTo(Date)} but given Date is represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if given date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not after or equals to the given Date represented as String.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public DateAssert isAfterOrEqualsTo(String dateAsString) {
    return isAfterOrEqualsTo(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is in [start, end[ period (start included, end excluded).
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if start {@code Date} is {@code null}.
   * @throws NullPointerException if end {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in [start, end[ period.
   */
  public DateAssert isBetween(Date start, Date end) {
    return isBetween(start, end, true, false);
  }

  /**
   * Same assertion as {@link #isBetween(Date, Date)} but given Dates are represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if start Date as String is {@code null}.
   * @throws NullPointerException if end Date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in [start, end[ period.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public DateAssert isBetween(String start, String end) {
    return isBetween(parse(start), parse(end));
  }

  /**
   * Verifies that the actual {@code Date} is in the given period defined by start and end dates.<br>
   * To include start in the period set inclusiveStart parameter to <code>true</code>.<br>
   * To include end in the period set inclusiveEnd parameter to <code>true</code>.<br>
   * @param start the period start, expected not to be null.
   * @param end the period end, expected not to be null.
   * @param inclusiveStart wether to include start date in period.
   * @param inclusiveEnd wether to include end date in period.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start {@code Date} is {@code null}.
   * @throws NullPointerException if end {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in (start, end) period.
   */
  public DateAssert isBetween(Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    dates.assertIsBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    return this;
  }

  /**
   * Same assertion as {@link #isBetween(Date, Date, boolean, boolean)} but given Dates are represented as String either with ISO
   * date format (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param start the period start, expected not to be null.
   * @param end the period end, expected not to be null.
   * @param inclusiveStart wether to include start date in period.
   * @param inclusiveEnd wether to include end date in period.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start Date as String is {@code null}.
   * @throws NullPointerException if end Date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in (start, end) period.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public DateAssert isBetween(String start, String end, boolean inclusiveStart, boolean inclusiveEnd) {
    dates.assertIsBetween(info, actual, parse(start), parse(end), inclusiveStart, inclusiveEnd);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} is not in the given period defined by start and end dates.<br>
   * To include start in the period set inclusiveStart parameter to <code>true</code>.<br>
   * To include end in the period set inclusiveEnd parameter to <code>true</code>.<br>
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @param inclusiveStart wether to include start date in period.
   * @param inclusiveEnd wether to include end date in period.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start {@code Date} is {@code null}.
   * @throws NullPointerException if end {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in (start, end) period.
   */
  public DateAssert isNotBetween(Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    dates.assertIsNotBetween(info, actual, start, end, inclusiveStart, inclusiveEnd);
    return this;
  }

  /**
   * Same assertion as {@link #isNotBetween(Date, Date, boolean, boolean)} but given Dates are represented as String either with
   * ISO date format (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @param inclusiveStart wether to include start date in period.
   * @param inclusiveEnd wether to include end date in period.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start Date as String is {@code null}.
   * @throws NullPointerException if end Date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in (start, end) period.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public DateAssert isNotBetween(String start, String end, boolean inclusiveStart, boolean inclusiveEnd) {
    return isNotBetween(parse(start), parse(end), inclusiveStart, inclusiveEnd);
  }

  /**
   * Verifies that the actual {@code Date} is not in [start, end[ period
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if start {@code Date} is {@code null}.
   * @throws NullPointerException if end {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is in [start, end[ period.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public DateAssert isNotBetween(Date start, Date end) {
    return isNotBetween(start, end, true, false);
  }

  /**
   * Same assertion as {@link #isNotBetween(Date, Date)} but given Dates are represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param start the period start (inclusive), expected not to be null.
   * @param end the period end (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws NullPointerException if start Date as String is {@code null}.
   * @throws NullPointerException if end Date as String is {@code null}.
   * @throws AssertionError if the actual {@code Date} is in [start, end[ period.
   * @throws AssertionError if one of the given date as String could not be converted to a Date.
   */
  public DateAssert isNotBetween(String start, String end) {
    return isNotBetween(parse(start), parse(end), true, false);
  }

  /**
   * Verifies that the actual {@code Date} is strictly in the past.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in the past.
   */
  public DateAssert isInThePast() {
    dates.assertIsInThePast(info, actual);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} is today, that is matching current year, month and day (no check on hour, minute,
   * second, milliseconds).
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not today.
   */
  public DateAssert isToday() {
    dates.assertIsToday(info, actual);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} is strictly in the future.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in the future.
   */
  public DateAssert isInTheFuture() {
    dates.assertIsInTheFuture(info, actual);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} is <b>strictly</b> before the given year.
   * @param year the year to compare actual year to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} year is after or equals to the given year.
   */
  public DateAssert isBeforeYear(int year) {
    dates.assertIsBeforeYear(info, actual, year);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} is <b>strictly</b> after the given year.
   * @param year the year to compare actual year to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} year is before or equals to the given year.
   */
  public DateAssert isAfterYear(int year) {
    dates.assertIsAfterYear(info, actual, year);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} year is equal to the given year.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param year the year to compare actual year to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} year is not equal to the given year.
   */
  public DateAssert isWithinYear(int year) {
    dates.assertIsWithinYear(info, actual, year);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} month is equal to the given month, <b>month value starting at 1</b> (January=1,
   * February=2, ...).
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param month the month to compare actual month to, <b>month value starting at 1</b> (January=1, February=2, ...).
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} month is not equal to the given month.
   */
  public DateAssert isWithinMonth(int month) {
    dates.assertIsWithinMonth(info, actual, month);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} day of month is equal to the given day of month.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param dayOfMonth the day of month to compare actual day of month to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} month is not equal to the given day of month.
   */
  public DateAssert isWithinDayOfMonth(int dayOfMonth) {
    dates.assertIsWithinDayOfMonth(info, actual, dayOfMonth);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} day of week is equal to the given day of week (see {@link Calendar#DAY_OF_WEEK} for
   * valid values).
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param dayOfWeek the day of week to compare actual day of week to, see {@link Calendar#DAY_OF_WEEK} for valid values
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} week is not equal to the given day of week.
   */
  public DateAssert isWithinDayOfWeek(int dayOfWeek) {
    dates.assertIsWithinDayOfWeek(info, actual, dayOfWeek);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} hour od day is equal to the given hour of day (24-hour clock).
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param hourOfDay the hour of day to compare actual hour of day to (24-hour clock)
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} hour is not equal to the given hour.
   */
  public DateAssert isWithinHourOfDay(int hourOfDay) {
    dates.assertIsWithinHourOfDay(info, actual, hourOfDay);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} minute is equal to the given minute.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param minute the minute to compare actual minute to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} minute is not equal to the given minute.
   */
  public DateAssert isWithinMinute(int minute) {
    dates.assertIsWithinMinute(info, actual, minute);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} second is equal to the given second.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param second the second to compare actual second to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} second is not equal to the given second.
   */
  public DateAssert isWithinSecond(int second) {
    dates.assertIsWithinSecond(info, actual, second);
    return this;
  }

  /**
   * Verifies that the actual {@code Date} millisecond is equal to the given millisecond.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param millisecond the millisecond to compare actual millisecond to
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} millisecond is not equal to the given millisecond.
   */
  public DateAssert isWithinMillisecond(int millisecond) {
    dates.assertIsWithinMillisecond(info, actual, millisecond);
    return this;
  }

  /**
   * Verifies that actual and given {@code Date} are in the same year.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same year.
   */
  public DateAssert isInSameYearAs(Date other) {
    dates.assertIsInSameYearAs(info, actual, other);
    return this;
  }

  /**
   * Same assertion as {@link #isInSameYearAs(Date)} but given Date is represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given Date represented as String are not in the same year.
   * @throws AssertionError if the given date as String could not be converted to a Date.
   */
  public DateAssert isInSameYearAs(String dateAsString) {
    return isInSameYearAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same month (and thus in the same year).
   * <p>
   * If you want to compare month only (without year), use : <code>assertThat(myDate).isWithinMonth(monthOf(otherDate))</code><br>
   * See {@link org.fest.util.Dates#monthOf(Date)} to get the month of a given Date.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same month.
   */
  public DateAssert isInSameMonthAs(Date other) {
    dates.assertIsInSameMonthAs(info, actual, other);
    return this;
  }

  /**
   * Same assertion as {@link #isInSameMonthAs(Date)} but given Date is represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same month.
   */
  public DateAssert isInSameMonthAs(String dateAsString) {
    return isInSameMonthAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same day of month (and thus in the same month and
   * year).
   * <p>
   * If you want to compare day of month only (without month and year), you could write :
   * <code>assertThat(myDate).isWithinDayOfMonth(dayOfMonthOf(otherDate))</code><br>
   * see {@link org.fest.util.Dates#dayOfMonthOf(Date)} to get the day of month of a given Date.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same day of month.
   */
  public DateAssert isInSameDayAs(Date other) {
    dates.assertIsInSameDayAs(info, actual, other);
    return this;
  }

  /**
   * Same assertion as {@link #isInSameDayAs(Date)} but given Date is represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same day of month.
   */
  public DateAssert isInSameDayAs(String dateAsString) {
    return isInSameDayAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same hour (and thus in the same day, month and year).
   * <p>
   * If you want to compare hour only (without day, month and year), you could write :
   * <code>assertThat(myDate).isWithinHour(hourOfDayOf(otherDate))</code><br>
   * see {@link org.fest.util.Dates#hourOfDay(Date)} to get the hour of a given Date.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same hour.
   */
  public DateAssert isInSameHourAs(Date other) {
    dates.assertIsInSameHourAs(info, actual, other);
    return this;
  }

  /**
   * Same assertion as {@link #isInSameHourAs(Date)} but given Date is represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same hour.
   */
  public DateAssert isInSameHourAs(String dateAsString) {
    return isInSameHourAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same minute (and thus in the same hour, day, month and
   * year).
   * <p>
   * If you want to compare minute only (without hour, day, month and year), you could write :
   * <code>assertThat(myDate).isWithinMinute(minuteOf(otherDate))</code><br>
   * see {@link org.fest.util.Dates#minuteOf(Date)} to get the minute of a given Date.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same minute.
   */
  public DateAssert isInSameMinuteAs(Date other) {
    dates.assertIsInSameMinuteAs(info, actual, other);
    return this;
  }

  /**
   * Same assertion as {@link #isInSameMinuteAs(Date)} but given Date is represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same minute.
   */
  public DateAssert isInSameMinuteAs(String dateAsString) {
    return isInSameMinuteAs(parse(dateAsString));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same second (and thus in the same minute, hour, day,
   * month and year).
   * <p>
   * If you want to compare second only (without minute, hour, day, month and year), you could write :
   * <code>assertThat(myDate).isWithinSecond(secondOf(otherDate))</code><br>
   * see {@link org.fest.util.Dates#secondOf(Date)} to get the second of a given Date.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * 
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same second.
   */
  public DateAssert isInSameSecondAs(Date other) {
    dates.assertIsInSameSecondAs(info, actual, other);
    return this;
  }

  /**
   * Same assertion as {@link #isInSameSecondAs(Date)} but given Date is represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same second.
   */
  public DateAssert isInSameSecondAs(String dateAsString) {
    return isInSameSecondAs(parse(dateAsString));
  }

  /**
   * Verifies that the actual {@code Date} is close to the other date by less than delta (expressed in milliseconds), if
   * difference is equals to delta it's ok.
   * <p>
   * One can use handy {@link TimeUnit} to convert a duration in milliseconds, for example you can express a delta of 5 seconds
   * with <code>TimeUnit.SECONDS.toMillis(5)</code>.
   * <p>
   * Note that using a custom comparator has no effect on this assertion (see {@link #usingComparator(Comparator)}.
   * @param other the date to compare actual to
   * @param deltaInMilliseconds the delta used for date comparison, expressed in milliseconds
   * @return this assertion object.
   * @throws NullPointerException if {@code Date} parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} week is not close to the given date by less than delta.
   */
  public DateAssert isCloseTo(Date other, long deltaInMilliseconds) {
    dates.assertIsCloseTo(info, actual, other, deltaInMilliseconds);
    return this;
  }

  /**
   * Same assertion as {@link #isCloseTo(Date, long)} but given Date is represented as String either with ISO date format
   * (yyyy-MM-dd) or user custom date format (set with method {@link #withDateFormat(DateFormat)}).
   * @param dateAsString the given Date represented as String in default or custom date format.
   * @param deltaInMilliseconds the delta used for date comparison, expressed in milliseconds
   * @return this assertion object.
   * @throws NullPointerException if dateAsString parameter is {@code null}.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} week is not close to the given date by less than delta.
   */
  public DateAssert isCloseTo(String dateAsString, long deltaInMilliseconds) {
    return isCloseTo(parse(dateAsString), deltaInMilliseconds);
  }

  /**
   * Verifies that the actual {@code Date} has the same time as the given timestamp.
   * <p>
   * Both time or timestamp express a number of milliseconds since January 1, 1970, 00:00:00 GMT.
   * @param timestamp the timestamp to compare actual time to.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} time is not equal to the given timestamp.
   * @see Date#getTime()
   */
  public DateAssert hasTime(long timestamp) {
    dates.assertHasTime(info, actual, timestamp);
    return this;
  }

  /**
   * For String based Date assertions like {@link #isBefore(String)}, given String is expected to follow the default Date format,
   * that is ISO 8601 format : "yyyy-MM-dd".
   * <p>
   * With this method, user can specify its own date format, replacing the current date format for all future Date assertions in
   * the test suite (i.e. not only the current assertions) since custom DateFormat is stored in a static field.
   * <p>
   * To revert to default format simply call {@link #withIsoDateFormat()}.
   * 
   * @param userCustomDateFormat the new Date format used for String based Date assertions.
   * @return this assertion object.
   */
  public DateAssert withDateFormat(DateFormat userCustomDateFormat) {
    useDateFormat(userCustomDateFormat);
    return this;
  }

  /**
   * For String based Date assertions like {@link #isBefore(String)}, given String is expected to follow the default Date format,
   * that is ISO 8601 format : "yyyy-MM-dd".
   * <p>
   * With this method, user can specify its own date format, replacing the current date format for all future Date assertions in
   * the test suite (i.e. not only the current assertions) since custom DateFormat is stored in a static field.
   * <p>
   * To revert to default format simply call {@link #useIsoDateFormat()} (static method) or {@link #withIsoDateFormat()}.
   * 
   * @param userCustomDateFormat the new Date format used for String based Date assertions.
   */
  public static void useDateFormat(DateFormat userCustomDateFormat) {
    if (userCustomDateFormat == null) throw new NullPointerException("The given date format should not be null");
    dateFormat = userCustomDateFormat;
  }

  /**
   * Use ISO 8601 date format ("yyyy-MM-dd") for String based Date assertions.
   * @return this assertion object.
   */
  public DateAssert withIsoDateFormat() {
    useIsoDateFormat();
    return this;
  }

  /**
   * Use ISO 8601 date format ("yyyy-MM-dd") for String based Date assertions.
   */
  public static void useIsoDateFormat() {
    dateFormat = ISO_DATE_FORMAT;
  }

  /**
   * Utillity method to parse a Date with {@link #dateFormat}, note that it is thread safe.<br>
   * Returns <code>null</code> if dateAsString parameter is <code>null</code>.
   * @param dateAsString the string to parse as a Date with {@link #dateFormat}
   * @return the corrresponding Date, null if dateAsString parameter is null.
   * @throws AssertionError if the string can't be parsed as a Date
   */
  private static Date parse(String dateAsString) {
    if (dateAsString == null) { return null; }
    try {
      // synchronized is used because SimpleDateFormat which is not thread safe (sigh).
      synchronized (dateFormat) {
        return dateFormat.parse(dateAsString);
      }
    } catch (ParseException e) {
      throw Failures.instance().failure("Failed to parse " + dateAsString + " with date format " + dateFormat);
    }
  }

  @Override
  public DateAssert usingComparator(Comparator<? super Date> customComparator) {
    super.usingComparator(customComparator);
    this.dates = new Dates(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public DateAssert usingDefaultComparator() {
    super.usingDefaultComparator();
    this.dates = Dates.instance();
    return myself;
  }
}
