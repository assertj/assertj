package org.assert4j.core.assertions.internal;

import static org.assert4j.core.assertions.test.BooleanArrays.arrayOf;
import static org.assert4j.core.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assert4j.core.assertions.internal.BooleanArrays;
import org.assert4j.core.assertions.internal.ComparatorBasedComparisonStrategy;
import org.assert4j.core.assertions.internal.Failures;
import org.assert4j.core.assertions.internal.StandardComparisonStrategy;
import org.assert4j.core.assertions.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link BooleanArrays}</code>, set up an instance with {@link StandardComparisonStrategy} and
 * another with {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link BooleanArrays#failures} appropriately.
 * 
 * @author Joel Costigliola
 */
public class BooleanArraysBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected boolean[] actual;
  protected Failures failures;
  protected BooleanArrays arrays;

  @Before
  public void setUp() {
    actual = arrayOf(true, false);
    failures = spy(new Failures());
    arrays = new BooleanArrays();
    arrays.failures = failures;
  }

}