package org.assertj.core.api.file;

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link org.assertj.core.api.FileAssert#hasName(String)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class FileAssert_hasName_Test extends FileAssertBaseTest {

  private String expected = "name";

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.hasName(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertHasName(getInfo(assertions), getActual(assertions), expected);
  }
}
