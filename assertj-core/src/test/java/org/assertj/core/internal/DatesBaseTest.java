/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal;

import static org.assertj.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.spy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;

import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.YearAndMonthComparator;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for {@link Dates} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Dates#failures} appropriately.
 *
 * @author Joel Costigliola
 */
public abstract class DatesBaseTest {

  protected static final WritableAssertionInfo INFO = someInfo();

  protected Failures failures;
  protected Dates dates;
  protected ComparatorBasedComparisonStrategy yearAndMonthComparisonStrategy;
  protected Dates datesWithCustomComparisonStrategy;
  protected Date actual;

  private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

  private final YearAndMonthComparator yearAndMonthComparator = new YearAndMonthComparator();

  @BeforeEach
  public void setUp() {
    failures = spy(Failures.instance());
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

  public static Date parseDate(String dateAsString) {
    try {
      return SIMPLE_DATE_FORMAT.get().parse(dateAsString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static Date parseDatetime(String dateAsString) {
    return Date.from(LocalDateTime.parse(dateAsString).atZone(ZoneId.systemDefault()).toInstant());
  }

  protected Comparator<?> comparatorForCustomComparisonStrategy() {
    return yearAndMonthComparator;
  }

}
