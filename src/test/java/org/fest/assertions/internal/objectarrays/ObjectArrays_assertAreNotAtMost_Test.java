/*
 * Created on Mar 17, 2012
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
package org.fest.assertions.internal.objectarrays;

import static org.fest.assertions.error.ElementsShouldNotBeAtMost.elementsShouldNotBeAtMost;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.Condition;
import org.fest.assertions.internal.ObjectArrays;
import org.fest.assertions.internal.ObjectArraysWithConditionBaseTest;

/**
 * Tests for <code>{@link ObjectArrays#assertAreNotAtMost(AssertionInfo, Object[], Condition, int)}</code> .
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
public class ObjectArrays_assertAreNotAtMost_Test extends ObjectArraysWithConditionBaseTest {

  @Test
  public void should_pass_if__not_satisfies_at_least_times_condition() {
    actual = array("Yoda", "Solo", "Leia");
    arrays.assertAreNotAtMost(someInfo(), actual, 2, jedi);
    verify(conditions).assertIsNotNull(jedi);
  }

  @Test
  public void should_pass_if_never_satisfies_condition_() {
    actual = array("Yoda", "Luke", "Obiwan");
    arrays.assertAreNotAtMost(someInfo(), actual, 2, jedi);
    verify(conditions).assertIsNotNull(jedi);
  }

  @Test
  public void should_throw_error_if_condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    actual = array("Yoda", "Luke");
    arrays.assertAreNotAtMost(someInfo(), actual, 2, null);
    verify(conditions).assertIsNotNull(null);
  }

  @Test
  public void should_fail_if_condition_is_not_met_enought() {
    testCondition.shouldMatch(false);
    AssertionInfo info = someInfo();
    try {
      actual = array("Solo", "Leia", "Chewbacca");
      arrays.assertAreNotAtMost(someInfo(), actual, 2, jedi);
    } catch (AssertionError e) {
      verify(conditions).assertIsNotNull(jedi);
      verify(failures).failure(info, elementsShouldNotBeAtMost(actual, 2, jedi));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
