package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Floats;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.AbsValueComparator;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link Floats}</code>, set up an instance with {@link StandardComparisonStrategy} and another with
 * {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Floats#failures} appropriately and to use
 * {@link Floats#NaN()}.
 * 
 * @author Joel Costigliola
 */
public class FloatsBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Failures failures;
  protected Floats floats;

  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected Floats floatsWithAbsValueComparisonStrategy;

  public FloatsBaseTest() {
    super();
  }

  @Before
  public void setUp() {
    failures = spy(new Failures());
    floats = new Floats();
    floats.setFailures(failures);
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<Float>());
    floatsWithAbsValueComparisonStrategy = new Floats(absValueComparisonStrategy);
    floatsWithAbsValueComparisonStrategy.failures = failures;
  }

  protected Float NaN() {
    return floats.NaN();
  }

}