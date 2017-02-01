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
package org.assertj.core.util;

import static org.assertj.core.util.DateUtil.*;

import java.util.Comparator;
import java.util.Date;

/**
 * 
 * Compares two date by looking at their year and month only, thus 2011-01-01 and 2011-01-31 are considered equal.
 * 
 * @author Joel Costigliola
 * 
 */
public class YearAndMonthComparator implements Comparator<Date> {

  public final static YearAndMonthComparator instance = new YearAndMonthComparator();

  @Override
  public int compare(Date date1, Date date2) {
    if (yearOf(date1) != yearOf(date2)) { return yearOf(date1) - yearOf(date2); }
    return monthOf(date1) - monthOf(date2);
  }
}