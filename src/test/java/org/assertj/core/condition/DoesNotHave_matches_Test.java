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
import static org.assertj.core.condition.DoesNotHave.doesNotHave;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Not#matches(Object)}</code>.
 * 
 * @author Nicolas François
 */
public class DoesNotHave_matches_Test {

  private TestCondition<Object> condition;
  private Condition<Object> doesNotHave;

  @BeforeEach
  public void setUp() {
    condition = new TestCondition<>();
    doesNotHave = doesNotHave(condition);
  }

  @Test
  public void should_match_if_Condition_not_match() {
    condition.shouldMatch(false);
    assertThat(doesNotHave.matches("Yoda")).isTrue();
  }

  @Test
  public void should_not_match_Conditions_match() {
    condition.shouldMatch(true);
    assertThat(doesNotHave.matches("Yoda")).isFalse();
  }

}
