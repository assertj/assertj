package org.fest.assertions.util;

import static org.fest.util.Dates.*;

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

  public int compare(Date date1, Date date2) {
    if (yearOf(date1) != yearOf(date2)) { return yearOf(date1) - yearOf(date2); }
    return monthOf(date1) - monthOf(date2);
  }
}