package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

import static org.mockito.Mockito.spy;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.AbsValueComparator;
import org.fest.assertions.util.BigDecimalComparator;

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