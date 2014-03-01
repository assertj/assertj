package org.assertj.core.api.file;

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link FileAssert#hasParent(java.io.File)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class FileAssert_hasParent_Test extends FileAssertBaseTest {

  private File parent = mock(File.class);

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.hasParent(parent);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertHasParent(getInfo(assertions), getActual(assertions), parent);
  }
}
