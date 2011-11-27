package org.fest.assertions.util;

import static java.lang.Math.abs;

import java.util.Comparator;

public class AbsValueComparator<NUMBER extends Number> implements Comparator<NUMBER> {

  public int compare(NUMBER i1, NUMBER i2) {
    double diff = abs(i1.doubleValue()) - abs(i2.doubleValue());
    if (diff == 0.0) return 0;
    return diff < 0.0 ? -1 : 1;
  }
}
