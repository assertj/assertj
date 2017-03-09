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
package org.assertj.core.internal.maps;

import static org.assertj.core.error.ShouldContainEntry.shouldContainEntry;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Maps#assertHasEntrySatisfyingConditions(AssertionInfo, Map, Condition, Condition)}</code>.
 */
public class Maps_assertHasEntrySatisfying_with_key_and_value_conditions_Test extends MapsBaseTest {

  private Condition<String> isColor = new Condition<String>("is color condition") {
    @Override
    public boolean matches(String value) {
      return "color".equals(value);
    }
  };

  private Condition<String> isGreen = new Condition<String>("green color condition") {
    @Override
    public boolean matches(String value) {
      return "green".equals(value);
    }
  };

  private Condition<Object> isAge = new Condition<Object>() {
    @Override
    public boolean matches(Object value) {
      return "age".equals(value);
    }
  };

  private Condition<Object> isBlack = new Condition<Object>("greenColorCondition") {
    @Override
    public boolean matches(Object value) {
      return "black".equals(value);
    }
  };

  @Test
  public void should_fail_if_key_condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    maps.assertHasEntrySatisfyingConditions(someInfo(), actual, null, isGreen);
  }

  @Test
  public void should_fail_if_value_condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    maps.assertHasEntrySatisfyingConditions(someInfo(), actual, isColor, null);
  }


  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertHasEntrySatisfyingConditions(someInfo(), null, isColor, isGreen);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_entry_matching_key_condition() {
    AssertionInfo info = someInfo();
    try {
      maps.assertHasEntrySatisfyingConditions(info, actual, isAge, isGreen);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainEntry(actual, isAge, isGreen));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_entry_matching_value_condition() {
    AssertionInfo info = someInfo();
    try {
      maps.assertHasEntrySatisfyingConditions(info, actual, isColor, isBlack);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainEntry(actual, isColor, isBlack));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_entry_matching_both_key_and_value_conditions() {
    AssertionInfo info = someInfo();
    try {
      maps.assertHasEntrySatisfyingConditions(info, actual, isAge, isBlack);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainEntry(actual, isAge, isBlack));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_entry_matching_conditions() {
    maps.assertHasEntrySatisfyingConditions(someInfo(), actual, isColor, isGreen);
  }
}
