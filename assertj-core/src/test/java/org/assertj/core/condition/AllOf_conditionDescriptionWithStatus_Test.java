/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.condition;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.condition.AllOf.allOf;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllOf_conditionDescriptionWithStatus_Test {

  private TestCondition<Object> condition1;
  private TestCondition<Object> condition2;
  private Condition<Object> allOf;

  @BeforeEach
  public void setUp() {
    condition1 = new TestCondition<>();
    condition2 = new TestCondition<>();
    allOf = allOf(condition1, condition2);
  }

  @Test
  void should_return_description_with_all_succeeding_conditions() {
    // GIVEN
    condition1.shouldMatch(true);
    condition2.shouldMatch(true);
    // WHEN/THEN
    then(allOf.conditionDescriptionWithStatus("Yoda")).hasToString(format("[✓] all of:[%n" +
                                                                          "   [✓] TestCondition,%n" +
                                                                          "   [✓] TestCondition%n" +
                                                                          "]"));
  }

  @Test
  void should_return_description_with_failing_and_succeeding_conditions() {
    // GIVEN
    condition1.shouldMatch(true);
    condition2.shouldMatch(false);
    // WHEN/THEN
    then(allOf.conditionDescriptionWithStatus("Yoda")).hasToString(format("[✗] all of:[%n" +
                                                                          "   [✓] TestCondition,%n" +
                                                                          "   [✗] TestCondition%n" +
                                                                          "]"));
  }
}
