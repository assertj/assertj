package org.assertj.core.api.classes;

/**
 * @author phx
 */

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ClassAssertBaseTest;
import org.assertj.core.api.WritableAssertionInfo;

/**
 * Tests for <code>{@link ClassAssert#hasConstructor(Class[])}</code>.
 *
 * @author phx
 */

public class ClassAssert_hasConstructors_Test extends ClassAssertBaseTest {
  @Override
  protected ClassAssert invoke_api_method() {
    return assertions.hasConstructor(String.class,char.class);
  }

  @Override
  protected void verify_internal_effects() {
    verify(classes).assertHasConstuctors((WritableAssertionInfo) getInfo(assertions),
      getActual(assertions), String.class,char.class);
  }

}
