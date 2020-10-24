package org.assertj.core.api.classes;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests for <code>{@link ClassAssert#hasPackage(String)}</code>.
 *
 * @author Matteo Mirk
 */
@DisplayName("ClassAssert hasPackage(String)")
class ClassAssert_hasPackage_with_String_Test extends ClassAssertBaseTest {

  private static final String PACKAGE = "org.assertj.core.api";

  @Override
  protected ClassAssert invoke_api_method() {
    return assertions.hasPackage(PACKAGE);
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertHasPackage(getInfo(assertions), getActual(assertions), PACKAGE);
  }

}
