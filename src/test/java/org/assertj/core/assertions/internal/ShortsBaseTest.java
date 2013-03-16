package org.assertj.core.assertions.internal;

import static org.assertj.core.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assertj.core.assertions.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.assertions.internal.Failures;
import org.assertj.core.assertions.internal.Shorts;
import org.assertj.core.assertions.test.ExpectedException;
import org.assertj.core.assertions.util.AbsValueComparator;
import org.junit.Before;
import org.junit.Rule;


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