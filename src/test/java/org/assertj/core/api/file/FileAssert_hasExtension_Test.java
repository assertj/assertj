package org.assertj.core.api.file;

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link org.assertj.core.api.FileAssert#hasExtension(String)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class FileAssert_hasExtension_Test extends FileAssertBaseTest {

  private String extension = "java";

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.hasExtension(extension);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertHasExtension(getInfo(assertions), getActual(assertions), extension);
  }
}
