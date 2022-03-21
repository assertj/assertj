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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.ShouldBe.shouldBe;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Optional;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.junit.jupiter.api.Test;

class OptionalAssert_hasValueSatisfying_Condition_Test {

  private Condition<String> passingCondition = new TestCondition<>(true);
  private Condition<String> notPassingCondition = new TestCondition<>();

  @Test
  void should_fail_when_optional_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((Optional<String>) null).hasValueSatisfying(passingCondition))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_when_optional_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(Optional.<String> empty()).hasValueSatisfying(passingCondition))
                                                   .withMessage(shouldBePresent(Optional.empty()).create());
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
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(Optional.of("something")).hasValueSatisfying(notPassingCondition))
                                                   .withMessage(shouldBe("something", notPassingCondition).create());
  }
}
