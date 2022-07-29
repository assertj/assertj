package org.assertj.core.api.classes;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;

/**
 * * Tests for <code>{@link ClassAssert#isPrimitive()} ()}</code>. * * @author Manuel Gutierrez
 */
class ClassAssert_isPrimitive_Test extends ClassAssertBaseTest {

  @Override
  protected ClassAssert invoke_api_method() {
    return assertions.isPrimitive();
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertIsPrimitive(getInfo(assertions), getActual(assertions));
  }
}
