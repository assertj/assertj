package org.assertj.core.api.path;

import org.assertj.core.api.PathAssert;
import org.assertj.core.api.PathAssertBaseTest;
import org.junit.jupiter.api.DisplayName;

import java.nio.charset.Charset;

import static org.mockito.Mockito.verify;

/**
 * Test for <code>{@link PathAssert#usingCharset(Charset)}</code> when the provided charset is null.
 */
@DisplayName("PathAssert isEmptyPath")
public class PathAssert_isEmptyPath_Test extends PathAssertBaseTest {

  @Override
  protected PathAssert invoke_api_method() {
    return assertions.isEmpty();
  }

  @Override
  protected void verify_internal_effects() {
    verify(paths).assertIsEmptyPath(getInfo(assertions), getActual(assertions));
  }

}
