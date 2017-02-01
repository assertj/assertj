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
package org.assertj.core.internal;

import static org.assertj.core.error.ShouldBeAfter.shouldBeAfter;
import static org.assertj.core.error.ShouldBeAfterOrEqualsTo.shouldBeAfterOrEqualsTo;
import static org.assertj.core.error.ShouldBeAfterYear.shouldBeAfterYear;
import static org.assertj.core.error.ShouldBeBefore.shouldBeBefore;
import static org.assertj.core.error.ShouldBeBeforeOrEqualsTo.shouldBeBeforeOrEqualsTo;
import static org.assertj.core.error.ShouldBeBeforeYear.shouldBeBeforeYear;
import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.error.ShouldBeCloseTo.shouldBeCloseTo;
import static org.assertj.core.error.ShouldBeInSameDay.shouldBeInSameDay;
import static org.assertj.core.error.ShouldBeInSameHour.shouldBeInSameHour;
import static org.assertj.core.error.ShouldBeInSameHourWindow.shouldBeInSameHourWindow;
import static org.assertj.core.error.ShouldBeInSameMinute.shouldBeInSameMinute;
import static org.assertj.core.error.ShouldBeInSameMinuteWindow.shouldBeInSameMinuteWindow;
import static org.assertj.core.error.ShouldBeInSameMonth.shouldBeInSameMonth;
import static org.assertj.core.error.ShouldBeInSameSecond.shouldBeInSameSecond;
import static org.assertj.core.error.ShouldBeInSameSecondWindow.shouldBeInSameSecondWindow;
import static org.assertj.core.error.ShouldBeInSameYear.shouldBeInSameYear;
import static org.assertj.core.error.ShouldBeInTheFuture.shouldBeInTheFuture;
import static org.assertj.core.error.ShouldBeInThePast.shouldBeInThePast;
import static org.assertj.core.error.ShouldBeToday.shouldBeToday;
import static org.assertj.core.error.ShouldHaveDateField.shouldHaveDateField;
import static org.assertj.core.error.ShouldHaveSameTime.shouldHaveSameTime;
import static org.assertj.core.error.ShouldHaveTime.shouldHaveTime;
import static org.assertj.core.error.ShouldNotBeBetween.shouldNotBeBetween;
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
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.error.ShouldBeEqualWithTimePrecision;
import org.assertj.core.util.VisibleForTesting;


/**
 * Reusable assertions for <code>{@link Date}</code>s.
 *
 * @author Joel Costigliola
 * @author William Delanoue
 */
public class Dates {

  private static final Dates INSTANCE = new Dates();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Dates instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Dates() {
    this(StandardComparisonStrategy.instance());
  }

  private final ComparisonStrategy comparisonStrategy;

  public Dates(ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
  }

