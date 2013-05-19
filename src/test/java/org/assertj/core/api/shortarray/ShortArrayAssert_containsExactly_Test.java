package org.assertj.core.api.shortarray;

import static org.assertj.core.test.ShortArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ShortArrayAssert;
import org.assertj.core.api.ShortArrayAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.ShortArrayAssert#containsExactly(short...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class ShortArrayAssert_containsExactly_Test extends ShortArrayAssertBaseTest {

  @Override
  protected ShortArrayAssert invoke_api_method() {
    return assertions.containsExactly((short) 1, (short) 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), arrayOf(1, 2));
  }
}
