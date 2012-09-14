package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.internal.Failures;
import org.fest.assertions.internal.Shorts;
import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.AbsValueComparator;

/**
 * 
 * Base class for {@link Shorts} tests.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Shorts#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class ShortsBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected Failures failures;
  protected Shorts shorts;

  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected Shorts shortsWithAbsValueComparisonStrategy;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    shorts = new Shorts();
    shorts.setFailures(failures);
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<Short>());
    shortsWithAbsValueComparisonStrategy = new Shorts(absValueComparisonStrategy);
    shortsWithAbsValueComparisonStrategy.failures = failures;
  }

}