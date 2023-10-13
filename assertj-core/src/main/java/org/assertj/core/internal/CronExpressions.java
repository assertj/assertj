package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link CronExpression}</code>s.
 * @author Neil Wang
 */
public class CronExpressions {

  private static final CronExpressions INSTANCE = new CronExpressions();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static CronExpressions instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  CronExpressions() {
  }

  /**
   * Verify the given
   * <a href="https://www.manpagez.com/man/5/crontab/">crontab expression</a>
   * string is a {@code CronExpression} or not.
   * @param actual the expression string to parse
   * @throws AssertionError       if {@code actual} is invalid.
   * @throws NullPointerException if {@code actual} is {@code null}.
   */
  public void assertIsValid(AssertionInfo info, CronExpression actual) {
    assertNotNull(info, actual);
    actual.assertIsValid(info, failures);
  }

  /**
   * Asserts that the seconds of the cron expression exactly equals to the given values.
   * @param actual the expression string to parse
   * Examples:
   * <code>CronExpressions.assertContainsExactlySeconds(info, new CronExpression("1,2 * * * * *"), "1", "2");</code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public void assertContainsExactlySeconds(AssertionInfo info, CronExpression actual, String... values) {
    assertIsValid(info, actual);
    actual.assertContainsExactlySeconds(info, failures, values);
  }

  /**
   * Asserts that the minutes of the cron expression exactly equals to the given values.
   * @param actual the expression string to parse
   * Examples:
   * <code>CronExpressions.assertContainsExactlyMinutes(info, new CronExpression("* 1,2 * * * *"), "1", "2");</code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public void assertContainsExactlyMinutes(AssertionInfo info, CronExpression actual, String... values) {
    assertIsValid(info, actual);
    actual.assertContainsExactlyMinutes(info, failures, values);
  }

  /**
   * Asserts that the hours of the cron expression exactly equals to the given values.
   * @param actual the expression string to parse
   * Examples:
   * <code>CronExpressions.assertContainsExactlyHours(info, new CronExpression("* * 1,2 * * *"), "1", "2");</code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public void assertContainsExactlyHours(AssertionInfo info, CronExpression actual, String... values) {
    assertIsValid(info, actual);
    actual.assertContainsExactlyHours(info, failures, values);
  }

  /**
   * Asserts that the day of month of the cron expression exactly equals to the given values.
   * @param actual the expression string to parse
   * Examples:
   * <code>CronExpressions.assertContainsExactlyDayOfMonth(info, new CronExpression("* * * 1,2 * *"), "1", "2");</code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public void assertContainsExactlyDayOfMonth(AssertionInfo info, CronExpression actual, String[] values) {
    assertIsValid(info, actual);
    actual.assertContainsExactlyDayOfMonth(info, failures, values);
  }

  /**
   * Asserts that the month of the cron expression exactly equals to the given values.
   * @param actual the expression string to parse
   * Examples:
   * <code>CronExpressions.assertContainsExactlyMonth(info, new CronExpression("* * * * 1,2 *"), "1", "2");</code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public void assertContainsExactlyMonth(AssertionInfo info, CronExpression actual, String[] values) {
    assertIsValid(info, actual);
    actual.assertContainsExactlyMonth(info, failures, values);
  }

  /**
   * Asserts that the day of week of the cron expression exactly equals to the given values.
   * @param actual the expression string to parse
   * Examples:
   * <code>CronExpressions.assertContainsExactlyMonth(info, new CronExpression("* * * * * 1,2"), "1", "2");</code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws NullPointerException if the given {@code CronExpression} is like "* * * * 2005", The sixth field is year. Should use {@link #assertContainsExactlyYear(AssertionInfo, CronExpression, String[])} instead.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public void assertContainsExactlyDayOfWeek(AssertionInfo info, CronExpression actual, String[] values) {
    assertIsValid(info, actual);
    actual.assertContainsExactlyDayOfWeek(info, failures, values);
  }

  /**
   * Asserts that the year of the cron expression exactly equals to the given values.
   * @param actual the expression string to parse
   * Examples:
   * <code>CronExpressions.assertContainsExactlyYear(info, new CronExpression("* * * * * 2005"), "2005");</code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws NullPointerException if the given {@code CronExpression} is like "* * * * 6#2", The sixth field is day of week. Should use {@link #assertContainsExactlyDayOfWeek(AssertionInfo, CronExpression, String[])} instead.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public void assertContainsExactlyYear(AssertionInfo info, CronExpression actual, String[] values) {
    assertIsValid(info, actual);
    actual.assertContainsExactlyYear(info, failures, values);
  }

  private void assertNotNull(AssertionInfo info, CronExpression actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
