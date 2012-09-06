package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;

/**
 * Base class for {@link InputStreams} unit tests
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link InputStreams} attributes appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class InputStreamsBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Diff diff;
  protected Failures failures;
  protected InputStreams inputStreams;

  protected static InputStream actual;
  protected static InputStream expected;

  @BeforeClass
  public static void setUpOnce() {
    actual = new ByteArrayInputStream(new byte[0]);
    expected = new ByteArrayInputStream(new byte[0]);
  }

  @Before
  public void setUp() {
    diff = mock(Diff.class);
    failures = spy(new Failures());
    inputStreams = new InputStreams();
    inputStreams.diff = diff;
    inputStreams.failures = failures;
  }

}