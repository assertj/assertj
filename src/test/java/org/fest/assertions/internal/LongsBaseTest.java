package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.AbsValueComparator;

/**
 * Base class for testing <code>{@link Longs}</code>, set up an instance with {@link StandardComparisonStrategy} and another with
 * {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Longs#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class LongsBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected Failures failures;
  protected Longs longs;

  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected Longs longsWithAbsValueComparisonStrategy;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    longs = new Longs();
    longs.setFailures(failures);
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<Long>());
    longsWithAbsValueComparisonStrategy = new Longs(absValueComparisonStrategy);
    longsWithAbsValueComparisonStrategy.failures = failures;
  }

}