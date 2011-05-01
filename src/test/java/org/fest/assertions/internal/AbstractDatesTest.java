package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import java.util.Date;

import org.junit.*;

import org.fest.assertions.test.ExpectedException;

public abstract class AbstractDatesTest {

  @Rule
  public ExpectedException thrown = none();
  protected Failures failures;
  protected Dates dates;
  protected Date actual;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    dates = new Dates();
    dates.failures = failures;
  }

  /**
   * Simply delegate to {@link org.fest.util.Dates#parse(String)}
   * @param dateAsString see {@link org.fest.util.Dates#parse(String)}
   * @return see {@link org.fest.util.Dates#parse(String)}
   */
  protected static Date parseDate(String dateAsString) {
    return org.fest.util.Dates.parse(dateAsString);
  }

  /**
   * Simply delegate to {@link org.fest.util.Dates#parseDatetime(String)}
   * @param dateAsString see {@link org.fest.util.Dates#parseDatetime(String)}
   * @return see {@link org.fest.util.Dates#parseDatetime(String)}
   */
  protected static Date parseDatetime(String dateAsString) {
    return org.fest.util.Dates.parseDatetime(dateAsString);
  }

}