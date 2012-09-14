package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.AbsValueComparator;

/**
 * Base class for {@link Bytes} unit tests
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Bytes#failures} appropriately.
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

}