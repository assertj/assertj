package org.assertj.core.api.bytearray;

import static org.assertj.core.test.ByteArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteArrayAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.ByteArrayAssert#containsExactly(byte...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class ByteArrayAssert_containsExactly_Test extends ByteArrayAssertBaseTest {

  @Override
  protected ByteArrayAssert invoke_api_method() {
    return assertions.containsExactly((byte) 1, (byte) 2);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), arrayOf(1, 2));
  }
}
