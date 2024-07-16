package org.assertj.core.api;

import org.assertj.core.error.ShouldBeInFloatingPointFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInFloatingPointFormat.shouldBeInFloatingPointFormat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

public class BigDecimalAssert_isInFloatingPointFormat_Test {

  @ParameterizedTest
  @ValueSource(strings = { "653.401", "0.001", "1.23E3", "4.56e-2" })
  void should_pass_if_actual_is_in_floating_point_format(String value) {
    // given
    BigDecimal bigDecimal = new BigDecimal(value);
    // when/then
    BDDAssertions.then(bigDecimal).isInFloatingPointFormat();
  }

  @ParameterizedTest
  @ValueSource(strings = { "12", "100", "42", "0" })
  void should_fail_if_actual_is_not_in_floating_point_format(String value) {
    // given
    BigDecimal bigDecimal = new BigDecimal(value);
    // when
    ThrowableAssert.ThrowingCallable throwingCallable = () -> BDDAssertions.then(bigDecimal).isInFloatingPointFormat();
    // then
    assertThatThrownBy(throwingCallable)
                                        .isInstanceOf(AssertionError.class)
                                        .hasMessage(ShouldBeInFloatingPointFormat.shouldBeInFloatingPointFormat(bigDecimal)
                                                                                 .create());
  }
}
