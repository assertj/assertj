/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.optionaldouble;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.error.OptionalDoubleShouldHaveValueCloseTo.shouldHaveValueCloseTo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.OptionalDouble;

import org.assertj.core.api.BaseTest;
import org.assertj.core.data.Offset;
import org.junit.Test;

public class OptionalDoubleAssert_hasValueCloseTo_Test extends BaseTest {

  @Test
  public void should_fail_when_optionaldouble_is_null() {
    thrown.expectAssertionError(actualIsNull());

    assertThat((OptionalDouble) null).hasValueCloseTo(10.0, within(2.0));
  }

  @Test
  public void should_fail_if_optionaldouble_is_empty() {
    double expectedValue = 10.0;

    thrown.expectAssertionError(shouldHaveValueCloseTo(expectedValue).create());

    assertThat(OptionalDouble.empty()).hasValueCloseTo(expectedValue, within(2.0));
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    double expectedValue = 10.0;
    Offset<Double> offset = within(1.0);
    OptionalDouble actual = OptionalDouble.of(1.0);

    thrown.expectAssertionError(
          shouldHaveValueCloseTo(actual, expectedValue, offset, abs(expectedValue - actual.getAsDouble()
                                 )).create());

    assertThat(actual).hasValueCloseTo(expectedValue, offset);
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    double expectedValue = 10.0;
    assertThat(OptionalDouble.of(10.0)).hasValueCloseTo(expectedValue, null);
  }

  @Test
  public void should_pass_if_optionaldouble_has_expected_value_close_to() {
    assertThat(OptionalDouble.of(10.0)).hasValueCloseTo(10.0, within(2.0));
  }

  @Test
  public void should_pass_if_optionaldouble_has_expected_value_with_less_than_given_offset() {
    assertThat(OptionalDouble.of(1.0)).hasValueCloseTo(1.0, within(1.0));
    assertThat(OptionalDouble.of(1.0)).hasValueCloseTo(2.0, within(10.0));
  }

  @Test
  public void should_pass_if_optionaldouble_has_expected_value_equal_given_offset() {
    assertThat(OptionalDouble.of(1.0)).hasValueCloseTo(1.0, within(0.0));
    assertThat(OptionalDouble.of(1.0)).hasValueCloseTo(0.0, within(1.0));
    assertThat(OptionalDouble.of(1.0)).hasValueCloseTo(2.0, within(1.0));
  }
}
