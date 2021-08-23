package org.assertj.core.api.bigdecimal;

import org.assertj.core.api.BigDecimalAssert;
import org.assertj.core.api.BigDecimalAssertBaseTest;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link BigDecimalAssert#isScaleEqualTo(int)} ()}</code>.
 *
 * @author Almir Lucena
 */
class BigDecimalsAssert_assertScaleIsEqual_Test extends BigDecimalAssertBaseTest {

  int EXPECTED_SCALE = 2;

  BigDecimal TWO_SCALED_BIG_DECIMAL = new BigDecimal("1.00");

  @Override
  protected BigDecimalAssert create_assertions() {
    return new BigDecimalAssert(TWO_SCALED_BIG_DECIMAL);
  }

  @Override
  protected BigDecimalAssert invoke_api_method() {
    return assertions.isScaleEqualTo(EXPECTED_SCALE);
  }

  @Override
  protected void verify_internal_effects() {
    verify(bigDecimals).assertEqual(getInfo(assertions), getActual(assertions).scale(), EXPECTED_SCALE);
  }

}
