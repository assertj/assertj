package org.assertj.core.api.charsequence;

import org.assertj.core.api.CharSequenceAssert;
import org.assertj.core.api.CharSequenceAssertBaseTest;

import static org.mockito.Mockito.verify;

public class CharSequenceAssert_isVisible_Test extends CharSequenceAssertBaseTest {
  @Override
  protected CharSequenceAssert invoke_api_method() {
    return assertions.isVisible();
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertVisible(getInfo(assertions), getActual(assertions));
  }
}
