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

import static org.assertj.core.test.ExpectedException.none;
import static org.mockito.Mockito.spy;

import java.util.Comparator;
import java.util.Date;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.DateUtil;
import org.assertj.core.util.YearAndMonthComparator;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for {@link Dates} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Dates#failures} appropriately.
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
   * Simply delegate to {@link org.assertj.core.util.DateUtil#parse(String)}
   * @param dateAsString see {@link org.assertj.core.util.DateUtil#parse(String)}
   * @return see {@link org.assertj.core.util.DateUtil#parse(String)}
   */
  protected static Date parseDate(String dateAsString) {
    return DateUtil.parse(dateAsString);
  }

  /**
   * Simply delegate to {@link org.assertj.core.util.DateUtil#parseDatetime(String)}
   * @param dateAsString see {@link org.assertj.core.util.DateUtil#parseDatetime(String)}
   * @return see {@link org.assertj.core.util.DateUtil#parseDatetime(String)}
   */
  protected static Date parseDatetime(String dateAsString) {
    return DateUtil.parseDatetime(dateAsString);
  }

  /**
   * Simply delegate to {@link org.assertj.core.util.DateUtil#parseDatetimeWithMs(String)}}
   * @param dateAsString see {@link org.assertj.core.util.DateUtil#parseDatetimeWithMs(String)} }
   * @return see {@link org.assertj.core.util.DateUtil#parseDatetimeWithMs(String)}}
   */
  protected static Date parseDatetimeWithMs(String dateAsString) {
    return DateUtil.parseDatetimeWithMs(dateAsString);
  }

  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return yearAndMonthComparator;
  }

}