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
package org.assertj.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;

import org.assertj.core.api.BaseTest;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class OptionalAssert_contains_Test extends BaseTest {

  @Test
  public void should_fail_when_optional_is_null() {
    // GIVEN
    Optional<String> nullActual = null;
    // THEN
    assertThatAssertionErrorIsThrownBy(() -> assertThat(nullActual).contains("something")).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_expected_value_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(Optional.of("something")).contains(null))
                                        .withMessage("The expected value should not be <null>.");
  }

  @Test
  public void should_pass_if_optional_contains_expected_value() {
    assertThat(Optional.of("something")).contains("something");
  }

  @Test
  public void should_fail_if_optional_does_not_contain_expected_value() {
    // GIVEN
    Optional<String> actual = Optional.of("not-expected");
    String expectedValue = "something";
    // WHEN
    AssertionFailedError error = catchThrowableOfType(() -> assertThat(actual).contains(expectedValue),
                                                      AssertionFailedError.class);
    // THEN
    assertThat(error).hasMessage(shouldContain(actual, expectedValue).create());
    assertThat(error.getActual().getStringRepresentation()).isEqualTo(actual.get());
    assertThat(error.getExpected().getStringRepresentation()).isEqualTo(expectedValue);
  }

  @Test
  public void should_fail_if_optional_is_empty() {
    // GIVEN
    String expectedValue = "something";
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(Optional.empty()).contains(expectedValue));
    // THEN
    assertThat(error).hasMessage(shouldContain(expectedValue).create());
  }
}
