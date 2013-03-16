package org.assertj.core.assertions.internal;

import static org.assertj.core.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assertj.core.assertions.internal.Booleans;
import org.assertj.core.assertions.internal.Failures;
import org.assertj.core.assertions.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link Booleans}</code>.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Booleans#failures} appropriately.
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