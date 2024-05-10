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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.condition;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.condition.AllOf.allOf;
import static org.assertj.core.condition.Not.not;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AllOf#toString()}</code>.
 *
 * @author Yvonne Wang
 */
class AllOf_toString_Test {

  @Test
  void should_implement_toString_showing_descriptions_of_inner_Conditions() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("Condition 1");
    TestCondition<Object> condition2 = new TestCondition<>("Condition 2");
    DynamicCondition<Object> condition3 = new DynamicCondition<>("Condition 3");
    Condition<Object> allOf = allOf(condition1, condition2, condition3);
    // THEN
    then(allOf).hasToString(format("all of:[%n" +
                                   "   Condition 1,%n" +
                                   "   Condition 2,%n" +
                                   "   Condition 3%n" +
                                   "]"));
    condition1.shouldMatch(true);
    condition2.shouldMatch(true);
    allOf.matches(true);

    then(allOf).hasToString(format("all of:[%n" +
                                   "   Condition 1,%n" +
                                   "   Condition 2,%n" +
                                   "   ChangedDescription%n" +
                                   "]"));
  }

  @Test
  void should_implement_toString_showing_descriptions_of_inner_Conditions_list() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("Condition 1");
    TestCondition<Object> condition2 = new TestCondition<>("Condition 2");
    DynamicCondition<Object> condition3 = new DynamicCondition<>("Condition 3");
    Condition<Object> allOf = allOf(list(condition1, condition2, condition3));
    // THEN
    then(allOf).hasToString(format("all of:[%n" +
                                   "   Condition 1,%n" +
                                   "   Condition 2,%n" +
                                   "   Condition 3%n" +
                                   "]"));
    // WHEN
    // evaluating the condition will change the DynamicCondition description
    condition1.shouldMatch(true);
    condition2.shouldMatch(true);
    allOf.matches(true);
    // THEN
    then(allOf).hasToString(format("all of:[%n" +
                                   "   Condition 1,%n" +
                                   "   Condition 2,%n" +
                                   "   ChangedDescription%n" +
                                   "]"));
  }

  @Test
  void should_implement_toString_showing_descriptions_of_inner_not_Condition() {
    // GIVEN
    Condition<Object> allNot = allOf(not(new TestCondition<>("AllNot")));
    // WHEN/THEN
    then(allNot).hasToString(format("all of:[%n" +
                                    "   not :<AllNot>%n" +
                                    "]"));
  }

  static class DynamicCondition<T> extends TestCondition<T> {
    public DynamicCondition(String description) {
      super(description);
    }

    @Override
    public boolean matches(T value) {
      describedAs("ChangedDescription");
      return true;
    }
  }
}
