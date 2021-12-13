package org.assertj.core.internal.bigdecimals;

import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class BigDecimalAssert_scale_Test {

  @Test
  void should_be_able_to_use_scale_assertions_on_big_decimal_scale() throws Exception {
    //GIVEN
    BigDecimal THREE_DECIMAL = new BigDecimal("1.111");
    //THEN
    assertThat(THREE_DECIMAL).scale().isLessThan(4)
                                     .isGreaterThan(2)
                                     .isPositive()
                             .returnToBigDecimal().hasScaleOf(3);
  }

  @Test
  void should_have_a_helpful_error_message_when_scale_assertion_is_used_on_a_null_big_decimal() {
    //GIVEN
    BigDecimal nullBigDecimal = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(nullBigDecimal).scale().isBetween(2, 3))
                                    .withMessage("Can not perform assertions on the scale of a null BigDecimal");
  }

}
