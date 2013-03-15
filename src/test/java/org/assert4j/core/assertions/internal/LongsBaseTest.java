package org.assert4j.core.assertions.internal;

import static org.assert4j.core.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assert4j.core.assertions.internal.ComparatorBasedComparisonStrategy;
import org.assert4j.core.assertions.internal.Failures;
import org.assert4j.core.assertions.internal.Longs;
import org.assert4j.core.assertions.internal.StandardComparisonStrategy;
import org.assert4j.core.assertions.test.ExpectedException;
import org.assert4j.core.assertions.util.AbsValueComparator;
import org.junit.Before;
import org.junit.Rule;


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