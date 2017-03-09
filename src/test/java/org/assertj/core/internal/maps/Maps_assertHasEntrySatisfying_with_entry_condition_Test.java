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
 * Tests for <code>{@link Maps#assertHasEntrySatisfying(AssertionInfo, Map, Condition)}</code>.
 */
public class Maps_assertHasEntrySatisfying_with_entry_condition_Test extends MapsBaseTest {

  private Condition<Map.Entry<String, String>> greenColorCondition =
      new Condition<Map.Entry<String, String>>("green color condition") {
      @Override
      public boolean matches(Map.Entry<String, String> entry) {
        return entry.getKey().equals("color") && entry.getValue().equals("green");
      }
    };

  private Condition<Map.Entry<String, String>> blackColorCondition =
    new Condition<Map.Entry<String, String>>("black color condition") {
      @Override
      public boolean matches(Map.Entry<String, String> entry) {
        return entry.getKey().equals("color") && entry.getValue().equals("black");
      }
    };

  @Test
  public void should_fail_if_entry_condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    maps.assertHasEntrySatisfying(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertHasEntrySatisfying(someInfo(), null, greenColorCondition);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_any_entry_matching_the_given_condition() {
    AssertionInfo info = someInfo();
    try {
      maps.assertHasEntrySatisfying(info, actual, blackColorCondition);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainEntry(actual, blackColorCondition));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_an_entry_matching_the_given_condition() {
    maps.assertHasEntrySatisfying(someInfo(), actual, greenColorCondition);
  }
}
