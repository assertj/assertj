package org.assertj.core.api.charsequence;

import org.assertj.core.api.CharSequenceAssert;
import org.assertj.core.api.CharSequenceAssertBaseTest;

import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link CharSequenceAssert#containsExactly(CharSequence...)}</code>.
 *
 * @author Billy Yuan
 */
public class CharSequenceAssert_containsExactly_with_var_args_Test extends CharSequenceAssertBaseTest {

  @Override
  protected CharSequenceAssert invoke_api_method() {
    return assertions.containsExactly("od", "do");
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertContainsExactly(getInfo(assertions), getActual(assertions), array("od", "do"));
  }
}
