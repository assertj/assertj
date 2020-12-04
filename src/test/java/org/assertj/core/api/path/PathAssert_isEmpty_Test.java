package org.assertj.core.api.path;

import org.assertj.core.api.PathAssert;
import org.assertj.core.api.PathAssertBaseTest;
import org.junit.jupiter.api.DisplayName;

import static org.mockito.Mockito.verify;

/**
 * Test for <code>{@link PathAssert#isEmpty()}</code>.
 */
@DisplayName("PathAssert isEmpty")
class PathAssert_isEmpty_Test extends PathAssertBaseTest {

  @Override
  protected PathAssert invoke_api_method() {
    return assertions.isEmpty();
  }

  @Override
  protected void verify_internal_effects() {
    verify(paths).assertIsEmptyFile(getInfo(assertions), getActual(assertions));
  }

}
