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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.OptionalShouldContain.shouldContainSame;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class OptionalAssert_containsSame_Test {

  @Test
  void should_fail_when_actual_is_null() {
    // GIVEN
    @SuppressWarnings("OptionalAssignedToNull")
    Optional<Object> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSame("something"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_value_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(Optional.of("something")).containsSame(null))
                                        .withMessage("The expected value should not be <null>.");
  }

  @Test
  void should_pass_if_optional_contains_the_expected_object_reference() {
    assertThat(Optional.of("something")).containsSame("something");
  }

  @Test
  void should_fail_if_optional_does_not_contain_the_expected_object_reference() {
    // GIVEN
    Optional<String> actual = Optional.of("not-expected");
    String expectedValue = "something";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSame(expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldContainSame(actual, expectedValue).create());
  }

  @Test
  void should_fail_if_optional_contains_equal_but_not_same_value() {
    // GIVEN
    Optional<String> actual = Optional.of(new String("something"));
    String expectedValue = "something";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSame(expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldContainSame(actual, expectedValue).create());
  }

  @Test
  void should_fail_if_optional_is_empty() {
    // GIVEN
    String expectedValue = "something";
    // WHEN
    Optional<Object> actual = Optional.empty();
    var assertionError = expectAssertionError(() -> assertThat(actual).containsSame(expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldContainSame(actual, expectedValue).create());
  }
}
