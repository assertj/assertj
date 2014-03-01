package org.assertj.core.api.file;

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;

import java.io.File;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link org.assertj.core.api.FileAssert#hasParent(String)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class FileAssert_hasParentWithStringParameter_Test extends FileAssertBaseTest {

  private String parent = "parent";

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.hasParent(parent);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertHasParent(getInfo(assertions), getActual(assertions), new File(parent));
  }
}
