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

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.ShouldBe.shouldBe;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Optional;

import org.assertj.core.api.Condition;
import org.assertj.tests.core.testkit.TestCondition;
import org.junit.jupiter.api.Test;

class OptionalAssert_hasValueSatisfying_Condition_Test {

  private final Condition<String> passingCondition = new TestCondition<>(true);
  private final Condition<String> notPassingCondition = new TestCondition<>();

  @Test
  void should_fail_when_optional_is_null() {
    // GIVEN
    @SuppressWarnings("OptionalAssignedToNull")
    Optional<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasValueSatisfying(passingCondition));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_when_optional_is_empty() {
    // GIVEN
    Optional<String> actual = Optional.empty();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasValueSatisfying(passingCondition));
    // THEN
    then(assertionError).hasMessage(shouldBePresent(Optional.empty()).create());
  }

  @Test
  void should_fail_when_condition_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(Optional.of("something")).hasValueSatisfying((Condition<String>) null))
                                    .withMessage("The condition to evaluate should not be null");
  }

  @Test
  void should_pass_when_condition_is_met() {
    assertThat(Optional.of("something")).hasValueSatisfying(passingCondition);
  }

  @Test
  void should_fail_when_condition_is_not_met() {
    // GIVEN
    Optional<String> actual = Optional.of("something");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasValueSatisfying(notPassingCondition));
    // THEN
    then(assertionError).hasMessage(shouldBe("something", notPassingCondition).create());
  }
}
