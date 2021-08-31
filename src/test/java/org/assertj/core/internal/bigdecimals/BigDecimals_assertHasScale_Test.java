package org.assertj.core.internal.bigdecimals;

import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.lang.String.format;
import static java.math.BigDecimal.ONE;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveScale.shouldHaveScale;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

class BigDecimals_assertHasScale_Test extends BigDecimalsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    //GIVEN
    BigDecimal nullBigDecimal = null;

    //WHEN/THEN
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertHasScale(someInfo(), nullBigDecimal, 0))
      .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_scales_are_equal() {
    // GIVEN
    int EXPECTED_SCALE = ONE_WITH_3_DECIMALS.scale();

    // WHEN/THEN
    numbers.assertHasScale(someInfo(), ONE_WITH_3_DECIMALS, EXPECTED_SCALE);
  }

  @Test
  void should_pass_if_scales_are_equal_whatever_custom_strategy_is_used() {
    // GIVEN
    int EXPECTED_SCALE = ONE_WITH_3_DECIMALS.scale();

    // WHEN/THEN
    numbersWithAbsValueComparisonStrategy.assertHasScale(someInfo(), ONE_WITH_3_DECIMALS, EXPECTED_SCALE);
  }

  @Test
  void should_fail_if_scales_are_not_equal() {
    // GIVEN
    int EXPECTED_SCALE = 3;
    BigDecimal WITHOUT_DECIMAL = ONE;

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> numbers.assertHasScale(someInfo(), WITHOUT_DECIMAL, EXPECTED_SCALE));

    // THEN
    then(assertionError).hasMessage(format("%nexpecting actual 1 to have a scale of:%n  3%nbut had a scale of:%n  0"));
    verify(failures).failure(someInfo(), shouldHaveScale(WITHOUT_DECIMAL, EXPECTED_SCALE));
  }

  @Test
  void should_fail_if_scales_are_not_equal_whatever_custom_strategy_is_used() {
    // GIVEN
    int EXPECTED_SCALE = 3;
    BigDecimal WITHOUT_DECIMAL = ONE;

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> numbers.assertHasScale(someInfo(), WITHOUT_DECIMAL, EXPECTED_SCALE));

    // THEN
    then(assertionError).hasMessage(format("%nexpecting actual 1 to have a scale of:%n  3%nbut had a scale of:%n  0"));
    verify(failures).failure(someInfo(), shouldHaveScale(WITHOUT_DECIMAL, EXPECTED_SCALE));
  }

}
