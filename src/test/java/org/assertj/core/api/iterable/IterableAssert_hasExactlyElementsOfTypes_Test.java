package org.assertj.core.api.iterable;

import org.assertj.core.api.*;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link AbstractIterableAssert#hasExactlyElementsOfTypes(Class...)}</code>.
 */
class IterableAssert_hasExactlyElementsOfTypes_Test extends ObjectArrayAssertBaseTest
{
  @Override
  protected ObjectArrayAssert<Object> invoke_api_method()
  {
    return assertions.hasExactlyElementsOfTypes(Integer.class, Double.class);
  }

  @Override
  protected void verify_internal_effects()
  {
    verify(arrays).assertHasExactlyElementsOfTypes(getInfo(assertions), getActual(assertions), Integer.class, Double.class);
  }
}
