package org.assertj.core.api.doublearray;

import static org.assertj.core.test.DoubleArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.DoubleArrayAssert;
import org.assertj.core.api.DoubleArrayAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.DoubleArrayAssert#containsExactly(double...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class DoubleArrayAssert_containsExactly_Test extends DoubleArrayAssertBaseTest {

  @Override
  protected DoubleArrayAssert invoke_api_method() {
    return assertions.containsExactly(1d, 2d);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), arrayOf(1d, 2d));
  }
}
