package org.assertj.core.api.objectarray;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;

import static org.mockito.Mockito.verify;

public class ObjectArrayAssert_hasExactlyElementsOfTypes_Test extends ObjectArrayAssertBaseTest
{
  @Override
  protected ObjectArrayAssert<Object> invoke_api_method()
  {
    return assertions.hasExactlyElementsOfTypes(Integer.class);
  }

  @Override
  protected void verify_internal_effects()
  {
    verify(arrays).assertHasExactlyElementsOfTypes(getInfo(assertions), getActual(assertions), Integer.class);
  }
}
