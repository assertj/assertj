package org.fest.assertions.util;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 
 * A {@link BigDecimal} {@link Comparator} based on {@link BigDecimal#compareTo(BigDecimal)}.<br>
 * Is useful if ones wants to use BigDecimal assertions based on {@link BigDecimal#compareTo(BigDecimal)} instead of
 * {@link BigDecimal#equals(Object)} method.
 * 
 * @author Joel Costigliola
 */
public class BigDecimalComparator implements Comparator<BigDecimal> {

  /**
   * an instance of {@link BigDecimalComparator}.
   */
  public static final BigDecimalComparator BIG_DECIMAL_COMPARATOR = new BigDecimalComparator();

  public int compare(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
    return bigDecimal1.compareTo(bigDecimal2);
  }
}
