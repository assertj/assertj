package org.assertj.core.api;

import org.assertj.core.internal.CronExpression;
import org.assertj.core.internal.CronExpressions;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link CronExpression}s.
 * <p>
 * Make the given
 * <a href="https://www.manpagez.com/man/5/crontab/">crontab expression</a>
 * string into a {@code CronExpression}.
 * <p>The following rules apply:
 * <ul>
 * <li>
 * A field may be an asterisk ({@code *}), which always stands for
 * "first-last". For the "day of the month" or "day of the week" fields, a
 * question mark ({@code ?}) may be used instead of an asterisk.
 * </li>
 * <li>
 * Ranges of numbers are expressed by two numbers separated with a hyphen
 * ({@code -}). The specified range is inclusive.
 * </li>
 * <li>Following a range (or {@code *}) with {@code /n} specifies
 * the interval of the number's value through the range.
 * </li>
 * <li>
 * English names can also be used for the "month" and "day of week" fields.
 * Use the first three letters of the particular day or month (case does not
 * matter).
 * </li>
 * <li>
 * The "day of month" and "day of week" fields can contain a
 * {@code L}-character, which stands for "last", and has a different meaning
 * in each field:
 * <ul>
 * <li>
 * In the "day of month" field, {@code L} stands for "the last day of the
 * month". If followed by an negative offset (i.e. {@code L-n}), it means
 * "{@code n}th-to-last day of the month". If followed by {@code W} (i.e.
 * {@code LW}), it means "the last weekday of the month".
 * </li>
 * <li>
 * In the "day of week" field, {@code dL} or {@code DDDL} stands for
 * "the last day of week {@code d} (or {@code DDD}) in the month".
 * </li>
 * </ul>
 * </li>
 * <li>
 * The "day of month" field can be {@code nW}, which stands for "the nearest
 * weekday to day of the month {@code n}".
 * If {@code n} falls on Saturday, this yields the Friday before it.
 * If {@code n} falls on Sunday, this yields the Monday after,
 * which also happens if {@code n} is {@code 1} and falls on a Saturday
 * (i.e. {@code 1W} stands for "the first weekday of the month").
 * </li>
 * <li>
 * The "day of week" field can be {@code d#n} (or {@code DDD#n}), which
 * stands for "the {@code n}-th day of week {@code d} (or {@code DDD}) in
 * the month".
 * </li>
 * </ul>
 *
 * <p>Example expressions:
 * <ul>
 * <li>{@code "0 0 * * * *"} = the top of every hour of every day.</li>
 * <li><code>"*&#47;10 * * * * *"</code> = every ten seconds.</li>
 * <li>{@code "0 0 8-10 * * *"} = 8, 9 and 10 o'clock of every day.</li>
 * <li>{@code "0 0 6,19 * * *"} = 6:00 AM and 7:00 PM every day.</li>
 * <li>{@code "0 0/30 8-10 * * *"} = 8:00, 8:30, 9:00, 9:30, 10:00 and 10:30 every day.</li>
 * <li>{@code "0 0 9-17 * * MON-FRI"} = on the hour nine-to-five weekdays</li>
 * <li>{@code "0 0 0 25 12 ?"} = every Christmas Day at midnight</li>
 * <li>{@code "0 0 0 L * *"} = last day of the month at midnight</li>
 * <li>{@code "0 0 0 L-3 * *"} = third-to-last day of the month at midnight</li>
 * <li>{@code "0 0 0 1W * *"} = first weekday of the month at midnight</li>
 * <li>{@code "0 0 0 LW * *"} = last weekday of the month at midnight</li>
 * <li>{@code "0 0 0 * * 5L"} = last Friday of the month at midnight</li>
 * <li>{@code "0 0 0 ? * 5#2"} = the second Friday in the month at midnight</li>
 * <li>{@code "0 0 0 ? * MON#1"} = the first Monday in the month at midnight</li>
 * </ul>
 *
 * <p>The following macros are also supported:
 * <ul>
 * <li>{@code "@yearly"} (or {@code "@annually"}) to run un once a year, i.e. {@code "0 0 0 1 1 *"},</li>
 * <li>{@code "@monthly"} to run once a month, i.e. {@code "0 0 0 1 * *"},</li>
 * <li>{@code "@weekly"} to run once a week, i.e. {@code "0 0 0 * * 0"},</li>
 * <li>{@code "@daily"} (or {@code "@midnight"}) to run once a day, i.e. {@code "0 0 0 * * *"},</li>
 * <li>{@code "@hourly"} to run once an hour, i.e. {@code "0 0 * * * *"}.</li>
 * </ul>
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *               target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *               for more details.
 * @author Neil Wang
 */
public class AbstractCronExpressionAssert<SELF extends AbstractCronExpressionAssert<SELF>> extends AbstractAssert<SELF, CronExpression> {

  @VisibleForTesting
  CronExpressions cronExpressions = CronExpressions.instance();

  protected AbstractCronExpressionAssert(CronExpression cronExpression, Class<?> selfType) {
    super(cronExpression, selfType);
  }

  /**
   * Verifies that the actual value is a valid cron expression.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThatCronExpression("* * * * * *").isValid();
   * assertThat("* 0/5 * * * ?").isTrue();
   *
   * // assertions will fail
   * assertThat("* * * * *").isValid();
   * assertThat("5-1 * * * * *").isValid();</code></pre>
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not valid.
   */
  public SELF isValid() {
    objects.assertNotNull(info, actual);
    cronExpressions.assertIsValid(info, actual);
    return myself;
  }

  /**
   * Asserts that the seconds of the cron expression exactly equals to the given values.
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new CronExpression("1,2 * * * * *")).containsExactlySeconds("1", "2");
   * assertThat(new CronExpression("* * * * * *")).containsExactlySeconds("*");
   * assertThat(new CronExpression("0/25 * * * * *")).containsExactlySeconds("25", "50");
   *  // assertions will fail
   *  assertThat(new CronExpression("0/25 * * * * *").containsExactlySeconds("50", "25");
   *  assertThat(new CronExpression("* * * * * *").containsExactlySeconds("0");
   * // or use assertThatCronExpression(...) instead
   * </code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public SELF containsExactlySeconds(String... values) {
    objects.assertNotNull(info, actual);
    cronExpressions.assertContainsExactlySeconds(info, actual, values);
    return myself;
  }

  /**
   * Asserts that the minutes of the cron expression exactly equals to the given values.
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new CronExpression("* 1,2 * * * *")).containsExactlyMinutes("1", "2");
   * assertThat(new CronExpression("* * * * * *").containsExactlyMinutes("*");
   * assertThat(new CronExpression("* 0/25 * * * *").containsExactlyMinutes("25", "50");
   * // assertions will fail
   * assertThat(new CronExpression("* 0/25 * * * *").containsExactlyMinutes("50", "25");
   * assertThat(new CronExpression("* * * * * *").containsExactlyMinutes("0");
   * // or use assertThatCronExpression(...) instead
   * </code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public SELF containsExactlyMinutes(String... values) {
    objects.assertNotNull(info, actual);
    cronExpressions.assertContainsExactlyMinutes(info, actual, values);
    return myself;
  }

  /**
   * Asserts that the hours of the cron expression exactly equals to the given values.
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new CronExpression("* * 1,2 * * *").containsExactlyHours("1", "2");
   * assertThat(new CronExpression("* * * * * *").containsExactlyHours("*");
   * assertThat(new CronExpression("* * 1/9 * * *").containsExactlyHours("1", "10", "19");
   * // assertions will fail
   * assertThat(new CronExpression("* * 1/9 * * *").containsExactlyHours("1", "19", "10");
   * assertThat(new CronExpression("* * * * * *").containsExactlyHours("1", "2", "3");
   * // or use assertThatCronExpression(...) instead
   * </code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public SELF containsExactlyHours(String... values) {
    objects.assertNotNull(info, actual);
    cronExpressions.assertContainsExactlyHours(info, actual, values);
    return myself;
  }

  /**
   * Asserts that the day of month of the cron expression exactly equals to the given values.
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new CronExpression("* * * 1,2 * *").containsExactlyDayOfMonth("1", "2");
   * assertThat(new CronExpression("* * * * * *").containsExactlyDayOfMonth("*");
   * assertThat(new CronExpression("* * * LW * *").containsExactlyDayOfMonth("LW");
   * assertThat(new CronExpression("* * * 1/9 * *").containsExactlyDayOfMonth("1", "10", "19", "28");
   * // assertions will fail
   * assertThat(new CronExpression("* * * 1/9 * *")).containsExactlyDayOfMonth("1", "10", "28", "19");
   * assertThat(new CronExpression("* * * * * *").containsExactlyDayOfMonth("1", "2", "3");
   * // or use assertThatCronExpression(...) instead
   * </code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public SELF containsExactlyDayOfMonth(String... values) {
    objects.assertNotNull(info, actual);
    cronExpressions.assertContainsExactlyDayOfMonth(info, actual, values);
    return myself;
  }

  /**
   * Asserts that the month of the cron expression exactly equals to the given values.
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new CronExpression("* * * * JAN,FEB *").containsExactlyMonth("1", "2");
   * assertThat(new CronExpression("* * * * 1,2 *").containsExactlyMonth(*);
   * assertThat(new CronExpression("* * * * 1/9 *").containsExactlyMonth("1", "10");
   * // assertions will fail
   * assertThat(new CronExpression("* * * * JAN,FEB *").containsExactlyMonth("JAN", "FEB");
   * assertThat(new CronExpression("* * * * JAN,FEB *").containsExactlyMonth("2", "1");
   * assertThat(new CronExpression("* * * * * *").containsExactlyMonth("1", "2", "3");
   * // or use assertThatCronExpression(...) instead
   * </code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   */
  public SELF containsExactlyMonth(String... values) {
    objects.assertNotNull(info, actual);
    cronExpressions.assertContainsExactlyMonth(info, actual, values);
    return myself;
  }


  /**
   * Asserts that the day of week of the cron expression exactly equals to the given values.
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new CronExpression("* * * * * 6#2").containsExactlyDayOfWeek("6#2");
   * assertThat(new CronExpression("* * * * * *").containsExactlyDayOfWeek("*");
   * assertThat(new CronExpression("* * * * * 1,2,3").containsExactlyDayOfWeek("1", "2", "3");
   * assertThat(new CronExpression("* * * * * 4L").containsExactlyDayOfWeek("4L");
   * // assertions will fail
   * assertThat(new CronExpression("* * * * * 2005").containsExactlyDayOfWeek("2005");
   * assertThat(new CronExpression("* * * * * 1,2,3").containsExactlyDayOfWeek("2", "1", "3");
   * assertThat(new CronExpression("* * * * * *").containsExactlyDayOfWeek("1", "2", "3");
   * // or use assertThatCronExpression(...) instead
   * </code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   * @throws AssertionError       if the given {@code CronExpression} is like "* * * * 2005", The sixth field is year. Should use {@link #containsExactlyYear} instead.
   */
  public SELF containsExactlyDayOfWeek(String... values) {
    objects.assertNotNull(info, actual);
    cronExpressions.assertContainsExactlyDayOfWeek(info, actual, values);
    return myself;
  }

  /**
   * Asserts that the year of the cron expression exactly equals to the given values.
   * Examples:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new CronExpression("* * * * * 2005").containsExactlyYear("2005");
   * assertThat(new CronExpression("* * * * * 2005-2009").containsExactlyYear("2005", "2006", "2007", "2008", "2009");
   * assertThat(new CronExpression("* * * * * 2000-2023/10").containsExactlyYear("2000", "2010", "2020");
   * // assertions will fail
   * assertThat(new CronExpression("* * * * * 2005").containsExactlyYear("2008");
   * assertThat(new CronExpression("* * * * * 2000-2023/10").containsExactlyYear("2000", "2020", "2010");
   * // or use assertThatCronExpression(...) instead
   * </code>
   * @throws NullPointerException if the given {@code CronExpression} is {@code null}.
   * @throws AssertionError       if the actual value does not exactly same as the given values.
   * @throws AssertionError       if the given {@code CronExpression} is like "* * * * 2005", The sixth field is day of week. Should use {@link #containsExactlyDayOfWeek} instead.
   */
  public SELF containsExactlyYear(String... values) {
    objects.assertNotNull(info, actual);
    cronExpressions.assertContainsExactlyYear(info, actual, values);
    return myself;
  }
}
