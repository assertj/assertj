/*
 * Created on Mar 5, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2012 the original author or authors.
 */
package org.fest.assertions.internal.conditions;

import org.junit.Test;

import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;
import org.fest.assertions.internal.Conditions;
import org.fest.assertions.internal.ConditionsBaseTest;

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

}
