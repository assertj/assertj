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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.condition;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.condition.AnyOf.anyOf;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AnyOf#toString()}</code>.
 *
 * @author Yvonne Wang
 */
class AnyOf_toString_Test {

  @Test
  void should_implement_toString_showing_descriptions_of_inner_Conditions() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("Condition 1");
    TestCondition<Object> condition2 = new TestCondition<>("Condition 2");
    Condition<Object> anyOf = anyOf(condition1, condition2);
    // THEN
    then(anyOf).hasToString(format("any of:[%n" +
                                   "   Condition 1,%n" +
                                   "   Condition 2%n" +
                                   "]"));
  }

  @Test
  void should_implement_toString_showing_descriptions_of_inner_Conditions_list() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("Condition 1");
    TestCondition<Object> condition2 = new TestCondition<>("Condition 2");
    Condition<Object> anyOf = anyOf(list(condition1, condition2));
    // THEN
    then(anyOf).hasToString(format("any of:[%n" +
                                   "   Condition 1,%n" +
                                   "   Condition 2%n" +
                                   "]"));
  }
}
