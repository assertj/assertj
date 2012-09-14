package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.CaseInsensitiveStringComparator;

/**
 * Base class for testing <code>{@link Lists}</code>, set up an instance with {@link StandardComparisonStrategy} and another with
 * {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Lists#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class ListsBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected ComparatorBasedComparisonStrategy comparisonStrategy;
  protected Lists listsWithCaseInsensitiveComparisonStrategy;

  protected Failures failures;
  protected Lists lists;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    lists = new Lists();
    lists.failures = failures;
    comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance);
    listsWithCaseInsensitiveComparisonStrategy = new Lists(comparisonStrategy);
    listsWithCaseInsensitiveComparisonStrategy.failures = failures;
  }

}