package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.File;

import org.assertj.core.internal.BinaryDiff;
import org.assertj.core.internal.Diff;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Files;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link Files}</code>, set up diff and failures attributes (which is why it is in
 * <code>org.assertj.core.internal</code> package.
 * 
 * @author Joel Costigliola
 */
public class FilesBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected File actual;
  protected Failures failures;
  protected Files files;
  protected Diff diff;
  protected BinaryDiff binaryDiff;

  @Before
  public void setUp() {
    actual = mock(File.class);
    failures = spy(new Failures());
    files = new Files();
    files.failures = failures;
    diff = mock(Diff.class);
    files.diff = diff;
    binaryDiff = mock(BinaryDiff.class);
    files.binaryDiff = binaryDiff;
  }

}