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

import static org.assertj.core.error.ShouldContainKey.shouldContainKey;
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
 * Tests for <code>{@link Maps#assertHasKeySatisfying(AssertionInfo, Map, Condition)}</code>.
 */
public class Maps_assertHasKeySatisfying_Test extends MapsBaseTest {

  private Condition<String> isColor = new Condition<String>("is color condition") {
    @Override
    public boolean matches(String value) {
      return "color".equals(value);
    }
  };

  private Condition<Object> isAge = new Condition<Object>() {
    @Override
    public boolean matches(Object value) {
      return "age".equals(value);
    }
  };

  @Test
  public void should_fail_if_condition_is_null() {
    thrown.expectNullPointerException("The condition to evaluate should not be null");
    maps.assertHasKeySatisfying(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertHasKeySatisfying(someInfo(), null, isColor);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_any_key_matching_the_given_condition() {
    AssertionInfo info = someInfo();
    try {
      maps.assertHasKeySatisfying(info, actual, isAge);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainKey(actual, isAge));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_a_key_matching_the_given_condition() {
    maps.assertHasKeySatisfying(someInfo(), actual, isColor);
  }
}
