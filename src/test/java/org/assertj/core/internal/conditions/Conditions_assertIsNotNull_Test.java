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
package org.assertj.core.internal.conditions;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.ConditionsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Conditions#assertIsNotNull(Condition)}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Conditions_assertIsNotNull_Test extends ConditionsBaseTest {

  @Test
  public void should_pass_if_condition_is_not_null() {
    conditions.assertIsNotNull(new TestCondition<String>());
  }

  @Test
  public void should_throw_error_if_Condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    conditions.assertIsNotNull(null);
  }

  @Test
  public void should_throw_error_with_the_given_alternative_error_message_if_Condition_is_null() {
    thrown.expectNullPointerException("alternative error message");
    conditions.assertIsNotNull(null, "%s error message", "alternative");
  }

}
