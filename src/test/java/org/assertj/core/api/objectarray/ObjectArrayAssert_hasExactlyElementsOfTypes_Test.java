package org.assertj.core.api.objectarray;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link ObjectArrayAssert#hasExactlyElementsOfTypes(Class...)} </code>.
 */
class ObjectArrayAssert_hasExactlyElementsOfTypes_Test extends ObjectArrayAssertBaseTest
{
  private final Class<?>[] types = { Integer.class };

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method()
  {
    return assertions.hasExactlyElementsOfTypes(types);
  }

  @Override
  protected void verify_internal_effects()
  {
    verify(arrays).assertHasExactlyElementsOfTypes(getInfo(assertions), getActual(assertions), types);
  }
}
