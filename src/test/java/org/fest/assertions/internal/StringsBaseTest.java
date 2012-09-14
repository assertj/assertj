package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.CaseInsensitiveStringComparator;

/**
 * 
 * Base class for {@link Strings} tests.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Strings#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class StringsBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected Failures failures;
  protected Strings strings;

  protected ComparatorBasedComparisonStrategy comparisonStrategy;
  protected Strings stringsWithCaseInsensitiveComparisonStrategy;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    strings = new Strings();
    strings.failures = failures;
    comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance);
    stringsWithCaseInsensitiveComparisonStrategy = new Strings(comparisonStrategy);
    stringsWithCaseInsensitiveComparisonStrategy.failures = failures;
  }

}