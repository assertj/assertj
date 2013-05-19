package org.assertj.core.api.booleanarray;

import static org.assertj.core.test.BooleanArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.BooleanArrayAssert;
import org.assertj.core.api.BooleanArrayAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.BooleanArrayAssert#containsExactly(boolean...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class BooleanArrayAssert_containsExactly_Test extends BooleanArrayAssertBaseTest {

  @Override
  protected BooleanArrayAssert invoke_api_method() {
    return assertions.containsExactly(true, false);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), arrayOf(true, false));
  }
}
