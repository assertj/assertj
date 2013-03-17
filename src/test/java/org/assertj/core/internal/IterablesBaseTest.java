package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;


import static org.mockito.Mockito.spy;

import java.util.List;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link Iterables}</code>, set up an instance with {@link StandardComparisonStrategy} and another
 * with {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Iterables#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class IterablesBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected List<String> actual;
  protected Failures failures;
  protected Iterables iterables;

  protected ComparatorBasedComparisonStrategy comparisonStrategy;
  protected Iterables iterablesWithCaseInsensitiveComparisonStrategy;

  @Before
  public void setUp() {
    actual = newArrayList("Luke", "Yoda", "Leia");
    failures = spy(new Failures());
    iterables = new Iterables();
    iterables.failures = failures;
    comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance);
    iterablesWithCaseInsensitiveComparisonStrategy = new Iterables(comparisonStrategy);
    iterablesWithCaseInsensitiveComparisonStrategy.failures = failures;
  }

}