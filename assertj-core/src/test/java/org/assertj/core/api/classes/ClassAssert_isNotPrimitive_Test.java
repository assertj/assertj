package org.assertj.core.api.classes;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;

/**
 *  * Tests for <code>{@link ClassAssert#isNotPrimitive()} ()} ()}</code>.
 *  *
 *  * @author Manuel Gutierrez
 */
public class ClassAssert_isNotPrimitive_Test extends ClassAssertBaseTest {

  @Override
  protected ClassAssert invoke_api_method() {
    return assertions.isNotPrimitive();
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertIsNotPrimitive(getInfo(assertions), getActual(assertions));

  }
}
