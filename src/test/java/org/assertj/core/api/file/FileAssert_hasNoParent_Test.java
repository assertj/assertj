package org.assertj.core.api.file;

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link org.assertj.core.api.FileAssert#hasNoParent()}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class FileAssert_hasNoParent_Test extends FileAssertBaseTest {

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.hasNoParent();
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertHasNoParent(getInfo(assertions), getActual(assertions));
  }
}