  @VisibleForTesting
  public Comparator<?> getComparator() {
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy) { return ((ComparatorBasedComparisonStrategy) comparisonStrategy)
        .getComparator(); }
    return null;
  }

  /**
   * Verifies that the actual {@code Date} is strictly before the given one.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the other date to compare actual with.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly before the given one.
   */
  public void assertIsBefore(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!isBefore(actual, other))
        throw failures.failure(info, shouldBeBefore(actual, other, comparisonStrategy));
  }

  /**
   * Verifies that the actual {@code Date} is before or equal to the given one.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the other date to compare actual with.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not before or equal to the given one.
   */
  public void assertIsBeforeOrEqualsTo(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!isBeforeOrEqualTo(actual, other))
        throw failures.failure(info, shouldBeBeforeOrEqualsTo(actual, other, comparisonStrategy));
  }

  /**
   * Verifies that the actual {@code Date} is strictly after the given one.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given Date.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not strictly after the given one.
   */
  public void assertIsAfter(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!isAfter(actual, other))
        throw failures.failure(info, shouldBeAfter(actual, other, comparisonStrategy));
  }

  /**
   * Verifies that the actual {@code Date} is after or equal to the given one.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given Date.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not after or equal to the given one.
   */
  public void assertIsAfterOrEqualsTo(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!isAfterOrEqualTo(actual, other))
        throw failures.failure(info, shouldBeAfterOrEqualsTo(actual, other, comparisonStrategy));
  }

  /**
   * Verifies that the actual {@code Date} is equal to the given one with precision.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given Date.
   * @param precision maximum precision for the comparison.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not equal to the given one.
   */
  public void assertIsEqualWithPrecision(AssertionInfo info, Date actual, Date other, TimeUnit precision) {
    assertNotNull(info, actual);
    Calendar calendarActual = Calendar.getInstance();
    calendarActual.setTime(actual);
    Calendar calendarOther = Calendar.getInstance();
    calendarOther.setTime(other);
    switch (precision) {
      case DAYS:
        calendarActual.set(Calendar.DAY_OF_WEEK, 0);
        calendarOther.set(Calendar.DAY_OF_WEEK, 0);
      case HOURS:
        calendarActual.set(Calendar.HOUR_OF_DAY, 0);
        calendarOther.set(Calendar.HOUR_OF_DAY, 0);
      case MINUTES:
        calendarActual.set(Calendar.MINUTE, 0);
        calendarOther.set(Calendar.MINUTE, 0);
      case SECONDS:
        calendarActual.set(Calendar.SECOND, 0);
        calendarOther.set(Calendar.SECOND, 0);
      case MILLISECONDS:
        calendarActual.set(Calendar.MILLISECOND, 0);
        calendarOther.set(Calendar.MILLISECOND, 0);
      case MICROSECONDS:
        break;
      default:
        break;
    }
    if (calendarActual.compareTo(calendarOther) != 0)
      throw failures.failure(info, ShouldBeEqualWithTimePrecision.shouldBeEqual(actual, other, precision));
  }

  /**
   * Verifies that the actual {@code Date} is in <i>start:end</i> period.<br>
   * start date belongs to the period if inclusiveStart is true.<br>
   * end date belongs to the period if inclusiveEnd is true.<br>
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param start the period start, expected not to be null.
   * @param end the period end, expected not to be null.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start {@code Date} is {@code null}.
   * @throws NullPointerException if end {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in <i>start:end</i> period.
   */
  public void assertIsBetween(AssertionInfo info, Date actual, Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    if (!actualIsBetweenGivenPeriod(info, actual, start, end, inclusiveStart, inclusiveEnd))
        throw failures.failure(info, shouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, comparisonStrategy));
  }

  private boolean actualIsBetweenGivenPeriod(AssertionInfo info, Date actual, Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd) {
    assertNotNull(info, actual);
    startDateParameterIsNotNull(start);
    endDateParameterIsNotNull(end);
    boolean checkLowerBoundaryPeriod = inclusiveStart ? isAfterOrEqualTo(actual, start) : isAfter(actual, start);
    boolean checkUpperBoundaryPeriod = inclusiveEnd ? isBeforeOrEqualTo(actual, end) : isBefore(actual, end);
    boolean isBetweenGivenPeriod = checkLowerBoundaryPeriod && checkUpperBoundaryPeriod;
    return isBetweenGivenPeriod;
  }

  /**
   * Verifies that the actual {@code Date} is not in <i>start:end</i> period..<br>
   * start date belongs to the period if inclusiveStart is true.<br>
   * end date belongs to the period if inclusiveEnd is true.<br>
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param start the period start, expected not to be null.
   * @param end the period end, expected not to be null.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if start {@code Date} is {@code null}.
   * @throws NullPointerException if end {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is in <i>start:end</i> period.
   */
  public void assertIsNotBetween(AssertionInfo info, Date actual, Date start, Date end, boolean inclusiveStart,
      boolean inclusiveEnd) {
    if (actualIsBetweenGivenPeriod(info, actual, start, end, inclusiveStart, inclusiveEnd))
        throw failures.failure(info, shouldNotBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, comparisonStrategy));
  }

  /**
   * Verifies that the actual {@code Date} is strictly in the past.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in the past.
   */
  public void assertIsInThePast(AssertionInfo info, Date actual) {
    assertNotNull(info, actual);
    if (!isBefore(actual, now()))
        throw failures.failure(info, shouldBeInThePast(actual, comparisonStrategy));
  }

  /**
   * Verifies that the actual {@code Date} is today, by comparing only year, month and day of actual to today (ie. we don't check
   * hours).
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not today.
   */
  public void assertIsToday(AssertionInfo info, Date actual) {
    assertNotNull(info, actual);
    Date todayWithoutTime = truncateTime(now());
    Date actualWithoutTime = truncateTime(actual);
    if (!areEqual(actualWithoutTime, todayWithoutTime))
        throw failures.failure(info, shouldBeToday(actual, comparisonStrategy));
  }

  /**
   * Verifies that the actual {@code Date} is strictly in the future.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} is not in the future.
   */
  public void assertIsInTheFuture(AssertionInfo info, Date actual) {
    assertNotNull(info, actual);
    if (!isAfter(actual, now()))
        throw failures.failure(info, shouldBeInTheFuture(actual, comparisonStrategy));
  }

  /**
   * Verifies that the actual {@code Date} is strictly before the given year.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param year the year to compare actual year to
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} year is after or equal to the given year.
   */
  public void assertIsBeforeYear(AssertionInfo info, Date actual, int year) {
    assertNotNull(info, actual);
    if (yearOf(actual) >= year)
        throw failures.failure(info, shouldBeBeforeYear(actual, year));
  }

  /**
   * Verifies that the actual {@code Date} is strictly after the given year.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param year the year to compare actual year to
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} year is before or equal to the given year.
   */
  public void assertIsAfterYear(AssertionInfo info, Date actual, int year) {
    assertNotNull(info, actual);
    if (yearOf(actual) <= year)
        throw failures.failure(info, shouldBeAfterYear(actual, year));
  }

  /**
   * Verifies that the actual {@code Date} year is equal to the given year.
   * @param year the year to compare actual year to
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} year is not equal to the given year.
   */
  public void assertHasYear(AssertionInfo info, Date actual, int year) {
    assertNotNull(info, actual);
    if (yearOf(actual) != year)
        throw failures.failure(info, shouldHaveDateField(actual, "year", year));
  }

  /**
   * Verifies that the actual {@code Date} month is equal to the given month, <b>month value starting at 1</b> (January=1,
   * February=2, ...).
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param month the month to compare actual month to, see {@link Calendar#MONTH} for valid values
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} month is not equal to the given month.
   */
  public void assertHasMonth(AssertionInfo info, Date actual, int month) {
    assertNotNull(info, actual);
    if (monthOf(actual) != month)
        throw failures.failure(info, shouldHaveDateField(actual, "month", month));
  }

  /**
   * Verifies that the actual {@code Date} day of month is equal to the given day of month.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param dayOfMonth the day of month to compare actual day of month to
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} month is not equal to the given day of month.
   */
  public void assertHasDayOfMonth(AssertionInfo info, Date actual, int dayOfMonth) {
    assertNotNull(info, actual);
    if (dayOfMonthOf(actual) != dayOfMonth)
        throw failures.failure(info, shouldHaveDateField(actual, "day of month", dayOfMonth));
  }

  /**
   * Verifies that the actual {@code Date} day of week is equal to the given day of week.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param dayOfWeek the day of week to compare actual day of week to, see {@link Calendar#DAY_OF_WEEK} for valid values
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} week is not equal to the given day of week.
   */
  public void assertHasDayOfWeek(AssertionInfo info, Date actual, int dayOfWeek) {
    assertNotNull(info, actual);
    if (dayOfWeekOf(actual) != dayOfWeek)
        throw failures.failure(info, shouldHaveDateField(actual, "day of week", dayOfWeek));
  }

  /**
   * Verifies that the actual {@code Date} hour od day is equal to the given hour of day (24-hour clock).
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param hourOfDay the hour of day to compare actual hour of day to (24-hour clock)
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} hour is not equal to the given hour.
   */
  public void assertHasHourOfDay(AssertionInfo info, Date actual, int hourOfDay) {
    assertNotNull(info, actual);
    if (hourOfDayOf(actual) != hourOfDay)
        throw failures.failure(info, shouldHaveDateField(actual, "hour", hourOfDay));
  }

  /**
   * Verifies that the actual {@code Date} minute is equal to the given minute.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param minute the minute to compare actual minute to
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} minute is not equal to the given minute.
   */
  public void assertHasMinute(AssertionInfo info, Date actual, int minute) {
    assertNotNull(info, actual);
    if (minuteOf(actual) != minute)
        throw failures.failure(info, shouldHaveDateField(actual, "minute", minute));
  }

  /**
   * Verifies that the actual {@code Date} second is equal to the given second.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param second the second to compare actual second to
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} second is not equal to the given second.
   */
  public void assertHasSecond(AssertionInfo info, Date actual, int second) {
    assertNotNull(info, actual);
    if (secondOf(actual) != second)
        throw failures.failure(info, shouldHaveDateField(actual, "second", second));
  }

  /**
   * Verifies that the actual {@code Date} millisecond is equal to the given millisecond.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param millisecond the millisecond to compare actual millisecond to
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} millisecond is not equal to the given millisecond.
   */
  public void assertHasMillisecond(AssertionInfo info, Date actual, int millisecond) {
    assertNotNull(info, actual);
    if (millisecondOf(actual) != millisecond)
        throw failures.failure(info, shouldHaveDateField(actual, "millisecond", millisecond));
  }

  /**
   * Verifies that actual and given {@code Date} are in the same year.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not in the same year.
   */
  public void assertIsInSameYearAs(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!areInSameYear(actual, other))
        throw failures.failure(info, shouldBeInSameYear(actual, other));
  }

  /**
   * Returns true if both date are in the same year, false otherwise.
   * @param actual the actual date. expected not be null
   * @param other the other date. expected not be null
   * @return true if both date are in the same year, false otherwise
   */
  private static boolean areInSameYear(Date actual, Date other) {
    return yearOf(actual) == yearOf(other);
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same month (and thus in the same year).
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not chronologically speaking in the same month.
   */
  public void assertIsInSameMonthAs(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!areInSameMonth(actual, other))
        throw failures.failure(info, shouldBeInSameMonth(actual, other));
  }

  /**
   * Returns true if both date are in the same year and month, false otherwise.
   * @param actual the actual date. expected not be null
   * @param other the other date. expected not be null
   * @return true if both date are in the same year and month, false otherwise
   */
  private static boolean areInSameMonth(Date actual, Date other) {
    return areInSameYear(actual, other) && monthOf(actual) == monthOf(other);
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same day of month (and thus in the same month and
   * year).
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not chronologically speaking in the same day of month.
   */
  public void assertIsInSameDayAs(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!areInSameDayOfMonth(actual, other))
        throw failures.failure(info, shouldBeInSameDay(actual, other));
  }

  /**
   * Returns true if both date are in the same year, month and day of month, false otherwise.
   * @param actual the actual date. expected not be null
   * @param other the other date. expected not be null
   * @return true if both date are in the same year, month and day of month, false otherwise
   */
  private static boolean areInSameDayOfMonth(Date actual, Date other) {
    return areInSameMonth(actual, other) && dayOfMonthOf(actual) == dayOfMonthOf(other);
  }

  /**
   * Verifies that actual and given {@code Date} are in the same hour (and thus in the same day of month, month
   * and year).
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not chronologically speaking in the same hour.
   */
  public void assertIsInSameHourAs(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!areInSameHour(actual, other))
        throw failures.failure(info, shouldBeInSameHour(actual, other));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same hour, day of month, month and year.
   *
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not chronologically speaking in the same hour.
   */
  public void assertIsInSameHourWindowAs(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!areInSameHourWindow(actual, other))
        throw failures.failure(info, shouldBeInSameHourWindow(actual, other));
  }

  /**
   * Returns true if both date are in the same year, month and day of month, hour, minute and second, false otherwise.
   * @param actual the actual date. expected not be null
   * @param other the other date. expected not be null
   * @return true if both date are in the same year, month and day of month, hour, minute and second, false otherwise.
   */
  private static boolean areInSameHourWindow(Date actual, Date other) {
    return timeDifference(actual, other) < TimeUnit.HOURS.toMillis(1);
  }

  /**
   * Returns true if both date are in the same year, month, day of month and hour, false otherwise.
   * @param actual the actual date. expected not be null
   * @param other the other date. expected not be null
   * @return true if both date are in the same year, month, day of month and hour, false otherwise.
   */
  private static boolean areInSameHour(Date actual, Date other) {
    return areInSameDayOfMonth(actual, other) && hourOfDayOf(actual) == hourOfDayOf(other);
  }

  /**
   * Verifies that actual and given {@code Date} are in the same minute, hour, day of month, month and year.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not chronologically speaking in the same minute.
   */
  public void assertIsInSameMinuteAs(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!areInSameMinute(actual, other))
        throw failures.failure(info, shouldBeInSameMinute(actual, other));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same minute.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not chronologically speaking in the same minute.
   */
  public void assertIsInSameMinuteWindowAs(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!areInSameMinuteWindow(actual, other))
        throw failures.failure(info, shouldBeInSameMinuteWindow(actual, other));
  }

  /**
   * Returns true if both date are in the same year, month, day of month, hour and minute, false otherwise.
   * @param actual the actual date. expected not be null
   * @param other the other date. expected not be null
   * @return true if both date are in the same year, month, day of month, hour and minute, false otherwise.
   */
  private static boolean areInSameMinute(Date actual, Date other) {
    return areInSameHour(actual, other) && minuteOf(actual) == minuteOf(other);
  }

  private static boolean areInSameMinuteWindow(Date actual, Date other) {
    return timeDifference(actual, other) < TimeUnit.MINUTES.toMillis(1);
  }

  /**
   * Verifies that actual and given {@code Date} are in the same second, minute, hour, day of month, month and year.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not chronologically speaking in the same second.
   */
  public void assertIsInSameSecondAs(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!areInSameSecond(actual, other))
        throw failures.failure(info, shouldBeInSameSecond(actual, other));
  }

  /**
   * Verifies that actual and given {@code Date} are chronologically in the same second.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if actual and given {@code Date} are not chronologically speaking in the same second.
   */
  public void assertIsInSameSecondWindowAs(AssertionInfo info, Date actual, Date other) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    if (!areInSameSecondWindow(actual, other))
        throw failures.failure(info, shouldBeInSameSecondWindow(actual, other));
  }

  /**
   * Returns true if both date are in the same year, month and day of month, hour, minute and second, false otherwise.
   * @param actual the actual date. expected not be null
   * @param other the other date. expected not be null
   * @return true if both date are in the same year, month and day of month, hour, minute and second, false otherwise.
   */
  private static boolean areInSameSecondWindow(Date actual, Date other) {
    return timeDifference(actual, other) < TimeUnit.SECONDS.toMillis(1);
  }

  /**
   * Returns true if both date are in the same year, month and day of month, hour, minute and second, false otherwise.
   * @param actual the actual date. expected not be null
   * @param other the other date. expected not be null
   * @return true if both date are in the same year, month and day of month, hour, minute and second, false otherwise.
   */
  private static boolean areInSameSecond(Date actual, Date other) {
    return areInSameMinute(actual, other) && secondOf(actual) == secondOf(other);
  }

  /**
   * Verifies that the actual {@code Date} is close to the other date by less than delta, if difference is equals to delta it is
   * ok.<br>
   * Note that delta expressed in milliseconds.<br>
   * Use handy TimeUnit to convert a duration in milliseconds, for example you can express a delta of 5 seconds with
   * <code>TimeUnit.SECONDS.toMillis(5)</code>.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param other the given {@code Date} to compare actual {@code Date} to.
   * @param deltaInMilliseconds the delta used for date comparison, expressed in milliseconds
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   * @throws AssertionError if the actual {@code Date} week is not close to the given date by less than delta.
   */
  public void assertIsCloseTo(AssertionInfo info, Date actual, Date other, long deltaInMilliseconds) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(other);
    long difference = Math.abs(actual.getTime() - other.getTime());
    if (difference > deltaInMilliseconds)
        throw failures.failure(info, shouldBeCloseTo(actual, other, deltaInMilliseconds, difference));
  }

  /**
   * Verifies that the actual {@code Date} time is equal to the given timestamp.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param timestamp the timestamp to compare actual time to
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} time is not equal to the given timestamp.
   */
  public void assertHasTime(AssertionInfo info, Date actual, long timestamp) {
    assertNotNull(info, actual);
    if (actual.getTime() != timestamp)
        throw failures.failure(info, shouldHaveTime(actual, timestamp));
  }

  /**
   * Verifies that the actual {@code Date} has same time as the given {@code Date}.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param expected the "expected" {@code Date} to compare actual time to
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if {@code expected} is {@code null}.
   * @throws AssertionError if the actual {@code Date} time is not equal to the given {@code Date}.
   */
  public void assertHasSameTime(AssertionInfo info, Date actual, Date expected) {
    assertNotNull(info, actual);
    assertNotNull(info, expected);
    if (actual.getTime() != expected.getTime())
        throw failures.failure(info, shouldHaveSameTime(actual, expected));
  }

  /**
   * Verifies that the actual {@code Date} is equal to the given date by comparing their time.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   * @param date the date to compare actual time to
   * @throws AssertionError if {@code actual} is {@code null}.
   * @throws AssertionError if the actual {@code Date} time is not equal to the given date time.
   * @throws NullPointerException if other {@code Date} is {@code null}.
   */
  public void hasSameTimeAs(AssertionInfo info, Date actual, Date date) {
    assertNotNull(info, actual);
    dateParameterIsNotNull(date);
    assertHasSameTime(info, actual, date);
  }

  /**
   * used to check that the date to compare actual date to is not null, in that case throws a {@link NullPointerException} with an
   * explicit message
   * @param date the date to check
   * @throws NullPointerException with an explicit message if the given date is null
   */
  private static void dateParameterIsNotNull(Date date) {
    checkNotNull(date, "The date to compare actual with should not be null");
  }

  /**
   * used to check that the start of period date to compare actual date to is not null, in that case throws a
   * {@link NullPointerException} with an explicit message
   * @param start the start date to check
   * @throws NullPointerException with an explicit message if the given start date is null
   */
  private static void startDateParameterIsNotNull(Date start) {
    checkNotNull(start, "The start date of period to compare actual with should not be null");
  }

  /**
   * used to check that the end of period date to compare actual date to is not null, in that case throws a
   * {@link NullPointerException} with an explicit message
   * @param end the end date to check
   * @throws NullPointerException with an explicit message if the given end date is null
   */
  private static void endDateParameterIsNotNull(Date end) {
    checkNotNull(end, "The end date of period to compare actual with should not be null");
  }

  private void assertNotNull(AssertionInfo info, Date actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  /**
   * Returns <code>true</code> if the actual {@code Date} is before or equal to the given one according to underlying
   * {@link #comparisonStrategy}, false otherwise.
   * @param actual the actual date - must not be null.
   * @param other the given Date.
   * @return <code>true</code> if the actual {@code Date} is before or equal to the given one according to underlying
   *         {@link #comparisonStrategy}, false otherwise.
   * @throws NullPointerException if {@code actual} is {@code null}.
   */
  private boolean isBeforeOrEqualTo(Date actual, Date other) {
    return comparisonStrategy.isLessThanOrEqualTo(actual, other);
  }

  /**
   * Returns true if the actual {@code Date} is equal to the given one according to underlying {@link #comparisonStrategy}, false
   * otherwise.
   * @param actual the actual date - must not be null.
   * @param other the given Date.
   * @return <code>true</code> if the actual {@code Date} is equal to the given one according to underlying
   *         {@link #comparisonStrategy}, false otherwise.
   */
  private boolean areEqual(Date actual, Date other) {
    return comparisonStrategy.areEqual(other, actual);
  }

  /**
   * Returns <code>true</code> if the actual {@code Date} is after or equal to the given one according to underlying
   * {@link #comparisonStrategy}, false otherwise.
   * @param actual the actual date - must not be null.
   * @param other the given Date.
   * @return <code>true</code> if the actual {@code Date} is after or equal to the given one according to underlying
   *         {@link #comparisonStrategy}, false otherwise.
   * @throws NullPointerException if {@code actual} is {@code null}.
   */
  private boolean isAfterOrEqualTo(Date actual, Date other) {
    return comparisonStrategy.isGreaterThanOrEqualTo(actual, other);
  }

  /**
   * Returns true if actual is before other according to underlying {@link #comparisonStrategy}, false otherwise.
   * @param actual the {@link Date} to compare to other
   * @param other the {@link Date} to compare to actual
   * @return true if actual is before other according to underlying {@link #comparisonStrategy}, false otherwise.
   */
  private boolean isBefore(Date actual, Date other) {
    return comparisonStrategy.isLessThan(actual, other);
  }

  /**
   * Returns true if actual is after other according to underlying {@link #comparisonStrategy}, false otherwise.
   * @param actual the {@link Date} to compare to other
   * @param other the {@link Date} to compare to actual
   * @return true if actual is after other according to underlying {@link #comparisonStrategy}, false otherwise.
   */
  private boolean isAfter(Date actual, Date other) {
    return comparisonStrategy.isGreaterThan(actual, other);
  }

}
