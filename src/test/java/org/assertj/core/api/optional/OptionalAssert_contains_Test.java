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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;

import org.assertj.core.api.BaseTest;
import org.junit.Test;

public class OptionalAssert_contains_Test extends BaseTest {

  @Test
  public void should_fail_when_optional_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((Optional<String>) null).contains("something"))
                                                   .withMessage(actualIsNull());
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
	Optional<String> actual = Optional.of("not-expected");
	String expectedValue = "something";

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).contains(expectedValue))
                                                   .withMessage(shouldContain(actual, expectedValue).create());
  }

  @Test
  public void should_fail_if_optional_is_empty() {
	String expectedValue = "something";

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(Optional.empty()).contains(expectedValue))
                                                   .withMessage(shouldContain(expectedValue).create());
  }
}
