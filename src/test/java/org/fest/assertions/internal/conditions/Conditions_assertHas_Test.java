/*
 * Created on Jan 30, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal.conditions;

import static org.fest.assertions.error.ShouldHave.shouldHave;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.Condition;
import org.fest.assertions.internal.Conditions;
import org.fest.assertions.internal.ConditionsBaseTest;

/**
 * Tests for <code>{@link Conditions#assertHas(AssertionInfo, Object, Condition)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Conditions_assertHas_Test extends ConditionsBaseTest {

  @Test
  public void should_throw_error_if_Condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    conditions.assertHas(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_Condition_is_met() {
    condition.shouldMatch(true);
    conditions.assertHas(someInfo(), actual, condition);
  }

  @Test
  public void should_fail_if_Condition_is_not_met() {
    condition.shouldMatch(false);
    AssertionInfo info = someInfo();
    try {
      conditions.assertHas(info, actual, condition);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHave(actual, condition));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
