package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;
import static org.mockito.Mockito.spy;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.AbsValueComparator;
import org.junit.Before;
import org.junit.Rule;

/**
 * Base class for {@link Bytes} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Bytes#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class BytesBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Failures failures;
  protected Bytes bytes;
  protected ComparatorBasedComparisonStrategy absValueComparisonStrategy;
  protected Bytes bytesWithAbsValueComparisonStrategy;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    bytes = new Bytes();
    bytes.setFailures(failures);
    absValueComparisonStrategy = new ComparatorBasedComparisonStrategy(new AbsValueComparator<Byte>());
    bytesWithAbsValueComparisonStrategy = new Bytes(absValueComparisonStrategy);
    bytesWithAbsValueComparisonStrategy.failures = failures;
  }

  protected void resetFailures() {
    bytes.resetFailures();
    bytesWithAbsValueComparisonStrategy.resetFailures();
  }
}
