package org.assertj.core.api.classes;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;

import static org.mockito.Mockito.verify;

class ClassAssert_isStatic_Test extends ClassAssertBaseTest {

  @Override
  protected ClassAssert invoke_api_method() {
    return assertions.isStatic();
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertIsStatic(getInfo(assertions), getActual(assertions));
  }

}
