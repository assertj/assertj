package org.assertj.core.internal;

import static org.assertj.core.test.BooleanArrays.arrayOf;
import static org.assertj.core.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link BooleanArrays}</code>, set up an instance with {@link StandardComparisonStrategy} and
 * another with {@link ComparatorBasedComparisonStrategy}.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link BooleanArrays#failures} appropriately.
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