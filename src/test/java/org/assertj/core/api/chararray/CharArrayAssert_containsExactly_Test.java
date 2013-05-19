package org.assertj.core.api.chararray;

import static org.assertj.core.test.CharArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.CharArrayAssert;
import org.assertj.core.api.CharArrayAssertBaseTest;

/**
 * Tests for <code>{@link org.assertj.core.api.CharArrayAssert#containsExactly(char...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class CharArrayAssert_containsExactly_Test extends CharArrayAssertBaseTest {

  @Override
  protected CharArrayAssert invoke_api_method() {
    return assertions.containsExactly('a', 'b');
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertEqual(getInfo(assertions), getActual(assertions), arrayOf('a', 'b'));
  }
}
