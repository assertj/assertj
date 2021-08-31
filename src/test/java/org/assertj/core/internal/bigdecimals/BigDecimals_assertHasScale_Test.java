package org.assertj.core.internal.bigdecimals;

import org.assertj.core.internal.BigDecimalsBaseTest;
import org.assertj.core.util.AssertionsUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.lang.String.format;
import static java.math.BigDecimal.ONE;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

class BigDecimals_assertHasScale_Test extends BigDecimalsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    BigDecimal nullBigDecimal = null;

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertHasScale(someInfo(), nullBigDecimal, 0))
      .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_scales_are_equal() {
    numbers.assertHasScale(someInfo(), ONE_WITH_3_DECIMALS, 3);
  }

  @Test
  void should_pass_if_scales_are_equal_whatever_custom_strategy_is_used() {
    numbersWithAbsValueComparisonStrategy.assertHasScale(someInfo(), ONE_WITH_3_DECIMALS, 3);
  }

  @Test
  void should_fail_if_scales_are_not_equal() {
    AssertionError assertionError = AssertionsUtil.expectAssertionError(() -> numbers.assertHasScale(someInfo(), ONE, 3));

    then(assertionError).hasMessage(format("%nexpecting actual to have a scale of: 3%n  but has a scale of: 0"));
  }

  @Test
  void should_fail_if_scales_are_not_equal_whatever_custom_strategy_is_used() {
    AssertionError assertionError = AssertionsUtil.expectAssertionError(() -> numbers.assertHasScale(someInfo(), ONE, 3));

    then(assertionError).hasMessage(format("%nexpecting actual to have a scale of: 3%n  but has a scale of: 0"));
  }

}
