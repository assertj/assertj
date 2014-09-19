package org.assertj.core.api.objectarray;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;

/**
 * Tests for {@link ObjectArrayAssert#hasAtLeastOneElementOfType(Class)}.
 */
public class ObjectArrayAssert_hasAtLeastOneElementOfType_Test extends ObjectArrayAssertBaseTest {

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.hasAtLeastOneElementOfType(String.class);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertHasAtLeastOneElementOfType(getInfo(assertions), getActual(assertions), String.class);
  }
}