package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.AbsValueComparator;
import org.fest.util.ComparatorBasedComparisonStrategy;

public class AbstractTest_for_Longs {

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