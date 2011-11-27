package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.util.Collections.list;

import static org.mockito.Mockito.spy;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.CaseInsensitiveStringComparator;
import org.fest.util.ComparatorBasedComparisonStrategy;

public class AbstractTest_for_Collections {

  @Rule
  public ExpectedException thrown = none();

  protected List<String> actual;
  protected Failures failures;
  protected Collections collections;

  protected ComparatorBasedComparisonStrategy comparisonStrategy;
  protected Collections collectionsWithCaseInsensitiveComparisonStrategy;
  
  @Before
  public void setUp() {
    actual = list("Luke", "Yoda", "Leia");
    failures = spy(new Failures());
    collections = new Collections();
    collections.failures = failures;
    comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance);
    collectionsWithCaseInsensitiveComparisonStrategy = new Collections(comparisonStrategy);
    collectionsWithCaseInsensitiveComparisonStrategy.failures = failures;
  }

}