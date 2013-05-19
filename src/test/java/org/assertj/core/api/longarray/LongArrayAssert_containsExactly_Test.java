package org.assertj.core.api.longarray;

import static org.assertj.core.test.LongArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.LongArrayAssert;
import org.assertj.core.api.LongArrayAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.LongArrayAssert#containsExactly(long...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class LongArrayAssert_containsExactly_Test extends LongArrayAssertBaseTest {

  @Override
  protected LongArrayAssert invoke_api_method() {
    return assertions.containsExactly(1L, 2L);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), arrayOf(1L, 2L));
  }
}
