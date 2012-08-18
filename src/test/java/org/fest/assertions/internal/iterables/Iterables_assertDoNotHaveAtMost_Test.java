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
package org.fest.assertions.internal.iterables;

import static org.fest.assertions.error.ElementsShouldNotHaveAtMost.elementsShouldNotHaveAtMost;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.util.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Collections.list;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.Condition;
import org.fest.assertions.internal.Iterables;
import org.fest.assertions.internal.IterablesWithConditionsBaseTest;

/**
 * Tests for <code>{@link Iterables#assertDoNotHaveAtMost(AssertionInfo, Iterable, Condition, int)}</code> .
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
public class Iterables_assertDoNotHaveAtMost_Test extends IterablesWithConditionsBaseTest {

  @Test
  public void should_pass_if_not_satisfies_at_most_times_condition() {
    actual = list("Yoda", "Solo", "Leia");
    iterables.assertDoNotHaveAtMost(someInfo(), actual, 2, jediPower);
    verify(conditions).assertIsNotNull(jediPower);
  }

  @Test
  public void should_pass_if_never_satisfies_condition_() {
    actual = list("Yoda", "Luke", "Obiwan");
    iterables.assertDoNotHaveAtMost(someInfo(), actual, 2, jediPower);
    verify(conditions).assertIsNotNull(jediPower);
  }

  @Test
  public void should_throw_error_if_condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    actual = list("Yoda", "Luke");
    iterables.assertDoNotHaveAtMost(someInfo(), actual, 2, null);
    verify(conditions).assertIsNotNull(null);
  }

  @Test
  public void should_fail_if_condition_is_not_met_much() {
    testCondition.shouldMatch(false);
    AssertionInfo info = someInfo();
    try {
      actual = list("Solo", "Leia", "Chewbacca");
      iterables.assertDoNotHaveAtMost(someInfo(), actual, 2, jediPower);
    } catch (AssertionError e) {
      verify(conditions).assertIsNotNull(jediPower);
      verify(failures).failure(info, elementsShouldNotHaveAtMost(actual, 2, jediPower));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
