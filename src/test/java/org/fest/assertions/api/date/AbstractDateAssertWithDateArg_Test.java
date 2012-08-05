package org.fest.assertions.api.date;

import static org.fest.assertions.test.ExpectedException.none;
import static org.junit.Assert.assertSame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.fest.assertions.api.DateAssertBaseTest;
import org.fest.assertions.api.DateAssert;
import org.fest.assertions.internal.Dates;
import org.fest.assertions.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * 
 * Abstract class that factorize DateAssert tests with a date arg (either Date or String based).
 * <p>
 * For the most part, date assertion tests are (whatever the concrete date assertion method invoked is) :
 * <ul>
 * <li>successfull assertion test with a date</li>
 * <li>successfull assertion test with a date as string following default date format</li>
 * <li>successfull assertion test with a date as string following custom date format</li>
 * <li>failed assertion test when date as string does not follow the expected date format</li>
 * <li>checking that DateAssert instance used for assertions is returned to allow fluent assertions chaining</li>
 * </ul>
 * 
 * Subclasses are expected to define the invoked assertion method.
 * 
 * @author Joel Costigliola
 * 
 */
public abstract class AbstractDateAssertWithDateArg_Test extends DateAssertBaseTest {

  protected Date otherDate;
  protected String dateAsStringWithDefaultFormat;
  protected String dateAsStringWithCustomFormat;
  protected String dateAsStringWithBadFormat;
  protected SimpleDateFormat customDateFormat;
  @Rule
  public ExpectedException thrown = none();

  @Override
  @Before
  public void setUp() {
    super.setUp();
    customDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    otherDate = new Date();
    dateAsStringWithCustomFormat = "31/12/2000";
    dateAsStringWithDefaultFormat = "2011-01-01";
    dateAsStringWithBadFormat = "31/12/2000"; // expected format is "yyyy-MM-dd"
  }

  @Test
  public void should_verify_assertion_with_date_arg() {
    assertionInvocationWithDateArg();
    verifyAssertionInvocation(otherDate);
  }

  @Test
  public void should_verify_assertion_with_date_arg_string_with_default_format() throws ParseException {
    assertionInvocationWithStringArg(dateAsStringWithDefaultFormat);
    verifyAssertionInvocation(dateFormat().parse(dateAsStringWithDefaultFormat));
  }

  @Test
  public void should_verify_assertion_with_date_arg_string_following_custom_format() throws ParseException {
    assertions.withDateFormat(customDateFormat);
    assertionInvocationWithStringArg(dateAsStringWithCustomFormat);
    verifyAssertionInvocation(dateFormat().parse(dateAsStringWithCustomFormat));
    assertions.withIsoDateFormat();
  }

  @Test
  public void should_fail_because_date_string_representation_does_not_follow_expected_format() {
    thrown.expectAssertionError("Failed to parse " + dateAsStringWithBadFormat + " with date format " + dateFormat());
    assertionInvocationWithStringArg(dateAsStringWithBadFormat);
  }

  @Test
  public void should_return_this() {
    DateAssert returned = assertionInvocationWithDateArg();
    assertSame(assertions, returned);
  }

  /**
   * Overrides to invoke the {@link DateAssert} assertion method under test with the {@link #otherDate} attribute.
   * <p>
   * example with <code>isBefore</code> date assertion:<br>
   * <code>assertions.isBefore(otherDate);</code>
   * 
   * @return the DateAssert instance called
   */
  protected abstract DateAssert assertionInvocationWithDateArg();

  /**
   * Overrides to invoke the {@link DateAssert} assertion method under test with the given date as String.
   * <p>
   * example with <code>isBefore</code> date assertion:<br>
   * <code>assertions.isBefore(date);</code>
   * 
   * @param dateAsString a date in String based format
   * @return the DateAssert instance called
   */
  protected abstract DateAssert assertionInvocationWithStringArg(String dateAsString);

  /**
   * Overrides to verify that the {@link Dates} assertion method was invoked with the given date.
   * <p>
   * example with <code>isBefore</code> date assertion:<br>
   * <code>verify(dates).assertIsBefore(getInfo(assertions), getActual(assertions), date);</code>
   * 
   * @param date the given date (not the actual date !)
   */
  protected abstract void verifyAssertionInvocation(Date date);

}
