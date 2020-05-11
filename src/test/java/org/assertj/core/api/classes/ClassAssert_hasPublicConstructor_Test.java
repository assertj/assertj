package org.assertj.core.api.classes;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;
import org.assertj.core.api.WritableAssertionInfo;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link ClassAssert#hasPublicConstructor(Class[])}</code>.
 *
 * @author phx
 */
public class ClassAssert_hasPublicConstructor_Test extends ClassAssertBaseTest {
  @Override
  protected ClassAssert invoke_api_method() {
    return assertions.hasPublicConstructor(String.class,char.class);
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertHasPublicConstuctors((WritableAssertionInfo) getInfo(assertions),
      getActual(assertions), String.class,char.class);
  }
}
