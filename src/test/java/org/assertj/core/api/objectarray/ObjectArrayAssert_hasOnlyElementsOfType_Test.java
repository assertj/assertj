package org.assertj.core.api.objectarray;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;

/**
 * Tests for {@link ObjectArrayAssert#hasOnlyElementsOfType(Class)}.
 */
public class ObjectArrayAssert_hasOnlyElementsOfType_Test extends ObjectArrayAssertBaseTest {

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.hasOnlyElementsOfType(String.class);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertHasOnlyElementsOfType(getInfo(assertions), getActual(assertions), String.class);
  }
}