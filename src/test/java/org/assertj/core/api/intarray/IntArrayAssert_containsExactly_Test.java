package org.assertj.core.api.intarray;

import static org.assertj.core.test.IntArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.IntArrayAssert;
import org.assertj.core.api.IntArrayAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.IntArrayAssert#containsExactly(int...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class IntArrayAssert_containsExactly_Test extends IntArrayAssertBaseTest {

  @Override
  protected IntArrayAssert invoke_api_method() {
    return assertions.containsExactly(1, 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), arrayOf(1, 2));
  }
}
