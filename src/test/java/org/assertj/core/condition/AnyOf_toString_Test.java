/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.condition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.condition.AnyOf.anyOf;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link AnyOf#toString()}</code>.
 * 
 * @author Yvonne Wang
 */
public class AnyOf_toString_Test {
  private TestCondition<Object> condition1;
  private TestCondition<Object> condition2;
  private Condition<Object> anyOf;

  @Before
  public void setUp() {
    condition1 = new TestCondition<>("Condition 1");
    condition2 = new TestCondition<>("Condition 2");
    anyOf = anyOf(condition1, condition2);
  }

  @Test
  public void should_implement_toString_showing_descriptions_of_inner_Conditions() {
    String expected = "any of:<[Condition 1, Condition 2]>";
    assertThat(anyOf).hasToString(expected);
  }
}
