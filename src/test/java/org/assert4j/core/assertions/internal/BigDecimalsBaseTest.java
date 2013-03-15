package org.assert4j.core.assertions.internal;

import static org.assert4j.core.assertions.test.ExpectedException.none;
import static org.assert4j.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;


import static org.mockito.Mockito.spy;

import java.math.BigDecimal;

import org.assert4j.core.assertions.internal.BigDecimals;
import org.assert4j.core.assertions.internal.ComparatorBasedComparisonStrategy;
import org.assert4j.core.assertions.internal.Failures;
import org.assert4j.core.assertions.test.ExpectedException;
import org.assert4j.core.assertions.util.AbsValueComparator;
import org.assert4j.core.util.BigDecimalComparator;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for {@link BigDecimals} unit tests
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link BigDecimals#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class BigDecimalsBaseTest {

  protected static final BigDecimal ONE_WITH_3_DECIMALS = new BigDecimal("1.000");

  @Rule
  public ExpectedException thrown = none();
  protected Failures failures;
  protected BigDecimals bigDecimals;

  protected ComparatorBasedComparisonStrategy comparatorComparisonStrategy;
  /**
   * {@link BigDecimals} using a comparison strategy based on {@link BigDecimalComparator}.
   */
  protected BigDecimals bigDecimalsWithComparatorComparisonStrategy;
  // another BigDecimals with a custom ComparisonStrategy other than bigDecimalsWithComparatorComparisonStrategy
  protected BigDecimals bigDecimalsWithAbsValueComparisonStrategy;
  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    bigDecimals = new BigDecimals();
    bigDecimals.setFailures(failures);
    comparatorComparisonStrategy = new ComparatorBasedComparisonStrategy(BIG_DECIMAL_COMPARATOR);
    bigDecimalsWithComparatorComparisonStrategy = new BigDecimals(comparatorComparisonStrategy);
    bigDecimalsWithComparatorComparisonStrategy.failures = failures;
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<BigDecimal>());
    bigDecimalsWithAbsValueComparisonStrategy = new BigDecimals(absValueComparisonStrategy);
    bigDecimalsWithAbsValueComparisonStrategy.failures = failures;
  }
}