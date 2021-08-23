package org.assertj.core.internal.bigdecimals;

import org.assertj.core.api.BDDAssertions;
import org.assertj.core.api.ThrowableAssertAlternative;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.assertj.core.util.AssertionsUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.lang.String.format;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

class BigDecimals_assertScaleIsEqualTo_Test extends BigDecimalsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertEqual(someInfo(), null, ONE_WITH_3_DECIMALS.scale()))
      .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_scales_are_equal() {
    numbers.assertEqual(someInfo(), ONE_WITH_3_DECIMALS.scale(), ONE_WITH_3_DECIMALS.scale());
  }

  @Test
  void should_fail_if_scales_are_not_equal() {
    AssertionError assertionError = AssertionsUtil.expectAssertionError(() -> assertThat(ONE_WITH_3_DECIMALS).isScaleEqualTo(2));

    then(assertionError).hasMessage(format("%nexpected: 2%n but was: 3"));
  }

}
