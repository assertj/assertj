package org.assertj.core.api.atomic.referencearray;

import org.assertj.core.api.AtomicReferenceArrayAssert;
import org.assertj.core.api.AtomicReferenceArrayAssertBaseTest;

import static org.mockito.Mockito.verify;

public class AtomicReferenceArrayAssert_hasExactlyElementsOfTypes_Test extends AtomicReferenceArrayAssertBaseTest
{
  @Override
  protected AtomicReferenceArrayAssert<Object> invoke_api_method()
  {
    return assertions.hasExactlyElementsOfTypes(Short.class);
  }

  @Override
  protected void verify_internal_effects()
  {
    verify(arrays).assertHasExactlyElementsOfTypes(getInfo(assertions), internalArray(), Short.class);
  }
}
