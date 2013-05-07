package org.assertj.core.api.charsequence;

import org.assertj.core.api.CharSequenceAssert;
import org.assertj.core.api.CharSequenceAssertBaseTest;

import static org.mockito.Mockito.verify;



/**
 * Tests for <code>{@link CharSequenceAssert#containsOnlyOnce(CharSequence)}</code>.
 * 
 * @author Pauline Iogna
 * @author Joel Costigliola
 */
public class CharSequenceAssert_containsOnlyOnce_Test extends CharSequenceAssertBaseTest {

  @Override
  protected CharSequenceAssert invoke_api_method() {
    return assertions.containsOnlyOnce("od");
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertContainsOnlyOnce(getInfo(assertions), getActual(assertions), "od");
  }
}