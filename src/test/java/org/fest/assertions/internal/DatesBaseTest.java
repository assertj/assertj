package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import java.util.Comparator;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.YearAndMonthComparator;

/**
 * Base class for {@link Dates} unit tests
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Dates#failures} appropriately.
 * 
 * @author Joel Costigliola
 */
public abstract class DatesBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Failures failures;
  protected Dates dates;
  protected ComparatorBasedComparisonStrategy yearAndMonthComparisonStrategy;
  protected Dates datesWithCustomComparisonStrategy;
  protected Date actual;

  private YearAndMonthComparator yearAndMonthComparator = new YearAndMonthComparator();

  @Before
  public void setUp() {
    failures = spy(new Failures());
    dates = new Dates();
    dates.failures = failures;
    yearAndMonthComparisonStrategy = new ComparatorBasedComparisonStrategy(comparatorForCustomComparisonStrategy());
    datesWithCustomComparisonStrategy = new Dates(yearAndMonthComparisonStrategy);
    datesWithCustomComparisonStrategy.failures = failures;
    initActualDate();
  }

  protected void initActualDate() {
    actual = parseDate("2011-01-01");
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

  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return yearAndMonthComparator;
  }

}