package org.assertj.core.api.charsequence;

import org.assertj.core.api.CharSequenceAssert;
import org.assertj.core.api.CharSequenceAssertBaseTest;

import java.util.Arrays;

import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link CharSequenceAssert#containsExactly(Iterable<CharSequence>)}</code>.
 *
 * @author Billy Yuan
 */
public class CharSequenceAssert_containsExactly_Test extends CharSequenceAssertBaseTest {

  @Override
  protected CharSequenceAssert invoke_api_method() {
    return assertions.containsExactly(Arrays.<CharSequence>asList("od", "do"));
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertContainsExactly(getInfo(assertions), getActual(assertions), array("od", "do"));
  }
}
