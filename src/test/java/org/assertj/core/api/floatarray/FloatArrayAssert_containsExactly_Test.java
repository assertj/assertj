package org.assertj.core.api.floatarray;

import static org.assertj.core.test.FloatArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.FloatArrayAssert;
import org.assertj.core.api.FloatArrayAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.FloatArrayAssert#containsExactly(float...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class FloatArrayAssert_containsExactly_Test extends FloatArrayAssertBaseTest {

  @Override
  protected FloatArrayAssert invoke_api_method() {
    return assertions.containsExactly(1.0f, 2.0f);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), arrayOf(1.0f, 2.0f));
  }
}
