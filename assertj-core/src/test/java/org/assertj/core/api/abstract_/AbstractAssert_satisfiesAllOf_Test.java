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
package org.assertj.core.api.abstract_;

import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.condition.AllOf.allOf;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.Objects;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AbstractAssert_satisfiesAllOf_Test {

  @Test
  void should_pass_when_all_of_the_condition_is_met() {
    // GIVEN
    Condition<String> conditionA = new Condition<>(Objects::nonNull, "Input not null");
    Condition<String> conditionB = new Condition<>(text -> text.equals("ABC"), "Input equals ABC");
    // WHEN/THEN
    then("ABC").satisfies(allOf(conditionA, conditionB));
  }

  @Test
  void should_fail_if_one_of_the_condition_is_not_met() {
    // GIVEN
    Condition<String> condition1 = new Condition<>(Objects::nonNull, "not null");
    Condition<String> condition2 = new Condition<>(text -> text.equals("abc"), "equals abc");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("def").satisfies(allOf(condition1, condition2)));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n" +
                                           "  \"def\"%n" +
                                           "to satisfy:%n" +
                                           "  [✗] all of:[%n" +
                                           "   [✓] not null,%n" +
                                           "   [✗] equals abc%n" +
                                           "]"));
  }

  @Test
  void should_fail_if_all_the_conditions_are_not_met() {
    // GIVEN
    Condition<String> condition1 = new Condition<>(text -> text.length() > 10, "has more that 10 characters");
    Condition<String> condition2 = new Condition<>(text -> text.equals("abc"), "not equals abc");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("def").satisfies(allOf(condition1, condition2)));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting actual:%n" +
                                           "  \"def\"%n" +
                                           "to satisfy:%n" +
                                           "  [✗] all of:[%n" +
                                           "   [✗] has more that 10 characters,%n" +
                                           "   [✗] not equals abc%n" +
                                           "]"));
  }
}
