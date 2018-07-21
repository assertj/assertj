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
 * Tests for <code>{@link DoesNotHave#toString()}</code>.
 * 
 * @author Nicolas François
 */
public class DoesNotHave_toString_Test {

  private TestCondition<Object> condition;
  private Condition<Object> doesNotHave;

  @BeforeEach
  public void setUp() {
    condition = new TestCondition<>("JediPower");
    doesNotHave = doesNotHave(condition);
  }

  @Test
  public void should_implement_toString_showing_descriptions_of_inner_Conditions() {
    String expected = "does not have :<JediPower>";
    assertThat(doesNotHave).hasToString(expected);
  }

}
