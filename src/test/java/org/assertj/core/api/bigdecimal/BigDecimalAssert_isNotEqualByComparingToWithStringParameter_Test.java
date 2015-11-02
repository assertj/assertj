package org.assertj.core.api.bigdecimal;

import org.assertj.core.api.BigDecimalAssert;
import org.assertj.core.api.BigDecimalAssertBaseTest;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link BigDecimalAssert#isNotEqualByComparingTo(String)}</code>.
 *
 * @author Guillaume Husta
 */
public class BigDecimalAssert_isNotEqualByComparingToWithStringParameter_Test extends BigDecimalAssertBaseTest {

  @Override
  protected BigDecimalAssert invoke_api_method() {
      return assertions.isNotEqualByComparingTo(ONE_AS_STRING);
  }

  @Override
  protected void verify_internal_effects() {
    verify(comparables).assertNotEqualByComparison(getInfo(assertions), getActual(assertions), new BigDecimal(ONE_AS_STRING));
  }

}
