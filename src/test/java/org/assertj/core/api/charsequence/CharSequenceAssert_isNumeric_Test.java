package org.assertj.core.api.charsequence;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.CharSequenceAssert;
import org.assertj.core.api.CharSequenceAssertBaseTest;

public class CharSequenceAssert_isNumeric_Test extends CharSequenceAssertBaseTest {
  @Override
  protected CharSequenceAssert invoke_api_method() {
    return assertions.isNumeric();
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertNumeric(getInfo(assertions), getActual(assertions));
  }
}
