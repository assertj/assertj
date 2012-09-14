package org.fest.assertions.internal;

import static org.fest.assertions.test.BooleanArrays.arrayOf;
import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;

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