package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.Reader;
import java.io.StringReader;

import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

/**
 * Base class for {@link org.assertj.core.internal.Readers} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link org.assertj.core.internal.Readers}
 * attributes appropriately.
 *
 * @author Joel Costigliola
 * @author Bartosz Bierkowski
 *
 */
public class ReadersBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Diff diff;
  protected Failures failures;
  protected Readers readers;

  protected static Reader actual;
  protected static Reader expected;

  @BeforeClass
  public static void setUpOnce() {
    actual = new StringReader("");
    expected = new StringReader("");
  }

  @Before
  public void setUp() {
    diff = mock(Diff.class);
    failures = spy(new Failures());
    readers = new Readers();
    readers.diff = diff;
    readers.failures = failures;
  }

}