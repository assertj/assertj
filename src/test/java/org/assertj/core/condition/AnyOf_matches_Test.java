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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.condition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.condition.AnyOf.anyOf;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AnyOf#matches(Object)}</code>.
 * 
 * @author Yvonne Wang
 */
public class AnyOf_matches_Test {

  private TestCondition<Object> condition1;
  private TestCondition<Object> condition2;
  private Condition<Object> anyOf;

  @BeforeEach
  public void setUp() {
    condition1 = new TestCondition<>();
    condition2 = new TestCondition<>();
    anyOf = anyOf(condition1, condition2);
  }

  @Test
  public void should_match_if_at_least_one_Condition_matches() {
    condition1.shouldMatch(false);
    condition2.shouldMatch(true);
    assertThat(anyOf.matches("Yoda")).isTrue();
  }

  @Test
  public void should_not_match_if_none_of_the_Conditions_match() {
    condition1.shouldMatch(false);
    condition2.shouldMatch(false);
    assertThat(anyOf.matches("Yoda")).isFalse();
  }
}
