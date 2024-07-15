package org.assertj.core.api;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInIntegerFormat.shouldBeInIntegerFormat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
public class BigDecimalAssert_isInIntegerFormat_Test {
  @Test
  void should_pass_if_actual_is_in_integer_format() {
    // given
    BigDecimal bigDecimal = new BigDecimal("12");
    // when/then
    assertThat(bigDecimal).isInIntegerFormat();
  }

  @Test
  void should_fail_if_actual_is_not_in_integer_format() {
    // given
    BigDecimal bigDecimal = new BigDecimal("653.401");
    // when
    AssertionError assertionError = expectAssertionError(() -> assertThat(bigDecimal).isInIntegerFormat());
    // then
    then(assertionError).hasMessage(shouldBeInIntegerFormat(bigDecimal).create());
  }
}
