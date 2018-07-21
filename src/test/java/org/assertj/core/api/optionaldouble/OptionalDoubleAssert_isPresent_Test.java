/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.optionaldouble;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.OptionalDouble;

import org.assertj.core.api.BaseTest;
import org.junit.jupiter.api.Test;

public class OptionalDoubleAssert_isPresent_Test extends BaseTest {

  @Test
  public void should_pass_when_optionaldouble_is_present() {
    assertThat(OptionalDouble.of(10.0)).isPresent();
  }

  @Test
  public void should_fail_when_optionaldouble_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(OptionalDouble.empty()).isPresent())
                                                   .withMessage(shouldBePresent(OptionalDouble.empty()).create());
  }

  @Test
  public void should_fail_when_optionaldouble_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((OptionalDouble) null).isPresent())
                                                   .withMessage(actualIsNull());
  }
}
