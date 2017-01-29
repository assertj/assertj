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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.OptionalDouble;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OptionalDoubleAssert_hasValue_Test extends BaseTest {

  @Test
  public void should_fail_when_optionalDouble_is_null() {
    thrown.expectAssertionError(actualIsNull());

    assertThat((OptionalDouble) null).hasValue(10.0);
  }

  @Test
  public void should_pass_if_optionalDouble_has_expected_value() {
    assertThat(OptionalDouble.of(10.0)).hasValue(10.0);
  }

  @Test
  public void should_fail_if_optionalDouble_does_not_have_expected_value() {
    OptionalDouble actual = OptionalDouble.of(5.0);
    double expectedValue = 10.0;

    thrown.expectAssertionError(shouldContain(actual, expectedValue).create());

    assertThat(actual).hasValue(expectedValue);
  }

  @Test
  public void should_fail_if_optionalDouble_is_empty() {
    double expectedValue = 10.0;

    thrown.expectAssertionError(shouldContain(expectedValue).create());

    assertThat(OptionalDouble.empty()).hasValue(expectedValue);
  }
}
