package org.assert4j.core.assertions.internal;

import static org.assert4j.core.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assert4j.core.assertions.internal.Booleans;
import org.assert4j.core.assertions.internal.Failures;
import org.assert4j.core.assertions.test.ExpectedException;
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