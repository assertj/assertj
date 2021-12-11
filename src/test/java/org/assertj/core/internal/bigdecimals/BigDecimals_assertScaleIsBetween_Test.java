package org.assertj.core.internal.bigdecimals;

import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class BigDecimals_assertScaleIsBetween_Test extends BigDecimalsBaseTest {

  @Test
  public void should_be_able_to_use_scale_assertions_on_big_decimal_scale() throws Exception {
    //THEN
    assertThat(ONE_WITH_3_DECIMALS).scale()
        .isLessThan(4)
        .isGreaterThan(2)
        .isPositive()
        .returnToBigDecimal().hasScaleOf(3);
  }

  @Test
  void should_have_a_helpful_error_message_when_scale_is_used_on_a_null_big_decimal() {
    //GIVEN
    BigDecimal nullBigDecimal = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(nullBigDecimal).scale().isBetween(2, 3))
        .withMessage("Can not perform assertions on the scale of a null BigDecimal");
  }

}
