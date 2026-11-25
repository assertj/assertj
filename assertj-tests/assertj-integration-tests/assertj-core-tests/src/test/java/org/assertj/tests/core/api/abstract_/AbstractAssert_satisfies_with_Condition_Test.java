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

import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfy;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfyAll;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.Condition;
import org.assertj.tests.core.testkit.TestCondition;
import org.junit.jupiter.api.Test;

class AbstractAssert_satisfies_with_Condition_Test {

  private static final String ACTUAL = "Yoda";

  @Test
  void should_throw_error_if_condition_is_null() {
    // GIVEN
    Condition<Object> condition = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(ACTUAL).satisfies(condition));
    // THEN
    then(npe).hasMessage("The condition to evaluate should not be null");
  }

  @Test
  void should_pass_if_condition_is_met() {
    // GIVEN
    Condition<Object> condition = new TestCondition<>(true);
    // WHEN/THEN
    then(ACTUAL).satisfies(condition);
  }

  @Test
  void should_fail_if_condition_is_not_met() {
    // GIVEN
    Condition<Object> condition = new TestCondition<>(false);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).satisfies(condition));
    // THEN
    then(assertionError).hasMessage(shouldSatisfy(ACTUAL, condition).create());
  }

  @Test
  void should_fail_with_the_status_of_each_condition_if_allOf_condition_is_not_met() {
    // GIVEN
    Condition<String> startsWithY = new Condition<>(value -> value.startsWith("Y"), "starts with Y");
    Condition<String> endsWithZ = new Condition<>(value -> value.endsWith("Z"), "ends with Z");
    Condition<String> condition = allOf(startsWithY, endsWithZ);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).satisfies(condition));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyAll(ACTUAL, condition.conditionDescriptionWithStatus(ACTUAL)).create());
  }

  @Test
  void should_pass_if_allOf_condition_is_met() {
    // GIVEN
    Condition<String> condition = allOf(new Condition<>(value -> value.startsWith("Y"), "starts with Y"),
                                        new Condition<>(value -> value.endsWith("a"), "ends with a"));
    // WHEN/THEN
    then(ACTUAL).satisfies(condition);
  }

}
