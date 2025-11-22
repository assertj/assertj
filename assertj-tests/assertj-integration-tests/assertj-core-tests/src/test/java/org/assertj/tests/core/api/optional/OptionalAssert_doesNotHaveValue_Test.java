/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class OptionalAssert_doesNotHaveValue_Test {

  @Test
  void should_fail_when_optional_is_null() {
    // GIVEN
    @SuppressWarnings("OptionalAssignedToNull")
    Optional<String> nullActual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(nullActual).doesNotHaveValue("something"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_value_is_null() {
    // GIVEN
    Optional<?> actual = Optional.of("something");

    // WHEN
    var thrown = catchThrowable(() -> assertThat(actual).doesNotHaveValue(null));
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(shouldNotBeNull("value").create());
  }

  @Test
  void should_fail_if_optional_contains_value() {
    // GIVEN
    Optional<String> actual = Optional.of("something");

    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotHaveValue("something"));

    // THEN
    assertThat(assertionError).hasMessage(shouldNotContainValue("something", "something").create());
  }

  @Test
  void should_pass_if_optional_does_not_contain_value() {
    // GIVEN
    Optional<String> actual = Optional.of("something");

    // THEN
    assertThatCode(() -> assertThat(actual).doesNotHaveValue("something else")).doesNotThrowAnyException();
  }

}
