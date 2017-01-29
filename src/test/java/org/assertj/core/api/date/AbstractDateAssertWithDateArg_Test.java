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
package org.assertj.core.api.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.assertj.core.api.DateAssert;
import org.assertj.core.api.DateAssertBaseTest;
import org.assertj.core.internal.Dates;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Abstract class that factorize DateAssert tests with a date arg (either Date or String based).
 * <p/>
 * For the most part, date assertion tests are (whatever the concrete date assertion method invoked is) :
 * <ul>
 * <li>successful assertion test with a date</li>
 * <li>successful assertion test with a date as string following default date format</li>
 * <li>successful assertion test with a date as string following custom date format</li>
 * <li>failed assertion test when date as string does not follow the expected date format</li>
 * <li>checking that DateAssert instance used for assertions is returned to allow fluent assertions chaining</li>
 * </ul>
 * <p/>
 * Subclasses are expected to define the invoked assertion method.
 *
 * @author Joel Costigliola
 */
public abstract class AbstractDateAssertWithDateArg_Test extends DateAssertBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Date otherDate;
  protected String dateAsStringWithDefaultFormat;
  protected String dateAsStringWithCustomFormat;
  protected String dateAsStringWithBadFormat;
  protected SimpleDateFormat customDateFormat;

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
  public void should_verify_assertion_with_date_arg_string_with_default_format() {
    assertionInvocationWithStringArg(dateAsStringWithDefaultFormat);
    verifyAssertionInvocation(parse(dateAsStringWithDefaultFormat));
  }

  @Test
  public void should_verify_assertion_with_date_arg_string_following_custom_format() {
    assertions.withDateFormat(customDateFormat);
    assertionInvocationWithStringArg(dateAsStringWithCustomFormat);
    verifyAssertionInvocation(parse(dateAsStringWithCustomFormat));
    assertions.withDefaultDateFormatsOnly();
  }

  @Test
  public void should_fail_because_date_string_representation_does_not_follow_expected_format() {
    thrown.expectAssertionError("Failed to parse " + dateAsStringWithBadFormat +
                                " with any of these date formats:%n" +
                                "   [yyyy-MM-dd'T'HH:mm:ss.SSS,%n" +
                                "    yyyy-MM-dd HH:mm:ss.SSS,%n" +
                                "    yyyy-MM-dd'T'HH:mm:ss,%n" +
                                "    yyyy-MM-dd]");
    assertionInvocationWithStringArg(dateAsStringWithBadFormat);
  }

  @Test
  public void should_return_this() {
    DateAssert returned = assertionInvocationWithDateArg();
    assertThat(returned).isSameAs(assertions);
  }

  /**
   * Overrides to invoke the {@link DateAssert} assertion method under test with the {@link #otherDate} attribute.
   * <p/>
   * example with <code>isBefore</code> date assertion:<br>
   * <code>assertions.isBefore(otherDate);</code>
   *
   * @return the DateAssert instance called
   */
  protected abstract DateAssert assertionInvocationWithDateArg();

  /**
   * Overrides to invoke the {@link DateAssert} assertion method under test with the given date as String.
   * <p/>
   * example with <code>isBefore</code> date assertion:<br>
   * <code>assertions.isBefore(date);</code>
   *
   * @param dateAsString a date in String based format
   * @return the DateAssert instance called
   */
  protected abstract DateAssert assertionInvocationWithStringArg(String dateAsString);

  /**
   * Overrides to verify that the {@link Dates} assertion method was invoked with the given date.
   * <p/>
   * example with <code>isBefore</code> date assertion:<br>
   * <code>verify(dates).assertIsBefore(getInfo(assertions),
   * getActual(assertions), date);</code>
   *
   * @param date the given date (not the actual date !)
   */
  protected abstract void verifyAssertionInvocation(Date date);

}
