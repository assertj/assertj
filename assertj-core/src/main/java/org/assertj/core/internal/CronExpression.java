package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;

import static org.assertj.core.error.ShouldCronExpressionBeValid.shouldCronExpressionBeValid;
import static org.assertj.core.internal.CronUnit.*;

/**
 * CronExpression contains a cronable string that to set up a scheduled task.
 * @author Neil Wang
 */
public class CronExpression {

  private final String value;

  private static final String[] MACROS = new String[]{
    "@yearly", "0 0 0 1 1 *",
    "@annually", "0 0 0 1 1 *",
    "@monthly", "0 0 0 1 * *",
    "@weekly", "0 0 0 * * 0",
    "@daily", "0 0 0 * * *",
    "@midnight", "0 0 0 * * *",
    "@hourly", "0 0 * * * *"
  };
  /**
   * <pre>
   * &#9484;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472; second (0-59) {@link CronExpression#seconds}
   * &#9474; &#9484;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472; minute (0 - 59) {@link CronExpression#minutes}
   * &#9474; &#9474; &#9484;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472; hour (0 - 23) {@link CronExpression#hours}
   * &#9474; &#9474; &#9474; &#9484;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472; day of the month (1 - 31)  {@link CronExpression#dayOfMonth}
   * &#9474; &#9474; &#9474; &#9474; &#9484;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472; month (1 - 12) (or JAN-DEC)  {@link CronExpression#month}
   * &#9474; &#9474; &#9474; &#9474; &#9474; &#9484;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472; day of the week (0 - 7) or year (1970-2099)
   * &#9474; &#9474; &#9474; &#9474; &#9474; &#9474;                      {@link CronExpression#dayOfWeek} or {@link CronExpression#year}
   * &#9474; &#9474; &#9474; &#9474; &#9474; &#9474;          (0 or 7 is Sunday, or MON-SUN)
   * &#9474; &#9474; &#9474; &#9474; &#9474; &#9474;
   * &#42; &#42; &#42; &#42; &#42; &#42;
   * </pre>
   */
  private CronField seconds;
  private CronField minutes;
  private CronField hours;
  private CronField dayOfMonth;
  private CronField month;
  private CronField dayOfWeek;
  private CronField year;


  public CronExpression(String value) {
    this.value = value;
  }

  void assertIsValid(AssertionInfo info, Failures failures) {
    String expression = resolveMacros(this.value);
    Objects.instance().assertNotNull(info, expression);
    String[] fields = expression.split(" ");
    if (fields.length != 6) {
      throw failures.failure(info, shouldCronExpressionBeValid(this.value));
    }
    this.seconds = new CronField(SECONDS, fields[0]);
    CronFieldValidation.instance().assertIsValid(info, this.seconds, failures);
    this.minutes = new CronField(MINUTES, fields[1]);
    CronFieldValidation.instance().assertIsValid(info, this.minutes, failures);
    this.hours = new CronField(HOURS, fields[2]);
    CronFieldValidation.instance().assertIsValid(info, this.hours, failures);
    this.dayOfMonth = new CronField(DAY_OF_MONTH, fields[3]);
    CronFieldValidation.instance().assertIsValid(info, this.dayOfMonth, failures);
    this.month = new CronField(MONTH, fields[4]);
    CronFieldValidation.instance().assertIsValid(info, this.month, failures);
    try {
      this.dayOfWeek = new CronField(DAY_OF_WEEK, fields[5]);
      CronFieldValidation.instance().assertIsValid(info, this.dayOfWeek, failures);
    } catch (AssertionError e) {
      this.dayOfWeek = null;
      this.year = new CronField(YEAR, fields[5]);
      CronFieldValidation.instance().assertIsValid(info, this.year, failures);
    }
  }

  public void assertContainsExactlySeconds(AssertionInfo info, Failures failures, String[] values) {
    assertContainsExactly(seconds, info, failures, values);
  }

  public void assertContainsExactlyMinutes(AssertionInfo info, Failures failures, String[] values) {
    assertContainsExactly(minutes, info, failures, values);
  }

  public void assertContainsExactlyHours(AssertionInfo info, Failures failures, String[] values) {
    assertContainsExactly(hours, info, failures, values);
  }

  public void assertContainsExactlyDayOfMonth(AssertionInfo info, Failures failures, String[] values) {
    assertContainsExactly(dayOfMonth, info, failures, values);
  }

  public void assertContainsExactlyMonth(AssertionInfo info, Failures failures, String[] values) {
    assertContainsExactly(month, info, failures, values);
  }

  public void assertContainsExactlyDayOfWeek(AssertionInfo info, Failures failures, String[] values) {
    assertContainsExactly(dayOfWeek, info, failures, values);
  }

  public void assertContainsExactlyYear(AssertionInfo info, Failures failures, String[] values) {
    assertContainsExactly(year, info, failures, values);
  }

  private void assertContainsExactly(CronField field, AssertionInfo info, Failures failures, String[] values) {
    Objects.instance().assertNotNull(info, field);
    field.parse(info, failures);
    Iterables.instance().assertContainsExactly(info, field.parsedValues(), values);
  }

  private static String resolveMacros(String expression) {
    expression = expression.trim();
    for (int i = 0; i < MACROS.length; i = i + 2) {
      if (MACROS[i].equalsIgnoreCase(expression)) {
        return MACROS[i + 1];
      }
    }
    return expression;
  }
}
