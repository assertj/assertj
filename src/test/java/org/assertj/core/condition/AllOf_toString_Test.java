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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.condition;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.condition.AllOf.allOf;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AllOf#toString()}</code>.
 *
 * @author Yvonne Wang
 */
public class AllOf_toString_Test {

  @Test
  public void should_implement_toString_showing_descriptions_of_inner_Conditions() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("Condition 1");
    TestCondition<Object> condition2 = new TestCondition<>("Condition 2");
    Condition<Object> allOf = allOf(condition1, condition2);
    // THEN
    assertThat(allOf).hasToString(format("all of:[%n" +
                                         "   Condition 1,%n" +
                                         "   Condition 2%n" +
                                         "]"));
  }
}
