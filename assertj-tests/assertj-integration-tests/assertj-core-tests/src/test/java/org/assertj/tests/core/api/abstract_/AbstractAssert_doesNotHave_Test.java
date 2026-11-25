/*
 * Copyright 2012-2026 the original author or authors.
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
package org.assertj.tests.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotHave.shouldNotHave;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.Condition;
import org.assertj.tests.core.testkit.TestCondition;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Yvonne Wang
/// @author Joel Costigliola
class AbstractAssert_doesNotHave_Test {

  private static final Object ACTUAL = "Yoda";

  @Test
  void should_throw_error_if_condition_is_null() {
    // GIVEN
    Condition<Object> condition = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(ACTUAL).doesNotHave(condition));
    // THEN
    then(npe).hasMessage("The condition to evaluate should not be null");
  }

  @Test
  void should_pass_if_condition_is_not_met() {
    // GIVEN
    Condition<Object> condition = new TestCondition<>(false);
    // WHEN/THEN
    then(ACTUAL).doesNotHave(condition);
  }

  @Test
  void should_fail_if_condition_is_met() {
    // GIVEN
    Condition<Object> condition = new TestCondition<>(true);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotHave(condition));
    // THEN
    then(assertionError).hasMessage(shouldNotHave(ACTUAL, condition).create());
  }

  @Test
  void should_evaluate_condition_against_null_actual() {
    // GIVEN
    Object actual = null;
    Condition<Object> condition = new Condition<>(value -> value != null, "not null");
    // WHEN/THEN
    then(actual).doesNotHave(condition);
  }

}
