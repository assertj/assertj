package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.AbsValueComparator;

/**
 * Base class for testing <code>{@link Integers}</code>, set up an instance with {@link StandardComparisonStrategy} and another
 * with {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Integers#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class IntegersBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected Failures failures;
  protected Integers integers;

  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected Integers integersWithAbsValueComparisonStrategy;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    integers = new Integers();
    integers.setFailures(failures);
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<Integer>());
    integersWithAbsValueComparisonStrategy = new Integers(absValueComparisonStrategy);
    integersWithAbsValueComparisonStrategy.failures = failures;
  }

}