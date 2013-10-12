package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assertj.core.internal.Booleans;
import org.assertj.core.internal.Failures;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link Booleans}</code>.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Booleans#failures} appropriately.
 * 
 * @author Joel Costigliola
 */
public class BooleansBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected Failures failures;
  protected Booleans booleans;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    booleans = new Booleans();
    booleans.failures = failures;
  }

}