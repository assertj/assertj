package org.assertj.core.api.atomic.referencearray;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link AtomicReferenceArrayAssert#hasExactlyElementsOfTypes(Class...)} </code>.
 */
class AtomicReferenceArrayAssert_hasExactlyElementsOfTypes_Test extends AtomicReferenceArrayAssertBaseTest
{
  private final Class<?>[] types = { Short.class };

  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method()
  {
    return assertions.hasExactlyElementsOfTypes(types);
  }

  @Override
  protected void verify_internal_effects()
  {
    verify(arrays).assertHasExactlyElementsOfTypes(getInfo(assertions), internalArray(), types);
  }
}
