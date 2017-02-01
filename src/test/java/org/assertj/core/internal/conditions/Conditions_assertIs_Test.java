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

import static org.assertj.core.error.ShouldBe.shouldBe;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.ConditionsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Conditions#assertIs(AssertionInfo, Object, Condition)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Conditions_assertIs_Test extends ConditionsBaseTest {

  @Test
  public void should_throw_error_if_Condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    conditions.assertIs(someInfo(), actual, null);
  }

  @Test
  public void should_pass_if_Condition_is_met() {
    condition.shouldMatch(true);
    conditions.assertIs(someInfo(), actual, condition);
  }

  @Test
  public void should_fail_if_Condition_is_not_met() {
    condition.shouldMatch(false);
    AssertionInfo info = someInfo();
    try {
      conditions.assertIs(info, actual, condition);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBe(actual, condition));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
