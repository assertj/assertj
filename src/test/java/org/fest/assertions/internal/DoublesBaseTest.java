package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.AbsValueComparator;

/**
 * Base class for tests involving {@link Doubles}
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Doubles#failures} appropriately.
 * 
 * @author Joel Costigliola
 */
public class DoublesBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected Failures failures;
  protected Doubles doubles;

  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected Doubles doublesWithAbsValueComparisonStrategy;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    doubles = new Doubles();
    doubles.setFailures(failures);
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<Double>());
    doublesWithAbsValueComparisonStrategy = new Doubles(absValueComparisonStrategy);
    doublesWithAbsValueComparisonStrategy.failures = failures;
  }

  protected Double NaN() {
    return doubles.NaN();
  }

}
