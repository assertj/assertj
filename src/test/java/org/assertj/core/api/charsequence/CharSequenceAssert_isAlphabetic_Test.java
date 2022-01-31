package org.assertj.core.api.charsequence;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.CharSequenceAssert;
import org.assertj.core.api.CharSequenceAssertBaseTest;

public class CharSequenceAssert_isAlphabetic_Test extends CharSequenceAssertBaseTest {
  @Override
  protected CharSequenceAssert invoke_api_method() {
    return assertions.isAlphabetic();
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertMatches(getInfo(assertions), getActual(assertions), "\\p{Alpha}");
  }
}
