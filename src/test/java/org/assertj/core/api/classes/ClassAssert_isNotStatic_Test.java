package org.assertj.core.api.classes;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;

import static org.mockito.Mockito.verify;

class ClassAssert_isNotStatic_Test extends ClassAssertBaseTest {

  @Override
  protected ClassAssert invoke_api_method() {
    return assertions.isNotStatic();
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertIsNotStatic(getInfo(assertions), getActual(assertions));
  }
}
