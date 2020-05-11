package org.assertj.core.api.classes;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;
import org.assertj.core.api.WritableAssertionInfo;

/**
 * Tests for <code>{@link ClassAssert#hasPrivateConstructor(Class[])}</code>.
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
