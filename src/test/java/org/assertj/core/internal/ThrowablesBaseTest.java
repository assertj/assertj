package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Throwables;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;


/**
 * 
 * Base class for {@link Throwables} tests.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Throwables#failures} appropriately.
 * 
 * @author Joel Costigliola
 */
public class ThrowablesBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected Failures failures;
  protected Throwables throwables;
  protected static Throwable actual;

  @BeforeClass
  public static void setUpOnce() {
    actual = new NullPointerException("Throwable message");
  }

  @Before
  public void setUp() {
    failures = spy(new Failures());
    throwables = new Throwables();
    throwables.failures = failures;
  }

}