package org.assertj.core.api.classes;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;
import org.assertj.core.api.WritableAssertionInfo;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link org.assertj.core.api.ClassAssert#hasPrivateConstructor(Class[])}</code>.
 *
 * @author phx
 */
public class ClassAssert_hasPrivateConstructor_Test extends ClassAssertBaseTest {
  @Override
  protected ClassAssert invoke_api_method() {
    return assertions.hasPrivateConstructor(String.class,char.class);
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertHasPrivateConstuctors((WritableAssertionInfo) getInfo(assertions),
      getActual(assertions), String.class,char.class);
  }
}
